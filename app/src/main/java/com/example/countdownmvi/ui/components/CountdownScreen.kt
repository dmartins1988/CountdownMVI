package com.example.countdownmvi.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.countdownmvi.presentation.CountDownIntent
import com.example.countdownmvi.presentation.CountdownViewModel

@Composable
fun CountdownScreen(
    modifier: Modifier = Modifier,
    viewModel: CountdownViewModel
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val displayRemainingSeconds = state.value.displayTime

            CircularProgressIndicator(
                progress = { state.value.progressPercentage },
            )
            Text(
                text = "$displayRemainingSeconds",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                if (state.value.isRunning) {
                    Button(onClick = { viewModel.dispatchIntent(CountDownIntent.Pause) }) {
                        Text("Pause")
                    }
                } else {
                    Button(onClick = { viewModel.dispatchIntent(CountDownIntent.Start) }) {
                        Text("Start")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { viewModel.dispatchIntent(CountDownIntent.Reset) }) {
                    Text("Reset")
                }
            }
        }
    }
}