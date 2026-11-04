package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostSoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulLossTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Aggregatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroArcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroCavalry;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroPioneer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroScout;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroWarlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Wisp;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LichKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;
public class LichKing extends Boss {

    private boolean warStart = false;


    {
        initProperty();
        initBaseStatus(10, 35, 30, 12, 550, 2, 6);
        initStatus(0);
        spriteClass = LichKingSprite.class;
    }

    @Override
    public boolean act() {
        ensureSoulLossTracker();
        return super.act();
    }

    private void ensureSoulLossTracker() {
        for (Char ch : Actor.chars()) {
            if (ch == this || !ch.isAlive()) continue;
            if (ch instanceof LichKing) continue;
            if (ch.buff(SoulLossTracker.class) == null) {
                Buff.affect(ch, SoulLossTracker.class);
            }
        }
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Dungeon.level.seal();
            Camera.main.shake(1f,3f);
            GameScene.bossReady();
            yell(Messages.get(this, "notice",Dungeon.hero.name()));
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
        if(!warStart){
            summonNeroMobs();
            warStart = true;
        }
    }

    int particles = 2 + (int) Random.Float(2f);

    private void summonNeroMobs(){

        // 每种死灵各 1 个，直接 new
        List<Mob> mobs = new ArrayList<>();
        mobs.add(new NecroArcher());
        mobs.add(new NecroGuard());
        mobs.add(new Aggregatus());
        mobs.add(new NecroCavalry());
        mobs.add(new NecroPioneer());
        mobs.add(new NecroScout());
        mobs.add(new NecroWarlock());
        mobs.add(new Wisp());

        List<Integer> cells = new ArrayList<>();
        cells.add(121);
        cells.add(125);
        cells.add(186);
        cells.add(190);
        cells.add(316);
        cells.add(320);
        cells.add(398);
        cells.add(394);

        Random.shuffle(mobs);
        Random.shuffle(cells);

        for (Mob mob : mobs){
            int pos = -1;
            while (!cells.isEmpty()){
                int c = cells.remove(0);
                if (Actor.findChar(c) == null){ pos = c; break; }
            }
            if (pos < 0) break;

            if(mob instanceof NecroGuard){
                ((NecroGuard) mob).setLegionSpawn(true);
            } else if(mob instanceof NecroArcher){
                ((NecroArcher) mob).setLegionSpawn(true);
            }

            mob.pos = pos;

            GameScene.add(mob);
            mob.sprite.emitter().startDelayed(
                    Speck.factory(Speck.BLUE_LIGHT), 0.02f, particles, 0.05f);
        }
    }

    @Override
    public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti, DamageType type) {
        if (enemy != null && enemy.isAlive()) {
            int stacks = enemy.buffs(LostSoul.class).size();
            if (stacks > 0) dmgMulti *= (1f + 0.15f * stacks);
        }
        return super.attack(enemy, dmgMulti, dmgBonus, accMulti, DamageType.MAGIC);
    }

    private int totalSoulStacks() {
        int total = 0;
        for (Char ch : Actor.chars()) {
            if (ch == this || !ch.isAlive()) continue;
            total += ch.buffs(LostSoul.class).size();
        }
        return total;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return super.defenseSkill(enemy) + totalSoulStacks() * 4;
    }

    @Override
    public int drRoll() {
        int total = totalSoulStacks();
        int bonus = 0;
        for (int i = 0; i < total; i++) {
            bonus += Random.Int(0, 4);
        }
        return super.drRoll() + bonus;
    }

    private static final String WARSTART = "WAR_START";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(WARSTART,warStart);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        warStart = bundle.getBoolean(WARSTART);
    }
}
