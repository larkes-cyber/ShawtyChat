package com.example.room

import com.example.data.MessageDataSource
import com.example.data.model.Message
import io.ktor.http.cio.websocket.*
import io.ktor.util.*
import io.ktor.util.collections.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RoomController(
    private val messageDataSource:MessageDataSource
) {

    @OptIn(InternalAPI::class)
    private val members = ConcurrentMap<String, Member>()

    @OptIn(InternalAPI::class)
    fun onJoin(
        username:String,
        sessionId:String,
        socket: WebSocketSession
    ){
        if(!members.containsKey(username)){
            members[username] = Member(
                username = username,
                sessionId = sessionId,
                socket = socket
            )
        }
    }

   @OptIn(InternalAPI::class)
   suspend fun sendMessage(senderUsername:String, message: String){

        members.values.forEach{member ->
            val messageEntity = Message(
                text = message,
                username = senderUsername,
                timestamp = System.currentTimeMillis()
            )
            messageDataSource.insertMessage(messageEntity)

            val parsedMessage = Json.encodeToString(messageEntity)
            member.socket.send(Frame.Text(parsedMessage))
        }

    }

    suspend fun getAllMessages():List<Message>{
        return messageDataSource.getAllMessages()
    }

    @OptIn(InternalAPI::class)
    suspend fun tryDisconnect(username:String){
        members[username]?.socket!!.close()
        if(members.containsKey(username)){
            members.remove(username)
        }
    }

}