package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Preparation;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChooseSubclass;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

import java.util.ArrayList;

public class OldSunShadow extends Item {

    private static final String AC_WEAR	= "WEAR";

    {
        stackable = false;
        image = ItemSpriteSheet.OLDSUNSHADOW;

        defaultAction = AC_WEAR;

        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_WEAR );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_WEAR )) {

            if(hero.subClass == HeroSubClass.NONE){
                curUser = hero;

                GameScene.show( new WndChooseSubclass.WndOldMageClass( this, hero ) );
            } else {
                GLog.w(Messages.get(this,"not_apply"));
                detach( curUser.belongings.backpack );
            }
        }
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        Badges.validateMastery();
        return super.doPickUp( hero, pos );
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public void choose( HeroSubClass way ) {

        detach( curUser.belongings.backpack );
        Catalog.countUse( getClass() );

        curUser.spend( Actor.TICK );
        curUser.busy();

        curUser.subClass = way;
        Talent.initSubclassTalents(curUser);

        if (way == HeroSubClass.ASSASSIN && curUser.invisible > 0){
            Buff.affect(curUser, Preparation.class);
        }

        curUser.sprite.operate( curUser.pos );
        Sample.INSTANCE.play( Assets.Sounds.MASTERY );

        Emitter e = curUser.sprite.centerEmitter();
        e.pos(e.x-2, e.y-6, 4, 4);
        e.start(Speck.factory(Speck.MASK), 0.05f, 20);
        GLog.p( Messages.get(this, "used"));

    }
}

