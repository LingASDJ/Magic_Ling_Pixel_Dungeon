package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class RiverSlade extends MissileWeapon {

    {
        image = ItemSpriteSheet.GREEN_DARK;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.1f;

        tier = 4;
        baseUses = 10;
        sticky = true;
    }

    @Override
    public float durabilityPerUse(){
        float usages = baseUses * (float)(Math.pow(5, 0));

        //+50%/75% durability
        if (Dungeon.hero.hasTalent(Talent.DURABLE_PROJECTILES)){
            usages *= 1.25f + (0.25f*Dungeon.hero.pointsInTalent(Talent.DURABLE_PROJECTILES));
        }
        if (holster) {
            usages *= MagicalHolster.HOLSTER_DURABILITY_FACTOR;
        }

        usages *= RingOfSharpshooting.durabilityMultiplier( Dungeon.hero );

        //at 100 uses, items just last forever.
        if (usages >= 100f) return 0;

        usages = Math.round(usages);

        //add a tiny amount to account for rounding error for calculations like 1/3
        return (MAX_DURABILITY/usages) + 0.001f;
    }


    @Override
    public int min(int lvl) {
        return 8+2*lvl;
    }

    @Override
    public int max(int lvl) {
        return 18 + 4*lvl;
    }

    @Override
    protected void onThrow(int cell) {
        if (Dungeon.level.pit[cell]){
            super.onThrow(cell);
            return;
        }

        rangedHit( null, cell );
        Dungeon.level.pressCell(cell);
        ArrayList<Char> targets = new ArrayList<>();
        if (Actor.findChar(cell) != null) targets.add(Actor.findChar(cell));

        for (int i : PathFinder.NEIGHBOURS5){
            if (Actor.findChar(cell + i) != null){
                Buff.prolong( Actor.findChar(cell + i), Blindness.class, 4+(level()/4f) );
            }
            GameScene.flash(0x80808080);
        }


        for (Char target : targets){
            curUser.shoot(target, this);
            if (target == Dungeon.hero && !target.isAlive()) {
                Badges.validateDeathFromFriendlyMagic();
                Dungeon.fail(this);
                GLog.n(Messages.get(this, "ondeath"));
            }
        }
    }

    @Override
    protected void rangedHit(Char enemy, int cell) {

        super.rangedHit( enemy, cell);

        //need to spawn a dart
        if (durability <= 0){
            //attempt to stick the dart to the enemy, just drop it if we can't.
            Dart d = new Dart();
            if (sticky && enemy != null && enemy.isAlive() && enemy.alignment != Char.Alignment.ALLY){
                PinCushion p = Buff.affect(enemy, PinCushion.class);
                if (p.target == enemy){
                    p.stick(d);
                    return;
                }
            }
        }
    }


}
