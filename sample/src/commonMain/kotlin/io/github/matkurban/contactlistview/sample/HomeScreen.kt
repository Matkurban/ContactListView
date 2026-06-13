package io.github.matkurban.contactlistview.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.matkurban.contactlistview.ContactListView
import io.github.matkurban.contactlistview.sample.model.User
import io.github.matkurban.contactlistview.sample.model.getTag
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val userList = remember {
        mutableStateListOf<User>().apply {
            addAll(buildTestUsers(count = 26))
        }
    }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val colorScheme = MaterialTheme.colorScheme
    val textTheme = MaterialTheme.typography

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("ContactList") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    userList.add(User(userId = "123", nickname = "Kurban${Random.nextInt(100)}"))
                },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add contact")
            }
        },
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    delay(2000.milliseconds)
                    userList.addAll(buildTestUsers(count = 26, startIndex = userList.size))
                    isRefreshing = false
                }
            },
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
        ) {
            ContactListView(
                contactsList = userList,
                tag = ::getTag,
                sticky = false,
                endContent = {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("总共${userList.size}位好友")
                        }
                    }
                },
                itemBuilder = { model ->
                    ListItem(
                        headlineContent = { Text(model.nickname) },
                        leadingContent = {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = colorScheme.primary,
                                        shape = RoundedCornerShape(4.dp),
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = getTag(model),
                                    style = textTheme.titleMedium,
                                    color = colorScheme.onPrimary,
                                )
                            }
                        },
                    )
                },
            )
        }
    }
}
