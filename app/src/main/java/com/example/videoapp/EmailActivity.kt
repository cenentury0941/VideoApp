package com.example.videoapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.videoapp.ui.theme.VideoAppTheme

class EmailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoAppTheme{
            var email_to by remember {
                mutableStateOf("")
            }
            var email_from by remember {
                mutableStateOf("")
            }
            var email_sub by remember {
                mutableStateOf("")
            }
            var email_email by remember {
                mutableStateOf("")
            }

            var con = LocalContext.current
            Surface( modifier = Modifier.fillMaxSize(1f),
                color = Color.Black
                ) {

                Column( modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(10.dp, 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Send Email:", color = Color.White , fontSize = 32.sp, modifier = Modifier.fillMaxWidth(1f))
                    Card( elevation = CardDefaults.cardElevation(10.dp),
                        modifier = Modifier.padding(10.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(10.dp, 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            TextField(
                                value = email_from,
                                onValueChange = { email_from = it },
                                placeholder = {
                                    Text(
                                        text = "From"
                                    )
                                })
                            TextField(value = email_to, onValueChange = { email_to = it }, placeholder = {
                                Text(
                                    text = "To"
                                )
                            })
                            TextField(value = email_sub, onValueChange = { email_sub = it }, placeholder = {
                                Text(
                                    text = "Subject"
                                )
                            })
                            TextField(
                                value = email_email,
                                onValueChange = { email_email = it },
                                minLines = 10,
                                maxLines = 10,
                                placeholder = {
                                    Text(
                                        text = "Content"
                                    )
                                })
                        }

                    }
                    Button(
                        colors = ButtonDefaults.buttonColors( containerColor = Color.White, contentColor = Color.Black )
                        ,modifier = Modifier.fillMaxWidth(1f),
                        contentPadding = PaddingValues(5.dp)
                        ,onClick = {
                            con.sendMail(to = email_to, subject = email_sub, content = email_email)
                        }) {
                        Text(text = "Send Email")
                    }
                }
            }

        }
        }
    }
}


fun Context.sendMail(to: String, subject: String, content:String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email" // or "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // TODO: Handle case where no email app is available
    } catch (t: Throwable) {
        // TODO: Handle potential other type of exceptions
    }
}