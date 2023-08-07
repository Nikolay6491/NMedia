package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentSignInBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.viewmodel.SignInViewModel

class SignInFragment: Fragment() {

    private val viewModel: SignInViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val postViewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(
            inflater,
            container,
            false
        )



        viewModel.dataState.observe(viewLifecycleOwner){state ->
            binding.loading.isVisible = state.loading
            binding.signIn.isVisible = !state.loading
            binding.incorrect.isVisible = state.error
            if (state.success){
                postViewModel.refresh()
                viewModel.clean()
                findNavController().navigateUp()
            }
        }

        binding.signIn.setOnClickListener{
            binding.incorrect.isVisible = false
            binding.emptyField.isVisible = false

            AndroidUtils.hideKeyboard(binding.root)

            val login = binding.loginField.text.toString().trim()
            val password = binding.passwordField.text.toString()

            if (login.isBlank() || password.isBlank()){
                binding.emptyField.isVisible = true
            } else {
                viewModel.signIn(login,password)
            }
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    viewModel.clean()
                    findNavController().navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        )

        return binding.root
    }
}