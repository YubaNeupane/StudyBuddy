package com.yjn5031.studybuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText userName, password;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        userName = findViewById(R.id.username);
        password = findViewById(R.id.signInPassword);
        db = AppDatabase.getDatabase(getApplicationContext());

        if(LoginedUser.user != null){
            startActivity(new Intent(this, Navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

    }


    public void goToSignUpPage(View view) {
        startActivity(new Intent(this, RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void signIn(View view){

        UserDao userDao = db.userDao();
        ToDoDao toDoDao = db.toDoDao();

        User databaseUser = userDao.getUserByUsername(userName.getText().toString());

        if(databaseUser != null){
            String temp = databaseUser.mPassword.toString();
            if(databaseUser.mPassword.equals(password.getText().toString())){
                LoginedUser.userToDos = toDoDao.getAllOfUserToDos(databaseUser.mId);
                startActivity(new Intent(this, Navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));



                LoginedUser.user = databaseUser;
                Toast.makeText(getApplicationContext(), "Hello, "+databaseUser.mUserName, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Invalid Password!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "User Not Found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(LoginedUser.user != null){
            startActivity(new Intent(this, Navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }
}