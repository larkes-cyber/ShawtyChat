package com.example.plugins

import com.example.room.RoomController
import com.example.routes.chatSocket
import io.ktor.application.*
import io.ktor.routing.*
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject


fun Application.configureRouting() {
    val roomController by inject<RoomController>()
    install(Routing){
        chatSocket(roomController = roomController)
    }
}
