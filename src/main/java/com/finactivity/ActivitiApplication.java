package com.finactivity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:security.xml")
@EnableAutoConfiguration
public class ActivitiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivitiApplication.class, args);
	}
}
//Test Change
/*Event -> Start Event , End Event
Task -> User Task
Gateway -> Parallel Gateway, Exclusive Gateway
Sub Process

Easy Integeration
Jpa Query Supported
Acitiviti Task History
Multitenant


References:
https://www.activiti.org/userguide
https://spring.io/blog/2015/03/08/getting-started-with-activiti-and-spring-boot

https://github.com/Activiti/Activiti/releases/tag/activiti-5.21.0*/