package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.WndTextNumberInput;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CommRelay extends Artifact {
    {

        image = ItemSpriteSheet.DM100RELEY;
        level = 0;
        levelCap = 5;

        defaultAction=AC_MERC;
    }

    private static final int NIMAGES = 1;
    //private static final String AC_SUPP = "SUPPORT PACKAGE";
    //private static final String AC_MERC = "HIRE MERCENARY";
    //private static final String TXT_MERC = "A mercenary teleports adjacent to you!";
    //private static final String TXT_BOSS = "Strong magic aura of this place prevents the guilds from teleporting
    // you supplies!";
//    private static final String AC_SUPP = "补给物";
//    private static final String AC_MERC = "呼唤雇佣兵";
//    private static final String TXT_MERC = "雇佣兵传送到你附近！";
//    private static final String TXT_BOSS = "这里强大的魔力流阻止了地牢国际安全委员公会预传送给你的东西！";

    private static final String AC_SUPP = "supp";
    private static final String AC_CALL = "call";
    private static final String AC_MERC = "merc";

    private int callNumber;
    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);

            if (isEquipped(hero)){
                actions.add(AC_MERC);
            }
            actions.add(AC_CALL);
            actions.add(AC_SUPP);

        return actions;
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Collection();
    }

    protected boolean useable(){
            return true;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_MERC)) {
            if(!cursed){
                if (!isEquipped(hero))
                    GLog.i("你需要先装备神器才能使用！");
                else if(!useable())
                    GLog.i("你想做什么？");
                else if (!(Dungeon.gold >= 3500))//TODO adjust the gold cost based on current level.
                    GLog.w("你穷困潦倒，不能使用神器的该技能！");
                else {
                    ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

                    for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                        int p = curUser.pos + PathFinder.NEIGHBOURS8[i];
                        if (Actor.findChar(p) == null) {
                            respawnPoints.add(p);
                        }
                    }

                    int nImages = NIMAGES;
                    while (nImages > 0 && respawnPoints.size() > 0) {
                        int index = Random.index(respawnPoints);

                        MirrorImage mob = new MirrorImage();
                        mob.duplicate( hero );
                        GameScene.add(mob);
                        ScrollOfTeleportation.appear(mob, respawnPoints.get(index));

                        respawnPoints.remove(index);
                        nImages--;
                    }

                    Sample.INSTANCE.play(Assets.Sounds.READ);
                    Invisibility.dispel();
                    Dungeon.gold -= 3500;
                    GLog.p("TXT_MERC");
                }
            } else {
                GLog.i("我们不会服从你!");
                //GLog.i("The item will not obey you!");
            }
        } else if (action.equals(AC_SUPP)) {
            if (!(Dungeon.gold >= 5000)){//TODO adjust the gold cost based on current level.
                GLog.w("你穷困潦倒，不能使用神器的该技能！");
            } else if(Dungeon.bossLevel()){
                GLog.i("TXT_BOSS");
            } else{
                GameScene.selectCell(listener);
            }
        } else if(action.equals(AC_CALL)) {
            Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                    Messages.get(CommRelay.class, "call_title"), Messages.get(CommRelay.class, "call_desc"),
                    "",
                    8, false, Messages.get(CommRelay.class, "confirm"),
                    Messages.get(CommRelay.class, "cancel")) {
                @Override
                public void onSelect(boolean check, String text) {
                    if (text.matches("^[1-9]\\d*$")) {
                        callNumber = Integer.parseInt(text);
                        if (callNumber == Statistics.commonrelaycall) {
                            GLog.n(Messages.get(CommRelay.class, "call_success"));
                        } else {
                            GLog.w(Messages.get(CommRelay.class, "call_fail"));
                        }
                    }

                }
            }));
        }
    }

    @Override
    public String desc() {
        String desc = "你惊讶的在地牢里发现了一个正在工作的通讯中继器！这还有一份说明书，";

            desc += "加上一点阅读时的相互参照，你搞明白了它的功能。\n\n" +
                    "似乎它连接着雇佣兵协会的数据库。你很确定你可以和协会做个交易，当然是以一些金钱作为交换。";

        if(isEquipped(Dungeon.hero)){
            desc += "\n\n";
            if(cursed){
                desc += "中继器在你背包里自己打开了，并发出了一道响亮的静电在地牢中回荡。";
            } else if(level < 2){
                desc += "你用无线电联系上了一个雇佣兵协会，他们很乐意给你提供一些帮助，只要你花一些钱来启动他们的物质传送器，";
            } else if (level < 10){
                desc += "有人远程在你的通讯中继器上添加了一个大大的红色按钮，上面写着“补给包：5000金币 和 雇佣兵：3500金币“，你当然知道这会花你一大笔钱，但你不禁对它的用处浮想联翩。";
            } else {
                desc += "你和电台上的协会很熟了，他们愿意为你提供跟好的服务，并收取更低费用。";
            }
        }

        return desc;
    }
    protected static CellSelector.Listener listener = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer pos) {
            if (pos != null) {
                Dungeon.level.drop(Generator.random(Generator.Category.FOOD), pos).type = Heap.Type.CHEST;
                int loot = Random.Int(3);
                if (loot == 0) {
                    Dungeon.level.drop(Generator.random(Generator.Category.RING), pos);
                } else if (loot == 1) {
                    Dungeon.level.drop(Generator.random(Generator.Category.WAND), pos);
                } else {
                    Dungeon.level.drop(Generator.random(Random.oneOf(Generator.Category.WEAPON,
                            Generator.Category.ARMOR)), pos);
                }
//                Dungeon.level.drop(new Ankh(), pos);
                Dungeon.level.drop(Generator.random(Generator.Category.POTION), pos);
                Dungeon.level.drop(Generator.random(Generator.Category.SCROLL), pos);
                Dungeon.gold -= 5000;
                Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
                GameScene.updateMap(pos);
                Dungeon.observe();
                GLog.p("地表商会为你带来了丰厚的物资。请尽情享用。");
            }
        }

        @Override
        public String prompt() {
            return "选择补给物的位置";
        }
    };


    public class Collection extends ArtifactBuff{

        public void collectGold(int gold){
            exp += gold / 4;
        }

        @Override
        public boolean act(){
            if(isCursed()&& level < levelCap){
                exp -= (500 * level);
                upgrade();
            }
            spend(TICK);

            if(exp >= (500 * level) && level < levelCap){
                exp -= (500 * level);
                upgrade();
            }
            updateQuickslot();
            return true;
        }
        public void checkUpgrade(){
            while (exp >= 1000 && level < levelCap){
                exp -= 1000;
                upgrade();
            }
        }
    }
}
