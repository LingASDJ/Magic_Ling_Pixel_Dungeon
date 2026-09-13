package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.KusumiMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.INFINITE_ACCURACY;

//万象之杖
//四阶，力量需求16
//初始5-20，成长1-5
//每使用这把武器击杀过一种敌人，就获得0-2的伤害成长以及仅限此武器的1精准修正。对于boss这个加成效果翻倍。
//已击杀过n种敌人，分别是……
//需要将森罗万象填充进去，才能熠熠生辉。
//武技：万象辉光，消耗5充能，视野内每有1个任意单位（不包括自己）就获得1回合无敌。每击杀过13种敌人，最终额外获得1回合无敌。
public class StaffofMyriadThings extends MeleeWeapon{
    {
        image = ItemSpriteSheet.EARTH_STICK;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 4;
    }

    @Override
    public int max(int lvl) { return 20 + lvl * 5 + 2*CengShu(); }

    @Override
    public int min(int lvl) { return 5 + lvl; }

    // 已击杀过的敌人种类（按类名记录，每种只计一次）
    private HashSet<String> killedTypes = new HashSet<>();
    // 其中属于 Boss 的种类（每种额外再计一层）
    private HashSet<String> bossTypes = new HashSet<>();
    // 层数：普通敌人每种1层，Boss每种2层
    public int CengShu(){
        return killedTypes.size() + bossTypes.size();
    }

    @Override
    public String desc() {
        String desc = super.desc();

        String killed;
        if (killedTypes.isEmpty()){
            killed = Messages.get(this, "killed_none");
        } else {
            killed = Messages.get(this, "killed", killedTypes.size(), killedList());
        }

        return desc + "\n\n" + killed + "\n" + Messages.get(this, "bonus", 2*CengShu(), CengShu());
    }

    // 已击杀种类的名字列表（最多显示10种，超出部分用“等N种”省略）
    private String killedList(){
        ArrayList<String> names = new ArrayList<>();
        for (String cls : killedTypes){
            try {
                names.add(Messages.get(Class.forName(cls), "name"));
            } catch (Throwable t) {
                names.add(cls);
            }
        }
        Collections.sort(names);
        if (names.size() > 10){
            return String.join(", ", names.subList(0, 10)) + Messages.get(this, "and_more", names.size() - 10);
        }
        return String.join(", ", names);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        // 用集合存击杀敌人的名字
        killedTypes = new HashSet<>();
        bossTypes = new HashSet<>();
        String[] killed = bundle.getStringArray("killed_types");
        String[] bosses = bundle.getStringArray("boss_types");
        if (killed != null) Collections.addAll(killedTypes, killed);
        if (bosses != null) Collections.addAll(bossTypes, bosses);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("killed_types", killedTypes.toArray(new String[0]));
        bundle.put("boss_types", bossTypes.toArray(new String[0]));
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        final Char def = defender;

        Actor.add(new Actor() {
            {
                actPriority = VFX_PRIO;
            }

            @Override
            protected boolean act() {
                if (!def.isAlive()) {
                    // 记录击杀的敌人种类：同一类只计一次
                    String cls = def.getClass().getName();
                    if (killedTypes.add(cls)) {
                        // Boss 种类额外再计一层
                        if (def instanceof Boss) {
                            bossTypes.add(cls);
                        }
                    }
                }
                Actor.remove(this);
                return true;
            }
        });

        return super.proc(attacker, defender, damage);
    }

    // ========== 武技：万象辉光 ==========
    // 每次使用武技消耗的充能点数（由决斗者的 Charger buff 提供）
    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 5;
    }

    // 仿照MerchantSword的5g
    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        // 1. 先扣充能：beforeAbilityUsed 会按 baseChargeUse 的返回值扣掉对应充能
        beforeAbilityUsed(hero, null);

        // 2. 上无敌效果
        Buff.affect(hero, Invulnerability.class, Duration(hero));

        // 3. 播放使用动作，并消耗一个回合
        hero.sprite.operate(hero.pos);
        hero.spendAndNext(1);

        // 4. 武技收尾：处理与武技相关的天赋联动
        afterAbilityUsed(hero);
    }

    // 计数器，用于数单位数量
    private int countChars(Char origin) {
        int n = 0;
        // 增强for使用前确保一下遍历的目标不为空
        if (origin.fieldOfView == null) return n;
        for (Char ch : Actor.chars()) {
            if (ch != origin
                    && ch.pos >= 0                             // 此句与下一句同表示目标位置存在暨不会使得第三句发生数组越界
                    && ch.pos < origin.fieldOfView.length
                    && origin.fieldOfView[ch.pos]
                    && ch.isAlive()) {
                n++;
            }
        }
        return n;
    }

    // 计算器，用于计算武技最终给予的无敌回合数
    private float Duration(Char origin){
        return countChars(origin) + ((float)killedTypes.size() / 13f);
    }

    // 武技描述相关
    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc");
        } else {
            return Messages.get(this, "typical_ability_desc");
        }
    }
}