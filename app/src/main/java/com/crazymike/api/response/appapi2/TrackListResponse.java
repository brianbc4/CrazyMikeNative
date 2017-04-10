package com.crazymike.api.response.appapi2;

import com.crazymike.api.response.BaseResponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackListResponse extends BaseResponse{

    private List<Integer> rtn;

}
