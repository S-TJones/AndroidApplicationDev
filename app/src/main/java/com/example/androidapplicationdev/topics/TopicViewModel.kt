package com.example.androidapplicationdev.topics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TopicRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allTopics: LiveData<List<Topics>>

    init {
        val topicsDao = TopicRoomDatabase.getDatabase(application, viewModelScope).topicDao()
        repository = TopicRepository(topicsDao)
        allTopics = repository.allWords
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(topic: Topics) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(topic)
    }
}