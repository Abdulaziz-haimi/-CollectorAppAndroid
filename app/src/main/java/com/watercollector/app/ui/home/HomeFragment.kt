package com.watercollector.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.watercollector.app.CollectorApp
import com.watercollector.app.R
import com.watercollector.app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = (requireActivity().application as CollectorApp).appContainer.sessionManager

        binding.tvCollectorName.text = session.collectorName ?: session.userName ?: "المحصل"
        binding.tvPendingInfo.text = "CollectorID: ${session.collectorId}"
        binding.tvLastSyncInfo.text = "آخر تنزيل: ${session.lastExportedAt ?: "لا يوجد"}"

        binding.btnSubscribers.setOnClickListener { findNavController().navigate(R.id.subscribersFragment) }
        binding.btnReceipts.setOnClickListener { findNavController().navigate(R.id.localReceiptsFragment) }
        binding.btnSync.setOnClickListener { findNavController().navigate(R.id.syncFragment) }
        binding.btnLogout.setOnClickListener {
            session.clear()
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
