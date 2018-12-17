package com.example.admin.clothes.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Bitmap iconBitmap ;
    private String titleStr ;
    private String descStr ;

    public void setIcon(Bitmap icon) {
        iconBitmap = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public Bitmap getIcon() {
        return this.iconBitmap ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}
