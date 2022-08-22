package com.jetpack.mewcheat

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetpack.mewcheat.ui.theme.WeComposeTheme

@Composable
fun WeBottomBar(selected: Int,onSelectTabClick:(Int) -> Unit) {
    Row(modifier = Modifier
        .background(WeComposeTheme.colors.bottomBar)
    ) {
        TabView(
            if (selected == 0) R.mipmap.tab_chat else R.mipmap.tab_chat_normal_nor,
            "微信",
            Modifier
                .weight(1f)
                .clickable {
                    onSelectTabClick(0)
                },
            if (selected == 0) WeComposeTheme.colors.iconCurrent else WeComposeTheme.colors.icon
        )
        TabView(
            if (selected == 1) R.mipmap.tab_group else R.mipmap.tab_group_normal_nor,
            "通讯录",
            Modifier
                .weight(1f)
                .clickable {
                    onSelectTabClick(1)
                },
            if (selected == 1) WeComposeTheme.colors.iconCurrent else WeComposeTheme.colors.icon
        )
        TabView(
            if (selected == 2) R.mipmap.tab_find else R.mipmap.tab_find_normal_nor,
            "发现",
            Modifier
                .weight(1f)
                .clickable {
                    onSelectTabClick(2)
                },
            if (selected == 2) WeComposeTheme.colors.iconCurrent else WeComposeTheme.colors.icon
        )
        TabView(
            if (selected == 3) R.mipmap.tab_my else R.mipmap.tab_my_normal_nor,
            "我",
            Modifier
                .weight(1f)
                .clickable {
                    onSelectTabClick(3)
                },
            if (selected == 3) WeComposeTheme.colors.iconCurrent else WeComposeTheme.colors.icon
        )
    }
}

@Composable
fun TabView(@DrawableRes iconId: Int, title: String, modifier: Modifier = Modifier, tint: Color) {
    Column(modifier = modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(painter = painterResource(iconId), contentDescription = title, Modifier.size(24.dp), tint = tint)
        Spacer(modifier = Modifier.size(2.dp))
        Text(text = title, fontSize = 11.sp, color = tint)
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeComposeTheme(WeComposeTheme.Theme.Light) {
        var index by remember {
            mutableStateOf(0)
        }
        WeBottomBar(index){
            index = it
        }
    }
}