package com.ing.ingterior.ui.chat

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ing.ingterior.model.ConversationModel
import com.ing.ingterior.model.DummyModel.getConversationDummyList

class ChatViewModel : ViewModel() {

    fun loadConversation(context: Context): ArrayList<ConversationModel> {
        return getConversationDummyList()
    }

}