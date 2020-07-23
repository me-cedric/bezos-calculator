package eu.cedricmeyer.myapplication.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import eu.cedricmeyer.myapplication.R
import eu.cedricmeyer.myapplication.cgi_bot.*
import java.io.InputStream

class ChatView : Fragment() {
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ChatViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MainListAdapter(
            appExecutors = appExecutors,
            viewModel = viewModel
        )

        binding.recyclerView.adapter = adapter

        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        adapter.submitList((1..100).toList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChatView()
    }
}