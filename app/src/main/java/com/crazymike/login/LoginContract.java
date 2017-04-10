package com.crazymike.login;

public interface LoginContract {

    interface View{

        void onLoginSuccess(String memberId, String type, String user);

        void onLoginFailure();
    }
    interface Presenter{

        void handleHTML(String html);
    }
}
