package ru.kovsheful.wallcraft.core

import androidx.compose.foundation.background
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ru.kovsheful.wallcraft.R
import ru.kovsheful.wallcraft.ui.theme.DropDownMenuColor
import ru.kovsheful.wallcraft.ui.theme.PrimaryColor
import ru.kovsheful.wallcraft.ui.theme.SecondaryText
import ru.kovsheful.wallcraft.ui.theme.typography

@Composable
fun WallCraftTopBar(title: String) {
    val dropDownMenuExpanded = remember {
        mutableStateOf(false)
    }
    TopAppBar(
        title = {
            Text(
                text = title,
                style = typography.titleLarge
            )
        },
        backgroundColor = PrimaryColor,
        actions = {
            IconButton(
                onClick = { dropDownMenuExpanded.value = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_vertical_dots),
                    contentDescription = "Dropdown menu"
                )
            }
            // drop down menu
            DropdownMenu(
                expanded = dropDownMenuExpanded.value,
                onDismissRequest = {
                    dropDownMenuExpanded.value = false
                },
                modifier = Modifier.background(DropDownMenuColor),
                offset = DpOffset(x = 10.dp, y = (-60).dp)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.dropdown_menu_settings),
                            style = typography.bodySmall
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
                            style = typography.bodySmall
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
                            style = typography.bodySmall
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