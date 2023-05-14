package com.example.routes

import com.example.plugins.ChatSession
import com.example.room.RoomController
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Routing.chatSocket(roomController: RoomController){
    webSocket("/chat-socket") {
        val session = call.sessions.get<ChatSession>()
        if(session == null){
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "no sess"))
            return@webSocket
        }
        try {
            roomController.onJoin(
                username = session.username,
                sessionId = session.sessionId,
                socket = this
            )
            incoming.consumeEach { frame ->
                if(frame is Frame.Text){
                    roomController.sendMessage(
                        senderUsername = session.username,
                        message = frame.readText()
                    )
                }

            }
        }catch (e:Exception){
            call.respond(HttpStatusCode.Conflict)
        }finally {
            roomController.tryDisconnect(session.username)
        }

    }
}

fun Route.getAllMessages(roomController: RoomController){
    get("/messages"){
        call.respond(HttpStatusCode.OK, roomController.getAllMessages())
    }
}