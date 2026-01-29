//package com.lyncan.opus.Modules
//
//import com.lyncan.opus.DAOs.AttendanceDAO
//import com.lyncan.opus.DAOs.SubjectDAO
//import com.lyncan.opus.entities.AttendanceEntity
//import com.lyncan.opus.entities.SubjectEntity
//
//class SubjectRepository(private val subjectDAO: SubjectDAO ) {
//    fun getAllSubjects() = subjectDAO.getAllSubjects()
//
//
//    suspend fun insert(subject: SubjectEntity) {
//        subjectDAO.insertSubject(subject)
//    }
//
//    suspend fun getSubjectById(subjectId: Int): SubjectEntity? {
//        return subjectDAO.getSubjectById(subjectId)
//    }
//    suspend fun update(subject: SubjectEntity) {
//        subjectDAO.updateSubject(subject)
//    }
//    suspend fun delete(subject: SubjectEntity) {
//        subjectDAO.deleteSubject(subject)
//    }
//}
//
//class AttendanceRepository(private val attendanceDao: AttendanceDAO) {
//    fun getAttendanceBySubject(subjectId: Int) = attendanceDao.getAttendanceBySubject(subjectId)
//    fun getALl() = attendanceDao.getAllAttendance()
//    suspend fun insert(attendance: AttendanceEntity){
//        attendanceDao.insertAttendance(attendance)
//    }
//    suspend fun update(attendance: AttendanceEntity){
//        attendanceDao.updateAttendance(attendance)
//    }
//    suspend fun delete(attendance: AttendanceEntity){
//        attendanceDao.deleteAttendance(attendance)
//    }
//}