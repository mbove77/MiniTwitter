package com.bove.martin.minitwitter.ui.fragments.favtweets;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bove.martin.minitwitter.model.Tweet;
import com.bove.martin.minitwitter.repository.TweetRepository;

import java.util.List;

public class FavTweetsViewModel extends AndroidViewModel {

    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> favTweets;

    public FavTweetsViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = TweetRepository.getInstance();
        favTweets = tweetRepository.getFavTweets();
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

}