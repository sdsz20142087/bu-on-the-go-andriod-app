package com.cs501.cs501app.buotg.view.homeScreen.navDrawer

import android.content.Intent
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.cs501.cs501app.buotg.CustomText
import com.cs501.cs501app.buotg.database.entities.KVEntry
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.common.Ping
import com.cs501.cs501app.utils.sendNotification
import com.cs501.cs501app.buotg.view.homeScreen.POLL_STATE_KEY
import com.cs501.cs501app.buotg.view.user_map.getCurrentLocation
import com.cs501.cs501app.buotg.view.user_setting.ComposedMiscBtns
import com.cs501.cs501app.utils.BGService
import com.cs501.cs501app.utils.setupBackgroundWork
import kotlinx.coroutines.launch

@Composable
fun DrawerHeader(
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val ctx = LocalContext.current
    val userRepo = AppRepository.get().userRepo()
    val coroutineScope = rememberCoroutineScope()
    val currentUser = remember { mutableStateOf<User?>(null) }
    LaunchedEffect(true){
        currentUser.value = userRepo.getCurrentUser()
    }
    DisposableEffect(lifeCycleOwner){
        val observer = LifecycleEventObserver{ _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                coroutineScope.launch {
                    currentUser.value = userRepo.getCurrentUser()
                }
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
    ) {
        val text = currentUser.value?.full_name ?: "No Logon"
        CustomText(text = "BU TOGO: $text", fontSize = 24.sp)
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn(modifier) {
        itemsIndexed(items) { idx, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
                    .background(color = if (idx == 0) Color.LightGray else Color.Transparent),
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.contentDescription
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
//    Column(
//        modifier = Modifier
//            .fillMaxHeight()
//            .verticalScroll(rememberScrollState()),
//        verticalArrangement = Arrangement.Top,
//    ){
//        Spacer(modifier = Modifier
//            .height(16.dp)
//            .padding(8.dp))
//        ComposedMiscBtns()
//    }
}
