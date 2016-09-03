package com.guysagy.gamersweb.user;

public interface UserListener 
{
    void onAccountCreate();
    void onAccountLogin();
    void onAccountLogout();
    void onAccountDelete();
}
