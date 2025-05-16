package com.example.hotelhackapp.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

val @receiver:DrawableRes Int.painter: Painter
    @Composable
    inline get() = painterResource(this)