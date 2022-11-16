package com.yjn5031.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText email, username, password, confirmPassword;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        email = findViewById(R.id.regEmail);
        username = findViewById(R.id.regUsername);
        password = findViewById(R.id.regPassword);
        confirmPassword = findViewById(R.id.regConfim_password);
        db = AppDatabase.getDatabase(getApplicationContext());
    }

    public void goToSignInPage(View view) {
        startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }

    public void signUp(View view){
        User user = new User();
        user.mUserName = username.getText().toString();
        user.mEmail = email.getText().toString();
        UserDao userDao = db.userDao();

        if(password.getText().toString().equals(confirmPassword.getText().toString())){
            user.mPassword = password.getText().toString();

            userDao.addUser(user);
            LoginedUser.user = user;
            startActivity(new Intent(this, Navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();

        }
    }
}