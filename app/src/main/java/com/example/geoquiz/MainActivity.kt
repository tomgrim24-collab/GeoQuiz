package com.example.geoquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GeoQuizScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GeoQuizScreen(modifier: Modifier = Modifier) {
    // Вопросы и ответы
    data class Question(val text: String, val answer: Boolean)

    val questions = listOf(
        Question("Canberra is the capital of Australia.", true),
        Question("The Pacific Ocean is larger than the Atlantic Ocean.", true),
        Question("The Suez Canal connects the Red Sea and the Indian Ocean.", true),
        Question("The source of the Nile River is in Egypt.", false),
        Question("The Amazon River is the longest river in the Americas.", false),
        Question("Lake Baikal is the world's oldest and deepest freshwater lake.", true)
    )

    // Состояние
    var currentIndex by remember { mutableStateOf(0) }
    var isAnswered by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var answerMessage by remember { mutableStateOf("") }

    // Текущий вопрос
    val currentQuestion = questions[currentIndex]

    // Обработчики
    fun handleAnswer(userAnswer: Boolean) {
        if (userAnswer == currentQuestion.answer) {
            score++
            answerMessage = "Правильно! 👍"
        } else {
            answerMessage = "Неправильно! 👎"
        }
        isAnswered = true
    }

    fun goToNext() {
        if (currentIndex == questions.size - 1) {
            // Последний вопрос
            showResult = true
        } else {
            currentIndex++
            isAnswered = false
            answerMessage = ""
        }
    }
}