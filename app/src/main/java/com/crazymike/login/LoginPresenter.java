package com.crazymike.login;

import android.util.Log;

import com.crazymike.respositories.CookieRepository;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void handleHTML(String html) {


        //<script>appLogin(true, "aKbUI3CaZlB3wKGb10ehFDXHdidJaETtVIlQ59UuVyg.", "crazy", "J4EkzLOftvrOCVsy8ieRX8sjFyotP-4VUKDi3kEf4Bc.");</script>
        if (!html.contains("appLogin")) {
            return;
        }

        String data = html.replace("<script>appLogin(", "").replace(");</script>", "");
        //true, "aKbUI3CaZlB3wKGb10ehFDXHdidJaETtVIlQ59UuVyg.", "crazy", "J4EkzLOftvrOCVsy8ieRX8sjFyotP-4VUKDi3kEf4Bc."


        String success = data.split(",")[0];
        if (!success.equals("true")) {
            view.onLoginFailure();
            return;
        }

        try {
            String memberId = data.split(",")[1].replace("\"", "").replace(" ", "");
            String type = data.split(",")[2].replace("\"", "").replace(" ", "");
            String user = data.split(",")[3].replace("\"", "").replace(" ", "");

            PreferencesTool.getInstance().put(PreferencesKey.MEMBER_ID, memberId);
            PreferencesTool.getInstance().put(PreferencesKey.LOGIN_TYPE, type);
            PreferencesTool.getInstance().put(PreferencesKey.LOGIN_USER, user);

            CookieRepository.getInstance().setIsLogin(true);
            view.onLoginSuccess(memberId, type, user);
        } catch (Exception e) {
            e.printStackTrace();
            view.onLoginFailure();
        }
    }
}
