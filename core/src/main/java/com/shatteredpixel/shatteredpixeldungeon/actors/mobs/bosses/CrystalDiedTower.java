package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SpellCaster;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireCrystalSprites;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class CrystalDiedTower extends Mob {

    {
        spriteClass = FireCrystalSprites.class;

        HP = HT = 1;

        properties.add(Property.MINIBOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.ELECTRIC);
        properties.add(Property.IMMOVABLE);

        state = HUNTING;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public void add( Buff buff ) {
    }

    protected enum State{
        PREPARING, AIMING, SHOOTING
    }

    protected int count=0;
    protected State countDown(){
        if(maxCount() - count > 0 && maxCount() - count < 3);
        if(count >= maxCount()){
            count = Random.Int(0, 3) - 3; return State.SHOOTING;
        }else if(count == maxCount() - 1){
            count ++;
            sprite.showStatus( CharSprite.NEGATIVE, "!!!" );
            return State.AIMING;
        }else{
            count ++;
            return State.PREPARING;
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
        State s = countDown();
        if(s == State.SHOOTING) {
            zapProc();
        }else{
            spend(TICK);
        }
        return true;
    }

}