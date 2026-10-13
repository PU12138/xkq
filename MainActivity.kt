package com.pu12138.harmonicascore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class Song(val title: String, val artist: String, val key: String, val notes: String, val tags: String)
private val songs = listOf(
    Song("小星星", "传统儿歌", "C 调", "1 1 5 5 | 6 6 5 -\n4 4 3 3 | 2 2 1 -\n5 5 4 4 | 3 3 2 -\n5 5 4 4 | 3 3 2 -\n1 1 5 5 | 6 6 5 -\n4 4 3 3 | 2 2 1 -", "入门 儿歌 童谣"),
    Song("欢乐颂", "贝多芬", "C 调", "3 3 4 5 | 5 4 3 2 | 1 1 2 3 | 3 2 2 -\n3 3 4 5 | 5 4 3 2 | 1 1 2 3 | 2 1 1 -", "古典 入门"),
    Song("生日快乐", "传统歌曲", "F 调示例", "5 5 6 5 | 1̅ 7 -\n5 5 6 5 | 2̅ 1̅ -", "生日 祝福")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HarmonicaApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HarmonicaApp() {
    var query by remember { mutableStateOf("") }
    var exact by remember { mutableStateOf(false) }
    var type by remember { mutableStateOf("十孔布鲁斯") }
    var selected by remember { mutableStateOf<Song?>(null) }
    var saved by remember { mutableStateOf(setOf<String>()) }
    var showFavorites by remember { mutableStateOf(false) }
    val filtered = songs.filter { song ->
        val haystack = "${song.title} ${song.artist} ${song.key} ${song.tags}".lowercase()
        val q = query.trim().lowercase()
        (q.isBlank() || if (exact) song.title.lowercase() == q || song.artist.lowercase() == q else haystack.contains(q)) && (!showFavorites || saved.contains(song.title))
    }
    MaterialTheme(colorScheme = lightColorScheme(primary = androidx.compose.ui.graphics.Color(0xFF176B5B), secondary = androidx.compose.ui.graphics.Color(0xFFB96E37))) {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(selected?.title ?: "口琴谱大全") },
                navigationIcon = { if (selected != null) IconButton(onClick = { selected = null }) { Icon(Icons.Default.ArrowBack, "返回") } },
                actions = { IconButton(onClick = { showFavorites = !showFavorites; selected = null }) { Icon(Icons.Default.Favorite, "收藏") } }
            )
        }) { padding ->
            if (selected != null) {
                val song = selected!!
                Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(song.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("${song.artist} · ${song.key} · 示例曲谱", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("口琴类型", fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("十孔布鲁斯", "半音阶", "复音口琴").forEach { item ->
                            FilterChip(selected = type == item, onClick = { type = item }, label = { Text(item) })
                        }
                    }
                    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                        Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("简谱", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(song.notes, style = MaterialTheme.typography.headlineSmall)
                            HorizontalDivider()
                            Text("当前口琴：$type", style = MaterialTheme.typography.bodyMedium)
                            Text("注意：此原型展示数字简谱；吹吸孔位映射和移调转换尚未实现。", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Button(onClick = { saved = if (saved.contains(song.title)) saved - song.title else saved + song.title }, modifier = Modifier.fillMaxWidth()) {
                        Icon(if (saved.contains(song.title)) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (saved.contains(song.title)) "取消收藏" else "收藏曲谱")
                    }
                }
            } else {
                Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
                    Text("搜索你想吹奏的歌曲", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 8.dp, bottom = 12.dp))
                    OutlinedTextField(value = query, onValueChange = { query = it; showFavorites = false }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("输入歌名、歌手或关键词") }, leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true, shape = RoundedCornerShape(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = !exact, onClick = { exact = false }); Text("模糊")
                            RadioButton(selected = exact, onClick = { exact = true }); Text("精准")
                        }
                        Text("${filtered.size} 首", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(if (showFavorites) "我的收藏" else "曲谱示例", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
                        items(filtered) { song ->
                            Card(onClick = { selected = song }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(48.dp)) {
                                        Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.MusicNote, null, tint = MaterialTheme.colorScheme.primary) }
                                    }
                                    Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                                        Text(song.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        Text("${song.artist} · ${song.key}", style = MaterialTheme.typography.bodySmall)
                                        Text(song.tags, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    IconButton(onClick = { saved = if (saved.contains(song.title)) saved - song.title else saved + song.title }) {
                                        Icon(if (saved.contains(song.title)) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "收藏")
                                    }
                                }
                            }
                        }
                        if (filtered.isEmpty()) item { Text("没有找到匹配的曲谱。试试歌名或关键词。", modifier = Modifier.padding(20.dp), color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    }
                    Text("当前为离线示例曲库，后续可接入合法曲谱数据。", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp))
                }
            }
        }
    }
}
