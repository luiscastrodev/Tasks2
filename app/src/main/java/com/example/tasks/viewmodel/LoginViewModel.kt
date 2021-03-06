package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.helper.FingerPrintHelper
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.Main
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mPriorityRepository = PriorityRepository(application)

    private val mSharedPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<Main<Unit>>()
    var login: LiveData<Main<Unit>> = mLogin

    private val mLoggedUser = MutableLiveData<Boolean>()
    var loggedUser: LiveData<Boolean> = mLoggedUser


    private val mFingerPrint = MutableLiveData<Boolean>()
    var fingerPrin: LiveData<Boolean> = mFingerPrint

    fun isAuThenticationAvailable(){

        val token = mSharedPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val personKey = mSharedPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        val logged = token.isNotEmpty() && personKey.isNotEmpty()

        RetrofitClient.addHeader(token, personKey)

        if (!logged) {
            mPriorityRepository.all()
        }

        mLoggedUser.value = logged

        if(FingerPrintHelper.isAuthenticationAvailable(getApplication())){
            mFingerPrint.value = logged
        }
    }

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        mPersonRepository.login(email, password, object : APIListener<HeaderModel> {
            override fun onSucess(response: HeaderModel) {

                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY, response.token)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY, response.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, response.name)

                RetrofitClient.addHeader(response.token, response.personKey)

                mLogin.value = Main<Unit>().apply {
                    this.status = true
                }
            }

            override fun onError(erro: String) {
                mLogin.value = Main<Unit>().apply {
                    this.message = erro
                    this.status = false
                }
            }
        })
    }

    /**
     * Verifica se usu??rio est?? logado
     */
    fun verifyLoggedUser() {
        val token = mSharedPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val personKey = mSharedPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        val logged = token.isNotEmpty() && personKey.isNotEmpty()

        RetrofitClient.addHeader(token, personKey)

        if (!logged) {
            mPriorityRepository.all()
        }

        mLoggedUser.value = logged
    }

}