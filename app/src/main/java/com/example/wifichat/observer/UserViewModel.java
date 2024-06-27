package com.example.wifichat.observer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wifichat.model.User;

import java.util.ArrayList;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<User>> users;

    public UserViewModel() {
        this.users = new MutableLiveData<>(new ArrayList<>());
    }

    public UserViewModel(MutableLiveData<ArrayList<User>> users) {
        this.users = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<ArrayList<User>> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> newUsers) {
        this.users.setValue(newUsers);
    }

}
