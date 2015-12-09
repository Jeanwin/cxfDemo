package com.zonekey.cxf.test;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.zonekey.cxf.service.HelloWorld;

public final class Client {  
  
    private Client() {  
    }  
  
	public static void main(String args[]) throws Exception {  
        // START SNIPPET: client  
        /*ClassPathXmlApplicationContext context   
            = new ClassPathXmlApplicationContext("spring-cxfClient.xml");  
  
        HelloWorld client = (HelloWorld)context.getBean("client");  
  
        String response = client.sayHi("Joe");  
        System.out.println("Response: " + response);  
        System.exit(0);*/  
        // END SNIPPET: client
    	JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();
        svr.setServiceClass(HelloWorld.class);
        svr.setAddress("http://localhost/cxfDemo/ws/HelloWorld");
        HelloWorld hw = (HelloWorld) svr.create();
        System.out.println(hw.sayHi("jeanwin"));
    }  
}  