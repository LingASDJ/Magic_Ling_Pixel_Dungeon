package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BloodBat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.Pets;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TPDoorSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class TPDoor extends Boss {

    {
        spriteClass = TPDoorSprites.class;

        HP = HT = 100;

        properties.add(Property.BOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.ELECTRIC);
        properties.add(Property.IMMOVABLE);

        state = PASSIVE;

        baseSpeed =0;
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
                Buff.affect( enemy, MindVision.class, 1f );
                enemy.sprite.jump(hero.pos, pos, new Callback() {
                    @Override
                    public void call() {
                        Dungeon.level.occupyCell(hero);
                        Dungeon.observe();
                        GameScene.updateFog();
                        Dungeon.level.occupyCell(hero);
                        Dungeon.observe();
                        GameScene.updateFog();

                        Camera.main.shake(2, 0.5f);

                        hero.spendAndNext(1);
                        ScrollOfTeleportation.appear(hero, hero.pos);
                        int doorPos = 918;

                        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                            if (	mob instanceof Pets ||
                                    mob instanceof DriedRose.GhostHero ||
                                    mob instanceof BloodBat ||
                                    mob instanceof SpiritHawk.HawkAlly) {
                                ScrollOfTeleportation.appear(mob, doorPos);
                            }
                        }
                    }
                });
                GameScene.scene.add(new Delayer(0.1f){
                    @Override
                    protected void onComplete() {
                        GameScene.scene.add(new Delayer(2f){
                            @Override
                            protected void onComplete() {
                                Buff.affect( hero, Paralysis.class, 5f);
                            }
                        });
                    }
                });
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
                                    Statistics.mustTengu = true;
                                    if(Dungeon.level.locked) Dungeon.level.unseal();
                                    InterlevelScene.mode = InterlevelScene.Mode.RESET;
                                    Game.switchScene(InterlevelScene.class);
                                } else if(index == 1){
                                    kill = 0;
                                    yell( Messages.get(DiamondKnight.class, "ten"));
                                }
                            }
                        });
                    }
                });


            }

            if(kill < 6){
                GLog.n(Messages.get(this,"lowdamage"));
                kill++;
                for (Buff b : buffs(Buff.class)){
                    b.detach();
                }
            } else {
                kill--;
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

