package com.cs501.cs501app.buotg.view.user_invite

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.database.entities.Group
import com.cs501.cs501app.buotg.database.entities.GroupInvite
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.utils.GenericTopAppBar
import kotlinx.coroutines.launch

//a list to save user names get from server
val userNames = listOf("user1", "user2")

class InviteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowView()
            MaterialTheme {
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun ShowView() {

        val ctx = LocalContext.current
        val invites = remember { mutableStateOf(listOf<GroupInvite>()) }
        val inviteRepo = AppRepository.get().inviteRepository()

        suspend fun reloadList() {
            invites.value = inviteRepo.listInvites(ctx)?.group_invites ?: listOf()
        }

        LaunchedEffect(true) {
            reloadList()
        }



        Scaffold(
            topBar = {
                GenericTopAppBar(title = "My Group Invites")
            }) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                //a list to show group invites
                LazyColumn() {
                    items(invites.value.size) { index ->
                        InviteRow(invites.value[index], reload = ::reloadList, allowModify = true)
                    }
                }
            }
        }
    }
}