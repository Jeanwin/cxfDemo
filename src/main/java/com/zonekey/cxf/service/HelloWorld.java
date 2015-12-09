package com.zonekey.cxf.service;
import javax.jws.WebService;
 
@WebService
public interface HelloWorld {
    String sayHi(String text);
}