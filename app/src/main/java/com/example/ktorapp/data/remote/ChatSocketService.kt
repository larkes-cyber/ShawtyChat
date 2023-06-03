package com.example.ktorapp.data.remote

import com.example.ktorapp.data.remote.dto.MessageDto
import com.example.ktorapp.domain.model.Message
import com.example.ktorapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {

    suspend fun initSession(
        username:String
    ):Resource<Unit>

    suspend fun sendMessage(message:String)

    fun observeMessages(): Flow<Message>

    suspend fun closeConnection()

    companion object{
        const val BASE_URL = "ws://localhost:8080"
    }

    sealed class Endpoints(val url: String){
        object ChatSocket: Endpoints("$BASE_URL/chat-socket")
    }
}