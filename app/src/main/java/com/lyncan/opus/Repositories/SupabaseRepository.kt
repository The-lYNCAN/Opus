package com.lyncan.opus.Repositories

import android.content.Context
import android.net.Uri
import com.lyncan.opus.data.Assignment
import com.lyncan.opus.data.Groups
import com.lyncan.opus.data.Subject
import com.lyncan.opus.data.TimeTableEntry
import com.lyncan.opus.data.Uploads
import com.lyncan.opus.data.user
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.UploadStatus
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.uploadAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

class SupabaseRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val applicationScope: CoroutineScope,
    private val context: Context
) {
    val client = supabaseClient
    fun isAuthenticated(): Boolean {
        return supabaseClient.auth.currentUserOrNull() != null
    }

    suspend fun getCurrentUserDetails(): user?{
        if(isAuthenticated()){
            val currentUser = supabaseClient.auth.currentUserOrNull()
            return supabaseClient.postgrest.from("users").select() {
                filter {
                    eq("id", currentUser!!.id)
                }
            }.decodeSingle<user>()

        } else {
            return null
        }
    }

    fun database(): Postgrest {
        return supabaseClient.postgrest
    }

    suspend fun getSubjects(group_id: Int): List<Subject>{
        database().from("sujet").select {
            filter {
                eq("group_id", group_id)
            }
        }.decodeList<Subject>().also { subjects ->
            return subjects
        }
    }

    suspend fun getAssignments(subject_id: Int): List<Assignment> {
        return database().from("affectation").select {
            filter {
                eq("subject_id", subject_id)
            }
        }.decodeList<Assignment>()
    }


    suspend fun getUploads(assignmentId: Int): List<Uploads> {
        val li = database().from("uploads").select {
            filter {
                eq("assignment_id", assignmentId)
            }
        }.decodeList<Uploads>()
        return li
    }

    suspend fun getSubjectFromSubjectId(subjectId: Int): Subject {
        return database().from("sujet").select {
            filter {
                eq("subject_id", subjectId)
            }
        }.decodeSingle<Subject>()
    }

    suspend fun getUserDetailsFromUserId(userId: String): user? {
        val us = database().from("users").select {
            filter {
                eq("id", userId)
            }
        }.decodeSingleOrNull<user>()
        return us

    }

    fun uploadPdf(pdfFiles: File, fileName: String): Flow<UploadStatus> {
        val bucket = supabaseClient.storage.from("assignments")
        val progress = bucket.uploadAsFlow("affectations/$fileName", pdfFiles)
        return progress
    }
    fun getPublicKey(fileKey: String): String {
        val publicKey = supabaseClient.storage.from("").publicUrl(fileKey)
        return publicKey
    }
    suspend fun upload(uploadDetails: Uploads){
        database().get("uploads").insert(uploadDetails)
    }

    suspend fun createGroup(groupName: String, description: String?= null): Int? {
        val group = database().from("Group").insert(
            Groups(
                group_name = groupName,
                description = description,
                group_banner = "",
                admin = supabaseClient.auth.currentUserOrNull()!!.id,
                invite_code = generateRandomAlphabetString(8),
                updated_at = System.currentTimeMillis().toString()
            ),
        ){
            select()
        }
        val groupId = group.decodeSingleOrNull<Groups>()?.group_id
        val updation = database().from("users").update( {
            set("group_id", groupId)
        }) {
            select()
            filter {
                eq("id", supabaseClient.auth.currentUserOrNull()!!.id)
            }

        }
        return groupId
    }
    fun generateRandomAlphabetString(length: Int): String {
        val alphabet = ('a'..'z') + ('A'..'Z') // Define the pool of alphabet characters
        return List(length) { alphabet.random(Random.Default) }.joinToString("")
    }

    suspend fun getGroupIdByInviteCode(inviteCode: String): Int? {
        return 0
    }

    suspend fun joinGroup(inviteCode: String): Int? {
        val groupId = database().from("Group").select {
            filter {
                eq("invite_code", inviteCode)
            }
        }.decodeSingleOrNull<Groups>()?.group_id
        val updation = database().from("users").update( {
            set("group_id", groupId)
        }) {
            select()
            filter {
                eq("id", supabaseClient.auth.currentUserOrNull()!!.id)
            }
        }
        return groupId
    }

    suspend fun deleteGroup(){
        val user = getCurrentUserDetails()
        database().from("Group").delete {
            filter {
                eq("group_id", user?.group_id ?: -1)
            }
        }

    }

    suspend fun uploadAssignment(subjectId: Int, subName: String, assignment: Uri, dueDate: String): Flow<UploadStatus> {
        val uniqueId = UUID.randomUUID().toString()
        val progress = supabaseClient.storage.from("assignments").uploadAsFlow("assignment-problems/$subjectId/$uniqueId.pdf", assignment)
        database().from("affectation").insert(
            Assignment(
                assignment_name = "$subName Assignment",
                subject_id = subjectId,
                assignment_url = "assignment-problems/$subjectId/$uniqueId.pdf",
                due_date = dueDate,
            )
        )
        return progress

    }

    suspend fun exitGroup(){
        database().from("users").update (
            {
                set<Int?>("group_id", null)
            }
        ) {
            filter {
                eq("id", supabaseClient.auth.currentUserOrNull()!!.id)
            }
        }
    }

    suspend fun createTimeTable(timeTableEntry: TimeTableEntry){

    }

}