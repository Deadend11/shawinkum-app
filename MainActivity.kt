package com.shawinkum.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ShawinkumApp() }
    }
}

private val Ink = Color(0xFF10131A)
private val Panel = Color(0xFF1C2230)
private val Accent = Color(0xFFFF3158)

@Composable
fun ShawinkumApp() {
    var tab by remember { mutableStateOf(0) }
    var selectedVideo by remember { mutableStateOf<Uri?>(null) }
    val videoPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        selectedVideo = it
    }

    MaterialTheme(colorScheme = darkColorScheme(
        background = Ink, surface = Panel, primary = Accent,
        onBackground = Color.White, onSurface = Color.White
    )) {
        Scaffold(
            containerColor = Ink,
            bottomBar = {
                NavigationBar(containerColor = Panel) {
                    listOf(Icons.Default.Home, Icons.Default.Search, Icons.Default.AddCircle,
                        Icons.Default.Favorite, Icons.Default.Person).forEachIndexed { i, icon ->
                        NavigationBarItem(
                            selected = tab == i,
                            onClick = { tab = i },
                            icon = { Icon(icon, contentDescription = null) },
                            label = { Text(listOf("الرئيسية", "بحث", "نشر", "الإعجابات", "حسابي")[i]) }
                        )
                    }
                }
            }
        ) { padding ->
            when (tab) {
                0 -> HomeScreen(padding, selectedVideo)
                1 -> SimpleScreen(padding, "اكتشف", "ابحث عن فيديوهات وهاشتاكات ومبدعين")
                2 -> {
                    LaunchedEffect(Unit) { videoPicker.launch(arrayOf("video/*")) }
                    SimpleScreen(padding, "نشر فيديو", selectedVideo?.lastPathSegment ?: "اختَر فيديو من جهازك")
                }
                3 -> SimpleScreen(padding, "الإعجابات", "الفيديوهات التي أعجبتك ستظهر هنا")
                else -> ProfileScreen(padding)
            }
        }
    }
}

@Composable
private fun HomeScreen(padding: PaddingValues, video: Uri?) {
    LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
        item {
            Text("شاوينكم", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("فيديوهات قصيرة… بطريقتك", color = Color.LightGray)
            Spacer(Modifier.height(18.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(18.dp)) {
                    Text("أهلاً بيك 👋", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text("هذا إصدار تجريبي أولي. تگدر تختار فيديو من تبويب نشر.")
                    if (video != null) {
                        Spacer(Modifier.height(8.dp))
                        Text("آخر فيديو مختار: ${video.lastPathSegment ?: "فيديو من الجهاز"}", color = Color(0xFFFFA6B6))
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            Text("لك", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            repeat(3) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Panel),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Box(Modifier.fillMaxWidth().height(170.dp).background(Color(0xFF293449), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PlayCircle, null, tint = Color.White, modifier = Modifier.size(54.dp))
                        }
                        Spacer(Modifier.height(10.dp))
                        Text("مثال فيديو ${index + 1}", fontWeight = FontWeight.Bold)
                        Text("مكان عرض الفيديوهات راح يرتبط بالخادم بمرحلة لاحقة.", color = Color.LightGray, fontSize = 13.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FavoriteBorder, null, tint = Accent)
                            Text("  إعجاب")
                            Spacer(Modifier.width(20.dp))
                            Icon(Icons.Default.ChatBubbleOutline, null)
                            Text("  تعليق")
                            Spacer(Modifier.width(20.dp))
                            Icon(Icons.Default.Share, null)
                            Text("  مشاركة")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen(padding: PaddingValues) {
    Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
        Text("حسابي", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Box(Modifier.fillMaxWidth().height(118.dp).background(
            Color(0xFF34445C), RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) {
            Text("مساحة الغلاف", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(14.dp))
        Text("مستخدم شاوينكم", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("@shawinkum_user", color = Color.LightGray)
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Stat("0", "متابعون"); Stat("0", "يتابع"); Stat("0", "إعجابات")
        }
        Spacer(Modifier.height(20.dp))
        Text("الفيديوهات", fontWeight = FontWeight.Bold)
        Text("ماكو فيديوهات منشورة حالياً", color = Color.LightGray)
    }
}

@Composable
private fun Stat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = Color.LightGray, fontSize = 12.sp)
    }
}

@Composable
private fun SimpleScreen(padding: PaddingValues, title: String, subtitle: String) {
    Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.Center) {
        Text(title, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        Text(subtitle, color = Color.LightGray)
    }
}
