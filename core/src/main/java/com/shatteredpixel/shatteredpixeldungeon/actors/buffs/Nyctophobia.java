package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.DHXD;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero.badLanterFire;
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero.goodLanterFire;
import static com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.cure;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.ClearLanterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayCursed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayKill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayMoneyMore;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayNoSTR;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSaySlowy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayTimeLast;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Nyctophobia extends Buff implements Hero.Doom {

    public static class NoRoadMobs extends Buff {

        int spawnPower = 0;

        {
            revivePersists = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.VERTIGO;
        }
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.GDX_COLOR);
        }

        @Override
        public boolean act() {
            if(!Dungeon.bossLevel() || !Dungeon.sbbossLevel()){
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                    if(!(mob instanceof NPC)) {
                        ArrayList<Integer> candidates = new ArrayList<>();
                        int minDist = Math.round(Dungeon.hero.viewDistance / 3f);
                        for (int i = 0; i < Dungeon.level.length(); i++) {
                            if (Dungeon.level.heroFOV[i]
                                    && !Dungeon.level.solid[i]
                                    && Actor.findChar(i) == null
                                    && Dungeon.level.distance(i, Dungeon.hero.pos) > minDist) {
                                candidates.add(i);
                            }
                        }
                        if (!candidates.isEmpty()) {
                            ScrollOfTeleportation.teleportToLocation(mob, Random.element(candidates));
                            Sample.INSTANCE.play(Assets.Sounds.CURSED);
                        }
                    }
                }
            }
            spend(30f);
            return true;
        }

        private static String SPAWNPOWER = "spawnpower";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put( SPAWNPOWER, spawnPower );
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            spawnPower = bundle.getInt( SPAWNPOWER );
        }
    }

    @Override
    public String heroMessage() {
        return "";
    }

    private static final String LEVEL = "level";
    private static final String PARTIALDAMAGE = "partialDamage";

    private static final float STEP = 100.0f;

    private float level;
    private float partialDamage;

    @Override
    public boolean act() {
        NoRoadMobs bs = Dungeon.hero.buff(NoRoadMobs.class);
        if(Dungeon.isChallenged(DHXD) && hero.lanterfire<10){
            Buff.affect(hero, NoRoadMobs.class);
        } else if(bs != null) {
            Buff.detach(hero, NoRoadMobs.class);
        }
        spend(1f);

        if(hero.lanterfire >= 90){
            for (Buff b : hero.buffs(ClearLanterBuff.class)){
               if(b == null){
                   goodLanterFire();
               }
               spend(200f);
           }
        }

        if (hero.lanterfire < 51 && hero.lanterfire>31) {
            cure( Dungeon.hero );
            badLanterFire();
            spend(100f);
        } else if (hero.lanterfire < 31 && hero.lanterfire > 1) {
            cure( Dungeon.hero );
            switch (Random.Int(5)){
                case 0: case 1:
                    Buff.affect(hero, MagicGirlSayMoneyMore.class).set( (100), 1 );
                    Buff.affect(hero, MagicGirlSayCursed.class).set( (100), 1 );
                    break;
                case 2:case 3:
                    Buff.affect(hero, MagicGirlSayTimeLast.class).set( (100), 1 );
                    Buff.affect(hero, MagicGirlSaySlowy.class).set( (100), 1 );
                    break;
                case 4:
                    Buff.affect(hero, MagicGirlSayKill.class).set( (100), 1 );
                    Buff.affect(hero, MagicGirlSayNoSTR.class).set( (100), 1 );
                    break;
            }
            spend(90f);
        } else if(hero.lanterfire<=0) {
            hero.damage(1 +Challenges.activeChallenges()/3*Dungeon.depth/5, trueDamge.class);
            GLog.n(Messages.get(this,"dead"));
            cure( Dungeon.hero );
            switch (Random.Int(4)){
                case 0: case 1:
                    Buff.affect(hero, MagicGirlSayMoneyMore.class).set( (100), 1 );
                    Buff.affect(hero, MagicGirlSayCursed.class).set( (100), 1 );
                    Buff.affect(hero, MagicGirlSayTimeLast.class).set( (100), 1 );
                    break;
                case 2: case 3:
                    Buff.affect(hero, MagicGirlSaySlowy.class).set( (100), 1 );
                    Buff.affect(hero, MagicGirlSayKill.class).set( (100), 1 );
                    Buff.affect(hero, MagicGirlSayNoSTR.class).set( (100), 1 );
                    break;
            }
            spend(40f);
        }
        return true;
    }

    private static class trueDamge{};

    @Override
    public String desc() {
        String result;
        if (hero.lanterfire >= 90 && hero.lanterfire <= 100) {
            result =  Messages.get(this, "desc");
        } else if (hero.lanterfire >= 80 && hero.lanterfire <= 89) {
            result = Messages.get(this, "desc2");
        } else if (hero.lanterfire >= 60 && hero.lanterfire <= 79) {
            result = Messages.get(this, "desc3");
        } else if (hero.lanterfire >= 35 && hero.lanterfire <= 59) {
            result = Messages.get(this, "desc4");
        } else if (hero.lanterfire >= 1 && hero.lanterfire <= 34) {
            result = Messages.get(this, "desc5");
        } else {
            result = Messages.get(this, "desc6");
        }
        return result;
    }


    @Override
    public int icon() {
        return BuffIndicator.NONE;
    }

    @Override
    public void onDeath() {
        GLog.n(Messages.get(this, "ondeath"));
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.level = bundle.getFloat(LEVEL);
        this.partialDamage = bundle.getFloat(PARTIALDAMAGE);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, this.level);
        bundle.put(PARTIALDAMAGE, this.partialDamage);
    }

    @Override
    public String name() {
        if (hero.lanterfire >= 90 && hero.lanterfire <= 100) {
            return Messages.get(this, "name");
        } else if (hero.lanterfire >= 80 && hero.lanterfire <= 89) {
            return Messages.get(this, "name2");
        } else if (hero.lanterfire >= 60 && hero.lanterfire <= 79) {
            return Messages.get(this, "name3");
        } else if (hero.lanterfire >= 35 && hero.lanterfire <= 59) {
            return Messages.get(this, "name4");
        } else if (hero.lanterfire >= 1 && hero.lanterfire <= 34) {
            return Messages.get(this, "name5");
        } else {
            return Messages.get(this, "name6");
        }
    }
}
