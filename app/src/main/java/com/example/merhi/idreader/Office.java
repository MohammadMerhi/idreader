package com.example.merhi.idreader;

/**
 * Created by Merhi on 4/1/2018.
 */

public class Office {

    private int id;
    private String officeName;
    private int floorNb;
    private String premiseOwner;

    public Office() {

    }

    public Office(int id, String officeName, int floorNb, String premiseOwner) {
        this.id = id;
        this.officeName = officeName;
        this.floorNb = floorNb;
        this.premiseOwner = premiseOwner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public int getFloorNb() {
        return floorNb;
    }

    public void setFloorNb(int floorNb) {
        this.floorNb = floorNb;
    }

    public String getPremiseOwner() {
        return premiseOwner;
    }

    public void setPremiseOwner(String premiseOwner) {
        this.premiseOwner = premiseOwner;
    }

    @Override
    public String toString() {
        return officeName + " - " + floorNb + " - " + premiseOwner;
    }
}
