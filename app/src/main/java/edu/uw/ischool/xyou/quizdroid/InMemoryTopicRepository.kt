package edu.uw.ischool.xyou.quizdroid

import android.content.Context
import android.util.Log
import org.json.JSONArray
import java.io.File
import java.io.IOException

class InMemoryTopicRepository(context: Context) : TopicRepository {
    private val TAG = "InMemoryTopicRepository"
    private val topics: List<Topic> = initTopics(context)

    private fun initTopics(context: Context): List<Topic> {
        // read from internal storage
        val jsonString = readSavedData(context)

        return parseQuizTopics(jsonString)
    }

    private fun readSavedData(context: Context):String {
        val filename = "new_questions.json"
        val jsonString = context.assets.open("data/questions.json").bufferedReader().use { it.readText() }

        val file = File(context.getFilesDir(), filename)
        if (!file.exists()) {
            Log.i(TAG, "File does not exist")
            file.createNewFile()
            file.writeText(jsonString)
        }

        return try {
             context.openFileInput(filename).use { inputStream ->
                inputStream.bufferedReader().use { it.readText() }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error reading from file", e)
            ""
        }
    }

    private fun parseQuizTopics(jsonString: String): List<Topic> {
        // handle topics
        val topics = mutableListOf<Topic>()
        val topicArray = JSONArray(jsonString)

        for (i in 0 until topicArray.length()) {
            val topicObject = topicArray.getJSONObject(i)

            val name = topicObject.getString("title")
            val shortDescription = topicObject.getString("desc")
            val longDescription = topicObject.getString("desc")

            // handle questions
            val questions = mutableListOf<Question>()
            val questionArray = topicObject.getJSONArray("questions")
            for (j in 0 until questionArray.length()) {
                val questionObject = questionArray.getJSONObject(j)

                val questionText = questionObject.getString("text")
                val correctAnswerIndex = questionObject.getInt("answer")

                // handle options
                val options = mutableListOf<String>()
                val optionsArray = questionObject.getJSONArray("answers")
                for (k in 0 until optionsArray.length()) {
                    options.add(optionsArray.getString(k))
                }

                questions.add(Question(questionText, options, correctAnswerIndex))
            }

            topics.add(Topic(name, shortDescription, longDescription, questions))
        }

        Log.i(TAG, "$topics")

        return topics
    }

    override fun getTopics(): List<Topic> = topics

    override fun getTopicByName(name: String): Topic {
        return topics.find { it.name == name } ?: throw IllegalArgumentException("No such topic")
    }
}