package com.lyncan.opus.uI.components.Home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun RowScope.Card(img: Int, num: Int, diff: Boolean) {
    Row (modifier = Modifier.weight(1f).height(100.dp)
        .border(width = 2.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
        .padding(15.dp)
    ) {
        AsyncImage(img, contentDescription = "Card Image", modifier = Modifier.height(55.dp))

        Column (modifier = Modifier.padding(start = 9.dp)){
            Text(num.toString(), modifier = Modifier, fontSize = 33.sp, fontWeight = FontWeight.Bold)
            if(diff){
                text("Completed")
            }else{
                text("Pending")
            }
        }
    }
}

@Composable
fun text(text: String) {
    Text(text, modifier = Modifier.padding(top = 1.dp))
}