package com.example.codelearningpuzzle;

import androidx.room.TypeConverter;
import java.util.Arrays;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String fromList(List<String> list) {
        if (list == null) return null;
        return String.join("___", list); // Склеиваем строки через три подчеркивания
    }

    @TypeConverter
    public static List<String> toList(String data) {
        if (data == null) return null;
        return Arrays.asList(data.split("___"));
    }
}
