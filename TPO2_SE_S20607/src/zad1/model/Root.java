package zad1.model;

import java.util.List;

public class Root {
    public String table;
    public String currency;
    public String code;
    public List<Rate> rates;
}

class Rate {
    public String no;
    public String effectiveDate;
    public double mid;
}



