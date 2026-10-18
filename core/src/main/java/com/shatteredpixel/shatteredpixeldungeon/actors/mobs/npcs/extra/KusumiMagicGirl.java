package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.jokings.HornOfPlentyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.jokings.HornOfPlentyRiceBomb;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KusumiMagicGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class KusumiMagicGirl extends Mob {

    public boolean first=true;

    {
        HP = HT = 1;
        spriteClass = KusumiMagicGirlSprites.class;
        properties.add(Char.Property.UNKNOWN);
        properties.add(Char.Property.IMMOVABLE);
    }

    // 修复卡墙：不被 beckon 吸引而进入 HUNTING
    @Override
    public void beckon(int cell) {
    }

    private boolean vanishing;      // 已经决定要消失

    @Override
    public boolean isActive() {
        return !vanishing;          // 先例：Ghoul.java:251-254、PrismaticImage.java:124
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    // 命中能力
    @Override
    public int attackSkill(Char target) {
        return 100;
    }

    @Override
    public void move(int step) {

    }

    @Override
    public synchronized boolean isAlive() {
        return true;
    }

    @Override
    public int damageRoll() {
        return 10;
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        super.damage(dmg, src, type);
        if(dmg < 0) return;
        if(first){
            if (enemy == null && src instanceof Char) {
                enemy = (Char) src;
            }
            if (enemy != null && enemy.isAlive() && enemy != this && Dungeon.level.adjacent(pos, enemy.pos)) {
                if (attack(enemy, 1f, 0f, INFINITE_ACCURACY, DamageType.REAL))
                    if (enemy instanceof Hero)
                        GLog.n(Messages.get(this, "ha"));
                first = false;
            }
        }
    }

    @Override
    protected boolean act() {
        // 修复卡墙：偶发被外部效果（爆炸/传送/推挤）落到不可通行格时，自动回落到相邻可通行格
        if (!vanishing && Dungeon.level != null
                && !Dungeon.level.passable[pos] && !Dungeon.level.avoid[pos]) {
            for (int i : com.watabou.utils.PathFinder.NEIGHBOURS8) {
                int n = pos + i;
                if (n >= 0 && n < Dungeon.level.length()
                        && (Dungeon.level.passable[n] || Dungeon.level.avoid[n])
                        && Actor.findChar(n) == null) {
                    if (sprite != null) sprite.move(pos, n);
                    pos = n;
                    Dungeon.level.occupyCell(this);
                    break;
                }
            }
        }

        if(vanishing || HP <= 0){
            vanishing = true;
            sprite.showAlert();
            selfTeleCooldown--;
            if(!teleporting){
                ((KusumiMagicGirlSprites) sprite).teleParticles(true);
                teleporting = true;
            }
            if(selfTeleCooldown == 0) {
                Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
                CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
                destroy();
                if (enemy != null && enemy.isAlive() && enemy != this && Dungeon.level.adjacent(pos, enemy.pos)) {
                    if (attack(enemy, 1f, 0f, INFINITE_ACCURACY, DamageType.REAL))
                        if (enemy instanceof Hero)
                            GLog.n(Messages.get(this, "ha2"));
                }
                sprite.die();
                return true;
            }
        }

        return super.act();
    }

    // 覆写destroy，防止进杀敌结算
    @Override public void destroy() {
        vanishing = true;
        dropLoot();
        Alignment a = alignment;
        alignment = Alignment.NEUTRAL;   // 让 Mob.destroy 的 ENEMY 结算整段跳过
        super.destroy();
        alignment = a;
    }

    private boolean teleporting = false;
    private int selfTeleCooldown = 2;

    private static final String TELEPORTING = "teleporting";
    private static final String FIRST = "first";
    private static final String SELF_COOLDOWN = "self_cooldown";
    private static final String VANISHING = "vanishing";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TELEPORTING, teleporting);
        bundle.put(SELF_COOLDOWN, selfTeleCooldown);
        bundle.put(FIRST, first);
        bundle.put(VANISHING, vanishing);
        bundle.put(DROPPED, dropped);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        teleporting = bundle.getBoolean( TELEPORTING );
        selfTeleCooldown = bundle.getInt( SELF_COOLDOWN );
        first = bundle.getBoolean(FIRST);
        vanishing = bundle.getBoolean(VANISHING);
        dropped = bundle.getBoolean(DROPPED);
    }

    //================ 掉落 ================
    private boolean dropped = false;
    private static final String DROPPED = "dropped";

    private void dropLoot() {
        if (dropped) return;
        if (Dungeon.level == null || Dungeon.hero == null) return;

        // removeArtifact 返回 true = 本局排行里还有丰收号角（此前没出现过），顺手把它从生成器余量里划掉
        float GeneratorRandom = Random.Float();
        if (GeneratorRandom < 0.05f && Generator.removeArtifact(HornOfPlenty.class)) {
            HornOfPlenty horn = new HornOfPlenty();
            horn.cursed = true;
            horn.cursedKnown = true;      // 和 Bones.java:262-272 的写法一致，玩家一眼能看出是诅咒的
            Dungeon.level.drop(horn, pos).sprite.drop(pos);
        }
        else if(GeneratorRandom < 0.5f){
            HornOfPlentyRiceBomb bomb = new HornOfPlentyRiceBomb();
            Dungeon.level.drop(bomb, pos).sprite.drop(pos);
        }
        else{
            HornOfPlentyBomb bomb = new HornOfPlentyBomb();
            bomb.isLit = false;
            Dungeon.level.drop(bomb, pos).sprite.drop(pos);                  // 先落地
            Actor.addDelayed(bomb.fuse = bomb.createFuse().ignite(bomb), 1); // 再点引信（1 回合后炸）
        }

        dropped = true;
    }
}

