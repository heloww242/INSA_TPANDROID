package com.insa.mygameslist

import SimpleSearchBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insa.mygameslist.data.IGDB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun forme(
    backStack: MutableList<Any>,
    requete: String,
    onSearchQueryChange: (String) -> Unit,
    rechercheActiv: Boolean,
    onRechercheActivChange: (Boolean) -> Unit,
    favoris: Set<Long>,
    onToggleFavori: (Long) -> Unit
) {

    val filteredGames = remember(requete) {
        if (requete.isBlank()) {
            IGDB.games
        } else {
            //filtre selon le titre, la platform, le genre
            IGDB.games.filter { game ->
                val groupNom = game.name.contains(requete, ignoreCase = true)
                val groupGenre = game.genres.any { genreId ->
                    val nomGenre = IGDB.genres.firstOrNull { it.id == genreId }?.name ?: ""
                    nomGenre.contains(requete, ignoreCase = true)
                }
                val groupPlat = game.platforms.any { platformsId ->
                    val nomPlat =
                        IGDB.platforms.firstOrNull { it.id == platformsId }?.name ?: ""
                    nomPlat.contains(requete, ignoreCase = true)
                }
                groupNom || groupGenre || groupPlat
            }
        }
    }
    Scaffold(
        topBar = @Composable {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.LightGray,
                    titleContentColor = Color.Black,
                ),
                title = {
                    if (rechercheActiv) { //si on est dans fonction de recherche (Search Bar)
                        SimpleSearchBar(
                            query = requete, // ce qu'on écrit dans la barre de recherche
                            onQueryChange = { onSearchQueryChange(it) },
                            onSearch = { },
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    } else {
                        Text("My Games List")
                    }
                },
                actions = { //affichage selon si on a cliqué sur la barre de recherche ou non
                    if (rechercheActiv) {
                        IconButton(onClick = {
                            onRechercheActivChange(false)
                            onSearchQueryChange("")
                        }) {
                            Text("X", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        IconButton(onClick = { onRechercheActivChange(true) }) {
                            Icon(
                                painter = painterResource(R.drawable.loupe),
                                contentDescription = "Rechercher",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }
            )

        },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier.fillMaxSize()

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            if (filteredGames.isEmpty() && requete.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucuns résultats",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 115.dp)
                            .height(50.dp)
                            .padding(10.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        textAlign = TextAlign.Center
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .padding(top = 30.dp)
            ) {
                items(
                    items = filteredGames
                ) { game ->
                    val url = IGDB.covers.firstOrNull { it.id == game.cover }?.url ?: ""
                    val resume = game.summary
                    val platforms = mutableListOf<String>()
                    val platforms_id1 = game.platforms
                    val platforms_id2 = mutableListOf<Long>()

                    for (plat1 in (platforms_id1)) {
                        platforms_id2.add(
                            IGDB.platforms.firstOrNull { it.id == plat1 }?.platform_logo ?: 0
                        )
                    }
                    for (plat2 in (platforms_id2)) {
                        platforms.add(
                            IGDB.platform_logos.firstOrNull { it.id == plat2 }?.url ?: ""
                        )
                    }
                    var serie_genres = ""
                    for (g in (game.genres)) {
                        val str = IGDB.genres.firstOrNull { it.id == g }?.name ?: ""
                        serie_genres += "$str, "
                    }
                    serie_genres = serie_genres.removeSuffix(", ")
                    appel(
                        url,
                        game.name,
                        serie_genres,
                        backStack,
                        game.id,
                        platforms,
                        resume,
                        isFavori = favoris.contains(game.id),
                        onToggleFavori = { onToggleFavori(game.id) })
                }
            }

        }

    }
}