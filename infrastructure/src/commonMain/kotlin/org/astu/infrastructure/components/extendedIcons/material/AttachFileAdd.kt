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
@Suppress("UnusedReceiverParameter")
val Icons.Outlined.AttachFileAdd: ImageVector
    get() {
        if (_attachFileAdd != null) {
            return _attachFileAdd!!
        }
        _attachFileAdd = Builder(
            name = "Outlined.AttachFileAdd",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 960.0f,
            viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(646.924f, 447.693f)
                verticalLineToRelative(-192.307f)
                horizontalLineToRelative(59.998f)
                verticalLineToRelative(192.307f)
                horizontalLineToRelative(-59.998f)
                close()
                moveTo(436.154f, 694.845f)
                quadToRelative(-26.538f, -10.385f, -43.269f, -34.192f)
                quadToRelative(-16.73f, -23.808f, -16.73f, -54.115f)
                verticalLineToRelative(-351.152f)
                horizontalLineToRelative(59.999f)
                verticalLineToRelative(439.459f)
                close()
                moveTo(474.207f, 859.999f)
                quadToRelative(-97.437f, 0.0f, -165.437f, -68.396f)
                quadToRelative(-67.999f, -68.395f, -67.999f, -165.834f)
                verticalLineToRelative(-359.615f)
                quadToRelative(0.0f, -69.23f, 48.076f, -117.691f)
                quadToRelative(48.077f, -48.462f, 117.307f, -48.462f)
                quadToRelative(69.231f, 0.0f, 117.307f, 48.462f)
                quadToRelative(48.077f, 48.461f, 48.077f, 117.691f)
                lineTo(571.538f, 560.0f)
                horizontalLineToRelative(-59.999f)
                verticalLineToRelative(-294.23f)
                quadToRelative(-0.615f, -44.308f, -30.776f, -75.039f)
                reflectiveQuadTo(406.154f, 160.0f)
                quadToRelative(-44.261f, 0.0f, -74.823f, 30.923f)
                quadToRelative(-30.562f, 30.923f, -30.562f, 75.231f)
                verticalLineToRelative(359.615f)
                quadToRelative(-0.615f, 72.538f, 50.154f, 123.385f)
                quadTo(401.693f, 800.0f, 474.231f, 800.0f)
                quadToRelative(23.611f, 0.0f, 44.861f, -5.731f)
                quadToRelative(21.25f, -5.73f, 40.139f, -17.192f)
                verticalLineToRelative(66.691f)
                quadToRelative(-19.846f, 8.0f, -41.192f, 12.115f)
                quadToRelative(-21.346f, 4.116f, -43.832f, 4.116f)
                close()
                moveTo(646.924f, 819.999f)
                verticalLineToRelative(-112.308f)
                lineTo(534.616f, 707.691f)
                verticalLineToRelative(-59.998f)
                horizontalLineToRelative(112.308f)
                verticalLineToRelative(-112.307f)
                horizontalLineToRelative(59.998f)
                verticalLineToRelative(112.307f)
                horizontalLineToRelative(112.307f)
                verticalLineToRelative(59.998f)
                lineTo(706.922f, 707.691f)
                verticalLineToRelative(112.308f)
                horizontalLineToRelative(-59.998f)
                close()
            }
        }.build()
        return _attachFileAdd!!
    }

private var _attachFileAdd: ImageVector? = null
