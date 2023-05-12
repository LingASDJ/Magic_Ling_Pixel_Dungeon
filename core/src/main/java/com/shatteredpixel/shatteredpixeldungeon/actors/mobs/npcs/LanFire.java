package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.IceHealHP;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LanFireSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class LanFire extends NPC {
    {
        spriteClass = LanFireSprites.class;
        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);
    }
    public static boolean seenBefore = false;

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public boolean interact(Char c) {
        MysteryMeat mysteryMeat = Dungeon.hero.belongings.getItem(MysteryMeat.class);
        ChargrilledMeat chargrilledMeat = new ChargrilledMeat();
        if( mysteryMeat != null){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.MEAT),
                            Messages.get(LanFire.class, "meatTitle"),
                            Messages.get(LanFire.class, "meatDesc"),
                            Messages.get(LanFire.class, "one_meat"),
                            Messages.get(LanFire.class, "three_meat"),
                            Messages.get(LanFire.class, "all_meat")+
                            (mysteryMeat.quantity()/5f < 1 ? 10f :
                            mysteryMeat.quantity()/5f)+Messages.get(LanFire.class, "round"),
                            Messages.get(LanFire.class, "cancel")){
                        @Override
                        protected void onSelect(int index) {
                            super.onSelect(index);
                            if(index == 0) {
                                hero.healIcehp(Math.max(0,1));
                                GameScene.pickUp(chargrilledMeat, Dungeon.hero.pos);
                                chargrilledMeat.quantity(1).collect();
                                mysteryMeat.detach(hero.belongings.backpack);
                                hero.spend( 3f );
                                hero.busy();
                                hero.sprite.operate(hero.pos);
                                GLog.p("你烹饪了一个神秘的肉，回复了少量的温度。");
                            } else if(index == 1 ) {
                                if(mysteryMeat.quantity() >= 3){
                                    hero.healIcehp(Math.max(0, 3));
                                    GameScene.pickUp(chargrilledMeat, Dungeon.hero.pos);
                                    chargrilledMeat.quantity(3).collect();
                                    mysteryMeat.detach(hero.belongings.backpack);
                                    mysteryMeat.detach(hero.belongings.backpack);
                                    mysteryMeat.detach(hero.belongings.backpack);
                                    hero.spend(9f);
                                    hero.busy();
                                    hero.sprite.operate(hero.pos);
                                    GLog.p("你烹饪了三个神秘的肉，回复了一些的温度。");
                                } else {
                                    GLog.p("你并没有3个神秘的肉！");
                                }
                            } else if (index == 2){
                                GameScene.pickUp(chargrilledMeat, Dungeon.hero.pos);
                                hero.healIcehp(Math.max(0,mysteryMeat.quantity()));
                                chargrilledMeat.quantity(mysteryMeat.quantity()).collect();
                                mysteryMeat.detachAll( hero.belongings.backpack );
                                GLog.p("你将所有神秘的肉全部烹饪了，同时回复了大量的温度。");
                                hero.busy();
                                hero.spend( mysteryMeat.quantity()/5f < 1 ? 10f : mysteryMeat.quantity()/5f );
                                hero.sprite.operate(hero.pos);
                            }
                        }
                    });
                }
            }
            );
        }
        return false;
    }

    @Override
    protected boolean act() {
        if (!seenBefore && Dungeon.level.heroFOV[pos] && Dungeon.level.distance(pos, hero.pos) <= 3) {
            seenBefore = true;
            Buff.affect(hero, IceHealHP.class).set( (100), 1 );
            GLog.p("篝火的温暖照亮每一个在寒冷中徒步的冒险者，在寒冷而又凛冽的地牢中，这里是唯一的休息站。");
            spend(TICK);
            return true;
        } else if(seenBefore && !Dungeon.level.heroFOV[pos]) {
            seenBefore = false;
            Buff.detach( hero, IceHealHP.class );
            GLog.n("离开温暖的篝火休息站，前方迎面而来的又是凛冽的寒风。");
            return true;
        }
        return super.act();
    }


}
