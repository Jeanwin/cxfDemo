package com.zonekey.cxf.endpoint;

import javax.xml.ws.Endpoint;

import com.zonekey.cxf.service.impl.FileTransferServiceImpl;

public class FileTransferServer {

	public static void main(String[] args) throws Exception {
		Endpoint.publish("http://localhost:9000/ws/jaxws/fileTransferService", new FileTransferServiceImpl());
	}
}