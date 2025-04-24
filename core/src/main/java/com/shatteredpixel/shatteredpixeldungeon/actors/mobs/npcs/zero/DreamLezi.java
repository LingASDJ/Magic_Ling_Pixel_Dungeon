package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.extra.ScrollOfTeleTation;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DreamSprite;
import com.watabou.utils.Bundle;

public class DreamLezi extends NTNPC {

    {
        spriteClass = DreamSprite.class;
    }

    private boolean first=true;
    private boolean secnod=true;
    private boolean rd=true;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
    }

    @Override
    public boolean interact(Char c) {
        if (c != hero) return true;

        //TODO 赶时间 潘多拉别鲨我
        if(first){
            Dungeon.gold -= 720;
            yell("没收720金币");
            first = false;
        } else if(secnod){
           secnod = false;yell("嬗变一下吧！但是这个会必定诅咒哦！并且只能在0层使用哦！");
            Dungeon.level.drop(new ScrollOfTeleTation(), hero.pos);
        } else {
            yell("新年快乐，我的动物园好看吗？");
        }




        return true;
    }
}
