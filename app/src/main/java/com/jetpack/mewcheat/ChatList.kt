package com.jetpack.mewcheat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.mewcheat.data.Chat
import com.jetpack.mewcheat.ui.WeTopBar
import com.jetpack.mewcheat.ui.theme.WeComposeTheme

@Composable
fun ChatList(chats: List<Chat>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WeComposeTheme.colors.background)
    ) {
        WeTopBar(title = "微信")
        LazyColumn(modifier = Modifier.background(WeComposeTheme.colors.listItem)) {
            itemsIndexed(chats) { index, chat ->
                ChatListItem(chat)
                if (index < chats.lastIndex) {
                    Divider(
                        startIndent = 64.dp,
                        color = WeComposeTheme.colors.chatListDivider,
                        thickness = 0.8.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatListItem(chat: Chat) {
    val viewModel:WeViewModel = viewModel()
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
        viewModel.startChat(chat)
        //更新状态 耦合业务, 提到viewmodel中
//        viewModel.currentChat = chat
//        viewModel.chatting =  true

    }.fillMaxWidth()) {
        Image(
            painter = painterResource(id = chat.friend.avatar),
            contentDescription = chat.friend.name,
            modifier = Modifier
                .padding(4.dp)
                .size(48.dp)
                .unRead(!chat.msgs.last().read, WeComposeTheme.colors.badge)
                .clip(RoundedCornerShape(4.dp))
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = chat.friend.name,
                modifier = Modifier.padding(bottom = 4.dp),
                color = WeComposeTheme.colors.textPrimary
            )
            Text(
                text = chat.msgs.last().text,
                fontSize = 12.sp,
                color = WeComposeTheme.colors.textSecondary
            )
        }
    }
}

@Preview
@Composable
fun PreviewChatList() {
    val model: WeViewModel = viewModel()
    WeComposeTheme(model.theme) {
        Box {
            Home(model)
            ChatPage()
        }
    }
}

fun Modifier.unRead(show: Boolean, color: Color): Modifier = this.drawWithContent {
    drawContent()
    if (show) {
        drawCircle(
            color,
            5.dp.toPx(),
            Offset(size.width - 1.dp.toPx(), 1.dp.toPx())
        )
    }
}
