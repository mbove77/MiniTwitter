package com.bove.martin.minitwitter.retrofit;

import com.bove.martin.minitwitter.retrofit.request.RequestLogin;
import com.bove.martin.minitwitter.retrofit.request.RequestSingup;
import com.bove.martin.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.POST;

/**
 * Created by Martín Bove on 20-Jul-20.
 * E-mail: mbove77@gmail.com
 */
public interface MiniTwitterService {
    @POST("auth/login")
    Call<ResponseAuth> doLogin(@Body RequestLogin requestLogin);

    @POST("auth/signup")
    Call<ResponseAuth> doSingUp(@Body RequestSingup requestSingup);
}
