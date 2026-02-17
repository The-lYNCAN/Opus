package com.lyncan.opus.DataLayer.Repositories

import android.util.Log
import com.lyncan.opus.DataLayer.local.DAOs.AttendanceDAO
import com.lyncan.opus.DataLayer.local.DAOs.SubjectDAO
import com.lyncan.opus.DataLayer.local.DAOs.TimeTableDAO
import com.lyncan.opus.DataLayer.local.entities.AttendanceEntity
import com.lyncan.opus.DataLayer.local.entities.SubjectEntity
import com.lyncan.opus.DataLayer.local.entities.TimeTableEntity
import com.lyncan.opus.data.Subject
import com.lyncan.opus.data.TimeTableEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SubjectRepository(private val subjectDAO: SubjectDAO ) {
    fun getAllSubjects() = subjectDAO.getAllSubjects()


    suspend fun insert(subject: SubjectEntity) {
        subjectDAO.insertSubject(subject)
    }

    suspend fun getSubjectById(subjectId: Int): SubjectEntity? {
        return subjectDAO.getSubjectById(subjectId)
    }
    suspend fun update(subject: SubjectEntity) {
        subjectDAO.updateSubject(subject)
    }
    suspend fun delete(subject: SubjectEntity) {
        subjectDAO.deleteSubject(subject)
    }

    suspend fun replaceAll(subjects: List<Subject>) {
        subjectDAO.deleteAll()
        insertAll(subjects)
    }

    suspend fun insertAll(subjects: List<Subject>) {
        subjects.forEach { subjectDAO.insertSubject(SubjectEntity(it.subject_id!!, name = it.Subject_name, code = it.subject_code,
            type = it.type)) }
    }
}

class AttendanceRepository(private val attendanceDao: AttendanceDAO) {
    fun getAttendanceBySubject(subjectId: Int) = attendanceDao.getAttendanceBySubject(subjectId)

    suspend fun markPresent(id: Int){
        val gett = attendanceDao.getAttendanceById(id).first()[0].copy(isPresent = true)
        attendanceDao.updateAttendance(gett)
    }
    suspend fun markAbsent(id: Int){
        val gett = attendanceDao.getAttendanceById(id).first()[0].copy(isPresent = false)
        attendanceDao.updateAttendance(gett)
    }

    fun getALl() = attendanceDao.getAllAttendance()
    suspend fun insert(attendance: AttendanceEntity){
        Log.d("AttendanceRepository", "Inserting attendance: $attendance")
        attendanceDao.insertAttendance(attendance)
    }
    suspend fun update(attendance: AttendanceEntity){
        attendanceDao.updateAttendance(attendance)
    }
    suspend fun delete(attendance: AttendanceEntity){
        attendanceDao.deleteAttendance(attendance)
    }
    suspend fun attendancePresent(
        date: String,
        timeTableId: Int
    ): Boolean {
        return getALl()
            .first()   // ⬅️ take ONE emission
            .any { attendance ->
                attendance.date == date &&
                        attendance.timeTableId == timeTableId
            }
    }
    fun getTodaysAttendance(date: String): Flow<List<AttendanceEntity>> {
        return attendanceDao.getAttendanceByDate(date)
    }


}

class TimeTableRepository(private val timeTableDAO: TimeTableDAO) {
    fun getAllTimeTableEntries() = timeTableDAO.getAllTimeTable()

    suspend fun getTimeTableByDay(day: String) = timeTableDAO.getTimetableByDay(day)

    suspend fun getTimeTableById(timetableId: Int): TimeTableEntity? {
        return timeTableDAO.getTimeTableById(timetableId)
    }

    suspend fun insert(entry: TimeTableEntity) {
        timeTableDAO.insertTimeTable(entry)
    }

    suspend fun update(entry: TimeTableEntity) {
        timeTableDAO.updateTimetable(entry)
    }

    suspend fun delete(entry: Int) {
        timeTableDAO.deleteTimetableById(entry)
    }

    suspend fun replaceAll(entries: List<TimeTableEntry>) {
        timeTableDAO.deleteAll()
        entries.forEach { timeTableDAO.insertTimeTable(TimeTableEntity(
            id = it.id!!,
            subjectid = it.subjectid,
            day = it.day,
            startTime = it.startTime,
            endTime = it.endTime,
            type = it.type,
            room = it.room,
            group = it.group,
            editable = true
        )) }
    }
}