package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.DogDogMusicPlot;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DogDogMusicSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class DogDogMusic extends FiveYearsNPC {

    private static String[] TXT_RANDOM = {
            Messages.get(DogDogMusic.class,"dialog_1"),
            Messages.get(DogDogMusic.class,"dialog_2"),
            Messages.get(DogDogMusic.class,"dialog_3"),
            Messages.get(DogDogMusic.class,"dialog_4"),
            Messages.get(DogDogMusic.class,"dialog_5"),
            Messages.get(DogDogMusic.class,"dialog_6"),
            Messages.get(DogDogMusic.class,"dialog_7"),
            Messages.get(DogDogMusic.class,"dialog_8"),
            Messages.get(DogDogMusic.class,"dialog_9"),
            Messages.get(DogDogMusic.class,"dialog_10"),
            Messages.get(DogDogMusic.class,"dialog_11"),
            Messages.get(DogDogMusic.class,"dialog_12"),
            Messages.get(DogDogMusic.class,"dialog_13"),
            Messages.get(DogDogMusic.class,"dialog_14"),
            Messages.get(DogDogMusic.class,"dialog_15"),
            Messages.get(DogDogMusic.class,"dialog_16"),
            Messages.get(DogDogMusic.class,"dialog_17"),
            Messages.get(DogDogMusic.class,"dialog_18"),
            Messages.get(DogDogMusic.class,"dialog_19"),
            Messages.get(DogDogMusic.class,"dialog_20")
    };

    {
        spriteClass = DogDogMusicSprite.class;
        plot1 = !(SPDSettings.isItemUnlock("DogDogLingDang")) ? new DogDogMusicPlot() : null;
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );
        if(first && !(SPDSettings.isItemUnlock("DogDogLingDang"))){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
            first = false;
        } else {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new DogDogMusicSprite(),
                            Messages.titleCase(Messages.get(DogDogMusic.class, "name")),
                            Messages.titleCase(Messages.get(DogDogMusic.class, "ik")) + "\n" +TXT_RANDOM[Random.Int(TXT_RANDOM.length)],
                            Messages.get(DogDogMusic.class, "sw_bgm")) {
                        @Override
                        protected void onSelect(int index) {
                           if(index == 0){
                               GameScene.show(new WndOptions(new DogDogMusicSprite(),
                                       Messages.titleCase(Messages.get(DogDogMusic.class, "sw_music")),
                                       Messages.titleCase(Messages.get(DogDogMusic.class, "sw_music_desc")),
                                       Messages.get(DogDogMusic.class, "star_bgm"),
                                       Messages.get(DogDogMusic.class, "sand_bgm"),
                                       Messages.get(DogDogMusic.class, "peach_bgm"),
                                       Messages.get(DogDogMusic.class, "defitit")) {
                                   @Override
                                   protected void onSelect(int index) {
                                       SPDSettings.currentBGM(index);
                                       Game.runOnRenderThread(() -> Music.INSTANCE.fadeOut(5f,
                                               () ->  Dungeon.level.playLevelMusic()));
                                   }
                               });
                           }
                        }
                    });
                }

            });
        }
        return true;
    }

    public static class CICREMUSIC extends MeleeWeapon {

        {
            tier = 4;
            image = ItemSpriteSheet.CICRE_MUSIC;
            RCH = 3;
        }

        @Override
        public String desc() {
            return Messages.get(this,"desc",1,18 + (level() * 3 ));
        }

        @Override
        public boolean doEquip(Hero hero) {
            Buff.affect(hero, CicreStats.class,12345678f);
            return super.doEquip(hero);
        }

        @Override
        public int STRReq(int lvl) {
            int req = (7 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
            if (masteryPotionBonus){
                req -= 2;
            }
            return req;
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            CicreStats cicreStats = attacker.buff(CicreStats.class);

            int dmg = Random.IntRange(1, 18 + (level() * 3));

            if (cicreStats != null && cicreStats.attackStats < 4) {
                cicreStats.attackStats++;
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob.isAlive()
                            && Dungeon.level.distance(Dungeon.hero.pos, mob.pos) <= 3) {
                        mob.damage(dmg, this, DamageType.PHYSICAL);
                        mob.sprite.emitter().burst(Speck.factory(Speck.FORGE), 3);
                    }
                }

                Sample.INSTANCE.play(Assets.Sounds.EVOKE);

                if (cicreStats.attackStats > 1) {
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (mob.isAlive()
                                && Dungeon.level.distance(Dungeon.hero.pos, mob.pos) <= 3) {
                            mob.damage(dmg, this, DamageType.PHYSICAL);
                            mob.sprite.emitter().burst(Speck.factory(Speck.FORGE), 3);
                        }
                    }
                }
            }

            return super.proc(attacker, defender, damage);
        }

        @Override
        public boolean doUnequip(Hero hero, boolean collect, boolean single) {
            Buff.detach(hero, CicreStats.class);
            return super.doUnequip(hero, collect, single);
        }

        @Override
        public int min(int lvl) {
            return 1;
        }

        @Override
        public int max(int lvl) {
            return 1;
        }

        @Override
        public String targetingPrompt() {
            return Messages.get(this, "prompt");
        }

        @Override
        protected void duelistAbility(Hero hero, Integer target) {
            if (target == null) return;
            Char dummyTarget = Actor.findChar(target);
            if (dummyTarget == hero || !Dungeon.level.heroFOV[target]) {
                GLog.w(Messages.get(this, "ability_no_target"));
                return;
            }

            hero.belongings.abilityWeapon = this;
            if (!hero.canAttack(dummyTarget)) {
                GLog.w(Messages.get(this, "ability_target_range"));
                hero.belongings.abilityWeapon = null;
                return;
            }
            hero.belongings.abilityWeapon = null;

            if(dummyTarget != null){
                hero.sprite.attack(dummyTarget.pos, new Callback() {
                    @Override
                    public void call() {
                        beforeAbilityUsed(hero, dummyTarget);
                        AttackIndicator.target(dummyTarget);
                        int baseDmg = Random.IntRange(1, 18 + (level() * 3));
                        castWideEcho(hero, baseDmg);

                        CellEmitter.center(hero.pos).burst(EnergyParticle.FACTORY, 15);
                        Sample.INSTANCE.play(Assets.Sounds.EVOKE);

                        Invisibility.dispel();
                        hero.spendAndNext(hero.attackDelay());
                        afterAbilityUsed(hero);
                    }
                });
            }
        }

        private void castWideEcho(Hero hero, int baseDamage) {
            int normalRange = Math.min(16,5 + (level()/2));
            boolean bossFloor = Dungeon.bossLevel();
            if (bossFloor) normalRange = Math.min(8,2 + (level()/2));

            for (Char ch : Actor.chars()) {
                if (ch == hero || ch.alignment == Char.Alignment.ALLY || ch.alignment == hero.alignment) {
                    continue;
                }

                boolean inFov = Dungeon.level.heroFOV[ch.pos];
                if (!inFov) {
                    continue;
                }

                boolean blockedByDoor = hasDoorBetween(hero.pos, ch.pos);
                if (blockedByDoor) {
                    continue;
                }

                int dist = Dungeon.level.distance(hero.pos, ch.pos);
                if (dist > normalRange) {
                    continue;
                }

                float dmgMult = 1f;
                int baseStandard = bossFloor ? 2 : 5;
                if (dist > baseStandard) {
                    int overRange = dist - baseStandard;
                    dmgMult = Math.max(0.1f, 1f - (0.15f * overRange));
                }

                int finalDmg = Math.round(baseDamage * dmgMult);

                if (finalDmg > 0) {
                    ch.damage(finalDmg, hero);
                    if (ch.sprite != null) ch.sprite.flash();
                }
            }
        }

        /**
         * 手动实现射线检测：两点之间路径是否存在关闭的门 Terrain.DOOR
         * @param from 起点
         * @param to 终点
         * @return true=中间有门阻隔，false=无门可穿透
         */
        private boolean hasDoorBetween(int from, int to) {
            Level level = Dungeon.level;
            int w = level.width();

            int x0 = from % w;
            int y0 = from / w;
            int x1 = to % w;
            int y1 = to / w;

            int dx = Math.abs(x1 - x0);
            int dy = Math.abs(y1 - y0);

            int sx = x0 < x1 ? 1 : -1;
            int sy = y0 < y1 ? 1 : -1;
            int err = dx - dy;

            int x = x0;
            int y = y0;

            while (true) {
                int currPos = y * w + x;
                if (level.map[currPos] == Terrain.DOOR) {
                    return true;
                }
                if (x == x1 && y == y1) break;

                int e2 = 2 * err;
                if (e2 > -dy) {
                    err -= dy;
                    x += sx;
                }
                if (e2 < dx) {
                    err += dx;
                    y += sy;
                }
            }
            return false;
        }

        @Override
        protected int baseChargeUse(Hero hero, Char target) {
            return 2;
        }

        @Override
        public String abilityInfo() {
            if (levelKnown){
                return Messages.get(this, "typical_ability_desc",Math.min(16,5 + (level()/2)),Math.min(8,2 + (level()/2)));
            } else {
                return Messages.get(this, "ability_desc",5,2);
            }
        }
    }

    public static class CicreStats extends FlavourBuff {


        {
            type = buffType.POSITIVE;
        }

        public static int level = 0;
        private int interval = 1;

        public int attackStats = 0;

        @Override
        public void detach() {
            if (target.sprite != null) fx( false );
            target.remove( this );
        }

        public int level() {
            return level;
        }

        public void set( int value, int time ) {
            //decide whether to override, preferring high value + low interval
            if (Math.sqrt(interval)*level <= Math.sqrt(time)*value) {
                level = value;
                interval = time;
                spend(time - cooldown() - 1);
            }
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", attackStats);
        }

        private static final String LEVEL	    = "level";
        private static final String INTERVAL    = "interval";
        private static final String ATTACK_STATS    = "stats";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( INTERVAL, interval );
            bundle.put( LEVEL, level );
            bundle.put( ATTACK_STATS, attackStats);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            interval = bundle.getInt( INTERVAL );
            level = bundle.getInt( LEVEL );
            attackStats = bundle.getInt( ATTACK_STATS );
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x66bbcc);
        }

        @Override
        public int icon() {
            return BuffIndicator.DUEL_COMBO;
        }
    }


}
