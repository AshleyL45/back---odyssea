package com.example.odyssea.entities.mainTables;

public class Image {

    private int idImage;
    private int idEntity;
    private String entityType;
    private String sizeType;
    private String link;
    private String altText;


    public Image(){}
    public Image(int idImage, int idEntity, String entityType, String sizeType, String link, String altText){
        this.idImage = idImage;
        this.idEntity =idEntity;
        this.entityType = entityType;
        this.sizeType = sizeType;
        this.link = link;
        this.altText = altText;
    }


    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImages) {
        this.idImage = idImages;
    }

    public int getIdEntity() {
        return idEntity;
    }

    public void setIdEntity(int idEntity) {
        this.idEntity = idEntity;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }


    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }
}
