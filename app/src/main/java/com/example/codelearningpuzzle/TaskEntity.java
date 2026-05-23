package com.example.codelearningpuzzle;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import java.util.List;

@Entity(tableName = "tasks")
@TypeConverters({Converters.class}) // Конвертер нужен, чтобы сохранять списки строк в БД
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String resultText;
    public List<String> correctOrder;

    public TaskEntity(String resultText, List<String> correctOrder) {
        this.resultText = resultText;
        this.correctOrder = correctOrder;
    }
}
