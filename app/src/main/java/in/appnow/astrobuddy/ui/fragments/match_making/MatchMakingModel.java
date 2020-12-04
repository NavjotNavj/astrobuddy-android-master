package in.appnow.astrobuddy.ui.fragments.match_making;

/**
 * Created by sonu on 19:25, 09/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class MatchMakingModel {
    private String className, description, maleKootAttribute, femaleKootAttribute;
    private int totalPoints, receivedPoints;

    public MatchMakingModel(String className, String description, String maleKootAttribute, String femaleKootAttribute, int totalPoints, int receivedPoints) {
        this.className = className;
        this.description = description;
        this.maleKootAttribute = maleKootAttribute;
        this.femaleKootAttribute = femaleKootAttribute;
        this.totalPoints = totalPoints;
        this.receivedPoints = receivedPoints;
    }

    public String getClassName() {
        return className;
    }

    public String getDescription() {
        return description;
    }

    public String getMaleKootAttribute() {
        return maleKootAttribute;
    }

    public String getFemaleKootAttribute() {
        return femaleKootAttribute;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getReceivedPoints() {
        return receivedPoints;
    }
}
