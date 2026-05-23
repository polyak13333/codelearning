package com.example.codelearningpuzzle;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks")
    List<TaskEntity> getAllTasks();

    @Insert
    void insertAll(TaskEntity... tasks);
}
