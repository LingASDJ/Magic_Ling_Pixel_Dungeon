package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.SliceDogPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeadDogSleepCerberusSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class CerbusSleep extends NPC {

    {
        spriteClass = DeadDogSleepCerberusSprite.class;
        properties.add(Property.IMMOVABLE);
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
    }

    private boolean first=true;

    private boolean sd=true;

    private int touchdog;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";


    private static final String SNOD = "secnodx";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, touchdog);
        bundle.put(SNOD,sd);
    }



    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        touchdog = bundle.getInt(SECNOD);
        sd = bundle.getBoolean(SNOD);
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, Dungeon.hero.pos);

        if (first && Dungeon.level.heroFOV[pos] && Dungeon.level.distance(pos, hero.pos) <= 3) {
            SliceDogPlot plot = new SliceDogPlot();
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot,false)));

            SliceGirl sliceGirl = new SliceGirl();
            sliceGirl.pos = pos - 2;
            GameScene.add(sliceGirl);

            first = false;
        } else if(touchdog < 4){
            touchdog++;
            GLog.p(Messages.get(this,"touch_good"));
        } else if(sd){
            sd =false;
            GLog.n(Messages.get(this,"touch_dog"));
            Bomb bomb = new Bomb();
            bomb.explode(c.pos);
            Sample.INSTANCE.play( Assets.Sounds.DOG_ANAGY );
            PaswordBadges.DOG_TOUCH();
        }

        return true;
    }

    @Override
    public boolean add(Buff buff ) {
        return false;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public boolean reset() {
        return true;
    }

}
