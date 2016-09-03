package com.guysagy.gamersweb.user;

import java.util.regex.Pattern;
import android.text.Editable;
import android.widget.EditText;

public class EmailValidator extends DataValidator
{
    public EmailValidator(ValidationResultListener listener, EditText dataView)
    {
        super(listener, dataView);
    }
    
    @Override
    public void afterTextChanged(Editable view) 
    {
        mIsValid = validateStringEntered(mDataView.getText().toString() 
                                                    , "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
                                                    , Pattern.CASE_INSENSITIVE);	
        mListener.onEmailResult();
    }
}