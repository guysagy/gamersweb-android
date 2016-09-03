package com.guysagy.gamersweb.user;

import android.text.Editable;
import android.widget.EditText;

public class PasswordValidator extends DataValidator
{
    public PasswordValidator(ValidationResultListener listener, EditText dataView)
    {
        super(listener, dataView);
    }
    
    @Override
    public void afterTextChanged(Editable view) 
    {
        mIsValid = validateStringEntered(mDataView.getText().toString()
                                            // TODO: for dev purposes, password req. are simple
                                            , "^[a-zA-Z0-9_-]{3,15}$"
                                            , 0);
        
        mListener.onPasswordResult();
    }
}