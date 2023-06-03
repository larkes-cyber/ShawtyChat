package com.example.ktorapp.presentation.chat

import com.example.ktorapp.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading:Boolean = false
)