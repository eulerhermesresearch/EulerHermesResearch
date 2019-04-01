package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.model.CountryRisks;

public class CountryRiskRequest extends NubyServiceRequest<CountryRisks> {
    public CountryRiskRequest() {
        super(CountryRisks.class);
    }

    protected String endPoint() {
        return "/countryrisklist";
    }

    public String createCacheKey() {
        return "countryrisklist";
    }
}
