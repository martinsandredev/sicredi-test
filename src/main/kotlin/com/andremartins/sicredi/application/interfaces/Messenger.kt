package com.andremartins.sicredi.application.interfaces

interface Messenger {
    sealed class MessageType {
        object SessionCreated : MessageType()
        object SessionClosed : MessageType()
    }

    fun <T> send(type: MessageType, payload: T)
}