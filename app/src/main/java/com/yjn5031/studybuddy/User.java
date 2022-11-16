package com.yjn5031.studybuddy;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long mId;


    @NonNull
    @ColumnInfo(name = "UserName")
    public String mUserName;

    @NonNull
    @ColumnInfo(name = "Email")
    public String mEmail;

    @NonNull
    @ColumnInfo(name = "Password")
    public String mPassword;

    @NonNull
    @ColumnInfo(name = "CreatedAt")
    public long mCreatedAt;

    public User (){
        mCreatedAt = System.currentTimeMillis();
    }
}