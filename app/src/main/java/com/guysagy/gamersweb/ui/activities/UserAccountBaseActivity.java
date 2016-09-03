package com.guysagy.gamersweb.ui.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import com.guysagy.gamersweb.db.UserTable;
import com.guysagy.gamersweb.R;
import com.guysagy.gamersweb.user.DataValidator;
import com.guysagy.gamersweb.user.DobValidator;
import com.guysagy.gamersweb.user.EmailValidator;
import com.guysagy.gamersweb.user.FirstNameValidator;
import com.guysagy.gamersweb.user.LastNameValidator;
import com.guysagy.gamersweb.user.PasswordValidator;
import com.guysagy.gamersweb.user.UserNameValidator;
import com.guysagy.gamersweb.user.ValidationResultListener;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.LinearLayout;

public abstract class UserAccountBaseActivity  extends BaseActivity 
                                                implements View.OnClickListener
                                                            , DatePickerDialog.OnDateSetListener
                                                            , ValidationResultListener
{
    public final static String extraExit = "exit";
    protected final static String avatarFileNameExtension = ".jpg"; // String ok in code.
    protected final static int mLegalAgeLimitForAccount = 18; 
    
    abstract protected int  getActivityStringId();
    abstract protected void resetActivity();
    abstract protected void updateApplyButtonState();
    abstract protected void setActivityValidators();
    abstract protected void removeActivityValidators();
    
    protected TextView  mFirstNameLabel;
    protected TextView  mLastNameLabel;
    protected TextView  mUserNameLabel;
    protected TextView  mEmailLabel;
    protected TextView  mPasswordLabel;
    protected TextView  mDobLabel;
    protected TextView  mGenderLabel;
    
    protected EditText      mFirstNameEditText;
    protected EditText      mLastNameEditText;
    protected EditText      mUserNameEditText;
    protected EditText      mEmailEditText;
    protected ImageButton   mPasswordButton;
    protected EditText      mPasswordEditText;
    protected ImageButton   mDOBButton;
    protected EditText      mDOBEditText;
    protected RadioButton   mGenderButtonMale;
    protected RadioButton   mGenderButtonFemale;
    protected ImageButton   mAvaterImageButton;
    
    protected Button        mCreateButton;
    protected Button        mUpdateButton;
    
    protected String        mFirstName;
    protected String        mLastName;
    protected String        mUserName;
    protected String        mEmail;
    protected String        mPassword;
    protected String        mGender;
    protected String        mDob;
    protected String        mAvatarFileName;
    
    protected boolean       mIsFirstNameValid;
    protected boolean       mIsLastNameValid;	
    protected boolean       mIsUserNameValid;
    protected boolean       mIsEmailValid;
    protected boolean       mIsPasswordValid;
    protected boolean       mIsGenderValid;
    protected boolean       mIsDobValid;
    protected boolean       mIsAvatarFileNameValid;	
    
    protected Bitmap        mAvatarImage;
    protected int           mDobYear;
    protected int           mDobMonth;
    protected int           mDobDay;

    private Dialog      mPasswordDialog;
    private EditText    mPassword1EditText1;
    private EditText    mPassword1EditText2;   
    private Button      mPasswordDlgOK;
    private Button      mPasswordDlgCancel;
    
    protected FirstNameValidator            mFirstNameValidator;
    protected LastNameValidator             mLastNameValidator;
    protected UserNameValidator             mUserNameValidator;
    protected EmailValidator                mEmailValidator;
    protected PasswordValidator             mPasswordValidator;
    protected DobValidator                  mDobValidator;
    protected PasswordDlgTextWatcher        mPasswordDlgTextWatcher;
    
    static final int     DATE_DIALOG_ID = 1;
    static final int     PASSWORD_DIALOG_ID = 2;
    
    static final int     TAKE_AVATER_CAMERA_REQUEST = 3;
    static final int     TAKE_AVATAR_GALLERY_REQUEST = 4;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        View accountView = getLayoutInflater().inflate(R.layout.account, null);
        setActivityView(accountView, getActivityStringId(), Color.WHITE);

        initActivity();
        resetActivity();
        setActivityValidators();
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
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode)
        {
            case TAKE_AVATER_CAMERA_REQUEST:
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    mAvatarImage = (Bitmap)data.getExtras().get("data");	// String ok in code.
                    mAvaterImageButton.setImageBitmap(mAvatarImage); 
                    mAvatarFileName = mUserName;
                }
            }
            break;
            
            case TAKE_AVATAR_GALLERY_REQUEST:
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    Uri photoUri = data.getData();
                    try 
                    {
                        mAvatarImage = Media.getBitmap(getContentResolver(), photoUri);
                        mAvaterImageButton.setImageBitmap(mAvatarImage);  
                        mAvatarFileName = mUserName;
                    } 
                    catch (FileNotFoundException e) 
                    {
                        // mAvatarFileName = "";
                    } 
                    catch (IOException e) 
                    {
                        // mAvatarFileName = "";
                    }
                }
            }
            break;
        }
    }
    
    @Override
    public void onClick(View view)
    {
        int viewId = view.getId();
        switch(viewId)
        {
        case(R.id.Button_Password):
            showDialog(PASSWORD_DIALOG_ID);
            break;
            
        case(R.id.Button_DOB):
            showDialog(DATE_DIALOG_ID);
            break;
            
        case(R.id.Button_CreateAvatar):
            Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            Intent cameraChooser = Intent.createChooser(pictureIntent, getString(R.string.str_cameraChooserTitle));
            startActivityForResult(cameraChooser, TAKE_AVATER_CAMERA_REQUEST);
            break;
            
        case(R.id.Button_PickGraphic):
            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK);
            pickPhotoIntent.setType("image/*");	// String ok in code.
            Intent pickPhotoChooser = Intent.createChooser(pickPhotoIntent, getString(R.string.str_imagePickerChooserTitle));
            startActivityForResult(pickPhotoChooser, TAKE_AVATAR_GALLERY_REQUEST);
            break;
        
        case(R.id.Button_PasswordOk):
            mPassword = mPassword1EditText1.getText().toString();
            mPasswordEditText.setText(getString(R.string.str_passwordAsStars));
            // Note: fall through to cancel case.
        case(R.id.Button_PasswordCancel):
            mPassword1EditText1.removeTextChangedListener(mPasswordValidator);
            mPassword1EditText2.removeTextChangedListener(mPasswordDlgTextWatcher);
            removeDialog(PASSWORD_DIALOG_ID);	// This dialog needs to be reset to initial state each time it is shown.
            updateApplyButtonState();
            break; 
            
        case(R.id.RadioButton_Male):
        case(R.id.RadioButton_Female):
            mIsGenderValid = true;
            onGenderResult();
            break;
            
        default:
            break;
        }
    }
     
    @Override
    protected Dialog onCreateDialog(int id) 
    {
        switch (id) 
        {
            case PASSWORD_DIALOG_ID:
                mPasswordDialog = new Dialog(this);
                mPasswordDialog.setContentView(R.layout.password_dialog);
                mPasswordDialog.setTitle(getString(R.string.str_passwordDialogTitle));
                
                // Design: 1st edit is always enabled.
                // 2nd edit is enabled when 1st edit contains valid password.
                // Ok button is enabled when two edits contain same password.
                mPassword1EditText1 = (EditText)(mPasswordDialog.findViewById(R.id.EditText_Password1));
                mPasswordValidator 	= new PasswordValidator(this, mPassword1EditText1);
                mPassword1EditText1.addTextChangedListener(mPasswordValidator);
                
                mPassword1EditText2 = (EditText)(mPasswordDialog.findViewById(R.id.EditText_Password2)); 
                mPassword1EditText2.addTextChangedListener(mPasswordDlgTextWatcher);
                mPassword1EditText2.setEnabled(false);
                
                mPasswordDlgOK = (Button)(mPasswordDialog.findViewById(R.id.Button_PasswordOk));
                mPasswordDlgOK.setText(getResources().getText(android.R.string.ok));
                mPasswordDlgOK.setOnClickListener(this);
                
                mPasswordDlgCancel = (Button)(mPasswordDialog.findViewById(R.id.Button_PasswordCancel));
                mPasswordDlgCancel.setText(getResources().getText(android.R.string.cancel));
                mPasswordDlgCancel.setOnClickListener(this);                
                
                mPasswordDlgOK.setEnabled(false);        		
                return mPasswordDialog;
                
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, this, 1990, 0, 1); // A random initial date value.
        }
        
        return null;
    } 
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) 
    {
        switch (id) 
        {
            case DATE_DIALOG_ID:
                Calendar userCalendar = null;
                int legalAgeLimitForAccount = DobValidator.mLegalAgeLimitForAccount;
                if (mLoggedInUser != null)
                {
                    String dob = mDOBEditText.getText().toString();
                    mDobMonth = Integer.parseInt(dob.substring(0,2)) - 1; // See updateDobDisplay() for the '-1'.
                    mDobDay = Integer.parseInt(dob.substring(3,5)); // The month that was set (0-11) for compatibility with Calendar
                    mDobYear = Integer.parseInt(dob.substring(6,10));

                    userCalendar = Calendar.getInstance();
                    userCalendar.set(Calendar.YEAR, mDobYear);
                    userCalendar.set(Calendar.MONTH, mDobMonth);
                    userCalendar.set(Calendar.DAY_OF_MONTH, 1); 
                }
                else
                {
                    userCalendar = Calendar.getInstance();
                    userCalendar.add(Calendar.YEAR, -legalAgeLimitForAccount);
                    userCalendar.set(Calendar.DAY_OF_MONTH, 1);
                }

                ((DatePickerDialog) dialog).updateDate(userCalendar.get(Calendar.YEAR)
                                                            , userCalendar.get(Calendar.MONTH)
                                                            , userCalendar.get(Calendar.DAY_OF_MONTH));
                break;
                
            case PASSWORD_DIALOG_ID:
                break;
        }
    }    
    
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
    {
        mDobYear = year;
        mDobMonth = monthOfYear; // The month that was set (0-11) for compatibility with Calendar.
        mDobDay = dayOfMonth;
        updateDobDisplay();
    }   
    
    private void updateDobDisplay() 
    {
        // mDobMonth is 0 based. Display is 1 based.
        ++mDobMonth;
        String strMonth = Integer.toString(mDobMonth);
        if (mDobMonth < 10)
            strMonth = "0" + strMonth;
        
        String strDay = Integer.toString(mDobDay);
        if (mDobDay < 10)
            strDay = "0" + strDay;
        
        mDob = new StringBuilder(strMonth).append("-").append(strDay).append("-").append(mDobYear).toString();
        mDOBEditText.setText(mDob);
    }

    void initActivity()
    {
        mFirstNameLabel 	= (TextView)findViewById(R.id.TextView_FirstName);
        mLastNameLabel 		= (TextView)findViewById(R.id.TextView_LastName);
        mUserNameLabel 		= (TextView)findViewById(R.id.TextView_UserName);
        mEmailLabel 		= (TextView)findViewById(R.id.TextView_Email);
        mPasswordLabel 		= (TextView)findViewById(R.id.TextView_Password);
        mDobLabel 			= (TextView)findViewById(R.id.TextView_DOB);
        mGenderLabel		= (TextView)findViewById(R.id.TextView_Gender);
       
        mFirstNameEditText 	= (EditText)findViewById(R.id.EditText_FirstName);
        mLastNameEditText 	= (EditText)findViewById(R.id.EditText_LastName);
        mUserNameEditText 	= (EditText)findViewById(R.id.EditText_Nickname);
        mEmailEditText 		= (EditText)findViewById(R.id.EditText_Email);
        mPasswordEditText 	= (EditText)findViewById(R.id.EditText_Password);
        mDOBEditText 		= (EditText)findViewById(R.id.EditText_DOB);
        
        mPasswordButton 	= (ImageButton)findViewById(R.id.Button_Password);
        mDOBButton 			= (ImageButton)findViewById(R.id.Button_DOB);
        mGenderButtonMale 	= (RadioButton)findViewById(R.id.RadioButton_Male);
        mGenderButtonFemale = (RadioButton)findViewById(R.id.RadioButton_Female);
        mAvaterImageButton  = (ImageButton)findViewById(R.id.ImageButton_Avatar);
        
        mPasswordButton.setOnClickListener(this);
        mDOBButton.setOnClickListener(this);
        mGenderButtonMale.setOnClickListener(this);
        mGenderButtonFemale.setOnClickListener(this);
        findViewById(R.id.Button_CreateAvatar).setOnClickListener(this);
        findViewById(R.id.Button_PickGraphic).setOnClickListener(this);
        
        mUpdateButton = (Button)findViewById(R.id.Button_UpdateAccount);
        mUpdateButton.setOnClickListener(this);
        findViewById(R.id.Button_Reset).setOnClickListener(this);
        
        mCreateButton = (Button)findViewById(R.id.Button_CreateNLoginAccount);
        mCreateButton.setOnClickListener(this);
        findViewById(R.id.Button_DiscardNExit).setOnClickListener(this);
        
        mPasswordDlgTextWatcher = new PasswordDlgTextWatcher();	    
    }
    
    protected void collectEnteredAccountData()
    {
        mFirstName  = mFirstNameEditText.getText().toString();
        mLastName   = mLastNameEditText.getText().toString();
        mUserName   = mUserNameEditText.getText().toString();
        mEmail      = mEmailEditText.getText().toString();
        // mPassword - collected when dialog box is closed
        // mDob - collected when dialog box is closed
        
        if (mGenderButtonMale.isChecked())
        {
            mGender = getString(R.string.str_Male);
        }
        else if (mGenderButtonFemale.isChecked())
        {
            mGender = getString(R.string.str_Female);
        }
        
        // mAvatarFileName - collected when user created / selected an avatar
    }

    protected boolean loadAvatarImage()
    {
        if (mAvatarFileName.equals(""))
            return false;
        
        String path = new File(getFilesDir(), mAvatarFileName + avatarFileNameExtension).getAbsolutePath(); 
        mAvatarImage = BitmapFactory.decodeFile(path);
        return true;
    }
    
    protected boolean saveAvatarImage()
    {
        if (mAvatarFileName.equals(""))
            return true; // User did not select an avatar, therefore no error here.

        boolean success = false;
        try 
        {
            success = mAvatarImage.compress(CompressFormat.JPEG, 100, openFileOutput(mAvatarFileName + avatarFileNameExtension, MODE_PRIVATE)); 
        } 
        catch (FileNotFoundException e) 
        {
        }
        return success;
    }	
    
    @Override
    public void onUserNameResult()	
    {
        mIsUserNameValid = mUserNameValidator.isValid();
        if (mIsUserNameValid)
        {
            boolean alreadyUsed = UserTable.userNameAlreadyUsed(this, mUserNameEditText.getText().toString());
            mUserNameLabel.setTextColor(!alreadyUsed ? Color.BLACK : Color.RED);
            ((LinearLayout)findViewById(R.id.Layout_UserName_Error)).setVisibility(!alreadyUsed ? View.GONE : View.VISIBLE);
            mIsUserNameValid = !alreadyUsed;
        }
        else
        {
            mUserNameLabel.setTextColor(Color.RED);
        }
        updateApplyButtonState();
    }

    @Override
    public void onFirstNameResult() 
    {
        if (mFirstNameValidator.isValid() != mIsFirstNameValid)
        {
            mIsFirstNameValid = mFirstNameValidator.isValid();
            mFirstNameLabel.setTextColor( mIsFirstNameValid ? Color.BLACK : Color.RED);
            updateApplyButtonState();
        }
    }
    
    @Override
    public void onLastNameResult() 
    {
        if (mLastNameValidator.isValid() != mIsLastNameValid)
        {
            mIsLastNameValid = mLastNameValidator.isValid();
            mLastNameLabel.setTextColor( mIsLastNameValid ? Color.BLACK : Color.RED);
            updateApplyButtonState();
        }
    }
    
    @Override
    public void onEmailResult() 
    {
        if (mEmailValidator.isValid() != mIsEmailValid)
        {
            mIsEmailValid = mEmailValidator.isValid();
            mEmailLabel.setTextColor( mIsEmailValid ? Color.BLACK : Color.RED);
            updateApplyButtonState();
        }
    }
    
    @Override
    public void onPasswordResult() 
    {
        if (mPasswordValidator.isValid() != mIsPasswordValid)
        {
            mIsPasswordValid = mPasswordValidator.isValid();
            mPasswordLabel.setTextColor( mIsPasswordValid ? Color.BLACK : Color.RED);
            mPassword1EditText2.setEnabled(mIsPasswordValid);
        }
    }
    
    @Override
    public void onDobResult() 
    {
        if (mDobValidator.isValid() != mIsDobValid)
        {
            mIsDobValid = mDobValidator.isValid();
            mDobLabel.setTextColor( mIsDobValid ? Color.BLACK : Color.RED);
            updateApplyButtonState();
        }
    }
    
    @Override
    public void onGenderResult() 
    {
        mGenderLabel.setTextColor( mIsGenderValid? Color.BLACK : Color.RED);
        updateApplyButtonState();
    }
    
    protected class PasswordDlgTextWatcher extends DataValidator
    {
        @Override
        public void afterTextChanged(Editable view) 
        {
            String strPassword1 = mPassword1EditText1.getText().toString();
            String strPassword2 = mPassword1EditText2.getText().toString();
            TextView passwordConfirmation = (TextView)(mPasswordDialog.findViewById(R.id.Password_Confirmation));
            String message = "";
            int messageId = 0;
            boolean isPasswordDlgOkEnabled = false;
            if (strPassword1.length() != 0 && strPassword1.equals(strPassword2))
            {
                messageId = R.string.str_PasswordsMatch;
                isPasswordDlgOkEnabled = true;
                mIsPasswordValid = true;
            }
            else
            {
                messageId = R.string.str_PasswordsNoMatch;
                isPasswordDlgOkEnabled = false;
                mIsPasswordValid = false;
            }
            
            mPasswordDlgOK.setEnabled(isPasswordDlgOkEnabled);
            message = getResources().getString(messageId);
            passwordConfirmation.setText(message);
        }
    }
}