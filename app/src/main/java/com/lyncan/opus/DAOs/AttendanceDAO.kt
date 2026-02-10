package com.lyncan.opus.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lyncan.opus.entities.AttendanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDAO {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity)

    @Query("SELECT * FROM attendance ORDER BY date DESC")
    fun getAllAttendance(): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE subjectId = :subjectId")
    fun getAttendanceBySubject(subjectId: Int): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE id = :id")
    fun getAttendanceById(id: Int): Flow<List<AttendanceEntity>>

    @Query("""
    SELECT * FROM attendance 
    WHERE date = :date
    """)
    fun getAttendanceByDate(date: String): Flow<List<AttendanceEntity>>

    @Update
    suspend fun updateAttendance(attendance: AttendanceEntity)

    @Delete
    suspend fun deleteAttendance(attendance: AttendanceEntity)


}