package com.whatsmode.shopify.block.account.data;

import java.io.Serializable;

/**
 * Created by tom on 17-11-21.
 */

public class UserInfo implements Serializable {
    private String username;//email

    public UserInfo(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                '}';
    }
}
