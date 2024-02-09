package modal

data class Topic (
    val name: String,
    val description: String,
    val questions: List<Question>
)

data class Question (
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

// populate data here
// model: topic details
val topics = listOf<Topic>(
    Topic(
        name = "Math",
        description = "Mathematical quizzes covering various topics.",
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
        description = "Quizzes about physical laws and phenomena.",
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
        description = "Quizzes about Marvel characters and their stories.",
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
        description = "Quizzes about Android app development.",
        questions = listOf(
            Question(
                questionText = "What is the programming language used for Android app development in INFO 448?",
                options = listOf("Java", "Kotlin", "Swift", "Objective-C"),
                correctAnswerIndex = 1
            )
            // Add more questions as needed
        )
    )
)