package com.crazymike.product.detail.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crazymike.R;
import com.crazymike.models.Img;
import com.crazymike.models.ItemDetail;
import com.crazymike.models.ItemList;
import com.crazymike.widget.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

public class NormalProductDetailAdapter extends BaseProductDetailAdapter {

    private static final int SECTION_COUNT = 9;

    private List<ProductInfoHolder> countDownViewList;

    @Override
    public void setIsOrderNotice(boolean isNoticeOrder) {

    }

    public void setCountDownTime(int sec) {
        for (ProductInfoHolder holder : countDownViewList) {
            int h = (sec / 3600) % 24;
            int m = (sec % 3600) / 60;
            int s = sec % 3600 % 60;
            holder.textViewCountDown.setText(String.format("%s %s %s %s %s %s %s", surplusString, h, hourString, m, minuteString, s, secondString));
        }
    }

    public NormalProductDetailAdapter(Context context, Listener event, GridLayoutManager manager, ItemDetail itemDetail, boolean isTrack) {
        countDownViewList = new ArrayList<>();
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
                return TYPE_TITLE;
            case 1:
                return TYPE_IMAGE;
            case 2:
                return TYPE_PRICE;
            case 3:
                return TYPE_PRODUCT_INFO;
            case 4:
                return TYPE_SALES;
            case 5:
                return TYPE_SECOND;
            case 6:
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
//                if(itemDetail.getInfo().getApp_banner1().equals("")){
                    return new DefaultBannerHolder(inflater.inflate(R.layout.item_product_detail_blank_banner, parent, false));
//                }else{
//                    return new TopBannerHolder(inflater.inflate(R.layout.item_product_detail_top_banner, parent, false));
//                }
            case BOTTOM_BANNER:
//                if(itemDetail.getInfo().getApp_banner2().equals("")){
                    return new DefaultBannerHolder(inflater.inflate(R.layout.item_product_detail_blank_banner, parent, false));
//                }else{
//                    return new BottomBannerHolder(inflater.inflate(R.layout.item_product_detail_bottom_banner, parent, false));
//                }
            case TYPE_TITLE:
                return new TitleHolder(inflater.inflate(R.layout.item_product_detail_title, parent, false));
            case TYPE_PRICE:
                return new PriceHolder(inflater.inflate(R.layout.item_product_detail_price, parent, false));
            case TYPE_COUNT_DOWN:
                return new CountDownHolder(inflater.inflate(R.layout.item_product_detail_travel_count_down, parent, false));
            case TYPE_IMAGE:
                return new ImageHolder(inflater.inflate(R.layout.item_product_detail_image, parent, false));
            case TYPE_SECOND:
                return new SecondHolder(inflater.inflate(R.layout.item_product_detail_second, parent, false));
            case TYPE_PRODUCT_INFO:
                return new ProductInfoHolder(inflater.inflate(R.layout.item_product_detail_info, parent, false));
            case TYPE_SALES:
                return new SalesHolder(inflater.inflate(R.layout.item_product_detail_sales, parent, false));
            case TYPE_ITEM_HEADER:
                return new ItemListHeader(inflater.inflate(R.layout.item_product_detail_item_header, parent, false));
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
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
 if (holder instanceof TitleHolder) {
            TitleHolder titleHolder = (TitleHolder) holder;

            //name
            String titleDiscount = !discount.equals("") && info.getIs_hilife() != null && info.getIs_hilife().equals("t") ? String.format("%s%s%s!", downDiscount, discountString, discString) : "";
            Spannable spannable = new SpannableString(titleDiscount + info.getName());
            spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, titleDiscount.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleHolder.txtName.setText(spannable, TextView.BufferType.SPANNABLE);

            //sub title
            titleHolder.txtSubTitle.setText(info.getSubtitle());

        } else if (holder instanceof PriceHolder) {
            PriceHolder priceHolder = (PriceHolder) holder;

            //price
            if(info.getPrice_title() != Integer.valueOf(info.getPrice())) {
                priceHolder.txtPrice.setText(String.format("$%s ~ $%s", info.getPrice_title(), info.getPrice()));
            } else {
                priceHolder.txtPrice.setText(String.format("$%s", info.getPrice_title() > 0 ? info.getPrice_title() : info.getPrice()));
            }

            //notaxString
            if (info.getWithout_is_notax() != null && info.getWithout_is_notax().equals("t")) {
                priceHolder.txtNoTax.setText(String.format("%s$%s", notaxString, info.getWithout_notax_price()));
            } else {
                priceHolder.txtNoTax.setVisibility(View.GONE);
            }

            //bonus amt
            if (info.getBonus_amt() == 0) {
                priceHolder.lineLayBonus.setVisibility(View.GONE);
            } else {
                priceHolder.lineLayBonus.setVisibility(View.VISIBLE);
                priceHolder.txtBonusAmt.setText(String.format(bonusAmtString, info.getBonus_amt()));
            }

            //coupons
            priceHolder.lineLayCoupon.setVisibility(
                    info.getIs_no_coupons().equals("t") ? View.GONE : View.VISIBLE);

            //priceFakeString
            priceHolder.txtPriceFake.setText(String.format("$%s", info.getPrice_fake()));
            priceHolder.txtPriceFake.setPaintFlags(priceHolder.txtPriceFake.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Paint paint = priceHolder.txtPriceFake.getPaint();
            paint.setColor(context.getResources().getColor(R.color.black_text_secondary));
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            paint.setAntiAlias(true);

            //sales
            priceHolder.imgSales.setVisibility(itemDetail.getSales().size() > 0 ? View.VISIBLE : View.GONE);
            priceHolder.relativeLayRoot.setOnClickListener(view -> {
                if (itemDetail.getSales().size() > 0) event.onSalesInfoBtnClick(itemDetail);
            });

            //i want buy
            priceHolder.btnIWantBuy.setVisibility(position == 2 && online.getIs_stock() && !(getTime(online.getDate_online()) > date.getTime()) ? View.VISIBLE : View.GONE);
            priceHolder.btnIWantBuy.setOnClickListener(view -> {
                if (online.getIs_stock()) event.onBuyClick();
            });

        } else if (holder instanceof CountDownHolder) {

            String buyItems = itemDetail.getOnline().getBuy_online_items();
            Spannable buyIteSpannable = new SpannableString(String.format("%s %s / ", favorString, buyItems));
            buyIteSpannable.setSpan(new ForegroundColorSpan(Color.RED), favorString.length(), favorString.length() + buyItems.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((CountDownHolder) holder).txtBuyItems.setText(buyIteSpannable);


        } else if (holder instanceof ImageHolder) {

            List<Img> imgList = new ArrayList<>();
            Img img = new Img();
            img.setUrl(info.getImg1_url());
            imgList.add(img);


            if (itemDetail.getImg() != null && itemDetail.getImg().size() != 0) {
                imgList.addAll(itemDetail.getImg());
                ((ImageHolder) holder).pager.setAdapter(new ImgPagerAdapter(context, imgList));
                ((ImageHolder) holder).indicator.setViewPager(((ImageHolder) holder).pager);
            } else {
                ((ImageHolder) holder).pager.setAdapter(new ImgPagerAdapter(context, imgList));
                ((ImageHolder) holder).indicator.setVisibility(View.GONE);
            }

        } else if (holder instanceof SecondHolder) {

            ((SecondHolder) holder).btnAddToCart.setVisibility(View.GONE);
            ((SecondHolder) holder).btnBuyNow.setVisibility(View.GONE);
            ((SecondHolder) holder).btnNextTime.setVisibility(View.GONE);
            ((SecondHolder) holder).txtNotSale.setVisibility(View.GONE);
            ((SecondHolder) holder).vContact.setVisibility(View.GONE);

            ((SecondHolder) holder).btnAddToCart.setOnClickListener(view -> event.onAddToCartClick());
            ((SecondHolder) holder).btnBuyNow.setOnClickListener(view -> event.onBuyClick());

            if (online.getIs_stock() && getTime(online.getDate_online()) <= date.getTime()) {

                if (!online.getChannel_id().equals("13") && !info.getIs_hilife_shop().equals("t")) {
                    ((SecondHolder) holder).btnAddToCart.setVisibility(View.VISIBLE);
                }

                if (isTravelTag || (itemDetail.getInfo().getIs_hilife_shop() != null && itemDetail.getInfo().getIs_hilife_shop().equals("t"))) {
                    ((SecondHolder) holder).btnAddToCart.setVisibility(View.GONE);
                }

                if (online.getChannel_id().equals("13")) {
                    ((SecondHolder) holder).vContact.setVisibility(View.VISIBLE);
                }

                ((SecondHolder) holder).btnBuyNow.setVisibility(View.VISIBLE);
            } else if (online.getIs_stock() && getTime(online.getDate_online()) > date.getTime()) {
                ((SecondHolder) holder).txtNotSale.setVisibility(View.VISIBLE);
            } else {
                ((SecondHolder) holder).btnNextTime.setVisibility(View.VISIBLE);
            }

        } else if (holder instanceof ProductInfoHolder) {

            ProductInfoHolder productInfoHolder = (ProductInfoHolder) holder;

            productInfoHolder.linearLayoutTabDesc.setOnClickListener(view -> selectPage(productInfoHolder, view));
            productInfoHolder.linearLayoutTabSpecification.setOnClickListener(view -> selectPage(productInfoHolder, view));
            productInfoHolder.linearLayoutTabReturn.setOnClickListener(view -> selectPage(productInfoHolder, view));

            countDownViewList.add(((ProductInfoHolder) holder));
            productInfoHolder.textViewBuyItems.setText(String.valueOf(itemDetail.getOnline().getBuy_online_items()));

            productInfoHolder.imageViewFavorite.setImageResource(isTrack ? R.mipmap.like_active : R.mipmap.like64x64);
            productInfoHolder.linearLayoutFavorite.setOnClickListener(view -> event.onTrackClick());
            productInfoHolder.linearLayoutShare.setOnClickListener(view -> event.onShareBtnClick());
            productInfoHolder.textViewProductId.setText(String.format("%s: %s", context.getResources().getString(R.string.product_id), online.getItem_id()));

            Spanned html;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                html = Html.fromHtml(itemDetail.getDeliver_notic(), Html.FROM_HTML_MODE_LEGACY);
            } else {
                html = Html.fromHtml(itemDetail.getDeliver_notic());
            }

            productInfoHolder.deliverNotice.setText(html);

            settingYoutubeWebView(productInfoHolder.youtube1, info.getYoutube_url1());
            settingYoutubeWebView(productInfoHolder.youtube2, info.getYoutube_url2());
            settingWebView(productInfoHolder.content, info.getContent());
            settingWebView(productInfoHolder.buyReason, info.getBuy_reason());

            if (info.getMade_in() != null && !info.getMade_in().equals("")) {
                productInfoHolder.madeIn.setText(info.getMade_in());
            }

            if (info.getItem_state() != null && !info.getItem_state().equals("")) {
                productInfoHolder.itemState.setText(info.getItem_state());
            }

            settingWebView(productInfoHolder.specification, info.getSpecification());
            settingWebView(productInfoHolder.returnNotice, itemDetail.getReturn_notice());

        } else if (holder instanceof SalesHolder) {
            SalesHolder salesHolder = (SalesHolder) holder;

            salesHolder.cardViewSales.setVisibility(itemDetail.getSales().size() > 0 ? View.VISIBLE : View.GONE);

            salesHolder.recyclerViewSales.setLayoutManager(new LinearLayoutManager(context));
            salesHolder.recyclerViewSales.setAdapter(new SalesAdapter(context, itemDetail));

            salesHolder.linearLayoutMore.setVisibility(itemDetail.getSales().size() > 3 ? View.VISIBLE : View.GONE);
            salesHolder.linearLayoutMore.setOnClickListener(view -> event.onSalesInfoBtnClick(itemDetail));

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

    private void selectPage(ProductInfoHolder productInfoHolder, View view) {
        //tab
        productInfoHolder.textViewTabDesc.setTextColor(
                view.getId() == R.id.linear_layout_tab_desc ? context.getResources().getColor(R.color.tab_text_selected) : context.getResources().getColor(R.color.tab_text_unselected));
        productInfoHolder.textViewTabSpecification.setTextColor(
                view.getId() == R.id.linear_layout_tab_specification ? context.getResources().getColor(R.color.tab_text_selected) : context.getResources().getColor(R.color.tab_text_unselected));
        productInfoHolder.textViewTabReturn.setTextColor(
                view.getId() == R.id.linear_layout_tab_return ? context.getResources().getColor(R.color.tab_text_selected) : context.getResources().getColor(R.color.tab_text_unselected));

        //under line
        productInfoHolder.linearLayoutUnderLineDesc.setBackgroundColor(
                view.getId() == R.id.linear_layout_tab_desc ? context.getResources().getColor(R.color.tab_text_selected) : context.getResources().getColor(R.color.grey_alpha25));
        productInfoHolder.linearLayoutUnderLineSpecification.setBackgroundColor(
                view.getId() == R.id.linear_layout_tab_specification ? context.getResources().getColor(R.color.tab_text_selected) : context.getResources().getColor(R.color.grey_alpha25));
        productInfoHolder.linearLayoutUnderLineReturn.setBackgroundColor(
                view.getId() == R.id.linear_layout_tab_return ? context.getResources().getColor(R.color.tab_text_selected) : context.getResources().getColor(R.color.grey_alpha25));

        //page
        productInfoHolder.linearLayoutPageDesc.setVisibility(
                view.getId() == R.id.linear_layout_tab_desc ? View.VISIBLE : View.GONE);
        productInfoHolder.linearLayoutPageSpecification.setVisibility(
                view.getId() == R.id.linear_layout_tab_specification ? View.VISIBLE : View.GONE);
        productInfoHolder.linearLayoutPageReturn.setVisibility(
                view.getId() == R.id.linear_layout_tab_return ? View.VISIBLE : View.GONE);

        event.onPageSelected();
    }


    class DefaultBannerHolder extends RecyclerView.ViewHolder {
        DefaultBannerHolder(View itemView) {
            super(itemView);
        }
    }

    class TopBannerHolder extends RecyclerView.ViewHolder {
        ImageView topBanner;

        TopBannerHolder(View itemView) {
            super(itemView);
            topBanner = (ImageView) itemView.findViewById(R.id.top_banner);
        }
    }

    class BottomBannerHolder extends RecyclerView.ViewHolder {
        ImageView bottomBanner;

        BottomBannerHolder(View itemView) {
            super(itemView);
            bottomBanner = (ImageView) itemView.findViewById(R.id.bottom_banner);
        }
    }

    class TitleHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtSubTitle;

        TitleHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtSubTitle = (TextView) itemView.findViewById(R.id.txt_sub_title);
        }
    }

    class PriceHolder extends RecyclerView.ViewHolder {

        private RelativeLayout relativeLayRoot;
        private TextView txtPrice;
        private TextView txtNoTax;
        private TextView txtPriceFake;
        private ImageView imgSales;
        private Button btnIWantBuy;
        private LinearLayout lineLayBonus;
        private TextView txtBonusAmt;
        private TextView txtBonusAmtUp;
        private LinearLayout lineLayCoupon;

        PriceHolder(View itemView) {
            super(itemView);
            relativeLayRoot = (RelativeLayout) itemView.findViewById(R.id.relative_layout_root);
            txtPrice = (TextView) itemView.findViewById(R.id.price);
            txtNoTax = (TextView) itemView.findViewById(R.id.notax);
            txtPriceFake = (TextView) itemView.findViewById(R.id.priceFake);
            imgSales = (ImageView) itemView.findViewById(R.id.image_view_sales);
            btnIWantBuy = (Button) itemView.findViewById(R.id.iWantBuy);
            lineLayBonus = (LinearLayout) itemView.findViewById(R.id.linear_layout_bonus);
            txtBonusAmt = (TextView) itemView.findViewById(R.id.bonusAmt);
            txtBonusAmtUp = (TextView) itemView.findViewById(R.id.bonusAmtUp);
            lineLayCoupon = (LinearLayout) itemView.findViewById(R.id.linear_layout_coupon);
        }
    }

    class CountDownHolder extends RecyclerView.ViewHolder {

        private TextView txtBuyItems;
        private TextView txtCountDown;

        CountDownHolder(View itemView) {
            super(itemView);
            txtBuyItems = (TextView) itemView.findViewById(R.id.text_view_buy_items);
            txtCountDown = (TextView) itemView.findViewById(R.id.text_view_count_down);
        }
    }

    class ImageHolder extends RecyclerView.ViewHolder {

        private ViewPager pager;
        private CircleIndicator indicator;

        ImageHolder(View itemView) {
            super(itemView);
            pager = (ViewPager) itemView.findViewById(R.id.imgViewPager);
            indicator = (CircleIndicator) itemView.findViewById(R.id.indicator);
        }
    }

    class SecondHolder extends RecyclerView.ViewHolder {

        private Button btnAddToCart;
        private Button btnBuyNow;
        private TextView txtNotSale;
        private Button btnNextTime;
        private View vContact;

        SecondHolder(View itemView) {
            super(itemView);
            btnAddToCart = (Button) itemView.findViewById(R.id.addToCart);
            btnBuyNow = (Button) itemView.findViewById(R.id.buyNow);
            txtNotSale = (TextView) itemView.findViewById(R.id.notSale);
            btnNextTime = (Button) itemView.findViewById(R.id.nextTime);
            vContact = (View) itemView.findViewById(R.id.contact);
        }
    }

    class ProductInfoHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayoutTabDesc;
        private LinearLayout linearLayoutTabSpecification;
        private LinearLayout linearLayoutTabReturn;

        private LinearLayout linearLayoutPageDesc;
        private LinearLayout linearLayoutPageSpecification;
        private LinearLayout linearLayoutPageReturn;

        private TextView textViewTabDesc;
        private TextView textViewTabSpecification;
        private TextView textViewTabReturn;

        private LinearLayout linearLayoutUnderLineDesc;
        private LinearLayout linearLayoutUnderLineSpecification;
        private LinearLayout linearLayoutUnderLineReturn;

        private LinearLayout linearLayoutFavorite;
        private LinearLayout linearLayoutShare;
        private ImageView imageViewFavorite;
        private TextView textViewBuyItems;
        private TextView textViewCountDown;
        private TextView textViewProductId;

        private View expectDeliverLayout;
        private TextView deliverNotice;
        private WebView youtube1;
        private WebView youtube2;
        private WebView content;
        private WebView buyReason;
        private TextView madeIn;
        private TextView itemState;
        private WebView specification;
        private WebView returnNotice;

        ProductInfoHolder(View itemView) {
            super(itemView);
            linearLayoutTabDesc = (LinearLayout) itemView.findViewById(R.id.linear_layout_tab_desc);
            linearLayoutTabSpecification = (LinearLayout) itemView.findViewById(R.id.linear_layout_tab_specification);
            linearLayoutTabReturn = (LinearLayout) itemView.findViewById(R.id.linear_layout_tab_return);

            linearLayoutPageDesc = (LinearLayout) itemView.findViewById(R.id.linear_layout_page_desc);
            linearLayoutPageSpecification = (LinearLayout) itemView.findViewById(R.id.linear_layout_page_specification);
            linearLayoutPageReturn = (LinearLayout) itemView.findViewById(R.id.linear_layout_page_return);

            textViewTabDesc = (TextView) itemView.findViewById(R.id.text_view_tab_desc);
            textViewTabSpecification = (TextView) itemView.findViewById(R.id.text_view_tab_specification);
            textViewTabReturn = (TextView) itemView.findViewById(R.id.text_view_tab_return);

            linearLayoutUnderLineDesc = (LinearLayout) itemView.findViewById(R.id.linear_layout_under_line_desc);
            linearLayoutUnderLineSpecification = (LinearLayout) itemView.findViewById(R.id.linear_layout_under_line_specification);
            linearLayoutUnderLineReturn = (LinearLayout) itemView.findViewById(R.id.linear_layout_under_line_return);

            linearLayoutFavorite = (LinearLayout) itemView.findViewById(R.id.linear_layout_favorite);
            linearLayoutShare = (LinearLayout) itemView.findViewById(R.id.linear_layout_share);
            imageViewFavorite = (ImageView) itemView.findViewById(R.id.image_view_favorite);
            textViewBuyItems = (TextView) itemView.findViewById(R.id.text_view_buy_items);
            textViewCountDown = (TextView) itemView.findViewById(R.id.text_view_count_down);
            textViewProductId = (TextView) itemView.findViewById(R.id.text_view_product_id);

            expectDeliverLayout = itemView.findViewById(R.id.expectDeliverLayout);
            deliverNotice = (TextView) itemView.findViewById(R.id.deliverNotice);
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

    class SalesHolder extends RecyclerView.ViewHolder {

        private CardView cardViewSales;
        private RecyclerView recyclerViewSales;
        private LinearLayout linearLayoutMore;

        public SalesHolder(View itemView) {
            super(itemView);
            cardViewSales = (CardView) itemView.findViewById(R.id.card_view_sales);
            recyclerViewSales = (RecyclerView) itemView.findViewById(R.id.recycler_view_sales);
            linearLayoutMore = (LinearLayout) itemView.findViewById(R.id.linear_layout_more);
        }
    }

    class ItemListHeader extends RecyclerView.ViewHolder {

        ItemListHeader(View itemView) {
            super(itemView);
        }
    }

    class ItemListHolder extends RecyclerView.ViewHolder {

        private ImageView thumb;
        private ImageView stock;
        private TextView name;
        private TextView priceFake;
        private TextView priceTitle;
        private TextView priceUp;

        ItemListHolder(View itemView) {
            super(itemView);
            thumb = (ImageView) itemView.findViewById((R.id.thumb));
            stock = (ImageView) itemView.findViewById(R.id.stock);
            name = (TextView) itemView.findViewById(R.id.name);
            priceFake = (TextView) itemView.findViewById(R.id.priceFake);
            priceTitle = (TextView) itemView.findViewById(R.id.priceTitle);
            priceUp = (TextView) itemView.findViewById(R.id.priceUp);
        }
    }
}
