package com.fitmate.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue700 = Color(0xFF3E78AD)
private val Gray200 = Color(0xFFE2E6EA)
private val Gray300 = Color(0xFFA7AEB3)
private val Gray500 = Color(0xFF6C757D)
private val Gray900 = Color(0xFF16181A)
private val ScreenBg = Color(0xFFF8F9FA)

@Composable
fun LoginScreen(
    onNavigateToSignup: () -> Unit,
    onLoginSuccess: () -> Unit,
    onFindAccount: () -> Unit = {}
) {
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg)
            .padding(horizontal = 20.dp, vertical = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .size(100.dp)
                .background(Gray200, RoundedCornerShape(12.dp))
                .border(1.dp, Gray300, RoundedCornerShape(12.dp))
        ) {}

        Text(
            text = "FitMate",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Gray900,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            text = "나에게 맞는 운동을 시작해보세요.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Gray500,
            modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
        )

        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Gray500) },
            placeholder = { Text("아이디") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = androidx.compose.ui.graphics.Color.White, focusedContainerColor = androidx.compose.ui.graphics.Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Gray500) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Gray500
                    )
                }
            },
            placeholder = { Text("비밀번호") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = androidx.compose.ui.graphics.Color.White, focusedContainerColor = androidx.compose.ui.graphics.Color.White),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onLoginSuccess,
            colors = ButtonDefaults.buttonColors(containerColor = Blue700),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(52.dp)
        ) {
            Text("로그인", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        TextButton(onClick = onFindAccount, modifier = Modifier.padding(top = 8.dp)) {
            Text("아이디/비밀번호 찾기", color = Gray500, fontSize = 14.sp)
        }

        HorizontalDivider(color = Gray200, modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("아직 회원이 아니신가요?", color = Gray500, fontSize = 14.sp)
        }
        TextButton(onClick = onNavigateToSignup) {
            Text("회원가입", color = Blue700, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
