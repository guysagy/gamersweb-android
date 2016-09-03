package com.guysagy.gamersweb.ui.activities;

import java.util.Calendar;
import com.guysagy.gamersweb.db.ScoresTable;
import com.guysagy.gamersweb.db.UserTable;
import com.guysagy.gamersweb.settings.AppSettings;
import com.guysagy.gamersweb.R;
import com.guysagy.gamersweb.user.DobValidator;
import com.guysagy.gamersweb.user.EmailValidator;
import com.guysagy.gamersweb.user.FirstNameValidator;
import com.guysagy.gamersweb.user.LastNameValidator;
import com.guysagy.gamersweb.user.User;
import com.guysagy.gamersweb.user.UserNameValidator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public final class CreateUserAccountActivity extends UserAccountBaseActivity 
{
    protected int getActivityStringId()
    {
        return R.string.str_Account_Create;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) 
    {
        super.onNewIntent(intent);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.v(AppSettings.DebugPrefix, "onResume " + toString());
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
    
    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();
        switch(viewId)
        {
        case(R.id.Button_CreateNLoginAccount):
            onCreateAccount();
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
    
    protected void resetActivity()
    {
        mFirstName = "";
        mFirstNameEditText.setText("");
        
        mLastName = "";
        mLastNameEditText.setText("");   		
        
        mUserName = "";
        mUserNameEditText.setText("");
        
        mEmail = "";
        mEmailEditText.setText(""); 
        
        mPassword = "";
        mPasswordEditText.setText(getString(R.string.str_NotSet));
        
        // Known issue: The code below allows illegal dates to be input. 
        // The DatePickerDialog does not validate the update data. 
        final Calendar c = Calendar.getInstance();
        mDobYear = c.get(Calendar.YEAR) - mLegalAgeLimitForAccount;
        mDobMonth = c.get(Calendar.MONTH);
        mDobDay = c.get(Calendar.DAY_OF_MONTH);   
        mDOBEditText.setText(getString(R.string.str_NotSet));
        
        mGenderButtonMale.setChecked(false);
        mGenderButtonFemale.setChecked(false);
        mGender = "";
        
        mAvatarImage = BitmapFactory.decodeResource(getResources(), R.drawable.person_picture_placeholder);
        mAvaterImageButton.setImageBitmap(mAvatarImage); 
        mAvatarFileName = "";
        
        mIsFirstNameValid = false;
        mIsLastNameValid = false;
        mIsUserNameValid = false;
        mIsEmailValid = false;
        mIsPasswordValid = false;
        mIsGenderValid = false;
        mIsDobValid = false;
        mIsAvatarFileNameValid = true; // User not required to create avatar.	     		
        
        mFirstNameLabel.setTextColor(Color.RED);
        mLastNameLabel.setTextColor(Color.RED);
        mUserNameLabel.setTextColor(Color.RED);
        mEmailLabel.setTextColor(Color.RED);
        mPasswordLabel.setTextColor(Color.RED);
        mDobLabel.setTextColor(Color.RED);
        mGenderLabel.setTextColor(Color.RED);
        
        mUpdateButton.setVisibility(Button.GONE);
        updateApplyButtonState();

        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        mFirstNameEditText.requestFocus();
    }
    
    protected void updateApplyButtonState()
    {
        // TODO: use reflection to iterate over all member variables of type boolean that
        // have template name "mIsXXXValid", instead of this ....
        boolean isDataEnteredValid = mIsFirstNameValid
                                        && mIsLastNameValid
                                        && mIsUserNameValid
                                        && mIsEmailValid 
                                        && mIsPasswordValid
                                        && mIsDobValid
                                        && mIsGenderValid 
                                        && mIsAvatarFileNameValid;
        
        mCreateButton.setEnabled(isDataEnteredValid); 
    }
    
    private void onCreateAccount()
    {
        collectEnteredAccountData();
        
        boolean success = createLocalAccount();
        /*
        if (success)
        {
            success = createServerAccount();
            if (!success)
            {
                if (deleteLocalAccount() == false)
                {
                    // TODO: log error
                }
            }
        }
        */

        if (!success)
        {
            makeLongToast(R.string.str_accountCreateFailure);
        }
        else
        {
            if (!saveAvatarImage())
            {
                // Failure to save the avatar image does not constitute failure to create the account. 
                makeLongToast(R.string.str_avatarCreateFailureAfterCreate);
            }
            else
            {	
                makeShortToast(R.string.str_accountCreateSuccess);
            }
            
            Intent intent = new Intent(this, LoginUserAccountActivity.class); 
            startActivity(intent);
            finish();
        }			    		
    }

    protected boolean createLocalAccount()
    {
        // The UI should have blocked creation of an account that already exists. 
        // Nevertheless, code should check it here too.
        boolean success = false;
        if (!UserTable.userNameAlreadyUsed(this, mUserName))
        {
            // If success, then user needs to login with created credentials to set mLoggedInUser.
            success = UserTable.insert(this, mFirstName, mLastName, mUserName, mEmail, mPassword, mDob, mGender, mAvatarFileName);
            Log.d(AppSettings.DebugPrefix, "Creation of account user name = " + mUserName + ", password = " + mPassword + ", success = " + success);
            
            if (success)
            {
                // Failure of creating a scores entry does not constitute a failure to create an account.
                // If on a login none is found, we will try to re-create it.
                // TODO: implement the above statement.
                ScoresTable.insert(this, mUserName, 0, getString(R.string.str_Android), 0); 
            }
        }
        return success;
    }
    
    protected boolean deleteLocalAccount()
    {
        return UserTable.delete(this, mUserName);
    }
    
    protected void setActivityValidators()
    {
        mFirstNameValidator = new FirstNameValidator(this, mFirstNameEditText);
        mLastNameValidator 	= new LastNameValidator(this, mLastNameEditText);
        mUserNameValidator 	= new UserNameValidator(this, mUserNameEditText);
        mEmailValidator  	= new EmailValidator(this, mEmailEditText);
        mDobValidator 		= new DobValidator(this, mDOBEditText);
        
        mFirstNameEditText.addTextChangedListener(mFirstNameValidator);
        mLastNameEditText.addTextChangedListener(mLastNameValidator);
        mUserNameEditText.addTextChangedListener(mUserNameValidator);
        mEmailEditText.addTextChangedListener(mEmailValidator);  
        mDOBEditText.addTextChangedListener(mDobValidator);	    	
    }
    
    protected void removeActivityValidators()
    {
        mFirstNameEditText.removeTextChangedListener(mFirstNameValidator);
        mLastNameEditText.removeTextChangedListener(mLastNameValidator);
        mUserNameEditText.removeTextChangedListener(mUserNameValidator);
        mEmailEditText.removeTextChangedListener(mEmailValidator);  
        mDOBEditText.removeTextChangedListener(mDobValidator);	    	
    }  	
}