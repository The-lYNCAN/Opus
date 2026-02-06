package com.lyncan.opus.Repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lyncan.opus.DAOs.AttendanceDAO
import com.lyncan.opus.DAOs.SubjectDAO
import com.lyncan.opus.DAOs.TimeTableDAO
import com.lyncan.opus.entities.AttendanceEntity
import com.lyncan.opus.entities.SubjectEntity
import com.lyncan.opus.entities.TimeTableEntity

@Database(
    entities = [AttendanceEntity::class, SubjectEntity::class, TimeTableEntity::class],
    version = 4
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun attendanceDAO(): AttendanceDAO
    abstract fun subjectDAO(): SubjectDAO
    abstract fun timetableDAO(): TimeTableDAO
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "opus_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}