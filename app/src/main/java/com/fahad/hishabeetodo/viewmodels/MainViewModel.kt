package com.fahad.hishabeetodo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fahad.hishabeetodo.db.TodoEntity
import com.fahad.hishabeetodo.interfaces.TodoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val todoDao: TodoDao) : ViewModel() {

    var dataList: MutableLiveData<List<TodoEntity>> = MutableLiveData(listOf())
    fun getAllData(): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            dataList.postValue(todoDao.getAll())
        }
    }

    fun saveData(channel: TodoEntity): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            todoDao.insert(channel)
        }
    }

    fun updateData(note_txt: String, id: Long): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            todoDao.updateText(note_txt, id)
        }
    }

    fun updateIsChecked(isChecked: Boolean, id: Long): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            todoDao.updateBool(isChecked, id)
        }
    }

    fun deleteData(id: Long): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            todoDao.delete(id)
        }
    }

}