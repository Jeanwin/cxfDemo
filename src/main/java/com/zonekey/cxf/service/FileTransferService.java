package com.zonekey.cxf.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.zonekey.cxf.bean.MyFile;
import com.zonekey.cxf.exception.FileTransferException;

@WebService
public interface FileTransferService {

	@WebMethod
	void uploadFile(MyFile myFile) throws FileTransferException;

	@WebMethod
	MyFile downloadFile(MyFile myFile) throws FileTransferException;
}