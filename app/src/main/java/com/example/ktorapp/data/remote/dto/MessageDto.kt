package com.example.ktorapp.data.remote.dto

import com.example.ktorapp.domain.model.Message
import java.text.DateFormat
import java.util.Date

@kotlinx.serialization.Serializable
data class MessageDto(
    val text:String,
    val timestamp:Long,
    val username:String,
    val id:String
){
    fun toMessage(): Message {

        val date = Date(timestamp)
        val formattedDate = DateFormat
            .getDateInstance(DateFormat.DEFAULT)
            .format(date)

        return Message(
            text = text,
            formattedTime = formattedDate,
            username = username
        )
    }
}