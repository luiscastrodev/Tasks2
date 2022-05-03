package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.Main
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository  = PersonRepository(application)
    private  val mSharedPreferences = SecurityPreferences(application)

    private val mCreate = MutableLiveData<Main<Unit>>()
    var create : LiveData<Main<Unit>> = mCreate

    fun create(name: String, email: String, password: String) {
        mPersonRepository.create(email,password,name, object : APIListener<HeaderModel> {
            override fun onSucess(response: HeaderModel) {
                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY,response.token)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY,response.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME,response.name)

                mCreate.value = Main<Unit>().apply {
                    this.status = true
                }
            }

            override fun onError(erro: String) {
                mCreate.value = Main<Unit>().apply {
                    this.message = erro
                    this.status = false
                }
            }
        })
    }

}