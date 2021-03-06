package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.Main
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository = TaskRepository(application)
    private var mTaskFilter = 0
    private val mTaskList = MutableLiveData<Main<List<TaskModel>>>()
    var tasks: LiveData<Main<List<TaskModel>>> = mTaskList

    private val mValidation = MutableLiveData<Main<Unit>>()
    var validation: LiveData<Main<Unit>> = mValidation

    fun undo(id: Int) {
        mTaskRepository.undo(id, object : APIListener<Boolean> {
            override fun onSucess(response: Boolean) {
                list(mTaskFilter)

            }

            override fun onError(msg: String) {
                mValidation.value = Main<Unit>().apply {
                    this.message = msg
                    this.status = false
                }
            }

        })
    }

    fun complete(id: Int) {
        mTaskRepository.complete(id, object : APIListener<Boolean> {
            override fun onSucess(response: Boolean) {
                list(mTaskFilter)
            }

            override fun onError(erro: String) {
                mValidation.value = Main<Unit>().apply {
                    this.message = erro
                    this.status = false
                }
            }
        })
    }


    fun delete(id: Int) {
        mTaskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSucess(response: Boolean) {
                list(mTaskFilter)
                mValidation.value = Main<Unit>().apply {
                    this.status = true
                }
            }

            override fun onError(erro: String) {
                mValidation.value = Main<Unit>().apply {
                    this.message = erro
                    this.status = false
                }
            }
        })
    }

    fun list(taskFilter: Int) {
        mTaskFilter = taskFilter

        val listener = object : APIListener<List<TaskModel>> {
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
        }

        if (mTaskFilter == TaskConstants.FILTER.ALL) {
            mTaskRepository.all(listener)

        } else if (mTaskFilter == TaskConstants.FILTER.NEXT) {
            mTaskRepository.nextWeek(listener)
        } else if (mTaskFilter == TaskConstants.FILTER.EXPIRED) {
            mTaskRepository.overDue(listener)
        }
    }
}