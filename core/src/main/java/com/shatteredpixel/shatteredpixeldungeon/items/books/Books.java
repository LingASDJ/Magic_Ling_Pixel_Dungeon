package com.shatteredpixel.shatteredpixeldungeon.items.books;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.MainBooks;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBook;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Books extends MainBooks {

    //每本书的计数只能计算一次，避免玩家重复阅读同一本书刷成就。
    public boolean readOnly = false;

    {
        defaultAction = Read;
        stackable = true;
    }



    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }


    public String desc = Messages.get(this, "desc");
    public Integer icon = null;
    private static final String Read	= "Read";
    public Emitter emitter() { return null; }

    public ItemSprite.Glowing glowing() {
        return null;
    }
    @Override
    public int value() {
        return quantity * 12;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.HP > 0) {
            actions.add(Read);
        }
        actions.remove(AC_DROP);
        actions.remove(AC_THROW);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action ) {
        super.execute(hero, action);
        if (action.equals( Read ) && quantity>1) {
            if(!readOnly){
                Statistics.readBooks++;
                Badges.valiReadBooks();
                GameScene.show(new WndBook(this));
                readOnly = true;
            } else {
                GLog.w(Messages.get(MainBooks.class, "readed"));
            }
        } else {
            GLog.w( Messages.get(MainBooks.class, "no") );
        }
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.readOnly = bundle.getBoolean("readOnly");
    }

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("readOnly", this.readOnly);
    }


}
