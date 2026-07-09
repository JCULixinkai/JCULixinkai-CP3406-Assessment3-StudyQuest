package au.edu.jcu.cp3406.studyquest.domain

import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.Question
import au.edu.jcu.cp3406.studyquest.model.StudyCategories

object SeedQuestions {
    fun all(): List<Question> = listOf(
        seed(
            categoryId = "general",
            difficulty = Difficulty.Easy,
            question = "Which study habit is most useful for long-term memory?",
            answers = listOf("Cramming once", "Spaced repetition", "Skipping review", "Only rereading notes"),
            correct = "Spaced repetition",
            explanation = "Spaced repetition strengthens recall by reviewing material over increasing intervals.",
        ),
        seed(
            categoryId = "science",
            difficulty = Difficulty.Medium,
            question = "What is a hypothesis in scientific learning?",
            answers = listOf("A final answer", "A testable explanation", "A random guess", "A measurement tool"),
            correct = "A testable explanation",
            explanation = "A hypothesis is useful because evidence can support or challenge it.",
        ),
        seed(
            categoryId = "computers",
            difficulty = Difficulty.Easy,
            question = "What does a database help an app do?",
            answers = listOf("Only draw icons", "Store and query structured data", "Change screen brightness", "Remove all errors"),
            correct = "Store and query structured data",
            explanation = "Databases such as Room help apps keep structured local records like scores and progress.",
        ),
        seed(
            categoryId = "maths",
            difficulty = Difficulty.Medium,
            question = "If a student answers 8 out of 10 questions correctly, what is the accuracy?",
            answers = listOf("8%", "18%", "80%", "100%"),
            correct = "80%",
            explanation = "Accuracy is correct answers divided by total answers, so 8 / 10 = 80%.",
        ),
        seed(
            categoryId = "history",
            difficulty = Difficulty.Easy,
            question = "Why is it useful to connect facts with context?",
            answers = listOf("It makes facts harder to remember", "It supports deeper understanding", "It removes evidence", "It replaces practice"),
            correct = "It supports deeper understanding",
            explanation = "Context helps students understand why facts matter and how events relate to each other.",
        ),
        seed(
            categoryId = "computers",
            difficulty = Difficulty.Medium,
            question = "Which principle best protects student learning data?",
            answers = listOf("Collect everything", "Store data locally when possible", "Hide all settings", "Make deletion impossible"),
            correct = "Store data locally when possible",
            explanation = "Local storage reduces unnecessary sharing and supports privacy by design.",
        ),
        seed(
            categoryId = "general",
            difficulty = Difficulty.Medium,
            question = "Which method checks learning without looking at notes first?",
            answers = listOf("Active recall", "Copying text", "Changing the font", "Skipping hard topics"),
            correct = "Active recall",
            explanation = "Active recall asks the learner to retrieve knowledge before checking the answer.",
        ),
        seed(
            categoryId = "science",
            difficulty = Difficulty.Easy,
            question = "In an experiment, what is a variable?",
            answers = listOf("Something that can change", "A fixed conclusion", "A finished report", "A type of calculator"),
            correct = "Something that can change",
            explanation = "Variables are factors that can change and may affect the result of an experiment.",
        ),
        seed(
            categoryId = "computers",
            difficulty = Difficulty.Medium,
            question = "Why do many mobile apps use JSON from an API?",
            answers = listOf("It hides the app name", "It transfers structured data", "It deletes local files", "It replaces the UI"),
            correct = "It transfers structured data",
            explanation = "JSON is a common format for sending structured information between a server and an app.",
        ),
        seed(
            categoryId = "maths",
            difficulty = Difficulty.Easy,
            question = "What is the mean of 4, 6, and 8?",
            answers = listOf("4", "6", "8", "18"),
            correct = "6",
            explanation = "The mean is the total divided by the number of values: 18 / 3 = 6.",
        ),
        seed(
            categoryId = "history",
            difficulty = Difficulty.Medium,
            question = "What is a primary source in history?",
            answers = listOf("A source from the time being studied", "A modern summary only", "A random opinion", "A fictional scene"),
            correct = "A source from the time being studied",
            explanation = "Primary sources are original evidence from the period or event being investigated.",
        ),
        seed(
            categoryId = "maths",
            difficulty = Difficulty.Medium,
            question = "If a score rises from 12 to 15, how much did it increase?",
            answers = listOf("2", "3", "12", "27"),
            correct = "3",
            explanation = "The increase is the new value minus the old value: 15 - 12 = 3.",
        ),
    )

    private fun seed(
        categoryId: String,
        difficulty: Difficulty,
        question: String,
        answers: List<String>,
        correct: String,
        explanation: String,
    ): Question {
        val category = StudyCategories.byId(categoryId)
        return Question(
            source = "seed",
            categoryId = category.id,
            categoryName = category.label,
            difficulty = difficulty,
            questionText = question,
            answers = answers,
            correctAnswer = correct,
            explanation = explanation,
        )
    }
}
