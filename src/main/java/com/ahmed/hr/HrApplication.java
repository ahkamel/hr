package com.ahmed.hr;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "com.ahmed" })
@SpringBootApplication
public class HrApplication {

	
	@PostConstruct
	void started() {
	    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}
}
