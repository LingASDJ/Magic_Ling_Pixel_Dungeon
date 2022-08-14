package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;

public class StyledButtonCOP extends Button {

    private boolean checked = false;

    public boolean checked() {
        return checked;
    }

    public void checked( boolean value ) {
        if (checked != value) {
            checked = value;
            //icon.copy( Icons.get( checked ? Icons.CHECKED : Icons.UNCHECKED ) );
        }
    }

    protected NinePatch bg;
    protected RenderedTextBlock text;
    protected Image icon;
    public boolean leftJustify = false;

    public boolean multiline;

    public StyledButtonCOP(Chrome.Type type, String label ) {
        this(type, label, 9);
    }

    public StyledButtonCOP(Chrome.Type type, String label, int size ){
        super();

        bg = Chrome.get( type );
        addToBack( bg );

        text = PixelScene.renderTextBlock( size );
        text.text( label );
        add( text );
    }

    @Override
    protected void onClick() {
        super.onClick();
        checked( !checked );
    }

    @Override
    protected void layout() {

        super.layout();

        bg.x = x;
        bg.y = y;
        bg.size( width, height );

        float componentWidth = 0;

        if (icon != null) componentWidth += icon.width() + 2;

        if (text != null && !text.text().equals("")){
            if (multiline) text.maxWidth( (int)(width - componentWidth - bg.marginHor() - 2));
            componentWidth += text.width() + 2;

            text.setPos(
                    x + (width() + componentWidth)/2f - text.width() - 1,
                    y + (height() - text.height()) / 2f
            );
            PixelScene.align(text);

        }

        if (icon != null) {

            icon.x = x + (width() - componentWidth)/2f + 1;
            icon.y = y + (height() - icon.height()) / 2f;
            PixelScene.align(icon);
        }

        if (leftJustify){
            if (icon != null){
                icon.x = x + bg.marginLeft() + 1;
                PixelScene.align(icon);
                text.setPos( icon.x + icon.width() + 1, text.top());
                PixelScene.align(text);
            } else if (text != null) {
                text.setPos( x + bg.marginLeft() + 1, text.top());
                PixelScene.align(text);
            }
        }

    }

    @Override
    protected void onPointerDown() {
        bg.brightness( 1.2f );
        Sample.INSTANCE.play( Assets.Sounds.CLICK );
    }

    @Override
    protected void onPointerUp() {
        bg.resetColor();
    }

    public void enable( boolean value ) {
        active = value;
        text.alpha( value ? 1.0f : 0.3f );
    }

    public void text( String value ) {
        text.text( value );
        layout();
    }

    public String text(){
        return text.text();
    }

    public void textColor( int value ) {
        text.hardlight( value );
    }

    public void icon( Image icon ) {
        if (this.icon != null) {
            remove( this.icon );
        }
        this.icon = icon;
        if (this.icon != null) {
            add( this.icon );
            layout();
        }
    }

    public Image icon(){
        return icon;
    }

    public void alpha(float value){
        if (icon != null) icon.alpha(value);
        if (bg != null)   bg.alpha(value);
        if (text != null) text.alpha(value);
    }

    public float reqWidth() {
        float reqWidth = 0;
        if (icon != null){
            reqWidth += icon.width() + 2;
        }
        if (text != null && !text.text().equals("")){
            reqWidth += text.width() + 2;
        }
        return reqWidth;
    }

    public float reqHeight() {
        float reqHeight = 0;
        if (icon != null){
            reqHeight = Math.max(icon.height() + 4, reqHeight);
        }
        if (text != null && !text.text().equals("")){
            reqHeight = Math.max(text.height() + 4, reqHeight);
        }
        return reqHeight;
    }
}

