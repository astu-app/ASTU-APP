package org.astu.infrastructure.components.extendedIcons.material

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// если убрать "Icons.Outlined.",  AttachFileAdd не будет соотноситься с этим объектом
@Suppress("UnusedReceiverParameter", "unused")
val Icons.Outlined.CheckSmall: ImageVector
    get() {
        if (_checkSmall != null) {
            return _checkSmall!!
        }
        _checkSmall = Builder(
            name = "Outlined.CheckSmall",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 960.0f,
            viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF5f6368)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(400.0f, 656.0f)
                lineTo(240.0f, 496.0f)
                lineToRelative(56.0f, -56.0f)
                lineToRelative(104.0f, 104.0f)
                lineToRelative(264.0f, -264.0f)
                lineToRelative(56.0f, 56.0f)
                lineToRelative(-320.0f, 320.0f)
                close()
            }
        }
            .build()
        return _checkSmall!!
    }

private var _checkSmall: ImageVector? = null