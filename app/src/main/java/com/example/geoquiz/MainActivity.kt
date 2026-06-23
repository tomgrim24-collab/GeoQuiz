package com.example.geoquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.graphics.Color
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
    // Данные вопросов
    data class Question(val text: String, val answer: Boolean)

    val questions = listOf(
        Question("Canberra is the capital of Australia.", true),
        Question("The Pacific Ocean is larger than the Atlantic Ocean.", true),
        Question("The Suez Canal connects the Red Sea and the Indian Ocean.", true),
        Question("The source of the Nile River is in Egypt.", false),
        Question("The Amazon River is the longest river in the Americas.", false),
        Question("Lake Baikal is the world's oldest and deepest freshwater lake.", true)
    )

    // Состояния
    var currentIndex by remember { mutableStateOf(0) }
    var isAnswered by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var answerMessage by remember { mutableStateOf("") }

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
            showResult = true
        } else {
            currentIndex++
            isAnswered = false
            answerMessage = ""
        }
    }

    // Основной UI
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Счётчик вопросов
        Text(
            text = "Вопрос ${currentIndex + 1} из ${questions.size}",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Карточка с вопросом
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = currentQuestion.text,
                modifier = Modifier.padding(24.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Сообщение о результате ответа
        if (answerMessage.isNotEmpty()) {
            Text(
                text = answerMessage,
                fontSize = 18.sp,
                color = if (answerMessage.contains("Правильно"))
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Кнопки ответа
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { handleAnswer(true) },
                modifier = Modifier.weight(1f),
                enabled = !isAnswered && !showResult
            ) {
                Text("True")
            }
            Button(
                onClick = { handleAnswer(false) },
                modifier = Modifier.weight(1f),
                enabled = !isAnswered && !showResult
            ) {
                Text("False")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Следующий"
        Button(
            onClick = { goToNext() },
            modifier = Modifier.fillMaxWidth(),
            enabled = isAnswered && !showResult
        ) {
            Text(if (currentIndex == questions.size - 1) "Завершить" else "Следующий")
        }
    }

    // Диалог с результатом
    if (showResult) {
        AlertDialog(
            onDismissRequest = { /* Не закрывается */ },
            title = { Text("Результат теста") },
            text = {
                Column {
                    Text("Правильных ответов: $score из ${questions.size}")
                    Spacer(modifier = Modifier.height(8.dp))
                    val percentage = (score.toDouble() / questions.size * 100).toInt()
                    Text(
                        text = "Результат: $percentage%",
                        fontSize = 18.sp
                    )
                    if (percentage >= 80) {
                        Text("Отлично! 🎉", color = Color(0xFF4CAF50))
                    } else if (percentage >= 60) {
                        Text("Хорошо! 😊", color = Color(0xFFFF9800))
                    } else {
                        Text("Попробуйте ещё раз! 💪", color = Color(0xFFF44336))
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Сброс теста
                        currentIndex = 0
                        isAnswered = false
                        score = 0
                        showResult = false
                        answerMessage = ""
                    }
                ) {
                    Text("Пройти заново")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GeoQuizPreview() {
    MaterialTheme {
        GeoQuizScreen()
    }
}