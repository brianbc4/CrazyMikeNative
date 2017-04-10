package com.crazymike.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Spec {
    private String spec_id;
    private String item_id;
    private String spec_value;
    private String spec_stock;
    private String is_show_sale_out;
    private String group_name;
}
