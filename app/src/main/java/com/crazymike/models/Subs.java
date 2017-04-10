package com.crazymike.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subs {
    private String tag_id;
    private String name;
    private String ename;
    private String parent_id;
    private String groups;
    private Object content;
    private Object prom_id;
    private List<?> subs;
}
