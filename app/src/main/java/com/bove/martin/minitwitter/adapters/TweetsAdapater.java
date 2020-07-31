package com.bove.martin.minitwitter.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bove.martin.minitwitter.R;
import com.bove.martin.minitwitter.common.Constantes;
import com.bove.martin.minitwitter.common.SharedPreferecesManager;
import com.bove.martin.minitwitter.model.Like;
import com.bove.martin.minitwitter.model.Tweet;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Martín Bove on 22-Jul-20.
 * E-mail: mbove77@gmail.com
 */
public class TweetsAdapater extends RecyclerView.Adapter<TweetsAdapater.ViewHolder> {
    private List<Tweet> tweets;
    private int layout;
    private OnFavoriteClickListener favListener;
    private OnMenuClickListener menuListener;
    private Activity activity;

    public TweetsAdapater(List<Tweet> tweets, int layout, Activity activity, OnFavoriteClickListener favListener, OnMenuClickListener menuClickListener) {
        this.tweets = tweets;
        this.layout = layout;
        this.activity = activity;
        this.favListener = favListener;
        this.menuListener = menuClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflamos la vista
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(tweets.get(position), favListener, menuListener);
    }

    @Override
    public int getItemCount() { return tweets.size(); }

    // ViewHolder
    public  class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView textViewUserName, textViewMensaje, textViewLikeCounter;
        private ImageView imageViewAvatar, imageViewLike, imageViewMenu;
        private String userName;

        // Consturctor
        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewUserName = itemView.findViewById(R.id.textViewUsername);
            this.textViewMensaje = itemView.findViewById(R.id.textViewMensaje);
            this.textViewLikeCounter = itemView.findViewById(R.id.textViewLinkeCounter);
            this.imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            this.imageViewLike = itemView.findViewById(R.id.imageViewLike);
            this.imageViewMenu = itemView.findViewById(R.id.imageViewArrowMenu);


            this.userName = SharedPreferecesManager.getStringValue(Constantes.USERNAME);
        }

        // Aca es donde se cargan las datos reales
        public void bind(final Tweet tweet, final OnFavoriteClickListener favListener, OnMenuClickListener menuClickListener) {

            this.textViewUserName.setText("@"+tweet.getUser().getUsername());
            this.textViewMensaje.setText(tweet.getMensaje());
            this.textViewLikeCounter.setText(String.valueOf(tweet.getLikes().size()));

            if(!tweet.getUser().getPhotoUrl().equals("")) {
                Glide.with(activity)
                        .load(Constantes.API_PHOT_URL + tweet.getUser().getPhotoUrl())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .into(this.imageViewAvatar);
            } else {
                Glide.with(activity)
                        .load(R.drawable.ic_account_blue)
                        .into(this.imageViewAvatar);
            }

            // Reset likes color
            Glide.with(activity)
                    .load(R.drawable.ic_tweet_linke_outline)
                    .into(this.imageViewLike);

            this.textViewLikeCounter.setTextColor(activity.getResources().getColor(android.R.color.primary_text_light));
            this.textViewLikeCounter.setTypeface(null, Typeface.BOLD);

            // loop para pintar likes
            for (Like like: tweet.getLikes() ) {
                if(like.getUsername().equals(this.userName)) {
                    Glide.with(activity)
                            .load(R.drawable.ic_tweet_like)
                            .into(this.imageViewLike);

                    this.textViewLikeCounter.setTextColor(activity.getResources().getColor(R.color.like));
                    this.textViewLikeCounter.setTypeface(null, Typeface.BOLD);

                    break;
                }
            }

            this.imageViewMenu.setVisibility(View.GONE);
            if(tweet.getUser().getUsername().equals(userName)) {
                this.imageViewMenu.setVisibility(View.VISIBLE);
            }

            // Like Click
            imageViewLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favListener.onFavItemClick(tweet, getAdapterPosition());
                }
            });
            //Menu Click
            imageViewMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    menuListener.onMenuItemClick(tweet, getAdapterPosition());
                }
            });
        }
    }

    // Interfaz que define el onClick del adapter
    public interface OnFavoriteClickListener {
        void onFavItemClick(Tweet tweet, int posicion);
    }

    public interface OnMenuClickListener {
        void onMenuItemClick(Tweet tweet, int posicion);
    }
}
