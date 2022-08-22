package com.jetpack.mewcheat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jetpack.mewcheat.data.Chat
import com.jetpack.mewcheat.data.Msg
import com.jetpack.mewcheat.data.User
import com.jetpack.mewcheat.ui.theme.WeComposeTheme

class WeViewModel:ViewModel() {
    var chats by mutableStateOf(
        listOf( // List<Chat>
            Chat(
                friend = User("gaolaoshi", "高老师", R.mipmap.avatar_other),
                mutableStateListOf(
                    Msg(User("gaolaoshi", "高老师", R.mipmap.avatar_other), "锄禾日当午", "14:20"),
                    Msg(User.Me, "汗滴禾下土", "14:20"),
                    Msg(User("gaolaoshi", "高老师", R.mipmap.avatar_other), "谁知盘中餐", "14:20"),
                    Msg(User.Me, "粒粒皆辛苦", "14:20"),
                    Msg(User("gaolaoshi", "高老师", R.mipmap.avatar_other), "唧唧复唧唧，木兰当户织。不闻机杼声，惟闻女叹息。", "14:20"),
                    Msg(User.Me, "双兔傍地走，安能辨我是雄雌？", "14:20"),
                    Msg(User("gaolaoshi", "高老师", R.mipmap.avatar_other), "床前明月光，疑是地上霜。", "14:20"),
                    Msg(User.Me, "吃饭吧？", "14:20"),
                )
            ),
            Chat(
                friend = User("zhangtianshi", "张天师", R.mipmap.avatar),
                mutableStateListOf(
                    Msg(User("zhangtianshi", "张天师", R.mipmap.avatar), "哈哈哈", "13:48"),
                    Msg(User.Me, "哈哈昂", "13:48"),
                    Msg(User("zhangtianshi", "张天师", R.mipmap.avatar), "你笑个屁呀", "13:48").apply { read = false },
                )
            ),
        )
    )
    val contacts by mutableStateOf(
        listOf(
            User("gaolaoshi", "高老师", R.mipmap.avatar_other),
            User("zhangtianshi", "张天师", R.mipmap.avatar)
        )
    )

    var selectId by mutableStateOf(0)

    var theme by mutableStateOf(WeComposeTheme.Theme.Light)

    var currentChat:Chat? by mutableStateOf(null)

    var chatting by mutableStateOf(false)

    fun startChat(chat : Chat){
        currentChat = chat
        chatting =  true
    }

    fun endChat():Boolean{
        if (chatting) {
            chatting = false
            return true
        }
        return false
    }

    fun boom(chat: Chat) {
        chat.msgs.add(Msg(User.Me,"\uD83D\uDCA3","15:10").apply { read = true })
    }
}
