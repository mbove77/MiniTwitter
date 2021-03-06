package com.bove.martin.minitwitter.ui.fragments.tweets;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bove.martin.minitwitter.model.Tweet;
import com.bove.martin.minitwitter.repository.TweetRepository;
import com.bove.martin.minitwitter.ui.fragments.BottomMenuDialogFragment;

import java.util.List;

public class TweetsViewModel extends AndroidViewModel {

    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweets;
    private LiveData<List<Tweet>> favTweets;

    public TweetsViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = TweetRepository.getInstance();
        tweets = tweetRepository.getAllTweets();
        favTweets = tweetRepository.getFavTweets();
    }

    public LiveData<List<Tweet>> getTweets() {
        return tweets;
    }

    public LiveData<List<Tweet>> getNewTweets() {
        tweets = tweetRepository.getAllTweets();
        return tweets;
    }

    public void insertTweet(String mensaje){
        tweetRepository.createTweet(mensaje);
    }

    public LiveData<List<Tweet>> getFavTweets() {
        return favTweets;
    }

    public LiveData<List<Tweet>> getNewFavTweets() {
        favTweets = tweetRepository.getFavTweets();
        return favTweets;
    }

    public void likeTweet(int idTweet) {
        tweetRepository.likeTweet(idTweet);
    }

    public void deleteTweet(int idTweet) {tweetRepository.deleteTweet(idTweet); }

    public void openDialogTweetMenu(Context ctx, int idTweet) {
        BottomMenuDialogFragment dialogTweet = BottomMenuDialogFragment.newInstance(idTweet);
        dialogTweet.show(((AppCompatActivity)ctx).getSupportFragmentManager() , "openDialogTweetMenu");
    }
}