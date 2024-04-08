package com.ing.ingterior.ui.chat

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ing.ingterior.model.Conversation

class ChatViewModel : ViewModel() {

    fun loadConversation(context: Context): ArrayList<Conversation> {
        return Conversation.getDummyList()
    }

}