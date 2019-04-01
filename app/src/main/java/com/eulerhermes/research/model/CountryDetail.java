package com.eulerhermes.research.model;

import com.yabeman.android.extended.items.BaseType;
import com.google.api.client.util.Key;
import java.util.ArrayList;

public class CountryDetail extends BaseType {
    @Key("GDP")
    private float gdp;
    @Key("GDPpc")
    private int gdppc;
    @Key("Grade")
    private String grade;
    @Key("ISO")
    private String iso;
    @Key("LatestReports")
    private Docs latestReports;
    @Key("Pop")
    private float pop;
    @Key("RiskLevel")
    private int riskLevel;
    @Key("Strengths")
    private Strengths strengths;
    @Key("Weaknesses")
    private Weaknesses weaknesses;

    public static class Strengths extends ArrayList<String> {
    }

    public static class Weaknesses extends ArrayList<String> {
    }

    public float getGdp() {
        return this.gdp;
    }

    public void setGdp(float gdp) {
        this.gdp = gdp;
    }

    public int getGdppc() {
        return this.gdppc;
    }

    public void setGdppc(int gdppc) {
        this.gdppc = gdppc;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getIso() {
        return this.iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public Docs getLatestReports() {
        return this.latestReports;
    }

    public void setLatestReports(Docs latestReports) {
        this.latestReports = latestReports;
    }

    public float getPop() {
        return this.pop;
    }

    public void setPop(float pop) {
        this.pop = pop;
    }

    public int getRiskLevel() {
        return this.riskLevel;
    }

    public void setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Strengths getStrengths() {
        return this.strengths;
    }

    public void setStrengths(Strengths strengths) {
        this.strengths = strengths;
    }

    public Weaknesses getWeaknesses() {
        return this.weaknesses;
    }

    public void setWeaknesses(Weaknesses weaknesses) {
        this.weaknesses = weaknesses;
    }

    public int getItemViewType() {
        return 102;
    }

    public String getItemPrimaryLabel() {
        return null;
    }
}
