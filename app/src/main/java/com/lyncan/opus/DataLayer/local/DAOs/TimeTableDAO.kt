package com.lyncan.opus.DataLayer.local.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lyncan.opus.DataLayer.local.entities.TimeTableEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeTableDAO {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertTimeTable(timetable: TimeTableEntity)

    @Query("SELECT * FROM timetable ORDER BY day ASC")
    fun getAllTimeTable(): Flow<List<TimeTableEntity>>


    @Query("SELECT * FROM timetable WHERE id = :timetableId")
    suspend fun getTimeTableById(timetableId: Int): TimeTableEntity?

    @Update
    suspend fun updateTimetable(timetable: TimeTableEntity)

    @Delete
    suspend fun deleteTimetable(timetable: TimeTableEntity)

    @Query("SELECT * FROM timetable WHERE day = :day ORDER BY startTime ASC")
    suspend fun getTimetableByDay(day: String): List<TimeTableEntity>

    @Query("DELETE FROM timetable")
    suspend fun deleteAll()

    @Query("DELETE FROM timetable WHERE id = :timetableId")
    suspend fun deleteTimetableById(timetableId: Int)
}