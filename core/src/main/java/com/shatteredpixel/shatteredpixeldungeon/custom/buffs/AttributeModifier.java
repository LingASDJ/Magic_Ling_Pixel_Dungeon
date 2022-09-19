package com.shatteredpixel.shatteredpixeldungeon.custom.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.GME;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class AttributeModifier extends FlavourBuff {
    {
        type = buffType.NEUTRAL;
        announced = true;
    }

    public float atk_m = 1f;
    public float def_m = 1f;
    public float acc_m = 1f;
    public float eva_m = 1f;
    public float dmg_m = 1f;
    public float hp_m =  1f;
    public float atk_l = 0f;
    public float def_l = 0f;
    public float acc_l = 0f;
    public float eva_l = 0f;
    public float dmg_l = 0f;
    public float hp_l =  0f;
    public int orig_HT = -1;

    public void merge(AttributeModifier another){
        atk_m *= another.atk_m;
        def_m *= another.def_m;
        acc_m *= another.acc_m;
        eva_m *= another.eva_m;
        dmg_m *= another.dmg_m;
        hp_m  *= another.hp_m;
        atk_l += another.atk_l;
        def_l += another.def_l;
        acc_l += another.acc_l;
        eva_l += another.eva_l;
        dmg_l += another.dmg_l;
        hp_l  += another.hp_l;
    }

    public int affectAtk(float attackPower){
        return GME.accurateRound(attackPower * atk_m + atk_l);
    }

    public int affectDef(float defensePower){
        return GME.accurateRound(defensePower * def_m + def_l);
    }

    public float affectAcc(float accuracy){
        return accuracy * acc_m + acc_l;
    }

    public float affectEva(float evasion){
        return evasion * eva_m + eva_l;
    }

    public int affectDmg(float damage){
        return GME.accurateRound(damage * dmg_m + dmg_l);
    }

    public void affectHp(Char ch){
        float percent = (float) ch.HP / ch.HT;
        //HT of Char is bundled, need to roll back first
        if(orig_HT < 0) {
            orig_HT = ch.HT;
        }else{
            ch.HT = orig_HT;
        }
        ch.HT = (int)(ch.HT * hp_m + hp_l);
        if(ch.HT <= 0){
            ch.HT = 1;
        }
        ch.HP = GME.accurateRound(ch.HT * percent);
        if(ch.HP <= 0){
            ch.HP = 1;
        }
    }

    public void setHPBack(Char ch){
        float percent = (float) ch.HP / ch.HT;
        ch.HT = orig_HT;
        boolean alive = ch.isAlive();
        ch.HP = GME.accurateRound(ch.HT * percent);
        if(ch.HP <= 0 && alive) ch.HP = 1;
    }

    public AttributeModifier setAtk(float m, float l){
        atk_m = m; atk_l = l;
        return this;
    }

    public AttributeModifier setDef(float m, float l){
        def_m = m; def_l = l;
        return this;
    }

    public AttributeModifier setAcc(float m, float l){
        acc_m = m; acc_l = l;
        return this;
    }

    public AttributeModifier setEva(float m, float l){
        eva_m = m; eva_l = l;
        return this;
    }

    public AttributeModifier setDmg(float m, float l){
        dmg_m = m; dmg_l = l;
        return this;
    }

    public AttributeModifier setHP(float m, float l){
        hp_m = m; hp_l = l;
        if(target != null){
            affectHp(target);
        }
        return this;
    }

    public AttributeModifier setAll(float[] mul, float[] add){
        atk_m = mul[0];
        def_m = mul[1];
        acc_m = mul[2];
        eva_m = mul[3];
        dmg_m = mul[4];
        hp_m = mul[5];
        atk_l = add[0];
        def_l = add[1];
        acc_l = add[2];
        eva_l = add[3];
        dmg_l = add[4];
        hp_l = add[5];
        if(target != null){
            affectHp(target);
        }
        return this;
    }

    @Override
    public boolean attachTo(Char target) {
        for(Buff b: target.buffs()){
            if(b instanceof AttributeModifier){
                merge((AttributeModifier) b);
                b.detach();
            }
        }
        boolean ret_val = super.attachTo(target);
        if(ret_val){
            affectHp(target);
        }
        return ret_val;
    }

    @Override
    public void detach() {
        setHPBack(target);
        super.detach();
    }

    @Override
    public int icon() {
        return BuffIndicator.COMBO;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", atk_m, atk_l, def_m, def_l, acc_m, acc_l, eva_m, eva_l, dmg_m, dmg_l, hp_m, hp_l, target.HP, target.HT);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("atk_mul", atk_m);
        bundle.put("def_mul", def_m);
        bundle.put("acc_mul", acc_m);
        bundle.put("eva_mul", eva_m);
        bundle.put("dmg_mul", dmg_m);
        bundle.put("hp_mul", hp_m);
        bundle.put("atk_lin", atk_l);
        bundle.put("def_lin", def_l);
        bundle.put("acc_lin", acc_l);
        bundle.put("eva_lin", eva_l);
        bundle.put("dmg_lin", dmg_l);
        bundle.put("hp_lin", hp_l);
        bundle.put("orig_hp", orig_HT);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        atk_m = bundle.getFloat("atk_mul");
        def_m = bundle.getFloat("def_mul");
        acc_m = bundle.getFloat("acc_mul");
        eva_m = bundle.getFloat("eva_mul");
        dmg_m = bundle.getFloat("dmg_mul");
        hp_m  = bundle.getFloat("hp_mul");
        atk_l = bundle.getFloat("atk_lin");
        def_l = bundle.getFloat("def_lin");
        acc_l = bundle.getFloat("acc_lin");
        eva_l = bundle.getFloat("eva_lin");
        dmg_l = bundle.getFloat("dmg_lin");
        hp_l  = bundle.getFloat("hp_lin");
        orig_HT = bundle.getInt("orig_hp");
    }
}
