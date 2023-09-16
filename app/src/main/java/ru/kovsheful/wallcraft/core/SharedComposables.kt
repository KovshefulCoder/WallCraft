package ru.kovsheful.wallcraft.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.recreate
import ru.kovsheful.wallcraft.R


@Composable
fun WallCraftScaffoldNColumn(
    scaffoldTitle: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            WallCraftTopBar(
                title = scaffoldTitle
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            content(this)
        }

    }
}

@Composable
fun WallCraftTopBar(title: String) {
    val dropDownMenuExpanded = remember {
        mutableStateOf(false)
    }
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        backgroundColor = MaterialTheme.colorScheme.primary,
        actions = {
            IconButton(
                onClick = { dropDownMenuExpanded.value = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_vertical_dots),
                    contentDescription = "Dropdown menu",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            // drop down menu
            DropdownMenu(
                expanded = dropDownMenuExpanded.value,
                onDismissRequest = {
                    dropDownMenuExpanded.value = false
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
                offset = DpOffset(x = 10.dp, y = (-60).dp)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.dropdown_menu_settings),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    },
                    onClick = {
                        dropDownMenuExpanded.value = false
                    }
                )
                Divider()
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.dropdown_menu_favorite),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    },
                    onClick = {
                        dropDownMenuExpanded.value = false
                    }
                )
                Divider()
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.dropdown_menu_downloads),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    },
                    onClick = {
                        dropDownMenuExpanded.value = false
                    }
                )
            }
        }
    )
}