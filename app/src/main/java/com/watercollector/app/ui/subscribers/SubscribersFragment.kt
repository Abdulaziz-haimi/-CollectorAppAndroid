package com.watercollector.app.ui.subscribers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.watercollector.app.CollectorApp
import com.watercollector.app.R
import com.watercollector.app.databinding.FragmentSubscribersBinding
import kotlinx.coroutines.launch

class SubscribersFragment : Fragment() {
    private var _binding: FragmentSubscribersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubscribersViewModel by viewModels {
        val container = (requireActivity().application as CollectorApp).appContainer
        SubscribersViewModelFactory(container.subscriberRepository)
    }

    private lateinit var adapter: SubscribersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSubscribersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SubscribersAdapter { item ->
            findNavController().navigate(
                R.id.subscriberDetailsFragment,
                bundleOf("subscriberId" to item.subscriberId)
            )
        }
        binding.rvSubscribers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSubscribers.adapter = adapter

        binding.etSearch.doAfterTextChanged { viewModel.setSearchText(it?.toString().orEmpty()) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { adapter.submitList(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
