package in.appnow.astrobuddy.models;

/**
 * Created by sonu on 11:27, 20/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PlanetryPositionsModel {
    private String id, name, sign, signLord, nakshatra, nakshatraLord;
    private double fullDegree, normDegree, speed;
    private boolean isRetro;
    private int house;

    public PlanetryPositionsModel(){
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setSignLord(String signLord) {
        this.signLord = signLord;
    }

    public void setNakshatra(String nakshatra) {
        this.nakshatra = nakshatra;
    }

    public void setNakshatraLord(String nakshatraLord) {
        this.nakshatraLord = nakshatraLord;
    }

    public void setFullDegree(double fullDegree) {
        this.fullDegree = fullDegree;
    }

    public void setNormDegree(double normDegree) {
        this.normDegree = normDegree;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setRetro(boolean retro) {
        isRetro = retro;
    }

    public void setHouse(int house) {
        this.house = house;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSign() {
        return sign;
    }

    public String getSignLord() {
        return signLord;
    }

    public String getNakshatra() {
        return nakshatra;
    }

    public String getNakshatraLord() {
        return nakshatraLord;
    }

    public double getFullDegree() {
        return fullDegree;
    }

    public double getNormDegree() {
        return normDegree;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isRetro() {
        return isRetro;
    }

    public int getHouse() {
        return house;
    }

    @Override
    public String toString() {
        return "PlanetryPositionsModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sign='" + sign + '\'' +
                ", signLord='" + signLord + '\'' +
                ", nakshatra='" + nakshatra + '\'' +
                ", nakshatraLord='" + nakshatraLord + '\'' +
                ", fullDegree=" + fullDegree +
                ", normDegree=" + normDegree +
                ", speed=" + speed +
                ", isRetro=" + isRetro +
                ", house=" + house +
                '}';
    }
}
