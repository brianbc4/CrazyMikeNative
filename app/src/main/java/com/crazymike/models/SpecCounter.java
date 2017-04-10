package com.crazymike.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ChaoJen on 2017/2/24.
 */

@Getter
@Setter
public class SpecCounter {

    private Spec spec;
    private int qty;

    public SpecCounter(Spec spec, int qty) {
        this.spec = spec;
        this.qty = qty;
    }
}
