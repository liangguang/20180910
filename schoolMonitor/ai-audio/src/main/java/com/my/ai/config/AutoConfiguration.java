package com.my.ai.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
		"com.my.ai.rs",
		"com.my.ai.service",
		"com.my.ai.schedule"
})
public class AutoConfiguration {

	
	
}
