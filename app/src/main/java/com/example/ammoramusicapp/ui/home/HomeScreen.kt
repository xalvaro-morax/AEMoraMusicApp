package com.example.ammoramusicapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ammoramusicapp.data.model.Album
import com.example.ammoramusicapp.ui.components.MiniPlayer

@Composable
fun HomeScreen(
    navController: NavController,
    vm: HomeViewModel = viewModel()
) {
    val albums by vm.albums.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val errorMessage by vm.errorMessage.collectAsState()

    var selectedAlbum by remember { mutableStateOf<Album?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF1A1625)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Color.White) }

                errorMessage != null -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text(text = errorMessage ?: "", color = Color.Red) }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 100.dp)
                    ) {

                        item { HeaderSection() }


                        item { SectionTitle("Albums") {} }

                        item {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                items(albums) { album ->
                                    AlbumCard(
                                        album = album,
                                        onPlay = {
                                            selectedAlbum = album
                                            isPlaying = true
                                        },
                                        onClick = {
                                            navController.navigate("detail/${album.id}")
                                        }
                                    )
                                }
                            }
                        }


                        item { SectionTitle("Recently Played") {} }

                        items(albums) { album ->
                            RecentlyPlayedItem(
                                album = album,
                                onClick = {
                                    selectedAlbum = album
                                    isPlaying = true
                                }
                            )
                        }
                    }
                }
            }


            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                if (selectedAlbum != null) {
                    MiniPlayer(
                        album = selectedAlbum!!,
                        isPlaying = isPlaying,
                        onPlayPause = { isPlaying = !isPlaying },
                        onClick = { navController.navigate("detail/${selectedAlbum!!.id}") }
                    )
                } else {

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .clip(RoundedCornerShape(50.dp)),
                        color = Color(0xFF2A2535),
                        shadowElevation = 10.dp
                    ) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Selecciona un álbum para reproducir 🎧",
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF7C4DFF), Color(0xFF6200EE))
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "Good Morning,",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Álvaro Mora",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}


@Composable
private fun SectionTitle(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "See all",
            color = Color(0xFF7C4DFF),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.clickable { onClick() }
        )
    }
}


@Composable
private fun AlbumCard(
    album: Album,
    onPlay: () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .padding(end = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = album.image,
            contentDescription = album.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0xAA3B1966))
                    )
                )
        )
        Column(
            Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(album.title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(album.artist, color = Color.LightGray)
        }
        IconButton(
            onClick = onPlay,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF3B1966))
        }
    }
}


@Composable
private fun RecentlyPlayedItem(
    album: Album,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
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
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(album.title, color = Color.White)
                Text(album.artist, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
