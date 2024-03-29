package ru.netology.nmedia.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.AuthApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Token
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.model.AuthModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val appAuth: AppAuth
): ViewModel() {

    private val _dataState = MutableLiveData<AuthModel>()
    val dataState: LiveData<AuthModel>
        get() = _dataState

    fun signIn(login: String, pass: String) = viewModelScope.launch {
        _dataState.value = AuthModel(loading = true)
        try {
            val response = AuthApiService.service.updateUser(login,pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val token: Token = requireNotNull( response.body())
            appAuth.setAuth(token)
            _dataState.value = AuthModel(success = true)
        } catch (e: Exception) {
            _dataState.value = AuthModel(error = true)
        }
    }

    fun clean(){
        _dataState.value = AuthModel(loading = false,error = false,success = false)
    }

}