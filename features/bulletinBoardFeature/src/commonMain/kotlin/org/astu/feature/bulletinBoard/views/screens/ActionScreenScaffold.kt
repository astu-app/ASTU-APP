package org.astu.feature.bulletinBoard.views.screens

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Функция, создающая контейнер для экрана действия над объявлениями, например, экрана просмотра подробностей, создания.
 * Предоставляет Scaffold с заданным title и областью с вертикальной прокруткой для отображения указанного контента.
 * @param onReturn действие, выполняемое при возврате с экрана
 * @param topBarTitle заголовок экрана
 * @param returnScreenIcon иконка возврата с экрана
 * @param animateScrollTo
 * @param modifier модификатор, который будет применен к этому контейнеру
 * @param floatingActionButton плавающая в правом нижнем углу кнопка
 * @param actions действия, отображаемые в конце TopBar
 * @param content контент контейнера
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionScreenScaffold(
    onReturn: () -> Unit,
    topBarTitle: @Composable () -> Unit,
    returnScreenIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    animateScrollTo: Int = 100,
    modifier: Modifier = Modifier.fillMaxSize(),
    floatingActionButton: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = topBarTitle,
                navigationIcon = {
                    IconButton(onClick = onReturn) {
                        Icon(
                            imageVector = returnScreenIcon,
                            contentDescription = "Покинуть экран"
                        )
                    }
                },
                actions = actions
            )
        },
        floatingActionButton = floatingActionButton,
        modifier = modifier
    ) {
        val scrollState = rememberScrollState()
        LaunchedEffect(Unit) { scrollState.animateScrollTo(animateScrollTo) }

        Surface(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .verticalScroll(scrollState),
            content = content,
        )
    }
}