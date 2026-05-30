package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Seekingspear extends MeleeWeapon{
    //private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0x660022 );
    {
        image = ItemSpriteSheet.SEEKSXS;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 0.9f;
        tier = 4;
        RCH= 2;
    }
    @Override
    public int min(int lvl) {
        return  4+ lvl * 2;
    }
    @Override
    public int max(int lvl) {
        return  16 + lvl * 4;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        int dmg;

        if (attacker instanceof Hero) {
            Hero hero = (Hero) attacker;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                dmg = (int) ((damage * 1.5f) + (0.25f * level()));
                damage = dmg;
                attacker.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "crit"));
            }
        } else if(Random.Float()<=0.43f){
            dmg = (int) ((damage * 1.5f) + (0.25f * level()));
            damage = dmg;
            attacker.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "crit"));
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    public Emitter emitter() {
        Emitter emitter = new Emitter();
        emitter.pos(4.5f, 6);
        emitter.fillTarget = false;
        emitter.pour(StaffParticleFactory, 0.6f);//动画内容，动画间隔（越大播放速度越慢）
        //pour 有间隔无量，超过间隔会结束播放，burst为瞬间爆发，有量无间隔，也就是瞬间爆发直到量完
        return emitter;
    }

    private final Emitter.Factory StaffParticleFactory = new Emitter.Factory() {
        /**
         * @param emitter 目标来源
         * @param index 特效来源
         * @param x,y 位置
         */
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            StaffParticle c = (StaffParticle)emitter.getFirstAvailable(StaffParticle.class);
            if (c == null) {
                c = new StaffParticle();
                emitter.add(c);
            }
            c.reset(x, y);
        }
        @Override
        public boolean lightMode() {
            return false;
        }
    };


    private class StaffParticle extends PixelParticle {

        private float minSize;
        private float maxSize;
        public float sizeJitter = 0;

        public StaffParticle(){
            super();
        }

        public void reset( float x, float y ) {
            revive();

            speed.set(0);

            this.x = x;
            this.y = y;
            staffFx( this );

        }
        private void staffFx(StaffParticle particle) {
            particle.color( 0x6E1B1B );//改颜色
            particle.am = 0.8f;//不透明度
            particle.setLifespan(2f);//影响大小，相当于影响衰减完所需全程
            particle.speed.set(0, 8);//粒子运动速度
            particle.setSize( 0.5f, 2f);//设置粒子的最小/大尺寸
            particle.shuffleXY(1f);//随机调节起始点，以原点为中心在一个半边长为amt的正方形里
        }
        public void setSize( float minSize, float maxSize ){
            this.minSize = minSize;
            this.maxSize = maxSize;
        }

        public void setLifespan( float life ){
            lifespan = left = life;
        }

        public void shuffleXY(float amt){
            x += Random.Float(-amt, amt);
            y += Random.Float(-amt, amt);
        }

        public void radiateXY(float amt){
            float hypot = (float)Math.hypot(speed.x, speed.y);
            this.x += speed.x/hypot*amt;
            this.y += speed.y/hypot*amt;
        }

        @Override
        public void update() {
            super.update();
            size(minSize + (left / lifespan)*(maxSize-minSize) + Random.Float(sizeJitter));
        }
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {

        ArrayList<Char> targets = new ArrayList<>();
        Char closest = null;

        hero.belongings.abilityWeapon = this;
        for (Char ch : Actor.chars()){
            if (ch.alignment == Char.Alignment.ENEMY
                    && !hero.isCharmedBy(ch)
                    && Dungeon.level.heroFOV[ch.pos]
                    && hero.canAttack(ch)){
                targets.add(ch);
                if (closest == null || Dungeon.level.trueDistance(hero.pos, closest.pos) > Dungeon.level.trueDistance(hero.pos, ch.pos)){
                    closest = ch;
                }
            }
        }
        hero.belongings.abilityWeapon = null;

        if (targets.isEmpty()) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        throwSound();
        Char finalClosest = closest;
        hero.sprite.attack(hero.pos, new Callback() {
            @Override
            public void call() {
                beforeAbilityUsed(hero, finalClosest);
                for (Char ch : targets) {
                    //ability does no extra damage
                    hero.attack(ch, 1, 0, Char.INFINITE_ACCURACY);
                    if (!ch.isAlive()){
                        hero.sprite.parent.add(new Chains(hero.sprite.center(), ch.sprite.destinationCenter(),
                                Effects.Type.P_CHAIN,
                                new Callback() {
                                    public void call() {
                                        onAbilityKill(hero, ch);
                                    }
                                }));
                    }
                }
                Invisibility.dispel();
                hero.spendAndNext(hero.attackDelay());
                afterAbilityUsed(hero);
            }
        });
    }

    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()), augment.damageFactor(max()));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0), max(0));
        }
    }

    public String upgradeAbilityStat(int level){
        return augment.damageFactor(min(level)) + "-" + augment.damageFactor(max(level));
    }

}
