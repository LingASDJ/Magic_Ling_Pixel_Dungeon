package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rlpt;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GunHuntsmanSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GunHuntsman extends Mob {

    {
        spriteClass = GunHuntsmanSprite.class;

        HP = HT = 45;
        defenseSkill = 15;

        EXP = 10;

        maxLvl = 30;

        //at half quantity, see createLoot()
        loot = Generator.Category.MISSILE;
        lootChance = 0.5f;
    }

    @Override
    public void move( int step ) {
        super.move(step);
        reload = Math.max(0, reload - 1);
        if(reload == 1){
            sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "fuck"));
        }
    }

    @Override
    public int damageRoll() {
        return Char.combatRoll( 10, 12 );
    }

    private int combo = 0;

    private int reload;

    @Override
    public int attackSkill( Char target ) {
        return 27;
    }

    @Override
    public String info() {
        String desc = super.info();

        if(reload != 0){
            desc += "\n\n"+Messages.get(this, "reload_desc",reload);
        }


        return desc;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return !Dungeon.level.adjacent( pos, enemy.pos ) && (super.canAttack(enemy) && reload==0 || new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos) && reload==0;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );

        combo++;

        sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "reload"));
        reload = Random.Int(9,17);

        if(combo>8){
            combo = 0;
        }

        if(Random.Float()<0.15f&& enemy.buff(Burning.class) == null){
            if (Dungeon.level.flamable[enemy.pos])
                GameScene.add(Blob.seed(enemy.pos, 7+combo, Fire.class));
            Buff.affect(enemy, Burning.class).reignite( enemy );
        }

        return damage;
    }

    @Override
    protected boolean getCloser( int target ) {
        combo = 0; //if he's moving, he isn't attacking, reset combo.
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    public void aggro(Char ch) {
        //cannot be aggroed to something it can't see
        if (ch == null || fieldOfView == null || fieldOfView[ch.pos]) {
            super.aggro(ch);
        }
    }

    @Override
    public Item createLoot() {
        MissileWeapon drop = (MissileWeapon)super.createLoot();
        //half quantity, rounded up
        drop.quantity((drop.quantity()+1)/2);
        return drop;
    }

    private static final String COMBO = "combo";

    private static final String RELOAD = "roload";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(COMBO, combo);
        bundle.put(RELOAD,reload);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        combo = bundle.getInt( COMBO );
        reload = bundle.getInt(RELOAD);
    }

}
