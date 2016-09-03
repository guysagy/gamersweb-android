package com.guysagy.gamersweb.user;

import android.text.Editable;
import android.widget.EditText;

public class LastNameValidator extends DataValidator
{
    public LastNameValidator(ValidationResultListener listener, EditText dataView)
    {
        super(listener, dataView);
    }
    
    @Override
    public void afterTextChanged(Editable view) 
    {
        mIsValid = validateStringEntered(mDataView.getText().toString() 
                                                    , "^[a-zA-Z0-9-]{1,15}$"
                                                    , 0);
        
        mListener.onLastNameResult();
    }
}