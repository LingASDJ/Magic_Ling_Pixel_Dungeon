package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class DiamondKnight extends Boss  {
    private int ventCooldown = 0;
    private int phase = 1;

    private final int OneHP = 179;
    private final int TwoHP = 99;
    private final int ThreeHP = 49;

    private static final float TIME_TO_ZAP	= 5f;

    {
        spriteClass = DimandKingSprite.class;

        initProperty();
        initBaseStatus(14, 23, 33, 22, 420, 5, 12);
        initStatus(80);
        
        maxLvl = 30;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
    }

    private static final String PHASE = "phase";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, phase);
        bundle.put(VENT_COOLDOWN, ventCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt(PHASE);
        ventCooldown = bundle.getInt( VENT_COOLDOWN );
    }

    private static final float ATTACSWORD_DELAY	= 5f;

    private static final String VENT_COOLDOWN = "vent_cooldown";

    @Override
    protected boolean act() {
    switch (phase){
        case 1:
           if(Random.Float()>0.64f){

           } else {
               super.act();
           }
        break;
        case 2:
            break;
        case 3:
            break;
        case 4:
            break;
    }

        return super.act();
    }
    public static class DarkBolt{}

    //the actual affected cells
    private HashSet<Integer> affectedCells;
    //the cells to trace fire shots to, for visual effects.
    private HashSet<Integer> visualCells;
    private int direction = 0;
    private int left(int direction){
        return direction == 0 ? 2 : direction-1;
    }

    protected void fx(Ballistica bolt, Callback callback, Char ch ) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.
        affectedCells = new HashSet<>();
        visualCells = new HashSet<>();

        int maxDist = 4 + 4*4;
        int dist = Math.min(bolt.dist, maxDist);

        for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
            if (bolt.sourcePos+PathFinder.CIRCLE8[i] == bolt.path.get(1)){
                direction = i;
                break;
            }
        }

        float strength = maxDist;
        for (int c : bolt.subPath(1, dist)) {
            strength--; //as we start at dist 1, not 0.
            affectedCells.add(c);
            if (strength > 1) {
                spreadFlames(c + PathFinder.CIRCLE8[direction], strength - 1);
            } else {
                visualCells.add(c);
            }
        }

        //going to call this one manually
        visualCells.remove(bolt.path.get(dist));

        for (int cell : visualCells){
            //this way we only get the cells at the tip, much better performance.
            ((MagicMissile)ch.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.FIRE,
                    ch.sprite,
                    cell,
                    null
            );
        }
        MagicMissile.boltFromChar( ch.sprite.parent,
                MagicMissile.FIRE,
                ch.sprite,
                bolt.path.get(dist/2),
                callback );
        if(Dungeon.level.heroFOV[bolt.sourcePos] || Dungeon.level.heroFOV[bolt.collisionPos]){
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
        }
    }

    private int right(int direction){
        return direction == 2 ? 0 : direction+1;
    }
    private void spreadFlames(int cell, float strength){
        if (strength >= 0 && (Dungeon.level.passable[cell] || Dungeon.level.flamable[cell])){
            affectedCells.add(cell);
            if (strength >= 1.5f) {
                visualCells.remove(cell);
                spreadFlames(cell + PathFinder.CIRCLE8[direction], strength - 1.5f);
            } else {
                visualCells.add(cell);
            }
        } else if (!Dungeon.level.passable[cell])
            visualCells.add(cell);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10,20 );
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.distance(pos, enemy.pos) <= 1) {

            return super.doAttack(enemy);

        } else {

            boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
            if (visible) {
                sprite.attack(enemy.pos);
                spend(TIME_TO_ZAP);
                shoot(this, enemy.pos);
            }

            return !visible;
        }
    }

    public void damage(int dmg, Object src) {
        if (!Dungeon.level.mobs.contains(this)){
            return;
        }

        ColdChestBossLevel.State state = ((ColdChestBossLevel)Dungeon.level).pro();

        int hpBracket = HT / 8;

        int beforeHitHP = HP;
        super.damage(dmg, src);
        dmg = beforeHitHP - HP;

        if ((beforeHitHP/hpBracket - HP/hpBracket) >= 2){
            HP = hpBracket * ((beforeHitHP/hpBracket)-1) + 1;
        }

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = state == ColdChestBossLevel.State.MAZE_START ? 1 : 4;
            lock.addTime(dmg*multiple);
        }

        if (HP == 0 && state == ColdChestBossLevel.State.VSBOSS_START) {
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);
                    ((ColdChestBossLevel)Dungeon.level).progress();
                    return true;
                }
            });
            return;
        }

        if (state == ColdChestBossLevel.State.MAZE_START && HP <= OneHP) {
            HP = OneHP;
            yell(Messages.get(this, "interesting"));
            ((ColdChestBossLevel) Dungeon.level).progress();
            BossHealthBar.bleed(true);
        }
    }

    protected void onZap( Ballistica bolt ) {

        for( int cell : affectedCells){

            //ignore caster cell
            if (cell == bolt.sourcePos){
                continue;
            }

            //only ignite cells directly near caster if they are flammable
            if (!Dungeon.level.adjacent(bolt.sourcePos, cell)
                    || Dungeon.level.flamable[cell]){
                //GameScene.add( Blob.seed( cell, 1+2, Fire.class ) );
            }
        }
    }

    public void shoot(Char ch, int pos){
        final Ballistica shot = new Ballistica( ch.pos, pos, Ballistica.PROJECTILE);
        fx(shot, new Callback() {
            @Override
            public void call() {
                onZap(shot);
            }
        }, ch);
    }

}
