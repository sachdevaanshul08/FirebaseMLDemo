package com.p.objectidentification.imagelabeler;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;

import java.util.List;

public class ImageLabeler {

    static ImageLabeler imageLabeler;

    OnSuccessListener<List<FirebaseVisionLabel>> successListener;
    OnFailureListener failureListener;
    FirebaseVisionLabelDetectorOptions options;

    public static ImageLabeler getInstance() {
        if (imageLabeler == null) {
            imageLabeler = new ImageLabeler();
        }
        return imageLabeler;
    }

    private ImageLabeler() {
    }

    public void setSuccessListener(OnSuccessListener<List<FirebaseVisionLabel>> successListener) {
        this.successListener = successListener;
    }

    public void setFailureListener(OnFailureListener failureListener) {
        this.failureListener = failureListener;
    }

    /**
     *
     */
    private void setConfidenceLimit(float limit) {
        // [START set_detector_options]
        //if threshhold is out of range,set it to default
        if (limit < 0.0f || limit > 1.0f)
            limit = 0.5f;
        options =
                new FirebaseVisionLabelDetectorOptions.Builder()
                        .setConfidenceThreshold(limit)
                        .build();
        // [END set_detector_options]

    }


    /**
     * @return
     */
    private FirebaseVisionLabelDetector getDefaultDetector() {
        return FirebaseVision.getInstance().getVisionLabelDetector();
    }

    /**
     * @return
     */
    private FirebaseVisionLabelDetector getOptionsDetector() {
        if (options != null) {
            return FirebaseVision.getInstance()
                    .getVisionLabelDetector(options);
        } else {
            return getDefaultDetector();
        }
    }

    /**
     *
     * @param image
     * @param isMinimumConfidenceRequired
     * @param confidenceThresshold
     */
    public void labelImages(FirebaseVisionImage image, boolean isMinimumConfidenceRequired, int confidenceThresshold) {

        FirebaseVisionLabelDetector detector;

        if (isMinimumConfidenceRequired) {
            setConfidenceLimit(confidenceThresshold);
            detector = getOptionsDetector();
        } else {
            detector = getDefaultDetector();
        }

        Task<List<FirebaseVisionLabel>> result = null;
        // [START run_detector]
        if (successListener != null) {
            result =
                    detector.detectInImage(image);
            result.addOnSuccessListener(successListener);
        }

        if (failureListener != null && result != null) {
            result.addOnFailureListener(failureListener);
        }
        // [END run_detector]
    }
}