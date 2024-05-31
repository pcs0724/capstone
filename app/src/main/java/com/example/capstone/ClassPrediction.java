package com.example.capstone;

public class ClassPrediction {//클래스 별 이름, 정확도
    private int classIndex;
    private float probability;

    public ClassPrediction(int classIndex, float probability) {
        this.classIndex = classIndex;
        this.probability = probability;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public float getProbability() {
        return probability;
    }
    public void setClassIndex(int classIndex) {
        this.classIndex=classIndex;
    }

    public void setProbability(float probability) {

        this.probability=probability;
    }

}
