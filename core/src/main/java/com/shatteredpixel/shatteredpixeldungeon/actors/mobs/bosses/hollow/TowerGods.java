package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerGodSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class TowerGods extends Boss {
    private boolean LastHP = HP*2 <= HT;
    public int magicDefence = 100;
    public int physicDefence = 100;
    {
        initProperty();
        initBaseStatus(15, 20, 33, 10, 500, 0, 0);
        initStatus(120);
        first = true;
        spriteClass = TowerGodSprite.class;

        viewDistance = 100;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
    }

    private static final String LAST_HP = "last_hp";
    private static final String MAGIC_DEFENCE = "magic_defence";
    private static final String PHYSIC_DEFENCE = "physic_defence";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LAST_HP, LastHP);
        bundle.put(MAGIC_DEFENCE, magicDefence);
        bundle.put(PHYSIC_DEFENCE, physicDefence);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        LastHP = bundle.getBoolean(LAST_HP);
        magicDefence = bundle.getInt(MAGIC_DEFENCE);
        physicDefence = bundle.getInt(PHYSIC_DEFENCE);
    }

    protected boolean act() {
        alerted = false;
        for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (boss.alignment == Alignment.ENEMY && !(boss instanceof TowerGods || boss instanceof TowerTime|| boss instanceof TowerMachine || boss instanceof TowerMind )) {
                if(LastHP){
                    switch (Random.Int(13)){
                        case 9: case 11: case 12:
                            Buff.affect(boss, Double_AttackUP_Palf.class, AttackUP_Palf.DURATION);
                            break;
                        case 6: case 7: case 8:
                            Buff.affect(boss, Healing.class).setHeal(45, 0f, 6);
                            break;
                        case 3: case 4: case 5:
                            PotionOfCleansing.cleanse(boss);
                            break;
                        case 0: case 1: case 2:
                            Buff.affect(boss, Barrier.class).setShield(75);
                            break;
                    }
                } else {
                    switch (Random.Int(13)){
                        case 9: case 11: case 12:
                            Buff.affect(boss, AttackUP_Palf.class, AttackUP_Palf.DURATION);
                            break;
                        case 6: case 7: case 8:
                            Buff.affect(boss, Healing.class).setHeal(30, 0f, 6);
                            break;
                        case 3: case 4: case 5:
                            PotionOfHealing.cure(boss);
                            break;
                        case 0: case 1: case 2:
                            Buff.affect(boss, Barrier.class).setShield(50);
                            break;
                    }
                }

            }
        }
        spend(25f);
        state = PASSIVE;
        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 15, 20 );
    }

    public static class AttackUP_Palf extends FlavourBuff {

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 5f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.TITLE_COLOR);
        }

        @Override
        public int icon() {
            return BuffIndicator.WEAPON;
        }

    }

    public static class Double_AttackUP_Palf extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION	= 5f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.RED_COLOR);
        }

        @Override
        public int icon() {
            return BuffIndicator.WEAPON;
        }

    }



    @Override
    public void damage(int dmg, Object src) {
        if(src == TowerMachine.class){
            return;
        }

        // 神选之人：计算有效增益Buff数量
        int buffCount = 0;
        for (Buff b : Dungeon.hero.buffs()) {
            if (b.type == Buff.buffType.POSITIVE) {
                buffCount++;
            }
        }

        // 至多加成50%（10层）
        float buffMultiplier = 1 + Math.min(buffCount * 0.05f, 0.5f);
        dmg = (int)(dmg * buffMultiplier);

        // 元素抗性检测开始
        Class<?> srcClass = src.getClass();
        HashSet<Class> resists = new HashSet<>(RingOfElements.RESISTS);
        boolean flag = false;
        for (Class c : resists){
            if (c.isAssignableFrom(srcClass)){
                flag=true;
                break;
            }
        }

        if (flag) {
            if (Dungeon.level.distance(pos,enemy.pos)>=5) {
                dmg *= 0.25f;
            } else {
                float rate = ((float) magicDefence / 100);
                dmg *= rate;
            }
        } else if (!(src instanceof Buff)) {
            float rate = ((float) physicDefence / 100);
            dmg *= rate;
        }

        super.damage(dmg, src);
    }


}
