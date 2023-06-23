package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.FrostFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class FrostBurning extends Buff implements Hero.Doom {

    private static final float DURATION = 8f;

    private float left;

    private static final String LEFT	= "left";

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEFT, left );
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int)left);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        left = bundle.getFloat( LEFT );
    }
    private int burnIncrement = 0;
    @Override
    public boolean act() {

        if (target.isAlive() && !target.isImmune(getClass())) {

            int damage = Random.NormalIntRange( 4,6 + Dungeon.depth/5 + hero.lvl/5 );
            //初始伤害4到6点 随着每深入5层+1伤害 随着玩家等级每升级5级+1伤害
            Buff.detach( target, Chill.class);

            if (target instanceof Hero) {

                Hero hero = (Hero)target;

                hero.damage( damage, this );
                burnIncrement++;

                //at 4+ turns, there is a (turns-3)/3 chance an item burns
                if (Random.Int(3) < (burnIncrement - 3)){
                    burnIncrement = 0;

                    ArrayList<Item> burnable = new ArrayList<>();
                    //does not reach inside of containers
                    for (Item i : hero.belongings.backpack.items){
                        if (!i.unique && (i instanceof Scroll || i instanceof MysteryMeat || i instanceof FrozenCarpaccio)){
                            burnable.add(i);
                        }
                    }

                    if (!burnable.isEmpty()){
                        Item toBurn = Random.element(burnable).detach(hero.belongings.backpack);
                        GLog.w( Messages.get(this, "burnsup", Messages.capitalize(toBurn.toString())) );
                        if (toBurn instanceof MysteryMeat || toBurn instanceof FrozenCarpaccio){
                            ChargrilledMeat steak = new ChargrilledMeat();
                            if (!steak.collect( hero.belongings.backpack )) {
                                Dungeon.level.drop( steak, hero.pos ).sprite.drop();
                            }
                        }
                        Heap.burnFX( hero.pos );
                    }
                }

            } else {
                target.damage( damage, this );
            }

            if (target instanceof Thief && ((Thief) target).item != null) {

                Item item = ((Thief) target).item;

                if (!item.unique && item instanceof Scroll) {
                    target.sprite.emitter().burst( ElmoParticle.FACTORY, 6 );
                    ((Thief)target).item = null;
                } else if (item instanceof MysteryMeat) {
                    target.sprite.emitter().burst( ElmoParticle.FACTORY, 6 );
                    ((Thief)target).item = new ChargrilledMeat();
                }

            }

        } else {

            detach();
        }

        if (Dungeon.level.flamable[target.pos] && Blob.volumeAt(target.pos, FrostFire.class) == 0) {
            GameScene.add( Blob.seed( target.pos, 6, FrostFire.class ) );
        }

        spend( TICK );
        left -= TICK;

        if (left <= 0 ) {
            detach();
        }

        return true;
    }

    public void reignite( Char ch ) {
        reignite( ch, DURATION );
    }

    public void reignite( Char ch, float duration ) {
        left = duration;
    }

    @Override
    public int icon() {
        return BuffIndicator.SACRIFICE;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.FROSTBURNING);
        else target.sprite.remove(CharSprite.State.FROSTBURNING);
    }

    @Override
    public String heroMessage() {
        return Messages.get(this, "heromsg");
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(left));
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - left) / DURATION);
    }

    @Override
    public void onDeath() {
        Dungeon.fail( getClass() );
        GLog.n( Messages.get(this, "ondeath") );
    }
}

