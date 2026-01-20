package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Random;

public class DemonLord extends GoldMob {
    {
        spriteClass = DemonLordSprite.class;

        HP = HT = 120;
        defenseSkill = 30;

        EXP = 14;
        maxLvl = 29;

        properties.add(Property.DEMONIC);

        loot = Random.oneOf(new PotionOfExperience(),new ScrollOfTransmutation(),new StoneOfEnchantment());

        lootChance = 0.3f;
    }

    private int buffCnt = 0;



    @Override
    protected boolean canAttack( Char enemy ) {
        return Dungeon.level.distance(pos,enemy.pos)<=2;
    }

    private double damageBoost(int lv){
        //return lv * 1.15f;
        return Math.pow(1.15,lv)-1;
    }
    @Override
    public String description() {
        String desc = super.description();
        desc += "\n\n";
        desc += Messages.get(this, "damage_boost", (int)(damageBoost(buffCnt)*100) );
        return desc;
    }

    public int damageRoll() {
        buffCnt = 0;
        // accumulate without buff with no icon
        if(enemy != null){
            for(Object i:enemy.buffs(Buff.class).toArray()){
                if(i != null){
                    if(((Buff) i).icon()!= BuffIndicator.NONE) buffCnt+=1;
                }
            }
            if(enemy.buff(Light.class)!=null) buffCnt-=1;
        }
        buffCnt = Math.max(buffCnt, 0);
        return (int) (Random.NormalIntRange( 40, 45 ) * (1+damageBoost(buffCnt)));
    }

    @Override
    public int attackSkill( Char target ) {
        return 30;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(1, 10);
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
    }

    @Override
    protected boolean act() {
        return super.act();
    }

}
