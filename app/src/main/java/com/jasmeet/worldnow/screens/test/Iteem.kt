package com.jasmeet.worldnow.screens.test

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Entity(tableName = "items")
data class Item(
    @PrimaryKey val id: Int,
    val name: String,
    val isFavorite: Boolean
)

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getAllItems(): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)
}

@Composable
fun LazyColumnWithFavorites(
    items: List<Item>,
    itemDao: ItemDao
) {
    val coroutine = rememberCoroutineScope()
    val favoriteItems = remember { mutableStateListOf<Item>() }

    // Initialize favoriteItems with data from the database
    favoriteItems.addAll(items.filter { it.isFavorite })

    LazyColumn {
        items(favoriteItems) { item ->
            Row() {
                Text(text = item.name)

                IconButton(
                    onClick = {
                        val updatedItem = item.copy(isFavorite = !item.isFavorite)
                        coroutine.launch {
                            itemDao.insertItem(updatedItem)
                            // Update the favoriteItems list locally
                            if (item.isFavorite) {
                                favoriteItems.remove(item)
                            } else {
                                favoriteItems.add(updatedItem)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (item.isFavorite) Color.Red else Color.Gray
                    )
                }
            }
        }
    }
}


@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}

fun getDatabaseInstance(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "app_database"
    ).build()
}

@Composable
fun MainUi() {
    val context = LocalContext.current
    val dataBase = getDatabaseInstance(context)
    val itemDao = dataBase.itemDao()

    // Initialize a list from 1 to 100
    val items = (1..100).map { Item(id = it, name = "Item $it", isFavorite = false) }

    val favoriteItems = remember { mutableStateListOf<Item>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.IO) {
            val savedItems = itemDao.getAllItems()
            favoriteItems.clear()
            favoriteItems.addAll(savedItems.filter { it.isFavorite })
        }
    }

    LazyColumn (verticalArrangement = Arrangement.spacedBy(4.dp)){
        items(favoriteItems) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item.name)

                IconButton(
                    onClick = {
                        val updatedItem = item.copy(isFavorite = !item.isFavorite)
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                itemDao.insertItem(updatedItem)
                            }
                            if (item.isFavorite) {
                                favoriteItems.remove(item)
                            } else {
                                favoriteItems.add(updatedItem)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (item.isFavorite) Color.Red else Color.Black,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MainUi2() {
    val context = LocalContext.current
    val dataBase = getDatabaseInstance(context)
    val itemDao = dataBase.itemDao()

    // Initialize a list from 1 to 100 with all items initially as not favorite
    val items = (1..100).map { Item(id = it, name = "Item $it", isFavorite = false) }

    val coroutineScope = rememberCoroutineScope()

    LazyColumn (verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier
        .padding(horizontal = 10.dp, vertical = 10.dp)
        .fillMaxSize()) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray)
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item.name)

                IconButton(
                    onClick = {
                        val updatedItem = item.copy(isFavorite = !item.isFavorite)
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                itemDao.insertItem(updatedItem)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (item.isFavorite) Color.Red else Color.Black,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
    }
}
