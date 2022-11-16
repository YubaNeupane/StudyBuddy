package com.yjn5031.studybuddy;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id",
        childColumns = "user_id", onDelete = CASCADE))
public class ToDo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long mId;

    @NonNull
    @ColumnInfo(name = "name")
    public String mName;

    @NonNull
    @ColumnInfo(name="isComplected")
    public boolean mIsComplected;

    @NonNull
    @ColumnInfo(name = "createdAt")
    public long mCreatedAt;


    @ColumnInfo(name = "dueAt")
    public long mDueAt;

    @ColumnInfo(name = "user_id")
    public long mUserId;

}
