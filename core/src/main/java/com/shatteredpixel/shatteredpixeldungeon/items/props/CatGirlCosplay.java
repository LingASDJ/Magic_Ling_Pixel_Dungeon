package com.shatteredpixel.shatteredpixeldungeon.items.props;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CatGirlCosplay extends Prop {

    {
        rareness = 0;
        kind = 2;
        image = ItemSpriteSheet.CATGIRL_COSPLAY;
    }

    public static class NoSeenBlobs extends Blob implements Hero.Doom {
        private static ArrayList<Class> affectedBlobs;
        {
            affectedBlobs = new ArrayList<>(new BlobImmunity().immunities());
        }

        @Override
        public String tileDesc() {
            return "";
        }
        @Override
        protected void evolve() {
            super.evolve();

            int damage = 0;

            Char ch;
            int cell;

            ArrayList<Blob> blobs = new ArrayList<>();
            for (Class c : affectedBlobs){
                Blob b = Dungeon.level.blobs.get(c);
                if (b != null && b.volume > 0){
                    blobs.add(b);
                }
            }

            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null && ch != hero) {
                        if (!ch.isImmune(this.getClass())) {
                            if(ch instanceof Mob){
                                if(Random.Float() <= 0.5f){
                                    Buff.affect(ch, Adrenaline.class,1f);
                                } else {
                                    ((Mob) ch).clearEnemy();
                                    if (ch.sprite != null) ch.sprite.showLost();
                                    Buff.affect(ch, Blindness.class,1f);
                                }
                                Sample.INSTANCE.play(Assets.Sounds.READ,1f,1.1f);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {

        }

        @Override
        public void onDeath() {
        }
    }


}
