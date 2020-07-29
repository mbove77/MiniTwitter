package com.bove.martin.minitwitter.retrofit;

import com.bove.martin.minitwitter.common.Constantes;
import com.bove.martin.minitwitter.common.SharedPreferecesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Martín Bove on 22-Jul-20.
 * E-mail: mbove77@gmail.com
 */
public class AuthIntercector implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SharedPreferecesManager.getStringValue(Constantes.TOKEN);
        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
        return chain.proceed(request);
    }
}
