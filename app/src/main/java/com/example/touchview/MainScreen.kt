package com.example.touchview

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket



@Composable
fun MainScreen(navController: NavHostController) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    //var text by remember { mutableStateOf("") }
    val context = LocalContext.current




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                // Navigate to AnotherScreen
                //navController.navigate("another_screen")
                //val currentTextFieldValue = text
                  //    connect(navController, context, currentTextFieldValue.toString())
                connect(navController, context, text.text)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Another Screen")
        }
    }
}
fun connect(navController: NavHostController, context: android.content.Context, currentTextFieldValue: String){


    GlobalScope.launch(Dispatchers.IO) {

        val ipAddress = currentTextFieldValue.trim()
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("0")
            // Create DataInputStream to receive response from server
            val inputFromServer = DataInputStream(socket.getInputStream())
            val response = inputFromServer.readUTF() // Read response from server
            Log.d("response", "response: ${response}")
            if(response == "Success"){
                //navController.navigate("another_screen/${ipAddress}")
            GlobalScope.launch(Dispatchers.Main) {
                navController.navigate("another_Screen?text=$ipAddress")
            }
            }else{
                val text = "Wrong IP Address so cannot cannot connect to server"
                GlobalScope.launch(Dispatchers.Main) {

                    Toast.makeText(context, "$text", Toast.LENGTH_SHORT).show()
                }

            }
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}