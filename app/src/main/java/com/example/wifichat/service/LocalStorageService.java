package com.example.wifichat.service;

public interface LocalStorageService {
    //读文件
    String readFileInternalStorage(String fileName);
    //删除文件
    boolean deleteFileInternalStorage(String fileName);
    //追加文件内容
    void appendFileInternalStorage(String fileName, String content);
    //覆写文件
    void overwriteFileInternalStorage(String fileName, String content);


}
