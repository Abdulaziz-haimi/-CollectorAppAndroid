package com.watercollector.app.ui.subscriberdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.watercollector.app.CollectorApp
import com.watercollector.app.R
import com.watercollector.app.databinding.FragmentSubscriberDetailsBinding
import kotlinx.coroutines.launch
import kotlin.math.abs

class SubscriberDetailsFragment : Fragment() {
    private var _binding: FragmentSubscriberDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubscriberDetailsViewModel by viewModels {
        val container = (requireActivity().application as CollectorApp).appContainer
        SubscriberDetailsViewModelFactory(container.subscriberRepository)
    }

    private lateinit var invoicesAdapter: SubscriberInvoicesAdapter
    private lateinit var metersAdapter: SubscriberMetersAdapter
    private lateinit var creditsAdapter: SubscriberCreditsAdapter
    private var subscriberId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSubscriberDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscriberId = requireArguments().getInt("subscriberId")

        invoicesAdapter = SubscriberInvoicesAdapter()
        metersAdapter = SubscriberMetersAdapter()
        creditsAdapter = SubscriberCreditsAdapter()

        binding.rvInvoices.layoutManager = LinearLayoutManager(requireContext())
        binding.rvInvoices.adapter = invoicesAdapter
        binding.rvMeters.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMeters.adapter = metersAdapter
        binding.rvCredits.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCredits.adapter = creditsAdapter

        binding.btnNewReceipt.setOnClickListener {
            findNavController().navigate(R.id.newReceiptFragment, bundleOf("subscriberId" to subscriberId))
        }

        observeData()
        viewModel.load(subscriberId)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.subscriber.collect { s ->
                if (s != null) {
                    binding.tvName.text = s.subscriberName
                    binding.tvPhone.text = s.phoneNumber ?: "بدون رقم"
                    binding.tvAddress.text = s.address ?: "بدون عنوان"
                    binding.tvMeter.text = s.primaryMeterNumber ?: "بدون عداد أساسي"
                    binding.tvBalance.text = when {
                        s.currentBalance > 0 -> "عليه ${"%.2f".format(s.currentBalance)}"
                        s.currentBalance < 0 -> "له ${"%.2f".format(abs(s.currentBalance))}"
                        else -> "متوازن"
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.invoices.collect {
                invoicesAdapter.submitList(it)
                binding.tvInvoicesCount.text = "الفواتير المفتوحة: ${it.size}"
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.meters.collect { metersAdapter.submitList(it) }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.credits.collect {
                creditsAdapter.submitList(it)
                binding.tvCreditsCount.text = "الأرصدة المقدمة: ${it.size}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
