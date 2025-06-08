package com.pensun.checkapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.pensun.checkapp.model.User;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    public LiveData<User> getUser() { return userLiveData; }
    public void setUser(User user) { userLiveData.setValue(user); }
} 