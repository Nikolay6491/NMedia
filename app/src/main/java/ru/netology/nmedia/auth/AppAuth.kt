package ru.netology.nmedia.auth

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.netology.nmedia.dto.Token

class AppAuth private constructor(context: Context) {

    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val _data = MutableStateFlow(Token())
    val data = _data.asStateFlow()


    companion object {
        private val idKey = "idKey"
        private val tokenKey = "tokenKey"

        private var INSTANCE: AppAuth? = null

        fun getInstance(): AppAuth = requireNotNull(INSTANCE){
            "init must be called"
        }

        fun init(context: Context){
            INSTANCE = AppAuth(context)
        }
    }

    init {
        val token = prefs.getString(tokenKey, null)
        val id = prefs.getLong(idKey, 0L)

        if (!prefs.contains(idKey) || !prefs.contains(tokenKey)){
            prefs.edit{
                clear()
            }
        } else {
            _data.value = Token(id, token)
        }
    }

    fun setAuth(token: Token) {
        _data.value = token
        prefs.edit {
            putString(tokenKey, token.token)
            putLong(idKey, token.id)

        }
    }

    fun remove() {
        prefs.edit { clear() }
        _data.value = Token()
    }
}