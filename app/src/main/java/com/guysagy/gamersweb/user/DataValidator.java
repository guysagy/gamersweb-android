package com.guysagy.gamersweb.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class DataValidator implements TextWatcher
{
    ValidationResultListener mListener;
    EditText mDataView;
    boolean mIsValid = false;
    
    public DataValidator()
    {
        super();
    }
    
    public DataValidator(ValidationResultListener listener, EditText dataView)
    {
        super();
        mListener = listener;
        mDataView = dataView;
    }
    
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,	int after) 
    {
        // Do nothing.
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) 
    {
        // Do nothing.
    }
    
    protected boolean validateStringEntered(String string, String regExpression, int regFlags)
    {
        boolean validData = string != null && string.length() > 0 ;
        if (validData && regExpression != null && regExpression.length() > 0)
        {
            Pattern pattern = Pattern.compile(regExpression,regFlags);   
            Matcher matcher = pattern.matcher(string);  
            validData = matcher.matches();		
        }
        return validData;			
    }
    
    public boolean isValid()
    {
        return mIsValid;
    }
}