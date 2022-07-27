package com.example.kidsappfyp.Activities.Numbers;

public class NumbersSecondaryModel {
    int imageNumber;
    String textNumber;
    int handNumber;

    public NumbersSecondaryModel(int imageNumber, String textNumber, int handNumber) {
        this.imageNumber = imageNumber;
        this.textNumber = textNumber;
        this.handNumber = handNumber;
    }

    public int getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }

    public String getTextNumber() {
        return textNumber;
    }

    public void setTextNumber(String textNumber) {
        this.textNumber = textNumber;
    }

    public int getHandNumber() {
        return handNumber;
    }

    public void setHandNumber(int handNumber) {
        this.handNumber = handNumber;
    }
}
