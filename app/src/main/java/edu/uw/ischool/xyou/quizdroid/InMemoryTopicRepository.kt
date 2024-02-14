package edu.uw.ischool.xyou.quizdroid

class InMemoryTopicRepository : TopicRepository {
    private val topics: List<Topic> = initTopics()

    private fun initTopics(): List<Topic> {
        return listOf<Topic>(
            Topic(
                name = "Math",
                shortDescription = "Mathematical quizzes.",
                longDescription = "Quizzes about basic arithmetic, algebra, geometry, and more.",
                questions = listOf(
                    Question(
                        questionText = "What is 2 + 2?",
                        options = listOf("3", "4", "5", "6"),
                        correctAnswerIndex = 1
                    ),
                    Question(
                        questionText = "What is 3 * 5?",
                        options = listOf("8", "12", "15", "18"),
                        correctAnswerIndex = 2
                    ),
                    Question(
                        questionText = "What is 10 - 7?",
                        options = listOf("1", "2", "3", "4"),
                        correctAnswerIndex = 2
                    )
                )
            ),
            Topic(
                name = "Physics",
                shortDescription = "Physics quizzes.",
                longDescription = "Quizzes about forces, energy, motion, and more.",
                questions = listOf(
                    Question(
                        questionText = "What force pulls objects toward Earth?",
                        options = listOf("Magnetism", "Gravity", "Friction", "Tension"),
                        correctAnswerIndex = 1
                    ),
                    Question(
                        questionText = "What is the unit of force?",
                        options = listOf("Newton", "Joule", "Watt", "Volt"),
                        correctAnswerIndex = 0
                    )
                )
            ),
            Topic(
                name = "Marvel Super Heroes",
                shortDescription = "Marvel superheroes quizzes.",
                longDescription = "Quizzes about Marvel characters and their stories.",
                questions = listOf(
                    Question(
                        questionText = "What is the real name of Thor?",
                        options = listOf("Tony Stark", "Steve Rogers", "Bruce Banner", "Thor Odinson"),
                        correctAnswerIndex = 3
                    ),
                    Question(
                        questionText = "What is the real name of Captain America?",
                        options = listOf("Tony Stark", "Steve Rogers", "Bruce Banner", "Thor Odinson"),
                        correctAnswerIndex = 1
                    )
                )
            ),
            Topic(
                name = "Android Programming",
                shortDescription = "Android programming quizzes.",
                longDescription = "Quizzes about Android app development using Java and Kotlin.",
                questions = listOf(
                    Question(
                        questionText = "What is the programming language used for Android app development in INFO 448?",
                        options = listOf("Java", "Kotlin", "Swift", "Objective-C"),
                        correctAnswerIndex = 1
                    )
                )
            )
        )
    }
    override fun getTopics(): List<Topic> = topics

    override fun getTopicByName(name: String): Topic {
        return topics.find { it.name == name } ?: throw IllegalArgumentException("No such topic")
    }
}