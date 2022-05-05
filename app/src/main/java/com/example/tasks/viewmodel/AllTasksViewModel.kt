package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.Main
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository = TaskRepository(application)

    private val mTaskList = MutableLiveData<Main<List<TaskModel>>>()
    var tasks: LiveData<Main<List<TaskModel>>> = mTaskList

     fun all() {
        mTaskRepository.all(object : APIListener<List<TaskModel>> {
            override fun onSucess(response: List<TaskModel>) {
                mTaskList.value = Main<List<TaskModel>>().apply {
                    this.status = true
                    this.data = response
                }
            }
            override fun onError(erro: String) {
                mTaskList.value = Main<List<TaskModel>>().apply {
                    this.status = false
                    this.message = erro
                }
            }
        })

    }
}