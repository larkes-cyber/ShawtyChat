package com.example.ktorapp.data.remote

import com.example.ktorapp.domain.model.Message

interface MessageService {

    suspend fun getAllMessages(): List<Message>

    companion object{
        const val BASE_URL = "http://localhost:8080"
    }

    sealed class Endpoints(val url: String){
        object GetAllMessages: Endpoints("$BASE_URL/messages")
    }

}