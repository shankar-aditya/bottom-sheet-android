package com.example.bottomsheetdialog

import android.icu.text.CaseMap.Title
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bottomsheetdialog.ui.theme.BottomSheetDialogTheme
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BottomSheet()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(){

    val reasonLists: List<String>? = listOf(
        "Reason 1",
        "Reason 10",
        "Reason 11",
    )
    val selectedReason = remember { mutableStateOf("") }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetGesturesEnabled = false,
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .background(Color.Red)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    )
            ){
                Column {
                    Text(
                        text = "Title goes here.",
                        fontSize = 24.sp
                    )
                    LazyColumn(){
                        item {
                            Text(text = "Description goes here."
                                    )
                        }
                        item {
                            ReasonList(reasonLists) {
                                selectedReason.value = it
                            }
                        }
                        item {
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button( onClick = {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                }){
                                    Text(
                                        text = "Cancel",
                                        fontSize = 15.sp
                                    )
                                }
                                Button( onClick = {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                }) {
                                    Text(
                                        text = "Continue",
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }

                    }
                }
            }

        }, sheetPeekHeight = 0.dp

    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                coroutineScope.launch {
                    if(bottomSheetScaffoldState.bottomSheetState.isCollapsed){
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                    else{
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            }) {
                Text(text = "Launch Bottom Dialog", fontSize = 18.sp)
            }
        }

    }
}


@Composable
fun ReasonList(radioOptions: List<String>? = null, selectionCallback: (String) -> Unit) {
    radioOptions?.let {
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(it[0]) }
        selectionCallback(selectedOption)

        LazyColumn(
            modifier = Modifier
                .heightIn(0.dp, 300.dp)
                .padding(top = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(radioOptions) {
                ReasonItem(it, selectedOption, onOptionSelected, selectionCallback)
            }
        }
    }
}

@Composable
fun ReasonItem(
    text: String,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    selectionCallback: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = (text == selectedOption),
                onClick = {
                    onOptionSelected(text)
                }
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.size(20.dp), //To avoid radio button have its own accessibility focus
            selected = (text == selectedOption),
//            colors = RadioButtonDefaults.colors(
//                selectedColor = colorResource(id = R.color.tlcm_popup_radio_button),
//                unselectedColor = colorResource(id = R.color.tlcm_popup_radio_button),
//                disabledColor = colorResource(id = R.color.tlcm_popup_radio_button)
//            ),
            onClick = {
                onOptionSelected(text)
                selectionCallback(text)
            }
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1.merge(),
//            color = colorResource(id = R.color.tlcm_popup_description),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}