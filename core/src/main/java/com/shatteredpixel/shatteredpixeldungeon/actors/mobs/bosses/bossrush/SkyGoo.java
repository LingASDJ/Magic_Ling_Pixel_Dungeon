package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.bossrush;

import static com.shatteredpixel.shatteredpixeldungeon.BGMPlayer.playBGM;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.levels.nosync.SkyGooBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Iterator;

@SuppressWarnings("all")
public class SkyGoo extends Mob implements Callback {
    private final String PUMPEDUP;
    private int pumpedUp;
    private int var2;
    public static class LightningBolt {}

    public SkyGoo() {
        this.HT = 300;
        this.HP = 300;
        this.EXP = 10;
        this.defenseSkill = 12;
        this.spriteClass = GooSprite.class;
        this.properties.add(Char.Property.BOSS);
        this.properties.add(Char.Property.DEMONIC);
        this.properties.add(Char.Property.ACIDIC);
        this.pumpedUp = 0;
        this.PUMPEDUP = "pumpedup";
    }

    private final int healInc = 1;

    @Override
    public int damageRoll() {
        int min = 1;
        int max = (HP*2 <= HT) ? 16 : 10;
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
    public int defenseSkill(Char enemy) {
        return (int)(super.defenseSkill(enemy) * ((HP*2 <= HT)? 1.5 : 1));
    }

    @Override // com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob, com.shatteredpixel.shatteredpixeldungeon.actors.Char, com.shatteredpixel.shatteredpixeldungeon.actors.Actor
    public boolean act() {
        if (Dungeon.level.water[this.pos] && this.HP < this.HT) {
            if (Dungeon.level.heroFOV[this.pos]) {
                this.sprite.emitter().burst(Speck.factory(0), 1);
            }
            if (this.HP * 2 == this.HT) {
                BossHealthBar.bleed(false);
                ((GooSprite) this.sprite).spray(false);
            }
            this.HP++;
        }
        if (this.state != this.SLEEPING) {
            Dungeon.level.seal();
        }
        return super.act();
    }

    @Override // com.watabou.utils.Callback
    public void call() {
    }

    @Override // com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob
    public boolean canAttack(Char enemy) {
        if (Dungeon.level.distance(this.pos, enemy.pos) <= 1) {
            return this.pumpedUp > 0 ? distance(enemy) <= 2 : super.canAttack(enemy);
        }
        spend(1.0f);
        if (hit(this, enemy, true)) {
            int dmg = Random.NormalIntRange(3, 6);
            enemy.damage(dmg, new LightningBolt());
            if (enemy.sprite.visible) {
                enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
                enemy.sprite.flash();
            }
            if (enemy == Dungeon.hero) {
                Camera.main.shake(2.0f, 0.3f);
                if (!enemy.isAlive()) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(this, "zap_kill"));
                }
            }
        } else {
            enemy.sprite.showStatus(16776960, enemy.defenseVerb());
        }
        if (this.sprite == null || !(this.sprite.visible || enemy.sprite.visible)) {
            return true;
        }
        this.sprite.zap(enemy.pos);
        return false;
    }

    @Override // com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob, com.shatteredpixel.shatteredpixeldungeon.actors.Char
    public int attackProc(Char enemy, int damage) {
        int damage2 = super.attackProc(enemy, damage);
        if (Random.Int(3) == 0) {
            ((Ooze) Buff.affect(enemy, Ooze.class)).set(20.0f);
            ((Poison) Buff.affect(enemy, Poison.class)).set(10.0f);
            enemy.sprite.burst(61166, 9);
        } else {
            ((Blindness) Buff.affect(enemy, Blindness.class)).set(10.0f);
            enemy.sprite.burst(15597568, 9);
        }
        if (this.pumpedUp > 0) {
            Camera.main.shake(3.0f, 0.2f);
        }
        int i = this.var2;
        if (Random.Int(3) == 0) {
            int i2 = this.var2 + 10;
            SummoningTrap one = new SummoningTrap();
            one.pos = this.pos;
            one.activate();
            AlarmTrap two = new AlarmTrap();
            two.pos = this.pos;
            two.activate();
            yell(Messages.get(this, "sr"));
        }
        int reg = Math.min(2, this.HT - this.HP);
        if (reg > 0) {
            this.HP += reg;
            this.sprite.emitter().burst(Speck.factory(0), 1);
        }
        yell(Messages.get(this, "ss"));
        return damage2;
    }

    @Override // com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob, com.shatteredpixel.shatteredpixeldungeon.actors.Char
    public void updateSpriteState() {
        super.updateSpriteState();
        if (this.pumpedUp > 0) {
            ((GooSprite) this.sprite).pumpUp(this.pumpedUp);
        }
    }

    @Override
    public boolean doAttack(Char enemy) {
        int i = this.pumpedUp;
        if (i == 1) {
            ((GooSprite) this.sprite).pumpUp(2);
            this.pumpedUp++;
            spend(attackDelay());
            return true;
        }
        if (i < 2) {
            if (Random.Int(this.HP * 2 <= this.HT ? 2 : 5) <= 0) {
                this.pumpedUp++;
                ((GooSprite) this.sprite).pumpUp(1);
                if (Dungeon.level.heroFOV[this.pos]) {
                    this.sprite.showStatus(16711680, Messages.get(this, "!!!"));
                    GLog.n(Messages.get(this, "pumpup"));
                }
                spend(attackDelay());
                return true;
            }
        }
        boolean visible = Dungeon.level.heroFOV[this.pos];
        if (visible) {
            if (this.pumpedUp >= 2) {
                ((GooSprite) this.sprite).pumpAttack();
                ((GooSprite) this.sprite).pumpAttack();
            } else {
                this.sprite.attack(enemy.pos);
            }
        } else {
            if (this.pumpedUp >= 2) {
                ((GooSprite) this.sprite).triggerEmitters();
            }
            attack(enemy);
        }
        spend(attackDelay());
        return !visible;
    }

    @Override // com.shatteredpixel.shatteredpixeldungeon.actors.Char
    public boolean attack(Char enemy) {
        boolean result = super.attack(enemy);
        this.pumpedUp = 0;
        return result;
    }

    @Override // com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob
    public boolean getCloser(int target) {
        if (this.pumpedUp != 0) {
            this.pumpedUp = 0;
            this.sprite.idle();
        }
        return super.getCloser(target);
    }

    @Override
    public void damage(int dmg, Object src) {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
        boolean bleeding = this.HP * 2 <= this.HT;
        super.damage(dmg, src);
        if (this.HP * 2 <= this.HT && !bleeding) {
            BossHealthBar.bleed(true);
            this.sprite.showStatus(16711680, Messages.get(this, "enraged"));
            ((GooSprite) this.sprite).spray(true);
            yell(Messages.get(this, "gluuurp"));
        }
        LockedFloor lock = (LockedFloor) Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) {
            lock.addTime(dmg * 2);
        }
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        SkyGooBossLevel level = (SkyGooBossLevel) Dungeon.level;
        level.unseal();
        int blobs = Random.chances(new float[]{0, 0, 6, 3, 1});
        for (int i = 0; i < blobs; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( new GooBlob(), pos + ofs ).sprite.drop( pos );
        }
        playBGM(Assets.BGM_1, true);
        GameScene.bossSlain();
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
            Music.INSTANCE.play(Assets.BGM_BOSSA, true);
            Iterator<Char> it = Actor.chars().iterator();
            while (it.hasNext()) {
                Char ch = it.next();
                if (ch instanceof DriedRose.GhostHero) {
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("pumpedup", this.pumpedUp);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.pumpedUp = bundle.getInt("pumpedup");
        if (this.state != this.SLEEPING) {
            BossHealthBar.assignBoss(this);
        }
        if (this.HP * 2 <= this.HT) {
            BossHealthBar.bleed(true);
        }
    }
}
