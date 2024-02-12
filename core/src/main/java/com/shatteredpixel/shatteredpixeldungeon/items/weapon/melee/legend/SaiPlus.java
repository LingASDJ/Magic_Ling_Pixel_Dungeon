package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class SaiPlus extends MeleeWeapon {
    public int R;

    {
        image = ItemSpriteSheet.THEDIED;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.3f;
        tier = 5;
        DLY = 1; // 攻速为 1
        ACC = 1;
        RCH = 1;
    }

    public String statsInfo(){
        return (Messages.get(this,"stats_info"));
    }

    @Override
    public int STRReq(int lvl) {
        return 17; // 力量要求为 17
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        float lifeStealPercentage = (float) (3 + 0.6 * level()) * damage; // 偷袭成功时恢复生命的百分比

        if (attacker instanceof Hero) {
            Hero hero = (Hero) attacker;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                int stolenLife = (int) lifeStealPercentage/10;
                attacker.HP += stolenLife;
                attacker.sprite.showStatus(CharSprite.POSITIVE, "+" + stolenLife + " HP");
                GLog.p(Messages.get(this, "success", attacker.name()));
            }
        }
        if(Random.Float()<0.4f){
            damage= (new Grim()).proc(this, attacker, defender, damage);
        }

        return super.proc(attacker, defender, damage);
    }

    @Override
    public int max(int lvl) {
        return (tier * 4+2)*lvl + 22;
    }

    @Override
    public int min(int lvl) {
        return tier+tier*lvl;  // 最小伤害
    }


    @Override
    public int iceCoinValue() {
        return 600;
    }

}
