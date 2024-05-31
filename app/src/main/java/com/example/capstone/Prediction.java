package com.example.capstone;

import java.util.List;

public class Prediction {//모델에서 받은 데이터 저장

    float x, y, w, h;
    float conf;
    List<ClassPrediction> classPredictions;

    public Prediction(float x, float y, float w, float h, float conf, List<ClassPrediction> classPredictions) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.conf = conf;
        this.classPredictions = classPredictions;
    }
    public ClassPrediction getMax(){
        ClassPrediction classPrediction=new ClassPrediction(0,0);
        for(int i=0;i<classPredictions.size();i++){
            if(classPrediction.getProbability()<classPredictions.get(i).getProbability()){
                classPrediction.setClassIndex(classPredictions.get(i).getClassIndex());
                classPrediction.setProbability(classPredictions.get(i).getProbability());
            }
        }
        return classPrediction;
    }
}
