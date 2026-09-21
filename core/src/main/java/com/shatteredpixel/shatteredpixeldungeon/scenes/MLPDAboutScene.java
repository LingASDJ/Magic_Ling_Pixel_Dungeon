/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.shatteredpixeldungeon.scenes;

import static com.watabou.noosa.Game.switchScene;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;

public class MLPDAboutScene extends PixelScene {
    private float rainbowHue = 0f;
    private CreditsBlock qingXianBlock;

    private Image About_Icons(String id){
        int left;
        int top;
        switch (id){
            default:
            case "LING":
                left = 0;
                top = 0;
            break;
            case "TAT":
                left = 16;
                top = 0;
            break;
            case "MIS":
                left = 32;
                top = 0;
            break;
            case "DOG":
                left = 48;
                top = 0;
                break;
            case "POR":
                left = 64;
                top = 0;
                break;
            case "ARE":
                left = 0;
                top = 16;
                break;
            case "WTR":
                left = 16;
                top = 16;
                break;
            case "BZMDR":
                left = 32;
                top = 16;
                break;
            case "KUZUMI":
                left = 48;
                top = 16;
                break;
            case "CHOCO":
                left = 64;
                top = 16;
                break;
            case "LUOBAI":
                left = 80;
                top = 16;
                break;
            case "XYE":
                left = 0;
                top = 32;
                break;
            case "FLASH":
                left = 16;
                top = 32;
                break;
            case "Daniel_Calan":
                left = 80;
                top = 32;
                break;
            case "JISHEN":
                left = 64;
                top = 32;
                break;
            case "zxcPandora":
                left = 0;
                top = 48;
                break;
            case "Noah_7385":
                left = 48;
                top = 32;
                break;
            case "SHENHAI":
                left = 80;
                top = 0;
                break;
            case "TSWQ":
                left = 32;
                top = 32;
                break;
            case "TELLER":
                left = 16;
                top = 48;
                break;
        }
        return new Image(Assets.Interfaces.ABOUT_ICONS,left,top,16,16);
    }

    @Override
    public void create() {
        super.create();

        final float colWidth = 120;
        final float fullWidth = colWidth * (landscape() ? 2 : 1);

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize( w, h );
        add( archs );

        //darkens the arches
        add(new ColorBlock(w, h, 0x88000000));

        ScrollPane list = new ScrollPane( new Component() );
        add( list );

        Component content = list.content();
        content.clear();

        //*** Magic Ling Pixel Dungeon 核心制作团队 ***
        CreditsBlock shpx = new CreditsBlock(true, 0x00C7CB,
                "Magic Ling Pixel Dungeon",
                Icons.SHPX.get(),
                "魔绫像素地牢",
                "mlpd.spldream.com",
                "https://mlpd.spldream.com/");
        shpx.setRect((w - fullWidth)/2f, 6, fullWidth, 0);
        content.add(shpx);
        addLine(48, content);

        //核心策划设计
        CreditsBlock mainPlan = new CreditsBlock(false, Window.TITLE_COLOR,
                "主策划",
                About_Icons("LING"),
                "JDSALing",
                null,
                null);
        mainPlan.setSize(colWidth/2f, 0);
        mainPlan.setPos(w/2f - colWidth/2f, shpx.bottom()+10);
        content.add(mainPlan);

        CreditsBlock mainDesign = new CreditsBlock(false, 0x9B7951,
                "主 设 计 ",
                About_Icons("JISHEN"),
                "寄神",
                null,
                null);
        mainDesign.setRect(mainPlan.right(), mainPlan.top(), colWidth/2f, 0);
        content.add(mainDesign);

        CreditsBlock mainDesign2 = new CreditsBlock(false, Window.CBLACK,
                "",
                About_Icons("BZMDR"),
                "BZMDR",
                null,
                null);
        mainDesign2.setRect(mainPlan.right()+40, mainPlan.top()+5, colWidth/2f, 0);
        content.add(mainDesign2);

        addLine(75, content);

        //*** 美术设计团队 ***
        CreditsBlock art1 = new CreditsBlock(true, 0xcf3227,
                "美  术  设  计 ",
                About_Icons("Daniel_Calan"),
                "Daniel Calan",
                null,
                null);
        art1.setSize(colWidth/2f, 0);
        art1.setPos(mainPlan.left(), mainDesign.bottom()+10);
        content.add(art1);

        CreditsBlock art2 = new CreditsBlock(true,0xffd2d2,
                "美  术  设  计",
                About_Icons("LUOBAI"),
                "落白",
                null,
                null);
        art2.setRect(art1.right(), art1.top(), colWidth/2f, 0);
        content.add(art2);

        addLine(art2.top()+31, content);

        //*** 程序开发团队 ***
        CreditsBlock code1 = new CreditsBlock(true, 0x2B8E68,
                " ",
                About_Icons("zxcPandora"),
                "zxcPandora",
                null,
                null);
        code1.setSize(colWidth/3f, 0);
        code1.setPos(art1.left(), art2.bottom()+20);
        content.add(code1);

        CreditsBlock code2 = new CreditsBlock(true,0xB71575,
                "程  序  编  码",
                About_Icons("LING"),
                "JDSALing",
                null,
                null);
        code2.setRect(code1.right(), code1.top(), colWidth/3f, 0);
        content.add(code2);

        CreditsBlock code3 = new CreditsBlock(true,0x123546,
                "",
                About_Icons("FLASH"),
                "手电",
                null,
                null);
        code3.setRect(code2.right(), code2.top(), colWidth/3f, 0);
        content.add(code3);


        CreditsBlock code4 = new CreditsBlock(true,0x25273e, "", About_Icons("WTR"), "箐筅", null, null);
        code4.setRect(code1.x, code1.bottom()+10, colWidth/3f, 0);
        content.add(code4);
        qingXianBlock = code4;
        addLine(code4.top()+28, content);

        CreditsBlock code5 = new CreditsBlock(true,0xFFFFFF,
                "",
                About_Icons("ARE"),
                "Archetto",
                null,
                null);
        code5.setRect(code4.right(), code1.bottom()+10, colWidth/3f, 0);
        content.add(code5);


        //*** 测试协力团队 ***
        CreditsBlock test1 = new CreditsBlock(true,0xF25CAC,
                "中  测  协  力",
                About_Icons("ARE"),
                "Archetto",
                null,
                null);
        test1.setRect(code2.x, code4.bottom()+10, colWidth/3f, 0);
        content.add(test1);

        CreditsBlock test2 = new CreditsBlock(true, 0x5C5C94,
                "",
                About_Icons("TSWQ"),
                "太上忘情",
                null,
                null);
        test2.setSize(colWidth/3f, 0);
        test2.setPos(code4.x, code4.bottom()+14);
        content.add(test2);

        CreditsBlock test3 = new CreditsBlock(true,0x8E1B44,
                "",
                About_Icons("SHENHAI"),
                "深海",
                null,
                null);
        test3.setRect(code3.x, code4.bottom()+14, colWidth/3f, 0);
        content.add(test3);

        CreditsBlock test4 = new CreditsBlock(true,0xc79654,
                "",
                About_Icons("CHOCO"),
                "Chocosuki",
                null,
                null);
        test4.setRect(code2.x, test2.bottom()+10, colWidth/3f, 0);
        content.add(test4);
        addLine(test4.bottom()+10, content);

        //*** 新增：皮肤设计（三人） ***
        CreditsBlock skin1 = new CreditsBlock(true, 0xCDF9FF,
                " ",
                About_Icons("Noah_7385"),
                "Noah-7385",
                null,
                null);
        skin1.setSize(colWidth/2f, 0);
        skin1.setPos(code2.x-30, test4.bottom()+20);
        content.add(skin1);

        CreditsBlock skin2 = new CreditsBlock(true, 0xEF0101,
                "皮 肤 设 计",
                null,
                " ",
                null,
                null);
        skin2.setRect(code2.x, test4.bottom()+15, colWidth/3f, 0);
        content.add(skin2);

        CreditsBlock skin3 = new CreditsBlock(true, 0xf898b8,
                "",
                About_Icons("Daniel_Calan"),
                "Daniel Calan",
                null,
                null);
        skin3.setRect(code2.x+20, test4.bottom()+20, colWidth/3f, 0);
        content.add(skin3);
        addLine(skin1.top()+25, content);

        CreditsBlock balance1 = new CreditsBlock(true, 0xFF19FF,
                " ",
                About_Icons("BZMDR"),
                "BZMDR",
                null,
                null);
       balance1.setSize(colWidth/2f, 0);
       balance1.setPos(code2.x-30, skin2.bottom()+40);
        content.add(balance1);

        CreditsBlock balance2 = new CreditsBlock(true, 0xf898b8,
                "平 衡 研 判 ",
                null,
                " ",
                null,
                null);
        balance2.setRect(code2.x, skin2.bottom()+35, colWidth/3f, 0);
        content.add(balance2);

        CreditsBlock balance3 = new CreditsBlock(true, 0xF1A0B8,
                "",
                About_Icons("XYE"),
                "小叶",
                null,
                null);
        balance3.setRect(balance2.x+20, skin2.bottom()+40, colWidth/3f, 0);
        content.add(balance3);
        addLine(balance3.top()+28, content);

        //*** 新增：文案设计（三人） ***
        CreditsBlock text1 = new CreditsBlock(true, 0XBFA041,
                "",
                About_Icons("KUZUMI"),
                "久住",
                null,
                null);
        text1.setSize(colWidth/3f, 0);
        text1.setPos(test2.x, balance1.bottom()+10);
        content.add(text1);

        CreditsBlock text2 = new CreditsBlock(true, 0X4557FF,
                "文 案 设 计",
                About_Icons("ARE"),
                "Archetto",
                null,
                null);
        text2.setRect(text1.right(), text1.top(), colWidth/3f, 0);
        content.add(text2);

        CreditsBlock text3 = new CreditsBlock(true, 0x9B7951,
                "",
                About_Icons("JISHEN"),
                "寄神",
                null,
                null);
        text3.setRect(text2.right(), text2.top(), colWidth/3f, 0);
        content.add(text3);
        addLine(text1.top()+32, content);

        //*** 运营宣发（四人） ***
        CreditsBlock openration1 = new CreditsBlock(true, 0x4ecdc4,
                "运 营 宣 发" +
                        "\n",
                Icons.BACKPACK.get(),
                "罗贝里",
                null,
                null);
        openration1.setRect(code2.x, text1.bottom()+10, colWidth/3f, 0);
        content.add(openration1);

        CreditsBlock openration2 = new CreditsBlock(true, 0xa884ec,
                "",
                About_Icons("JISHEN"),
                "那些回忆",
                null,
                null);
        openration2.setSize(colWidth/3f, 0);
        openration2.setPos(code4.x, text1.bottom()+14);
        content.add(openration2);

        CreditsBlock openration3 = new CreditsBlock(true, 0xFFD700,
                "",
                About_Icons("TELLER"),
                "泰勒",
                null,
                null);
        openration3.setRect(code3.x, text1.bottom()+14, colWidth/3f, 0);
        content.add(openration3);

        CreditsBlock openration4 = new CreditsBlock(true, 0xa884ec,
                "",
                About_Icons("JISHEN"),
                "omicronrg9",
                null,
                null);
        openration4.setRect(code2.x, openration2.bottom()+10, colWidth/3f, 0);
        content.add(openration4);
        addLine(openration4.bottom()+10, content);


        //*** 音乐设计（四人） ***
        CreditsBlock musicDesign1 = new CreditsBlock(true, 0x5F5653,
                "",
                About_Icons("TAT"),
                "Tatsro",
                null,
                null);
        musicDesign1.setSize(colWidth/3f, 0);
        musicDesign1.setPos(code4.x, openration4.bottom()+15);
        content.add(musicDesign1);
        CreditsBlock musicDesign2 = new CreditsBlock(true, Window.CBLACK,
                "音 乐 设 计 ",
                About_Icons("POR"),
                "Prohonor",
                null,
                null);
        musicDesign2.setRect(musicDesign1.right(), musicDesign1.top(), colWidth/3f, 0);
        content.add(musicDesign2);

        CreditsBlock musicDesign3 = new CreditsBlock(true, Window.WHITE,
                " ",
                About_Icons("DOG"),
                "犬罗",
                null,
                null);
        musicDesign3.setRect(musicDesign2.right(), musicDesign2.top(), colWidth/3f, 0);
        content.add(musicDesign3);

        CreditsBlock musicDesign4 = new CreditsBlock(true,0xA7A7A7,
                "",
                About_Icons("MIS"),
                "Misogi",
                null,
                null);
        musicDesign4.setRect(musicDesign1.x, musicDesign1.bottom()+10, colWidth/3f, 0);
        content.add(musicDesign4);

        content.setSize( fullWidth, musicDesign4.bottom()+10 );

        list.setRect( 0, 0, w, h );
        list.scrollTo(0, 0);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        switchScene(TitleScene.class);
    }

    private void addLine( float y, Group content ){
        ColorBlock line = new ColorBlock(Camera.main.width, 1, 0xFF333333);
        line.y = y;
        content.add(line);
    }

    private static class CreditsBlock extends Component {

        boolean large;
        RenderedTextBlock title;
        Image avatar;
        public Flare flare;
        RenderedTextBlock body;

        RenderedTextBlock link;
        ColorBlock linkUnderline;
        PointerArea linkButton;

        //many elements can be null, but body is assumed to have content.
        private CreditsBlock(boolean large, int highlight, String title, Image avatar, String body, String linkText, String linkUrl){
            super();

            this.large = large;

            if (title != null) {
                this.title = PixelScene.renderTextBlock(title,6);
                if (highlight != -1) this.title.hardlight(highlight);
                add(this.title);
            }

            if (avatar != null){
                this.avatar = avatar;
                add(this.avatar);
            }

            if (large && highlight != -1 && this.avatar != null){
                this.flare = new Flare( 7, 24 ).color( highlight, true ).show(this.avatar, 0);
                this.flare.angularSpeed = 20;
            }

            this.body = PixelScene.renderTextBlock(body, 6);
            if (highlight != -1) this.body.setHightlighting(true, highlight);
            if (large) this.body.align(RenderedTextBlock.CENTER_ALIGN);
            add(this.body);

            if (linkText != null && linkUrl != null){
                int color = 0xFFFFFFFF;
                if (highlight != -1) color = 0xFF000000 | highlight;
                this.linkUnderline = new ColorBlock(1, 1, color);
                add(this.linkUnderline);

                this.link = PixelScene.renderTextBlock(linkText, 6);
                if (highlight != -1) this.link.hardlight(highlight);
                add(this.link);

                linkButton = new PointerArea(0, 0, 0, 0){
                    @Override
                    protected void onClick( PointerEvent event ) {
                        ShatteredPixelDungeon.platform.openURI( linkUrl );
                    }
                };
                add(linkButton);
            }
        }

        @Override
        protected void layout() {
            super.layout();

            float topY = top();

            if (title != null){
                title.maxWidth((int)width());
                title.setPos( x + (width() - title.width())/2f, topY);
                topY += title.height() + (large ? 2 : 1);
            }

            if (large){

                if (avatar != null){
                    avatar.x = x + (width()-avatar.width())/2f;
                    avatar.y = topY;
                    PixelScene.align(avatar);
                    if (flare != null){
                        flare.point(avatar.center());
                    }
                    topY = avatar.y + avatar.height() + 2;
                }

                body.maxWidth((int)width());
                body.setPos( x + (width() - body.width())/2f, topY);
                topY += body.height() + 2;
            } else {
                if (avatar != null){
                    avatar.x = x;
                    body.maxWidth((int)(width() - avatar.width - 1));

                    float fullAvHeight = Math.max(avatar.height(), 16);
                    if (fullAvHeight > body.height()){
                        avatar.y = topY + (fullAvHeight - avatar.height())/2f;
                        PixelScene.align(avatar);
                        body.setPos( avatar.x + avatar.width() + 1, topY + (fullAvHeight - body.height())/2f);
                        topY += fullAvHeight + 1;
                    } else {
                        avatar.y = topY + (body.height() - fullAvHeight)/2f;
                        PixelScene.align(avatar);
                        body.setPos( avatar.x + avatar.width() + 1, topY);
                        topY += body.height() + 2;
                    }
                } else {
                    topY += 1;
                    body.maxWidth((int)width());
                    body.setPos( x, topY);
                    topY += body.height()+2;
                }
            }

            if (link != null){
                if (large) topY += 1;
                link.maxWidth((int)width());
                link.setPos( x + (width() - link.width())/2f, topY);
                topY += link.height() + 2;

                linkButton.x = link.left()-1;
                linkButton.y = link.top()-1;
                linkButton.width = link.width()+2;
                linkButton.height = link.height()+2;

                linkUnderline.size(link.width(), PixelScene.align(0.49f));
                linkUnderline.x = link.left();
                linkUnderline.y = link.bottom()+1;
            }

            topY -= 2;
            height = Math.max(height, topY - top());
        }
    }

    @Override
    public void update() {
        super.update();
        if (qingXianBlock != null && qingXianBlock.flare != null) {
            rainbowHue += Game.elapsed * 0.5f;
            rainbowHue %= 1f;
            int color = hsvToRgb(rainbowHue, 1f, 1f);
            qingXianBlock.flare.color(color, true);
        }
    }

    /**
     * HSV转接器
     */
    private int hsvToRgb(float h, float s, float v) {
        float r=0,g=0,b=0;
        int i = (int)(h*6);
        float f = h*6 - i;
        float p = v*(1-s);
        float q = v*(1-f*s);
        float t = v*(1-(1-f)*s);
        switch(i%6){
            case 0: r=v;g=t;b=p; break;
            case 1: r=q;g=v;b=p; break;
            case 2: r=p;g=v;b=t; break;
            case 3: r=p;g=q;b=v; break;
            case 4: r=t;g=p;b=v; break;
            case 5: r=v;g=p;b=q; break;
        }
        return 0xE67069 | ((int)(r*255)<<16) | ((int)(g*255)<<8) | (int)(b*255);
    }

}