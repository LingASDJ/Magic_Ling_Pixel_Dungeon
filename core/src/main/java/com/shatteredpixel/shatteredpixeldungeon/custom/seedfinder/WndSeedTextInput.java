package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChallenges;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.TextInput;

public class WndSeedTextInput extends Window {

    private static final int WIDTH = 135;
    private static final int W_LAND_EXTRA = 220; //extra width is sometimes used in landscape
    private static final int MARGIN = 1;
    private static final int BUTTON_HEIGHT = 16;

    protected TextInput textBox;

    protected RedButton btnCopy;
    protected RedButton btnPaste;
    protected OptionSlider reachSlider;
    protected IconButton  challengeButton;

    public WndSeedTextInput(final String title, final String body, final String initialValue, final int maxLength,
                            final boolean multiLine, final String posTxt, final String negTxt) {
        super();

        offset(0, -20);



        final int width;
        if (PixelScene.landscape() && (multiLine || body != null)) {
            width = W_LAND_EXTRA; //more space for landscape users
        } else {
            width = WIDTH;
        }

        IconButton infoButton = new IconButton(Icons.get(Icons.INFO)){
            @Override
            protected void onClick() {
                super.onClick();
                ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(this,"read")));
            }
        };
        infoButton.setRect(width-20, -3, 16, 16);
        add(infoButton);

        RenderedTextBlock txt = PixelScene.renderTextBlock(Messages.get(this,"read"), 9);
        txt.maxWidth(width);
        txt.hardlight(Window.TITLE_COLOR);
        txt.setPos(infoButton.left()-26, 2);
        add(txt);

        float pos = 2;

        if (title != null) {
            final RenderedTextBlock txtTitle = PixelScene.renderTextBlock(title, 9);
            txtTitle.maxWidth(width);
            txtTitle.hardlight(Window.TITLE_COLOR);
            txtTitle.setPos((width - txtTitle.width()) / 2, 2);
            add(txtTitle);

            pos = txtTitle.bottom() + 4 * MARGIN;
        }




        if (body != null) {
            final RenderedTextBlock txtBody = PixelScene.renderTextBlock(body, 6);
            txtBody.maxWidth(width);
            txtBody.setPos(0, pos);
            add(txtBody);

            pos = txtBody.bottom() + 2 * MARGIN;
        }

        int textSize = (int)PixelScene.uiCamera.zoom * (multiLine ? 6 : 9);
        textBox = new TextInput(Chrome.get(Chrome.Type.SCROLL), multiLine, textSize){
            @Override
            public void enterPressed() {
                //triggers positive action on enter pressed, only with non-multiline though.
                onSelect(true, getText());
                hide();
            }
        };
        if (initialValue != null) textBox.setText(initialValue);
        textBox.setMaxLength(maxLength);

        //sets different height depending on whether this is a single or multi line input.
        final float inputHeight;
        if (multiLine) {
            inputHeight = 64; //~8 lines of text
        } else {
            inputHeight = 36;
        }

        float textBoxWidth = width-3*MARGIN-BUTTON_HEIGHT;

        add(textBox);
        textBox.setRect(MARGIN, pos, textBoxWidth, inputHeight);

        btnCopy = new RedButton(""){
            @Override
            protected void onPointerDown() {
                super.onPointerDown();
                PointerEvent.clearKeyboardThisPress = false;
            }

            @Override
            protected void onPointerUp() {
                super.onPointerUp();
                PointerEvent.clearKeyboardThisPress = false;
            }

            @Override
            protected void onClick() {
                super.onClick();
                textBox.copyToClipboard();
            }
        };
        btnCopy.icon(Icons.COPY.get());
        add(btnCopy);

        btnPaste = new RedButton(""){
            @Override
            protected void onPointerDown() {
                super.onPointerDown();
                PointerEvent.clearKeyboardThisPress = false;
            }

            @Override
            protected void onPointerUp() {
                super.onPointerUp();
                PointerEvent.clearKeyboardThisPress = false;
            }

            @Override
            protected void onClick() {
                super.onClick();
                textBox.pasteFromClipboard();
            }

        };
        btnPaste.icon(Icons.PASTE.get());
        add(btnPaste);

        reachSlider = new OptionSlider(Messages.get(SeedFinderScene.class,"ds"), "1", "26", 1, 26) {
            @Override
            public int getTitleTextSize(){
                return 6;
            }
            @Override
            protected void onChange() {
                SPDSettings.FoundDepth(getSelectedValue());
                ShatteredPixelDungeon.seamlessResetScene();
            }
        };
        reachSlider.setSelectedValue(SPDSettings.FoundDepth());
        add(reachSlider);

        challengeButton = new IconButton(
                Icons.get( SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF)){
            @Override
            protected void onClick() {
                    ShatteredPixelDungeon.switchNoFade(SeedEmptyScene.class);
            }

            @Override
            public void update() {
                if (SPDSettings.challenges() > 0) {
                    icon(Icons.get( Icons.CHALLENGE_ON));
                } else {
                    icon(Icons.get( Icons.CHALLENGE_OFF));
                }
                super.update();
            }

            @Override
            protected String hoverText() {
                return Messages.titleCase(Messages.get(WndChallenges.class, "title"));
            }
        };
        add(challengeButton);

        btnCopy.setRect(textBoxWidth + 2*MARGIN, pos, BUTTON_HEIGHT+3, BUTTON_HEIGHT+10);
        btnPaste.setRect(textBoxWidth + 2*MARGIN, btnCopy.bottom()+MARGIN+2, BUTTON_HEIGHT+3, BUTTON_HEIGHT+10);

        reachSlider.setRect(0, pos/2f-5, width-20, 16);
        challengeButton.setRect(reachSlider.right()+2, pos/2f-5, 20, 16);

        pos += inputHeight + MARGIN;

        final RedButton positiveBtn = new RedButton(posTxt) {
            @Override
            protected void onClick() {
                onSelect(true, textBox.getText());
                hide();
            }
        };

        final RedButton negativeBtn;
        if (negTxt != null) {
            negativeBtn = new RedButton(negTxt) {
                @Override
                protected void onClick() {
                    onSelect(false, textBox.getText());
                    hide();
                }
            };
        } else {
            negativeBtn = null;
        }

        float btnWidth = multiLine ? width-2*MARGIN : textBoxWidth;
        if (negTxt != null) {
            positiveBtn.setRect(MARGIN, pos, (btnWidth - MARGIN) / 2, BUTTON_HEIGHT);
            add(positiveBtn);
            negativeBtn.setRect(positiveBtn.right() + MARGIN, pos, (btnWidth - MARGIN) / 2, BUTTON_HEIGHT);
            add(negativeBtn);
        } else {
            positiveBtn.setRect(MARGIN, pos, btnWidth, BUTTON_HEIGHT);
            add(positiveBtn);
        }

        pos += BUTTON_HEIGHT;

        //need to resize first before laying out the text box, as it depends on the window's camera
        resize(width, (int) pos);

        textBox.setRect(MARGIN, textBox.top(), textBoxWidth, inputHeight);

        PointerEvent.clearKeyboardThisPress = false;

    }

    @Override
    public synchronized void update() {
        super.update();
        btnCopy.enable(!textBox.getText().isEmpty());
        btnPaste.enable(Gdx.app.getClipboard().hasContents());
    }

    @Override
    public void offset(int xOffset, int yOffset) {
        super.offset(xOffset, yOffset);
        if (textBox != null){
            textBox.setRect(textBox.left(), textBox.top(), textBox.width(), textBox.height());
        }
    }

    public void onSelect(boolean positive, String text){ }

    @Override
    public void onBackPressed() {
        //Do nothing, prevents accidentally losing writing
    }
}

