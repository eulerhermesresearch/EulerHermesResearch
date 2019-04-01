package com.eulerhermes.research.adapter.holder;

import android.widget.TextView;

public class KeyFiguresHolder {
    public final TextView gdp;
    public final TextView gdpPerCapital;
    public final TextView population;

    public KeyFiguresHolder(TextView gdp, TextView gdpPerCapital, TextView population) {
        this.gdp = gdp;
        this.gdpPerCapital = gdpPerCapital;
        this.population = population;
    }
}
