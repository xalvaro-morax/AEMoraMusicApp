package com.example.ammoramusicapp.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ammoramusicapp.data.model.Album
import com.example.ammoramusicapp.ui.components.MiniPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    albumId: String,
    vm: DetailViewModel = viewModel()
) {
    val album by vm.album.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val errorMessage by vm.errorMessage.collectAsState()

    LaunchedEffect(albumId) {
        vm.loadAlbum(albumId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Fav", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1625))
            )
        },
        containerColor = Color(0xFF1A1625)
    ) { paddingValues ->
        when {
            isLoading -> Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = Color.White) }

            errorMessage != null -> Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) { Text(text = errorMessage ?: "", color = Color.Red) }

            album != null -> DetailContent(album!!, paddingValues)
        }
    }
}

@Composable
private fun DetailContent(album: Album, paddingValues: PaddingValues) {
    var isPlaying by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(bottom = 100.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    AsyncImage(model = album.image, contentDescription = album.title, modifier = Modifier.fillMaxSize())
                    Box(
                        modifier = Modifier.matchParentSize().background(
                            Brush.verticalGradient(listOf(Color.Transparent, Color(0xAA3B1966)))
                        )
                    )
                    Column(
                        Modifier.align(Alignment.BottomStart).padding(16.dp)
                    ) {
                        Text(album.title, color = Color.White, style = MaterialTheme.typography.headlineSmall)
                        Text(album.artist, color = Color.LightGray)
                        Spacer(Modifier.height(8.dp))
                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier.clip(RoundedCornerShape(50)).background(Color.White)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF3B1966))
                        }
                    }
                }
            }

            item {
                Text(
                    text = "About this album",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = album.description,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.LightGray
                )
            }

            items((1..10).toList()) { track ->
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF2A2535)
                ) {
                    Row(
                        Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = album.image,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("${album.title} • Track $track", color = Color.White)
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            MiniPlayer(
                album = album,
                isPlaying = isPlaying,
                onPlayPause = { isPlaying = !isPlaying },
                onClick = {}
            )
        }
    }
}
