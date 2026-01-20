package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Frankenstein;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Vampire;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class PotionOfHolyWater extends Item {

    {
        image = ItemSpriteSheet.HOLY_WATER;

        stackable = true;

        defaultAction = AC_THROW;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        return super.actions(hero);
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
    }

    @Override
    protected void onThrow(int cell) {
        if (Dungeon.level.map[cell] == Terrain.WELL || Dungeon.level.pit[cell]) {

            super.onThrow(cell);

        } else {

            Dungeon.level.pressCell(cell);
            shatter(cell);

        }
    }

    public void shatter(int cell) {

        if (Dungeon.level.heroFOV[cell]) {
            splash(cell);
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        }
    }

    protected void splash(int cell) {
        DEM(cell);
    }

    protected void DEM(int cell) {
        Char ch = Actor.findChar(cell);
        if(ch != null){
            if (ch instanceof Vampire) {
                ((Vampire) ch).holy = true;
                GLog.n(Messages.get(this,"vampire_reset"));
                SpellSprite.showRGB(ch, SpellSprite.ANKH,0.1f,1f,0.1f);
                CellEmitter.get( cell ).burst( Speck.factory( Speck.STEAM ), 5 );
                PaswordBadges.VAM_GHOST();
            } else if (ch instanceof Frankenstein){
                ((Frankenstein) ch).MustDied = true;
                GLog.n(Messages.get(this,"zombie_noreset"));
                ch.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
                CellEmitter.get( cell ).burst( Speck.factory( Speck.DISCOVER ), 5 );
                SpellSprite.showRGB(ch, SpellSprite.ANKH,0.1f,1f,0.1f);
            } else if (ch.properties.contains(Char.Property.HOLLOW)) {
                ch.damage(ch.HT/2,new DM100.LightningBolt());
                GLog.p(Messages.get(this,"hollow"));
                CellEmitter.get( cell ).burst( Speck.factory( Speck.EVOKE ), 5 );
            }
        }

    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public int price() {
        return 50;
    }
}
