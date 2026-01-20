//package com.shatteredpixel.shatteredpixeldungeon.custom.utils;
//
//import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
//import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
//import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
//import com.watabou.noosa.Image;
//
//public class CrashReportButton extends Button {
//
//    protected Image image;
//
//    public CrashReportButton() {
//        super();
//
//        width = image.width;
//        height = image.height;
//    }
//
//    @Override
//    protected void createChildren() {
//        super.createChildren();
//
//        image = Icons.WARNING.get();
//        add( image );
//    }
//
//    @Override
//    protected void layout() {
//        super.layout();
//
//        image.x = x;
//        image.y = y;
//    }
//
//    @Override
//    protected void onClick() {
//        ShatteredPixelDungeon.switchNoFade(CrashReportScene.class);
//    }
//}