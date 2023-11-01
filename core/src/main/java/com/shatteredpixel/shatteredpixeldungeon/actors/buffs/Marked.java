//package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;
//
//import com.shatteredpixel.shatteredpixeldungeon.Assets;
//import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
//import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
//import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
//import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroAction;
//import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BloodBat;
//import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
//import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
//import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
//import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
//import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
//import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
//import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
//import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
//import com.watabou.noosa.Image;
//import com.watabou.noosa.audio.Sample;
//import com.watabou.utils.Bundle;
//import com.watabou.utils.PathFinder;
//
//import java.text.DecimalFormat;
//
//public class Marked extends Buff implements ActionIndicator.Action {
//    {
//        type = buffType.NEGATIVE;
//        announced = true;
//    }
//
//    public int stack = 0;
//
//    @Override
//    public void storeInBundle(Bundle bundle) {
//        super.storeInBundle(bundle);
//        bundle.put("stack", stack);
//    }
//
//    @Override
//    public void restoreFromBundle(Bundle bundle) {
//        super.restoreFromBundle(bundle);
//        stack = bundle.getInt("stack");
//        if (stack > 0) ActionIndicator.setAction(this);
//    }
//
//    public float bonusDamage(){
//        return (float) Math.pow(1.085f, stack);
//    }
//
//    @Override
//    public int icon() {
//        return BuffIndicator.PREPARATION;
//    }
//
//    @Override
//    public void tintIcon(Image icon) {
//        switch (stack){
//            case 1: case 2: case 3:
//                icon.hardlight(0f, 1f, 0f);
//                break;
//            case 4: case 5: case 6:
//                icon.hardlight(1f, 1f, 0f);
//                break;
//            case 7: case 8: case 9:
//                icon.hardlight(1f, 0.6f, 0f);
//                break;
//            case 10: case 11: default:
//                icon.hardlight(1f, 0f, 0f);
//                break;
//        }
//    }
//
//    @Override
//    public boolean act() {
//        if (stack > 0){
//            ActionIndicator.setAction(this);
//        }
//        spend(TICK);
//        return true;
//    }
//
//    @Override
//    public void detach() {
//        super.detach();
//        ActionIndicator.clearAction(this);
//    }
//
//    @Override
//    public String toString() {
//        return Messages.get(this, "name");
//    }
//
//    @Override
//    public String desc() {
//        String desc = Messages.get(this, "desc");
//
//        desc += "\n\n" + Messages.get(this, "desc_dmg", new DecimalFormat("#.##").format(100f * Math.pow(1.085f, stack) - 100f));
//        if (stack >= 2)  desc += "\n\n" + Messages.get(this, "desc_poison");
//        if (stack >= 3)  desc += "\n" + Messages.get(this, "desc_cripple");
//        if (stack >= 4)  desc += "\n" + Messages.get(this, "desc_blinding");
//        if (stack >= 5) desc += "\n" + Messages.get(this, "desc_bleeding");
//        if (stack >= 6) desc += "\n" + Messages.get(this, "desc_vulnerable");
//        if (stack >= 7) desc += "\n" + Messages.get(this, "desc_hexed");
//        if (stack >= 8) desc += "\n" + Messages.get(this, "desc_slow");
//        if (stack >= 9) desc += "\n" + Messages.get(this, "desc_ooze");
//        if (stack >= 10) desc += "\n" + Messages.get(this, "desc_paralysis");
//        if (stack >= 11) desc += "\n" + Messages.get(this, "desc_doom");
//        return desc;
//    }
//
//    @Override
//    public String actionName() {
//        return null;
//    }
//
//    @Override
//    public Image actionIcon() {
//        return null;
//    }
//
//    @Override
//    public void doAction() {
//        GameScene.selectCell(attack);
//    }
//
//    private CellSelector.Listener attack = new CellSelector.Listener() {
//
//        @Override
//        public void onSelect(Integer cell) {
//            if (cell == null) return;
//            final Char enemy = Actor.findChar( cell );
//            if (enemy == null || enemy.buff(Marked.class) == null){
//                GLog.w(Messages.get(Marked.class, "no_target"));
//            } else {
//
//                if (Dungeon.hero.canAttack(enemy)){
//                    effect(enemy);
//                    Dungeon.hero.curAction = new HeroAction.Attack( enemy );
//                    Dungeon.hero.next();
//                    return;
//                }
//
//                boolean[] passable = Dungeon.level.passable.clone();
//                //need to consider enemy cell as passable in case they are on a trap or chasm
//                passable[cell] = true;
//                PathFinder.buildDistanceMap(Dungeon.hero.pos, passable, 100_000_000);
//                if (PathFinder.distance[cell] == Integer.MAX_VALUE){
//                    GLog.w(Messages.get(Preparation.class, "out_of_reach"));
//                    return;
//                }
//
//                //we can move through enemies when determining blink distance,
//                // but not when actually jumping to a location
//                for (Char ch : Actor.chars()){
//                    if (ch != Dungeon.hero)  passable[ch.pos] = false;
//                }
//
//                PathFinder.Path path = PathFinder.find(Dungeon.hero.pos, cell, passable);
//                int attackPos = path == null ? -1 : path.get(path.size()-2);
//
//                if (attackPos == -1 ||
//                        Dungeon.level.distance(attackPos, Dungeon.hero.pos) > 100_000_000){
//                    GLog.w(Messages.get(Preparation.class, "out_of_reach"));
//                    return;
//                }
//
//                Dungeon.hero.pos = attackPos;
//                Dungeon.level.occupyCell(Dungeon.hero);
//                //prevents the hero from being interrupted by seeing new enemies
//                Dungeon.observe();
//                Dungeon.hero.checkVisibleMobs();
//
//                Dungeon.hero.sprite.place( Dungeon.hero.pos );
//                Dungeon.hero.sprite.turnTo( Dungeon.hero.pos, cell);
//                CellEmitter.get( Dungeon.hero.pos ).burst( Speck.factory( Speck.WOOL ), 6 );
//                Sample.INSTANCE.play( Assets.Sounds.PUFF );
//                effect(enemy);
//                Dungeon.hero.curAction = new HeroAction.Attack( enemy );
//                Dungeon.hero.next();
//            }
//        }
//
//        @Override
//        public String prompt() {
//            return Messages.get(Marked.class, "prompt");
//        }
//    };
//
//    public static void effect(Char enemy){
//        Marked mark = enemy.buff(Marked.class);
//        if (mark.stack >= 2) Buff.affect(enemy, Poison.class).set(Dungeon.escalatingDepth());
//        if (mark.stack >= 3) Buff.affect(enemy, Cripple.class, 4f);
//        if (mark.stack >= 4) Buff.affect(enemy, Blindness.class, 4f);
//        if (mark.stack >= 5) Buff.affect(enemy, Bleeding.class).level = Dungeon.escalatingDepth();
//        if (mark.stack >= 6) Buff.affect(enemy, Vulnerable.class, 4f);
//        if (mark.stack >= 7) Buff.affect(enemy, Hex.class, 4f);
//        if (mark.stack >= 8) Buff.affect(enemy, Slow.class, 4f);
//        if (mark.stack >= 9) Buff.affect(enemy, Ooze.class).set(20f);
//        if (mark.stack >= 10) Buff.affect(enemy, Paralysis.class, 5f);
//        if (mark.stack >= 11) Buff.affect(enemy, Doom.class);
//
//        for (Char ch : Actor.chars()){
//            if (ch instanceof BloodBat){
//                ch.sprite.emitter().burst(Speck.factory(Speck.SMOKE), 10);
//                ch.HP = Math.min(ch.HT, ch.HP + ch.HT / 5 * mark.stack);
//            }
//        }
//        enemy.buff(Marked.class).detach();
//    }
//}
//
