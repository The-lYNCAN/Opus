package com.example.kaamkarlo.uI.components.Home

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.kaamkarlo.R
import com.example.kaamkarlo.data.Subject


@Composable
fun ColumnScope.SubCard(subjectId: Int?, progress: Float, subDetails: Subject) {

    Column (modifier = Modifier.height(150.dp).fillMaxWidth().padding(start = 18.dp, end = 18.dp, top = 10.dp, bottom = 10.dp).border(1.dp, Color.LightGray, shape = RoundedCornerShape(10.dp))) {
        Row (modifier = Modifier.padding(start = 18.dp, top = 18.dp, bottom = 10.dp)) {
            AsyncImage(model = R.mipmap.assignment, contentDescription = "", modifier = Modifier.height(50.dp))
            Text(subDetails.Subject_name, modifier = Modifier.padding(start = 10.dp, top = 10.dp))
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().padding(start = 18.dp, end = 10.dp, top = 5.dp),
            color = Color.Black,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )

        Text((progress*100.toInt()).toString() + "%", modifier = Modifier.padding(start = 18.dp, top = 10.dp))
        Text("20% complete", modifier = Modifier.padding(start = 18.dp, top = 10.dp))

    }
}