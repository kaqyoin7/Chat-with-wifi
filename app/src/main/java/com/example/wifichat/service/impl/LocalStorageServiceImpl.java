package com.example.wifichat.service.impl;

import android.content.Context;

import com.example.wifichat.service.LocalStorageService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LocalStorageServiceImpl implements LocalStorageService {
    private static LocalStorageServiceImpl instance;
    private Context context;

    private LocalStorageServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    //单例模式
    public static synchronized LocalStorageServiceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new LocalStorageServiceImpl(context);
        }
        return instance;
    }

    @Override
    public void appendFileInternalStorage(String fileName, String content) {
        FileOutputStream fos = null;
        String writeMsg = content + "\n";
        try {
            fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            fos.write(writeMsg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void overwriteFileInternalStorage(String fileName, String content) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String readFileInternalStorage(String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) {
            return null;
        }

        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean deleteFileInternalStorage(String fileName) {
        return context.deleteFile(fileName);
    }

    @Override
    public String getFilesPath() {
        File internalStorageDir = context.getFilesDir();
        String path = internalStorageDir.getAbsolutePath();
        return null;
    }
}
