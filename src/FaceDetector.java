package com.shekhar.facedetection;

import java.io.File;
import java.lang.*;

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

    private String faceFolderName = "faces";
    private String faceFolderOutputName = "haarcascade_frontalface_default_all";
    private String cascadeClassifierName = "haarcascade_frontalface_default.xml";
    private Integer faceDetected = 0;

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        FaceDetector faceDetector = new FaceDetector();

        File[] listOfFiles = faceDetector.getFiles();

        double startTime = System.currentTimeMillis();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileExtension = listOfFiles[i].getName().substring(listOfFiles[i].getName().length() - 3);

                if(fileExtension.equals("jpg")) {
                    faceDetector.recognizeFace(listOfFiles[i].getName());
                } else {
                    System.out.println("File is not a jpg file");
                }

            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }

        double endTime = System.currentTimeMillis();
        double duration = endTime - startTime;

        System.out.println("Duration: " + duration / 1000 + " seconds, detected " + faceDetector.faceDetected + " faces");
    }

    public File[] getFiles() {

        File folder = new File(System.getProperty("user.dir") + "/photos/input/" + this.faceFolderName);
        File[] listOfFiles = folder.listFiles();

        return listOfFiles;
    }

    public void recognizeFace(String filename) {

        File folderDirectory = new File("photos/output/" + this.faceFolderOutputName);

        CascadeClassifier faceDetector = new CascadeClassifier(System.getProperty("user.dir") + "/cascade-classifiers/" + this.cascadeClassifierName);

        Mat image = Imgcodecs
                .imread(System.getProperty("user.dir") + "/photos/input/" + this.faceFolderName + "/" + filename);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        this.faceDetected = this.faceDetected + faceDetections.toArray().length;

        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }

        if(folderDirectory.exists()) {
            String outputFile = "photos/output/" + this.faceFolderOutputName + "/" + filename;
            Imgcodecs.imwrite(outputFile, image);
            System.out.println(String.format("Writing %s", outputFile));
        } else {
            folderDirectory.mkdir();
            System.out.println("Created new folder: " + this.faceFolderOutputName);
            String outputFile = "photos/output/" + this.faceFolderOutputName + "/" + filename;
            Imgcodecs.imwrite(outputFile, image);
            System.out.println(String.format("Writing %s", outputFile));
        }

    }
}