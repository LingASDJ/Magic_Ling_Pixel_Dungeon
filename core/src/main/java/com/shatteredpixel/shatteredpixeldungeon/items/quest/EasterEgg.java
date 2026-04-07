package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class EasterEgg extends Item {

    public static final String AC_AAT	= "KITEM";

    {
        stackable = true;
        image = ItemSpriteSheet.EASTER_EGG;
        defaultAction = AC_AAT;
        bones = true;
        animation = false;
    }

    @Override
    public void frames(ItemSprite itemSprite){
        itemSprite.texture(Assets.Sprites.MIMIC);
        TextureFilm frames = new TextureFilm(itemSprite.texture, 16, 16);
        MovieClip.Animation idle = new MovieClip.Animation(5, true);
        idle.frames( frames,2,2,2,3,3);
        itemSprite.play(idle);
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_AAT);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute( hero, action );

        if (action.equals( AC_AAT )) {
            detach( hero.belongings.backpack );

            float roll = Random.Float();

            if (roll > 0.95f) {
                Mob w = Reflection.newInstance(MobSpawner.getMobRotation(Math.min(depth+Random.Int(3),24)).get(0));
                ArrayList<Integer> spawnPoints = new ArrayList<>();
                for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                    int p = hero.pos + PathFinder.NEIGHBOURS8[i];
                    if (Actor.findChar(p) == null
                            && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])
                            && !(PathFinder.distance[p] == Integer.MAX_VALUE)) {
                        spawnPoints.add(p);
                    }
                }
                if (spawnPoints.size() > 0) {
                    w.pos = Random.element(spawnPoints);
                    GameScene.add(w);
                }
            } else if (roll > 0.85f) {
                float chance = Random.NormalFloat(30, 80);
                switch (Random.Int(7)){
                    case 0: Buff.affect(hero, ChampionHero.AntiMagic.class,chance); break;
                    case 1: Buff.affect(hero,ChampionHero.Giant.class,chance); break;
                    case 2: Buff.affect(hero,ChampionHero.Growing.class,chance); break;
                    case 3: Buff.affect(hero,ChampionHero.Halo.class,chance); break;
                    case 4: Buff.affect(hero,ChampionHero.Blazing.class,chance); break;
                    case 5: Buff.affect(hero,ChampionHero.Projecting.class,chance); break;
                    case 6: Buff.affect(hero,ChampionHero.Blessed.class,chance); break;
                }
            } else {
                Dungeon.level.drop(Generator.random(),hero.pos).sprite.drop();
            }

            hero.sprite.operate( hero.pos );
            hero.busy();
            hero.sprite.emitter().burst( Speck.factory( Speck.STAR), 12 );
            Sample.INSTANCE.play( Assets.Sounds.EVOKE );
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

    @Override
    public int value() {
        return 10;
    }
}
