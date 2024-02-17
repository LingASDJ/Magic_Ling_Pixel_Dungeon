package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MIME;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TPDoorSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class TPDoor extends Mob {

    {
        spriteClass = TPDoorSprites.class;

        HP = HT = 100;
        properties.add(Property.MINIBOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.ABYSS);



        if(!Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
            properties.add(Property.IMMOVABLE);
            baseSpeed = 0.85f;
            state = PASSIVE;
        }

    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
           return false;
        } else {
            return super.canAttack(enemy);
        }
    }

    @Override
    protected boolean getCloser(int target) {
        if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            if (state == HUNTING) {
                if (Dungeon.level.distance(pos, target) > 12)
                    return super.getCloser(target);
                return enemySeen && getFurther(target);
            } else {
                return super.getCloser(target);
            }
        } else {
            return super.getCloser(target);
        }
        //return false;
    }

    private int kill = 0;

    private static final String KILL = "kill";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(KILL, kill);
    }


    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        kill = bundle.getInt(KILL);
    }

    @Override
    public void damage(int dmg, Object src) {

        if (AntiMagic.RESISTS.contains(src.getClass())) {
            dmg = 0;
        } else {
            if(!(src instanceof Hero)){
                return;
            }
            if (dmg >= 20){
                //20
                dmg = 20;
                if (hero.buff(DiamondKnight.DiedDamager.class) == null) {
                    Buff.affect(this, DiamondKnight.DiedDamager.class);
                    switch (Random.NormalIntRange(0,8)){
                        case 0:
                            pos = 613;
                            break;
                        case 1:
                            pos = 888;
                            break;
                        case 2:
                            pos = 1088;
                            break;
                        case 3:
                            pos = 732;
                            break;
                        case 4:
                            pos = 297;
                            break;
                        case 5:
                            pos = 206;
                            break;
                        case 6:
                            pos = 255;
                            break;
                        case 7:
                            pos = 1186;
                            break;
                        case 8:
                            pos = 820;
                            break;
                    }
                    ScrollOfTeleportation.appear(this, pos);
                    Buff.affect( hero, MindVision.class, 2f );
                    kill = 0;
                }
            } else {
                dmg = 0;

                if(kill >= 5){

                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(new DimandKingSprite(),
                                    Messages.titleCase(Messages.get(DiamondKnight.class, "name")),
                                    Messages.get(TPDoor.class, "quest_tengu_prompt"),
                                    Messages.get(TPDoor.class, "enter_yes"),
                                    Messages.get(TPDoor.class, "enter_no")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {

                                        GameScene.show(new WndOptions(new DimandKingSprite(),
                                                Messages.titleCase(Messages.get(DiamondKnight.class, "name")),
                                                Messages.get(TPDoor.class, "quest_tengu_really"),
                                                Messages.get(TPDoor.class, "enter_no"),
                                                Messages.get(TPDoor.class, "enter_no"),
                                                Messages.get(TPDoor.class, "enter_no"),
                                                Messages.get(TPDoor.class, "enter_yes")) {
                                            @Override
                                            protected void onSelect(int index) {
                                                if (index == 3) {
                                                    Statistics.mustTengu = true;
                                                    if(Dungeon.level.locked) Dungeon.level.unseal();
                                                    InterlevelScene.mode = InterlevelScene.Mode.RESET;
                                                    Game.switchScene(InterlevelScene.class);
                                                    for (Heap heap : level.heaps.valueList()) {
                                                        for (Item item : heap.items) {
                                                            if(!(item instanceof MIME)){
                                                                item.doPickUp(hero, hero.pos);
                                                                heap.destroy();
                                                            } else {
                                                                heap.destroy();
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    kill = 0;
                                                    yell( Messages.get(DiamondKnight.class, "ten"));
                                                }
                                            }
                                        });
                                    } else if(index == 1){
                                        kill = 0;
                                        yell( Messages.get(DiamondKnight.class, "ten"));
                                    }
                                }
                            });
                        }
                    });


                }

                if(kill < 6 && enemy == hero){
                    GLog.n(Messages.get(this,"lowdamage"));
                    kill++;
                } else {
                    kill--;
                }
            }
        }
        super.damage(dmg, src);
    }

    @Override
    public void die( Object cause ) {

        super.die(cause);
        GameScene.flash(0x808080);
        Statistics.TPDoorDieds = true;
        ((ColdChestBossLevel) Dungeon.level).progress();
    }

}

