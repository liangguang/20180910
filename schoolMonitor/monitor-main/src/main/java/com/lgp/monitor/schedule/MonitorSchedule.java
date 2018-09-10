package com.lgp.monitor.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;

public class MonitorSchedule  implements InitializingBean,DisposableBean{

	public static Logger logger = LoggerFactory.getLogger(MonitorSchedule.class);
	
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Scheduled(
			initialDelayString = "${monitor.task.initialDelay:5000}", //
			fixedDelayString = "${monitor.task.fixedDelay:30000}")
	public void execute() {
		
		// do something
		
	}


}
