package com.example.plugins

import io.ktor.application.*

import io.ktor.sessions.*
import io.ktor.util.*

class ChatSession(
   val username:String,
   val sessionId:String
)

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<ChatSession>("SESSION")
    }

    intercept(ApplicationCallPipeline.Features){
        if(call.sessions.get<ChatSession>() == null){
            val username = call.parameters["username"] ?: "guest"
            call.sessions.set(ChatSession(username, generateNonce()))
        }
    }

}
