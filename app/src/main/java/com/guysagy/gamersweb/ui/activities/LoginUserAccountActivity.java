package com.guysagy.gamersweb.ui.activities;

import com.guysagy.gamersweb.db.UserTable;
import com.guysagy.gamersweb.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.view.inputmethod.InputMethodManager;

public class LoginUserAccountActivity extends BaseActivity implements View.OnClickListener
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setActivityView(R.layout.login, R.string.str_Login, Color.WHITE);
        findViewById(R.id.Button_OkLoginAccount).setOnClickListener(this);   
        findViewById(R.id.Button_CancelLoginAccount).setOnClickListener(this);
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        EditText userNameEdit = (EditText)findViewById(R.id.EditText_Login_Username);
        userNameEdit.requestFocus();
    }
    
    @Override
    protected void onNewIntent(Intent intent) 
    {
        super.onNewIntent(intent);
    }
    
    @Override
    public void onClick(View view) 
    {
        switch(view.getId())
        {
            case(R.id.Button_OkLoginAccount):
                boolean success = doLogin();
                if (success == true)
                {
                    makeShortToast(R.string.str_accountLoginSuccess);
                    finish();
                }
                else
                {
                    makeLongToast(R.string.str_accountLoginFailure);
                }
                break;
                
            case(R.id.Button_CancelLoginAccount):
                finish();
                break;
        }
    }
    
    boolean doLogin()
    {
        EditText userNameEdit = (EditText)findViewById(R.id.EditText_Login_Username);
        String userName = userNameEdit.getText().toString(); 

        EditText passwordEdit = (EditText)findViewById(R.id.EditText_Login_Password);
        String password = passwordEdit.getText().toString();
        
        mLoggedInUser = UserTable.authenticate(this, userName, password);
        return mLoggedInUser != null;
    }
}