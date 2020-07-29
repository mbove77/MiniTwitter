package com.bove.martin.minitwitter.ui.fragments.tweets;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bove.martin.minitwitter.R;
import com.bove.martin.minitwitter.adapters.TweetsAdapater;
import com.bove.martin.minitwitter.model.Tweet;

import java.util.ArrayList;
import java.util.List;


public class TweetsFragment extends Fragment implements TweetsAdapater.OnFavoriteClickListener, TweetsAdapater.OnMenuClickListener {

    private TweetsViewModel tweetsViewModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TweetsAdapater tweetsAdapater;
    private List<Tweet> tweetList = new ArrayList<Tweet>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetsViewModel = new ViewModelProvider(getActivity()).get(TweetsViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = root.findViewById(R.id.swiperRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAzul));
        swipeRefreshLayout.setRefreshing(true);
        setupRecyclerView(root);
        getAllTweets();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getNewTweets();
            }
        });

        return root;
    }

    private void getAllTweets() {
        tweetsViewModel.getTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList.clear();
                tweetList.addAll(tweets);
                tweetsAdapater.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void getNewTweets() {
        tweetsViewModel.getNewTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList.clear();
                tweetList.addAll(tweets);
                tweetsAdapater.notifyDataSetChanged();
                tweetsViewModel.getNewTweets().removeObserver(this);
            }
        });
    }

    private void setupRecyclerView(View view) {
        tweetsAdapater = new TweetsAdapater(tweetList, R.layout.tweet_item, getActivity(), this, this);
        recyclerView = view.findViewById(R.id.recyclerViewAllTweets);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(tweetsAdapater);
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