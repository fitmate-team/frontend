
package com.fitmate.presentation.workout.components

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.fitmate.presentation.designsystem.theme.*

@Composable
fun WorkoutGuideContent(
    guide: String,
    bodyParts: List<String>,
    videoUrl: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val context = LocalContext.current

    val videoId = remember(videoUrl) {
        extractYouTubeVideoId(videoUrl)
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "운동 가이드 영상",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(346f / 192f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFE9EEF3)),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    VideoLoading()
                }

                videoId != null -> {
                    YouTubeVideoPlayer(
                        videoId = videoId,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    Text(
                        text = if (videoUrl.isBlank()) {
                            "등록된 운동 영상이 없어요."
                        } else {
                            "지원하지 않는 유튜브 주소예요."
                        },
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        if (!isLoading && videoId != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "영상이 재생되지 않나요? 유튜브에서 보기",
                fontSize = 12.sp,
                color = Blue600,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        try {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    "https://www.youtube.com/watch?v=$videoId"
                                )
                            )

                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Log.e(
                                "FitMateVideo",
                                "유튜브 열기 실패",
                                e
                            )
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "운동 가이드",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = BorderDefault,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = Color(0xFFF8F9FA),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 14.dp
                )
        ) {
            Text(
                text = if (isLoading && guide.isBlank()) {
                    "운동 가이드를 불러오는 중..."
                } else {
                    guide.ifBlank {
                        "등록된 운동 가이드가 없어요."
                    }
                },
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "주요 운동 부위",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            bodyParts.forEach { part ->
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = BorderDefault,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(
                            horizontal = 18.dp,
                            vertical = 10.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = part,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun VideoLoading() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(30.dp),
            color = Blue600,
            strokeWidth = 3.dp
        )

        Text(
            text = "운동 영상을 불러오는 중...",
            fontSize = 13.sp,
            color = TextSecondary
        )
    }
}

@Composable
private fun YouTubeVideoPlayer(
    videoId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var isLoading by remember(videoId) {
        mutableStateOf(true)
    }

    var hasError by remember(videoId) {
        mutableStateOf(false)
    }

    val webView = remember(context, videoId) {
        WebView(context).apply {

            setBackgroundColor(
                android.graphics.Color.BLACK
            )

            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = true

            webChromeClient = WebChromeClient()

            webViewClient = object : WebViewClient() {

                override fun onPageFinished(
                    view: WebView?,
                    url: String?
                ) {
                    Log.d(
                        "FitMateVideo",
                        "페이지 로딩 완료: $url"
                    )

                    isLoading = false
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    if (request?.isForMainFrame == true) {
                        Log.e(
                            "FitMateVideo",
                            "WebView 오류: ${error?.description}"
                        )

                        isLoading = false
                        hasError = true
                    }
                }
            }

            val watchUrl =
                "https://m.youtube.com/watch?v=$videoId"

            Log.d(
                "FitMateVideo",
                "모바일 유튜브 페이지 로딩: $watchUrl"
            )

            loadUrl(watchUrl)
        }
    }

    DisposableEffect(webView) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { webView },
            modifier = Modifier.fillMaxSize()
        )

        if (isLoading && !hasError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE9EEF3)),
                contentAlignment = Alignment.Center
            ) {
                VideoLoading()
            }
        }

        if (hasError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE9EEF3)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "영상을 불러오지 못했어요.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

private fun extractYouTubeVideoId(
    url: String
): String? {
    if (url.isBlank()) return null

    return try {
        val uri = Uri.parse(url.trim())

        val host = uri.host
            ?.lowercase()
            ?.removePrefix("www.")
            ?.removePrefix("m.")
            ?: return null

        val id = when (host) {

            "youtu.be" -> {
                uri.pathSegments.firstOrNull()
            }

            "youtube.com",
            "youtube-nocookie.com",
            "music.youtube.com" -> {
                when (uri.pathSegments.firstOrNull()) {

                    "watch" -> {
                        uri.getQueryParameter("v")
                    }

                    "shorts",
                    "embed",
                    "live" -> {
                        uri.pathSegments.getOrNull(1)
                    }

                    else -> null
                }
            }

            else -> null
        }

        id?.takeIf {
            it.matches(
                Regex("[A-Za-z0-9_-]{11}")
            )
        }

    } catch (e: Exception) {
        null
    }
}
