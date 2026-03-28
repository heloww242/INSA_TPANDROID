package com.insa.mygameslist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun appel( //affichage du bloc de jeu
    url: String,
    name: String,
    genres: String,
    backStack: MutableList<Any>,
    id: Long, platforms: MutableList<String>,
    resume: String,
    isFavori: Boolean,
    onToggleFavori: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(top = 5.dp)
            .padding(20.dp)
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
            .clickable { backStack.add(PageJeu(name, id, url, genres, platforms, resume)) }
    ) {
        Column(
            modifier = Modifier.height(100.dp).width(80.dp),
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = "https:$url",
                contentDescription = "Image depuis URL",
                modifier = Modifier.height(95.dp).fillMaxWidth().padding(10.dp),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier.padding(start = 80.dp, end = 50.dp).fillMaxWidth()
                .height(100.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = name,
                modifier = Modifier.padding(bottom = 5.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                color = Color.Black
            )
            Text(
                text = "Genres: $genres",
                fontSize = 14.sp, maxLines = 1,
                overflow = TextOverflow.Ellipsis, color = Color.Black
            )
        }

        IconButton(
            onClick = onToggleFavori,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        ) {
            if (isFavori) {
                Text("★", fontSize = 24.sp, color = Color.Yellow)
            } else {
                Text("☆", fontSize = 24.sp, color = Color.Black)
            }
        }
    }
}
