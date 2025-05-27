package com.example.musicapp.ui.dialog.optionmenu;

import com.example.musicapp.utils.MenuOptionUtils;

public class OptionMenuItem {
    private final MenuOptionUtils.MenuOption mMenuOption;
    private final int mIconId;
    private final int mMenuItemTitle;
    private boolean mIsFavorite;

    public OptionMenuItem(MenuOptionUtils.MenuOption menuOption, int iconId, int menuItemTitle) {
        mMenuOption = menuOption;
        mIconId = iconId;
        mMenuItemTitle = menuItemTitle;
    }

    public OptionMenuItem(MenuOptionUtils.MenuOption menuOption, int iconId, int menuItemTitle, boolean isFavorite) {
        mMenuOption = menuOption;
        mIconId = iconId;
        mMenuItemTitle = menuItemTitle;
        mIsFavorite = isFavorite;
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

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }
}
