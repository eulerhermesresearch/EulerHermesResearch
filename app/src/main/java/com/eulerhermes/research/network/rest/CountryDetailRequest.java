package com.eulerhermes.research.network.rest;

import android.util.Log;
import com.eulerhermes.research.model.CountryDetail;

public class CountryDetailRequest extends NubyServiceRequest<CountryDetail> {
    private final String iso;

    public CountryDetailRequest(String iso) {
        super(CountryDetail.class);
        this.iso = iso;
    }

    protected String endPoint() {
        Log.d("CountryDetailRequest", "endPoint: countrydetail/" + this.iso);
        return "/countrydetail/" + this.iso.toLowerCase();
    }

    public String createCacheKey() {
        return "countrydetail_" + this.iso;
    }
}
