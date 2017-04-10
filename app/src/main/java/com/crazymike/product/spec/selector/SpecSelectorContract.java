package com.crazymike.product.spec.selector;

/**
 * Created by Elliot on 2017/3/27.
 */

public interface SpecSelectorContract {

    interface View {

        void onCSServiceMessageResponseGet(boolean isSuccess, String message);
    }

    interface Presenter {
        boolean isTravelProduct(String tagId);

        String getCSServiceStartTime();

        String getCSServiceEndTime();

        void postCSServiceMessage(String name, String title, String phoneNumber, String email, String content);
    }
}
