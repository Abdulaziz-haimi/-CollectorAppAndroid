package com.watercollector.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.watercollector.app.CollectorApp
import com.watercollector.app.R
import com.watercollector.app.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

import com.watercollector.app.data.remote.ApiDiscoveryClient


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        val container = (requireActivity().application as CollectorApp).appContainer
        LoginViewModelFactory(container.authRepository, container.sessionManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etBaseUrl.setText(viewModel.getSavedBaseUrl())

        if (viewModel.isLoggedIn()) {
            findNavController().navigate(R.id.homeFragment)
            return
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                baseUrl = binding.etBaseUrl.text?.toString()?.trim().orEmpty(),
                userName = binding.etUserName.text?.toString()?.trim().orEmpty(),
                password = binding.etPassword.text?.toString()?.trim().orEmpty(),
                onSuccess = { findNavController().navigate(R.id.homeFragment) }
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loading.collect {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.message.collect {
                binding.tvMessage.text = it.orEmpty()
            }
        }
        binding.btnFindServer.setOnClickListener {
            binding.tvMessage.text = "جاري البحث عن السيرفر..."
            binding.btnFindServer.isEnabled = false

            viewLifecycleOwner.lifecycleScope.launch {
                val url = ApiDiscoveryClient().discoverServer()

                binding.btnFindServer.isEnabled = true

                if (url != null) {
                    binding.etBaseUrl.setText(url)
                    binding.tvMessage.text = "تم العثور على السيرفر: $url"
                } else {
                    binding.tvMessage.text = "لم يتم العثور على السيرفر. تأكد أن الهاتف والكمبيوتر على نفس الشبكة."
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
