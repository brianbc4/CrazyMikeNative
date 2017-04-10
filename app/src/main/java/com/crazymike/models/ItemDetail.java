package com.crazymike.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDetail {

    private Online online;
    private Info info;
    private List<Spec> specs = new ArrayList<>();
    private List<Sale> sales = new ArrayList<>();
    private List<Img> img = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();
//    private List<FtRule> ftRule = new ArrayList<>();
    private String return_notice;
    private String deliver_notic;
}
