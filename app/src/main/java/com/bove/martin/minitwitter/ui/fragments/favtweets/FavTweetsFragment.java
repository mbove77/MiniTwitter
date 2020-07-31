package com.bove.martin.minitwitter.ui.fragments.favtweets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

;import com.bove.martin.minitwitter.R;
import com.bove.martin.minitwitter.adapters.TweetsAdapater;
import com.bove.martin.minitwitter.model.Tweet;
import com.bove.martin.minitwitter.ui.fragments.tweets.TweetsViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavTweetsFragment extends Fragment implements TweetsAdapater.OnFavoriteClickListener, TweetsAdapater.OnMenuClickListener {

    private RecyclerView favRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TweetsAdapater favTweetsAdapater;
    private List<Tweet> favTweetList = new ArrayList<Tweet>();
    private TweetsViewModel tweetsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetsViewModel = new ViewModelProvider(getActivity()).get(TweetsViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fav_tweets, container, false);
        setupRecyclerView(root);
        getAllFavTweets();
        return root;
    }

    private void getAllFavTweets() {
        tweetsViewModel.getFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                favTweetList.clear();
                favTweetList.addAll(tweets);
                favTweetsAdapater.notifyDataSetChanged();
            }
        });
    }

    private void setupRecyclerView(View view) {
        favTweetsAdapater = new TweetsAdapater(favTweetList, R.layout.tweet_item, getActivity(), this, this);
        favRecyclerView = view.findViewById(R.id.recyclerViewAllFavTweets);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        favRecyclerView.setLayoutManager(linearLayoutManager);
        favRecyclerView.setItemAnimator(new DefaultItemAnimator());
        favRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        favRecyclerView.setAdapter(favTweetsAdapater);
    }

    @Override
    public void onFavItemClick(Tweet tweet, int posicion) {
        tweetsViewModel.likeTweet(tweet.getId());
    }

    @Override
    public void onMenuItemClick(Tweet tweet, int posicion) {
        tweetsViewModel.openDialogTweetMenu(getContext(), tweet.getId());
    }
}