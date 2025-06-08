package com.pensun.checkapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.pensun.checkapp.model.LoginRequest;
import com.pensun.checkapp.model.LoginResponse;
import com.pensun.checkapp.repository.UserRepository;

public class LoginViewModel extends ViewModel {
    private final UserRepository userRepository = new UserRepository();

    public LiveData<LoginResponse> login(LoginRequest request) {
        return userRepository.login(request);
    }
} 