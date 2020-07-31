package com.bove.martin.minitwitter.retrofit;

import com.bove.martin.minitwitter.model.Tweet;
import com.bove.martin.minitwitter.retrofit.request.RequestCreateTweet;
import com.bove.martin.minitwitter.retrofit.request.RequestUserProfile;
import com.bove.martin.minitwitter.retrofit.response.DeletedTweet;
import com.bove.martin.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.bove.martin.minitwitter.retrofit.response.ResponseUserProfile;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Martín Bove on 23-Jul-20.
 * E-mail: mbove77@gmail.com
 */
public interface TokenTwitterServices {

    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();

    @GET("tweets/favs")
    Call<List<Tweet>> getAllFavTweets();

    @POST("tweets/create")
    Call<Tweet> createTweet(@Body RequestCreateTweet requestCreateTweet);

    @POST("tweets/like/{idTweet}")
    Call<Tweet> likeTweet(@Path ("idTweet") int idTweet);

    @DELETE("tweets/{idTweet}")
    Call<DeletedTweet> deleteTweet(@Path ("idTweet") int idTweet);

    @GET("users/profile")
    Call<ResponseUserProfile> getUserProfile();

    @PUT("users/profile")
    Call<ResponseUserProfile> updateProfile(@Body  RequestUserProfile userProfile);

    @Multipart
    @POST("users/uploadprofilephoto")
    Call<ResponseUploadPhoto> uploadProfilePhoto(@Part("file\"; filename=\"photo.jpeg\" ")RequestBody file);
}
