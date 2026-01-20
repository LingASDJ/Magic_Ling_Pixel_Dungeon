package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MageHand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class MagicAbsorb extends Buff {

    {
        type = buffType.POSITIVE;
    }

    private int level = 0;
    private int interval = 1;

    public int absordCooldown = 100;

    public void downAbsord(int value) {
        absordCooldown -= value;
    }

    public void Abosord(){
        switch (Dungeon.hero.pointsInTalent(Talent.MAGIC_ABSORB)){
            case 1:
                absordCooldown = 41;
                break;
            case 2:
                absordCooldown = 31;
                break;
            case 3:
                absordCooldown = 21;
                break;
        }
    }

    private MagesStaff heroMagesStaff;
    private MageHand mageHand;

    private ArrayList<Wand> wands;

    @Override
    public boolean act() {
        if (target.isAlive()) {

            boolean isMageHand = false;
            wands = hero.belongings.getAllItems(Wand.class);
            if(absordCooldown < 2){
                Abosord();
                heroMagesStaff = Dungeon.hero.belongings.getItem(MagesStaff.class);

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob instanceof MageHand) {
                        isMageHand = true;
                        mageHand = (MageHand) mob;
                    }
                }

                if(isMageHand && mageHand.magesStaff != null){
                    if(mageHand.magesStaff.wand.curCharges < mageHand.magesStaff.wand.maxCharges) {
                        mageHand.magesStaff.wand.curCharges++;
                    }
                    for (Wand w : wands.toArray(new Wand[0])){
                        if(w.curCharges < w.maxCharges) {
                            w.curCharges++;
                        }
                    }
                    Buff.affect(Dungeon.hero, ArtifactRecharge.class).prolong(2f);
                    hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "msg"));
                } else {
                    if(heroMagesStaff != null){
                        Wand heroMagesStaffWand = heroMagesStaff.wand;
                        if(heroMagesStaffWand.curCharges >= heroMagesStaffWand.maxCharges){
                            for (Wand w : wands.toArray(new Wand[0])){
                                if(w.curCharges < w.maxCharges){
                                    w.curCharges++;
                                }
                            }
                            Buff.affect(Dungeon.hero, ArtifactRecharge.class).prolong(2f);
                            hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "msg"));
                        } else {
                            heroMagesStaffWand.curCharges++;
                            hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "msg"));
                        }
                    }
                }
            }

            if(Dungeon.hero.hasTalent(Talent.MAGIC_ABSORB)){
                if(absordCooldown == 100){
                    Abosord();
                } else {
                    absordCooldown--;
                }
            }

            spend( interval );
            if (level <= 0) {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }

    public int level() {
        return level;
    }

    public void set( int value, int time ) {
        if (level <= value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(absordCooldown);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", absordCooldown);
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";

    private static final String ABSORD_COOLDOWN = "absord_cooldown";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, level );
        bundle.put( ABSORD_COOLDOWN, absordCooldown );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        level = bundle.getInt( LEVEL );
        absordCooldown = bundle.getInt(ABSORD_COOLDOWN);
    }
}
