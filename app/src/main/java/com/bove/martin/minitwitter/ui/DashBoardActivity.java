package com.bove.martin.minitwitter.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bove.martin.minitwitter.R;
import com.bove.martin.minitwitter.common.Constantes;
import com.bove.martin.minitwitter.common.SharedPreferecesManager;
import com.bove.martin.minitwitter.ui.fragments.NewTweetDialogFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class DashBoardActivity extends AppCompatActivity {
    private FloatingActionButton newTweetFab;
    private ImageView imageViewUserAvatar;
    private TextView textViewUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_tweets_like, R.id.navigation_profile).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        String userPhotUrl = SharedPreferecesManager.getStringValue(Constantes.PHOTO_URL);
        String userName = SharedPreferecesManager.getStringValue(Constantes.USERNAME);

        imageViewUserAvatar = findViewById(R.id.imageViewUserAccount);
        textViewUserName = findViewById(R.id.textViewTittle);

        textViewUserName.setText("@"+userName);

        if(!userPhotUrl.isEmpty()) {
            Glide.with(this)
                    .load(Constantes.API_PHOT_URL + userPhotUrl)
                    .into(imageViewUserAvatar);
        }

        newTweetFab = findViewById(R.id.fabNewTweet);
        newTweetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTweetDialogFragment dialog = new NewTweetDialogFragment();
                dialog.show(getSupportFragmentManager(),"NuevoTweetDiaglogFragment");
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId()) {
                    case R.id.navigation_home:
                        newTweetFab.show();
                        break;

                    case R.id.navigation_tweets_like:
                        newTweetFab.hide();
                        break;

                    case R.id.navigation_profile:
                        newTweetFab.hide();
                        break;
                }
            }
        });

    }

}