package com.yjn5031.studybuddy;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User WHERE id = :id")
    public User getSUser(long id);

    @Query("SELECT * FROM User WHERE UserName = :username")
    public User getUserByUsername(String username);

    @Query("SELECT * FROM User ORDER BY UserName COLLATE NOCASE")
    public List<User> getUsers();

    @Query("SELECT * FROM User ORDER BY CreatedAt DESC")
    public List<User> getUsersNewerFirst();

    @Query("SELECT * FROM User ORDER BY CreatedAt ASC")
    public List<User> getUsersOlderFirst();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long addUser(User user);

    @Update
    public void updateSubject(User user);

    @Delete
    public void deleteSubject(User user);

}
