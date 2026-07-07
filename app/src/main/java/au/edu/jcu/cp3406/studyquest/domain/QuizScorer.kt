package au.edu.jcu.cp3406.studyquest.domain

import au.edu.jcu.cp3406.studyquest.model.AnswerResult
import au.edu.jcu.cp3406.studyquest.model.Question
import javax.inject.Inject

class QuizScorer @Inject constructor() {
    fun score(question: Question, selectedAnswer: String): AnswerResult = AnswerResult(
        question = question,
        selectedAnswer = selectedAnswer,
        isCorrect = selectedAnswer == question.correctAnswer,
        explanation = if (selectedAnswer == question.correctAnswer) {
            "Correct. ${question.explanation}"
        } else {
            "Not quite. The correct answer is ${question.correctAnswer}. ${question.explanation}"
        },
    )
}

