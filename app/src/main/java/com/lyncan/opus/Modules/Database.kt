//package com.lyncan.opus.Modules
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.RoomDatabase
//import com.lyncan.opus.DAOs.AttendanceDAO
//import com.lyncan.opus.DAOs.SubjectDAO
//import com.lyncan.opus.entities.AttendanceEntity
//import com.lyncan.opus.entities.SubjectEntity
//
//@Database(
//    entities = [AttendanceEntity::class, SubjectEntity::class],
//    version = 1
//)
//abstract class AppDatabase: RoomDatabase(){
//    abstract fun attendanceDAO(): AttendanceDAO
//    abstract fun subjectDAO(): SubjectDAO
//    companion object{
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//        fun getDatabase(context: Context): AppDatabase{
//            return INSTANCE ?: synchronized(this){
//                val instance = androidx.room.Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "opus_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}