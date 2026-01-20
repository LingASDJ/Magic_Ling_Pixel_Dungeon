package com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.GhostTemplate;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Anger;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Junko;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Pink;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Smart;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class PacManQuest extends Item {

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public static void GetScore(Hero hero,int score){
        if(hero.buff(ScoreBuff.class)!=null){
            ScoreBuff buff = hero.buff(ScoreBuff.class);
            buff.addScore(score);
        }
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        GameScene.pickUp(this, pos);
        Sample.INSTANCE.play( Assets.Sounds.ITEM );
        hero.spendAndNext( 0f );
        Catalog.setSeen(getClass());
        return true;
    }

    public void autocollect(Item item, int pos){
        PacManQuest.this.doPickUp(Dungeon.hero,pos);
    }

    public static class SmallPoint extends PacManQuest {

        {
            image = ItemSpriteSheet.SMALL_POINT;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            super.doPickUp(hero, pos);
            int score = 10;
            GetScore(hero,score);
            hero.sprite.showStatus(Window.TITLE_COLOR, "+"+score);
            return true;
        }
    }

    public static class BigPoint extends PacManQuest {

        {
            image = ItemSpriteSheet.BIG_POINT;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            super.doPickUp(hero, pos);
            int score = 50;
            GetScore(hero,score);
            hero.sprite.showStatus(Window.TITLE_COLOR, "+"+score);
            Buff.affect(hero, AntiAttack.class, AntiAttack.DURATION);
            return true;
        }
    }

    public static class Lollipop extends PacManQuest {

        {
            image = ItemSpriteSheet.LOLLIPOP;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            super.doPickUp(hero, pos);
            int score = 200;
            GetScore(hero,score);
            hero.sprite.showStatus(Window.TITLE_COLOR, "+"+score);
            return true;
        }
    }

    public static class Gumdrop extends PacManQuest {

        {
            image = ItemSpriteSheet.GUMDROP;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            super.doPickUp(hero, pos);
            int score = 300;
            GetScore(hero,score);
            hero.sprite.showStatus(Window.TITLE_COLOR, "+"+score);
            return true;
        }
    }

    public static class SugarBomb extends PacManQuest {

        {
            image = ItemSpriteSheet.BOMB_PACMAN;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            super.doPickUp(hero, pos);
            List<Mob> ghosts = new ArrayList<>();
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof Ghost_Junko ||
                        mob instanceof Ghost_Anger ||
                        mob instanceof Ghost_Smart ||
                        mob instanceof Ghost_Pink) {
                    ghosts.add(mob);
                }
            }
            if (!ghosts.isEmpty()) {
                Mob ghostToKill = Random.element(ghosts);
                if (ghostToKill != null) {
                    GetScore(hero,200);
                    hero.sprite.showStatus(Window.Pink_COLOR, "+"+200);
                    ghostToKill.damage(100,this, Char.DamageType.REAL);
                }
            }
            return true;
        }
    }

    public static class Toffee extends PacManQuest {

        {
            image = ItemSpriteSheet.TOFFEE;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            super.doPickUp(hero, pos);
            int score = 500;
            GetScore(hero,score);
            hero.sprite.showStatus(Window.TITLE_COLOR, "+"+score);
            return true;
        }
    }

    public static class Chocolate extends PacManQuest {

        {
            image = ItemSpriteSheet.CHOCOLATE;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            super.doPickUp(hero, pos);
            int score = 300;
            GetScore(hero,score);
            hero.sprite.showStatus(Window.TITLE_COLOR, "+"+score);
            return true;
        }
    }


    public static class AntiAttack extends FlavourBuff {

        public static final float DURATION	= 30f;
        public int Plus;
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.GHOST_SCARY;
        }

        @Override
        public void detach(){
            super.detach();
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob instanceof GhostTemplate) {
                    ((GhostTemplate) mob).active = false;
                }
            }
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        private static final String PLUS =  "plus";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( PLUS, Plus);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            Plus = bundle.getInt(PLUS);
        }

    }

    public static class RandomItemPlus extends Buff {
        public int Plus;
        public boolean onlyItem = false;
        {
            type = buffType.POSITIVE;
        }

        @Override
        public boolean act() {
            if (target.isAlive()) {
                if(Plus == 0) {
                    Plus = Random.IntRange(56, 115);
                } else if(Plus == 1 && onlyItem){
                    onlyItem = false;
                    Plus = Random.IntRange(56, 115);
                } else if(Plus > 1) {
                    Plus--;
                }
                spend( 1f );
            } else {
                detach();
            }
            return true;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", Plus);
        }

        private static final String PLUS =  "plus";
        private static final String ONLYITEM =  "onlyItem";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( PLUS, Plus);
            bundle.put( ONLYITEM, onlyItem);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            Plus = bundle.getInt(PLUS);
            onlyItem = bundle.getBoolean(ONLYITEM);
        }

    }

}
