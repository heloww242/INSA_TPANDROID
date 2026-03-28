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




}