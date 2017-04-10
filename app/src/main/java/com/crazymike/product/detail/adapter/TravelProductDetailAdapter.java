package com.crazymike.product.detail.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crazymike.R;
import com.crazymike.models.Img;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.ItemList;
import com.crazymike.models.Spec;
import com.crazymike.widget.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

public class TravelProductDetailAdapter extends BaseProductDetailAdapter {

    private static final String TAG = TravelProductDetailAdapter.class.getSimpleName();
    private static final int SECTION_COUNT = 12;

    private List<CountDownHolder> countDownViewList;
    private List<SecondHolder> secondHolderList;

    public TravelProductDetailAdapter(Context context, Listener event, GridLayoutManager manager, ItemDetail itemDetail, boolean isTrack) {
        countDownViewList = new ArrayList<>();
        secondHolderList = new ArrayList<>();
        this.context = context;
        this.event = event;
        this.itemDetail = itemDetail;
        this.isTrack = isTrack;
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position < SECTION_COUNT ? manager.getSpanCount() : 1;
            }
        });
        init(context);
    }

    private void setOrderNoticeBtnView(SecondHolder secondHolder) {
        Glide.with(context)
                .load(mIsNoticeOrder ? R.mipmap.order_notice_ready : R.mipmap.order_notice_default)
                .into(secondHolder.imgOrderNotice);
        secondHolder.txtOrderNotice
                .setTextColor(context.getResources().getColor(mIsNoticeOrder ? R.color.white : R.color.text_black_none_special));
        secondHolder.txtOrderNotice
                .setText(context.getString(mIsNoticeOrder ? R.string.order_notice_me_ready : R.string.order_notice_me));
    }

    public void setCountDownTime(int sec) {
        for (CountDownHolder holder : countDownViewList) {
            int h = (sec / 3600) % 24;
            int m = (sec % 3600) / 60;
            int s = sec % 3600 % 60;
            holder.countDown.setText(String.format("%s %s %s %s %s %s %s", surplusString, h, hourString, m, minuteString, s, secondString));
        }
    }

    @Override
    public void setIsOrderNotice(boolean isNoticeOrder) {
        this.mIsNoticeOrder = isNoticeOrder;
        for (SecondHolder secondHolder : secondHolderList) {
            setOrderNoticeBtnView(secondHolder);
        }
    }

    @Override
    public int getItemCount() {
        if (itemDetail == null) return 0;
        return SECTION_COUNT + items.size();
    }

    @Override
    public int getItemViewType(int position) {
//        if (general == null) position += 1;
        switch (position) {
            case 0:
                return TOP_BANNER;
            case 1:
                return TYPE_TITLE;
            case 2:
                return TYPE_PRICE;
            case 3:
                return TYPE_COUNT_DOWN;
            case 4:
                return TYPE_IMAGE;
            case 5:
                return TYPE_SECOND;
            case 6:
                return TYPE_PRODUCT_INFO;
            case 7:
                return TYPE_PRICE;
            case 8:
                return TYPE_SECOND;
            case 9:
                return TYPE_COUNT_DOWN;
            case 10:
                return BOTTOM_BANNER;
            case 11:
                return TYPE_ITEM_HEADER;
            default:
                return TYPE_ITEM_LIST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
            case TOP_BANNER:
//                if(itemDetail.getInfo().getApp_banner1().getHref_link().equals("")){
                    return new DefaultBannerHolder(inflater.inflate(R.layout.item_product_detail_blank_banner, parent, false));
//                }else{
//                    return new TopBannerHolder(inflater.inflate(R.layout.item_product_detail_banner, parent, false));
//                }
            case BOTTOM_BANNER:
//                if(itemDetail.getInfo().getApp_banner2().getHref_link().equals("")){
                    return new DefaultBannerHolder(inflater.inflate(R.layout.item_product_detail_blank_banner, parent, false));
//                }else{
//                    return new BottomBannerHolder(inflater.inflate(R.layout.item_product_detail_banner, parent, false));
//                }
            case TYPE_TITLE:
                return new TitleHolder(inflater.inflate(R.layout.item_product_detail_travel_title, parent, false));
            case TYPE_PRICE:
                return new PriceHolder(inflater.inflate(R.layout.item_product_detail_travel_price, parent, false));
            case TYPE_COUNT_DOWN:
                return new CountDownHolder(inflater.inflate(R.layout.item_product_detail_travel_count_down, parent, false));
            case TYPE_IMAGE:
                return new ImageHolder(inflater.inflate(R.layout.item_product_detail_travel_image, parent, false));
            case TYPE_SECOND:
                return new SecondHolder(inflater.inflate(R.layout.item_product_detail_travel_second, parent, false));
            case TYPE_PRODUCT_INFO:
                return new ProductInfoHolder(inflater.inflate(R.layout.item_product_detail_travel_info, parent, false));
            case TYPE_ITEM_HEADER:
                return new ItemListHeader(inflater.inflate(R.layout.item_product_detail_travel_item_header, parent, false));
            case TYPE_ITEM_LIST:
            default:
                return new ItemListHolder(inflater.inflate(R.layout.holder_item_grid, parent, false));
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof CountDownHolder) {
            countDownViewList.remove(holder);
        }
        if (holder instanceof SecondHolder) {
            secondHolderList.remove(holder);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

//        if (holder instanceof TopBannerHolder) {
//            Glide.with(context)
//                    .load(info.getApp_banner1().getImg_url())
//                    .centerCrop()
//                    .into(((TopBannerHolder) holder).banner);
//
//            holder.itemView.setOnClickListener(v -> {
//                        ProductDetailActivity.startActivityForBanner(context, info.getApp_banner1());
//                new UserBehaviorSender().sendTopBottomBannerClick(info.getApp_banner1().getTcode());
//                ((Activity)(context)).finish();
//                    }
//            );
//
//        } else if (holder instanceof BottomBannerHolder) {
//            Glide.with(context)
//                    .load(info.getApp_banner2().getImg_url())
//                    .centerCrop()
//                    .into(((BottomBannerHolder) holder).banner);
//
//            holder.itemView.setOnClickListener(v -> {
//                        ProductDetailActivity.startActivityForBanner(context, info.getApp_banner2());
//                new UserBehaviorSender().sendTopBottomBannerClick(info.getApp_banner2().getTcode());
//                        ((Activity)(context)).finish();
//                    }
//            );
//
//        }else

        if (holder instanceof TitleHolder) {

            //name
            String titleDiscount = !discount.equals("") && info.getIs_hilife() != null && info.getIs_hilife().equals("t") ? String.format("%s%s%s!", downDiscount, discountString, discString) : "";
            Spannable spannable = new SpannableString(titleDiscount + info.getName());
            spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, titleDiscount.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((TitleHolder) holder).name.setText(spannable, TextView.BufferType.SPANNABLE);

            //subTitle
            ((TitleHolder) holder).subTitle.setText(info.getSubtitle());

        } else if (holder instanceof PriceHolder) {

            //price
            ((PriceHolder) holder).price.setText(String.format("$%s", info.getPrice_title() > 0 ? info.getPrice_title() : info.getPrice()));
            ((PriceHolder) holder).up.setVisibility(info.is_priceup() ? View.VISIBLE : View.GONE);


            //notaxString
            if (info.getWithout_is_notax() != null && info.getWithout_is_notax().equals("t")) {
                ((PriceHolder) holder).notax.setText(String.format("%s$%s", notaxString, info.getWithout_notax_price()));
            } else {
                ((PriceHolder) holder).notax.setVisibility(View.GONE);
            }

            //priceFakeString
            ((PriceHolder) holder).priceFake.setText(String.format("%s：%s%s", priceFakeString, info.getPrice_fake(), dollarsString));

            //discountString
            ((PriceHolder) holder).discount.setText(String.format("%s:%s%s", discountString, discount, discString));

            //bonus amt
            if (info.getBonus_amt() == 0) {
                ((PriceHolder) holder).bonusAmt.setVisibility(View.GONE);
                ((PriceHolder) holder).bonusAmtUp.setVisibility(View.GONE);
            } else {
                ((PriceHolder) holder).bonusAmt.setVisibility(View.VISIBLE);
                ((PriceHolder) holder).bonusAmtUp.setVisibility(View.VISIBLE);
                ((PriceHolder) holder).bonusAmt.setText(String.format(bonusAmtString, info.getBonus_amt()));
            }

            Log.i(TAG, position + "\t" + online.getIs_stock() + "\t" + !(getTime(online.getDate_online()) > date.getTime()));
            //i want buy
            ((PriceHolder) holder).iWantBuy.setVisibility(position == 2 && online.getIs_stock() && !(getTime(online.getDate_online()) > date.getTime()) ? View.VISIBLE : View.GONE);
            ((PriceHolder) holder).iWantBuy.setOnClickListener(view -> {
                if (online.getIs_stock()) event.onBuyClick();
            });

            holder.itemView.setOnClickListener(view -> {
                if (online.getIs_stock()) event.onBuyClick();
            });

        } else if (holder instanceof CountDownHolder) {

            countDownViewList.add(((CountDownHolder) holder));
            String buyItems = online.getBuy_online_items();
            Spannable buyIteSpannable = new SpannableString(String.format("%s %s / ", favorString, buyItems));
            buyIteSpannable.setSpan(new ForegroundColorSpan(Color.RED), favorString.length(), favorString.length() + buyItems.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((CountDownHolder) holder).buyItems.setText(buyIteSpannable);

        } else if (holder instanceof ImageHolder) {

            List<Img> imgList = new ArrayList<>();
            Img img = new Img();
            img.setUrl(info.getImg1_url());
            imgList.add(img);


            if (itemDetail.getImg() != null && itemDetail.getImg().size() != 0) {
                imgList.addAll(itemDetail.getImg());
                ((ImageHolder) holder).imgViewPager.setAdapter(new ImgPagerAdapter(context, imgList));
                ((ImageHolder) holder).indicator.setViewPager(((ImageHolder) holder).imgViewPager);
            } else {
                ((ImageHolder) holder).imgViewPager.setAdapter(new ImgPagerAdapter(context, imgList));
                ((ImageHolder) holder).indicator.setVisibility(View.GONE);
            }

        } else if (holder instanceof SecondHolder) {
            SecondHolder secondHolder = (SecondHolder) holder;
            secondHolderList.add(secondHolder);

            secondHolder.btnAddToCart.setVisibility(View.GONE);
            secondHolder.btnBuyNow.setVisibility(View.GONE);
            secondHolder.btnNextTime.setVisibility(View.GONE);
            secondHolder.linearLayoutOrderNotice.setVisibility(View.GONE);
            secondHolder.txtNotSale.setVisibility(View.GONE);
            secondHolder.vContact.setVisibility(View.GONE);

            secondHolder.btnAddToCart.setOnClickListener(view -> event.onAddToCartClick());
            secondHolder.btnBuyNow.setOnClickListener(view -> event.onBuyClick());
            secondHolder.linearLayoutOrderNotice.setOnClickListener(view -> event.onOrderNoticeBtnClick());

            if(info.getSwrule_deliver_hours()== 24){
                secondHolder.deliver_icon.setImageResource(R.mipmap.deliver_24hr);
            }else if(info.getSwrule_deliver_hours()== 48){
                secondHolder.deliver_icon.setImageResource(R.mipmap.deliver_48hr);
            }

            if (online.getIs_stock() && getTime(online.getDate_online()) <= date.getTime()) {

                if (!online.getChannel_id().equals("13") && !info.getIs_hilife_shop().equals("t")) {
                    secondHolder.btnAddToCart.setVisibility(View.VISIBLE);
                }

                if (isTravelTag || (itemDetail.getInfo().getIs_hilife_shop() != null && itemDetail.getInfo().getIs_hilife_shop().equals("t"))) {
                    secondHolder.btnAddToCart.setVisibility(View.GONE);
                }

                if (online.getChannel_id().equals("13")) {
                    secondHolder.vContact.setVisibility(View.VISIBLE);
                }

                secondHolder.btnBuyNow.setVisibility(View.VISIBLE);
            } else if (online.getIs_stock() && getTime(online.getDate_online()) > date.getTime()) {
                secondHolder.txtNotSale.setVisibility(View.VISIBLE);
            } else {
                for (Spec spec : itemDetail.getSpecs()) {
                    String isShowSaleOut = spec.getIs_show_sale_out();
                    String useOrderNotice = info.getUse_order_notice();
                    if (isShowSaleOut != null && useOrderNotice != null && isShowSaleOut.equals("t") && useOrderNotice.equals("true")) {
                        secondHolder.linearLayoutOrderNotice.setVisibility(View.VISIBLE);
                        setOrderNoticeBtnView(secondHolder);
                        return;
                    }
                }
                secondHolder.btnNextTime.setVisibility(View.VISIBLE);
            }

            secondHolder.imgTrack.setImageResource(isTrack ? R.mipmap.like_active : R.mipmap.like64x64);
            secondHolder.imgTrack.setOnClickListener(view -> event.onTrackClick());
            secondHolder.imgShare.setOnClickListener(view -> event.onShareBtnClick());
            secondHolder.linearLayoutSpecialistAdvice.setOnClickListener(view -> event.onSpecialistAdviceBtnClick(itemDetail.getInfo().getItem_id()));

        } else if (holder instanceof ProductInfoHolder) {

            settingWebView(((ProductInfoHolder) holder).deliverNotice, itemDetail.getDeliver_notic());
            settingYoutubeWebView(((ProductInfoHolder) holder).youtube1, info.getYoutube_url1());
            settingYoutubeWebView(((ProductInfoHolder) holder).youtube2, info.getYoutube_url2());
            settingWebView(((ProductInfoHolder) holder).content, info.getContent());
            settingWebView(((ProductInfoHolder) holder).buyReason, info.getBuy_reason());

            if (info.getMade_in() != null && !info.getMade_in().equals("")) {
                ((ProductInfoHolder) holder).madeIn.setText(info.getMade_in());
            }

            if (info.getItem_state() != null && !info.getItem_state().equals("")) {
                ((ProductInfoHolder) holder).itemState.setText(info.getItem_state());
            }

            settingWebView(((ProductInfoHolder) holder).specification, info.getSpecification());
            settingWebView(((ProductInfoHolder) holder).returnNotice, itemDetail.getReturn_notice());

        } else if (holder instanceof ItemListHolder) {

            int itemPosition = position - SECTION_COUNT;
            if (itemPosition < 0 || items == null || items.size() == 0) return;

            ItemList itemList = items.get(itemPosition);

            String imgUrl = itemList.getPimg_m() == null || itemList.getPimg_m().equals("") ? itemList.getPimg_m() : itemList.getPimg();
            Glide.with(context).load(imgUrl)
                    .centerCrop()
                    .placeholder(R.mipmap.bg_download)
                    .into(((ItemListHolder) holder).thumb);

            ((ItemListHolder) holder).stock.setVisibility(itemList.getStock().equals("0") || itemList.getStock().equals("") ? View.VISIBLE : View.GONE);

            ((ItemListHolder) holder).name.setText(itemList.getPname());
            ((ItemListHolder) holder).priceFake.setText(String.format("$%s", itemList.getPrice_fake()));
            Paint paint = ((ItemListHolder) holder).priceFake.getPaint();
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            paint.setAntiAlias(true);

            if (itemList.getPrice_title() != null && !itemList.getPrice_title().equals("0") && itemList.getIs_priceup().equals("1")) {
                ((ItemListHolder) holder).priceUp.setVisibility(View.VISIBLE);
                ((ItemListHolder) holder).priceTitle.setText(String.format("$%s", itemList.getPrice_title()));
            } else {
                ((ItemListHolder) holder).priceUp.setVisibility(View.GONE);
                ((ItemListHolder) holder).priceTitle.setText(String.format("$%s", itemList.getPrice()));
            }

            holder.itemView.setOnClickListener(v -> event.onItemListClick(itemList));
        }
    }



    class DefaultBannerHolder extends RecyclerView.ViewHolder {
        DefaultBannerHolder(View itemView) {
            super(itemView);
        }
    }

    class TopBannerHolder extends RecyclerView.ViewHolder {
        ImageView banner;

        TopBannerHolder(View itemView) {
            super(itemView);
            banner = (ImageView) itemView.findViewById(R.id.banner);
        }
    }

    class BottomBannerHolder extends RecyclerView.ViewHolder {
        ImageView banner;

        BottomBannerHolder(View itemView) {
            super(itemView);
            banner = (ImageView) itemView.findViewById(R.id.banner);
        }
    }

    private class TitleHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView subTitle;

        TitleHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            subTitle = (TextView) itemView.findViewById(R.id.subTitle);
        }
    }

    private class PriceHolder extends RecyclerView.ViewHolder {

        TextView price;
        TextView up;
        TextView notax;
        TextView priceFake;
        TextView discount;
        TextView bonusAmt;
        TextView bonusAmtUp;
        Button iWantBuy;

        PriceHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.price);
            up = (TextView) itemView.findViewById(R.id.up);
            notax = (TextView) itemView.findViewById(R.id.notax);
            priceFake = (TextView) itemView.findViewById(R.id.priceFake);
            discount = (TextView) itemView.findViewById(R.id.discount);
            bonusAmt = (TextView) itemView.findViewById(R.id.bonusAmt);
            bonusAmtUp = (TextView) itemView.findViewById(R.id.bonusAmtUp);
            iWantBuy = (Button) itemView.findViewById(R.id.iWantBuy);
        }
    }

    private class CountDownHolder extends RecyclerView.ViewHolder {

        TextView buyItems;
        TextView countDown;

        CountDownHolder(View itemView) {
            super(itemView);

            buyItems = (TextView) itemView.findViewById(R.id.text_view_buy_items);
            countDown = (TextView) itemView.findViewById(R.id.text_view_count_down);
        }
    }

    private class ImageHolder extends RecyclerView.ViewHolder {

        ViewPager imgViewPager;
        CircleIndicator indicator;

        ImageHolder(View itemView) {
            super(itemView);

            imgViewPager = (ViewPager) itemView.findViewById(R.id.imgViewPager);
            indicator = (CircleIndicator) itemView.findViewById(R.id.indicator);
        }
    }

    private class SecondHolder extends RecyclerView.ViewHolder {

        private Button btnAddToCart;
        private Button btnBuyNow;
        private TextView txtNotSale;
        private Button btnNextTime;
        private LinearLayout linearLayoutOrderNotice;
        private ImageView imgOrderNotice;
        private TextView txtOrderNotice;
        private View vContact;
        private ImageView deliver_icon,imgTrack, imgShare;
        private LinearLayout linearLayoutSpecialistAdvice;

        SecondHolder(View itemView) {
            super(itemView);

            btnAddToCart = (Button) itemView.findViewById(R.id.addToCart);
            btnBuyNow = (Button) itemView.findViewById(R.id.buyNow);
            txtNotSale = (TextView) itemView.findViewById(R.id.notSale);
            btnNextTime = (Button) itemView.findViewById(R.id.nextTime);
            linearLayoutOrderNotice = (LinearLayout) itemView.findViewById(R.id.ll_order_notice);
            imgOrderNotice = (ImageView) itemView.findViewById(R.id.img_order_notice);
            txtOrderNotice = (TextView) itemView.findViewById(R.id.txt_order_notice);
            vContact = itemView.findViewById(R.id.contact);
            deliver_icon = (ImageView) itemView.findViewById(R.id.deliver_icon);
            imgTrack = (ImageView) itemView.findViewById(R.id.image_view_favorite);
            imgShare = (ImageView) itemView.findViewById(R.id.share);
            linearLayoutSpecialistAdvice = (LinearLayout) itemView.findViewById(R.id.linearLayout_specialistAdvice);
        }
    }

    private class ProductInfoHolder extends RecyclerView.ViewHolder {

        View expectDeliverLayout;
        WebView deliverNotice;
        WebView youtube1;
        WebView youtube2;
        WebView content;
        WebView buyReason;
        TextView madeIn;
        TextView itemState;
        WebView specification;
        WebView returnNotice;

        ProductInfoHolder(View itemView) {
            super(itemView);
            expectDeliverLayout = itemView.findViewById(R.id.expectDeliverLayout);
            deliverNotice = (WebView) itemView.findViewById(R.id.deliverNotice);
            youtube1 = (WebView) itemView.findViewById(R.id.youtobe1);
            youtube2 = (WebView) itemView.findViewById(R.id.youtobe2);
            content = (WebView) itemView.findViewById(R.id.content);
            buyReason = (WebView) itemView.findViewById(R.id.buyReason);
            madeIn = (TextView) itemView.findViewById(R.id.madeIn);
            itemState = (TextView) itemView.findViewById(R.id.itemState);
            specification = (WebView) itemView.findViewById(R.id.specification);
            returnNotice = (WebView) itemView.findViewById(R.id.returnNotice);
        }
    }

    private class ItemListHeader extends RecyclerView.ViewHolder {

        ItemListHeader(View itemView) {
            super(itemView);
        }
    }

    private class ItemListHolder extends RecyclerView.ViewHolder {

        ImageView thumb;
        ImageView stock;
        TextView name;
        TextView priceFake;
        TextView priceTitle;
        TextView priceUp;

        ItemListHolder(View itemView) {
            super(itemView);
            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            stock = (ImageView) itemView.findViewById(R.id.stock);
            name = (TextView) itemView.findViewById(R.id.name);
            priceFake = (TextView) itemView.findViewById(R.id.priceFake);
            priceTitle = (TextView) itemView.findViewById(R.id.priceTitle);
            priceUp = (TextView) itemView.findViewById(R.id.priceUp);
        }
    }
}
