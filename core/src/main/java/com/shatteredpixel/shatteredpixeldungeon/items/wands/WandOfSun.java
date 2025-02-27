package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SunFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SunSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class WandOfSun extends Wand{

    public Char owner;
    int collisionPos;
    public static final String AC_DISMISS = "DISMISS";

    {
        image = ItemSpriteSheet.HIGHTWAND_7;
    }

    @Override
    public void onZap(Ballistica bolt) {}

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);

        actions.add(AC_DISMISS);

        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);


        if (action.equals(AC_DISMISS)) {
            GameScene.selectCell(cellSelector);
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        for(Actor actor : Actor.all()){
            if(actor instanceof MiniSun){
                MiniSun s = (MiniSun) actor;
                s.duration += 3;
            }
        }
    }

    @Override
    public void fx(Ballistica beam, Callback callback) {
        curUser.sprite.parent.add(
                new Beam.LightRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(collisionPos)));
        callback.call();
    }
    public String statsDesc(){
        return Messages.get(this, "stats_desc",min(),max());
    }

    public int min(){
        return 2 + 2*level;
    }

    public int max(){
        return 8 + 4*level;
    }

    @Override
    public boolean tryToZap(Hero owner, int target) {

        for(Actor actor : Actor.all()){
            if(actor instanceof MiniSun){
                MiniSun s = (MiniSun) actor;
                if(s.pos == target){
                    GLog.i(Messages.get(WandOfSun.class,"hasSun"));
                    return false;
                }
            }
        }

        if(Dungeon.level.passable[target] && curCharges >0) {
            this.owner = owner;
            MiniSun sun = new MiniSun(target);
            sun.sprite.place(target);
            sun.sprite.parent = Dungeon.level.addVisuals();
            GameScene.scene.add(sun);
            sun.duration = (int) ( 3 + buffedLvl()*0.3f);
            sun.wand = this;
            collisionPos = target;
            return true;
        }
        GLog.i(Messages.get(Wand.class,"fizzles"));
        return false;
    }

    public static class MiniSun extends Actor {

        public static int[] circle25;
        public static int[] circle49;

        static {
            circle25 = initializeCircle25();
            circle49 = initializeCircle49();
        }

        private static int[] initializeCircle25() {
            return new int[]{
                    -2, +2,
                    -2 - Dungeon.level.width(), 2 - Dungeon.level.width(),
                    -2 + Dungeon.level.width(), 2 + Dungeon.level.width(),
                    -2 - (Dungeon.level.width() * 2), -1 - (Dungeon.level.width() * 2), -(Dungeon.level.width() * 2), 1 - (Dungeon.level.width() * 2), 2 - (Dungeon.level.width() * 2),
                    -2 + (Dungeon.level.width() * 2), -1 + (Dungeon.level.width() * 2), (Dungeon.level.width() * 2), 1 + (Dungeon.level.width() * 2), 2 + (Dungeon.level.width() * 2),
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
        public WandOfSun wand;
        public MagesStaff staff = null;
        public int duration = 3;
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

        public void SunStorm(Char s){
            sprite.parent.add(new Beam.DeathRayS(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(s.pos)));
        }

        @Override
        public boolean act(){
            duration--;

            if(duration == 1 && wand.curCharges >0){
                wand.curCharges--;
                updateQuickslot();
                duration += 3;
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
                    SunStorm(m);
                }

            }

            for (int i : Dungeon.level != null ? circle25 : new int[]{0}){
                Mob m = Dungeon.level.findMob(pos+i);
                if(m !=null && m.alignment == Char.Alignment.ENEMY) {
                    if (m.buff(SunFire.class) == null) {
                        m.damage((int) (damage * 0.75f), this);
                    } else if (m.buff(SunFire.class).source != this) {
                        m.damage((int) (damage * 1.25f * 0.75f), this);
                    }
                    SunStorm(m);
                }

            }

            for (int i : Dungeon.level != null ? circle49 : new int[]{0}){
                Mob m = Dungeon.level.findMob(pos+i);
                if(m !=null && m.alignment == Char.Alignment.ENEMY){
                    if(m.buff(SunFire.class) ==null){
                        m.damage((int) (damage * 0.5f),this);
                    }else if(m.buff(SunFire.class).source != this){
                        m.damage((int) (damage * 1.25f * 0.5f),this);
                    }
                    SunStorm(m);
                }
            }
            GameScene.updateFog(pos, viewDistance);



            if(duration<=0){
                die();
            }

            return true;
        }

    }

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
}
