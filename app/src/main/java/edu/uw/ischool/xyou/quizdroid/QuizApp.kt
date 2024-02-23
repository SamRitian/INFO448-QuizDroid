package edu.uw.ischool.xyou.quizdroid

import android.app.Application
import android.util.Log

class QuizApp : Application() {
    private val TAG = "QuizApp"
    lateinit var topicRepository: TopicRepository
        private set
    override fun onCreate() {
        super.onCreate()
        topicRepository = InMemoryTopicRepository(this)
    }

    companion object {
        lateinit var instance: QuizApp
            private set
    }

    init {
        instance = this
        Log.i(TAG, "QuizApp instance created")
    }
}