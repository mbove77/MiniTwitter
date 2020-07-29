package com.bove.martin.minitwitter.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bove.martin.minitwitter.R;
import com.bove.martin.minitwitter.common.Constantes;
import com.bove.martin.minitwitter.common.SharedPreferecesManager;

import com.bove.martin.minitwitter.ui.fragments.tweets.TweetsViewModel;
import com.bumptech.glide.Glide;

public class NewTweetDialogFragment extends DialogFragment implements View.OnClickListener {

    private Button twitterButton;
    private ImageView imageViewCerrar, imageViewAvatar;
    private EditText editTextMensaje;
    private TweetsViewModel tweetsViewModel;

    private Dialog dialogNuevoTweet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialofStyel);
        tweetsViewModel = new ViewModelProvider(getActivity()).get(TweetsViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.nuevo_tweet_dialog, container, false);

        imageViewCerrar = view.findViewById(R.id.imageViewClose);
        imageViewAvatar = view.findViewById(R.id.imageViewAvatar);
        twitterButton = view.findViewById(R.id.buttonNewTweet);
        editTextMensaje = view.findViewById(R.id.textViewTweetMensaje);

        imageViewCerrar.setOnClickListener(this);
        twitterButton.setOnClickListener(this);

        dialogNuevoTweet = getDialog();

        String userPhotUrl = SharedPreferecesManager.getStringValue(Constantes.PHOTO_URL);
        if(!userPhotUrl.isEmpty()) {
            Glide.with(getActivity())
                    .load(Constantes.API_PHOT_URL + userPhotUrl)
                    .into(imageViewAvatar);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.imageViewClose:
                if(! editTextMensaje.getText().toString().isEmpty()) {
                    showConfirmDialog();
                } else {
                    getDialog().dismiss();
                }
                break;

            case R.id.buttonNewTweet:
                if(editTextMensaje.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Escribe una mensaje para twittear.", Toast.LENGTH_LONG).show();
                } else {
                    tweetsViewModel.insertTweet(editTextMensaje.getText().toString());
                    dialogNuevoTweet.dismiss();
                }
                break;
        }
    }

    public void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.tweet_cancel_msj);
        builder.setTitle(R.string.tweet_cancel_titulo);
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                dialogNuevoTweet.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.dismiss();
            }
        });

        builder.create();
        builder.show();
    }

}
