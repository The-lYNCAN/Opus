package com.lyncan.opus.DataLayer.Repositories

import com.lyncan.opus.data.Groups
import com.lyncan.opus.data.user
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import kotlin.random.Random

class SupabaseRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
) {
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
        database().from("users").update( {
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


}