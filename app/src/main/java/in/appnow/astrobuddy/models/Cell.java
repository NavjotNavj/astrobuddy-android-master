package in.appnow.astrobuddy.models;

import com.evrencoskun.tableview.sort.ISortableModel;

/**
 * Created by sonu on 14:22, 06/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class Cell {

    private String mId;
    private String label;

    public Cell(String id) {
        this.mId = id;
    }

    public Cell(String mId, String label) {
        this.mId = mId;
        this.label = label;
    }

    public String getId() {
        return mId;
    }

    public String getLabel() {
        return label;
    }
}
