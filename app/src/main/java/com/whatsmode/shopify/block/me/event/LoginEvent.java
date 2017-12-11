package com.whatsmode.shopify.block.me.event;

/**
 * Created by tom on 17-12-4.
 */

public class LoginEvent {
    public boolean isLogin;
    public boolean singleRefresh;

    public LoginEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public LoginEvent(){}

    public static LoginEvent singleRefresh(){
        LoginEvent event = new LoginEvent();
        event.singleRefresh = true;
        return event;
    }
}
