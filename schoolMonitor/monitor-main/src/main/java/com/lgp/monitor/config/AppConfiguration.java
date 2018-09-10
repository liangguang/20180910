package com.lgp.monitor.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
		"com.lgp.monitor.schedule",
		"com.lgp.monitor.service"
})
public class AppConfiguration {
	
}
