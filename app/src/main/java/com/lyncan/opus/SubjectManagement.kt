package com.lyncan.opus

import android.util.Log
import com.lyncan.opus.Modules.SupabaseRepository
import com.lyncan.opus.data.Assignment
import com.lyncan.opus.data.Subject
import com.lyncan.opus.data.Uploads
import javax.inject.Inject
import kotlin.collections.forEach

class SubjectManagement @Inject constructor(private val repo: SupabaseRepository, val userState: UserState) {
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
        Log.d("SubjectManagement", "Retrieving subjects and assignments for group_id: ${userState.getUser().group_id}")
        repo.database().from("sujet").select {
            filter {
                eq("group_id", userState.getUser().group_id?:-1)
            }
        }.decodeList<Subject>().also { subjects ->
            subjects.forEach { subject ->
                addSubjectWithAssignments(subject, repo.getAssignments(subject.subject_id!!))
                repo.getAssignments(subject.subject_id!!).forEach { assignment ->
                    Log.d("Subject Management, checking if 3 occurs in assignments as assid", assignment.assignment_id.toString())
                    uploadList[assignment] = repo.getUploads(assignment.assignment_id!!)
                }
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
        val subjectToRemove = subjectList.keys.find { it.subject_id == subjectId }
        subjectToRemove?.let {
            subjectList.remove(it)
            val delSubjected = repo.database().from("sujet").delete {
                select()
                filter {
                    eq("subject_id", subjectId)
                }
            }.decodeSingleOrNull<Subject>()
            subjectList.remove(delSubjected)
        } ?: Log.d("SubjectManagement", "Subject with ID: $subjectId not found")
    }

    suspend fun createSubject(subjectName: String): Subject? {
        val user = userState.getUser()
        val newSubject = Subject(
            Subject_name = subjectName,
            group_id = user.group_id ?: -1
        )
        val insertedSubject = repo.database().from("sujet").insert(newSubject){
            select()
        }.decodeSingleOrNull<Subject>()
        subjectList[insertedSubject!!] = emptyList()

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
}
