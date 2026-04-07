package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.DogDogMusicPlot;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DogDogMusicSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
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
            return Messages.get(this,"desc",Random.IntRange(0, 18 + (level() * 3 )));
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

            int dmg = Random.IntRange(0, 18 + (level() * 3) );

            if(cicreStats != null && cicreStats.attackStats < 4 ) {
                cicreStats.attackStats++;
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                    if(mob.isAlive() && Dungeon.hero.fieldOfView[mob.pos]){
                        mob.damage(dmg,this,DamageType.PHYSICAL);
                        mob.sprite.emitter().burst( Speck.factory( Speck.FORGE ), 3 );
                    }
                }
                Sample.INSTANCE.play( Assets.Sounds.EVOKE );
                if(cicreStats.attackStats>1){
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                        if(mob.isAlive() && Dungeon.hero.fieldOfView[mob.pos]){
                            mob.damage(dmg,this,DamageType.PHYSICAL);
                            mob.sprite.emitter().burst( Speck.factory( Speck.FORGE ), 3 );
                        }
                    }
                }
            } else if(cicreStats == null) {
                Buff.affect(attacker, CicreStats.class,1f);
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
