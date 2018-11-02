package mx.ivancastro.android_realtime_labeling.cloudimagelabeling

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import mx.ivancastro.android_realtime_labeling.VisionProcessorBase
import mx.ivancastro.android_realtime_labeling.common.FrameMetadata
import mx.ivancastro.android_realtime_labeling.common.GraphicOverlay
import java.lang.Exception

/**
 * Cloud Label Detector.
 */
class CloudImageLabelingProcessor : VisionProcessorBase<List<FirebaseVisionCloudLabel>>() {
    private val detector : FirebaseVisionCloudLabelDetector by lazy {
        FirebaseVisionCloudDetectorOptions.Builder()
            .setMaxResults(10)
            .setModelType(FirebaseVisionCloudDetectorOptions.STABLE_MODEL)
            .build().let { options ->
                FirebaseVision.getInstance().getVisionCloudLabelDetector(options)
            }
    }

    override fun detectInImage(image: FirebaseVisionImage): Task<List<FirebaseVisionCloudLabel>> {
        return detector.detectInImage(image)
    }

    override fun onSuccess(result: List<FirebaseVisionCloudLabel>, frameMetadata: FrameMetadata, graphicOverlay: GraphicOverlay) {
        graphicOverlay.clear()

        Log.d(TAG, "cloud label size: ${result.size}")

        val labelsStr = ArrayList<String>()
        for (i in result.indices) {
            val resu = result[i]
            Log.d(TAG, "cloud label: ${resu}")
            resu.label?.let { labelsStr.add(it) }
        }

        val cloudLabelGraphic = CloudLabelGraphic(graphicOverlay)
        graphicOverlay.add(cloudLabelGraphic)
        cloudLabelGraphic.updateLabel(labelsStr)
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Cloud Label detection failed")
    }

    companion object {
        private const val TAG = "CloudImageLabelProc"
    }
}