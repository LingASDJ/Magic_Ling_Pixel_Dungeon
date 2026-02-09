package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SunFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SunSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class WandOfSun extends DamageWand implements Item.ThanksItem{

    public Char owner;
    int collisionPos;
    //public static final String AC_DISMISS = "DISMISS";

    public int maxAmount = 1;

    {
        image = ItemSpriteSheet.HIGHTWAND_7;
    }

    @Override
    public void onZap(Ballistica bolt) {}

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);

        //actions.add(AC_DISMISS);

        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        /*
        if (action.equals(AC_DISMISS)) {
            GameScene.selectCell(cellSelector);
        }

         */
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        for(Actor actor : Actor.all()){
            if(actor instanceof MiniSun){
                MiniSun s = (MiniSun) actor;
                s.duration += 1;
            }
        }
    }

    @Override
    public void fx(Ballistica beam, Callback callback) {
        curUser.sprite.parent.add(
                new Beam.LightRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(collisionPos)));
        callback.call();
    }

    @Override
    public int getMissileType() {
        return MagicMissile.SHAMAN_RED;
    }

    public String statsDesc(){

        maxAmount = (int) (1+(buffedLvl()/5));

        if(isIdentified()) return Messages.get(this, "stats_desc",min(),max(),maxAmount);
        else return Messages.get(this, "stats_desc",min(0),max(0),1);
    }

    @Override
    public int min(int lvl){
        return 2 + 1 * lvl;
    }

    @Override
    public int max(int lvl){
        return 8 + 2* lvl;
    }

    @Override
    public boolean tryToZap(Hero owner, int target) {
        if(curCharges == 0) {
            GLog.i(Messages.get(Wand.class,"fizzles"));
            return false;
        }

        if (!cursed) {

            maxAmount = (int) (1+(buffedLvl()/5));
            int curAmount = 0;

            for (Actor actor : Actor.all()) {
                if (actor instanceof MiniSun &&  ((MiniSun) actor).wand == this) {

                    curAmount++;

                    MiniSun s = (MiniSun) actor;
                    if (s.pos == target) {
                        s.die();
                        GLog.i(Messages.get(WandOfSun.class, "hasSun"));
                        return false;
                    }
                }
            }

            if(curAmount >= maxAmount){

                GLog.i(Messages.get(WandOfSun.class, "no_more_suns"));
                return false;
            }

            if (!Dungeon.level.solid[target] && curCharges > 0) {
                this.owner = owner;
                MiniSun sun = new MiniSun(target);
                sun.sprite.place(target);
                sun.sprite.parent = Dungeon.level.addVisuals();
                GameScene.scene.add(sun);
                sun.duration = (int) (4 + buffedLvl() * 0.2f);
                sun.wand = this;
                collisionPos = target;
                return true;
            } else if (Dungeon.level.solid[target]) {
                GLog.i(Messages.get(WandOfSun.class, "solid"));
            }
        }else{
            return super.tryToZap(owner,target);
        }

        return false;
    }

    public static class MiniSun extends Actor {

        public  int[] rim25;
        public  int[] rim49;

        {
            rim25 = initializeCircle25();
            rim49 = initializeCircle49();
        }

        private static int[] initializeCircle25() {
            return new int[]{
                    -2, +2,
                    -2 - Dungeon.level.width(), 2 - Dungeon.level.width(),
                    -2 + Dungeon.level.width(), 2 + Dungeon.level.width(),
                    -2 - (Dungeon.level.width() * 2), -1 - (Dungeon.level.width() * 2), -(Dungeon.level.width() * 2), 1 - (Dungeon.level.width() * 2), 2 - (Dungeon.level.width() * 2),
                    -2 + (Dungeon.level.width() * 2), -1 + (Dungeon.level.width() * 2), (Dungeon.level.width() * 2), 1 + (Dungeon.level.width() * 2), 2 + (Dungeon.level.width() * 2)
            };
        }

        private static int[] initializeCircle49() {
            return new int[]{
                    -3,+3,
                    -3-Dungeon.level.width(),3-Dungeon.level.width(),
                    -3+Dungeon.level.width(),3+Dungeon.level.width(),
                    -3-(Dungeon.level.width()*2),3-(Dungeon.level.width()*2),
                    -3+(Dungeon.level.width()*2),3+(Dungeon.level.width()*2),
                    -3-(Dungeon.level.width()*3),-2-(Dungeon.level.width()*3),-1-(Dungeon.level.width()*3),-(Dungeon.level.width()*3),1-(Dungeon.level.width()*3),2-(Dungeon.level.width()*3) ,3-(Dungeon.level.width()*3),
                    -3+(Dungeon.level.width()*3),-2+(Dungeon.level.width()*3),-1+(Dungeon.level.width()*3),(Dungeon.level.width()*3),1+(Dungeon.level.width()*3),2+(Dungeon.level.width()*3) ,3+(Dungeon.level.width()*3),
            };
        }

        public int level = 1;
        public DamageWand wand;
        public MagesStaff staff = null;
        public int duration;
        public int viewDistance = 9;
        public Class<? extends CharSprite> spriteClass = SunSprite.class;
        public int pos;
        public boolean[] fieldOfView = null;
        public CharSprite sprite = Reflection.newInstance(spriteClass);

        public MiniSun(int position){
            pos = position;
        }
        public CharSprite sprite() {
            return Reflection.newInstance(spriteClass);
        }

        private static final String MINISUNSTATUS = "minisun_status";
        private static final String POS = "pos";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put( MINISUNSTATUS, duration) ;
            bundle.put( POS, pos );
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            if( bundle.contains( MINISUNSTATUS ) )
                duration = bundle.getInt( MINISUNSTATUS );
            if( bundle.contains( POS ) )
                pos = bundle.getInt( POS );
        }

        public void die(){
            sprite.die();
            Dungeon.observe();
            GameScene.updateFog(pos, viewDistance);
            Actor.remove(this);
        }

        @Override
        public boolean act(){
            duration--;

            boolean damaged = false;

            if(duration == 1 && wand.curCharges >0){
                wand.curCharges--;
                updateQuickslot();
                duration += 2;
            }

            spend( TICK );

            if(sprite == null){
                sprite = sprite();
            }

            Mob mob = Dungeon.level.findMob(pos);
            if(mob!=null&& mob.alignment == Char.Alignment.ENEMY && mob.buff(SunFire.class)== null){
                Buff.affect(mob, SunFire.class);
            } else if (mob!=null&& mob.alignment == Char.Alignment.ENEMY && mob.buff(SunFire.class)!= null) {
                mob.buff(SunFire.class).duration++;
            }

            int damage = Random.Int(wand.min(),wand.max());

            for (int i : PathFinder.NEIGHBOURS9){
                Mob m = Dungeon.level.findMob(pos+i);
                if(m !=null && m.alignment == Char.Alignment.ENEMY){
                    if(m.buff(SunFire.class) ==null){
                        m.damage(damage,this);
                    }else if(m.buff(SunFire.class).source != this){
                        m.damage((int) (damage * 1.25f),this);
                    }
                    if(Dungeon.hero.subClass == HeroSubClass.WARLOCK){
                        SoulMark.prolong(m, SoulMark.class, SoulMark.DURATION + wand.buffedLvl());
                    }
                    damaged = true;
                }

            }

            for (int i : Dungeon.level != null ? rim25 : new int[]{0}){
                Mob m = Dungeon.level.findMob(pos+i);
                if(m !=null && m.alignment == Char.Alignment.ENEMY) {
                    if (m.buff(SunFire.class) == null) {
                        m.damage((int) (damage * 0.75f), this);
                    } else if (m.buff(SunFire.class).source != this) {
                        m.damage((int) (damage * 1.25f * 0.75f), this);
                    }
                    damaged = true;
                    if(Dungeon.hero.subClass == HeroSubClass.WARLOCK) {
                        SoulMark.prolong(m, SoulMark.class, SoulMark.DURATION + wand.buffedLvl());
                    }
                }

            }

            for (int i : Dungeon.level != null ? rim49 : new int[]{0}){
                Mob m = Dungeon.level.findMob(pos+i);
                if(m !=null && m.alignment == Char.Alignment.ENEMY){
                    if(m.buff(SunFire.class) ==null){
                        m.damage((int) (damage * 0.5f),this);
                    }else if(m.buff(SunFire.class).source != this){
                        m.damage((int) (damage * 1.25f * 0.5f),this);
                    }
                    damaged = true;
                    if(Dungeon.hero.subClass == HeroSubClass.WARLOCK) {
                        SoulMark.prolong(m, SoulMark.class, SoulMark.DURATION + wand.buffedLvl());
                    }
                }
            }
            GameScene.updateFog(pos, viewDistance);

            if(damaged) BlastWaveLarge.blast(pos,7);

            if(duration<=0){
                die();
            }

            return true;
        }

    }

    public static class BlastWaveLarge extends Image {

        private static final float TIME_TO_FADE = 0.9f;

        private float time;
        private float size;

        public BlastWaveLarge(){
            super(Effects.get(Effects.Type.RIPPLE));
            origin.set(width / 2, height / 2);
        }

        public void reset(int pos, float size) {
            revive();

            x = (pos % Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
            y = (pos / Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;

            time = TIME_TO_FADE;
            this.size = size;
        }

        @Override
        public void update() {
            super.update();

            if ((time -= Game.elapsed) <= 0) {
                kill();
            } else {
                float p = time / TIME_TO_FADE;
                alpha(p);
                scale.y = scale.x = (1-p)*size;
            }
        }

        public static void blast(int pos) {
            blast(pos, 3);
        }

        public static void blast(int pos, float radius) {
            Group parent = Dungeon.hero.sprite.parent;
            WandOfBlastWave.BlastWave b = (WandOfBlastWave.BlastWave) parent.recycle(WandOfBlastWave.BlastWave.class);
            parent.bringToFront(b);
            b.reset(pos, radius);
        }

    }

    /*
    protected CellSelector.Listener cellSelector = new CellSelector.Listener(){

        @Override
        public void onSelect(Integer cell) {
            if (cell != null){
                for(Actor a :Actor.all()){
                    if(a instanceof MiniSun){
                        MiniSun s = (MiniSun) a;
                        if(s.pos == cell){
                            s.die();
                            return;
                        }
                    }
                }
                GLog.i(Messages.get(WandOfSun.class,"dissun"));
            }
        }

        @Override
        public String prompt() {
            return Messages.get(SandalsOfNature.class, "prompt_target");
        }
    };
    */
}