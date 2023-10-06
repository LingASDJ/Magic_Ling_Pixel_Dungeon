package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.MagicalFireRoom;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfAnmy extends DamageWand {

    {
        defaultAction = AC_ZAP;
        usesTargeting = true;
        bones = true;
    }

    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = new ArrayList<>();
        if (curCharges > 0 || !curChargeKnown) {
            actions.add( AC_ZAP );
        }
        actions.remove(AC_DROP);
        actions.remove(AC_THROW);
        return actions;
    }

    @Override
    public int value() {
        return 0;
    }

    public static class AllyToRestartOK extends ChampionEnemy {

        @Override
        public boolean attachTo(Char target) {
            if (super.attachTo(target)){
                target.alignment = Char.Alignment.ALLY;
                if (target.buff(PinCushion.class) != null){
                    target.buff(PinCushion.class).detach();
                }
                return true;
            } else {
                return false;
            }
        }


        @Override
        public String toString() {
            return Messages.get(AllyToRestart.class, "name");
        }

        @Override
        public String desc() {
            return Messages.get(AllyToRestart.class, "desc");
        }

        @Override
        public void fx(boolean on) {
            if (on) {
                target.sprite.add(CharSprite.State.HEARTS);
                //Statistics.TryUsedAnmy = true;
            }
            else
                target.sprite.remove(CharSprite.State.HEARTS);
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x66bbcc);
        }

        public int icon() {
            return BuffIndicator.HEX;
        }
    }

    public static class AllyToRestart extends AllyBuff {

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add(CharSprite.State.HEARTS);
            else    target.sprite.remove(CharSprite.State.HEARTS);
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x66bbcc);
        }

        public int icon() {
            return BuffIndicator.HEX;
        }

    }

    @Override
    protected int initialCharges() {
        return 1;
    }

    @Override
    public void updateLevel() {
        maxCharges = 1;
        curCharges = Math.min( curCharges, maxCharges );
    }

    {
        image = ItemSpriteSheet.WAND_KCX;
    }


    public int min(int lvl){
        return 2+lvl;
    }

    public int max(int lvl){
        return 8+5*lvl;
    }

    @Override
    public void onZap(Ballistica bolt) {

        Heap heap = Dungeon.level.heaps.get(bolt.collisionPos);
        if (heap != null) {
            heap.freeze();
        }

        Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
        if (fire != null && fire.volume > 0) {
            fire.clear( bolt.collisionPos );
        }

        MagicalFireRoom.EternalFire eternalFire = (MagicalFireRoom.EternalFire)Dungeon.level.blobs.get(MagicalFireRoom.EternalFire.class);
        if (eternalFire != null && eternalFire.volume > 0) {
            eternalFire.clear( bolt.collisionPos );
            //bolt ends 1 tile short of fire, so check next tile too
            if (bolt.path.size() > bolt.dist+1){
                eternalFire.clear( bolt.path.get(bolt.dist+1) );
            }

        }

        Char ch = Actor.findChar(bolt.collisionPos);
        if (ch != null){

            int damage = 0;

            wandProc(ch, chargesPerCast());
            ch.damage(damage, this);
            Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, 1.1f * Random.Float(0.87f, 1.15f) );

            if (ch.isAlive() && (!ch.properties().contains(Char.Property.BOSS))){
                Buff.affect(ch, AllyToRestart.class);
            } else {
                GLog.n("不能影响Boss。");
            }
        } else {
            Dungeon.level.pressCell(bolt.collisionPos);
        }
    }

    @Override
    public void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(curUser.sprite.parent,
                MagicMissile.FROST,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
       //
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(0x88CCFF);
        particle.am = 0.6f;
        particle.setLifespan(2f);
        float angle = Random.Float(PointF.PI2);
        particle.speed.polar( angle, 2f);
        particle.acc.set( 0f, 1f);
        particle.setSize( 0f, 1.5f);
        particle.radiateXY(Random.Float(1f));
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

}

