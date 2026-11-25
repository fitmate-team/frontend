
package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.fitmate.presentation.designsystem.theme.FitMateFontFamily

@Composable
fun FitMateDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmText: String = "확인",
    dismissText: String = "취소",
    showDismissButton: Boolean = true
) {
    val dialogBlue = Color(0xFF3478F6)
    val dialogDark = Color(0xFF202532)
    val dialogMuted = Color(0xFF737B8C)

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = title,
                    fontFamily = FitMateFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = dialogDark
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = message,
                    fontFamily = FitMateFontFamily,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = dialogMuted
                )

                Spacer(Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (showDismissButton) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape = RoundedCornerShape(9.dp),
                            border = BorderStroke(1.dp, dialogBlue),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = dialogBlue
                            )
                        ) {
                            Text(
                                text = dismissText,
                                fontFamily = FitMateFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(9.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = dialogBlue
                        )
                    ) {
                        Text(
                            text = confirmText,
                            fontFamily = FitMateFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
