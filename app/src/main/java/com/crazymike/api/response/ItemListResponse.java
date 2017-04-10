package com.crazymike.api.response;

import com.crazymike.models.ItemList;

import java.util.List;

import lombok.Getter;

@Getter
public class ItemListResponse extends BaseResponse{


    /**
     * channel : 5
     * online_id : 86098
     * item_id : 63683
     * pname : 向日葵大燈霧化~車身刮傷強效修復劑(不分車色)
     * unit : 支
     * price_fake : 699
     * price_title : 339
     * pimg : https://img.crazymike.tw/upload/product/195/248/m63683_1_1470129969.jpg
     * pimg_m : https://img.crazymike.tw/upload/product/195/248/m63683_1_1470129969.jpg
     * price : 339
     * fee_freight :
     * date_offline : 2016-08-09 09:00:00
     * date_online : 2016-08-02 09:00:00
     * stock : 1
     * buy_items : 10
     * main_tag : 1519
     * main_tree : /car-motorcycle-bike/auto-accessories
     * item_url : https://crazymike.tw/product/car-motorcycle-bike/auto-accessories/item-63683
     * is_priceup : 1
     * style_page_display_type :
     * channel_name : 瘋賣團購
     * is_new : t
     * is_72hours : t
     * preorder_item_type :
     * is_cvspay : f
     */

    private List<ItemList> rtn;
}
