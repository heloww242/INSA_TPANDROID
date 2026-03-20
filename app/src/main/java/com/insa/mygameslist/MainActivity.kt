package com.insa.mygameslist
import SimpleSearchBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.insa.mygameslist.data.IGDB
import com.insa.mygameslist.ui.theme.MyGamesListTheme
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay

data object PagePrincipale
data class PageJeu(val name: String,val id: Long,val url: String,val genre:String,val platforms: MutableList<String>,val summary: String)
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {


            MyGamesListTheme {
                val backStack = remember { mutableStateListOf<Any>(PagePrincipale) }
                var requete by rememberSaveable() { mutableStateOf("") }
                var rechercheActiv by rememberSaveable() { mutableStateOf(false) }
                var favoris by rememberSaveable { mutableStateOf(setOf<Long>()) }
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = { key ->
                        when (key) {
                            is PageJeu -> NavEntry(key) {
                                jeux(
                                    key.name,
                                    key.id,
                                    backStack,
                                    key.url,
                                    key.genre,
                                    key.platforms,
                                    key.summary,
                                    isFavori = favoris.contains(key.id),
                                    onToggleFavori = {
                                        favoris = if (favoris.contains(key.id)){
                                            favoris - key.id
                                        } else {
                                            favoris + key.id
                                        }
                                    }
                                )
                            }

                            is PagePrincipale -> NavEntry(key) {
                                forme(
                                    backStack = backStack,
                                    requete = requete,
                                    onSearchQueryChange = { requete = it },
                                    rechercheActiv = rechercheActiv,
                                    onRechercheActivChange = { rechercheActiv = it },
                                    favoris = favoris,
                                    onToggleFavori = { id ->
                                        favoris =
                                            if (favoris.contains(id)) favoris - id else favoris + id
                                    }
                                )
                            }

                            else -> NavEntry(Unit) { Text("Problème de navigation") }
                        }
                    }
                )


            }
        }
    }


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
                        if (rechercheActiv) { //fonction de recherche (Search Bar)
                            SimpleSearchBar(
                                query = requete,
                                onQueryChange = { onSearchQueryChange(it) },
                                onSearch = { },
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        } else {
                            Text("My Games List")
                        }
                    },
                    actions = {
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
                        val summary = game.summary
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
                        var sg = ""
                        for (g in (game.genres)) {
                            val str = IGDB.genres.firstOrNull { it.id == g }?.name ?: ""
                            sg += "$str, "
                        }
                        sg = sg.removeSuffix(", ")
                        appel(
                            url,
                            game.name,
                            sg,
                            backStack,
                            game.id,
                            platforms,
                            summary,
                            isFavori = favoris.contains(game.id),
                            onToggleFavori = { onToggleFavori(game.id) })
                    }
                }

            }

        }
    }




}