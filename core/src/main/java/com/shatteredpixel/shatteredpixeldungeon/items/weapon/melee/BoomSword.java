package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ArcaneBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShockBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShrapnelBomb;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BoomSword extends MeleeWeapon implements Item.AnimationItem {

    public static final String AC_ZAP = "ZAP";

    public int ammo = 0;

    private Bomb nextBomb = null;

    {
        image = ItemSpriteSheet.BOMB_SWORD;
        tier = 5;
        animation = true;
        usesTargeting = true;
    }

    public int maxAmmo() {
        return 1 + level() / 2;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);

        if (ammo > 0){
            actions.add(AC_ZAP);
        } else {
            actions.remove(AC_ZAP);
        }

        return actions;
    }

    @Override
    public String defaultAction() {
        return ammo > 0 ? AC_ZAP : null;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_ZAP)) {
            if (ammo > 0) {
                GameScene.selectCell(zapper);
            } else {
                GLog.n(Messages.get(this, "no_ammo"));
            }
        }
    }

    protected CellSelector.Listener zapper = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                if (Dungeon.level.avoid[target] || !Dungeon.level.passable[target]){
                    GLog.w( Messages.get(this, "cant_reach") );
                    return;
                }

                ammo--;
                updateQuickslot();

                curUser.sprite.zap(target);
                Sample.INSTANCE.play(Assets.Sounds.MISS);

                final Bomb bomb = nextBomb;
                if (ammo > 0) {
                    nextBomb = generateBomb();
                } else {
                    nextBomb = null;
                }

                bomb.isLit = true;

                ((MissileSprite) hero.sprite.parent.recycle(MissileSprite.class)).
                        reset(hero.sprite, target, bomb, new Callback() {
                            @Override
                            public void call() {
                                bomb.explode(target);
                                Invisibility.dispel();
                                hero.spendAndNext(1f);
                            }
                        });
            }
        }

        @Override
        public String prompt() {
            return Messages.get(BoomSword.this, "prompt");
        }
    };


    private Bomb generateBomb() {
        Bomb bomb = new Bomb();
        if (level() >= 2) {
            switch (Random.Int(8)) {
                case 0:
                    bomb = new ArcaneBomb();
                    break;
                case 1:
                    bomb = new Firebomb();
                    break;
                case 2:
                    bomb = new Flashbang();
                    break;
                case 3:
                    bomb = new FrostBomb();
                    break;
                case 4:
                    bomb = new ShrapnelBomb();
                    break;
                case 5:
                    bomb = new ShockBomb();
                    break;
                case 6:
                    bomb = new Noisemaker();
                    break;
                case 7:
                    bomb = new HolyBomb();
                    break;
            }
        }
        return bomb;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 击败敌人时填充弹药
        if (defender.HP <= damage && Random.Float() < 0.25f + level() * 0.05f) {
            if (ammo < maxAmmo()) {
                ammo++;
                // 如果这是第一发弹药，或者刚刚用掉了上一发，生成新的炸弹
                if (nextBomb == null) {
                    nextBomb = generateBomb();
                    String bombName = Messages.get(nextBomb.getClass(), "name");
                    GLog.p(Messages.get(this, "ammo_ready", bombName));
                } else {
                    GLog.p(Messages.get(this, "ammo_added"));
                }

                if (attacker instanceof Hero) {
                    attacker.sprite.showStatus(CharSprite.POSITIVE, "AMMO!");
                }
                updateQuickslot();
            }
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    public String info() {
        String info = super.info();

        if (ammo > 0) {
            String bombName = Messages.get(nextBomb.getClass(), "name");
            info += "\n\n" + Messages.get(this, "has_ammo", ammo, maxAmmo(), bombName);
        } else {
            info += "\n\n" + Messages.get(this, "needs_ammo");
        }

        return info;
    }

    @Override
    public int min(int lvl) {
        return 8 + lvl * 3;
    }

    @Override
    public int max(int lvl) {
        return 15 + lvl * 6;
    }

    @Override
    public void frames(ItemSprite itemSprite) {
        if (animation) {
            itemSprite.texture(Assets.Sprites.ANIMATIONS_BOMBSWORD);
            TextureFilm frames = new TextureFilm(itemSprite.texture, 16, 16);
            MovieClip.Animation idle = new MovieClip.Animation(14, true);
            idle.frames(frames, 0, 0, 1, 1, 2, 2, 2, 3, 3);
            itemSprite.play(idle);
        } else {
            itemSprite.view(image(), glowing());
        }
    }

    @Override
    public void storeInBundle(com.watabou.utils.Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("ammo", ammo);
        bundle.put("bomb",nextBomb);
    }

    @Override
    public void restoreFromBundle(com.watabou.utils.Bundle bundle) {
        super.restoreFromBundle(bundle);
        ammo = bundle.getInt("ammo");
        if (ammo > 0) {
            nextBomb = generateBomb();
        }
    }
}
