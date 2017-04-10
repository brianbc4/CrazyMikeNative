package com.crazymike.product.spec.counter;

import com.crazymike.models.ItemDetail;
import com.crazymike.models.Sale;
import com.crazymike.models.Spec;
import com.crazymike.models.SpecCounter;
import com.crazymike.respositories.CartRepository;
import com.crazymike.respositories.ProductRepository;
import com.crazymike.respositories.SpecCounterRepository;
import com.crazymike.util.RxUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChaoJen on 2017/2/24.
 */

public class SpecCounterPresenter implements SpecCounterContract.Presenter {

    private static final String TAG = SpecCounterPresenter.class.getSimpleName();

    private SpecCounterContract.View mView;
    private ItemDetail mItemDetail;

    public SpecCounterPresenter(SpecCounterContract.View view) {
        mView = view;

        SpecCounterRepository.getInstance().clearSpecList();
        SpecCounterRepository.getInstance().getSpecCounterListChangedObservable()
                .compose(RxUtil.bindLifecycle(view))
                .subscribe(specCounterList -> {
                    view.onSpecCountChanged(specCounterList);
                }, Throwable::printStackTrace);
    }

    @Override
    public void getItemDetail(String itemId) {
        ProductRepository.getInstance().getItemDetail(itemId)
                .compose(RxUtil.mainAsync())
                .compose(RxUtil.bindLifecycle(mView))
                .subscribe(itemDetail -> {
                    mView.onItemDetailGet(itemDetail);
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void setItemDetail(ItemDetail itemDetail) {
        mItemDetail = itemDetail;
    }

    @Override
    public ItemDetail getItemDetail() {
        return mItemDetail;
    }

    @Override
    public void increaseSpecCount(Spec spec) {
        if (SpecCounterRepository.getInstance().getProductCount() < Integer.valueOf(mItemDetail.getInfo().getMax_order())) {
            SpecCounterRepository.getInstance().increaseSpecCount(spec);
        }
    }

    @Override
    public void decreaseSpecCount(Spec spec) {
        SpecCounterRepository.getInstance().decreaseSpecCount(spec);
    }

    @Override
    public void checkSpecListSize() {
        int specListSize = mItemDetail.getSpecs().size();
        if (specListSize == 1) SpecCounterRepository.getInstance().increaseSpecCount(mItemDetail.getSpecs().get(0));
        mView.onSpecListSizeChecked(specListSize);
    }

    @Override
    public void getBuyMoreInfo() {
        List<Sale> saleList = mItemDetail.getSales();
        int productCount = SpecCounterRepository.getInstance().getProductCount();
        int buyMore = 0;
        int discount;
        int buyMoreAveragePrice = 0;

        for (Sale sale : saleList) {
            if (productCount < Integer.valueOf(sale.getQty())) {
                buyMore = Integer.valueOf(sale.getQty()) - productCount;
                discount = (int) Math.ceil(Double.valueOf(sale.getPrice_discount()));
                buyMoreAveragePrice = Integer.valueOf(mItemDetail.getInfo().getPrice()) - discount;
                mView.onBuyMoreDiscountChanged(buyMore, buyMoreAveragePrice, mItemDetail.getInfo().getUnit());
                return;
            }
        }
    }

    @Override
    public void addToCart() {
        List<SpecForRequest> specList = new ArrayList<>();
        for (SpecCounter specCounter : SpecCounterRepository.getInstance().getSpecCounterList()) {
            specList.add(new SpecForRequest(specCounter.getSpec().getSpec_id(), specCounter.getQty()));
        }
        mView.onAddToCart(mItemDetail.getInfo().getItem_id(), SpecCounterRepository.getInstance().getProductCount(), new Gson().toJson(specList));
        CartRepository.getInstance().addCart(mItemDetail.getInfo().getItem_id(), SpecCounterRepository.getInstance().getProductCount(), new Gson().toJson(specList));
    }

    private class SpecForRequest {
        private String spec_id;
        private int qty;

        public SpecForRequest(String spec_id, int qty) {
            this.spec_id = spec_id;
            this.qty = qty;
        }
    }
}
