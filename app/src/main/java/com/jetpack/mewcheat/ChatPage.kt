package com.jetpack.mewcheat

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.mewcheat.data.Msg
import com.jetpack.mewcheat.data.User
import com.jetpack.mewcheat.ui.WeTopBar
import com.jetpack.mewcheat.ui.theme.WeComposeTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun ChatPage() {
    val model: WeViewModel = viewModel()
    val offsetPercentX by animateFloatAsState(if (model.chatting) 0f else 1f)
    val chat = model.currentChat
    var shakingTime by remember {
        mutableStateOf(0)
    }
    if (chat != null) {
        Column(
            modifier = Modifier
                .offsetPercent(offsetPercentX)
                .background(color = WeComposeTheme.colors.background)
                .fillMaxSize()
        ) {
            WeTopBar(title = chat.friend.name) {
                model.endChat()
            }

            Box(
                modifier = Modifier
                    .background(WeComposeTheme.colors.chatPage)
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .alpha(WeComposeTheme.colors.chatPageBgAlpha)
                        .fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_bg_newyear_left),
                        contentDescription = null,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterStart)
                            .padding(100.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_bg_newyear_top),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(horizontal = 24.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_bg_newyear_right),
                        contentDescription = null,
                        modifier = Modifier
                            .align(alignment = Alignment.BottomEnd)
                            .padding(vertical = 200.dp)
                    )
                }

                var shakingOffset = remember {
                    Animatable(0f)
                }

                LaunchedEffect(key1 = shakingTime) {
                    if (shakingTime != 0) {
                        shakingOffset.animateTo(
                            0f,
                            animationSpec = spring(0.3f, 600f),
                            initialVelocity = -2000f
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(
                            shakingOffset.value.dp,
                            shakingOffset.value.dp
                        )
                ) {
                    itemsIndexed(items = chat.msgs) { index, msg ->
                        MessageItem(msg, shakingTime, chat.msgs.size - index - 1)
                    }
                }
            }
            ChatBottomBar(onBombClicked = {
                model.boom(chat)
                shakingTime++
            })
        }
    }
}

@Composable
fun ChatBottomBar(onBombClicked: () -> Unit) {
    var editingText by remember { mutableStateOf("") }

    Row(
        Modifier
            .fillMaxWidth()
            .background(WeComposeTheme.colors.bottomBar)
            .padding(4.dp, 0.dp)
    ) {
        Icon(
            painterResource(R.drawable.ic_voice),
            contentDescription = null,
            Modifier
                .align(Alignment.CenterVertically)
                .padding(4.dp)
                .size(28.dp),
            tint = WeComposeTheme.colors.icon
        )
        BasicTextField(
            editingText, { editingText = it },
            Modifier
                .weight(1f)
                .padding(4.dp, 8.dp)
                .height(40.dp)
                .clip(MaterialTheme.shapes.small)
                .background(WeComposeTheme.colors.textFieldBackground)
                .padding(start = 8.dp, top = 10.dp, end = 8.dp),
            cursorBrush = SolidColor(WeComposeTheme.colors.textPrimary)
        )
        Text(
            "\uD83D\uDCA3",
            Modifier
                .clickable(onClick = onBombClicked)
                .padding(4.dp)
                .align(Alignment.CenterVertically),
            fontSize = 24.sp
        )
        Icon(
            painterResource(R.drawable.ic_add),
            contentDescription = null,
            Modifier
                .align(Alignment.CenterVertically)
                .padding(4.dp)
                .size(28.dp),
            tint = WeComposeTheme.colors.icon
        )
    }
}


@Composable
fun MessageItem(msg: Msg, shakingTime: Int, shakingLevel: Int) {
    val shakingAngleBubble = remember { Animatable(0f) }
    LaunchedEffect(key1 = shakingTime, block = {
        if (shakingTime != 0) {
            delay(shakingLevel.toLong() * 30)
            shakingAngleBubble.animateTo(
                0f,
                animationSpec = spring(0.4f, 500f),
                initialVelocity = 1200f / (1 + shakingLevel * 0.4f)
            )
        }
    })
    if (msg.from == User.Me) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            val colorText = WeComposeTheme.colors.bubbleMe
            Row(modifier = Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.End) {
                Text(text = "${msg.text}",
                    modifier = Modifier
                        .graphicsLayer(
                            rotationZ = shakingAngleBubble.value,
                            transformOrigin = TransformOrigin(1f, 0f)
                        )
                        .drawBehind {
                            val bubble = Path().apply {
                                val rect = RoundRect(
                                    10.dp.toPx(),
                                    0f,
                                    size.width - 10.dp.toPx(),
                                    size.height,
                                    4.dp.toPx(),
                                    4.dp.toPx()
                                )
                                addRoundRect(rect)
                                moveTo(size.width - 10.dp.toPx(), 15.dp.toPx())
                                lineTo(size.width - 5.dp.toPx(), 20.dp.toPx())
                                lineTo(size.width - 10.dp.toPx(), 25.dp.toPx())
                                close()
                            }
                            drawPath(path = bubble, color = colorText)
                        }
                        .padding(20.dp, 10.dp),
                    color = WeComposeTheme.colors.textPrimaryMe)
            }
            Image(
                painter = painterResource(msg.from.avatar),
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer(
                        rotationZ = shakingAngleBubble.value,
                        transformOrigin = TransformOrigin(1f, 0f)
                    )
                    .size(40.dp)
                    .clip(shape = RoundedCornerShape(4.dp))
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            val otherColor = WeComposeTheme.colors.bubbleOthers
            Image(
                painter = painterResource(id = msg.from.avatar), contentDescription = null,
                modifier = Modifier
                    .graphicsLayer(
                        rotationZ = shakingAngleBubble.value,
                        transformOrigin = TransformOrigin(0f, 0f)
                    )
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Row(
                modifier = Modifier
                    .graphicsLayer(
                        rotationZ = shakingAngleBubble.value,
                        transformOrigin = TransformOrigin(0f, 0f)
                    )
                    .fillMaxWidth(0.8f), horizontalArrangement = Arrangement.Start
            ) {
                Text(text = msg.text, modifier = Modifier
                    .drawBehind {
                        val path = Path().apply {
                            val rect =
                                RoundRect(
                                    10.dp.toPx(),
                                    0f,
                                    size.width - 10.dp.toPx(),
                                    size.height
                                )
                            addRoundRect(rect)
                            moveTo(10.dp.toPx(), 5.dp.toPx())
                            lineTo(5.dp.toPx(), 10.dp.toPx())
                            lineTo(10.dp.toPx(), 15.dp.toPx())
                            close()
                        }
                        drawPath(path = path, color = otherColor)
                    }
                    .padding(20.dp, 10.dp))
            }
        }
    }
}


fun Modifier.offsetPercent(offsetPercentX: Float = 0f, offsetPercentY: Float = 0f) =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        var offsetX = (offsetPercentX * placeable.width).roundToInt()
        var offsetY = (offsetPercentY * placeable.height).roundToInt()
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(offsetX, offsetY)
        }
    }
