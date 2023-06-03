package com.example.ktorapp.presentation.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.qos.logback.core.spi.LifeCycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    username:String?
) {
    val context = LocalContext.current
    LaunchedEffect(true){
        viewModel.toastEvent.collectLatest {message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner){
        val observer = LifecycleEventObserver {_, event ->
            if(event == Lifecycle.Event.ON_START){
                viewModel.connectToChat()
            }else if (event == Lifecycle.Event.ON_STOP){
                viewModel.disconnect()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val state = viewModel.state.value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ){
            
            item { 
                Spacer(modifier = Modifier.height(32.dp))
            }
            items(state.messages){messages ->
                
                val isOwnMessage = username == messages.username
                
                Box(
                    contentAlignment = if(isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .width(200.dp)
                            .drawBehind {
                                val cornerRadius = 10.dp.toPx()
                                val triangleHeight = 20.dp.toPx()
                                val triangleWidth = 25.dp.toPx()

                                val trianglePath = Path().apply {
                                    if (isOwnMessage) {
                                        moveTo(size.width, size.height - cornerRadius)
                                        lineTo(size.width, size.height + triangleHeight)
                                        lineTo(
                                            size.width - triangleWidth,
                                            size.height - cornerRadius
                                        )
                                        close()
                                    } else {
                                        moveTo(0f, size.height - cornerRadius)
                                        lineTo(size.width, size.height + triangleHeight)
                                        lineTo(triangleWidth, size.height - cornerRadius)
                                        close()
                                    }
                                }

                                drawPath(
                                    path = trianglePath,
                                    color = if (isOwnMessage) Color.Green else Color.Gray
                                )
                            }
                            .background(
                                color = if (isOwnMessage) Color.Green else Color.Gray,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Text(
                            text = messages.username,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = messages.text,
                            color = Color.White
                        )
                        Text(
                            text = messages.formattedTime,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
                
            }
            
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = viewModel.messageText.value,
                onValueChange = viewModel::onMessageText,
                placeholder = {
                    Text(text = "Enter a message")
                },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = viewModel::sendMessage) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = ""
                )
            }
        }

    }

}