package com.lyncan.opus.Repositories

import android.content.Context
import android.util.Log
import com.lyncan.opus.data.Assignment
import com.lyncan.opus.data.Groups
import com.lyncan.opus.data.Subject
import com.lyncan.opus.data.Uploads
import com.lyncan.opus.entities.SubjectEntity
import kotlinx.datetime.Clock
import javax.inject.Inject
import androidx.core.content.edit
import com.lyncan.opus.data.UpdateSubjectRequest
import com.lyncan.opus.data.user

class SubjectManagement @Inject constructor(private val repo: SupabaseRepository, val userState: UserState,
    private val subRepo: SubjectRepository, private val context: Context, private val subdb: SubjectRepository) {
    val subjectList = mutableMapOf<Subject, List<Assignment>>()
    val uploadList = mutableMapOf<Assignment, List<Uploads>>()
    var id = 0
    var whichOne = false

    fun clearAllData() {
        subjectList.clear()
        uploadList.clear()
    }

    fun addSubjectWithAssignments(subject: Subject, assignments: List<Assignment>) {
        subjectList[subject] = assignments
    }

    fun getAssignmentsForSubject(subject: Subject): List<Assignment>? {
        return subjectList[subject]
    }

    fun getAllSubjects(): Set<Subject> {
        return subjectList.keys
    }

    fun getAllData(): Pair<Map<Subject, List<Assignment>>, Map<Assignment, List<Uploads>>> {
        Log.d("subjectManagement", "Getting all data: ${subjectList.size} subjects and ${uploadList.size} uploads")
        return Pair(subjectList, uploadList)
    }
    suspend fun Retrieve(){
        val prefs = context.getSharedPreferences("SubjectManagementPrefs", Context.MODE_PRIVATE)
        val lastRetrieved = prefs.getLong("lastRetrieved", 0L)


        repo.database().from("Group").select {
            filter {
                eq("group_id", userState.getUser().group_id ?: -1)
            }
        }.decodeSingleOrNull<Groups>().also { it ->
            val time = it!!.updated_at!!.toLong()
            if (time > lastRetrieved){
                repo.database().from("sujet").select {
                    filter {
                        eq("group_id", userState.getUser().group_id?:-1)
                    }
                }.decodeList<Subject>().also { subjects ->
//                    subjects.forEach { subject ->
////                        addSubjectWithAssignments(subject, repo.getAssignments(subject.subject_id!!))
////                        repo.getAssignments(subject.subject_id!!).forEach { assignment ->
////                            Log.d("Subject Management, checking if 3 occurs in assignments as assid", assignment.assignment_id.toString())
////                            uploadList[assignment] = repo.getUploads(assignment.assignment_id!!)
////                        }
////                        subRepo.replaceAll(subject)
//                    }
                    subRepo.replaceAll(subjects)
                    prefs.edit {
                        putLong(
                            "lastRetrieved",
                            Clock.System.now().toEpochMilliseconds()
                        )
                    }
                    Log.d("SubjectManagement", "Data retrieved and local DB updated. Last retrieved time updated to ${Clock.System.now().toEpochMilliseconds()}")
                }
            }else{
                Log.d("SubjectManagement", "Data is up to date. No retrieval needed. Last retrieved time: $lastRetrieved, Group updated_at: $time")
            }
        }



    }
    fun retrieveProgressHome(): Pair<Int, Int>{
        var total = 0
        var completed = 0
        subjectList.forEach { (subject, assignment)->
            total += assignment.size
            assignment.forEach { assign->
                if(uploadList.any{(_, uploads) -> uploads.any{it.user_id == userState.getUser().user_id}}){
                    completed += 1

                }
            }
        }
        return Pair(total, completed)
    }

    suspend fun deleteSubject(subjectId: Int){
        val user = userState.getUser()
        val subjectToRemove = subjectList.keys.find { it.subject_id == subjectId }
//        subjectToRemove?.let {
//            subjectList.remove(it)
////            val delSubjected = .decodeSingleOrNull<Subject>()
////            subjectList.remove(delSubjected)
//        } ?: Log.d("SubjectManagement", "Subject with ID: $subjectId not found")
        repo.database().from("sujet").delete {
            select()
            filter {
                eq("subject_id", subjectId)
            }
        }
        updateTimeInGroup(user.group_id?: -1)
        try {
            subRepo.delete(subRepo.getSubjectById(subjectId)!!)
        }catch (e: Exception){
            Log.d("SubjectManagement", "Error deleting subject from local DB: ${e.message}")
        }
    }

    suspend fun createSubject(subjectName: String, subjectCode: String?, type: Int): Subject? {
        val user = userState.getUser()
        val newSubject = Subject(
            Subject_name = subjectName,
            group_id = user.group_id ?: -1,
            subject_code = subjectCode,
            type = type
        )
        val insertedSubject = repo.database().from("sujet").insert(newSubject){
            select()
        }.decodeSingleOrNull<Subject>()
        subjectList[insertedSubject!!] = emptyList()

        updateTimeInGroup(user.group_id ?: -1)
        subRepo.insert(
            SubjectEntity(
                name = insertedSubject.Subject_name,
                id = insertedSubject.subject_id!!,
                code = subjectCode,
                type = type
            )
        )

        return insertedSubject
    }

    fun changeWhichOne(value: Boolean){
        whichOne = value
    }

    fun readId(): Int{
        return id
    }


    fun getProblemSet(problemSetId: Int): String{
        subjectList.forEach { (subject, assignments) ->
            assignments.forEach { assignment ->
                if(assignment.assignment_id == problemSetId){
                    return assignment.assignment_url ?: ""
                }
            }
        }
        return ""
    }

    fun getUpload(uploadId: Int): String{
        uploadList.forEach { (assignments, uploads) ->
            uploads.forEach { upload ->
                if(upload.upload_id == uploadId){
                    return upload.upload_url
                }
            }
        }
        return ""
    }

    suspend fun updateSubject(subjectId: Int, newName: String, newCode: String?, groupId: Int, type: Int = 0){
        repo.database().from("sujet").update(
            UpdateSubjectRequest(
                subjectName = newName,
                subjectCode = newCode,
                type = type
            )
        ){
            filter {
                eq("subject_id", subjectId)
            }
        }
        updateTimeInGroup(groupId = groupId)
    }

    suspend fun updateTimeInGroup(groupId: Int){
        repo.database().from("Group").update({
            Groups::updated_at setTo Clock.System.now().toEpochMilliseconds().toString()
        }){
            filter {
                eq("group_id", groupId)
            }
        }
    }

}