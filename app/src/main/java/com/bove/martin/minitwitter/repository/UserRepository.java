package com.bove.martin.minitwitter.repository;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.bove.martin.minitwitter.R;
import com.bove.martin.minitwitter.common.Constantes;
import com.bove.martin.minitwitter.common.MyApp;
import com.bove.martin.minitwitter.common.SharedPreferecesManager;
import com.bove.martin.minitwitter.retrofit.TokenTwitterClient;
import com.bove.martin.minitwitter.retrofit.TokenTwitterServices;
import com.bove.martin.minitwitter.retrofit.request.RequestUserProfile;
import com.bove.martin.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.bove.martin.minitwitter.retrofit.response.ResponseUserProfile;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Martín Bove on 24-Jul-20.
 * E-mail: mbove77@gmail.com
 */
public class UserRepository {
    public static UserRepository instance;
    private TokenTwitterClient tokenTwitterClient;
    private TokenTwitterServices tokenTwitterService;

    private MutableLiveData<ResponseUserProfile> userProfile;
    private MutableLiveData<String> photoProfile;

    public UserRepository() {
        instance = this;
        tokenTwitterClient = TokenTwitterClient.getInstance();
        tokenTwitterService = tokenTwitterClient.getTokenTwitterService();
        userProfile = getUserProfile();
        photoProfile = getPhotoProfile();
    }

    public MutableLiveData<ResponseUserProfile> getUserProfile() {
        if(userProfile == null) {
            userProfile = new MutableLiveData<>();
        }
        Call<ResponseUserProfile> call = tokenTwitterService.getUserProfile();
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()) {
                    userProfile.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), R.string.response_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), R.string.conection_error, Toast.LENGTH_SHORT).show();
            }
        });

        return userProfile;
    }

    public void updateProfile(RequestUserProfile requestUserProfile) {
        Call<ResponseUserProfile> call = tokenTwitterService.updateProfile(requestUserProfile);
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()) {
                    userProfile.setValue(response.body());
                    Toast.makeText(MyApp.getContext(), "La información del perfil ha sido actualizada.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyApp.getContext(), R.string.response_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), R.string.conection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadPhoto(String photoPath){
       File file = new File(photoPath);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        Call<ResponseUploadPhoto> call = tokenTwitterService.uploadProfilePhoto(requestBody);
        call.enqueue(new Callback<ResponseUploadPhoto>() {
            @Override
            public void onResponse(Call<ResponseUploadPhoto> call, Response<ResponseUploadPhoto> response) {
                if(response.isSuccessful()) {
                    SharedPreferecesManager.setStringValue(Constantes.PHOTO_URL, response.body().getFilename());
                    photoProfile.setValue(response.body().getFilename());
                } else {
                    Toast.makeText(MyApp.getContext(), R.string.response_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUploadPhoto> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), R.string.conection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<String> getPhotoProfile(){
        if(photoProfile == null) {
            photoProfile = new MutableLiveData<>();
        }
        return photoProfile;
    }

    // Singleton para compartir datos entre los viewModels
    public static UserRepository getInstance() {
        if(instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
}
