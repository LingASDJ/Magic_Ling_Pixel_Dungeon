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
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.WndTextNumberInput;
import com.watabou.noosa.Game;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CommRelay extends Artifact {

    {
        image = ItemSpriteSheet.DM100RELEY;
        level = 0;
        levelCap = 10;
        animation = true;
        defaultAction=AC_MERC;
    }

    @Override
    public void frames(ItemSprite itemSprite){
        itemSprite.texture(Assets.Sprites.ANIMATIONS_TERMIAL);
        TextureFilm frames = new TextureFilm(itemSprite.texture, 16, 16);
        MovieClip.Animation idle = new MovieClip.Animation(15, true);
        idle.frames( frames,0,1,1,2,2,2,3,3,4,4,5,5);
//        if(animationToidle){
//
//        } else {
//            idle.frames( frames,3);
//        }

        itemSprite.play(idle);
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
                    GLog.i(Messages.get(CommRelay.class,"unequipped"));
                else if(!useable())
                    GLog.i(Messages.get(CommRelay.class,"unuseable"));
                else if (!(Dungeon.gold >= 3500))//TODO adjust the gold cost based on current level.
                    GLog.w(Messages.get(CommRelay.class,"no_money"));
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
                    GLog.p(Messages.get(CommRelay.class,"merc"));
                }
            } else {
                GLog.i(Messages.get(CommRelay.class,"already_cursed"));
                //GLog.i("The item will not obey you!");
            }
        } else if (action.equals(AC_SUPP)) {
            if (!(Dungeon.gold >= 5000)){//TODO adjust the gold cost based on current level.
                GLog.w(Messages.get(CommRelay.class,"no_money"));
            } else if(Dungeon.bossLevel()){
                GLog.i(Messages.get(CommRelay.class,"boss"));
            } else{
                GameScene.selectCell(listener);
            }
        } else if(action.equals(AC_CALL)) {
            Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                    Messages.get(CommRelay.class, "call_title"), Messages.get(CommRelay.class, "call_desc"),
                    "",
                    8, false, Messages.get(CommRelay.class, "confirm"),
                    Messages.get(CommRelay.class, "cancel"),false) {
                @Override
                public void onSelect(boolean check, String text) {
                    if (text.matches("^[1-9]\\d*$")) {
                        callNumber = Integer.parseInt(text);
                        if (callNumber == Statistics.commonrelaycall) {
                            GLog.n(Messages.get(CommRelay.class, "call_success"));
                            animationToidle = true;
                            updateQuickslot();
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
        String desc = Messages.get(CommRelay.class,"desc_1");

            desc += Messages.get(CommRelay.class,"desc_2") +
                    Messages.get(CommRelay.class,"desc_3");

        if(isEquipped(Dungeon.hero)){
            desc += "\n\n";
            if(cursed){
                desc += Messages.get(CommRelay.class,"desc_cursed");
            } else if(level < 2){
                desc += Messages.get(CommRelay.class,"desc_low_level");
            } else if (level < 10){
                desc += Messages.get(CommRelay.class,"desc_high_level");
            } else {
                desc += Messages.get(CommRelay.class,"desc_work_well");
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
                GLog.p(Messages.get(CommRelay.class,"item_tip"));
            }
        }

        @Override
        public String prompt() {
            return Messages.get(CommRelay.class,"item_pos");
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
