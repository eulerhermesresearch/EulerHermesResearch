package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.model.CountryDetailEHC;

public class CountryDetailEHCRequest extends EHCRequest<CountryDetailEHC> {
    private final String iso;

    public CountryDetailEHCRequest(String iso) {
        super(CountryDetailEHC.class);
        this.iso = iso;
    }

    protected String endPoint() {
        return "/countrydetail/" + this.iso.toLowerCase();
    }

    public String createCacheKey() {
        return "countrydetailehc_" + this.iso;
    }
}
