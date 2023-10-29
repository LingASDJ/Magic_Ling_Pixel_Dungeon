package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CerberusSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Cerberus extends Boss {

    {
        initProperty();
        initBaseStatus(20, 40, 30, 8, 800, 14, 9);
        initStatus(100);
        spriteClass = CerberusSprite.class;
    }

    //技能条
    public float skillLevel;

    //攻击次数
    public int attackKills;

    //阶段转换
    public int phase;

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("phaseCR", phase);
        bundle.put("skillLevelCR", skillLevel);
        bundle.put("attackKillsCR",attackKills);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt("phaseCR");
        skillLevel = bundle.getFloat("skillLevelCR");
        attackKills = bundle.getInt("attackKillsCR");
    }

//ACT暂时注释    
//    @Override
//    public boolean act(){
//        return super.act();
//    }

    @Override
    protected boolean doAttack( Char enemy ) {
        switch (phase){
            default:
            case 0:
                NightmareRoot();
            break;
//            case 1:
//                SoulReCharges(enemy);
//            break;
//            case 2:
//                HellDogBack(enemy);
//            break;
//            case 3:
//                CrystalActive(enemy);
//            break;
//            case 4:
//                TrueOrFalse(enemy);
//            break;
//            case 5:
//                DiedGlyphs(enemy);
//            break;
//            case 6:
//                NoWorldMaster(enemy);
//            break;
        }
        return super.doAttack( enemy );
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if(attackKills>=3){
            ((CerberusSprite)sprite).Skills(pos);
        }

        return damage;
    }

    //技能1：噩梦缠绕
    private void NightmareRoot(){
        attackKills++;
        if(attackKills>=3){
            SpellSprite.show(this, SpellSprite.CHARGE);
        }
    }

    private void NightmareRootAtive(Char enmey){
        enmey.damage(Random.NormalIntRange((int)(baseMin*1.15), (int)(baseMax*1.15)),damageRoll());
        Buff.prolong( enemy, Roots.class, 3f );
        spend(attackDelay()*3f);
        clearReset();
    }

    private void clearReset(){
        attackKills = 0;
    }

}
