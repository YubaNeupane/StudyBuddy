package com.yjn5031.studybuddy;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ToDoDao {
    @Query("SELECT * FROM ToDo WHERE id = :id")
    public ToDo getToDo(long id);

    @Query("SELECT * FROM ToDo WHERE user_id = :userID ORDER BY id")
    public List<ToDo> getAllOfUserToDos(long userID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long addToDO(ToDo toDo);

    @Update
    public void updateToDo(ToDo toDo);

    @Delete
    public void deleteToDo(ToDo toDo);
}
