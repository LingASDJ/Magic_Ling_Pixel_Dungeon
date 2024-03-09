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

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.STRONGER_BOSSES;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Level.set;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdMagicRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DiamondKnight extends Boss implements Hero.Doom {
    public int armTier;
    private static final float TIME_TO_ZAP	= 3f;
    {
        initProperty();
        initBaseStatus(14, 23, 33, 22, 420, 5, 12);
        initStatus(80);
        EXP = 10;
        defenseSkill = 8;

        flying=true;

        if(Dungeon.isChallenged(STRONGER_BOSSES)){
            viewDistance = 24;
        }

        spriteClass = DimandKingSprite.class;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        properties.add(Property.ACIDIC);
    }


    public String info(){
        StringBuilder desc = new StringBuilder(super.info());

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof TPDoor) {
                desc.append("\n\n").append(Messages.get(this, "TPDoor"));
            }
        }

        return desc.toString();
    }

    private int pumpedUp = 0;

    public int phase;
    private int healInc = 1;

    @Override
    public int damageRoll() {
        int min = 8;
        int max = (HP*2 <= HT) ?  14 : 9;

        if(Dungeon.isChallenged(STRONGER_BOSSES)){
            min = 8 + Statistics.dimandchestmazeCollected * 2 + 2;
            max = (HP*2 <= HT) ?  14 : 9 + Statistics.dimandchestmazeCollected * 3 + 3;
        }

        //模仿玩家的伤害
        ColdChestBossLevel.State level = ((ColdChestBossLevel)Dungeon.level).pro();
        if (level == ColdChestBossLevel.State.VSYOU_START){
            return hero.damageRoll();
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

    /**
     * 无敌判定
     * @param effect 无敌效果
     * @return true:无敌
     */
    @Override
    public boolean isInvulnerable(Class effect) {
        return (this.HP>=301 && this.HP<=360) && effect != DiamondKnight.DiedDamager.class && !Statistics.TPDoorDieds;
    }

    public static class DiedDamager extends Buff {

        @Override
        public boolean act() {
            detach();
            spend( TICK );
            return true;
        }

        @Override
        public void detach() {
            super.detach();
            for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
                if (m instanceof DiamondKnight){
                    m.damage(12, this);
                }
            }
        }
    }

    @Override
    public int defenseSkill(Char enemy) {
        return (int)(super.defenseSkill(enemy) * ((HP*2 <= HT)? 1.5 : 1));
    }

    @Override
    public int drRoll() {
        int dr = hero.drRoll() == 0 ? 7 : hero.drRoll();
        //模仿玩家的伤害
        ColdChestBossLevel.State level = ((ColdChestBossLevel)Dungeon.level).pro();
        if (level == ColdChestBossLevel.State.VSYOU_START && Dungeon.isChallenged(STRONGER_BOSSES)){
            if (hero.belongings.armor() != null) {
                dr = Random.NormalIntRange(hero.belongings.armor().DRMin(), hero.belongings.armor().DRMax());
            }
            return dr/3;
        } else {
            return Random.NormalIntRange(4,8);
        }


    }



    /**
     * 判定是否可以攻击
     * @param enemy 目标
     * @return true:可以攻击
     */
    @Override
    protected boolean canAttack( Char enemy ) {
        if ( Dungeon.level.distance(enemy.pos, pos) >= 2){
            //we check both from and to in this case as projectile logic isn't always symmetrical.
            //this helps trim out BS edge-cases
            return Dungeon.level.distance(enemy.pos, pos) <= 2
                    && new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos
                    && new Ballistica( enemy.pos, pos, Ballistica.PROJECTILE).collisionPos == pos;
        } else {
            return super.canAttack(enemy);
        }
    }

    private int combo = 0;
    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        //The gnoll's attacks get more severe the more the player lets it hit them
        combo++;
        int effect = Random.Int(5)+combo;

        if (effect > 2) {

            if (effect >=6 && enemy.buff(Burning.class) == null){

                if (Dungeon.level.flamable[enemy.pos])
                    GameScene.add(Blob.seed(enemy.pos, 4, Fire.class));
                Buff.affect(enemy, Burning.class).reignite( enemy );

            } else
                Buff.affect( enemy, Poison.class).set((effect-2) );
        }
        return damage;
    }


//    @Override
//    protected boolean getCloser(int target) {
//        combo = 0;
//        if (state == HUNTING) {
//            if(Dungeon.level.distance(pos,target)>1)
//                return super.getCloser( target );
//            return enemySeen && getFurther( target );
//        } else {
//            return super.getCloser(target);
//        }
//        //return false;
//    }

    /**
     * @param dmg 伤害
     * @param src 伤害来源
     */
    @Override
    public void damage(int dmg, Object src) {
        if (!BossHealthBar.isAssigned()){
            BossHealthBar.assignBoss( this );
            Dungeon.level.seal();
        }

        int hpBracket = HT / 6;

        dmg = Math.min(hpBracket, dmg);

        super.damage(dmg, src);
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg*2);

        ColdChestBossLevel.State level = ((ColdChestBossLevel)Dungeon.level).pro();
         //血量低于360后追加phase并加载楼层的进度方法,加载迷宫
        if (level == ColdChestBossLevel.State.START && this.HP <= 360 && phase == 0) {
            HP = 360;
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);
                    ((ColdChestBossLevel)Dungeon.level).progress();
                    phase++;

                    if(Dungeon.isChallenged(STRONGER_BOSSES)){
                        yell(Messages.get(DiamondKnight.class,"now_go_2"));
                    } else {
                        yell(Messages.get(DiamondKnight.class,"now_go"));
                    }


                    GameScene.flash(0x808080);
                    return true;
                }
            });
        } else if (level == ColdChestBossLevel.State.VSBOSS_START && this.HP <= 240 && phase == 1) {
            HP = 240;
            ((ColdChestBossLevel)Dungeon.level).progress();
            phase++;
            //血量低于200后变成玩家的样子，伤害和防御数值与玩家一致
        } else if (level == ColdChestBossLevel.State.VSLINK_START && this.HP <= 200 && phase == 2) {
            HP = 200;
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);
                    ((ColdChestBossLevel)Dungeon.level).progress();
                    phase++;
                    GLog.n(Messages.get(DiamondKnight.class,"iswar_go"));
                    GameScene.flash(0x808080);
                    ((ColdChestBossLevel)Dungeon.level).progress();
                    spriteClass=DimandKingSprite.PrismaticSprite.class;
                    ShatteredPixelDungeon.resetScene();
                    GameScene.flash(0x888888);
                    return true;
                }
            });
            //最终决斗
        } else if (level == ColdChestBossLevel.State.VSYOU_START && this.HP <= 100 && phase == 3) {
            HP = 100;
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);
                    ((ColdChestBossLevel)Dungeon.level).progress();
                    GLog.n(Messages.get(DiamondKnight.class,"ok_go"));
                    GameScene.flash(0x808080);
                    spriteClass=DimandKingSprite.class;
                    ShatteredPixelDungeon.resetScene();
                    GameScene.flash(0x888888);
                    ((ColdChestBossLevel)Dungeon.level).progress();
                    phase++;
                    return true;
                }
            });
        }

    }

    @Override
    public void onDeath() {
        Dungeon.fail( getClass() );
        yell( Messages.get(this, "ondeath") );
    }


    @Override
    public void die( Object cause ) {

        super.die( cause );
        //酸液体清0
        Statistics.SiderLing = 0;
        Dungeon.level.unseal();
        Statistics.bossScores[1] += 2500;

        if(!Statistics.bossRushMode){
            Dungeon.level.drop( new TengusMask(), pos ).sprite.drop();
        }

        int dropPos = this.pos;
        for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (boss instanceof DCrystal) {
                boss.die(true);
            }
        }

        if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
            Dungeon.level.drop(new IceCyanBlueSquareCoin(20),hero.pos);
        }

        Dungeon.level.drop(new ScrollOfRecharging().quantity(2),  dropPos).sprite.drop(pos);

        Ankh ankh = new Ankh();
        ankh.bless();

        Dungeon.level.drop(new Ankh(), dropPos).sprite.drop(pos);

        Dungeon.level.drop(new Gold().quantity(Random.Int(400,900)), pos).sprite.drop();

        Badges.KILL_SMK();

        if(Statistics.dimandchestmazeCollected>=3){
            PaswordBadges.validateOMP();
            Statistics.bossScores[1] += 1000;
        }
        Badges.validateBossSlain();
        phase++;

//        if (!Badges.isUnlocked(Badges.Badge.KILL_SM)){
//            //Dungeon.level.drop( new SDBSword(), pos ).sprite.drop();
//        } else if (Random.Float()<0.43f) {
//            //Dungeon.level.drop( new SDBSword(), pos ).sprite.drop();
//        } else {
            Dungeon.level.drop( new PotionOfExperience(), pos ).sprite.drop();
//        }

        GameScene.bossSlain();

        //入口
        set( 647, Terrain.EXIT );
        GameScene.updateMap( 647 );

        //出口
        set( 52, Terrain.ENTRANCE );
        GameScene.updateMap( 52 );

        if(Dungeon.isChallenged(STRONGER_BOSSES)){
            yell( Messages.get(this, "defeated_2") );
        } else {
            yell( Messages.get(this, "defeated") );
        }


    }

    @Override
    public void notice() {
        super.notice();

        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Dungeon.level.seal();
            GameScene.bossReady();
            if(Dungeon.isChallenged(STRONGER_BOSSES)){
                yell(Messages.get(this, "notice_2"));
            } else {
                yell(Messages.get(this, "notice"));
            }

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

    private static final String COMBO = "combo";
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put(PHASE,phase);

        bundle.put( PUMPEDUP , pumpedUp );
        bundle.put( HEALINC, healInc );
        bundle.put(COMBO, combo);

//        if(phase == 5) {
//            spriteClass=DimandKingSprite.PrismaticSprite.class;
//        } else {
//            spriteClass=DimandKingSprite.class;
//        }

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        phase = bundle.getInt(PHASE);
        pumpedUp = bundle.getInt( PUMPEDUP );
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);

        //if check is for pre-0.9.3 saves
        healInc = bundle.getInt(HEALINC);

        if(phase == 5 || phase == 6) {
            spriteClass=DimandKingSprite.PrismaticSprite.class;
        } else {
            spriteClass=DimandKingSprite.class;
        }

        combo = bundle.getInt( COMBO );

    }

    private void zap() {
        spend( TIME_TO_ZAP );

        if (hit( this, enemy, true )) {
            int dmg = Random.NormalIntRange( 20, 30 );
            enemy.damage( dmg, new ColdMagicRat.DarkBolt() );
            if(Random.Float()<0.45f){
                if(Dungeon.isChallenged(STRONGER_BOSSES)){
                    Buff.affect( enemy, Bleeding.class ).set( 9*Statistics.dimandchestmazeCollected*1.4f );
                } else {
                    Buff.affect( enemy, Bleeding.class ).set( 9 );
                }

                Sample.INSTANCE.play( Assets.Sounds.CURSED );
                Statistics.bossScores[1] -= 300;
            }
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

}
