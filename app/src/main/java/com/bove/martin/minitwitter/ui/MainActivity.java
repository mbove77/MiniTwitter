package com.bove.martin.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bove.martin.minitwitter.R;
import com.bove.martin.minitwitter.common.Constantes;
import com.bove.martin.minitwitter.common.SharedPreferecesManager;
import com.bove.martin.minitwitter.retrofit.MiniTwitterClient;
import com.bove.martin.minitwitter.retrofit.MiniTwitterService;
import com.bove.martin.minitwitter.retrofit.request.RequestLogin;
import com.bove.martin.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textViewCreateCuenta;
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;

    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        textViewCreateCuenta.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    private void bindViews() {
        textViewCreateCuenta = findViewById(R.id.textViewCreateCuenta);
        editTextEmail = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPass);
        buttonLogin = findViewById(R.id.buttonRegister);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.buttonRegister:
                goToLogin();
                break;

            case R.id.textViewCreateCuenta:
                Intent registerIntent = new Intent(view.getContext(), SingUpActivity.class);
                startActivity(registerIntent);
                break;
        }

    }

    private void goToLogin() {
        String email = editTextEmail.getText().toString();
        String pass = editTextPassword.getText().toString();

        if(email.isEmpty()) {
            editTextEmail.setError("El email es requerido");
        } else if(pass.isEmpty()) {
            editTextPassword.setError("El password es requerido.");
        }
        else {
            RequestLogin requestLogin = new RequestLogin(email, pass);
            Call<ResponseAuth> call = miniTwitterService.doLogin(requestLogin);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()) {
                        //Toast.makeText(MainActivity.this, "Sesión iniciada correctamente", Toast.LENGTH_SHORT).show();

                        SharedPreferecesManager.setStringValue(Constantes.TOKEN, response.body().getToken());
                        SharedPreferecesManager.setStringValue(Constantes.USERNAME, response.body().getUsername());
                        SharedPreferecesManager.setStringValue(Constantes.EMAIL, response.body().getEmail());
                        SharedPreferecesManager.setStringValue(Constantes.PHOTO_URL, response.body().getPhotoUrl());
                        SharedPreferecesManager.setStringValue(Constantes.CREATED, response.body().getCreated());
                        SharedPreferecesManager.setBooleanValue(Constantes.ACTIVE, response.body().getActive());

                        Intent i = new Intent(MainActivity.this, DashBoardActivity.class);
                        startActivity(i);

                        // Destruimos este activity para que no se pueda volver.
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Revise sus credenciales", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Problemas de conexión", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
