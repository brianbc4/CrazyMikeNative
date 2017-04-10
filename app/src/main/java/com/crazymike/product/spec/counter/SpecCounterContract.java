package com.crazymike.product.spec.counter;

import com.crazymike.models.ItemDetail;
import com.crazymike.models.Spec;
import com.crazymike.models.SpecCounter;

import java.util.List;

/**
 * Created by ChaoJen on 2017/2/24.
 */

public interface SpecCounterContract {
    interface View {

        void initView(ItemDetail itemDetail);

        void onItemDetailGet(ItemDetail itemDetail);

        void onSpecListSizeChecked(int specListSize);

        void onSpecCountChanged(List<SpecCounter> specCounterList);

        void onAddToCart(String item_id, int qty, String specs);

        void onBuyMoreDiscountChanged(int buyMore, int buyMoreAverage, String unit);
    }

    interface Presenter {

        void increaseSpecCount(Spec spec);

        void decreaseSpecCount(Spec spec);

        void getItemDetail(String itemId);

        void checkSpecListSize();

        ItemDetail getItemDetail();

        void setItemDetail(ItemDetail itemDetail);

        void addToCart();

        void getBuyMoreInfo();
    }
}
