package com.example.tasks.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.tasks.service.HeaderModel
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.repository.PersonRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository  = PersonRepository()

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        mPersonRepository.login(email,password, object : APIListener<HeaderModel> {
            override fun onSucess(response: HeaderModel) {
                Log.d("LoginViewModel", response.name)
            }
            override fun onError(erro: String) {
                Log.d("LoginViewModel", erro)

            }
        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
    }

}