package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Token
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appAuth: AppAuth,
) : ViewModel() {

    val state = appAuth.authStateFlow
        .asLiveData()

    val data: LiveData<Token> = appAuth
        .data
        .asLiveData()

    val authorized: Boolean
        get() = appAuth.authStateFlow.value.token != null
}