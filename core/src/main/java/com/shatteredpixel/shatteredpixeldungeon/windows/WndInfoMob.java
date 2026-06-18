package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.SPDSettings.ATBSettings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GreenDiamndMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NoneSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

public class WndInfoMob extends Window {

//    直接继承Window了
//    前面的基本是原本WndTitledMessage的功能
//


    protected static int maxHeight() {
        return (int) (PixelScene.uiCamera.height * 0.9);
    }


    protected static final int WIDTH_MIN = 120;
    protected static final int WIDTH_MAX = 220;
    protected static final int GAP = 2;

    private ScrollPane sp;

    public WndInfoMob(Mob mob) {
        super();

        String message = mob.info();
        MobTitle titlebar = new MobTitle(mob, this);

        int width = WIDTH_MIN;

        titlebar.setRect(0, 0, width, 0);
        add(titlebar);

        RenderedTextBlock text = PixelScene.renderTextBlock(6);
        text.text(message, width);
        text.setPos(titlebar.left(), titlebar.bottom() + 2 * GAP);

        while (PixelScene.landscape()
                && text.bottom() > (PixelScene.MIN_HEIGHT_L - 10)
                && width < maxHeight()) {
            width += 20;
            titlebar.setRect(0, 0, width, 0);
            text.setPos( titlebar.left(), titlebar.bottom() + 2*GAP );
            text.maxWidth(width);

            titlebar.setWidth(width);
            text.setPos(titlebar.left(), titlebar.bottom() + 2 * GAP);
        }
        Component comp = new Component();
        comp.add(text);
        text.setPos(0, GAP);
        comp.setSize(text.width(), text.height() + GAP * 2);
        resize(width, (int) Math.min((int) comp.bottom() + 2 + titlebar.height() + GAP, maxHeight()));

        add(sp = new ScrollPane(comp));
        sp.setRect(titlebar.left(), titlebar.bottom() + GAP, comp.width(), Math.min((int) comp.bottom() + 2, maxHeight() - titlebar.bottom() - GAP));

        bringToFront(titlebar);
    }

    @Override
    public void offset(int xOffset, int yOffset) {
        super.offset(xOffset, yOffset);
        // refresh the scrollbar pane
        sp.setPos(sp.left(), sp.top());
    }
    // adds to the bottom of a titled message, below the message itself.
    // this only works ONCE currently.
    public final void addToBottom(Component c) {
        addToBottom(c, GAP);
    }

    public final void addToBottom(Component c, int gap) {
        addToBottom(c, gap, 0);
    }

    public void addToBottom(Component c, int gapBefore, int gapAfter) {
        // attempt to place normally.
        c.setRect(0, height + gapBefore, width, c.height() + gapAfter); // assumes there is space at the bottom first. note that I'm baking in the bottom spacing into the component itself
        add(c);
        // in order to make things fit, we need to change the size of the scrollbar to make the component fit properly.
        // fixme even though it *feels* like I should be able to stop here if everything fits, I can't. why?
        setHeight((int) Math.min(c.bottom(), maxHeight()));
        float y; c.setY(y = height - c.height()); // put the component into its final position.
        // scrollbar height is reduced to respect top spacing
        sp.setRect(0, sp.top(), width, y - sp.top() - gapBefore);
    }

    // fixme this wrapper component is very unable to moved after it's placed. If I need to move it again, I won't be able to.
    // should be able to simulate the previous behavior...
    public Component addToBottom(int gapBefore, int gapAfter, Component... components) {
        // this ensures that things are formatted correctly vertically.
        Component wrapper = new Component();
        if(components.length == 0) return wrapper;
        float top=Float.MAX_VALUE, bottom=Float.MIN_VALUE;
        for(Component c : components) {
            top = Math.min(top, c.top());
            bottom = Math.max(bottom, c.bottom());
            wrapper.add(c);
        }
        wrapper.setRect(0, top, width, bottom-top);
        addToBottom(wrapper, gapBefore, gapAfter);

        top -= wrapper.top();

        for(Component c : components) c.setY(c.top() - top);

        return wrapper;
    }
    // yes the order is different. deal with it.
    public Component addToBottom(int gap, Component... components) { return addToBottom(gap, 0, components); }
    public Component addToBottom(Component... components) { return addToBottom(GAP, components); }



//
//下面开始是原来的WndInfoMob部分
//


    public static boolean reload = false;

    public static class MobTitle extends Component {

        private static final int GAP	= 2;

        private final CharSprite image;
        private final RenderedTextBlock name;
        private final HealthBar health;
        private final BuffIndicator buffs;

        public WndInfoMob.MobSixInfo mobSixInfo;

        private String DKLevel(Mob mob) {
            String level;
            if(mob.defenseSkill > 23){
                level = "S";
            } else if (mob.defenseSkill > 20) {
                level = "A+";
            } else if (mob.defenseSkill > 15) {
                level = "A";
            } else if (mob.defenseSkill > 10) {
                level = "B+";
            } else if (mob.defenseSkill > 5) {
                level = "B";
            } else if (mob.defenseSkill > 2) {
                level = "C";
            } else {
                level = "D";
            }
            return level;
        }

        private String SPLevel(Mob mob) {
            String level;
            if(mob.speed() == 1){
                level = "C";
            } else if (mob.speed() >= 2.5) {
                level = "S+";
            } else if (mob.speed() >= 2) {
                level = "S";
            } else if (mob.speed() >= 1.5) {
                level = "A+";
            } else if (mob.speed() > 1.0) {
                level = "A";
            } else if (mob.speed() < 0.8) {
                level = "D+";
            } else if (mob.speed() < 0.5) {
                level = "D";
            } else {
                level = "D-";
            }
            return level;
        }


        private String AttackDelayLevel(Mob mob) {
            String level;
            if(mob.attackDelay() == 1){
                level = "C";
            } else if (mob.attackDelay() <= 0.8) {
                level = "B+";
            } else if (mob.attackDelay() <= 0.6) {
                level = "B";
            } else if (mob.attackDelay() <= 0.5) {
                level = "A";
            } else if (mob.attackDelay() <= 0.4) {
                level = "A+";
            } else if (mob.attackDelay() <= 0.3) {
                level = "S";
            } else {
                level = "D";
            }
            return level;
        }

        private String HPLevel(Mob mob) {
            String level;
            if(mob.HP>2000) {
                level = "SSS";
            } else if(mob.HP>1000){
                level = "SS";
            } else if (mob.HP>600){
                level = "S+";
            } else if (mob.HP>500){
                level = "S";
            } else if (mob.HP>400){
                level = "S-";
            } else if (mob.HP>100) {
                level = "A+";
            } else if (mob.HP>50) {
                level = "A-";
            } else if (mob.HP>40){
                level = "B";
            } else if (mob.HP>30){
                level = "C";
            } else if (mob.HP>20) {
                level = "D";
            } else if (mob.HP>2) {
                level = "E";
            } else if (mob.HP==1) {
                level = "F";
            } else if(mob.HP<=0) {
                level = "X";
            } else {
                level = "Z";
            }
            return level;
        }

        public static String ProName(Mob mob) {
            String level;
            if (mob.properties.contains(Char.Property.UNLESS)) {
                level = Messages.get(WndInfoMob.class, "book");
            } else if (mob.properties.contains(Char.Property.GODCRACK)) {
                level = Messages.get(WndInfoMob.class, "godcrack");
            } else if (mob.properties.contains(Char.Property.BOSS)){
                level = Messages.get(WndInfoMob.class,"boss");
            } else if (mob.properties.contains(Char.Property.SEARCH)){
                level = Messages.get(WndInfoMob.class,"seach");
            } else if (mob.properties.contains(Char.Property.UNKNOWN)){
                level = Messages.get(WndInfoMob.class,"unknown");
            } else if (mob.properties.contains(Char.Property.MINIBOSS)){
                level = Messages.get(WndInfoMob.class,"miniboss");
            } else if (mob.properties.contains(Char.Property.HOLLOW)){
                level = Messages.get(WndInfoMob.class,"hollow");
            } else if (mob.properties.contains(Char.Property.HUNTER)){
                level = Messages.get(WndInfoMob.class,"hunter");
            } else if (mob.properties.contains(Char.Property.ABYSS)){
                level = Messages.get(WndInfoMob.class,"abyss");
            } else if (mob.properties.contains(Char.Property.UNDEAD)){
                level = Messages.get(WndInfoMob.class,"undied");
            } else if (mob.properties.contains(Char.Property.DEMONIC)){
                level = Messages.get(WndInfoMob.class,"demon");
            } else if (mob.properties.contains(Char.Property.NPC)){
                level = "NPC";
            } else if (mob.properties.contains(Char.Property.PETS)){
                level = Messages.get(WndInfoMob.class,"pets");
            } else if (mob.properties.contains(Char.Property.FIERY) || mob.properties.contains(Char.Property.ICY) || mob.properties.contains(Char.Property.ELECTRIC)) {
                level = Messages.get(WndInfoMob.class, "ling");
            } else if (mob.properties.contains(Char.Property.TUMULUS)) {
                level = Messages.get(WndInfoMob.class, "tumulus");
            } else {
                level = Messages.get(WndInfoMob.class,"normal");
            }
            return level;
        }

        private String MaxLevelName(Mob mob) {
            String level;

            if(hero.lvl <= mob.maxLvl || mob.properties.contains(Char.Property.BOSS) || mob.properties.contains(Char.Property.MINIBOSS)){
                level = Messages.get(WndInfoMob.class,"canroll");
            } else {
                level = Messages.get(WndInfoMob.class,"noroll");
            }
            return level;
        }


        private Window parentWindow;
        public MobTitle(Mob mob,Window parentwindow) {

            this.parentWindow = parentwindow;

            name = PixelScene.renderTextBlock(Messages.titleCase( mob.name() ), 9 );
            name.hardlight( TITLE_COLOR );
            add( name );


            image = mob.buff(ChampionEnemy.NoCode.class) != null ? new NoneSprite() : mob.sprite();
            add( image );

            health = new HealthBar();
            health.level(mob);
            add( health );

            mobSixInfo = new WndInfoMob.MobSixInfo(mob);
            add(mobSixInfo);

            mobSixInfo.info1 = PixelScene.renderTextBlock((ATBSettings() ? String.valueOf(mob.HP) : HPLevel(mob)),6);

            mobSixInfo.info2 = PixelScene.renderTextBlock(ATBSettings() ?
                    String.valueOf((double)Math.round(mob.attackDelay() * 10) /10) : AttackDelayLevel(mob),6);

            mobSixInfo.info3 = PixelScene.renderTextBlock( ATBSettings() ? String.valueOf(mob.maxLvl) : MaxLevelName(mob),5);
            mobSixInfo.info4 = PixelScene.renderTextBlock(String.valueOf(mob.drRoll()),6);

            mobSixInfo.info5 = PixelScene.renderTextBlock(ATBSettings() ? String.valueOf(mob.defenseSkill) : DKLevel(mob),6);
            mobSixInfo.info6 = PixelScene.renderTextBlock(ATBSettings() ?
                    String.valueOf((double)Math.round(mob.speed()*100)/100): SPLevel(mob),6);

            mobSixInfo.info7 = PixelScene.renderTextBlock(ProName(mob),6);

            int dmg = 0;
            int tries = 1000;
            for (int i = 0; i < tries; i++) {
                dmg += mob.damageRoll();
            }
            mobSixInfo.info8 = PixelScene.renderTextBlock(String.valueOf(dmg/tries),6);

            add(mobSixInfo.info1);
            add(mobSixInfo.info2);
            add(mobSixInfo.info3);
            add(mobSixInfo.info4);
            add(mobSixInfo.info5);
            add(mobSixInfo.info6);
            add(mobSixInfo.info7);
            add(mobSixInfo.info8);

            buffs = new BuffIndicator( mob,true ,true,false);
            addToFront( buffs );

            reload = (mob.alignment == Char.Alignment.NEUTRAL) && (mob instanceof GreenDiamndMimic || mob.properties.contains(Char.Property.HOLLOW));
        }

        @Override
        protected void layout() {

            image.x = 0;
            image.y = Math.max( 0, name.height() + health.height() - image.height() );

            float w = width - image.width() - GAP;

            mobSixInfo.WIDTH = width;

            name.maxWidth((int)w);
            name.setPos(x + image.width + GAP,
                    image.height() > name.height() ? y +(image.height() - name.height()) / 2 : y);

            health.setRect(image.width() + GAP, name.bottom() + GAP, w, health.height());


            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if(mob.buff(ChampionEnemy.NoCode.class) != null) {
                    mobSixInfo.info1.visible = false;	mobSixInfo.info2.visible = false;
                    mobSixInfo.info3.visible = false;	mobSixInfo.info4.visible = false;
                    mobSixInfo.info5.visible = false;	mobSixInfo.info6.visible = false;
                    mobSixInfo.info7.visible = false;	mobSixInfo.info8.visible = false;
                    mobSixInfo.image1.visible = false;	mobSixInfo.image2.visible = false;
                    mobSixInfo.image3.visible = false;	mobSixInfo.image4.visible = false;
                    mobSixInfo.image5.visible = false;	mobSixInfo.image6.visible = false;
                    mobSixInfo.image7.visible = false;	mobSixInfo.image8.visible = false;
                    health.visible = false;
                    height = health.bottom();
                    return;
                }
            }

            if(reload){
                height = health.bottom();
                mobSixInfo.info1.visible = false;	mobSixInfo.info2.visible = false;
                mobSixInfo.info3.visible = false;	mobSixInfo.info4.visible = false;
                mobSixInfo.info5.visible = false;	mobSixInfo.info6.visible = false;
                mobSixInfo.info7.visible = false;	mobSixInfo.info8.visible = false;
                mobSixInfo.image1.visible = false;	mobSixInfo.image2.visible = false;
                mobSixInfo.image3.visible = false;	mobSixInfo.image4.visible = false;
                mobSixInfo.image5.visible = false;	mobSixInfo.image6.visible = false;
                mobSixInfo.image7.visible = false;	mobSixInfo.image8.visible = false;
                health.visible = false;
            } else {
                mobSixInfo.setPos(-5,Math.max(health.bottom(),image.height()+5));
                mobSixInfo.layout();
                height = mobSixInfo.bottom();
                mobSixInfo.info1.visible = true;	mobSixInfo.info2.visible = true;
                mobSixInfo.info3.visible = true;	mobSixInfo.info4.visible = true;
                mobSixInfo.info5.visible = true;	mobSixInfo.info6.visible = true;
                mobSixInfo.info7.visible = true;	mobSixInfo.info8.visible = true;
                mobSixInfo.image1.visible = true;	mobSixInfo.image2.visible = true;
                mobSixInfo.image3.visible = true;	mobSixInfo.image4.visible = true;
                mobSixInfo.image5.visible = true;	mobSixInfo.image6.visible = true;
                mobSixInfo.image7.visible = true;	mobSixInfo.image8.visible = true;
                health.visible = true;
            }

            buffs.resizeWidth = (int) width();
            buffs.setPos(
                    GAP,
                    height + GAP
            );

            height += buffs.height();
            //GLog.i(buffs.height() + "");
        }

    }

    public static class MobSixInfo extends Component {

        public Mob mob;

        public ColorBlock colorBlock;

        public Image image1;
        public Image image2;
        public Image image3;
        public Image image4;
        public Image image5;
        public Image image6;
        public Image image7;
        public Image image8;

        public RenderedTextBlock info1;
        public RenderedTextBlock info2;
        public RenderedTextBlock info3;
        public RenderedTextBlock info4;
        public RenderedTextBlock info5;
        public RenderedTextBlock info6;
        public RenderedTextBlock info7;
        public RenderedTextBlock info8;

        public float WIDTH = 120f;
        public float GAP = 1f;

        public MobSixInfo(Mob mob) {
            this.mob = mob;
        }

        @Override
        public void createChildren() {
            super.createChildren();

            colorBlock = new ColorBlock(1,1,  0xFF555555);
            add(colorBlock);

            image1 = new BuffIcon(126, false);
            image2 = new BuffIcon(127, false);
            image3 = new BuffIcon(128, false);
            image4 = new BuffIcon(129, false);
            image5 = new BuffIcon(130, false);
            image6 = new BuffIcon(131, false);
            image7 = new BuffIcon(132, false);
            image8 = new BuffIcon(133, false);
            add(image1);
            add(image2);
            add(image3);
            add(image4);
            add(image5);
            add(image6);
            add(image7);
            add(image8);
        }

        @Override
        public void layout() {
            colorBlock.x = 0;
            colorBlock.y = y;

            image1.x = image5.x = WIDTH * 0 / 4 + GAP;
            image2.x = image6.x = WIDTH * 1 / 4 + GAP;
            image3.x = image7.x = WIDTH * 2 / 4 + GAP;
            image4.x = image8.x = WIDTH * 3 / 4 + GAP;

            image1.y = image2.y = image3.y = image4.y = y + GAP;
            image5.y = image6.y = image7.y = image8.y = (image1.y + image1.height() + GAP * 2);

            info1.setPos((image2.x + image1.x + image1.width() - info1.width()) / 2,
                    image1.y + image1.height() / 2-info1.height()/2);
            info2.setPos((image3.x + image2.x + image2.width() - info2.width()) / 2,image1.y + image1.height() / 2-info1.height()/2);
            info3.setPos((image4.x + image3.x + image3.width() - info3.width()) / 2,image1.y + image1.height() / 2-info1.height()/2);
            info4.setPos((WIDTH + image4.x + image4.width() - info4.width()) / 2,
                    image1.y + image1.height() / 2-info1.height()/2);

            info5.setPos((image6.x + image5.x + image5.width() - info5.width()) / 2,image5.y + image5.height() / 2-info1.height()/2);
            info6.setPos((image7.x + image6.x + image6.width() - info6.width()) / 2,image5.y + image5.height() / 2-info1.height()/2);
            info7.setPos((image8.x + image7.x + image7.width() - info7.width()) / 2,image5.y + image5.height() / 2-info1.height()/2);
            info8.setPos((WIDTH + image8.x + image8.width() - info8.width()) / 2,image5.y + image5.height() / 2-info1.height()/2);

            colorBlock.size(WIDTH,2 * image8.height() + 4 * GAP);

            height = 2 * image8.height() + 4 * GAP;
        }
    }
}
