package com.bove.martin.minitwitter.retrofit;

import com.bove.martin.minitwitter.common.Constantes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Martín Bove on 21-Jul-20.
 * E-mail: mbove77@gmail.com
 */
public class MiniTwitterClient {
    private static MiniTwitterClient instance = null;
    private MiniTwitterService miniTwitterService;
    private Retrofit retrofit;

    public MiniTwitterClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        miniTwitterService = retrofit.create(MiniTwitterService.class);
    }

    // Singleton
    public static MiniTwitterClient getInstance() {
        if(instance == null) {
            instance = new MiniTwitterClient();
        }
        return instance;
    }

    public  MiniTwitterService getMiniTwitterService() {
        return miniTwitterService;
    }
}
