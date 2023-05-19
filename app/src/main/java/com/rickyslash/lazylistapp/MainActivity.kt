package com.rickyslash.lazylistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rickyslash.lazylistapp.ui.theme.LazyListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyListAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HeroList()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LazyListAppTheme {
        HeroList()
    }
}

// Lazy Layout: data is loaded as its needed, to sustain performance & memory

// 2 type of LazyList: LazyColumn & LazyRow
// LazyList scope's block:
// - item: to add an item
// - items: to add multiple items (Integer, List, or Array)
// - itemsIndexed: to add indexed multiple items

// 2 type of LazyGrid: LazyVerticalGrid & LazyHorizontalGrid
// 2 type of LazyGrid's GridCells:
// - Fixed: for fixed number of column
// - Adaptive: to adjust with screen's width (need to define minimal screen size)

/* to customize GridCell's size to 2 by 1:
LazyVerticalGrid(columns = object : GridCells {
    override fun Density.calculateCrossAxisCellSizes(
        availableSize: Int,
        spacing: Int
    ): List<Int> {
        val firstColumn = (availableSize - spacing) * 2 / 3
        val secondColumn = availableSize - spacing - firstColumn
        return listOf(firstColumn, secondColumn)
    }
}) {
}*/

/* Parameters in LazyGrid:
LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
) {
    item(span = { GridItemSpan(2) }) {
        …
    }
}*/

// Item Key: even by default every item on LazyList has Key, it's good to apply Key if it's involving dataset
// --- it is because manipulating database will Recompose LazyList, and make LazyList component's state disappeared
/* example of applying Key when manipulating database:
data class Contact(val id: Int, val name: String)

@Composable
fun ColorBox() {
    val list = remember { mutableStateListOf<Contact>() }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item(span = { GridItemSpan(2) }) {
            Button(onClick = {
                list.add(Contact(Random.nextInt(1000), "new name"))
            }) {
                Text("Add")
            }
        }
        items(list, key = { it.id }) { item ->
            val color by remember {
                mutableStateOf(
                Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
                )
            }
            Button(
                onClick = { list.remove(item) },
                colors = ButtonDefaults.buttonColors(backgroundColor = color),
                modifier = Modifier
                    .height(120.dp)
                    .animateItemPlacement(tween(durationMillis = 250))
            ) {
                Text(item.id.toString())
            }
        }
    }
}*/

// Performance tips on LazyLayout:
// - set fixed item size (avoid 0px size)
// --- modifier = Modifier.size(40.dp)
// - avoid using nested scrollable with same direction
// - avoid placing many element inside 1 item
// --- those element will be considered as 1 component, make them can't be composed separately
// --- if those elements are wide, both of them need to be Composed even some of them not displayed within screen
// --- index will possibly be out of order (hard to use scrollToItem() or animateScrollToItem())
// - Consider to make Custom Arrangement (example: always display footer (from LazyList items) on the bottom of the screen)