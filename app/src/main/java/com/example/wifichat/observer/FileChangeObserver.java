package com.example.wifichat.observer;

import android.os.FileObserver;
import android.widget.TextView;

import com.example.wifichat.api.ChatRecordsApi;
import com.example.wifichat.service.LocalStorageService;
import com.example.wifichat.service.impl.LocalStorageServiceImpl;
import com.example.wifichat.util.ContextHolderUtil;

import java.util.List;

public class FileChangeObserver extends FileObserver {
    private String filePath;
    private TextView textView;
    private String friendId;
    private static ChatRecordsApi chatRecordsApi = new ChatRecordsApi(ContextHolderUtil.getContext());
    private static LocalStorageService localStorageService = LocalStorageServiceImpl.getInstance(ContextHolderUtil.getContext());

    public FileChangeObserver(String fileName, TextView textView, String friendId) {
        // Watch for modifications to the file.
        super(fileName, FileObserver.MODIFY | FileObserver.CREATE | FileObserver.DELETE);
        this.filePath = fileName + "/" + localStorageService.getFilesPath();
        this.textView = textView;
        this.friendId = friendId;
    }

    @Override
    public void onEvent(int event, String path) {
        if (path != null) {
            switch (event) {
                case FileObserver.MODIFY:
                    onFileChanged(filePath);
                    break;
                case FileObserver.CREATE:
                    onFileCreated(filePath);
                    break;
                case FileObserver.DELETE:
                    onFileDeleted(filePath);
                    break;
            }
        }
    }

    public void onFileChanged(String path) {
        // File content changed (increased or decreased).
        System.out.println("File " + path + " has been modified.");
        List<String> chatRecords = chatRecordsApi.getChatHistories(friendId);

        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText("");
                for (String chatRecord : chatRecords) {
                    textView.append(chatRecord + "\n");
                }
            }
        });
    }

    public void onFileCreated(String path) {
        // File created.
        System.out.println("File " + path + " has been created.");
    }

    public void onFileDeleted(String path) {
        // File deleted.
        System.out.println("File " + path + " has been deleted.");
    }
}
