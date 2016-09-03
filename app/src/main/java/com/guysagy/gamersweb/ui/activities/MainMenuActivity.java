package com.guysagy.gamersweb.ui.activities;

import com.guysagy.gamersweb.settings.AppSettings;
import com.guysagy.gamersweb.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public final class MainMenuActivity extends BaseActivity implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener
{
    ExpandableMenusListAdapter menuItemsAdapter;
    ExpandableListView menuList;
    View menuView;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        menuView = getLayoutInflater().inflate(R.layout.main_menu, null);
        setActivityView(menuView, R.string.menu_title, Color.BLACK);
        menuItemsAdapter = new ExpandableMenusListAdapter();
        menuList = (ExpandableListView)(findViewById(R.id.ListView_Menu));
        menuList.setAdapter(menuItemsAdapter);
        menuList.setOnGroupClickListener(this);
        menuList.setOnChildClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) 
    {
        super.onNewIntent(intent);
    }
    
    private boolean onMenuClick(View itemClicked)
    {
        String menuItemText = ((TextView)itemClicked).getText().toString();
        return menusHandler(menuItemText);
    } 
    
    public class ExpandableMenusListAdapter extends BaseExpandableListAdapter 
    {
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) 
        {
            TextView textView = getGenericView();
            textView.setText(getGroup(groupPosition).toString());
            return textView;
        }
        
        public TextView getGenericView() 
        {
            final int expandButtonRectSize = 128; // The size of the down arrow to expand menu item.
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, expandButtonRectSize);
            TextView textView = new TextView(MainMenuActivity.this);
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT); 	// Center the text vertically
            textView.setPadding(expandButtonRectSize, 0, 0, 0); 			// Set the text starting position
            return textView;
        }
        
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                    View convertView, ViewGroup parent) 
        {
            TextView textView = getGenericView();
            textView.setText(getChild(groupPosition, childPosition).toString());
            return textView;
        }
        
        private String[] menus = { getString(R.string.menu_item_welcome),
                                    getString(R.string.menu_item_games),
                                    getString(R.string.menu_item_scores),
                                    getString(R.string.menu_item_account),
                                    getString(R.string.menu_item_about) };
        
        private String[][] subMenus = 
        {
                // Welcome activity - no sub menus.
                {
                },
                // Games activity - only Tic Tac Toe currently.
                {
                    getResources().getString(R.string.str_TicTacToe),
                },
                // Scores activity.
                {
                    getString(R.string.menu_item_scores_by_name),
                    getString(R.string.menu_item_scores_by_score),
                },
                // Account activity.
                {
                    "Account Login/out",
                    getString(R.string.menu_item_account_create),
                    getString(R.string.menu_item_account_update),
                    getString(R.string.menu_item_account_delete)
                },
                // About activity.
                {
                    getString(R.string.menu_item_info),
                 //   getString(R.string.menu_item_feedback_post),
                 //   getString(R.string.menu_item_feedback_read)
                }
        };

        public int getGroupCount() 
        {
            return menus.length;
        }
        
        public Object getGroup(int groupPosition) 
        {
            return menus[groupPosition];
        }
        
        public long getGroupId(int groupPosition) 
        {
            return groupPosition;
        }        

        public int getChildrenCount(int groupPosition) 
        {
            return subMenus[groupPosition].length;
        }
        
        public Object getChild(int groupPosition, int childPosition) 
        {
            subMenus[3][0] = (mLoggedInUser == null) ? getString(R.string.menu_item_account_login) : getString(R.string.menu_item_account_logout) ;
            return subMenus[groupPosition][childPosition];
        }

        public long getChildId(int groupPosition, int childPosition) 
        {
            return childPosition;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) 
        {
            return true;
        }

        public boolean hasStableIds() 
        {
            return true;
        }
    }

    @Override    
    public boolean onGroupClick(ExpandableListView listView, View clickedView, int groupId, long groupRowId) 
    {
        return onMenuClick(clickedView);
    }    
    
    @Override
    public boolean onChildClick(ExpandableListView listView, View clickedView, int groupId, int childId, long childRowId) 
    {
        boolean retVal = onMenuClick(clickedView);
        menuItemsAdapter.notifyDataSetChanged();
        menuList.invalidateViews();
        menuList.refreshDrawableState();
        return retVal;
    }
} 