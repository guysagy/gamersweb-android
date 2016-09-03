package com.guysagy.gamersweb.ui.activities;

import com.guysagy.gamersweb.db.UserTable;
import com.guysagy.gamersweb.settings.AppSettings;
import com.guysagy.gamersweb.R;
import com.guysagy.gamersweb.user.DobValidator;
import com.guysagy.gamersweb.user.EmailValidator;
import com.guysagy.gamersweb.user.FirstNameValidator;
import com.guysagy.gamersweb.user.LastNameValidator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public final class UpdateUserAccountActivity extends UserAccountBaseActivity 
{
    static public boolean isActivityRunning = false;
    
    protected int getActivityStringId()
    {
        return R.string.str_updateAccount;
    }

    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        mCreateButton.setVisibility(Button.GONE);
        // Chainging username is not supported.
        ((EditText)findViewById(R.id.EditText_Nickname)).setEnabled(false);
        isActivityRunning = true;
    }	
    
    @Override
    protected void onStart()
    {
        super.onStart();
        Log.v(AppSettings.DebugPrefix, "onStart " + toString());
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.v(AppSettings.DebugPrefix, "onResume " + toString());
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.v(AppSettings.DebugPrefix, "onPause " + toString());
    }    
    
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.v(AppSettings.DebugPrefix, "onStop " + toString());
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.v(AppSettings.DebugPrefix, "onDestroy " + toString());
        isActivityRunning = false;
    }
    
    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();
        switch(viewId)
        {
        case(R.id.Button_UpdateAccount):
            onUpdateAccount();
            break; 
        
        case(R.id.Button_DiscardNExit):
            finish();
            break;
        
        case(R.id.Button_Reset):
            resetActivity();
            break;
            
        default:
            super.onClick(view);
            break;
        }
    } 
    
    protected boolean updateLocalAccount()
    {
        boolean success = UserTable.update(this, mFirstName, mLastName, mUserName, mEmail, mPassword, mDob, mGender, mAvatarFileName);
        Log.d(AppSettings.DebugPrefix, "Update of account user name = " + mUserName + ", success = " + success);
        return success;
    }

    private boolean loadLoggedInUserAccountData()
    {
        if (mLoggedInUser != null) 
        {
            mFirstName      = mLoggedInUser.getFirstName();
            mLastName       = mLoggedInUser.getLastName();
            mUserName       = mLoggedInUser.getUserName(); 
            mPassword       = mLoggedInUser.getPassword();
            mEmail          = mLoggedInUser.getEmail();
            mGender         = mLoggedInUser.getGender();
            mDob            = mLoggedInUser.getDob();
            mAvatarFileName = mLoggedInUser.getAvatarFileName();  
            return true;
        }
        
        return false;
    }
    
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        String extraValue = intent.getStringExtra(extraExit);
        if (extraValue != null && extraValue.equals(extraExit))
        {
            finish();
        }
    }

    // TODO: how can I mimic the system killing this activity to test this?
    protected void onSaveInstanceState (Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        // No need to provide explicit implementation.
        // From http://developer.android.com/reference/android/app/Activity.html#onSaveInstanceState(android.os.Bundle) :
        // The default implementation takes care of most of the UI per-instance state for you 
        // by calling onSaveInstanceState() on each view in the hierarchy that has an id, 
        // and by saving the id of the currently focused view (all of which is restored by the default 
        // implementation of onRestoreInstanceState(Bundle)). 
        // TODO: does this include the avatar image ?
    }
    
    private void populateLoggedInUserAccountData()
    {
        mFirstNameEditText.setText(mFirstName);
        mLastNameEditText.setText(mLastName);
        mUserNameEditText.setText(mUserName);
        mEmailEditText.setText(mEmail);
        mPasswordEditText.setText(getString(R.string.str_passwordAsStars));
        mDOBEditText.setText(mDob);
        
        if (mGender.equals(getString(R.string.str_Male)))
            mGenderButtonMale.setChecked(true);
        else if (mGender.equals(getString(R.string.str_Female)))
            mGenderButtonFemale.setChecked(true);
        
        if (loadAvatarImage())
            mAvaterImageButton.setImageBitmap(mAvatarImage); 
        else
        {
            mAvatarImage = BitmapFactory.decodeResource(getResources(), R.drawable.person_picture_placeholder);
            mAvaterImageButton.setImageBitmap(mAvatarImage); 
        }
    }
    
    protected boolean validateUserNameDoesNotExistInBb()
    {
        return true;
    }
    
    protected void resetActivity()
    {
        if (loadLoggedInUserAccountData())
            populateLoggedInUserAccountData();   
        
        mIsFirstNameValid = true;
        mIsLastNameValid = true;
        mIsUserNameValid = true;
        mIsEmailValid = true;
        mIsPasswordValid = true;
        mIsGenderValid = true;
        mIsDobValid = true;
        mIsAvatarFileNameValid = true; // User not required to create avatar.
        
        mFirstNameLabel.setTextColor(Color.BLACK);
        mLastNameLabel.setTextColor(Color.BLACK);
        mUserNameLabel.setTextColor(Color.BLACK);
        mEmailLabel.setTextColor(Color.BLACK);
        mPasswordLabel.setTextColor(Color.BLACK);
        mDobLabel.setTextColor(Color.BLACK);
        mGenderLabel.setTextColor(Color.BLACK);
        
        mCreateButton.setVisibility(Button.GONE);
        updateApplyButtonState();
        mFirstNameLabel.requestFocus();    	
    }
    
    protected void updateApplyButtonState()
    {
        boolean isDataEnteredValid = mIsFirstNameValid
                                        && mIsLastNameValid
                                        && mIsEmailValid 
                                        && mIsPasswordValid
                                        && mIsDobValid
                                        && mIsGenderValid 
                                        && mIsAvatarFileNameValid;
        
        mUpdateButton.setEnabled(isDataEnteredValid); 
    }
    
    private void onUpdateAccount()
    {
        collectEnteredAccountData();
        
        boolean success = updateLocalAccount();
        if (!success)
        {
            makeLongToast(R.string.str_accountUpdateFailure);
        }
        else
        {
            mLoggedInUser.setFirstName(mFirstName)
                            .setLastName(mLastName)
                            .setEmail(mEmail)
                            .setPassword(mPassword)
                            .setDob(mDob)
                            .setGender(mGender)
                            .setAvatarFileName(mAvatarFileName);

            if (!saveAvatarImage())
            {
                // Failure to update the avatar image does not constitute failure to update the account. 
                makeLongToast(R.string.str_avatarUpdateFailureAfterUpdate);
            }	
            else
            {
                makeShortToast(R.string.str_accountUpdateSuccess);
            }
        }
    }
    
    protected void setActivityValidators()
    {
        mFirstNameValidator     = new FirstNameValidator(this, mFirstNameEditText);
        mLastNameValidator      = new LastNameValidator(this, mLastNameEditText);
        // mUserNameValidator   = new UserNameValidator(this, mUserNameEditText);
        mEmailValidator         = new EmailValidator(this, mEmailEditText);
        mDobValidator           = new DobValidator(this, mDOBEditText);
        
        mFirstNameEditText.addTextChangedListener(mFirstNameValidator);
        mLastNameEditText.addTextChangedListener(mLastNameValidator);
        // mUserNameEditText.addTextChangedListener(mUserNameValidator);
        mEmailEditText.addTextChangedListener(mEmailValidator);  
        mDOBEditText.addTextChangedListener(mDobValidator);
    }
    
    protected void removeActivityValidators()
    {
        mFirstNameEditText.removeTextChangedListener(mFirstNameValidator);
        mLastNameEditText.removeTextChangedListener(mLastNameValidator);
        // mUserNameEditText.removeTextChangedListener(mUserNameValidator);
        mEmailEditText.removeTextChangedListener(mEmailValidator);  
        mDOBEditText.removeTextChangedListener(mDobValidator);
    }      
}
