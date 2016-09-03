package com.guysagy.gamersweb.user;

import android.text.Editable;
import android.widget.EditText;

public class UserNameValidator extends DataValidator
{
    public UserNameValidator(ValidationResultListener listener, EditText dataView)
    {
        super(listener, dataView);
    }
    
    @Override
    public void afterTextChanged(Editable view) 
    {
        mIsValid = validateStringEntered(mDataView.getText().toString() 
                                                    // TODO: for dev purposes, username req. are simple
                                                    , "^[a-zA-Z0-9_-]{3,15}$"
                                                    , 0);
        
        mListener.onUserNameResult();
    }
}