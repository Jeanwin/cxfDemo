package com.zonekey.cxf.service.impl;

import javax.jws.WebService;

import com.zonekey.cxf.service.HelloWorld;

@WebService(endpointInterface = "com.zonekey.cxf.service.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

	public String sayHi(String text) {
		System.out.println("sayHi called");
		return "Hello " + text;
	}
}