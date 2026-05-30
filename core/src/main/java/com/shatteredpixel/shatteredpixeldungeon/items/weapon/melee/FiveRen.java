package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.List;

public class FiveRen extends MeleeWeapon {

    {
        image = ItemSpriteSheet.FIVEREN;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 0.9f;

        tier = 5;
        DLY = 1.5f; //0.67x speed
        RCH = 2;    //extra reach
    }

    public String desc() {
        PaswordBadges.loadGlobal();
        List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);
        return passwordbadges.contains(PaswordBadges.Badge.ZQJ_GHOST)?  Messages.get(this, "desc") : Messages.get(this, "desc_alt");
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        switch (Random.Int(7)) {
            case 0:case 1:case 2:case 3:case 4:
            default:
                return super.proc(attacker, defender, damage);
            case 5:case 6:case 7:
                if (Random.Int(10)==1) {
                    Buff.affect(defender, Corrosion.class).set(5f, Dungeon.depth/3);
                    if (Dungeon.level.heroFOV[defender.pos]) {
                        defender.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
                        Sample.INSTANCE.play(Assets.Sounds.CHARMS);
                    }
                }
                return super.proc(attacker, defender, damage);
        }
    }

    @Override
    public int max(int lvl) {
        return  Math.round(6.67f*(tier+1)) +    //20 base, up from 15
                lvl*Math.round(1.33f*(tier+1)); //+4 per level, up from +3
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(9+2*lvl) damage, roughly +83% base damage, +80% scaling
        int dmgBoost = augment.damageFactor(9 + Math.round(2f*buffedLvl()));
        Spear.spikeAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 9 + Math.round(2f*buffedLvl()) : 9;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
        }
    }

    public String upgradeAbilityStat(int level){
        int dmgBoost = 9 + Math.round(2f*level);
        return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
    }


}

