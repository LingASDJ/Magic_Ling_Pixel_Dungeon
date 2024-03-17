package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ArcaneBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShrapnelBomb;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Random;

public class BoomSword extends MeleeWeapon implements Item.AnimationItem {

    public BoomSword() {
        image = ItemSpriteSheet.BOMB_SWORD;
        tier = 5;
       // animation = true;
    }

    @Override
    public void frames(ItemSprite itemSprite){
        if(animation) {
            itemSprite.texture(Assets.Sprites.ANIMATIONS_BOMBSWORD);
            TextureFilm frames = new TextureFilm(itemSprite.texture, 16, 16);
            MovieClip.Animation idle = new MovieClip.Animation(8, true);
            idle.frames( frames,0,0,1,1,2,2,2,3,3,4,4,5,5);
            itemSprite.play(idle);
        } else {
            itemSprite.view(image(),glowing());
        }
    }

    @Override
    public int damageRoll(Char attacker) {
        if (attacker instanceof Hero) {
            Hero hero = (Hero) attacker;
            Mob enemy = (Mob) hero.enemy();
            if (enemy != null && enemy.surprisedBy(hero)) {
                int maxDamage = max() + 2;
                int minDamage = min();
                int damage = augment.damageFactor(Random.NormalIntRange(minDamage + Math.round((maxDamage - minDamage) * 0.75f), maxDamage));
                int strengthDifference = hero.STR() - STRReq();
                if (strengthDifference > 0) {
                    damage += Random.IntRange(0, strengthDifference);
                }
                return damage;
            }
        }
        return super.damageRoll(attacker);
    }


    @Override
    public int proc(Char attacker, Char defender, int damage) {
        spawnBomb(defender.pos);
        return super.proc(attacker, defender, damage);
    }

    public Bomb missile = new Firebomb();
    private Bomb missile() {
        switch (Random.Int(11)) {
            case 1:
                missile = new ArcaneBomb();
                break;
            case 2:
                missile = new Firebomb();
                break;
            case 3:
                missile = new Flashbang();
                break;
            case 4:
                missile = new FrostBomb();
                break;
            default:
            case 8:
                missile = new ShrapnelBomb();
                break;
        }
        return missile;
    }

    @Override
    public int min(int lvl) {
        return 3 + lvl*3;
    }

    @Override
    public int max(int lvl) {
        return  10 + lvl*6;
    }

    private void spawnBomb(int position) {
        Bomb bomb;
        if(level()>=4){
            bomb = missile();
        } else {
            bomb = new Bomb();
        }
        Bomb.Fuse fuse = new Bomb.Fuse();
        bomb.isLit = true;
        bomb.quantity(0);
        fuse.bomb = bomb;
        bomb.fuse = fuse;
        Actor.add(fuse, Actor.now + 3.0f);
        Dungeon.level.drop(bomb, position).sprite.drop();
    }
}
