package com.example.tasks.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository  = PersonRepository()
    private  val mSharedPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<Boolean>()
    var login : LiveData<Boolean> = mLogin

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        mPersonRepository.login(email,password, object : APIListener<HeaderModel> {
            override fun onSucess(response: HeaderModel) {
                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY,response.token)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY,response.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME,response.name)
                mLogin.value = true
            }
            override fun onError(erro: String) {
                mLogin.value = false

            }
        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
    }

}