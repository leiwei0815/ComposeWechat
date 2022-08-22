package com.jetpack.mewcheat

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun Home(model: WeViewModel) {
    Column {
        val pagerState = rememberPagerState()
        HorizontalPager(
            count = 4, modifier = Modifier.weight(1f),
            pagerState
        ) { index ->
            when (index) {
                0 -> ChatList(model.chats)
                1 -> ContactList()
                2 -> DiscoveryList()
                3 -> MeList()
            }
        }

        val scope = rememberCoroutineScope()

        WeBottomBar(pagerState.currentPage) { page ->
            //点击页签后, 在协程里翻页
            scope.launch {
                pagerState.animateScrollToPage(page)
            }
        }
    }
}