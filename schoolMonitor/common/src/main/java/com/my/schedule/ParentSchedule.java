package com.my.schedule;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ParentSchedule implements InitializingBean,DisposableBean{
	
	public static Logger logger = LoggerFactory.getLogger(ParentSchedule.class);
	
	public final static ObjectMapper objectMapper = new ObjectMapper();
	
	@Scheduled(
			initialDelayString = "${agent.task.initialDelay:1000}", //
			fixedDelayString = "${agent.task.fixedDelay:300000}")
	
	public void dowork(){
		execute();
	}
	//定时任务一
	public abstract void execute();

	@Scheduled(cron = "${agent.task.cron:0 0 10,14,16 * * ?}")
	public void timeTask(){
		executeTimeTask();
	}
	//定时任务三
	public abstract void executeTimeTask();
	
	//每天12点出发
	@Scheduled(cron = "0 0 12 * * ?")
	public void otherTask(){
		executeOtherTask();
	}
	//定时任务三
	public abstract void executeOtherTask();
}
