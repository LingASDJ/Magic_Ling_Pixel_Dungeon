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
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;

public class MLPDAboutScene extends PixelScene {

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
            case "XYE":
                left = 0;
                top = 32;
                break;
            case "FLASH":
                left = 16;
                top = 32;
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
        CreditsBlock shpx = new CreditsBlock(true, Window.Pink_COLOR,
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

        CreditsBlock mainDesign = new CreditsBlock(false, Window.GDX_COLOR,
                "主 设 计 ",
                Icons.BACKPACK.get(),
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
        mainDesign2.setRect(mainPlan.right()+30, mainPlan.top()+5, colWidth/2f, 0);
        content.add(mainDesign2);

        addLine(75, content);

        //*** 美术设计团队 ***
        CreditsBlock art1 = new CreditsBlock(true, 0xcf3227,
                " ",
                Icons.BACKPACK.get(),
                "Daniel Calan",
                null,
                null);
        art1.setSize(colWidth/3f, 0);
        art1.setPos(mainPlan.left(), mainDesign.bottom()+10);
        content.add(art1);

        CreditsBlock art2 = new CreditsBlock(true,0xffd2d2,
                "美  术  设  计",
                Icons.BACKPACK.get(),
                "落白",
                null,
                null);
        art2.setRect(art1.right(), art1.top(), colWidth/3f, 0);
        content.add(art2);

        CreditsBlock art3 = new CreditsBlock(true,0xc79654,
                "",
                About_Icons("CHOCO"),
                "Chocosuki",
                null,
                null);
        art3.setRect(art2.right(), art2.top(), colWidth/3f, 0);
        content.add(art3);
        addLine(art2.top()+31, content);

        //*** 程序开发团队 ***
        CreditsBlock code1 = new CreditsBlock(true, 0x008ac1,
                " ",
                Icons.BACKPACK.get(),
                "zxcPandora",
                null,
                null);
        code1.setSize(colWidth/3f, 0);
        code1.setPos(art1.left(), art2.bottom()+20);
        content.add(code1);

        CreditsBlock code2 = new CreditsBlock(true,0xffca18,
                "程  序  编  码",
                About_Icons("LING"),
                "JDSALing",
                null,
                null);
        code2.setRect(code1.right(), code1.top(), colWidth/3f, 0);
        content.add(code2);

        CreditsBlock code3 = new CreditsBlock(true,0x25273e,
                "",
                About_Icons("FLASH"),
                "手电",
                null,
                null);
        code3.setRect(code2.right(), code2.top(), colWidth/3f, 0);
        content.add(code3);

        CreditsBlock code4 = new CreditsBlock(true,0x25273e,
                "",
                About_Icons("WTR"),
                "箐筅",
                null,
                null);
        code4.setRect(code1.x, code1.bottom()+10, colWidth/3f, 0);
        content.add(code4);
        addLine(code4.top()+28, content);


        //*** 测试协力团队 ***
        CreditsBlock test1 = new CreditsBlock(true,0xF25CAC,
                "中  测  协  力",
                About_Icons("ARE"),
                "Archetto",
                null,
                null);
        test1.setRect(code2.x, code4.bottom()+10, colWidth/3f, 0);
        content.add(test1);

        CreditsBlock test2 = new CreditsBlock(true, 0x008ac1,
                "",
                Icons.BACKPACK.get(),
                "太上忘情",
                null,
                null);
        test2.setSize(colWidth/3f, 0);
        test2.setPos(code4.x, code4.bottom()+14);
        content.add(test2);

        CreditsBlock test3 = new CreditsBlock(true,0xffca18,
                "",
                Icons.BACKPACK.get(),
                "深海",
                null,
                null);
        test3.setRect(code3.x, code4.bottom()+14, colWidth/3f, 0);
        content.add(test3);
        addLine(test2.top()+28, content);

        //*** 新增：皮肤设计（三人） ***
        CreditsBlock skin1 = new CreditsBlock(true, 0xf898b8,
                " ",
                Icons.BACKPACK.get(),
                "Noah-7385",
                null,
                null);
        skin1.setSize(colWidth/2f, 0);
        skin1.setPos(code2.x-30, test2.bottom()+20);
        content.add(skin1);

        CreditsBlock skin2 = new CreditsBlock(true, 0xf898b8,
                "皮 肤 设 计",
                null,
                " ",
                null,
                null);
        skin2.setRect(code2.x, test2.bottom()+15, colWidth/3f, 0);
        content.add(skin2);

        CreditsBlock skin3 = new CreditsBlock(true, 0xf898b8,
                "",
                Icons.BACKPACK.get(),
                "Daniel Calan",
                null,
                null);
        skin3.setRect(code2.x+20, test2.bottom()+20, colWidth/3f, 0);
        content.add(skin3);
        addLine(skin1.top()+25, content);

        CreditsBlock balance1 = new CreditsBlock(true, 0x2CE8F5,
                " ",
                About_Icons("BZMDR"),
                "Bzmdr",
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

        CreditsBlock text3 = new CreditsBlock(true, 0xa884ec,
                "",
                Icons.BACKPACK.get(),
                "寄神",
                null,
                null);
        text3.setRect(text2.right(), text2.top(), colWidth/3f, 0);
        content.add(text3);
        addLine(text1.top()+32, content);

        //*** 新增：音乐设计 ***
        CreditsBlock musicDesign = new CreditsBlock(true, 0x4ecdc4,
                "运 营 宣 发",
                Icons.BACKPACK.get(),
                "QinYue",
                null,
                null);
        musicDesign.setRect((w - fullWidth)/2f, text1.bottom()+15, fullWidth, 0);
        content.add(musicDesign);
        addLine(musicDesign.top()+28, content);

        //*** 新增：运营宣发（三人） ***
        CreditsBlock opera1 = new CreditsBlock(true, 0x5F5653,
                "",
                About_Icons("TAT"),
                "Tatsro",
                null,
                null);
        opera1.setSize(colWidth/3f, 0);
        opera1.setPos(code4.x, musicDesign.bottom()+10);
        content.add(opera1);

        CreditsBlock opera2 = new CreditsBlock(true, Window.CBLACK,
                "音 乐 设 计 ",
                About_Icons("POR"),
                "Prohonor",
                null,
                null);
        opera2.setRect(opera1.right(), opera1.top(), colWidth/3f, 0);
        content.add(opera2);

        CreditsBlock opera3 = new CreditsBlock(true, Window.WHITE,
                " ",
                About_Icons("DOG"),
                "犬罗",
                null,
                null);
        opera3.setRect(opera2.right(), opera2.top(), colWidth/3f, 0);
        content.add(opera3);

        CreditsBlock opra4 = new CreditsBlock(true,0xA7A7A7,
                "",
                About_Icons("MIS"),
                "Misogi",
                null,
                null);
        opra4.setRect(opera1.x, opera1.bottom()+10, colWidth/3f, 0);
        content.add(opra4);

        content.setSize( fullWidth, opra4.bottom()+10 );

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
        Flare flare;
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
}