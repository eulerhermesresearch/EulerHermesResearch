package com.eulerhermes.research.model;

import com.yabeman.android.extended.items.BaseType;
import com.google.api.client.util.Key;

public class CountryDetailEHC extends BaseType {
    @Key("CourtLevel")
    private int courtLevel;
    @Key("EnglishName")
    private String englishName;
    @Key("InsolvencyLevel")
    private int insolvencyLevel;
    @Key("ISO")
    private String iso;
    @Key("LatestReports")
    private Docs latestReports;
    @Key("PaymentLevel")
    private int paymentLevel;
    @Key("RiskLevel")
    private int riskLevel;

    public int getCourtLevel() {
        return this.courtLevel;
    }

    public void setCourtLevel(int courtLevel) {
        this.courtLevel = courtLevel;
    }

    public int getPaymentLevel() {
        return this.paymentLevel;
    }

    public void setPaymentLevel(int paymentLevel) {
        this.paymentLevel = paymentLevel;
    }

    public int getRiskLevel() {
        return this.riskLevel;
    }

    public void setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
    }

    public int getInsolvencyLevel() {
        return this.insolvencyLevel;
    }

    public void setInsolvencyLevel(int insolvencyLevel) {
        this.insolvencyLevel = insolvencyLevel;
    }

    public String getEnglishName() {
        return this.englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public Docs getLatestReports() {
        return this.latestReports;
    }

    public void setLatestReports(Docs latestReports) {
        this.latestReports = latestReports;
    }

    public String getIso() {
        return this.iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public int getItemViewType() {
        return 102;
    }

    public String getItemPrimaryLabel() {
        return null;
    }
}
