package com.bove.martin.minitwitter.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.bove.martin.minitwitter.common.Constantes;
import com.bove.martin.minitwitter.ui.fragments.tweets.TweetsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bove.martin.minitwitter.R;
import com.google.android.material.navigation.NavigationView;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     TweetListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class TweetListDialogFragment extends BottomSheetDialogFragment {

    private TweetsViewModel tweetsViewModel;
    private int idTweet;

    // TODO: Customize parameters
    public static TweetListDialogFragment newInstance(int idTweet) {
        final TweetListDialogFragment fragment = new TweetListDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(Constantes.ARG_TWEET_ID, idTweet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null)  {
            idTweet = getArguments().getInt(Constantes.ARG_TWEET_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog, container, false);

        final NavigationView nav = v.findViewById(R.id.navigationViewTweet);
        nav.setBackgroundColor(Color.TRANSPARENT);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.action_delete_tweet) {
                    tweetsViewModel.deleteTweet(idTweet);
                    getDialog().dismiss();
                }

                return false;
            }
        });
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tweetsViewModel = new ViewModelProvider(getActivity()).get(TweetsViewModel.class);
    }
}