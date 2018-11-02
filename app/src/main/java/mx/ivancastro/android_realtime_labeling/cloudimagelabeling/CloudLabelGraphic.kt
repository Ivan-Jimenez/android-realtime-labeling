package mx.ivancastro.android_realtime_labeling.cloudimagelabeling

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import mx.ivancastro.android_realtime_labeling.common.GraphicOverlay

/**
 * Graphic instance for rendering detected label.
 */
class CloudLabelGraphic (private val overlay: GraphicOverlay) : GraphicOverlay.Graphic(overlay) {
    private val textPlaint : Paint

    private lateinit var labels : List<String>

    init {
        textPlaint = Paint()
        textPlaint.color    = Color.CYAN
        textPlaint.textSize = 60.0f
    }

    @Synchronized
    internal fun updateLabel (labels : List<String>) {
        this.labels = labels
        postInvalidate()
    }

    @Synchronized
    override fun draw(canvas: Canvas) {
        val x = overlay.width / 4.0f
        var y = overlay.height / 4.0f

        for (label in labels) {
            canvas.drawText(label, x, y, textPlaint)
            y -= 62.0f
        }
    }
}