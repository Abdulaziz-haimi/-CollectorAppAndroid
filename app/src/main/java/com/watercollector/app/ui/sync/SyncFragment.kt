package com.watercollector.app.ui.sync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.watercollector.app.CollectorApp
import com.watercollector.app.databinding.FragmentSyncBinding
import kotlinx.coroutines.launch

class SyncFragment : Fragment() {
    private var _binding: FragmentSyncBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SyncViewModel by viewModels {
        val container = (requireActivity().application as CollectorApp).appContainer
        SyncViewModelFactory(container.syncRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSyncBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDownload.setOnClickListener { viewModel.downloadReceivables() }
        binding.btnUpload.setOnClickListener { viewModel.uploadPending() }
        binding.btnDecisions.setOnClickListener { viewModel.downloadDecisions() }
        binding.btnFullSync.setOnClickListener { viewModel.fullSync() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.resultText.collect { binding.tvResult.text = it }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loading.collect { binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
