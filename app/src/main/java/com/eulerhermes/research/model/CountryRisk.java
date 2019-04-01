package com.eulerhermes.research.model;

import com.google.api.client.util.Key;

public class CountryRisk {
    @Key("i")
    private String id;
    @Key("r")
    private int risk;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRisk() {
        return this.risk;
    }

    public void setRisk(int risk) {
        this.risk = risk;
    }
}
