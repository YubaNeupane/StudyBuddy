package com.yjn5031.studybuddy;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;


// class to make the event dap
@Dao
public interface EventDao {
    //gets the event with that inputted id
    @Query("SELECT * FROM EventElement WHERE id = :id")
    public EventElement getEvent(long id);

    //gets all of the event that was made by that current logied user
    @Query("SELECT * FROM EventElement WHERE user_id = :userID ORDER BY id")
    public List<EventElement> getAllOfUserEvents(long userID);

    //insters the current id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long addEvent(EventElement event);

    //udates the event
    @Update
    public void updateEvent(EventElement event);

    //deletes the event
    @Delete
    public void deleteEvent(EventElement event);
}
