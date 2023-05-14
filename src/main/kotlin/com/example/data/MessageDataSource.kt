package com.example.data

import com.example.data.model.Message

interface MessageDataSource {

    suspend fun getAllMessages(): List<Message>
    suspend fun insertMessage(message:Message)

}