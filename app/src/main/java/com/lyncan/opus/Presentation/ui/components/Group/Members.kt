package com.lyncan.opus.Presentation.ui.components.Group

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyncan.opus.data.user

@Composable
fun Members(memberList: List<user>, admin: String){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
    ) {
        Text(
            "memberList",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 15.dp, top = 20.dp)
        )
        Spacer(Modifier.height(20.dp))
//        Column(modifier = Modifier.height(300.dp).verticalScroll(rememberListcrollState())) {
//            if (memberList.value.isEmpty()) {
//                Text("Retrieving memberList", modifier = Modifier.padding(20.dp))
//            } else {
//
//                for (i in 0..memberList.size - 1) {
//
//
//                }
//            }
//
//
//        }
        if (!memberList.isEmpty()){
            Log.d("memberList", "Member List: $memberList")
            LazyColumn(modifier = Modifier.height(300.dp)) {
                items(memberList.size) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                modifier = Modifier
                                    .size(40.dp),
                                shape = CircleShape,
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.LightGray.copy(
                                        alpha = 0.3f
                                    )
                                )
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val name = ""
                                    Text(
                                        text = memberList[it].full_name.split(" ")[0][0].toString() + if (memberList[it].full_name.split(
                                                " "
                                            ).size > 1
                                        ) memberList[it].full_name.split(" ")[1][0].toString() else "",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    memberList[it].full_name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(memberList[it].email, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp)) // Clip to rounded shape
                                    .background(
                                        if (admin == memberList[it].user_id) Color(
                                            0xFF62C370
                                        ) else Color(red = 236, blue = 242, green = 238)
                                    ) // Apply background after clipping
                                    .border(
                                        1.dp,
                                        Color.LightGray,
                                        shape = RoundedCornerShape(10.dp)
                                    ) // Border with same shape
                                    .padding(horizontal = 10.dp)
                                    .padding(horizontal = 10.dp),
                                text = if (admin == memberList[it].user_id) "admin" else "member",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}