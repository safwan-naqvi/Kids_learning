package com.example.kidsappfyp.Activities.GeoShapes;

public class ShapesModel {
    int shapeImage;
    String shapeName;
    String shapeDescription;

    public ShapesModel(int shapeImage, String shapeName, String shapeDescription) {
        this.shapeImage = shapeImage;
        this.shapeName = shapeName;
        this.shapeDescription = shapeDescription;
    }

    public int getShapeImage() {
        return shapeImage;
    }

    public void setShapeImage(int shapeImage) {
        this.shapeImage = shapeImage;
    }

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }

    public String getShapeDescription() {
        return shapeDescription;
    }

    public void setShapeDescription(String shapeDescription) {
        this.shapeDescription = shapeDescription;
    }
}
