package com.bove.martin.minitwitter.ui.fragments.profile;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bove.martin.minitwitter.R;
import com.bove.martin.minitwitter.common.Constantes;
import com.bove.martin.minitwitter.retrofit.request.RequestUserProfile;
import com.bove.martin.minitwitter.retrofit.response.ResponseUserProfile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;


public class ProfileFragment extends Fragment {

    private ImageView imageViewProfile;
    private EditText editTextUserName, editTextEmail, editTextWebSite, editTextDescription ,editTextPassword;
    private Button buttonSave;

    private ProfileViewModel profileViewModel;

    private PermissionListener allPermissionsListener;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        setupComponents(view);

        profileViewModel.photoProfile.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Glide.with(getContext())
                        .load(Constantes.API_PHOT_URL + s)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .into(imageViewProfile);
            }
        });

        profileViewModel.userProfile.observe(getActivity(), new Observer<ResponseUserProfile>() {
            @Override
            public void onChanged(ResponseUserProfile responseUserProfile) {
                if(!responseUserProfile.getPhotoUrl().equals("")) {
                    Glide.with(getContext())
                            .load(Constantes.API_PHOT_URL + responseUserProfile.getPhotoUrl())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .centerCrop()
                            .into(imageViewProfile);
                }
                editTextUserName.setText(responseUserProfile.getUsername());
                editTextEmail.setText(responseUserProfile.getEmail());
                editTextWebSite.setText(responseUserProfile.getWebsite());
                editTextDescription.setText(responseUserProfile.getDescripcion());
                buttonSave.setEnabled(true);
            }
        });

        return view;
    }

    private void setupComponents(View view) {
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        editTextUserName = view.findViewById(R.id.editTextTextPersonName);
        editTextEmail = view.findViewById(R.id.editTextTextEmailAddress);
        editTextWebSite = view.findViewById(R.id.editTexWebsite);
        editTextDescription = view.findViewById(R.id.editTextTextDescription);
        editTextPassword = view.findViewById(R.id.editTextTextPassword);
        buttonSave = view.findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermisions();
            }
        });

    }

    private void updateProfile() {
        String username = editTextUserName.getText().toString();
        String email = editTextEmail.getText().toString();
        String descripcion = editTextDescription.getText().toString();
        String website = editTextWebSite.getText().toString();
        String password = editTextPassword.getText().toString();

        if(username.isEmpty()) editTextUserName.setError("El Username es requerido.");
        else if(email.isEmpty()) editTextEmail.setError("El Email es requerido.");
        else if(password.isEmpty()) editTextPassword.setError("El Password es requerido.");
        else {
            buttonSave.setEnabled(false);
            RequestUserProfile requestUserProfile = new RequestUserProfile(username, email, descripcion, website, password);
            profileViewModel.updaeUserProfile(requestUserProfile);
        }
    }

    private void checkPermisions() {
        PermissionListener dialogOnDeniedPermissionListener = DialogOnDeniedPermissionListener.Builder.withContext(getActivity())
                .withTitle("Permisos")
                .withMessage("Los permisos son necesarios para seleccionar la fotografía a subir.")
                .withButtonText("Aceptar")
                .withIcon(R.mipmap.ic_launcher)
                .build();

        allPermissionsListener = new CompositePermissionListener((PermissionListener) getActivity(), dialogOnDeniedPermissionListener);

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(allPermissionsListener)
                .check();
    }

}