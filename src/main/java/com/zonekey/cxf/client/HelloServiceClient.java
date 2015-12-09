package com.zonekey.cxf.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zonekey.cxf.service.HelloWorld;

public class HelloServiceClient {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-client.xml");
		HelloWorld helloService = (HelloWorld) context.getBean("client");
		String response = helloService.sayHi("Peter");
		System.out.println(response);
	}

}