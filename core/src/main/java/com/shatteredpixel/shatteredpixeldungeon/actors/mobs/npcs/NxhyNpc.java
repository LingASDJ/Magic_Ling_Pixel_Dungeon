package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NxhySprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class NxhyNpc extends NPC {

    private static final String[] TXT_RANDOM = {"你知道吗？我还有个妹妹失散在了地牢之中，她叫夏洛。希望你能找到她！\n" +
            "我记得我还有把祖传的尚方宝剑，可是我遗落在地牢某处了，希望你在地牢里可以所向披靡。"};

    {
        spriteClass = NxhySprite.class;

        properties.add(Property.IMMOVABLE);
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
    }



    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
    }

    @Override
    protected boolean act() {

        throwItem();

        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );
        return true;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, Dungeon.hero.pos);
        if(first) {
            first=false;
            tell(Messages.get(NxhyNpc.class, "message1"));
        }else if(secnod) {
            secnod=false;
            tell(Messages.get(NxhyNpc.class, "message2"));
        } else if(secnod) {
            rd=false;
            tell(Messages.get(NxhyNpc.class, "message3"));
        } else {
            GLog.b(TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
        }

        return true;
    }

    private void tell(String text) {
        Game.runOnRenderThread(new Callback() {
                                   @Override
                                   public void call() {
                                       GameScene.show(new WndQuest(new NxhyNpc(), text));
                                   }
                               }
        );
    }
}
