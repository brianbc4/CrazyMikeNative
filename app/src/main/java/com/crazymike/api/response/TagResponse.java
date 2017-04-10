package com.crazymike.api.response;

import com.crazymike.models.Tag;

import java.util.List;

import lombok.Getter;

@Getter
public class TagResponse extends BaseResponse{

    private List<Tag> rtn;
}
