package com.zonekey.cxf.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.zonekey.cxf.bean.MyFile;
import com.zonekey.cxf.exception.FileTransferException;
import com.zonekey.cxf.service.FileTransferService;

public class FileTransferServiceImpl implements FileTransferService {  
 
    public void uploadFile(MyFile myFile) throws FileTransferException {  
        OutputStream os = null;  
          
        try {  
            if (myFile.getPosition() != 0) {  
                os = FileUtils.openOutputStream(new File(myFile.getServerFile()), true);  
            } else {  
                os = FileUtils.openOutputStream(new File(myFile.getServerFile()), false);  
            }  
            os.write(myFile.getBytes());  
        } catch(IOException e) {  
            throw new FileTransferException(e.getMessage(), e);  
        } finally {  
            IOUtils.closeQuietly(os);  
        }  
    }  
 
    public MyFile downloadFile(MyFile myFile) throws FileTransferException {  
        InputStream is = null;  
          
        try {  
            is = new FileInputStream(myFile.getServerFile());  
            is.skip(myFile.getPosition());  
            byte[] bytes = new byte[1024 * 1024];  
            int size = is.read(bytes);  
            if (size > 0) {  
                byte[] fixedBytes = Arrays.copyOfRange(bytes, 0, size);  
                myFile.setBytes(fixedBytes);  
            } else {  
                myFile.setBytes(new byte[0]);  
            }  
        } catch(IOException e) {  
            throw new FileTransferException(e.getMessage(), e);  
        } finally {  
            IOUtils.closeQuietly(is);  
        }  
        return myFile;  
    }  
} 