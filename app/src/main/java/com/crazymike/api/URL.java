package com.crazymike.api;

public class URL {

    public static final String PLAY_URL = "market://details?id=com.crazymike";

    //appapi2
    public static final String DOMAIN = "http://appapi2.crazymike.tw/index.php";
    public static final String ADD_DEVICE_TOKEN = DOMAIN + "/addDeviceToken?callfrom=app";
    public static final String GET_Q_EVENT = DOMAIN + "/getQevent?callfrom=app";
    public static final String ADD_PUSH_TIMES = DOMAIN + "/addPushTimes?callfrom=app";
    public static final String GET_APP_VERSION = DOMAIN + "/getVersion?callfrom=app";

    //develappapi2
    public static final String DEVEL_DOMAIN = "http://appapi2.devel.crazymike.tw/index.php";
    public static final String DEVEL_ADD_DEVICE_TOKEN = DEVEL_DOMAIN + "/addDeviceToken?callfrom=app";
    public static final String DEVEL_GET_Q_EVENT = DEVEL_DOMAIN + "/getQevent?callfrom=app";
    public static final String DEVEL_ADD_PUSH_TIMES = DEVEL_DOMAIN + "/addPushTimes?callfrom=app";
    public static final String DEVEL_GET_APP_VERSION = DEVEL_DOMAIN + "/getVersion?callfrom=app";

    //native
    public static final String BASE_CDN = "https://apicdn.crazymike.tw/index.asp/";
    public static final String BASE_OPEN_API = "https://openapi.crazymike.tw/index.asp/";

    public static final String BASE_CDN_DEVEL = "https://apicdn.devel.crazymike.tw/index.asp/";
    public static final String BASE_OPEN_API_DEVEL = "https://openapi.devel.crazymike.tw/index.asp/";

    //Navigation Link
    public static final String BONUS_GIFT = "https://m2.crazymike.tw/bonus_gift/tag-1221";
    public static final String BONUS_MEMBER = "https://m2.crazymike.tw/member_bonus";
    public static final String PROMOTE_LIST = "https://m2.crazymike.tw/promote_list";
    public static final String CREDIT = "http://act.crazymike.tw/credit/index.php";
    public static final String CONTRACT = "https://m2.crazymike.tw/contact";
    public static final String FAQ = "https://m2.crazymike.tw/faq";

    //Nav guest
    public static final String GUEST_ORDER_LIST = "https://m2.crazymike.tw/not_m_order_list";
    public static final String LOGIN = "https://pc.crazymike.tw/crazy_login?out=app";

    //Nav member
    public static final String ORDER_LIST = "https://m2.crazymike.tw/order_list";
    public static final String TRACK_LIST = "https://m2.crazymike.tw/track_list";
    public static final String CONTRACT_LIST = "https://m2.crazymike.tw/contact_list";
    public static final String MEMBER_CASH = "https://m2.crazymike.tw/member_cash";
    public static final String USER_MENU = "https://m2.crazymike.tw/userMenu";

    //Product
    public static final String SEARCH = "https://pc.crazymike.tw/search?w=%s&callfrom=app";

    //Cart
    public static final String CART = "https://m2.crazymike.tw/cart.asp";
    public static final String GET_CART_LIST = "https://m2.crazymike.tw/ajax-cookie_set?k=carts&rtn=json&callfrom=app";
    public static final String CART_REDIRECT = "https://m2.crazymike.tw/redirect?to=/cart&carts=%s";
    public static final String ADD_CART = "https://m2.crazymike.tw/ajax-cookie_set?k=carts&sale_id=-1&callfrom=app";

    //Buy
    public static final String BUY = "https://m2.crazymike.tw/buy.asp";
    public static final String DEVELBUY = "https://m2.devel.crazymike.tw/buy.asp";
    public static final String BOTTOM_BANNER = "http://m2.crazymike.tw/act_app.asp?force=false&partner=app&utm_medium=banner&utm_source=app&utm_campaign=nt200";

    //Server log
    public static final String SERVER_LOG = "https://openapi.crazymike.tw/?callfrom=app&func=serverLog&agent=app-android";

    public static final String INSERT_COOKIE = "https://m2.devel.crazymike.tw/cmScript?maker=0&url=/";

    public static final String MEMBER_ORDER_NOTICE_INFO = "https://m2.crazymike.tw/openapi?func=member&tfunc=get";

    public static final String POST_CS_SERVICE_MESSAGE = "https://pc.devel.crazymike.tw/contact_chk.asp";
}
