package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextInput;
import com.watabou.utils.DeviceCompat;

public class WndTextNumberInput extends Window {

    private static final int WIDTH = 130;
    private static final int W_LAND_EXTRA = 190; //extra width is sometimes used in landscape
    private static final int MARGIN = 2;
    private static final int BUTTON_HEIGHT = 16;
    private boolean pressedDecimalPoint = false;

    protected TextInput textBox;

    protected RedButton btnNumber;

    protected RedButton btnNumberZero;

    protected RedButton btnDecimalPoint;

    protected RedButton btnCE;

    public WndTextNumberInput(final String title, final String body, final String initialValue, final int maxLength,
                              final boolean multiLine, final String posTxt, final String negTxt, final boolean allowDecimalPoint) {
        super();

        //need to offset to give space for the soft keyboard
        if (!DeviceCompat.isDesktop()) {
            if (PixelScene.landscape()) {
                offset(0, -45);
            } else {
                offset(0, multiLine ? -60 : -45);
            }
        }

        final int width;
        if (PixelScene.landscape() && (multiLine || body != null)) {
            width = W_LAND_EXTRA; //more space for landscape users
        } else {
            width = WIDTH;
        }

        float pos = 2;

        if (title != null) {
            final RenderedTextBlock txtTitle = PixelScene.renderTextBlock(title, 9);
            txtTitle.maxWidth(width);
            txtTitle.hardlight(Window.TITLE_COLOR);
            txtTitle.setPos((width - txtTitle.width()) / 2, 2);
            add(txtTitle);

            pos = txtTitle.bottom() + 2 * MARGIN;
        }

        if (body != null) {
            final RenderedTextBlock txtBody = PixelScene.renderTextBlock(body, 6);
            txtBody.maxWidth(width);
            txtBody.setPos(0, pos);
            add(txtBody);

            pos = txtBody.bottom() + 2 * MARGIN;
        }

        int textSize = (int)PixelScene.uiCamera.zoom * (multiLine ? 6 : 9);
        textBox = new TextInput(Chrome.get(Chrome.Type.TOAST_WHITE), multiLine, textSize){
            @Override
            public void enterPressed() {
                //triggers positive action on enter pressed, only with non-multiline though.
                onSelect(true, getText());
                hide();
            }
        };
        if (initialValue != null){
            textBox.setText(initialValue);
            if(initialValue.indexOf(".")!=-1)
                pressedDecimalPoint = true;
        }
        textBox.setMaxLength(maxLength);
        textBox.setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped(TextField textField, char c) {
                switch (c){
                    case '\r':
                    case '\n':
                        if(!multiLine)
                            textBox.enterPressed();
                        break;
                    case '.':
                        int position = textBox.getCursorPosition();
                        String text = textBox.getText();
                        if(pressedDecimalPoint){
                            textBox.setText(deleteChar(text,position-1));
                        }else {
                            if (text.indexOf(".")==0) {
                                textBox.setText(deleteChar(text,0));
                                textBox.setCursorPosition(textBox.getText().length());
                                return;
                            }
                            pressedDecimalPoint = true;
                        }
                        break;
                }
            }

            public String deleteChar(String string,int position){
                if( position < 0 || position > string.length() ) return null;

                StringBuilder stringBuilder = new StringBuilder(string);
                stringBuilder.deleteCharAt(position);
                return stringBuilder.toString();
            }
        });

        //sets different height depending on whether this is a single or multi line input.
        final float inputHeight;
        if (multiLine) {
            inputHeight = 64; //~8 lines of text
        } else {
            inputHeight = 16;
        }
        add(textBox);
        textBox.setRect(MARGIN, pos, width-2*MARGIN, inputHeight);

        pos += inputHeight + MARGIN;

        // 创建确认按钮
        final RedButton positiveBtn = new RedButton(posTxt) {
            @Override
            protected void onClick() {
                onSelect(true, textBox.getText());
                hide();
            }
        };

        // 计算数字按钮区域的高度
        float numberAreaHeight = BUTTON_HEIGHT * 4 + MARGIN * 3;


        boolean landscape = Game.width > Game.height;

        // 计算每行按钮的宽度
        float btnWidth = landscape ? 60 : 40;

        // 创建数字按钮
        for (int i = 1; i <= 9; i++) {
            final int number = i;
            btnNumber = new RedButton(String.valueOf(number)) {
                @Override
                protected void onClick() {
                    int position = textBox.getCursorPosition();
                    if(position==0){
                        textBox.setText( number+textBox.getText() );
                        textBox.setCursorPosition(1);
                    }else if(position==textBox.getText().length()){
                        textBox.setText(textBox.getText() + number);
                    }else {
                        textBox.setText(textBox.getText().substring(0,position)+number+textBox.getText().substring(position));
                        textBox.setCursorPosition(position+1);
                    }
                }
            };

            int row = (i - 1) / 3;  // 计算按钮所在的行数

            btnNumber.setRect((MARGIN + btnWidth * ((i - 1) % 3))+3, pos + row * (BUTTON_HEIGHT + MARGIN),
                    btnWidth, BUTTON_HEIGHT);

            add(btnNumber);
        }

        // 创建最后一行的0按钮
        btnNumberZero = new RedButton("0") {
            @Override
            protected void onClick() {
                textBox.setText(textBox.getText() + "0");
            }
        };
        btnNumberZero.setRect(MARGIN+3, pos + 3 * (BUTTON_HEIGHT + MARGIN),
                btnWidth, BUTTON_HEIGHT);
        add(btnNumberZero);

        if(allowDecimalPoint){
            btnDecimalPoint = new RedButton(".") {
                @Override
                protected void onClick() {
                    if(!pressedDecimalPoint){
                        int position = textBox.getCursorPosition();
                        if(position==0){
                            textBox.setCursorPosition(textBox.getText().length());
                            return;
                        }else if(position==textBox.getText().length()){
                            textBox.setText(textBox.getText() + ".");
                        }else {
                            textBox.setText(textBox.getText().substring(0,position)+"."+textBox.getText().substring(position));
                            textBox.setCursorPosition(position+1);
                        }
                        pressedDecimalPoint = true;
                    }
                }
            };
            btnDecimalPoint.setRect(btnNumberZero.right(), pos + 3 * (BUTTON_HEIGHT + MARGIN),
                    btnWidth, BUTTON_HEIGHT);
            add(btnDecimalPoint);
        }

        btnCE = new RedButton(Messages.get(WndTextNumberInput.class,"clear")) {
            @Override
            protected void onClick() {textBox.setText(null); pressedDecimalPoint = false;}
        };
        btnCE.setRect(allowDecimalPoint ? btnDecimalPoint.right() : btnNumberZero.right(), pos + 3 * (BUTTON_HEIGHT + MARGIN),
                allowDecimalPoint ? btnWidth : btnWidth*2, BUTTON_HEIGHT);
        add(btnCE);

        // 更新pos变量，确保按钮区域在确认按钮上方
        pos += numberAreaHeight + MARGIN;

//        // 设置确认按钮的位置
//        positiveBtn.setRect(MARGIN, pos, width - MARGIN * 2, BUTTON_HEIGHT);
//        add(positiveBtn);


        final RedButton negativeBtn;
        // 如果有取消按钮，创建取消按钮并设置位置
        if (negTxt != null) {
            negativeBtn = new RedButton(negTxt) {
                @Override
                protected void onClick() {
                    onSelect(false, textBox.getText());
                    hide();
                }
            };
            negativeBtn.setRect(positiveBtn.right() + MARGIN, pos, (width - MARGIN * 3) / 2f, BUTTON_HEIGHT);
            add(negativeBtn);
        } else {
            negativeBtn = null;
        }

        if (negTxt != null) {
            positiveBtn.setRect(MARGIN, pos, (width - MARGIN * 3) / 2, BUTTON_HEIGHT);
            add(positiveBtn);
            negativeBtn.setRect(positiveBtn.right() + MARGIN, pos, (width - MARGIN * 3) / 2, BUTTON_HEIGHT);
            add(negativeBtn);
        } else {
            positiveBtn.setRect(MARGIN, pos, width - MARGIN * 2, BUTTON_HEIGHT);
            add(positiveBtn);
        }

        pos += BUTTON_HEIGHT + MARGIN;

        //need to resize first before laying out the text box, as it depends on the window's camera
        resize(width, (int) pos);

        textBox.setRect(MARGIN, textBox.top(), width-2*MARGIN, inputHeight);

    }

    @Override
    public void offset(int xOffset, int yOffset) {
        super.offset(xOffset, yOffset);
        if (textBox != null){
            textBox.setRect(textBox.left(), textBox.top(), textBox.width(), textBox.height());
            Gdx.input.setOnscreenKeyboardVisible(false);
        }
    }

    public void onSelect(boolean positive, String text){ }

    @Override
    public void onBackPressed() {
        //Do nothing, prevents accidentally losing writing
    }
}
