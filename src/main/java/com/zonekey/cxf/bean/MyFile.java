package com.zonekey.cxf.bean;
public class MyFile {  
      
    private String clientFile;  
      
    private String serverFile;  
      
    private long position;  
      
    private byte[] bytes;  
 
    public String getClientFile() {  
        return clientFile;  
    }  
 
    public void setClientFile(String clientFile) {  
        this.clientFile = clientFile;  
    }  
 
    public String getServerFile() {  
        return serverFile;  
    }  
 
    public void setServerFile(String serverFile) {  
        this.serverFile = serverFile;  
    }  
 
    public long getPosition() {  
        return position;  
    }  
 
    public void setPosition(long position) {  
        this.position = position;  
    }  
 
    public byte[] getBytes() {  
        return bytes;  
    }  
 
    public void setBytes(byte[] bytes) {  
        this.bytes = bytes;  
    }  
}  