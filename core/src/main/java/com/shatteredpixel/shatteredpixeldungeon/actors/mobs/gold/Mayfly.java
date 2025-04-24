package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Mayfly extends GoldMob {
    {
        spriteClass = MayFlySprite.class;

        HP = HT = 12;
        defenseSkill = 4;

        EXP = 2;
        maxLvl = 8;

        baseSpeed = 0.5f;

        flying = true;

        loot = Generator.Category.SEED;
        lootChance = 0.5f;
    }
    private ArrayList<Emitter> HealingPos;
    private boolean isAlone = true;


    protected boolean doAttack(Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( pos );
                return false;
            } else {
                zap();
                return true;
            }
        }
    }
    private void zap() {
        spend( 1f );
        Invisibility.dispel(this);
    }

    public void onZapComplete() {
        zap();
        next();
    }

    public int damageRoll() {
        return Random.NormalIntRange( 0, 0 );
    }

    public int HealRoll(){
        return Random.NormalIntRange(2,4);
    }

    @Override
    protected boolean act() {
        Buff giantBuff = buff(ChampionEnemy.Giant.class);
        if(giantBuff != null){
            giantBuff.detach();
        }
        boolean isAct = super.act();
        if(this.HealingPos != null){
            for (Emitter e : HealingPos){
                e.on = false;
            }
        }
        if(isAlone && HP < HT){
            int Heal = Random.NormalIntRange(0,1);
            this.HP += Heal;
            this.HP = Math.min(HP, HT);
            this.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
            if(Heal>0)
                this.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", Heal);
        } else if (this.buff(Corruption.class)!=null || this.buff(ScrollOfSirensSong.Enthralled.class)!= null) {
            if(Dungeon.hero != null){
                if(isInRange(Dungeon.hero.pos) && fieldOfView[Dungeon.hero.pos]){
                    int healthHalo = HealRoll();
                    Dungeon.hero.HP += healthHalo;
                    Dungeon.hero.HP = Math.min(Dungeon.hero.HP, Dungeon.hero.HT);
                    Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), healthHalo);
                    Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", healthHalo);
                    if (sprite != null && (sprite.visible)) {
                        sprite.zap(Dungeon.hero.pos);
                    }
                }
            }
        } else{
            Set<Mob> mobs = Collections.synchronizedSet(Dungeon.level.mobs);
            for(Mob mob:mobs){
                if(isInRange(mob.pos) && mob != this && mob.alignment == Alignment.ENEMY && fieldOfView[mob.pos] && state == HUNTING){
                    int healthHalo = HealRoll();
                    mob.HP += healthHalo;
                    mob.HP = Math.min(mob.HP, mob.HT);
                    mob.sprite.emitter().burst(Speck.factory(Speck.HEALING), healthHalo);
                    mob.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", healthHalo);
                    if (sprite != null && (sprite.visible)) {
                        sprite.zap(mob.pos);
                    }
                    break;
                }
            }
        }
        return isAct;
    }
    @Override
    public int attackSkill( Char target ) {
        return 8;
    }
    
    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING && Dungeon.level.distance(pos,enemy.pos)<=2) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    public void aggro(Char ch) {
        if (ch == null || fieldOfView == null || fieldOfView[ch.pos]) {
            super.aggro(ch);
        }
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        for(Mob mob:Dungeon.level.mobs){
            if(isInRange(mob.pos) && mob != this){
                isAlone = false;
                break;
            }
            isAlone = true;
        }
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT);
        return !isAlone && isInRange(enemy.pos) && attack.collisionPos == enemy.pos && !Dungeon.level.adjacent(pos,enemy.pos);
    }

    public int attackProc( Char enemy, int damage ) {
        if(this.HealingPos != null){
            for (Emitter e : HealingPos){
                e.on = false;
            }
        }
        HealingPos = new ArrayList<Emitter>();
        for (int i = 0; i < Dungeon.level.length(); i++){
            if (!Dungeon.level.solid[i] && Dungeon.level.distance(pos,i) <= 3) {
                Emitter e = CellEmitter.get(i);
//                e.pour(LeafParticle.LEVEL_SPECIFIC, 1f);
                HealingPos.add(e);
            }
        }
        return damage;
    }
    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    private boolean isInRange(int pos){
        return Dungeon.level.distance(this.pos,pos)<=3;
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        if(this.HealingPos != null){
            for (Emitter e : HealingPos){
                e.on = false;
            }
        }
    }
}
