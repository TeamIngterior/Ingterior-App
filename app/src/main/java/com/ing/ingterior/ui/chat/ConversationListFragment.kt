package com.ing.ingterior.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.ConversationModel
import com.ing.ingterior.ui.IngTeriorViewModelFactory

class ConversationListFragment : Fragment(), ConversationClickedListener {

    private lateinit var tvEmpty: TextView
    private lateinit var rvConversationList: RecyclerView
    private lateinit var viewModel : ChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, IngTeriorViewModelFactory())[ChatViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_conversation_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvEmpty = view.findViewById(R.id.tv_conversation_empty)
        rvConversationList = view.findViewById(R.id.rv_conversation_list_view)
        rvConversationList.adapter = ConversationListAdapter()

    }

    companion object {
        private const val TAG = "ConversationListFragment"
    }

    inner class ConversationListAdapter : RecyclerView.Adapter<ConversationHolder>() {
        private val conversationList = viewModel.loadConversation(requireContext())
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationHolder {
            return ConversationHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false))
        }
        override fun getItemCount(): Int {
            return conversationList.size
        }
        override fun onBindViewHolder(holder: ConversationHolder, position: Int) {
            val conversationMessageView: ConversationListItemView = holder.itemView as ConversationListItemView
            conversationMessageView.setListener(this@ConversationListFragment)
            conversationMessageView.bind(position, conversationList[position])
        }
    }

    inner class ConversationHolder(itemView: View) : ViewHolder(itemView) {

    }

    override fun onConversationItemClicked(conversationModel: ConversationModel) {
        Factory.get().getMove().moveMessageListActivity(requireActivity(), conversationModel)
    }
}