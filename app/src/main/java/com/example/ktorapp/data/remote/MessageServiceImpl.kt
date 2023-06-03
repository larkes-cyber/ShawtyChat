package com.example.ktorapp.data.remote

import com.example.ktorapp.data.remote.dto.MessageDto
import com.example.ktorapp.domain.model.Message
import io.ktor.client.*
import io.ktor.client.request.*


class MessageServiceImpl(
    private val client: HttpClient
): MessageService {

    override suspend fun getAllMessages(): List<Message> {
        return try {
            client.get<List<MessageDto>>(MessageService.Endpoints.GetAllMessages.url)
                .map { it.toMessage() }
        }catch (e:Exception){
            emptyList()
        }
    }

}