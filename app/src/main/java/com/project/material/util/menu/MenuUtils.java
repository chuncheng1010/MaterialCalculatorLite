package com.project.material.util.menu;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.project.material.R;

public class MenuUtils {

    public static final int MENU_HOME = android.R.id.home;
    public static final int MENU_SAVE = 10000;
    public static final int MENU_GOTO_PRO = 20000;
    public static final int MENU_REFRESH_APP = 30000;
    public static final int MENU_SEARCH = 40000;
    public static final int MENU_RATE_US = 50000;
    public static final int MENU_SHARE_APP = 60000;
    public static final int MENU_FACEBOOK_ITEM = 60001;
    public static final int MENU_TWITTER_ITEM = 60002;
    public static final int MENU_EXIT_APP = 70000;


    public static void addMenu(Menu menu) {
        SubMenu subMenu, subItemMenu;

        subMenu = menu.addSubMenu(0, MENU_SAVE, 0, "Save");
        subMenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        subMenu.getItem().setIcon(android.R.drawable.ic_menu_save);

        /*
        subMenu = menu.addSubMenu("More");
        subMenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        subMenu.getItem().setIcon(R.drawable.ic_action_overflow);

        subMenu.addSubMenu(1, MENU_GOTO_PRO, 0, "PRO Version");
        subMenu.addSubMenu(2, MENU_REFRESH_APP, 0, "Refresh");
        subMenu.addSubMenu(3, MENU_SEARCH, 0, "Search");

        subMenu.addSubMenu(4, MENU_RATE_US, 0, "Rate us");
        */
        subItemMenu = subMenu.addSubMenu(5, MENU_SHARE_APP, 0, "Share");
        subItemMenu.add(5, MENU_FACEBOOK_ITEM, 0, "Facebook").setIcon(R.drawable.facebook);
        subItemMenu.add(5, MENU_TWITTER_ITEM, 1, "Twitter").setIcon(R.drawable.twitter);

        subMenu.addSubMenu(6, MENU_EXIT_APP, 0, "Exit");

    }

}
