package com.example.musicapp.ui.dialog.optionmenu;

import com.example.musicapp.utils.MenuOptionUtils;

public class OptionMenuItem {
    private final MenuOptionUtils.MenuOption mMenuOption;
    private final int mIconId;
    private final int mMenuItemTitle;

    public OptionMenuItem(MenuOptionUtils.MenuOption menuOption, int iconId, int menuItemTitle) {
        mMenuOption = menuOption;
        mIconId = iconId;
        mMenuItemTitle = menuItemTitle;
    }

    public MenuOptionUtils.MenuOption getMenuOption() {
        return mMenuOption;
    }

    public int getIconId() {
        return mIconId;
    }

    public int getMenuItemTitle() {
        return mMenuItemTitle;
    }
}
