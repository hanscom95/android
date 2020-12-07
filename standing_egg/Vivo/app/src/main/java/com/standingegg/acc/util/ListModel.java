package com.standingegg.acc.util;

/**
 * Created by moon on 2016-11-30.
 */

public class ListModel {
    String name;
    String type;
    String version_number;
    String feature;
    String walking;
    String running;
    String _key;

    public ListModel(String name, String type, String version_number, String feature, String walking, String running, String _key) {
        this.name=name;
        this.type=type;
        this.version_number=version_number;
        this.feature=feature;
        this.walking=walking;
        this.running=running;
        this._key=_key;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getVersion_number() {
        return version_number;
    }

    public String getFeature() {
        return feature;
    }

    public String getWalking() {
        return walking;
    }

    public String getRunning() {
        return running;
    }

    public String getKey() {
        return _key;
    }
}
