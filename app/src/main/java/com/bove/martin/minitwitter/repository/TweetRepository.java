package com.bove.martin.minitwitter.repository;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.bove.martin.minitwitter.R;
import com.bove.martin.minitwitter.common.Constantes;
import com.bove.martin.minitwitter.common.MyApp;
import com.bove.martin.minitwitter.common.SharedPreferecesManager;
import com.bove.martin.minitwitter.model.Like;
import com.bove.martin.minitwitter.model.Tweet;
import com.bove.martin.minitwitter.retrofit.TokenTwitterClient;
import com.bove.martin.minitwitter.retrofit.TokenTwitterServices;
import com.bove.martin.minitwitter.retrofit.request.RequestCreateTweet;
import com.bove.martin.minitwitter.retrofit.response.DeletedTweet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Martín Bove on 24-Jul-20.
 * E-mail: mbove77@gmail.com
 */
public class TweetRepository {
    public static TweetRepository instance;
    private TokenTwitterClient tokenTwitterClient;
    private TokenTwitterServices tokenTwitterService;
    private MutableLiveData<List<Tweet>> allTweets;
    private MutableLiveData<List<Tweet>> favTweets;
    private String userName;

    public TweetRepository() {
        instance = this;
        tokenTwitterClient = TokenTwitterClient.getInstance();
        tokenTwitterService = tokenTwitterClient.getTokenTwitterService();
        allTweets = getAllTweets();
        userName = SharedPreferecesManager.getStringValue(Constantes.USERNAME);
    }

    // Singleton para compartir datos entre los viewModels
    public static TweetRepository getInstance() {
        if(instance == null) {
            instance = new TweetRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Tweet>> getAllTweets() {
        if(allTweets == null) {
            allTweets = new MutableLiveData<>();
        }

        Call<List<Tweet>> call = tokenTwitterService.getAllTweets();

        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if(response.isSuccessful()) {
                    allTweets.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), R.string.response_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), R.string.conection_error, Toast.LENGTH_SHORT).show();
            }
        });

        return allTweets;
    }

    public void createTweet(String mensaje) {
        RequestCreateTweet requestCreateTweet = new RequestCreateTweet(mensaje);
        Call<Tweet> call = tokenTwitterService.createTweet(requestCreateTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()) {
                    List<Tweet> listaClonada = new ArrayList<>();
                    listaClonada.add(response.body());
                    listaClonada.addAll(allTweets.getValue());
                    allTweets.setValue(listaClonada);
                } else {
                    Toast.makeText(MyApp.getContext(), R.string.response_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), R.string.conection_error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void likeTweet(final int idTweet) {

        Call<Tweet> call = tokenTwitterService.likeTweet(idTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()) {
                    List<Tweet> listaClonada = new ArrayList<>();

                    for(int i=0; i < allTweets.getValue().size(); i++) {
                        // Si encontramos en la lista original el elemento sobre el que hemos
                        // hecho like, introducimos la respuesta del server, en otro caso
                        // copiamos el elemento de la lista antigua.
                        if(allTweets.getValue().get(i).getId() == idTweet) {
                            listaClonada.add(response.body());
                        } else {
                            listaClonada.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }
                    allTweets.setValue(listaClonada);
                    getFavTweets();

                } else {
                    Toast.makeText(MyApp.getContext(), R.string.conection_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), R.string.response_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<List<Tweet>> getFavTweets() {
        if(favTweets == null) {
            favTweets = new MutableLiveData<>();
        }

        List<Tweet> newFavTweets = new ArrayList<>();
        Iterator itTweet = allTweets.getValue().iterator();

        while (itTweet.hasNext()) {
            Tweet currentTweet = (Tweet) itTweet.next();
            Iterator itLikes = currentTweet.getLikes().iterator();
            boolean userLike = false;

            while (itLikes.hasNext() && !userLike) {
                Like currentLike = (Like) itLikes.next();
                if(currentLike.getUsername().equals(userName)) {
                    userLike = true;
                    newFavTweets.add(currentTweet);
                }
            }
        }

        favTweets.setValue(newFavTweets);
        return favTweets;
    }

    public void deleteTweet(final int idTweet) {
        Call<DeletedTweet> call = tokenTwitterService.deleteTweet(idTweet);
        call.enqueue(new Callback<DeletedTweet>() {
            @Override
            public void onResponse(Call<DeletedTweet> call, Response<DeletedTweet> response) {
                if (response.isSuccessful()) {
                    List<Tweet> listaClonada = new ArrayList<>();
                    for(int i=0; i< allTweets.getValue().size(); i++) {
                        if(allTweets.getValue().get(i).getId() != idTweet) {
                          listaClonada.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }
                    allTweets.setValue(listaClonada);
                    getFavTweets();
                    Toast.makeText(MyApp.getContext(), "El Tweet ha sido borrado!" , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyApp.getContext(), R.string.response_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeletedTweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), R.string.conection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
