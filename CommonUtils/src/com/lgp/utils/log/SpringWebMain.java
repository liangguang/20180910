package com.lgp.utils.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoaderListener;

public class SpringWebMain extends ContextLoaderListener {

	protected static final String INIT_PARAM_HOMEDIR = "homeDirectory";

	protected static final String INIT_PARAM_LOGCONFIGFILE = "logConfigFile";

	protected static final String INIT_PARAM_PROPERTIESFILES = "propertiesFiles";

	protected static final String SYS_PPT_APPHOME = "app.home";
	
	protected static final Logger logger = LoggerFactory
			.getLogger(SpringWebMain.class);
	
	public static <T> T getBean(Class<T> requiredType) {
		ApplicationContext ac = ContextLoaderListener
				.getCurrentWebApplicationContext();
		if (ac != null) {
			return ac.getBean(requiredType);
		} else {
			throw new org.springframework.context.ApplicationContextException(
					"ApplicationContext has not been set.");
		}
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		ApplicationContext ac = ContextLoaderListener
				.getCurrentWebApplicationContext();
		if (ac != null) {
			return ac.getBean(name, requiredType);
		} else {
			throw new org.springframework.context.ApplicationContextException(
					"ApplicationContext has not been set.");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		org.slf4j.bridge.SLF4JBridgeHandler.uninstall();
		super.contextDestroyed(event);
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		initialize(sc);
		super.contextInitialized(event);
	}

	protected String getLogbackConfigFileName() {
		return null;
	}

	protected String[] getPropertiesFileNames() {
		return new String[] {};
	}

	private String getLogbackConfigFileName(ServletContext servletContext) {
		String cfn = servletContext.getInitParameter(INIT_PARAM_LOGCONFIGFILE);
		if (StringUtils.hasLength(cfn)) {
			return cfn;
		} else {
			return this.getLogbackConfigFileName();
		}
	}

	private String[] getPropertiesFileNames(ServletContext servletContext) {
		String ppfs = servletContext
				.getInitParameter(INIT_PARAM_PROPERTIESFILES);
		if (StringUtils.hasLength(ppfs)) {
			return ppfs.split(";");
		} else {
			return this.getPropertiesFileNames();
		}
	}

	protected void initialize(ServletContext servletContext) {
		String appname = servletContext.getServletContextName();
		String homedir_ppt_name = appname + ".home";
		String homedir = servletContext.getInitParameter(INIT_PARAM_HOMEDIR);
		if (!StringUtils.hasText(homedir)) {
			homedir = System.getProperty(homedir_ppt_name);
		}
		if (!StringUtils.hasText(homedir)) {
			homedir = System.getenv(appname + "_home");
		}
		if (!StringUtils.hasText(homedir)) {
			homedir = ".";
			logger.warn("未找到" + homedir_ppt_name + "，请正确配置，否则系统将无法正常运行！");
			logger.warn("暂时将" + homedir_ppt_name + "的值设置为当前目录！");
		}
		// 强制加入System，以便logback等其他配置使用
		System.setProperty(homedir_ppt_name, homedir);
		System.setProperty(SYS_PPT_APPHOME, homedir);

		// 日志处理
		System.setProperty("org.apache.cxf.Logger",
				"org.apache.cxf.common.logging.Slf4jLogger");
		if (!org.slf4j.bridge.SLF4JBridgeHandler.isInstalled()) {
			org.slf4j.bridge.SLF4JBridgeHandler.install();
		}
		File logcf = new File(homedir, getLogbackConfigFileName(servletContext));
		logger.info("加载日志配置文件：{}", logcf.getAbsolutePath());
		LoggerConfigurator.config(logcf.getAbsolutePath());

		// 加载配置文件
		for (String str : getPropertiesFileNames(servletContext)) {
			if (StringUtils.hasLength(str)) {
				loadProperties(homedir + "/" + str);
			}
		}
	}

	private void loadProperties(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			logger.warn("未找到文件：" + filename + "，请正确配置，否则系统将无法正常运行！");
			return;
		}
		logger.info("加载配置文件：{}", file.getAbsolutePath());
		Properties ppts = new Properties();
		try {
			ppts.load(new FileReader(file));
		} catch (FileNotFoundException e) {
			logger.error("未找到文件：" + filename + "，请正确配置，否则系统将无法正常运行！");
		} catch (IOException e) {
			logger.error("加载文件：" + filename + "，请正确配置，否则系统将无法正常运行！");
		}
		// 插入到System.Properties
		for (Enumeration<?> e = ppts.propertyNames(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			System.setProperty(key, ppts.getProperty(key));
		}
	}
	
}
