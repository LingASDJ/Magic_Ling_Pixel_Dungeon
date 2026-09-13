package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BuffsOringinForWeapon.DoomsdayScepterVulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.KusumiMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

//末日节杖
//五阶，力量需求18
//初始4-25，成长1-5，精准1.5
//装备时，你可以主动降低此武器的1级真实等级，这视为你使用了一张灵爆秘卷。
//爆发性的能量蕴藏在这柄木杖中，似乎可以如阅读卷轴一般的使用它。
//武技：末日信使，消耗2充能，获得4回合灵视。接下来4回合你对视野内的敌人受到的所有来源最终伤害*1.5。
public class DoomsdayScepter extends MeleeWeapon{

    // 灵爆：降低 1 级真实等级，视为读取一张灵爆秘卷
    public static final String AC_BURST = "BURST";

    {
        image = ItemSpriteSheet.ENDSUN;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        ACC = 1.5f;

        tier = 5;
    }

    @Override
    public int max(int lvl) { return 25 + lvl * 5; }

    @Override
    public int min(int lvl) { return 4 + lvl; }

    // 装备且真实等级 ≥1 时，显示“灵爆”动作
    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero) && trueLevel() >= 1) {
            actions.add(AC_BURST);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_BURST)) {
            if (!isEquipped(hero)) {
                GLog.w(Messages.get(this, "need_equip"));
                return;
            }
            if (trueLevel() < 1) {
                GLog.w(Messages.get(this, "no_level"));
                return;
            }
            // 视为读取一张灵爆秘卷：走读卷轴完整流程（限制、效果、读卷轴联动），真正读出才扣等级
            ScrollOfPsionicBlast scroll = new ScrollOfPsionicBlast();
            scroll.curUser = hero;
            if (scroll.tryRead(hero)) {
                degrade();
                updateQuickslot();
            }
        }
    }

    // ==================== 武技：末日信使 ====================

    // 每次使用武技消耗的充能点数（由决斗者的 Charger buff 提供）
    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 2;
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        // 1. 先扣充能：beforeAbilityUsed 会按 baseChargeUse 的返回值扣掉对应充能
        beforeAbilityUsed(hero, null);

        // 2. 上灵视
        Buff.affect(hero, MindVision.class, 4f);

        // 对视野内敌人增加标记
        if (hero.fieldOfView != null) {
            for (Char ch : Actor.chars()) {
                if (ch != hero
                        && ch.pos >= 0                             // 此句与下一句同表示目标位置存在暨不会使得第三句发生数组越界
                        && ch.pos < hero.fieldOfView.length
                        && (hero.fieldOfView[ch.pos] || hero.mindVisionEnemies.contains(ch))
                        && ch.alignment != hero.alignment
                        && ch.alignment != Char.Alignment.NEUTRAL  // 不选择中立阵营以规避宝箱怪
                        && !(ch instanceof NPC || ch instanceof KusumiMagicGirl)  // 不选择NPC与NPC形态久住
                        && ch.isAlive()                            // 只选还活着的
                        && !ch.isInvulnerable(getClass())) {       // 不选无敌单位
                    Buff.affect(ch, DoomsdayScepterVulnerable.class, 4f);
                }
            }
        }

        // 3. 播放使用动作，并消耗一个回合
        hero.sprite.operate(hero.pos);
        hero.spendAndNext(1);

        // 4. 武技收尾：处理与武技相关的天赋联动
        afterAbilityUsed(hero);
    }
}
