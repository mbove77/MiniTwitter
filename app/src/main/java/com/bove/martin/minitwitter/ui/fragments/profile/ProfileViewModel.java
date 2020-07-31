package com.bove.martin.minitwitter.ui.fragments.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bove.martin.minitwitter.repository.UserRepository;
import com.bove.martin.minitwitter.retrofit.request.RequestUserProfile;
import com.bove.martin.minitwitter.retrofit.response.ResponseUserProfile;

public class ProfileViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    public LiveData<ResponseUserProfile> userProfile;
    public LiveData<String> photoProfile;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
        userProfile = userRepository.getUserProfile();
        photoProfile = userRepository.getPhotoProfile();
    }

    public void updaeUserProfile(RequestUserProfile requestUserProfile) {
        userRepository.updateProfile(requestUserProfile);
    }

    public void uploadPhoto(String photo) {
        userRepository.uploadPhoto(photo);
    }
}