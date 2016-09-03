package com.guysagy.gamersweb.user;

public interface ValidationResultListener 
{
    void onFirstNameResult();
    void onLastNameResult();
    void onUserNameResult();
    void onEmailResult();
    void onPasswordResult();
    void onDobResult();
    void onGenderResult();
}
