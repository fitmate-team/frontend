package com.fitmate.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue700 = Color(0xFF3E78AD)
private val Gray200 = Color(0xFFE2E6EA)
private val Gray500 = Color(0xFF6C757D)
private val Gray900 = Color(0xFF16181A)
private val ScreenBg = Color(0xFFF8F9FA)

@Composable
fun SignupIntroScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로", tint = Gray900)
            }
            Text("회원가입", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Gray900)
        }

        Column(modifier = Modifier.weight(1f).padding(20.dp)) {
            Text("FitMate를\n시작해볼까요?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Gray900)
            Column(modifier = Modifier.padding(top = 30.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Column {
                    OutlinedTextField(value = id, onValueChange = { id = it }, placeholder = { Text("아이디") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    Text("아이디는 8~16자", fontSize = 12.sp, color = Gray500, modifier = Modifier.padding(top = 6.dp, start = 4.dp))
                }
                Column {
                    OutlinedTextField(value = password, onValueChange = { password = it }, placeholder = { Text("비밀번호") }, singleLine = true, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                    Text("비밀번호는 영문, 숫자를 포함해 10자 이상", fontSize = 12.sp, color = Gray500, modifier = Modifier.padding(top = 6.dp, start = 4.dp))
                }
                OutlinedTextField(value = passwordConfirm, onValueChange = { passwordConfirm = it }, placeholder = { Text("비밀번호 확인") }, singleLine = true, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            }
        }

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(containerColor = Blue700),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 24.dp).height(52.dp)
        ) {
            Text("다음", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
