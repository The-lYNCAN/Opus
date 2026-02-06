package com.lyncan.opus.ui.components.SubjectManagement

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubjectForm(
    addSubject: MutableState<Boolean>,
    addSubjectFunc: (String, String, Int) -> Unit,
    listState: LazyListState,
    itemIndex: Int,
    subjectName: MutableState<String>,
    subjectCode: MutableState<String>,
    options: List<String>,
    selectedIndex: MutableIntState,
    update: MutableState<Boolean>,
    updateFunction: (String, String, Int) -> Unit
) {

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.imePadding().fillMaxWidth().padding(16.dp).border(2.dp, Color(0xFF4F39F6), shape = RoundedCornerShape(10.dp))
        .background(Color.White)

    ) {
        OutlinedTextField(
            value = subjectName.value,
            onValueChange = {subjectName.value = it},
            modifier = Modifier.fillMaxWidth().padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            ).bringIntoViewRequester(bringIntoViewRequester).onFocusEvent{focusState ->
                if(focusState.isFocused){
                    coroutineScope.launch {
                        listState.animateScrollToItem(itemIndex)
                    }
                }

            },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xFF4F39F6),
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.LightGray.copy(alpha = .3f)
            ),
            placeholder = { Text("Subject Name *") }
        )

        OutlinedTextField(
            value = subjectCode.value,
            onValueChange = {subjectCode.value = it},
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xFF4F39F6),
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.LightGray.copy(alpha = .3f)
            ),
            placeholder = { Text("Subject Code (Optional)") },

        )
        Row (modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            options.forEachIndexed { index, string ->

                SegmentedButtons(string, index == selectedIndex.intValue, selectedIndex, index)
            }
        }
        Row(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = {
                    if(!update.value){
                        addSubjectFunc(subjectName.value, subjectCode.value, selectedIndex.intValue)
                        addSubject.value = false
                    }else{
                        updateFunction(subjectName.value, subjectCode.value,
                            selectedIndex.intValue)
                        addSubject.value = false
                    }
                },
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = 4.dp), // spacing BETWEEN buttons
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4F39F6)
                ),
                contentPadding = PaddingValues( // 🔑 key fix
                    horizontal = 8.dp,
                    vertical = 12.dp
                )
            ) {
                Text(if (!update.value) "Add Subject" else "Update Subject", style = MaterialTheme.typography.titleLarge)
            }
            Button(onClick = {
                addSubject.value = false
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp), // spacing BETWEEN buttons
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray.copy(alpha = .3f)
                ),
                contentPadding = PaddingValues( // 🔑 key fix
                    horizontal = 8.dp,
                    vertical = 12.dp
                )
            ) {
                Text("Cancel", color = Color.Black,
                    style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
fun RowScope.SegmentedButtons(
    string: String,
    selected: Boolean,
    selectedIndex: MutableIntState,
    index: Int
) {
    Button(
        onClick = { selectedIndex.intValue = index },
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp), // spacing BETWEEN buttons
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF4F39F6) else Color.LightGray.copy(alpha = .3f)
        ),
        contentPadding = PaddingValues( // 🔑 key fix
            horizontal = 8.dp,
            vertical = 12.dp
        )
    ) {
        Text(
            text = string + "${selectedIndex.intValue} and $index",
            maxLines = 1,
            softWrap = false,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = if (selected) Color.White else Color.Black,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


