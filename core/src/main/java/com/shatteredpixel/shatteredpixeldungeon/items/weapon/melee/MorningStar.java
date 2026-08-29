package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//晨星
//四阶，力量需求16
//初始4-20，成长1-5
//这把武器的攻击会轮流给予敌人2+0.3*等级（向上取整）回合的虚弱、易伤、晕眩、失明。
//寒光四射的单手钉头锤，也许是因此得名。
public class MorningStar extends MeleeWeapon{
    {
        image = ItemSpriteSheet.MORNING_STAR;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 4;
    }

    @Override
    public int max(int lvl) { return 20 + lvl * 5; }

    @Override
    public int min(int lvl) { return 4 + lvl; }
    // buff回合数
    public int theDuration(){
        return (int) Math.ceil(2+0.3f*level());
    }
    // 表示当前加哪个buff,不序列化
    public int nowBuff = 0;
    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        switch(nowBuff%4){
            case 0: {
                Buff.affect(defender, Weakness.class, theDuration());
                nowBuff++;
                break;
            }
            case 1:{
                Buff.affect(defender, Vulnerable.class, theDuration());
                nowBuff++;
                break;
            }
            case 2:{
                Buff.affect(defender, Vertigo.class, theDuration());
                nowBuff++;
                break;
            }
            case 3:{
                Buff.affect(defender, Blindness.class, theDuration());
                nowBuff++;
                break;
            }
        }
        return super.proc(attacker, defender, damage);
    }
}
