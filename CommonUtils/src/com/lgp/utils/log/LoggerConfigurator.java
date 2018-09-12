package com.lgp.utils.log;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LoggerConfigurator {
	public static void config(String logbackCfg) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(lc);
			// the context was probably already configured by default
			// configuration rules
			lc.reset();
			configurator.doConfigure(logbackCfg);
		} catch (JoranException je) {
			// StatusPrinter will handle this
		}
		StatusPrinter.printIfErrorsOccured(lc);
	}
}
