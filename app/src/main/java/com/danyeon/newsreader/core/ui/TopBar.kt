package com.danyeon.newsreader.core.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(){
    TopAppBar(
        title = {
            Text("NewsReader")
        }
    )
}

@Preview
@Composable
private fun TopBarPreview(){
    TopBar()
}