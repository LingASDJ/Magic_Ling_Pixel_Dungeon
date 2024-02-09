package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SpellCaster;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.IceStalSprites;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class CrystalLingTower extends Mob {

    {
        spriteClass = IceStalSprites.class;

        HP = HT = 150;

        properties.add(Property.MINIBOSS);

        state = HUNTING;
    }

    protected enum State{
        PREPARING, AIMING, SHOOTING
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 15){
            dmg = 15 + (int)(Math.sqrt(8*(dmg - 4) + 1) - 1)/2;
        }
        super.damage(dmg, src);
    }

    protected int count=0;
    protected CrystalDiedTower.State countDown(){
        if(maxCount() - count > 0 && maxCount() - count < 3);
        if(count >= maxCount()){
            count = Random.Int(0, 3) - 3; return CrystalDiedTower.State.SHOOTING;
        }else if(count == maxCount() - 1){
            count ++;
            sprite.showStatus( CharSprite.NEGATIVE, "!!!" );
            return CrystalDiedTower.State.AIMING;
        }else{
            count ++;
            return CrystalDiedTower.State.PREPARING;
        }
    }

    protected int maxCount(){
        return 6;
    }

    protected boolean findTarget(){
        if(enemy != null){
            //record last pos of enemy. Not update if out of FOV.
            if(enemySeen){
                lastTargeting = enemy.pos;
                return true;
            }else{
                lastTargeting = Dungeon.hero.pos;
            }
        }else{
            lastTargeting = Dungeon.hero.pos;
        }
        return false;
    }

    protected void zapProc() {
        Ballistica ba = new Ballistica(pos, lastTargeting, Ballistica.PROJECTILE);

        sprite.parent.add(new BeamCustom(
                sprite.center(),
                DungeonTilemap.tileCenterToWorld(ba.collisionPos),
                Effects.Type.BLUE_RAY));

        if(ba.collisionPos != lastTargeting) {
            findTarget();
            Buff.affect(this, SpellCaster.HaloFireCaster.BouncePostShoot.class).setTrace(ba.collisionPos, lastTargeting);
        }
        hitProc(ba);
    }

    public static void zapDamage(Char ch, int min, int max, float modifier, Object src){
        int damage = Random.IntRange(min, max);
        if(ch.buff(SpellCaster.Marked.class)!=null){
            damage = Math.round(damage*(2f+modifier*1.15f));
        }
        ch.damage(damage, src);
        if(ch == Dungeon.hero && !ch.isAlive()){
            Dungeon.fail(src.getClass());
        }
    }

    protected void hitProc(Ballistica ba){
        Char ch = findChar(ba.collisionPos);
        if(ch != null){
            ch.sprite.burst( 0xFF99CCFF, 5 );
            if(ch.alignment != Alignment.ENEMY){
                Buff.affect(ch, Chill.class, 5f);
                zapDamage(ch, 8, 12, 0.45f, this);
            }
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("ALIGNMENT", alignment);
        bundle.put("lastPos", lastTargeting);
        bundle.put("countDown", count);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        alignment = bundle.getEnum("ALIGNMENT", Char.Alignment.class);
        lastTargeting = bundle.getInt("lastPos");
        count = bundle.getInt("countDown");
    }

    protected int lastTargeting=-1;
    @Override
    protected boolean act(){
        if(alignment == Alignment.NEUTRAL) return true;
        CrystalDiedTower.State s = countDown();
        if(s == CrystalDiedTower.State.SHOOTING) {
            zapProc();
        }else{
            spend(TICK);
        }
        return true;
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof FireMagicDied) {
                Viscosity.DeferedDamage deferred = Buff.affect( mob, Viscosity.DeferedDamage.class );
                deferred.prolong( HT/4 );
                GLog.n( Messages.get(FireMagicDied.class, "dixsdf" ));
            }
        }
    }

}