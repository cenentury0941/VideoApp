package com.example.videoapp

import VideoPlayer
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.videoapp.ui.theme.VideoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var playState = remember { mutableStateOf(0) }
            val openUrlDialog = remember { mutableStateOf(false) }
            val openHomeDialog = remember { mutableStateOf(false) }
            val url = remember { mutableStateOf("") }
            var expanded by remember { mutableStateOf(false) }
            var showBottomSheet = remember { mutableStateOf(false) }
            val mContext = LocalContext.current

            VideoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    when(playState.value)
                    {
                        0 -> Image(painterResource(id = R.drawable.dolby), contentDescription = "Dolby", modifier = Modifier.fillMaxSize(1f))
                        1 -> VideoPlayer((rememberResourceUri(resourceId = R.raw.vid)))
                        2 -> VideoPlayer(uri = Uri.parse(url.value))
                    }
                    Column( modifier = Modifier.fillMaxSize(1f) ,
                        horizontalAlignment = Alignment.CenterHorizontally ,
                        verticalArrangement = Arrangement.SpaceBetween ) {
                        Row( horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(1f)) {
                            Button(shape = CircleShape, border = BorderStroke(1.dp, Color.Gray), onClick = { expanded = true }, contentPadding = PaddingValues(0.dp) , modifier = Modifier.size(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Black))  {
                                Icon(
                                    Icons.Outlined.Menu,
                                    contentDescription = "Menu Button",
                                    tint = Color.White
                                )

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(onClick = {
                                        openHomeDialog.value = true
                                        expanded = false
                                    }) {
                                        Text("Home", color = Color.Black)
                                    }
                                    Divider()
                                    DropdownMenuItem(onClick = {
                                        showBottomSheet.value = true
                                        expanded = false
                                    }) {
                                        Text("Settings", color = Color.Black)
                                    }
                                    Divider()
                                    DropdownMenuItem(onClick = {
                                        mContext.startActivity(Intent(mContext, WebsiteActivity::class.java))
                                        expanded = false
                                    }) {
                                        Text("Presidio", color = Color.Black)
                                    }
                                }

                            }

                        }

                        Row {
                            Row( horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically , modifier = Modifier
                                .padding(10.dp, 0.dp, 10.dp, 30.dp)
                                .fillMaxWidth(1f)) {

                                Button(shape = CircleShape, border = BorderStroke(1.dp, Color.Gray), onClick = { playState.value = if (playState.value>0) 0 else 1 }, contentPadding = PaddingValues(0.dp) , modifier = Modifier.size(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                                    Icon(
                                        if (playState.value==0) Icons.Filled.PlayArrow else Icons.Filled.Close ,
                                        contentDescription = "Play Button",
                                        tint = Color.White
                                    )
                                }

                                Button(shape = CircleShape, border = BorderStroke(1.dp, Color.Gray), onClick = { openUrlDialog.value = true }, contentPadding = PaddingValues(0.dp) , modifier = Modifier.size(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Black))  {
                                    Icon(
                                        Icons.Outlined.Create,
                                        contentDescription = "Url Button",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }


                    if (openUrlDialog.value) {
                        urlAlert(openUrlDialog, url, playState)
                    }
                    
                    if(openHomeDialog.value)
                    {
                        homeAlert(openDialog = openHomeDialog)
                    }

                    if(showBottomSheet.value)
                    {
                        settingsPanel(showBottomSheet = showBottomSheet)
                    }

                }
            }
        }
    }
}

@Composable
fun rememberResourceUri(resourceId: Int): Uri {
    val context = LocalContext.current

    return remember(resourceId) {
        with(context.resources) {
            Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(getResourcePackageName(resourceId))
                .appendPath(getResourceTypeName(resourceId))
                .appendPath(getResourceEntryName(resourceId))
                .build()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun urlAlert(openDialog:MutableState<Boolean>, url:MutableState<String>, playState:MutableState<Int>)
{
        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .wrapContentHeight()
                    .padding(30.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = CenterHorizontally, verticalArrangement = SpaceEvenly) {
                    Text(text = "Enter video URL", fontSize = 24.sp)
                    TextField(value = url.value, onValueChange = {url.value=it }, placeholder = { Text(
                        text = "url"
                    )}, modifier = Modifier.padding(0.dp, 25.dp, 0.dp, 25.dp), maxLines = 1)
                    Button(onClick = {
                        playState.value = 2
                        openDialog.value = false
                    }) {
                        Text(text = "Confirm")
                    }
                }
            }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun homeAlert(openDialog:MutableState<Boolean>)
{
    var text by remember {
        mutableStateOf("Unset")
    }
    val cont = LocalContext.current
    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(1f)
                .wrapContentHeight()
                .padding(2.dp, 200.dp, 2.dp, 128.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Image(painter = painterResource(id = R.drawable.work), contentScale = ContentScale.Crop, contentDescription = "platelet", modifier = Modifier.fillMaxSize(1f))
            Column(modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(1f), horizontalAlignment = CenterHorizontally, verticalArrangement = Bottom ) {
                Card (
                    colors = CardDefaults.cardColors( containerColor = Color.White ),
                    elevation = CardDefaults.cardElevation(20.dp)
                    ,modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(1f)) {
                    Column( modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(10.dp), horizontalAlignment = CenterHorizontally, verticalArrangement = SpaceEvenly ) {
                        Text(text = "In Progress", fontSize = 24.sp)
                        Row( modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceEvenly , verticalAlignment = Alignment.CenterVertically  ) {

                            Button(onClick = {
                                Toast.makeText(cont , "Toast" , Toast.LENGTH_LONG).show()
                            }) {
                                Text(text = "A")
                            }

                            Button(onClick = {
                                text = "Text has been set"
                            }) {
                                Text(text = "B")
                            }

                            Button(onClick = {
                                cont.startActivity(Intent(cont, EmailActivity::class.java))
                            }) {
                                Text(text = "C")
                            }
                        }
                        Text(text = text)
                    }
                }
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun settingsPanel(showBottomSheet:MutableState<Boolean>)
{
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet.value = false
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier.padding(30.dp , 0.dp , 30.dp , 20.dp)) {
                    Text(text = "Settings", modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(0.dp, 0.dp, 0.dp, 25.dp), fontSize = 32.sp)
                    settingOption(text = "Option 1")
                    settingOption(text = "Option 2")
                    settingOption(text = "Option 3")
                    Divider( modifier = Modifier.padding(30.dp , 10.dp , 10.dp , 32.dp) )
                }
            }
}

@Composable
fun settingOption(text:String)
{
    Row(modifier = Modifier.fillMaxWidth(1f), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = text)
        var checked by remember { mutableStateOf(true) }
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }
}

