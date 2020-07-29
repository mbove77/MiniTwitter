package com.bove.martin.minitwitter.retrofit;

import com.bove.martin.minitwitter.common.Constantes;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Martín Bove on 21-Jul-20.
 * E-mail: mbove77@gmail.com
 */
public class TokenTwitterClient {
    private static TokenTwitterClient instance = null;
    private TokenTwitterServices tokenTwitterService;
    private Retrofit retrofit;

    public TokenTwitterClient() {
        // Incluimos el token a ala cabecera de la petición.
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new AuthIntercector());
        OkHttpClient client = okHttpClientBuilder.build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        tokenTwitterService = retrofit.create(TokenTwitterServices.class);
    }

    // Singleton
    public static TokenTwitterClient getInstance() {
        if(instance == null) {
            instance = new TokenTwitterClient();
        }
        return instance;
    }

    public  TokenTwitterServices getTokenTwitterService() {
        return tokenTwitterService;
    }
}
