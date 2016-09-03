package com.guysagy.gamersweb.user;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.text.Editable;
import android.widget.EditText;

public class DobValidator extends DataValidator
{
    static public int mLegalAgeLimitForAccount = 18;
    
    public DobValidator(ValidationResultListener listener, EditText dataView)
    {
        super(listener, dataView);
    }
    
    @Override
    public void afterTextChanged(Editable view) 
    {
        mIsValid = validateStringEntered(mDataView.getText().toString(), null, 0);
        if (mIsValid)
        {		
            Calendar minCalendar = Calendar.getInstance();
            minCalendar.add(Calendar.YEAR, -mLegalAgeLimitForAccount);
    
            try 
            {
                DateFormat formatter = new SimpleDateFormat("mm-dd-yyyy");
                Date userDate = (Date)formatter.parse(mDataView.getText().toString());  
                Calendar userCalendar = Calendar.getInstance();
                userCalendar.setTime(userDate);
                
                mIsValid = (minCalendar.after(userCalendar));			
            } 
            catch (ParseException e) 
            {
                mIsValid = false;
            }
        }

        mListener.onDobResult();
    }
}