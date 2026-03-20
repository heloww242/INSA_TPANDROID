package com.insa.mygameslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun jeux(
    name: String,
    id: Long,
    backStack: MutableList<Any>,
    url: String,
    genres: String,
    platforms: MutableList<String>,
    summary: String,
    isFavori: Boolean,
    onToggleFavori: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.Magenta,
                    titleContentColor = Color.Black,
                ),
                title = { Text(name) },
                navigationIcon = {
                    IconButton(
                        onClick = { backStack.removeLastOrNull() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrowback),
                            contentDescription = "Retour"
                        )
                    }
                }
            )

        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center

        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 115.dp)
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = onToggleFavori) {
                        if (isFavori) {
                            Text("★", fontSize = 30.sp,color = Color.Yellow)
                        } else {
                            Text("☆", fontSize = 30.sp)
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = "https:" + url,
                        contentDescription = "Image depuis URL",
                        modifier = Modifier
                            .height(280.dp)
                            .fillMaxWidth()
                    )
                }
                Text(
                    text = genres,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(10.dp),
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .padding(end = 10.dp)
                    ) {
                        items(platforms) { plat ->
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = "https:" + plat,
                                    contentDescription = "Image depuis URL",
                                    modifier = Modifier.height(50.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .padding(top = 10.dp)
                            .fillMaxHeight()
                    ) {
                        Text(text = summary, fontSize = 15.sp)
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .padding(top = 10.dp)
                        .fillMaxHeight()
                ) {
                    Text(text = summary, fontSize = 15.sp)
                }
            }
        }
    }
}