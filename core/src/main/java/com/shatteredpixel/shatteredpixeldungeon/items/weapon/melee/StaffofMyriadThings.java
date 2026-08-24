package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

//万象之杖
//四阶，力量需求16
//初始5-20，成长1-5
//每使用这把武器击杀过一种敌人，就获得0-2的伤害成长以及仅限此武器的1精准修正。对于boss这个加成效果翻倍。
//已击杀过n种敌人，分别是……
//需要将森罗万象填充进去，才能熠熠生辉。
public class StaffofMyriadThings extends MeleeWeapon{
    {
        image = ItemSpriteSheet.SKIN_5;

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
        if (defender.HP <= damage) {
            // 记录击杀的敌人种类：同一类只计一次
            String cls = defender.getClass().getName();
            if (killedTypes.add(cls) && defender instanceof Boss){
                // Boss 种类额外再计一层
                bossTypes.add(cls);
            }
        }
        return super.proc(attacker, defender, damage);
    }
}