package com.example.gigmarket.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.gigmarket.ui.theme.NeonCyan
import com.example.gigmarket.ui.theme.NeonPurple
import com.example.gigmarket.ui.theme.TextSecondary

@Composable
fun NeonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    maxLength: Int? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderGlow by animateFloatAsState(
        targetValue = if (isFocused) 1f else 0.3f,
        animationSpec = tween(durationMillis = 300),
        label = "borderGlow"
    )

    val shadowElevation by animateDpAsState(
        targetValue = if (isFocused) 15.dp else 5.dp,
        animationSpec = tween(durationMillis = 300),
        label = "shadow"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = shadowElevation,
                shape = RoundedCornerShape(16.dp),
                ambientColor = NeonPurple.copy(alpha = borderGlow * 0.5f),
                spotColor = NeonCyan.copy(alpha = borderGlow * 0.5f)
            )
            .background(
                color = Color.Black.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonCyan.copy(alpha = borderGlow),
                        NeonPurple.copy(alpha = borderGlow)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
    ) {
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                if (maxLength == null || newValue.length <= maxLength) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White
            ),
            cursorBrush = SolidColor(NeonCyan),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary.copy(alpha = 0.6f)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
