package com.example.room

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*

data class Member(
    val username:String,
    val sessionId:String,
    val socket: WebSocketSession
)
