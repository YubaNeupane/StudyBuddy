package com.yjn5031.studybuddy;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


//craeats the table on the database for the event
@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id",
        childColumns = "user_id", onDelete = CASCADE))
public class EventElement {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long mId;

    @NonNull
    @ColumnInfo(name="name")
    public String mName;

    @NonNull
    @ColumnInfo(name="isCompleted")
    public boolean mIsComplected;

    @NonNull
    @ColumnInfo(name="createdAt")
    public long mCreatedAt;

    @ColumnInfo(name="dueAt")
    public long mDueAt;

    @ColumnInfo(name = "user_id")
    public long mUserId;
}
