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
val Icons.Outlined.UserAttributes: ImageVector
    get() {
        if (_userAttributes != null) {
            return _userAttributes!!
        }

        _userAttributes = Builder(
            name = "UserAttributes",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 960.0f,
            viewportHeight = 960.0f
        ).apply {
            path(fill = SolidColor(Color(0xFF5f6368)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(560.0f, 280.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(320.0f)
                verticalLineToRelative(80.0f)
                lineTo(560.0f, 280.0f)
                close()
                moveTo(560.0f, 440.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(320.0f)
                verticalLineToRelative(80.0f)
                lineTo(560.0f, 440.0f)
                close()
                moveTo(560.0f, 600.0f)
                verticalLineToRelative(-80.0f)
                horizontalLineToRelative(320.0f)
                verticalLineToRelative(80.0f)
                lineTo(560.0f, 600.0f)
                close()
                moveTo(320.0f, 560.0f)
                quadToRelative(-50.0f, 0.0f, -85.0f, -35.0f)
                reflectiveQuadToRelative(-35.0f, -85.0f)
                quadToRelative(0.0f, -50.0f, 35.0f, -85.0f)
                reflectiveQuadToRelative(85.0f, -35.0f)
                quadToRelative(50.0f, 0.0f, 85.0f, 35.0f)
                reflectiveQuadToRelative(35.0f, 85.0f)
                quadToRelative(0.0f, 50.0f, -35.0f, 85.0f)
                reflectiveQuadToRelative(-85.0f, 35.0f)
                close()
                moveTo(80.0f, 800.0f)
                verticalLineToRelative(-76.0f)
                quadToRelative(0.0f, -21.0f, 10.0f, -40.0f)
                reflectiveQuadToRelative(28.0f, -30.0f)
                quadToRelative(45.0f, -27.0f, 95.5f, -40.5f)
                reflectiveQuadTo(320.0f, 600.0f)
                quadToRelative(56.0f, 0.0f, 106.5f, 13.5f)
                reflectiveQuadTo(522.0f, 654.0f)
                quadToRelative(18.0f, 11.0f, 28.0f, 30.0f)
                reflectiveQuadToRelative(10.0f, 40.0f)
                verticalLineToRelative(76.0f)
                lineTo(80.0f, 800.0f)
                close()
                moveTo(166.0f, 720.0f)
                horizontalLineToRelative(308.0f)
                quadToRelative(-35.0f, -20.0f, -74.0f, -30.0f)
                reflectiveQuadToRelative(-80.0f, -10.0f)
                quadToRelative(-41.0f, 0.0f, -80.0f, 10.0f)
                reflectiveQuadToRelative(-74.0f, 30.0f)
                close()
                moveTo(320.0f, 480.0f)
                quadToRelative(17.0f, 0.0f, 28.5f, -11.5f)
                reflectiveQuadTo(360.0f, 440.0f)
                quadToRelative(0.0f, -17.0f, -11.5f, -28.5f)
                reflectiveQuadTo(320.0f, 400.0f)
                quadToRelative(-17.0f, 0.0f, -28.5f, 11.5f)
                reflectiveQuadTo(280.0f, 440.0f)
                quadToRelative(0.0f, 17.0f, 11.5f, 28.5f)
                reflectiveQuadTo(320.0f, 480.0f)
                close()
                moveTo(320.0f, 440.0f)
                close()
                moveTo(320.0f, 720.0f)
                close()
            }
        }
        .build()
        return _userAttributes!!
    }

private var _userAttributes: ImageVector? = null
