package com.crazymike.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FtRule {

    private String fee_freight;
    private String lmt_type;
    private String lmt_buy_lower;
    private String lmt_buy_upper;
    private String lmt_price_lower;
    private String lmt_price_upper;
    private String is_topay;

}
