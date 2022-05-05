package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.Main
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityRepository = PriorityRepository(application)
    private val mTaskRepository = TaskRepository(application)

    private val mPriorityList = MutableLiveData<Main<List<PriorityModel>>>()
    var priorities: LiveData<Main<List<PriorityModel>>> = mPriorityList

    private val mValidation = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidation

    private val mTask = MutableLiveData<TaskModel>()
    var task: LiveData<TaskModel> = mTask

    fun listPriorities() {
        mPriorityList.value = Main<List<PriorityModel>>().apply {
            this.status = true
            this.data = mPriorityRepository.list()
        }
    }

    fun save(task: TaskModel) {

        if (task.id == 0)
        mTaskRepository.create(task, object : APIListener<Boolean> {
            override fun onSucess(response: Boolean) {
                mValidation.value = ValidationListener()
            }

            override fun onError(erro: String) {
                mValidation.value = ValidationListener(erro)
            }

        })
        else{
            mTaskRepository.update(task, object : APIListener<Boolean> {
                override fun onSucess(response: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onError(erro: String) {
                    mValidation.value = ValidationListener(erro)
                }

            })
        }
    }

    fun load(taskId:Int){
        mTaskRepository.load(taskId, object : APIListener<TaskModel> {
            override fun onError(erro: String) {

            }

            override fun onSucess(response: TaskModel) {
                mTask.value = response
            }
        })
    }

}