package com.yjn5031.studybuddy;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {User.class, ToDo.class, EventElement.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase{

    //makes the name of the database
    private static final String DATABASE_NAME = "AppDatabase.db";
    private static AppDatabase mAppDatabase;

    //makes a dtabase witht he conext of the application
    public static AppDatabase getDatabase(Context context){
        if(mAppDatabase == null){
            mAppDatabase = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mAppDatabase;
    }

    //function to get the dao of the user, todo, and the event.

    public abstract UserDao userDao();
    public abstract ToDoDao toDoDao();
    public abstract EventDao eventDao();

}
