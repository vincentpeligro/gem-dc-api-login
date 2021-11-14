package com.example.apilogin;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apilogin.api.RequestPlaceholder;
import com.example.apilogin.api.RetrofitBuilder;
import com.example.apilogin.pojos.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText username,password;
    private Button loginBtn;
    public RetrofitBuilder retrofitBuilder;
    public RequestPlaceholder requestPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginBtn);

        retrofitBuilder = new RetrofitBuilder();
        requestPlaceholder = retrofitBuilder.getRetrofit().create(RequestPlaceholder.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText() != null && password.getText() != null) {
                    Call<Login> LoginCall = requestPlaceholder.Login(new Login(null, username.getText().toString(), null, null, password.getText().toString()));

                    LoginCall.enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            if (!response.isSuccessful()) {
                                if (response.code() == 404) {
                                    Toast.makeText(LoginActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                                    Log.e("LOGING ERR 1", response.message());
                                    Log.e("response code : " , String.valueOf(response.code()));
                                } else {
                                    Toast.makeText(LoginActivity.this, "There was an error upon logging in the API", Toast.LENGTH_SHORT).show();
                                    Log.e("LOGING ERR 2", response.message());
                                }
                            }else{
                                if (response.code() == 200) {
                                    Login loginResponse = response.body();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("USER ID", loginResponse.getId());
                                    intent.putExtra("USERNAME", loginResponse.getUsername());
                                    intent.putExtra("TOKEN", loginResponse.getToken());

                                    startActivity(intent);
                                    finish();
//                                    Toast.makeText(getApplicationContext(), "Login API successful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "There was an error upon logging in the API", Toast.LENGTH_SHORT).show();
                            Log.e("LOGING ERR 2", t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Please supply all the fields to login!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}