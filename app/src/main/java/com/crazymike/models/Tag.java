package com.crazymike.models;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Tag {
    private String tag_id;
    private String name;
    private Object ename;
    private String parent_id;
    private String groups;
    private Object content;
    private Object prom_id;
    private ArrayList<Tag> subs;
    /**
     * tag_id : 1464
     * name : 滑鼠/鍵盤
     * ename : Mouse-Keyboard
     * parent_id : 1432
     * groups : PURPLE
     * content : null
     * prom_id : null
     * subs : []
     */
}
