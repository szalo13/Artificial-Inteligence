package com.shekhar.facedetection;

import java.io.File;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetector {

    private String faceFolderName = "faces1";
    private String faceFolderOutputName = "FullBody";
    private String cascadeClassifierName = "haarcascade_fullbody.xml";

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        FaceDetector faceDetector = new FaceDetector();

        File[] listOfFiles = faceDetector.getFiles();


        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                faceDetector.recognizeFace(listOfFiles[i].getName());

            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }


    }

    public File[] getFiles() {

        File folder = new File(System.getProperty("user.dir") + "/photos/input/" + this.faceFolderName);
        File[] listOfFiles = folder.listFiles();

        return listOfFiles;
    }

    public void recognizeFace(String filename) {

        CascadeClassifier faceDetector = new CascadeClassifier(System.getProperty("user.dir") + "/cascade-classifiers/" + this.cascadeClassifierName);

        Mat image = Imgcodecs
                .imread(System.getProperty("user.dir") + "/photos/input/" + this.faceFolderName + "/" + filename);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }


        String outputFile = "photos/output/" + this.faceFolderOutputName + "/" + filename;
        System.out.println(String.format("Writing %s", outputFile));
        Imgcodecs.imwrite(outputFile, image);

    }
}