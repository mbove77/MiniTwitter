package com.bove.martin.minitwitter.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bove.martin.minitwitter.R;
import com.bove.martin.minitwitter.common.Constantes;
import com.bove.martin.minitwitter.common.SharedPreferecesManager;
import com.bove.martin.minitwitter.ui.fragments.NewTweetDialogFragment;
import com.bove.martin.minitwitter.ui.fragments.profile.ProfileViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class DashBoardActivity extends AppCompatActivity implements PermissionListener {
    private FloatingActionButton newTweetFab;
    private ImageView imageViewUserAvatar;
    private TextView textViewUserName;
    private ProfileViewModel profileViewModel;

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
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
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

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constantes.SELECT_PHOTO_GALERY) {
                if (data != null) {
                    Uri imagenSelecionada = data.getData();
                    String[] filePathColum = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(imagenSelecionada, filePathColum, null,null, null);

                    if(cursor != null) {
                        cursor.moveToFirst();
                        int imageIndex = cursor.getColumnIndex(filePathColum[0]);
                        String photoPath = cursor.getString(imageIndex);
                        profileViewModel.uploadPhoto(photoPath);
                        cursor.close();
                    }
                }
            }
        }

    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        Intent seleccionarFoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(seleccionarFoto, Constantes.SELECT_PHOTO_GALERY);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

    }
}