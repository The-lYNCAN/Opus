package com.lyncan.opus.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lyncan.opus.entities.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDAO {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertSubject(subject: SubjectEntity)

    @Query("SELECT * FROM subject ORDER BY name ASC")
    fun getAllSubjects(): Flow<List<SubjectEntity>>


    @Query("SELECT * FROM subject WHERE id = :subjectId")
    suspend fun getSubjectById(subjectId: Int): SubjectEntity?

    @Update
    suspend fun updateSubject(subject: SubjectEntity)

    @Delete
    suspend fun deleteSubject(subject: SubjectEntity)

    @Query("DELETE FROM subject")
    suspend fun deleteAll()
}