package com.shatteredpixel.shatteredpixeldungeon.items.thanks;


import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.KingBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CelestialBrush extends Artifact implements Item.ThanksItem {

    {
        image = ItemSpriteSheet.SKY_PEN;
        levelCap = 4;
        charge = 3;
        partialCharge = 0;
        chargeCap = 3;
        defaultAction = AC_PAINT;
    }

    @Override
    public int image() {
        if (level() < levelCap) {
            return super.image = ItemSpriteSheet.SKY_PEN;
        } else {
            return super.image = ItemSpriteSheet.SKY_PEN_PLUS;
        }

    }

    public static final String AC_PAINT = "PAINT";
    public static final String AC_INK   = "INK";  // inking 上墨的意思

    public static boolean isEquippedAndCursed() {
        if (Dungeon.hero == null) return false;
        Artifact artifact = Dungeon.hero.belongings.artifact();
        return artifact instanceof CelestialBrush && artifact.cursed;
    }

    //----------------------------------------------------------
    // 1. 动作列表
    //----------------------------------------------------------
    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero) && charge > 0 && !cursed && hero.buff(MagicImmune.class) == null) {
            actions.add(AC_PAINT);
        }
        if (isEquipped(hero) && level() < levelCap && !cursed && hero.buff(MagicImmune.class) == null) {
            actions.add(AC_INK);
        }
        return actions;
    }

    //----------------------------------------------------------
    // 2. 执行动作（作画 + 献祭升级）
    //----------------------------------------------------------
    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (hero.buff(MagicImmune.class) != null) return;

        // -------- 作画 --------
        if (action.equals(AC_PAINT)) {
            if (charge <= 0) {
                GLog.i(Messages.get(this, "no_charge"));
                return;
            }
            if (cursed) {
                GLog.i(Messages.get(this, "cursed"));
                return;
            }

            // 弹出三种画作选择
            GameScene.show(new WndOptions(
                    new ItemSprite(this),
                    Messages.get(this, "paint_title"),
                    Messages.get(this, "paint_desc"),
                    Messages.get(this, "paint_flame"),
                    Messages.get(this, "paint_frost"),
                    Messages.get(this, "paint_sky")
            ) {
                @Override
                protected void onSelect(int index) {
                    if (index == -1) return; // 点击窗口外取消，什么都不做

                    Hero hero = Dungeon.hero;
                    switch (index) {
                        case 0: // 古焰之色
                            GameScene.selectCell(flameCellSelector);
                            return;
                        case 1: // 严冬之色
                            // 需要瞄准，进入选格模式
                            GameScene.selectCell(frostCellSelector );
                            return;
                        case 2: // 天际之色
                            skyPaint(hero);
                            charge--;
                            break;
                    }
                    updateQuickslot();
                }

            });
        } else if (action.equals(AC_INK)) {
        // 检查是否处于幻惑状态
        if (hero.buff(Hex.class)!=null) {
            GLog.w(Messages.get(this, "hex"));
            return;
        }

        // 等级 2 特殊处理：不打开背包，弹确认框
        if (level() == 2) {
            GameScene.show(new WndOptions(
                    new ItemSprite(this),
                    Messages.get(this, "prompt_lv2_title"),   // "献祭自身"
                    Messages.get(this, "prompt_lv2_desc"),    // "画笔正在索取诞生前的回忆，是否献上15点生命上限？"
                    Messages.get(this, "confirm_yes"),        // "献祭"
                    Messages.get(this, "confirm_no")          // "取消"
            ) {
                @Override
                protected void onSelect(int index) {
                    if (index == 0) {
                        Hero hero = Dungeon.hero;
                        if (hero.HT <= 20) {
                            GLog.w(Messages.get(CelestialBrush.this, "not_enough_hp"));
                            return;
                        }
                        hero.addResistHealth(20);
                        hero.updateHT(false);
                        if (hero.HP > hero.HT) hero.HP = hero.HT;
                        GLog.i(Messages.get(CelestialBrush.this, "upgrade_hp"));
                        doUpgrade(hero);
                        updateQuickslot();
                    }
                }
            });
            return;
        }

        // 其他等级正常打开背包挑选物品
        GameScene.selectItem(itemSelector);
    }
    }

    //----------------------------------------------------------
    // 3. 三种画作的具体实现
    //----------------------------------------------------------
    private CellSelector.Listener flameCellSelector = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;
            Hero hero = Dungeon.hero;
            flamePaint(hero, cell); // 执行火焰画作
            Talent.onArtifactUsed(Dungeon.hero);
        }
        @Override
        public String prompt() {
            return Messages.get(CelestialBrush.this, "prompt_flame_target");
        }
    };

    private void flamePaint(Hero hero, int targetCell) {
        int dmg = Random.Int(8 + level()*3, 12 * (1 + level()));
        int crippleTurns = 1 + level() / 2;

        // 构建弹道（从英雄到目标点）
        Ballistica bolt = new Ballistica(hero.pos, targetCell, Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);
        int maxDist = 3+2*charge;                       // 与法杖的 3+2*charges 类似
        float angle = 30+20*charge;                    // 角度，法杖为 30+20*charges 类似
        ConeAOE cone = new ConeAOE(bolt, maxDist, angle,
                Ballistica.STOP_TARGET | Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);

        // 播放声音
        Sample.INSTANCE.play( Assets.Sounds.BLAST );

        // 遍历锥形区域内的所有格子
        for (int cell : cone.cells) {
            // 忽略施法者自身格子
            if (cell == hero.pos) continue;

            // 对敌人造成伤害和致残，并产生粒子效果，然后烧地
            Char ch = Actor.findChar(cell);
            if (ch != null && ch.isAlive() && ch.alignment != Char.Alignment.ALLY) {
                ch.damage(dmg, this);
                Buff.affect(ch, Cripple.class, crippleTurns);
                CellEmitter.get(ch.pos).burst(ElmoParticle.FACTORY, 6);
            }
            GameScene.add(Blob.seed(cell, 3 + level() * 2, Fire.class));
            updateQuickslot();
        }


        // CellEmitter.get(defender.pos + i).burst(SmokeParticle.FACTORY, 4);
        GLog.n(Messages.get(this, "flame"));
        charge--;
        hero.spendAndNext(1f);
    }

    // 严冬投射物瞄准
    private CellSelector.Listener frostCellSelector = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            if (cell == null) {
                return;
            }
            frostPaint(cell);
            Talent.onArtifactUsed(Dungeon.hero);
        }
        @Override
        public String prompt() {
            return Messages.get(CelestialBrush.class, "prompt_frost_target");
        }
    };

    private void frostPaint(int cell) {
        final Hero hero = Dungeon.hero;
        if (charge <= 0) {
            GLog.i(Messages.get(CelestialBrush.this, "no_charge"));
            return;
        }
        charge--;
        Ballistica bolt = new Ballistica(hero.pos, cell, Ballistica.MAGIC_BOLT);
        final int targetPos = bolt.collisionPos;

        // 发射魔法飞弹（参照 Wand.fx() ）
        MagicMissile.boltFromChar(
                hero.sprite.parent,
                MagicMissile.FROST,
                hero.sprite,
                targetPos,
                new Callback() {
                    @Override
                    public void call() {
                        int radius = 1;

                        for (int dx = -radius; dx <= radius; dx++) {
                            for (int dy = -radius; dy <= radius; dy++) {
                                int pos = targetPos + dx + dy * Dungeon.level.width();
                                // 检查坐标是否在地图内，且不是墙体（非 solid）
                                if (Dungeon.level.insideMap(pos) && !Dungeon.level.solid[pos]) {
                                    Splash.at( pos, 0xFFB2D6FF, 5);
                                }
                            }
                        }
                        Sample.INSTANCE.play(Assets.Sounds.SHATTER);

                        int slowTurns = 4 + level()*2;
                        for (Mob mob : Dungeon.level.mobs) {
                            if (Dungeon.level.distance(mob.pos, targetPos) <= 1) {
                                Buff.affect(mob, Slow.class, slowTurns);
                            }
                        }
                        GLog.b(Messages.get(CelestialBrush.this, "frost"));
                        updateQuickslot();
                    }
                }
        );
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
        hero.spendAndNext(1f);
    }

    private void skyPaint(Hero hero) {
        // 天际：在自身周围产生暗夜迷雾（气体量 100+等级*25）
        int totalGas = 100 + level() * 25;
        int centerVolume = totalGas / 2;

        for (int i : PathFinder.NEIGHBOURS8) {
            if (!Dungeon.level.solid[hero.pos + i]) {
                GameScene.add(Blob.seed(hero.pos + i, totalGas / 8, SmokeScreen.class));
            } else {
                centerVolume += totalGas / 8;
            }
        }
        GameScene.add(Blob.seed(hero.pos, centerVolume, SmokeScreen.class));
        GLog.pink(Messages.get(this, "sky"));
        hero.spendAndNext(1f);
        Talent.onArtifactUsed(Dungeon.hero);
    }

    //----------------------------------------------------------
    // 4. 升级献祭逻辑
    //----------------------------------------------------------
    private WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            switch (level()) {
                case 0: return Messages.get(CelestialBrush.class, "prompt_lv0");
                case 1: return Messages.get(CelestialBrush.class, "prompt_lv1");
                case 2: return Messages.get(CelestialBrush.class, "prompt_lv2");
                case 3: return Messages.get(CelestialBrush.class, "prompt_lv3");
                default: return "";
            }
        }

        @Override
        public Class<? extends Bag> preferredBag() {
           switch (level){
               case 0:
                   return PotionBandolier.class;
               case 1:
                   return KingBag.class;
               case 3: default:
                   return Bag.class;

           }
        }

        @Override
        public boolean itemSelectable(Item item) {
            int lv = level();
            if (lv == 0) {
                return item instanceof BlizzardBrew && item.isIdentified();
            } else if (lv == 1) {
                if (item instanceof MeleeWeapon) {
                    MeleeWeapon mw = (MeleeWeapon) item;
                    return mw.isIdentified() && mw.level() >= 2 && mw.tier >= 2 && !mw.cursed;
                }
            } else if (lv == 2) {
                return false;
            } else if (lv == 3) {
                if (!(item instanceof Ring)) return false;
                Ring r = (Ring) item;
                return r.isIdentified() && r.level() >= 2 && !r.cursed;
            }
            return false;
        }

        @Override
        public void onSelect(Item item) {
            Hero hero = Dungeon.hero;

            if (level() == 2) {
                if (hero.HT <= 20) {
                    GLog.w(Messages.get(CelestialBrush.class, "not_enough_hp"));
                    return;
                }
                hero.HT -= 20;
                if (hero.HP > hero.HT) hero.HP = hero.HT;
                GLog.n(Messages.get(CelestialBrush.class, "upgrade_hp"));
                doUpgrade(hero);
                return;
            }

            // 其他等级需要物品
            if (item == null) return;
            if (!itemSelectable(item)) {
                GLog.w(Messages.get(CelestialBrush.class, "invalid_item"));
                return;
            }

            item.detach(hero.belongings.backpack);
            doUpgrade(hero);
            switch(level()) {
                case 1:
                    GLog.w(Messages.get(CelestialBrush.class, "upgrade_1", item.name()));
                    break;
                case 2:
                    GLog.b(Messages.get(CelestialBrush.class, "upgrade_2", item.name()));
                    break;
                case 4:
                    GLog.yellow(Messages.get(CelestialBrush.class, "upgrade_4", item.name()));
                    break;
            }
        }
    };

    // 实际执行升级 + 附加幻惑
    private void doUpgrade(Hero hero) {
        upgrade();
        chargeCap = 2 + level();
        if (charge > chargeCap) charge = chargeCap;

        // 幻惑 50 回合
        Buff.affect(hero, Hex.class, 50f);

        Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
        CellEmitter.center(hero.pos).burst(Speck.factory(Speck.STAR), 12);
        updateQuickslot();
        hero.spendAndNext(2f);
    }

    //----------------------------------------------------------
    // 5. 被动充能
    //----------------------------------------------------------
    @Override
    protected ArtifactBuff passiveBuff() {
        return new BrushRecharge();
    }

    public class BrushRecharge extends ArtifactBuff {
        @Override
        public boolean act() {
            if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null && Regeneration.regenOn()) {
                float chargeGain = 0;
                int lost = chargeCap - charge;
                // 公式：每 (120 - 等级*5 - 已损失充能*8) 回合获得 1 点
                float turnCost = 120f - level() * 5f - lost * 5f;
                if (turnCost <= 0) turnCost = 1f;
                chargeGain = 1f / turnCost;
                chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
                if ( target.buff(ArtifactRecharge.class) != null ) {
                    chargeGain = 0.1f;
                }
                partialCharge += chargeGain;
                while (partialCharge >= 1) {
                    partialCharge--;
                    charge++;
                    if (charge == chargeCap) {
                        partialCharge = 0;
                        break;
                    }
                }
            }


            updateQuickslot();
            spend(TICK);
            return true;
        }
    }

    //----------------------------------------------------------
    // 6. 诅咒效果（文字描述，数值实现需Hook Hero属性）
    //----------------------------------------------------------
    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped(Dungeon.hero)) {
            if (cursed) {
                desc += "\n\n" + Messages.get(this, "desc_cursed");
            }
            if (level() < levelCap) {
                desc += "\n\n" + Messages.get(this, "desc_need");
            } else {
                desc += "\n\n" + Messages.get(this, "desc_maxed");
            }
        }
        return desc;
    }

    //----------------------------------------------------------
    // 7. 存档与读档
    //----------------------------------------------------------
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        chargeCap = 3 + level();
        if (charge > chargeCap) charge = chargeCap;
        image();
    }
}