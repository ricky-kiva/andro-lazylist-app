package com.rickyslash.lazylistapp

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rickyslash.lazylistapp.model.HeroesData
import com.rickyslash.lazylistapp.ui.theme.LazyListAppTheme
import kotlinx.coroutines.launch
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rickyslash.lazylistapp.data.HeroRepository

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroListApp(
    modifier: Modifier = Modifier,
    viewModel: HeroListAppViewModel = viewModel(factory = ViewModelFactory(HeroRepository()))
) {
    val groupedHeroes by viewModel.groupedHeroes.collectAsState()

    Box(modifier = modifier) {
        val scope = rememberCoroutineScope() // to run scope manually on button click
        val listState = rememberLazyListState() // to remember LazyList's state
        val showButton: Boolean by remember {
            derivedStateOf { listState.firstVisibleItemIndex > 0 } // change state according given condition
        }
        LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 80.dp)) {
            groupedHeroes.forEach { (initial, heroes) ->
                stickyHeader { CharHeader(initial) }
                items(heroes, key = { it.id }) { hero ->
                    HeroListItem(name = hero.name, photoUrl = hero.photoUrl, modifier = Modifier
                        .fillMaxWidth()
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.BottomCenter)
        ) {
            ScrollToTopButton(onClick = {
                scope.launch { listState.scrollToItem(index = 0) }
            })
        }
    }
}

@Composable
fun CharHeader(char: Char, modifier: Modifier = Modifier) {
    Surface(color = MaterialTheme.colors.primary, modifier = modifier) {
        Text(
            text = char.toString(),
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

// best practice in making Composable is to only pass necessary parameter, not the whole object (will affect recomposition)
@Composable
fun HeroListItem(name: String, photoUrl: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.clickable {  }) {
            AsyncImage(
                model = photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(8.dp)
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Text(
                text = name,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun ScrollToTopButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .shadow(elevation = 10.dp, shape = CircleShape)
            .clip(shape = CircleShape)
            .size(56.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = MaterialTheme.colors.primary
        )
    ) {
        Icon(imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = null)
    }
}

@Composable
@Preview(showBackground = true)
fun LazyListAppPreview() {
    LazyListAppTheme {
        HeroListItem("Rickyslash", "")
    }
}