package com.crazymike.models;


import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class Cart {

    int item_id;
    int qty;
    String sale_id;
}
