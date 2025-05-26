package com.example.musicapp.data.source.local;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date longToDate(Long date) {
        if (date == null) {
            return null;
        }
        return new Date(date);
    }

    @TypeConverter
    public static Long dateToLong(Date date) {
        if (date == null) {
            return null;
        }
        return date.getTime();
    }
}
