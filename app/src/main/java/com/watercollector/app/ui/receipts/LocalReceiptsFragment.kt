package com.watercollector.app.ui.receipts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.watercollector.app.CollectorApp
import com.watercollector.app.databinding.FragmentLocalReceiptsBinding
import kotlinx.coroutines.launch

class LocalReceiptsFragment : Fragment() {
    private var _binding: FragmentLocalReceiptsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LocalReceiptsViewModel by viewModels {
        val container = (requireActivity().application as CollectorApp).appContainer
        LocalReceiptsViewModelFactory(container.receiptRepository)
    }

    private lateinit var adapter: LocalReceiptsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLocalReceiptsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = LocalReceiptsAdapter()
        binding.rvReceipts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReceipts.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { adapter.submitList(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
