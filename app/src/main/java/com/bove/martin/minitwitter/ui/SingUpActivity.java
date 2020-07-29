package com.bove.martin.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bove.martin.minitwitter.R;
import com.bove.martin.minitwitter.common.Constantes;
import com.bove.martin.minitwitter.common.SharedPreferecesManager;
import com.bove.martin.minitwitter.retrofit.MiniTwitterClient;
import com.bove.martin.minitwitter.retrofit.MiniTwitterService;
import com.bove.martin.minitwitter.retrofit.request.RequestSingup;
import com.bove.martin.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername, editTextEmail, editTextPass;
    private Button buttonRegister;

    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        retrofitInit();
        bindViews();
        bindEvents();
    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void bindEvents() {
        buttonRegister.setOnClickListener(this);
    }

    private void bindViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPass);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    private void gotoSingUp() {
        String username, email, pass;
        username = editTextUsername.getText().toString();
        email = editTextEmail.getText().toString();
        pass = editTextPass.getText().toString();

        if(username.isEmpty()) {
            editTextUsername.setError("El nombre de usuario es requerido.");
        } else if(email.isEmpty()) {
            editTextEmail.setError("El email es requerido.");
        } else if(pass.isEmpty()) {
            editTextPass.setError("El password es requerido.");
        } else {
            RequestSingup requestSingup = new RequestSingup(username, email, pass, Constantes.API_CODE);
            Call<ResponseAuth>call = miniTwitterService.doSingUp(requestSingup);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()) {
                        SharedPreferecesManager.setStringValue(Constantes.TOKEN, response.body().getToken());
                        SharedPreferecesManager.setStringValue(Constantes.USERNAME, response.body().getUsername());
                        SharedPreferecesManager.setStringValue(Constantes.EMAIL, response.body().getEmail());
                        SharedPreferecesManager.setStringValue(Constantes.PHOTO_URL, response.body().getPhotoUrl());
                        SharedPreferecesManager.setStringValue(Constantes.CREATED, response.body().getCreated());
                        SharedPreferecesManager.setBooleanValue(Constantes.ACTIVE, response.body().getActive());

                        Intent i = new Intent(SingUpActivity.this, DashBoardActivity.class);
                        startActivity(i);

                        // Destruimos este activity para que no se pueda volver.
                        finish();
                    } else {
                        Toast.makeText(SingUpActivity.this, "Revise sus credenciales", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SingUpActivity.this, "Problemas de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.buttonRegister:
                gotoSingUp();
                break;
        }
    }


}
