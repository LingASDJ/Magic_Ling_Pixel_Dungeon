package com.shatteredpixel.shatteredpixeldungeon.windows;


import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.ChoiceButton;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Plot;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Script;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.SkipIndicator;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.GameAction;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.utils.Bundle;

import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Choice;
import java.util.ArrayList;

public class WndDialog extends Window {

    //We also need at least an interface to show sentences,and some code is from Girl's front line pd
    //Detailed things are directly operated in Plot directory's files
    //By Teller in 2021/8/21

    //We used Teller ARKPD-CN in 2023-11-2
    //By KDSALing in 2023-11-2

    private static final int MARGIN 		= 2;
    private static final int BUTTON_HEIGHT	= 18;

    private static final int MARGIN_X = 5;
    private static final int MARGIN_Y = 5;

    private Image mainAvatar;
    private Image secondAvatar;
    private Image thirdAvatar;

    private RenderedTextBlock leftname;
    private RenderedTextBlock rightname;

    private RenderedTextBlock text;
    private String script;

    public static Plot settedPlot = null;

    public PointerArea area = null;

    public SkipIndicator skip = null;

    //public ColorBlock blocks[];
    public boolean readed;
    private final int color = 0xFF2F2F2F;

    public int nowAt;

    private boolean haveChoice = false;
    private com.shatteredpixel.shatteredpixeldungeon.custom.utils.Choice storedChoice[];

    private float timeLeft = 0.2f;
    private int times = 0;

    public WndDialog(Plot plot,Boolean regained) {

        super(0, 0, Chrome.get(Chrome.Type.TOAST_TR));

        resize(PixelScene.uiCamera.width ,PixelScene.uiCamera.height );

        settedPlot = plot;

        boolean landscape = false;
        if(SPDSettings.landscape()!=null)
        {
            landscape = SPDSettings.landscape();//yeah it sounds stupid but without it when entering game first time
            // without minding this would cause crash.And mostly android device don't use landscape as default
        }

        int heightUnit = landscape ? 4 : 5;
        int fontSize = landscape ? 10 : 8;

        int textSize = landscape ? 10 : 8;

        int chromeHeight = PixelScene.uiCamera.height / heightUnit - MARGIN_Y;
        int chromeWidth = PixelScene.uiCamera.width - 2 * MARGIN_X;

        shadow.am = 0;

        chrome.size(chromeWidth,chromeHeight);
        chrome.x = MARGIN_X;
        chrome.y = (float) (PixelScene.uiCamera.height - GameScene.ToolbarHeight() - chromeHeight);
        add(chrome);

        mainAvatar = Script.Portrait(Script.Character.NOBODY);
        mainAvatar.x = 0;
        add(mainAvatar);

        secondAvatar = Script.Portrait(Script.Character.NOBODY);
        secondAvatar.x = (float) (PixelScene.uiCamera.width - secondAvatar.width() - MARGIN_X);
        add(secondAvatar);

        thirdAvatar = Script.Portrait(Script.Character.NOBODY);
        thirdAvatar.x = secondAvatar.x - 16;
        add(thirdAvatar);

        float height = mainAvatar.height;

        mainAvatar.y = secondAvatar.y = thirdAvatar.y = chrome.y - height;

        leftname = PixelScene.renderTextBlock(Script.Name(Script.Character.NOBODY), fontSize);
        rightname = PixelScene.renderTextBlock(Script.Name(Script.Character.NOBODY), fontSize);

        leftname.setPos(mainAvatar.x + mainAvatar.width() , mainAvatar.y + mainAvatar.height() - leftname.height() - 2);

        rightname.setPos(thirdAvatar.x - rightname.width(), thirdAvatar.y + thirdAvatar.height() - rightname.height() - 2);

        add(leftname);
        add(rightname);

        text = PixelScene.renderTextBlock("", textSize);
        text.maxWidth(PixelScene.uiCamera.width - 2 * MARGIN_X - textSize);
        text.setPos(MARGIN_X + textSize / 2, chrome.y + MARGIN_Y);
        add(text);

        area = makeArea();
        add(area);

        skip = makeSkip();
        add(skip);

        float modifier = 0;

       /* switch (PDSettings.language()) {
            case CHINESE: modifier = 1f;
            default: }
            //FIXME Well it sounds weird but it really happens:Chinese characters have bigger possible height than English,so with its margin,in fact there are some small overlap between lines.
            //There are nothing elsewhere,but at here,if I wants to use colorblock to create some text gradually show effect,these small overlap would spoil everything,or at least makes things harder.
            //As a temporary fix,I add the margin for Chinese between lines,make it looks a little better.Also check RenderedTextBlock.java for the change.
            //By Teller,2021/9/8

            blocks = new ColorBlock[5];

            for(int i = 0;i < (landscape ? 3 :5) ; i++)
            {
                blocks[i] = new ColorBlock(1,1,color);
                blocks[i].size( width ,textSize + 2f );
                blocks[i].x = text.left();
                if(i == 0)
                {
                    blocks[i].y = text.top() - 2 * modifier;
                }
                else
                {
                    blocks[i].y = blocks[ i - 1].y + blocks[ i - 1 ].height() - modifier;
                }
                add( blocks[i]);
            }*/

        if(regained)
        {
            settedPlot.reachProcess(this);
        }
        else {
            settedPlot.initial(this);
        }
    }

    public void cancel() {
        super.hide();
    }

    public PointerArea makeArea() {
        PointerArea pointerArea = new PointerArea(0,0, PixelScene.uiCamera.width, PixelScene.uiCamera.height) //we wants to make you click anywhere to result in processing plot
        {
            @Override
            protected void onClick(PointerEvent event) {
                if(readed) {
                    if (settedPlot.end()) {
                        hide();
                    } else {
                        timeLeft = 0.02f;
                        times = 0;
                        readed = false;
                        settedPlot.process();
                    }
                }
                else skipWait();
            }

            @Override
            public GameAction keyAction(){
                return SPDAction.WAIT_OR_PICKUP;
            }

            @Override
            protected void onClick() {
                if(readed) {
                    if(settedPlot!=null) {
                        if (settedPlot.end()) {
                            hide();
                        } else {
                            timeLeft = 0.02f;
                            times = 0;
                            readed = false;
                            settedPlot.process();
                        }
                    }
                }
                else skipWait();//Due to evan's code base didn't support pointArea nor multiple hotkey,have to done this stupid thing now
            }
        };
        return pointerArea;
    }

    public void removeArea()
    {
        area.destroy();
        area.killAndErase();
        update();
    }

    public SkipIndicator makeSkip()
    {
        SkipIndicator skipIndicator = new SkipIndicator();
        skipIndicator.setPos(width - skipIndicator.width()-5,40);
        return skipIndicator;
    }

    private void removeSkip() {
        skip.destroy();
        skip.killAndErase();
        update();
    }

    //Below are all about changing things of this windows,to make things process
    public void setLeftName(String n) {
        if (leftname != null) {
            leftname.text(n);
            leftname.visible = true;
            rightname.visible = false;
        }
    }

    public void setRightName(String n) {
        if (rightname != null) {
            rightname.text(n);
            rightname.visible = true;
            leftname.visible = false;

            if (thirdAvatar.visible) {
                rightname.setPos(thirdAvatar.x - rightname.width(), mainAvatar.y + thirdAvatar.height() - rightname.height() - 2);
            } else {
                rightname.setPos(secondAvatar.x - rightname.width(), mainAvatar.y + thirdAvatar.height() - rightname.height() - 2);
            }
        }
    }

    public void setMainAvatar(Image mainAvatar) {
        this.mainAvatar.copy(mainAvatar);
        this.mainAvatar.visible = true;
    }

    public void setSecondAvatar(Image secondAvatar) {
        this.secondAvatar.copy(secondAvatar);
        this.secondAvatar.visible = true;
    }

    public void setThirdAvatar(Image thirdAvatar) {
        this.thirdAvatar.copy(thirdAvatar);
        this.thirdAvatar.visible = true;
    }

    public void secondAvatarToFront() {
        this.bringToFront(secondAvatar);
    }

    public void thirdAvaratToFront() {
        this.bringToFront(thirdAvatar);
    }

    public void darkenMainAvatar() {
        mainAvatar.visible = true;
        mainAvatar.tint(0x000000, 0.5f);
    }

    public void darkenSecondAvatar() {
        secondAvatar.visible = true;
        secondAvatar.tint(0x000000, 0.5f);
    }

    public void darkenThirdAvatar() {
        this.thirdAvatar.visible = true;
        thirdAvatar.tint(0x000000, 0.5f);
    }

    public void lightenMainAvatar() {
        mainAvatar.visible = true;
        mainAvatar.resetColor();
    }

    public void lightenSecondAvatar() {
        secondAvatar.visible = true;
        secondAvatar.resetColor();
    }

    public void lightenThirdAvatar() {
        this.thirdAvatar.visible = true;
        this.thirdAvatar.resetColor();
    }

    public void hideMainAvatar() {
        mainAvatar.visible = false;
    }

    public void hideSecondAvatar() {
        secondAvatar.visible = false;
    }

    public void hideThirdAvatar() {
        this.thirdAvatar.visible = false;
    }

    public void changeText(String txt) {
        script = txt;
        resetBlock();
    }

    public void resetBlock()
    {

    }

    public void showBackground(String txt) {
        if (mainAvatar.visible) {
            darkenMainAvatar();
        }

        if (secondAvatar.visible) {
            darkenSecondAvatar();
        }

        if (thirdAvatar.visible) {
            darkenThirdAvatar();
        }

        leftname.visible = false;
        rightname.visible = false;
        changeText(txt);
    }

    public void hideAll() {
        hideMainAvatar();
        hideSecondAvatar();
        hideThirdAvatar();
    }

    public void hide() {
        if (settedPlot!=null) {
            if(settedPlot.end()) {
                settedPlot = null;
                super.hide();
            }
        }
    }


    public static void storeInBundle(Bundle b) {

        if(settedPlot != null)
        {
            //FIXME that's weird because reach the last stop doesn't mean it's end,especially when some dialog-choice is needed(if wants to add things like this)
            Plot.storeInBundle(b,settedPlot);
        }

    }

    public static void restoreFromBundle(Bundle b) {
        settedPlot = Plot.restorFromBundle(b);
    }

    public void haveChoice(Choice... existing)
    {
        storedChoice = existing;
        haveChoice = true;
    }

    public void showChoice(Choice... existing) {

        resizeBeforeChoice();

        haveChoice = false;

        int width = PixelScene.uiCamera.width - 2 * MARGIN_X;

        float pos;
        ArrayList<ChoiceButton> buttons = new ArrayList<ChoiceButton>();

        boolean landscape = false;
        if(SPDSettings.landscape()!=null)
        {
            landscape = SPDSettings.landscape();//yeah it sounds stupid but without it when entering game first time
            // without minding this would cause crash.And mostly android device don't use landscape as default
        }

        if(landscape)
        {
            pos = GameScene.StatusHeight();
        }
        else
        {
            int half = existing.length / 2;
            float offset = (PixelScene.uiCamera.height - GameScene.ToolbarHeight() - GameScene.StatusHeight() ) / 2;
            pos = offset - half * BUTTON_HEIGHT  - (half - 1) * MARGIN - (existing.length % 2 == 0 ? MARGIN / 2f :
                    BUTTON_HEIGHT / 2f) ;
        }

        for (int i = 0; i < existing.length; i++) {
            {
                final int finalI = i;
                ChoiceButton btn = new ChoiceButton(existing[i].text , 6) {

                    @Override
                    protected void onClick() {
                        resizeAfterChoice();
                        existing[finalI].react();
                        for (ChoiceButton button : buttons) {
                            button.destroy();
                            button.killAndErase();
                        }
                    }

                    /*
                    @Override
                    public void update()
                    {
                        if(time < 1)
                        {
                            time += 30f * Game.elapsed;
                            if(time > 1) time = 1f;
                            alpha(time);
                        }
                    }*/
                };
                buttons.add(btn);
                btn.setRect( MARGIN_X , pos, width, BUTTON_HEIGHT);
                btn.layout();
                add(btn);
                pos += MARGIN + BUTTON_HEIGHT;
            }
        }
    }

    public void resizeBeforeChoice()//FIXME This is damn stupid but the whole chrome needs outside elements,and if don't change camera and resize and pretend other things in proper place,choice block would be invisible
    {
        removeArea();
        removeSkip();
    }

    public void resizeAfterChoice()
    {
        area = makeArea();
        if(this != null )add(area);

        skip = makeSkip();
        if(this != null )add(skip);
    }

    @Override
    public void update()
    {
        if(!readed) {
            if ((timeLeft -= Game.elapsed) <= 0) {
                timeLeft = 0.02f;
                if (times++ < script.length()) {
                    text.text(script.substring(0,times) , text.maxWidth());
                }
                else {
                    readed = true;
                    if (haveChoice) {
                        haveChoice = false;
                        showChoice(storedChoice);
                    }
                }
            }
        }
    }

    public void skipWait()
    {
        times = script.length();
        text.text(script.substring(0,times) , text.maxWidth());
        readed = true;
        if(haveChoice)
        {
            haveChoice = false;
            showChoice(storedChoice);
        }
    }

    public void skipText()
    {
        settedPlot.skip();
    }
}