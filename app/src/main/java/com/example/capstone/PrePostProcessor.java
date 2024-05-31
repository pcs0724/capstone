package com.example.capstone;

import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Result {//결과창
    int classIndex;
    Float score;
    Rect rect;
    RectF rect2;//사각형

    public Result(int cls, Float output, Rect rect,RectF rect2) {
        this.classIndex = cls;
        this.score = output;
        this.rect = rect;
        this.rect2 = rect2;
    }
};

public class PrePostProcessor {
    // for yolov5 model, no need to apply MEAN and STD
    //"Yolov5 모델의 경우 MEAN과 STD를 적용할 필요가 없습니다."
    static float[] NO_MEAN_RGB = new float[] {0.0f, 0.0f, 0.0f};
    static float[] NO_STD_RGB = new float[] {1.0f, 1.0f, 1.0f};

    // model input image size
    static int mInputWidth = 640;
    static int mInputHeight = 640;

    // model output is of size 25200*(num_of_class+5)
    //바꿔야할지도 25200->640*640
    private static int mOutputRow = 25200; // as decided by the YOLOv5 model for input image of size 640*640
    private static int mOutputColumn = 32; // left, top, right, bottom, score and 80 class probability
    private static float mThreshold = 0.30f; // score above which a detection is generated 객체 판단 기준점
    private static int mNmsLimit = 15;

    static String[] mClasses;

    // The two methods nonMaxSuppression and IOU below are ported from https://github.com/hollance/YOLO-CoreML-MPSNNGraph/blob/master/Common/Helpers.swift
    /**
     Removes bounding boxes that overlap too much with other boxes that have
     a higher score.
     - Parameters:
     - boxes: an array of bounding boxes and their scores
     - limit: the maximum number of boxes that will be selected
     - threshold: used to decide whether boxes overlap too much
     */
    static ArrayList<Result> nonMaxSuppression(ArrayList<Result> boxes, int limit, float threshold) {

        // Do an argsort on the confidence scores, from high to low.
        Collections.sort(boxes,
                new Comparator<Result>() {
                    @Override
                    public int compare(Result o1, Result o2) {
                        return o1.score.compareTo(o2.score);
                    }
                });

        ArrayList<Result> selected = new ArrayList<>();
        boolean[] active = new boolean[boxes.size()];
        Arrays.fill(active, true);
        int numActive = active.length;

        //비-최대 억제 알고리즘(NMS)을 사용
        // The algorithm is simple: Start with the box that has the highest score.
        // Remove any remaining boxes that overlap it more than the given threshold
        // amount. If there are any boxes left (i.e. these did not overlap with any
        // previous boxes), then repeat this procedure, until no more boxes remain
        // or the limit has been reached.
        boolean done = false;
        for (int i=0; i<boxes.size() && !done; i++) {
            if (active[i]) {
                Result boxA = boxes.get(i);
                selected.add(boxA);
                if (selected.size() >= limit) break;

                for (int j=i+1; j<boxes.size(); j++) {
                    if (active[j]) {
                        Result boxB = boxes.get(j);
                        if (IOU(boxA.rect, boxB.rect) > threshold) {
                            active[j] = false;
                            numActive -= 1;
                            if (numActive <= 0) {
                                done = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return selected;
    }

    /**
     Computes intersection-over-union overlap between two bounding boxes.
     */
    static float IOU(Rect a, Rect b) {
        float areaA = (a.right - a.left) * (a.bottom - a.top);
        if (areaA <= 0.0) return 0.0f;

        float areaB = (b.right - b.left) * (b.bottom - b.top);
        if (areaB <= 0.0) return 0.0f;

        float intersectionMinX = Math.max(a.left, b.left);
        float intersectionMinY = Math.max(a.top, b.top);
        float intersectionMaxX = Math.min(a.right, b.right);
        float intersectionMaxY = Math.min(a.bottom, b.bottom);
        float intersectionArea = Math.max(intersectionMaxY - intersectionMinY, 0) *
                Math.max(intersectionMaxX - intersectionMinX, 0);
        return intersectionArea / (areaA + areaB - intersectionArea);
    }

//    outputs: YOLOv5 모델의 출력입니다. 각 객체에 대한 정보가 포함된 1차원 배열입니다.
//    imgScaleX: 입력 이미지의 너비에 대한 비율로 이미지의 x 축을 스케일링하는 데 사용됩니다.
//    imgScaleY: 입력 이미지의 높이에 대한 비율로 이미지의 y 축을 스케일링하는 데 사용됩니다.
//    ivScaleX: 입력 이미지와 실제 이미지 뷰 사이의 x 축 스케일링 비율입니다.
//    ivScaleY: 입력 이미지와 실제 이미지 뷰 사이의 y 축 스케일링 비율입니다.
//    startX: 이미지 뷰 내에서 객체의 좌측 상단 모서리의 x 좌표입니다.
//    startY: 이미지 뷰 내에서 객체의 좌측 상단 모서리의 y 좌표입니다.

    static ArrayList<Result> outputsToNMSPredictions(List<Prediction> predictions, float imgScaleX, float imgScaleY, float ivScaleX, float ivScaleY, float startX, float startY) {
        ArrayList<Result> results = new ArrayList<>();
        for (Prediction prediction:predictions) {
            if (prediction.getMax().getProbability()> mThreshold) {
                float x = prediction.x;
                float y = prediction.y;
                float w = prediction.w*100;
                float h = prediction.h*100;

                float left = imgScaleX * (x - w/2);
                float top = imgScaleY * (y - h/2);
                float right = imgScaleX * (x + w/2);
                float bottom = imgScaleY * (y + h/2);

                //현재 좌상단만 저장되는듯
                Rect rect = new Rect((int)(startX + ivScaleX * left), (int)(startY + ivScaleY * top),
                        (int)(startX + ivScaleX * right), (int)(startY + ivScaleY * bottom));

                RectF rect2=new RectF((startX + ivScaleX * left), (startY + ivScaleY * top),
                        (startX + ivScaleX * right), (startY + ivScaleY * bottom));//사각형

                Result result = new Result(prediction.getMax().getClassIndex(), prediction.getMax().getProbability(), rect,rect2);
                results.add(result);
            }
        }
        return results;
        //return nonMaxSuppression(results, mNmsLimit, mThreshold);
    }
}
