package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoMob.MobTitle.ProName;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Acidic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Albino;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredBrute;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bandit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BlackHost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BrownBat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BruteBot;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ClearElemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdGurad;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdMagicRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalGuardian;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalWisp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FetidRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Fire_Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FlameC01;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FlowerSlime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FungalSpinner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollSapper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollTrickster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GreatCrab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Guard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.IceGolem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ice_Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.KagenoNusujin;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Katydid;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MolotovHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Necromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewBlackHost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OGPDLLS;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OGPDNQHZ;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OGPDZSLS;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PhantomPiranha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RedMurderer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RedSwarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RotHeart;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RotLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SRPDHBLR;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SRPDICLR;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Salamander;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShieldHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SkullShaman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Slime_Orange;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Slime_Red;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpectralNecromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Spinner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Succubus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.TormentedSpirit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.XTG200;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.AnomaloCaris;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.ThreeLeafBug;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.Turtle;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruitsLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireDragon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.CrivusStarFruits;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.CrivusStarFruitsLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.DiedClearElemet;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.ApprenticeWitch;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Butcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Crumb;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Frankenstein;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.HollowMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.PumkingBomber;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.BlackSoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rlpt.DrTerror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rlpt.GunHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.DM275;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.GnollHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.GreenSlting;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.SlimeKingMob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.SuccubusQueen;
import com.shatteredpixel.shatteredpixeldungeon.custom.dict.DictSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.utils.PointF;
import com.watabou.utils.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WndBestiary extends Window {


    private int mobTier = 1;
    private int mobIndex = 0;
    private static final int WIDTH = 150;
    private static final int HEIGHT = 180;
    private static final int BTN_SIZE = 18;
    private static final int GAP = 2;

    protected int maxMobIndex(int tier){
        switch (tier){
            case 1: return DataPack.GREAT_CRAB.ordinal();
            case 2: default: return DataPack.CRXBOSS.ordinal() - DataPack.GREAT_CRAB.ordinal() - 1;
//            case 3: return WndBestiary.DataPack.DM201.ordinal() - DataPack.CRXBOSS.ordinal() - 1;
//
//            case 4: return WndBestiary.DataPack.ELE_CHAOS.ordinal() - WndBestiary.DataPack.DM201.ordinal() - 1;
//            case 5: return WndBestiary.DataPack.ACIDIC.ordinal() - WndBestiary.DataPack.ELE_CHAOS.ordinal() - 1;
//            case 6: return WndBestiary.DataPack.PIRANHA.ordinal() - WndBestiary.DataPack.ACIDIC.ordinal() - 1;
//            case 7: return WndBestiary.DataPack.NQHZ.ordinal() - WndBestiary.DataPack.PIRANHA.ordinal() - 1;
//            case 8: return WndBestiary.DataPack.IAS.ordinal() - WndBestiary.DataPack.NQHZ.ordinal() - 1;
//            case 9: return WndBestiary.DataPack.DiedP.ordinal() - WndBestiary.DataPack.DR.ordinal() - 1;
//            case 10: default: return WndBestiary.DataPack.FLXX.ordinal() - WndBestiary.DataPack.Crystal.ordinal() - 1;
        }
    }
    private int dataThreshold(int tier) {
        switch (tier) {
            case 1:
            default:
                return 0;
            case 2:
                return DataPack.GREAT_CRAB.ordinal() + 1;
            case 3:
                return DataPack.CRXBOSS.ordinal() + 1;
//            case 4:
//                return WndBestiary.DataPack.DM201.ordinal() + 1;
//            case 5:
//                return WndBestiary.DataPack.ELE_CHAOS.ordinal() + 1;
//            case 6:
//                return WndBestiary.DataPack.ACIDIC.ordinal() + 1;
//            case 7:
//                return WndBestiary.DataPack.PIRANHA.ordinal() + 1;
//            case 8:
//                return WndBestiary.DataPack.NQHZ.ordinal() + 1;
//            case 9:
//                return WndBestiary.DataPack.IAS.ordinal() + 1;
//            case 10:
//                return WndBestiary.DataPack.Crystal.ordinal() + 1;
        }
    }

    private RenderedTextBlock selectedPage;
    private ArrayList<IconButton> mobButtons = new ArrayList<>();
    private RenderedTextBlock selectedMob;
    private StyledButton damage;
    private StyledButton hp;
    private StyledButton dr;
    private StyledButton speed;
    private StyledButton eva;
    private StyledButton atkSpeed;
    private StyledButton typeMob;
    private StyledButton maxLevel;
    private StyledButton zapType;
    private StyledButton skillsType;
    private StyledButton IconType;

    public WndBestiary(){
        super();

        resize(WIDTH, HEIGHT);

        RedButton lhs = new RedButton(Messages.get(WndBestiary.class,"last_page"), 6){
            @Override
            public void onClick(){
                mobTier--;
                if(mobTier < 1 || mobTier>2){
                    mobTier = 2;
                }
                mobIndex = Math.min(mobIndex, maxMobIndex(mobTier));
                refreshImage();
                updateSelectedMob();
            }
        };
        lhs.setRect(GAP, GAP, 24, 18);
        add(lhs);

        IconButton hs = new IconButton(Icons.CLOSE.get()){
            @Override
            public void onClick(){
              hide();
            }
        };
        hs.setSize(20, 20);
        hs.setPos(135,0);
        add(hs);

        RedButton rhs = new RedButton(Messages.get(WndBestiary.class,"next_page"), 6){
            @Override
            public void onClick(){
                mobTier++;
                if(mobTier < 1 || mobTier >2){
                    mobTier = 1;
                }
                mobIndex = Math.min(mobIndex, maxMobIndex(mobTier));
                refreshImage();
                updateSelectedMob();
            }
        };
        rhs.setRect(WIDTH - 35 - GAP,  GAP, 24, 18);
        add(rhs);

        selectedPage = PixelScene.renderTextBlock("", 9);
        PixelScene.align(selectedPage);
        add(selectedPage);

        selectedMob = PixelScene.renderTextBlock("", 9);
        selectedMob.hardlight(0xFFFF44);
        PixelScene.align(selectedMob);
        selectedMob.visible = false;
        add(selectedMob);
        float pos = 76;

        StyledButton op = new StyledButton(Chrome.Type.GEM, "NULL") {
            @Override
            public void update() {
                text(M.L(allData.get(dataThreshold(mobTier) + mobIndex).mobClass, "name"));
            }
        };
        op.setRect(0, pos, WIDTH, 16);
        add(op);

        StyledButton nameX = new StyledButton(Chrome.Type.BLANK, Messages.get(WndBestiary.class, "maxhp"),8);
        nameX.setRect(0, pos+18, WIDTH/4f-GAP, 16);
        add(nameX);

        hp = new StyledButton(Chrome.Type.RED_BUTTON, "NULL");
        hp.setRect(0, pos+32, WIDTH/4f-GAP, 16);
        add(hp);

        StyledButton damageX = new StyledButton(Chrome.Type.BLANK, Messages.get(WndBestiary.class, "damage"),8);
        damageX.setRect(37.5f, pos+18, WIDTH/4f-GAP, 16);
        add(damageX);

        damage = new StyledButton(Chrome.Type.RED_BUTTON,"");
        damage.setRect(37.5f, pos+32, WIDTH/4f-GAP, 16);
        add(damage);

        StyledButton drX = new StyledButton(Chrome.Type.BLANK, Messages.get(WndBestiary.class, "dr"),8);
        drX.setRect(75, pos+18, WIDTH/4f-GAP, 16);
        add(drX);

        dr = new StyledButton(Chrome.Type.RED_BUTTON, "NULL");
        dr.setRect(75, pos+32, WIDTH/4f-GAP, 16);
        add(dr);

        StyledButton speedX = new StyledButton(Chrome.Type.BLANK, Messages.get(WndBestiary.class, "speed"),8);
        speedX.setRect(112.5f, pos+18, WIDTH/4f-GAP, 16);
        add(speedX);

        speed = new StyledButton(Chrome.Type.RED_BUTTON, "NULL");
        speed.setRect(112.5f, pos+32, WIDTH/4f-GAP, 16);
        add(speed);

        StyledButton evaX = new StyledButton(Chrome.Type.BLANK, Messages.get(WndBestiary.class, "eva"),8);
        evaX.setRect(0, pos+46, WIDTH/4f-GAP, 16);
        add(evaX);

        eva = new StyledButton(Chrome.Type.RED_BUTTON, "NULL");
        eva.setRect(0, pos+60, WIDTH/4f-GAP, 16);
        add(eva);

        StyledButton atkSpeedX = new StyledButton(Chrome.Type.BLANK, Messages.get(WndBestiary.class, "ats"),8);
        atkSpeedX.setRect(37.5f, pos+46, WIDTH/4f-GAP, 16);
        add(atkSpeedX);

        atkSpeed = new StyledButton(Chrome.Type.RED_BUTTON, "NULL");
        atkSpeed.setRect(37.5f, pos+60, WIDTH/4f-GAP, 16);
        add(atkSpeed);

        StyledButton mobTypeX = new StyledButton(Chrome.Type.BLANK, Messages.get(WndBestiary.class, "mobtype"),8);
        mobTypeX.setRect(75f, pos+46, WIDTH/4f-GAP, 16);
        add(mobTypeX);

        typeMob = new StyledButton(Chrome.Type.RED_BUTTON, "NULL");
        typeMob.setRect(75f, pos+60, WIDTH/4f-GAP, 16);
        add(typeMob);

        StyledButton maxLevelX = new StyledButton(Chrome.Type.BLANK, Messages.get(WndBestiary.class, "maxlevel"),8){
        };
        maxLevelX.setRect(112.5f, pos+46, WIDTH/4f-GAP, 16);
        add(maxLevelX);

        maxLevel = new StyledButton(Chrome.Type.RED_BUTTON, "NULL");
        maxLevel.setRect(112.5f, pos+60, WIDTH/4f-GAP, 16);
        add(maxLevel);

        StyledButton zapX = new StyledButton(Chrome.Type.BLANK, Messages.get(WndBestiary.class, "zapType"),8);
        zapX.setRect(0f, pos+75, 60, 16);
        add(zapX);

        zapType = new StyledButton(Chrome.Type.GEM, "Null",6);
        zapType.setRect(0, pos+88, 60, 16);
        add(zapType);

        IconType = new StyledButton( Chrome.Type.BLANK,"" ){
            @Override
            public void onClick(){
                Mob mob  = Reflection.newInstance(allData.get(dataThreshold(mobTier)+mobIndex).getMobClass());
                GameScene.show( new WndInfoMob( mob ) );
            }
        };
        IconType.setPos( 67, pos+86);
        IconType.setSize(16,16);
        add( IconType );

        StyledButton skillsTypeX = new StyledButton(Chrome.Type.BLANK, Messages.get(WndBestiary.class, "sks"),8);
        skillsTypeX.setRect(90, pos+75, 60, 16);
        add(skillsTypeX);

        skillsType = new StyledButton(Chrome.Type.GEM, Messages.get(WndBestiary.class, "info"),6){
            @Override
            public void onClick(){
                Mob mob  = Reflection.newInstance(allData.get(dataThreshold(mobTier)+mobIndex).getMobClass());
                if(mob instanceof FireDragon){
                    ShatteredPixelDungeon.scene().add(new WndHardNotification(mob.sprite(),
                            mob.name(),
                            Messages.get(WndBestiary.class,"firedragon"),
                            "OK",
                            0){
                    });
                } else {
                    GameScene.show(new WndMessage(Messages.get(WndBestiary.class,"cs")));
                }

            }
        };
        skillsType.setRect(90, pos+88, 60, 16);
        add(skillsType);

        createMobImage();

        updateSelectedMob();
        layout();
    }
    private void updateSelectedMob() {
        int selected = mobTier;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 3; ++i) {
            sb.append((i == selected ? "* " : "- "));
        }
        selectedPage.text(sb.toString());
        selectedPage.maxWidth(WIDTH / 2);
        selectedPage.setPos((WIDTH - selectedPage.width()) / 2, 5);
        updateMobText();
    }

    private void updateMobText(){
        Mob mob  = Reflection.newInstance(allData.get(dataThreshold(mobTier)+mobIndex).getMobClass());
        selectedMob.text( M.L(allData.get(dataThreshold(mobTier) + mobIndex).mobClass, "name") );
        damage.text(String.valueOf(mob.damageRoll()));
        hp.text(String.valueOf(mob.HT));
        dr.text(String.valueOf(mob.drRoll()));
        DecimalFormat df = new DecimalFormat("#.##");
        
        speed.text(df.format((mob.baseSpeed)));
        eva.text(String.valueOf((mob.defenseSkill)));
        atkSpeed.text(df.format(mob.attackDelay()));
        typeMob.text(ProName(mob));
        maxLevel.text(df.format(mob.attackSkill(mob)));
        IconType.icon(mob.sprite());

        Class<?> clazz = mob.getClass();

        boolean hasZapMethod = false;
        boolean hasBallisticaReference = false;
        boolean hasGetCloserOverride = false;
        boolean hasGetonZapComplete = false;
        boolean hasCall = false;

        // 检查类中是否含有 zap 方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("zap")) {
                hasZapMethod = true;
                break;
            }
        }

        // 检查类中是否含有 Ballistica 类引用
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(Ballistica.class)) {
                hasBallisticaReference = true;
                break;
            }
        }

        Method[] methods2 = clazz.getDeclaredMethods();
        for (Method method : methods2) {
            if (method.getName().equals("getCloser")) {
                hasGetCloserOverride = true;
                break;
            }
        }

        Method[] methods3 = clazz.getDeclaredMethods();
        for (Method method : methods3) {
            if (method.getName().equals("onZapComplete")) {
                hasGetonZapComplete = true;
                break;
            }
        }

        // 检查 getGetCloser 方法是否被重写
        Method[] methods4 = clazz.getDeclaredMethods();
        for (Method method : methods4) {
            if (method.getName().equals("call")) {
                hasCall = true;
                break;
            }
        }

        if(mob.properties.contains(Char.Property.IMMOVABLE)) {
            zapType.text("不攻击");
        } else if (hasGetCloserOverride && hasZapMethod) {
            zapType.text("远程魔法");
        } else if(hasGetCloserOverride && !(mob instanceof GreatCrab || mob instanceof Piranha)){
            zapType.text("远程物理");
        } else if (hasZapMethod || hasGetonZapComplete || hasCall || (mob instanceof Elemental || mob instanceof Eye)) {
                zapType.text("近战/远程(魔法)");
            } else if (hasBallisticaReference) {
                zapType.text("近战/远程(物理)");
            } else {
                zapType.text("纯近战");
            }
    }

    private void layout(){
        selectedPage.maxWidth(WIDTH / 2);
        selectedPage.setPos((WIDTH - selectedPage.width())/2, 5);
        selectedMob.maxWidth(WIDTH);
        selectedMob.setPos((WIDTH - selectedMob.width())/2, 16);
        resize(WIDTH, HEIGHT);
    }

    private void createMobImage() {
        int maxNum = maxMobIndex(mobTier) + 1;
        int firstLine = (maxNum >> 1) + (maxNum & 1);
        float left1 = (WIDTH - (GAP + BTN_SIZE) * firstLine + GAP)/2f;
        float left2 = (WIDTH - (GAP + BTN_SIZE) * (maxNum - firstLine) + GAP)/2f;
        for (int i = 0; i < maxNum; ++i) {
            final int j = i;
            IconButton btn = new IconButton() {
                @Override
                public void onClick() {
                    super.onClick();
                    mobIndex = j;
                    updateMobText();
                }
            };
            btn.icon( Reflection.newInstance(allData.get(dataThreshold(mobTier)+i).getMobClass()).sprite());
            float max = Math.max(btn.icon().width(), btn.icon().height());
            btn.icon().scale = new PointF(BTN_SIZE/max, BTN_SIZE/max);
            if(i<firstLine){
                btn.setRect(left1, 30f, BTN_SIZE, BTN_SIZE );
                left1 += GAP + BTN_SIZE;
            }else{
                btn.setRect(left2, 56f, BTN_SIZE, BTN_SIZE);
                left2 += GAP + BTN_SIZE;
            }
            add(btn);
            mobButtons.add(btn);
        }
    }

    private void clearImage(){
        for(int i=0, len = mobButtons.size();i<len;++i){
            mobButtons.get(i).destroy();
        }
    }

    private void refreshImage(){
        clearImage();
        createMobImage();
    }

    @Override
    public void onBackPressed() {

    }

    //packed with a linkedHashmap to find class by ordinal at O(1);
    private static LinkedHashMap<Integer, WndBestiary.DataPack> allData = new LinkedHashMap<>();
        static {
        for(DataPack dp : DataPack.values()){
        allData.put(dp.ordinal(), dp);}}


    private enum DataPack{

    //Forest
    RAT(Rat.class, DictSpriteSheet.RAT),
    FLWW(FlowerSlime.class, DictSpriteSheet.FLOWER),
    GNOLL(Gnoll.class, DictSpriteSheet.GNOLL),
    SNAKE(Salamander.class, DictSpriteSheet.SNAKE),
    ALBINO(Albino.class, DictSpriteSheet.ALBINO),
    CRAB(Crab.class, DictSpriteSheet.CRAB),
    SWARM(Swarm.class, DictSpriteSheet.SWARM),
    SLIME(Slime_Orange.class, DictSpriteSheet.SLIME),
    KATD(Katydid.class, DictSpriteSheet.SLIME),
    X_RAT(ClearElemental.class, DictSpriteSheet.F_RAT),
    C_SLIME(Slime_Red.class, DictSpriteSheet.CAUSTIC_SLIME),
    F_RAT(FetidRat.class, DictSpriteSheet.F_RAT),
    GNOLL_DARTER(GnollTrickster.class, DictSpriteSheet.GNOLL_DARTER),
    GREAT_CRAB(GreatCrab.class, DictSpriteSheet.GREAT_CRAB),

    //Forest Boss

    DGBOSS_M1(DiedClearElemet.ClearElemetalBlood.class, 0),
    DGBOSS_M2(DiedClearElemet.ClearElemetalGreen.class, 0),
    DGBOSS_M3(DiedClearElemet.ClearElemetalDark.class, 0),
    DGBOSS_M4(DiedClearElemet.ClearElemetalPure.class, 0),
    DGBOSS_M5(DiedClearElemet.ClearElemetalGold.class, 0),
    CRVBOSS(CrivusFruits.class, 0),
    DGBOSS(FireDragon.class, 0),
    CRVBOSS_LASER(CrivusFruitsLasher.class, 0),
    CRXBOSS_LASER(CrivusStarFruitsLasher.class, 0),
    GOOBOSS(Goo.class, 0),
    CRXBOSS(CrivusStarFruits.class, 0),

    SKELETON(Skeleton.class, DictSpriteSheet.SKELETON),
    THIEF(Thief.class, DictSpriteSheet.THIEF),
    BANDIT(Bandit.class, DictSpriteSheet.BANDIT),
    DM100(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100.class, DictSpriteSheet.DM100),
    GUARD(Guard.class, DictSpriteSheet.GUARD),
    NECRO(Necromancer.class, DictSpriteSheet.NECROMANCER),
    ROT_LASHER(RotLasher.class, DictSpriteSheet.ROT_LASHER),
    ROT_HEART(RotHeart.class, DictSpriteSheet.ROT_HEART),
    NEW_FIRE_ELE(Elemental.NewbornFireElemental.class, DictSpriteSheet.NEW_FIRE_ELE),

    BAT(Bat.class, DictSpriteSheet.BAT),
    BRUTE(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Brute.class, DictSpriteSheet.BRUTE),
    NRXD(SpectralNecromancer.class, DictSpriteSheet.SLXJ),
    ARMORED_BRUTE(ArmoredBrute.class, DictSpriteSheet.ARMORED_BRUTE),
    SHAMAN(Shaman.random(), DictSpriteSheet.SHAMAN),
    SPINNER(Spinner.class, DictSpriteSheet.SPINNER),
    DM200(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM200.class, DictSpriteSheet.DM200),
    DM201(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM201.class, DictSpriteSheet.DM201),

    GHOUL(Ghoul.class, DictSpriteSheet.GHOUL),
    WARLOCK(Warlock.class, DictSpriteSheet.WARLOCK),
    MONK(Monk.class, DictSpriteSheet.MONK),
    SENIOR(Senior.class, DictSpriteSheet.SENIOR),
    GOLEM(Golem.class, DictSpriteSheet.GOLEM),
    ELE_FIRE(Elemental.FireElemental.class, DictSpriteSheet.ELEMENTAL_FIRE),
    ELE_FROST(Elemental.FrostElemental.class, DictSpriteSheet.ELEMENTAL_FROST),
    ELE_LIGHTNING(Elemental.ShockElemental.class, DictSpriteSheet.ELEMENTAL_SHOCK),
    ELE_CHAOS(Elemental.ChaosElemental.class, DictSpriteSheet.ELEMENTAL_CHAOS),

    RIPPER(RipperDemon.class, DictSpriteSheet.RIPPER),
    //        SPAWNER(DemonSpawner.class, DictSpriteSheet.SPAWNER),
    EYE(Eye.class, DictSpriteSheet.EYE),
    SUCCUBUS(Succubus.class, DictSpriteSheet.SUCCUBUS),
    SCORPIO(Scorpio.class, DictSpriteSheet.SCORPIO),
    ACIDIC(Acidic.class, DictSpriteSheet.AICDIC),

    STATUE(Statue.class, DictSpriteSheet.STATUE),
    ARMORED_STATUE(ArmoredStatue.class, DictSpriteSheet.ARMORED_STATUE),
    WRAITH(Wraith.class, DictSpriteSheet.WRAITH),
    TORMENTED_SPIRIT(TormentedSpirit.class,DictSpriteSheet.TORMENTED_SPIRIT),
    PHANTOM_PIRANHA(PhantomPiranha.class,DictSpriteSheet.PHANTOM_PIRANHA),
    PIRANHA(Piranha.class, DictSpriteSheet.FISH),



    ZSLS(OGPDZSLS.class, DictSpriteSheet.OGPDZSLS),
    LLS(OGPDLLS.class, DictSpriteSheet.OGPDLLS),
    COLD(ColdMagicRat.class, DictSpriteSheet.COLD),
    RED(RedSwarm.class, DictSpriteSheet.RED),
    SHOW(KagenoNusujin.class, DictSpriteSheet.SHOW),
    BLACK(BlackHost.class, DictSpriteSheet.BLACK),

    BLACK2(NewBlackHost.class, DictSpriteSheet.BLACK),

    HBLR(SRPDHBLR.class, DictSpriteSheet.HBLR),
    MOlO(MolotovHuntsman.class, DictSpriteSheet.MOLO),

    GnollK(GnollShiled.class, DictSpriteSheet.GnollK),
    GnollF(SkullShaman.class, DictSpriteSheet.GnollF),
    FlameC(FlameC01.class, DictSpriteSheet.FLAMEC01),
//        Good_VI50(GooMob.class, DictSpriteSheet.Goo),

    Flame(SlimeKingMob.class, DictSpriteSheet.FLAME),
    NQHZ(OGPDNQHZ.class, DictSpriteSheet.OGPDNQHZ),

    CLEARS(ClearElemental.class, DictSpriteSheet.CLEARS),
    SLXJ(SpectralNecromancer.class, DictSpriteSheet.SLXJ),
    BMHR(ShieldHuntsman.class, DictSpriteSheet.BMHR),
    KTX(Katydid.class,0),
    STX(Salamander.class, DictSpriteSheet.STX),
    XHDD(RedMurderer.class, DictSpriteSheet.XHDD),
    IAX(Ice_Scorpio.class, DictSpriteSheet.IAX),
    FAX(Fire_Scorpio.class, DictSpriteSheet.FAX),
    CAX(SRPDICLR.class, DictSpriteSheet.CAX),
    GOO(XTG200.class,DictSpriteSheet.GOO),
    IKS(ColdGurad.class,DictSpriteSheet.GKS),
    BRTX(BruteBot.class, DictSpriteSheet.BRTX),

    BRBAT(BrownBat.class, 0),
    IAS(IceGolem.class, DictSpriteSheet.IAS),


    DR(DrTerror.class, 0),
    CLIX(GunHuntsman.class, 0),
    GQ(GreenSlting.class, 0),
    GNOLLHERO(GnollHero.class, 0),
    DM275RPG(DM275.class, 0),
    SQ(SuccubusQueen.class, 0),
    Appwitch(ApprenticeWitch.class, 0),
    Zombie(Frankenstein.class, 0),
    Brute(Butcher.class, 0),
    Pump(Crumb.class, 0),
    Pump2(PumkingBomber.class, 0),
    HOWW(HollowMimic.class, 0),
    BlackSoulx(BlackSoul.class, 0),
    ANO(AnomaloCaris.class, 0),
    TB(ThreeLeafBug.class, 0),
    DiedXP(Turtle.class, 0),
    DiedP(Turtle.class, 0),

    Crystal(CrystalGuardian.class,0),
    CrystalS(CrystalWisp.class,0),
    GnollX(GnollGuard.class,0),
    GnollC(GnollSapper.class,0),
    FLX(CrystalGuardian.class,0),
        
    CERS(CrivusFruits.class,0),
        
    FLXX(FungalSpinner.class,0);



    private Class<? extends Mob> mobClass;
    private int imageId;

    DataPack(Class<? extends Mob> cls, int image){
        this.imageId = image;
        this.mobClass = cls;
    }

    public int getImageId(){return imageId;}
    public Class<? extends Mob> getMobClass(){return mobClass;}
    }
}