package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero.badLanterFire;
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero.goodLanterFire;
import static com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.cure;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.ClearLanterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayCursed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayKill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayMoneyMore;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayNoSTR;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSaySlowy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayTimeLast;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Nyctophobia extends Buff implements Hero.Doom {

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

        if(hero.lanterfire >= 90){
            //灯火大于90给予一个buff 然后不叠加
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
        } else if(hero.lanterfire == 0) {
            hero.damage((int)1+Challenges.activeChallenges()/3*Dungeon.depth/5, trueDamge.class);
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


        if (Dungeon.depth <= 5) {
            spend(STEP);
            return true;
        } else if (Dungeon.level.locked || this.target.buff(LighS.class) != null) {
            spend(STEP);
            return true;
        } else {
            Hero hero = (Hero) this.target;
            if (!this.target.isAlive()) {
                diactivate();
                return true;
            }
            if (hero.lanterfire > 0 ) {
                hero.damageLantern(1+Challenges.activeChallenges()/3);
                spend(20f-(float) Dungeon.depth/5+Challenges.activeChallenges());
            } else {
                spend(STEP);
            }
            return true;
        }
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
        if (hero.lanterfire >= 90 && hero.lanterfire <= 100) {
            return BuffIndicator.LANTERFIRE_ONE;
        } else if (hero.lanterfire >= 80 && hero.lanterfire <= 89) {
            return BuffIndicator.LANTERFIRE_TWO;
        } else if (hero.lanterfire >= 60 && hero.lanterfire <= 79) {
            return BuffIndicator.LANTERFIRE_THREE;
        } else if (hero.lanterfire >= 35 && hero.lanterfire <= 59) {
            return BuffIndicator.LANTERFIRE_FOUR;
        } else if (hero.lanterfire >= 1 && hero.lanterfire <= 34) {
            return BuffIndicator.LANTERFIRE_FIVE;
        } else {
            return BuffIndicator.LANTERFIRE_SIX;
        }
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
