package com.pensun.checkapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.pensun.checkapp.model.User;
import com.pensun.checkapp.repository.UserRepository;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final UserRepository userRepository = new UserRepository();
    public LiveData<User> getUser() { return userLiveData; }
    public void setUser(User user) { userLiveData.setValue(user); }
    public void loadUser(String token) {
        userRepository.getCurrentUser(token).observeForever(userLiveData::setValue);
    }
} 