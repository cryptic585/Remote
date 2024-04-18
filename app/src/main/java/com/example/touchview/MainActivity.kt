package com.example.touchview
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.net.Socket



/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}


@Composable
fun MyApp() {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var isPopupVisible by remember { mutableStateOf(false) }


    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { shortcuts() },
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 9.dp)
            ) {
                Text("Shortcuts")
            }
            Button(
                onClick = { screenshot() },
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 9.dp)
            ) {
                Text("Screenshot")
            }
            Button(
                onClick = { showKeyboard(context) },
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 9.dp)
            ) {
                Text("Keyboard")
            }


        }
        Box(
            modifier = Modifier
                .weight(1f)
                //.height(100.dp) // Set the height to be small
                .background(Color.Transparent),
                //.aspectRatio(1f),
            contentAlignment = Alignment.Center

        ) {
            TouchView(
                onTouch = { deltaX, deltaY ->
                    println("DeltaX: $deltaX, DeltaY: $deltaY")
                    // Send the coordinates to the server
                    sendCoordinatesToServer(deltaX, deltaY)
                },



            )
        }
        Box(
            modifier = Modifier
                //.weight(1f)
                .height(100.dp) // Set the height to be small
                .background(Color.Transparent)
                .padding(top = 16.dp), // Add padding to adjust the position from the top
                //.aspectRatio(1f),
            contentAlignment = Alignment.Center

        ) {
            TouchView2(
                onTap = {


                },
                onDoubleTap = {


                    Log.d("onDoubleTap", "onDoubleTap")
                },


                )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top // Align buttons at the top
                ) {
                    // First row with three buttons
                    Button(
                        onClick = { leftClick() },
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text("Left Click")
                    }
                    Button(
                        onClick = { scrollUp() },
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)

                    ) {
                        Text("Scroll Up")
                    }
                    Button(
                        onClick = { rightClick() },
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text("Right Click")
                    }
                }

                // Second row with one button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom // Align button at the bottom
                ) {
                    Button(
                        onClick = { scrollDown() },
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text("Scroll Down")
                    }
                }
            }
        }


    }
}

@Composable
fun TouchView2(onTap: () -> Unit, onDoubleTap: () -> Int) {
    var doubleTapCount by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {

                        Log.d("onDoubleTap", "onDoubleTap")
                        doubleTap()
                    },
                    onTap = {

                        Log.d("onTap", "onTap")
                        Tap()

                    }
                )
            })

}


@Composable
fun TouchView(onTouch: (x: Float, y: Float) -> Unit) {
    //val doubleTapCount = remember { mutableStateOf(0) }
    var doubleTapCount by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    onTouch(dragAmount.x, dragAmount.y)
                    Log.d("TouchView", "DeltaX: ${dragAmount.x}, DeltaY: ${dragAmount.y}")
                    sendCoordinatesToServer(dragAmount.x, dragAmount.y)
                }


            }
    )
}





fun Tap(){

    val ipAddress = "10.0.0.63" // Replace with your server's IP address
    val port = 1234 // Replace with your server's port number
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            //val dataToSend = "$x,$y,$otherData"
            // Send the x and y coordinates to the server
            //outToServer.writeUTF(dataToSend)

            outToServer.writeUTF("4");
            // Close the socket
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace() // Handle the exception appropriately
        }
    }

}

fun doubleTap() {

    val ipAddress = "10.0.0.63" // Replace with your server's IP address
    val port = 1234 // Replace with your server's port number
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            //val dataToSend = "$x,$y,$otherData"
            // Send the x and y coordinates to the server
            //outToServer.writeUTF(dataToSend)

            outToServer.writeUTF("5");
            // Close the socket
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace() // Handle the exception appropriately
        }
    }

}

fun sendCoordinatesToServer(x: Float, y: Float) {
    val ipAddress = "10.0.0.63" // Replace with your server's IP address
    val port = 1234 // Replace with your server's port number

    GlobalScope.launch(Dispatchers.IO) {
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            //val dataToSend = "$x,$y,$otherData"
            // Send the x and y coordinates to the server
            //outToServer.writeUTF(dataToSend)

            outToServer.writeUTF("1");
            outToServer.writeUTF("$x");
            outToServer.writeUTF("$y");
            // Close the socket
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace() // Handle the exception appropriately
        }
    }
}


fun leftClick() {
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = "10.0.0.63"
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("2")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun rightClick() {
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = "10.0.0.63"
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("3")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}



private fun showKeyboard(context: android.content.Context) {
    // Create an EditText to capture key events
    val editText = EditText(context)

    // Set the width and height to 0 to hide the text on the screen
    editText.layoutParams = ViewGroup.LayoutParams(0, 0)

    // Add the EditText to the layout to make it focusable
    val rootView = (context as Activity).findViewById<ViewGroup>(android.R.id.content)
    rootView.addView(editText)

    // Set up a TextWatcher to listen for text changes
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not used
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Log the changed text
            Log.d("Text Changed", s.toString()) // entire string
            Log.d("Text Changed", s?.lastOrNull().toString()) // last character
            var unicodeval = s?.lastOrNull()?.toInt()?.toString(16)?.toUpperCase() // unicode
            Log.d("Text Changed", "\\u$unicodeval")
            Log.d("Text Changed", s?.lastOrNull()?.toInt().toString())

            GlobalScope.launch(Dispatchers.IO) {
                val ipAddress = "10.0.0.63"
                val port = 1234
                try {
                    val socket = Socket(ipAddress, port)
                    val outToServer = DataOutputStream(socket.getOutputStream())
                    outToServer.writeUTF("6")
                    outToServer.writeUTF(s?.lastOrNull().toString())
                    socket.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
            // Not used
        }
    }

    editText.addTextChangedListener(textWatcher)

    // Request focus for the EditText to show the keyboard
    editText.requestFocus()

    // Open the keyboard
    val inputMethodManager =
        context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
    inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED)

    // Capture key events
    Log.d("showKeyboard", "showKeyboard")
}



fun screenshot(){

    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = "10.0.0.63"
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("7")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

fun scrollUp(){
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = "10.0.0.63"
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("8")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun scrollDown(){
    GlobalScope.launch(Dispatchers.IO) {
        val ipAddress = "10.0.0.63"
        val port = 1234
        try {
            val socket = Socket(ipAddress, port)
            val outToServer = DataOutputStream(socket.getOutputStream())
            outToServer.writeUTF("9")
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


fun shortcuts(){

}

@Preview
@Composable
fun MyAppPreview() {
    MyApp()
}
*/


import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    // Create a navigation controller
    val navController = rememberNavController()


    // Define the navigation graph
    NavigationGraph(navController)
}
