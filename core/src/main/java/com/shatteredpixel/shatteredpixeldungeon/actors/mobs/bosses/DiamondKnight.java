/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdMagicRat;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class DiamondKnight extends Boss {
    private static final float TIME_TO_ZAP	= 3f;
    {
        initProperty();
        initBaseStatus(14, 23, 33, 22, 420, 5, 12);
        initStatus(80);
        EXP = 10;
        defenseSkill = 8;
        spriteClass = DimandKingSprite.class;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        properties.add(Property.ACIDIC);
    }

    private int pumpedUp = 0;
    private int phase;
    private int healInc = 1;

    @Override
    public int damageRoll() {
        int min = 1;
        int max = (HP*2 <= HT) ? 12 : 8;
        if (pumpedUp > 0) {
            pumpedUp = 0;
            return Random.NormalIntRange( min*3, max*3 );
        } else {
            return Random.NormalIntRange( min, max );
        }
    }

    @Override
    public int attackSkill( Char target ) {
        int attack = 10;
        if (HP*2 <= HT) attack = 15;
        if (pumpedUp > 0) attack *= 2;
        return attack;
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return this.HP==360;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return (int)(super.defenseSkill(enemy) * ((HP*2 <= HT)? 1.5 : 1));
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    @Override
    public boolean act() {

        if (Dungeon.level.water[pos] && HP < HT) {
            HP += healInc;

            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null) lock.removeTime(healInc*2);

            if (Dungeon.level.heroFOV[pos] ){
                sprite.emitter().burst( Speck.factory( Speck.HEALING ), healInc );
            }
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES) && healInc < 3) {
                healInc++;
            }
            if (HP*2 > HT) {
                BossHealthBar.bleed(false);
                HP = Math.min(HP, HT);
            }
        } else {
            healInc = 1;
        }

        if (state != SLEEPING){
            Dungeon.level.seal();
        }

        return super.act();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (pumpedUp > 0){
            //we check both from and to in this case as projectile logic isn't always symmetrical.
            //this helps trim out BS edge-cases
            return Dungeon.level.distance(enemy.pos, pos) <= 2
                    && new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos
                    && new Ballistica( enemy.pos, pos, Ballistica.PROJECTILE).collisionPos == pos;
        } else {
            return super.canAttack(enemy);
        }
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (Random.Int( 3 ) == 0) {
            Buff.affect( enemy, Ooze.class ).set( Ooze.DURATION );
            enemy.sprite.burst( 0x000000, 5 );
        }

        if (pumpedUp > 0) {
            Camera.main.shake( 3, 0.2f );
        }

        return damage;
    }

    @Override
    public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
        boolean result = super.attack( enemy, dmgMulti, dmgBonus, accMulti );
        pumpedUp = 0;
        return result;
    }

    @Override
    protected boolean getCloser( int target ) {
        if (pumpedUp != 0) {
            pumpedUp = 0;
            sprite.idle();
        }
        return super.getCloser( target );
    }

    @Override
    public void damage(int dmg, Object src) {
        if (!BossHealthBar.isAssigned()){
            BossHealthBar.assignBoss( this );
            Dungeon.level.seal();
        }

        super.damage(dmg, src);
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg*2);

        ColdChestBossLevel.State level = ((ColdChestBossLevel)Dungeon.level).pro();
        //血量低于360后追加phase并加载楼层的进度方法
        if (level == ColdChestBossLevel.State.START && this.HP < 360 && phase == 0) {
            GLog.n("让这场游戏变得更加有趣吧");
            GameScene.flash(0x808080);
            ((ColdChestBossLevel)Dungeon.level).progress();
            phase++;
        } else if(level == ColdChestBossLevel.State.MAZE_START && this.HP < 360 && phase == 1) {
            GLog.n("让这场游戏变得更加有d趣吧");
        }

    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        if(Dungeon.depth!=28){
            Dungeon.level.unseal();

            GetBossLoot();

            GameScene.bossSlain();
            Dungeon.level.drop( new SkeletonKey( Dungeon.depth ), pos ).sprite.drop();

            //60% chance of 2 blobs, 30% chance of 3, 10% chance for 4. Average of 2.5
            int blobs = Random.chances(new float[]{0, 0, 6, 3, 1});
            for (int i = 0; i < blobs; i++){
                int ofs;
                do {
                    ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
                } while (!Dungeon.level.passable[pos + ofs]);
                Dungeon.level.drop( new GooBlob(), pos + ofs ).sprite.drop( pos );
            }

            Badges.validateBossSlain();

            yell( Messages.get(this, "defeated") );
        }

    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Dungeon.level.seal();
            yell(Messages.get(this, "notice"));
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    private final String PUMPEDUP = "pumpedup";
    private final String HEALINC = "healinc";
    private static final String PHASE       ="dimandphase";
    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );
        phase = bundle.getInt(PHASE);
        bundle.put( PUMPEDUP , pumpedUp );
        bundle.put( HEALINC, healInc );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );
        bundle.put(PHASE,phase);
        pumpedUp = bundle.getInt( PUMPEDUP );
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);

        //if check is for pre-0.9.3 saves
        healInc = bundle.getInt(HEALINC);

    }

    private void zap() {
        spend( TIME_TO_ZAP );

        if (hit( this, enemy, true )) {
            //TODO would be nice for this to work on ghost/statues too
            if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
                Buff.prolong( enemy, Blindness.class, Degrade.DURATION );
                Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
            }

            int dmg = Random.NormalIntRange( 10, 12 );
            enemy.damage( dmg, new ColdMagicRat.DarkBolt() );

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "frost_kill") );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

}
