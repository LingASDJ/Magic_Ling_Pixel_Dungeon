package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SaiPlus extends MeleeWeapon {
    public int R;

    {
        image = ItemSpriteSheet.THEDIED;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.3f;
        tier = 5;
        DLY = 1;
        ACC = 1;
        RCH = 1;
    }

    public String statsInfo(){
        return (Messages.get(this,"stats_info"));
    }

    @Override
    public int STRReq(int lvl) {
        int req = (7 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero)owner;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                //deals 25% toward max to max on surprise, instead of min to max.
                int diff = max() - min();
                int damage = augment.damageFactor(Random.NormalIntRange(
                        min() + Math.round(diff*0.25f),
                        max()));
                int exStr = hero.STR() - STRReq();
                if (exStr > 0) {
                    damage += Hero.heroDamageIntRange(0, exStr);
                }
                return damage;
            }
        }
        return super.damageRoll(owner);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        float lifeStealPercentage = (float) (3 + 0.6 * level() + 1) * damage; // 偷袭成功时恢复生命的百分比

        int maxChance = 35; // 封顶概率为35%
        int chance = 5; // 初始概率为5%
        int additionalChance = Random.Int(1, 5);

        // 根据武器等级计算概率
        for (int i = 0; i < level(); i++) {
            chance += additionalChance;
        }

        // 如果概率超过封顶，则设置为封顶概率
        chance = Math.min(chance, maxChance);

        if (attacker instanceof Hero) {
            Hero hero = (Hero) attacker;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                if (Random.Int(0, 100) <= chance) {
                    // 检查是否触发偷袭成功
                    int stolenLife = Math.min(attacker.HT - attacker.HP, (int) (lifeStealPercentage / 30f));
                    attacker.HP += stolenLife;
                    attacker.sprite.showStatus(CharSprite.POSITIVE, "+" + stolenLife + " HP");
                    GLog.p(Messages.get(this, "success", attacker.name()));
                }
            }
        }

        if (Random.Float() < 0.2f && level()>=3) {
            damage = (new Grim()).proc(this, attacker, defender, damage);
        }

        return super.proc(attacker, defender, damage);
    }

    @Override
    public int min(int lvl) {
        return 5 + lvl;  // 最小伤害
    }
    @Override
    public int max(int lvl) {
        return 22 + lvl * 6;
    }

    @Override
    public int iceCoinValue() {
        if (Badges.isUnlocked(Badges.Badge.NYZ_SHOP)){
            return (int) ((175 + tier*25) * 0.9f);
        }
        return 175 + tier*25;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    public boolean useTargeting(){
        return false;
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        sneakAbility(hero, target, 4, 2+buffedLvl(), this);
    }

    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc", 2+buffedLvl());
        } else {
            return Messages.get(this, "typical_ability_desc", 2);
        }
    }

    @Override
    public String upgradeAbilityStat(int level) {
        return Integer.toString(2+level);
    }

    public static void sneakAbility(Hero hero, Integer target, int maxDist, int invisTurns, MeleeWeapon wep){
        if (target == null) {
            return;
        }

        PathFinder.buildDistanceMap(Dungeon.hero.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null), maxDist);
        if (PathFinder.distance[target] == Integer.MAX_VALUE || !Dungeon.level.heroFOV[target] || hero.rooted) {
            GLog.w(Messages.get(wep, "ability_target_range"));
            if (Dungeon.hero.rooted) PixelScene.shake( 1, 1f );
            return;
        }

        if (Actor.findChar(target) != null) {
            GLog.w(Messages.get(wep, "ability_occupied"));
            return;
        }

        wep.beforeAbilityUsed(hero, null);
        Buff.affect(hero, Invisibility.class, invisTurns-1); //1 fewer turns as ability is instant

        Dungeon.hero.sprite.turnTo( Dungeon.hero.pos, target);
        Dungeon.hero.pos = target;
        Dungeon.level.occupyCell(Dungeon.hero);
        Dungeon.observe();
        GameScene.updateFog();
        Dungeon.hero.checkVisibleMobs();

        Dungeon.hero.sprite.place( Dungeon.hero.pos );
        CellEmitter.get( Dungeon.hero.pos ).burst( Speck.factory( Speck.WOOL ), 6 );
        Sample.INSTANCE.play( Assets.Sounds.PUFF );

        hero.next();
        wep.afterAbilityUsed(hero);
    }

}
