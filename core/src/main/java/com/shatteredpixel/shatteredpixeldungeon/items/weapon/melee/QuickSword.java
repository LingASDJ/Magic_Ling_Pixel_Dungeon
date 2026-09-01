package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//迅捷剑
//三阶，力量需求14
//初始4-20，成长1-4
//你每拥有1%的移速提升，这把武器的伤害就上升1%。（不会超过（武器等级+1）*50%）
//慢慢来？切，开什么玩笑，天下武功当然是唯快不破！
public class QuickSword extends MeleeWeapon {
    {
        image = ItemSpriteSheet.SKIN_5;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;
        tier = 3;
    }
    @Override
    public int max(int lvl) { return 20 + lvl * 4; }

    @Override
    public int min(int lvl) { return 4 + lvl; }

    @Override
    public int damageRoll(Char owner){
        int damage = super.damageRoll(owner);
        //每1%移速提升→1%伤害提升，最多提升(武器等级+1)*50%
        float maxMultiplier = 1f + (buffedLvl() + 1) * 0.5f;
        float speedMultiplier = Math.max(1f, owner.speed() / owner.baseSpeed);
        float multiplier = Math.min(speedMultiplier, maxMultiplier);
        return Math.round(damage * multiplier);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

    @Override
    public String statsInfo() {
        float maxMultiplier = 1f + (level() + 1) * 0.5f;
        float speedMultiplier = Dungeon.hero != null ? Math.max(1f, Dungeon.hero.speed() / Dungeon.hero.baseSpeed) : 1f;
        float multiplier = Math.min(speedMultiplier, maxMultiplier);
        return Messages.get(this, "stats_desc", multiplier, maxMultiplier);
    }
}