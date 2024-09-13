package com.shatteredpixel.shatteredpixeldungeon.items.armor.custom;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AlowGlyph.AncityStone;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Stone;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class AncityArmor extends CustomArmor {

    {
        image = ItemSpriteSheet.ARMOR_ANCITY;
        tier = 8;
        alowglyph = new AncityStone();
        icon = ItemSpriteSheet.Icons.POTION_PARAGAS;
        charge = 50;
    }

    @Override
    public int STRReq(int lvl){
        return STRReq(4, lvl)-1;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_CUSTOM )) {
            if(charge>39){
                curUser = hero;
                curItem = this;
                GameScene.selectCell( zapper );
            } else {
                GLog.w(Messages.get(AncityArmor.class,"no_charge"));
            }

        }
    }

    public void fxs(Ballistica beam, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.TOXIC_VENT,
                curUser.sprite,
                beam.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
        //buildBeams(beam);
        callback.call();
    }

    protected int collisionProperties = Ballistica.MAGIC_BOLT;

    public int collisionProperties(int target){
        return collisionProperties;
    }


    public void wandUsed() {
        curUser.spendAndNext( 1f );
    }

    protected static CellSelector.Listener zapper = new  CellSelector.Listener() {

        @Override
        public void onSelect( Integer target ) {

            if (target != null) {

                final AncityArmor curWand;

                if (curItem instanceof AncityArmor) {
                    curWand = (AncityArmor) curItem;
                } else {
                    return;
                }
                final Ballistica shot = new Ballistica( curUser.pos, target, curWand.collisionProperties(target));
                int cell = shot.collisionPos;
                if (target == curUser.pos || cell == curUser.pos) {
                    GLog.i( Messages.get(Wand.class, "self_target") );
                    return;
                }
                curUser.sprite.zap(cell);

                if (curWand.tryToZap(curUser)) {

                    curUser.busy();
                    curWand.fxs(shot, new Callback() {
                        public void call() {
                            curWand.onZap(shot);
                            curWand.wandUsed();
                        }
                    });
                    curWand.cursedKnown = true;

                }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(Wand.class, "prompt");
        }
    };

    public boolean tryToZap( Hero owner ){

        if (owner.buff(MagicImmune.class) != null){
            GLog.w( Messages.get(Wand.class, "no_magic") );
            return false;
        }

        return true;
    }


    public void onZap( Ballistica beam ) {
        Char ch = Actor.findChar(beam.collisionPos);

        if (ch != null){

            if (!(ch instanceof Mob)){
                return;
            }

            Mob enemy = (Mob) ch;
            GameScene.add(Blob.seed(enemy.pos, 200, ToxicGas.class));
            GameScene.add(Blob.seed(enemy.pos, 200, CorrosiveGas.class));
            charge -= 40;
            Sample.INSTANCE.play( Assets.Sounds.GAS, 1, 0.8f * Random.Float(0.87f, 1.15f) );
        }
    }

    @Override
    public float evasionFactor(Char owner, float evasion ){

        if (hasGlyph(Stone.class, owner)){
            return 0;
        }

        if (owner instanceof Hero){
            int aEnc = STRReq() - ((Hero) owner).STR();
            if (aEnc > 0) evasion /= Math.pow(2.5, aEnc);

            Momentum momentum = owner.buff(Momentum.class);
            if (momentum != null){
                evasion += momentum.evasionBonus(((Hero) owner).lvl, Math.max(0, -aEnc));
            }
        }

        return (evasion*1.35f + augment.evasionFactor(buffedLvl())) * (level() <3 ? 1 : level()/3f);
    }

    @Override
    public int DRMin(int lvl){
        if (Dungeon.isChallenged(Challenges.NO_ARMOR)){
            return 0;
        }

        int max = DRMax(lvl);
        if (lvl >= max){
            return (lvl - max);
        } else {
            return lvl*2;
        }
    }

    @Override
    public int DRMax(int lvl){
        int max = 3 * (2 + lvl ) + (lvl > 0 ? 1 : 0 ) + augment.defenseFactor(lvl);

        if (Dungeon.isChallenged(Challenges.NO_ARMOR)){
            return 3 + augment.defenseFactor(lvl);
        }


        if (lvl > max){
            return ((lvl - max)+1)/2;
        } else {
            return max;
        }

    }



}
