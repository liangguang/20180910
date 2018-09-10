package com.lgp.monitor;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableAsync
@ServletComponentScan
public class Application {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(Application.class, args);
	}
	
	
}
