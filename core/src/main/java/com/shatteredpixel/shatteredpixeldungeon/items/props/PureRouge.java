package com.shatteredpixel.shatteredpixeldungeon.items.props;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.DemonFodder;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class PureRouge extends Prop {

    public float entrlledchance = 0.05f;

    {
        rareness = 2;
        kind = 0;
        image = ItemSpriteSheet.PURE_ROUGE;
    }

    public static final String ENTER_CHANCE = "ENTER_CHANCE";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ENTER_CHANCE,entrlledchance);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        entrlledchance = bundle.getFloat(ENTER_CHANCE);
    }

    @Override
    public String desc() {
        String s;
        s = Messages.get(this,"rareness",rareness+1,kindRules());
        s += "\n\n" + Messages.get(this,"desc",String.format("%.2f",entrlledchance*100));
        return s;
    }

    public boolean CheckEnthralled(){
        return entrlledchance >= Random.Float();
    }

    public void PureRougeEffect(Char enemy, Char get,boolean defense) {
        if(CheckEnthralled() && defense && !enemy.isImmune(ScrollOfSirensSong.Enthralled.class)){
            if(enemy instanceof DemonFodder){
                enemy.die(true);
            } else {
                Buff.affect(enemy, ScrollOfSirensSong.Enthralled.class);
                AllyBuff.affectAndLoot((Mob) enemy, hero, ScrollOfSirensSong.Enthralled.class);
            }
            entrlledchance = 0.05f;
            Transmuting.show(Dungeon.hero, new RandomChest(),this);
            Dungeon.hero.sprite.emitter().start(Speck.factory(Speck.BLIZZARD), 0.2f, 10);
            GLog.pink(Messages.get(this,"pure_charm",enemy.name()));
        } else {
            Charm c = Buff.affect(enemy, Charm.class, 5f);
            c.object = get.id();
            entrlledchance += 0.075f;
            c.ignoreNextHit = true;
            if (Dungeon.level.heroFOV[enemy.pos]) {
                enemy.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
                Sample.INSTANCE.play(Assets.Sounds.CHARMS);
            }
            Buff.affect(enemy, Hex.class,5f);
        }

    }
}
