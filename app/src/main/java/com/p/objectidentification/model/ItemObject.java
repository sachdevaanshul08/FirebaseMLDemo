package com.p.objectidentification.model;

/**
 * Created by anshulsachdeva on 07/12/18.
 */

public class ItemObject {

    private String name;
    private String imagePath;
    private int index;

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
