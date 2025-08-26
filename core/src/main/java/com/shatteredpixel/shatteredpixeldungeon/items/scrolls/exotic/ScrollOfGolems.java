package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfAnmy;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ScrollOfGolems extends ExoticInventoryScroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_GOLEM;
        image = ItemSpriteSheet.SCROLL_GOLEM;
        bones = true;
        if(hero!=null){
            identify();
        }
    }

    @Override
    protected boolean usableOnItem(Item item) {
        return (item instanceof MeleeWeapon  ||
                item instanceof Armor) && !item.isEquipped(hero);
    }

    @Override
    protected void onItemSelected(Item item) {

        boolean cursedd = false;

        if (item instanceof MeleeWeapon){
            Sample.INSTANCE.play(Assets.Sounds.CHALLENGE,1f,0.8f);
            ArrayList<Integer> respawnPoints = new ArrayList<>();
            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = hero.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
                    respawnPoints.add( p );
                }
            }
            if (!respawnPoints.isEmpty()){
                if (item.cursed) {
                    cursedd = true;
                    item.cursed = false;
                }
                detach(hero.belongings.backpack);
                item.detach(hero.belongings.backpack);
                Statue statuewithmelee = new Statue((MeleeWeapon)item);
                if (((MeleeWeapon)item).enchantment!=null) statuewithmelee = new Statue((MeleeWeapon)item, ((MeleeWeapon)item).enchantment);
                statuewithmelee.HP = statuewithmelee.HT = hero.HP;
                statuewithmelee.alignment = Char.Alignment.ALLY;
                Buff.affect(statuewithmelee,WandOfAnmy.AllyToRestartOK.class);
                if (cursedd) statuewithmelee.HP = statuewithmelee.HT/2;
                statuewithmelee.pos = Random.element(respawnPoints);
                if (!cursedd)
                    CellEmitter.get(statuewithmelee.pos).burst(ElmoParticle.FACTORY, 10);
                else CellEmitter.get(statuewithmelee.pos).burst(BloodParticle.FACTORY, 10);
                statuewithmelee.state = statuewithmelee.HUNTING;
                GameScene.add(statuewithmelee);
            }

        } else if (item instanceof Armor){
            Sample.INSTANCE.play(Assets.Sounds.CHALLENGE,1f,0.8f);
            ArrayList<Integer> respawnPoints = new ArrayList<>();
            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = hero.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
                    respawnPoints.add( p );
                }
            }
            if (!respawnPoints.isEmpty()){
                if (item.cursed) {
                    cursedd = true;
                    item.cursed = false;
                }
                detach(hero.belongings.backpack);
                item.detach(hero.belongings.backpack);
                Statue statuewitharmor = new ArmoredStatue((Armor)item);
                if (((Armor)item).glyph!=null) statuewitharmor = new ArmoredStatue((Armor)item, ((Armor)item).glyph);
                statuewitharmor.HP = statuewitharmor.HT = hero.HP;
                if (cursedd) statuewitharmor.HP = statuewitharmor.HT/2;
                statuewitharmor.alignment = Char.Alignment.ALLY;
                statuewitharmor.pos = Random.element(respawnPoints);
                if (!cursedd)
                    CellEmitter.get(statuewitharmor.pos).burst(ElmoParticle.FACTORY, 10);
                else CellEmitter.get(statuewitharmor.pos).burst(BloodParticle.FACTORY, 10);
                statuewitharmor.state = statuewitharmor.HUNTING;
                GameScene.add(statuewitharmor);
            }

        }
    }
    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        identify();
    }

    @Override
    public int value() {
        return 50 * quantity;
    }

    @Override
    public int energyVal() {
        return 8 * quantity ;
    }
}
