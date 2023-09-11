package ru.kovsheful.wallcraft.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.kovsheful.wallcraft.core.Screens


fun NavGraphBuilder.home() {
    composable(
        route = Screens.Home.route,
    ) {
        MainScreen()
    }
}


@Composable
internal fun MainScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    MainScreen("")
}

@Preview
@Composable
fun PrevMainScreen() {
    MainScreen("")
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun MainScreen(a: String) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}
