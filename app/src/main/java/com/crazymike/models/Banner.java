package com.crazymike.models;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Banner {

    private String banner_id;
    private String type;
    private String code;
    private String img;
    private String img_width;
    private String img_height;
    private String img_wh_style;
    private Object url;
    private String status;
    private List<RotateJson> rotate_json;
}
