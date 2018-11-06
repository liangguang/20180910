package com.my.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
		"com.my.rs",
		"com.my.service",
		"com.my.schedule"
})
public class AutoConfiguration {

}
