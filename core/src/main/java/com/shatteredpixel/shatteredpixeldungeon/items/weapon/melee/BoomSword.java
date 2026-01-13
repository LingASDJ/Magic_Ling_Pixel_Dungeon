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

    public BoomSword() {
        super();
        image = ItemSpriteSheet.BOMB_SWORD;
        tier = 5;
        animation = true;
        usesTargeting = true;  // 启用目标选择
    }

    // 定义技能动作
    public static final String AC_ZAP = "ZAP";

    // 弹药状态
    public boolean hasAmmo = false;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);

        if (hasAmmo){
            actions.add(AC_ZAP);
        } else {
            actions.remove(AC_ZAP);
        }

        return actions;
    }

    @Override
    public String defaultAction() {
        return AC_ZAP;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_ZAP)) {
            if (hasAmmo) {
                GameScene.selectCell(zapper);
            } else {
                GLog.n(Messages.get(this, "no_ammo"));
            }
        }
    }

    // 目标选择器
    protected CellSelector.Listener zapper = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                if (Dungeon.level.avoid[target] || !Dungeon.level.passable[target]){
                    GLog.w( Messages.get(this, "cant_reach") );
                    return;
                }

                // 消耗弹药
                hasAmmo = false;
                updateQuickslot();

                // 播放投掷动画和音效
                curUser.sprite.zap(target);
                Sample.INSTANCE.play(Assets.Sounds.MISS);

                // 创建炸弹逻辑
                hero.busy();
                final Bomb bomb = getBomb();
                bomb.isLit = true; // 设置为已点燃

                // 使用投掷物效果将炸弹送到目标点
                ((MissileSprite) hero.sprite.parent.recycle(MissileSprite.class)).
                        reset(hero.sprite, target, bomb, new Callback() {
                            @Override
                            public void call() {
                                // 炸弹到达目标点后立即引爆
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

    // 获取一个随机炸弹
    private Bomb getBomb() {
        Bomb bomb = new Firebomb(); // 默认
        if (level() >= 4) {
            switch (Random.Int(5)) {
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
            }
        }
        return bomb;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 击败敌人时填充弹药
        if (defender.HP <= damage && Random.Float() <0.25f + level()*0.05f) {
            if (!hasAmmo) {
                hasAmmo = true;
                GLog.p(Messages.get(this, "ammo_ready"));
                // 可以在这里添加一个视觉提示，比如武器发光
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
        if (hasAmmo) {
            info += "\n\n" + Messages.get(this, "has_ammo");
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

    // 动画相关
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

}
