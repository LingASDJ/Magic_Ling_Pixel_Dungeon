package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ScrollEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.LaserPython;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class EndingBlade extends Weapon {

    {
        image = ItemSpriteSheet.ENDDIED;
        tier = 4;
        cursed = true;
        enchant(Enchantment.randomCurse());
    }

    //三大基础静态固定字节
    public static final String AC_LASERCRYSTAL	= "lastcrystal";
    public static final String AC_DIEDGHOST	    = "diedghost";
    public static final String AC_HEALRESET	    = "healreset";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if(isEquipped( hero )) {
            //激光晶柱
            actions.add(AC_LASERCRYSTAL);
            //亡灵宣告
            actions.add(AC_DIEDGHOST);
            //再生之力
            actions.add(AC_HEALRESET);

        }
        return actions;
    }

    public int fireenergy;

    private boolean firstx=true;

    private static final String FIRST = "first";
    private static final String INTRPS = "intrps";
    private static final String TDM = "shocking_ordinals";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, firstx);
        bundle.put(INTRPS, fireenergy);
        bundle.put(TDM, TIME_TO_DIED);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        firstx = bundle.getBoolean(FIRST);
        fireenergy = bundle.getInt(INTRPS);
        TIME_TO_DIED = bundle.getInt(TDM);
    }

    public String desc() {
        return Messages.get(this, "desc")+"_"+fireenergy+"_"+Messages.get(this, "desc_2")+"_"+TIME_TO_DIED+"_";
    }

    //每100点浊焰能量自动升级
    @Override
    public int level() {
        int level = Dungeon.hero == null ? 0 : fireenergy/100;
        if (Dungeon.hero == null){
            level += 1;
        }
        return level;
    }
    private int TIME_TO_DIED = 0;
    public void execute( Hero hero, String action ) {
        super.execute( hero, action );

            switch (action) {
                case AC_LASERCRYSTAL:
                    if(fireenergy >= 300 && firstx){
                        firstx = false;
                        new LaserPython().quantity(1).identify().collect();
                        GLog.n("你突然感觉你的背包鼓鼓的……");
                    } else if(!firstx) {
                        GLog.n("你尚未使用激光晶柱，无法继续使用");
                    } else {
                        GLog.n("等级不足，需求等级为3");
                    }
                    break;
                case AC_DIEDGHOST:
                    if(fireenergy >= 500 && TIME_TO_DIED == 0) {
                        curUser = hero;
                        curItem = this;
                        GameScene.selectCell(zapper);
                    } else if (TIME_TO_DIED != 0) {
                        GLog.n("技能正在冷却");
                    } else {
                        GLog.n("等级不足,需求等级为5");
                    }
                    break;
                case AC_HEALRESET:
                    if(fireenergy >= 700 && 0.30f >= (float)(hero.HP/hero.HT) && !Statistics.wangzheguilai) {
                        //0.30f >= (float)(hero.HP/hero.HT)
                        PotionOfHealing.cure( hero );
                        hero.belongings.uncurseEquipped();
                        hero.buff( Hunger.class ).satisfy( Hunger.STARVING );
                        hero.HP = hero.HT;
                        hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
                        CellEmitter.get( hero.pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );
                        Dungeon.hero.interrupt();
                        GLog.p( Messages.get(this, "procced") );
                        Statistics.wangzheguilai = true;
                        cursed = false;
                        for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                            if (!(mob instanceof NPC) ) {
                                mob.damage( Random.NormalIntRange( 30*(level/5), 50*(level/5) ), new Eye.DeathGaze() );
                            }
                        }
                    } else if(Statistics.wangzheguilai) {
                        GLog.n("你本局已经使用千里追魂，无法再次使用。");
                    } else if(level < 7) {
                        GLog.n("你的等级尚未达到7级以上，打败更多的怪物，鲜血会让你的武器变得更强……");
                    } else if(0.30f >= (float)(hero.HP/hero.HT)) {
                        GLog.n("你的血量尚未低于30%，无法使用");
                    }
                    break;
            }
    }

    public int maxCharges = initialCharges();
    public int curCharges = maxCharges;
    public int collisionProperties(int target){
        return collisionProperties;
    }
    protected int collisionProperties = Ballistica.MAGIC_BOLT;
    protected int initialCharges() {
        return 2;
    }

    protected static CellSelector.Listener zapper = new  CellSelector.Listener() {

        @Override
        public void onSelect( Integer target ) {

            if (target != null) {

                //FIXME this safety check shouldn't be necessary
                //it would be better to eliminate the curItem static variable.
                final EndingBlade curWand;
                if (curItem instanceof EndingBlade) {
                    curWand = (EndingBlade) EndingBlade.curItem;
                } else {
                    return;
                }

                final Ballistica shot = new Ballistica( curUser.pos, target, curWand.collisionProperties(target));
                int cell = shot.collisionPos;

                if (target == curUser.pos || cell == curUser.pos) {
                    if (target == curUser.pos && curUser.hasTalent(Talent.SHIELD_BATTERY)){
                        float shield = curUser.HT * (0.04f*curWand.curCharges);
                        if (curUser.pointsInTalent(Talent.SHIELD_BATTERY) == 2) shield *= 1.5f;
                        Buff.affect(curUser, Barrier.class).setShield(Math.round(shield));
                        curWand.curCharges = 0;
                        curUser.sprite.operate(curUser.pos);
                        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
                        ScrollOfRecharging.charge(curUser);
                        updateQuickslot();
                        curUser.spend(Actor.TICK);
                        return;
                    }
                    GLog.i( Messages.get(Wand.class, "self_target") );
                    return;
                }

                curUser.sprite.zap(cell);

                //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));

                if (curWand.tryToZap(curUser, target)) {

                    curUser.busy();
                        curWand.fxs(shot, new Callback() {
                            public void call() {
                                curWand.onZap(shot);
                                curWand.wandUsed();
                            }
                        });
                    curWand.cursedKnown = true;

                }

            }
        }

        @Override
        public String prompt() {
            return Messages.get(Wand.class, "prompt");
        }
    };

    public boolean tryToZap( Hero owner, int target ){

        if (owner.buff(MagicImmune.class) != null){
            GLog.w( Messages.get(Wand.class, "no_magic") );
            return false;
        }

        return true;
    }

    protected Wand.Charger charger;

    private static final int USES_TO_ID = 10;
    private float usesLeftToID = USES_TO_ID;
    private float availableUsesToID = USES_TO_ID/2f;
    private static final float TIME_TO_ZAP	= 1f;

    public void wandUsed() {
        if (!isIdentified()) {
            float uses = Math.min( availableUsesToID, Talent.itemIDSpeedFactor(Dungeon.hero, this) );
            availableUsesToID -= uses;
            usesLeftToID -= uses;
            if (usesLeftToID <= 0 || Dungeon.hero.pointsInTalent(Talent.SCHOLARS_INTUITION) == 2) {
                identify();
                GLog.p( Messages.get(Wand.class, "identify") );
                Badges.validateItemLevelAquired( this );
            }
        }

        curCharges -= cursed ? 1 : chargesPerCast();

        //remove magic charge at a higher priority, if we are benefiting from it are and not the
        //wand that just applied it
        WandOfMagicMissile.MagicCharge buff = curUser.buff(WandOfMagicMissile.MagicCharge.class);
        if (buff != null
                && buff.wandJustAppliedX() != this
                && buff.level() == buffedLvl()
                && buffedLvl() > super.buffedLvl()){
            buff.detach();
        } else {
            ScrollEmpower empower = curUser.buff(ScrollEmpower.class);
            if (empower != null){
                empower.use();
            }
        }

        //if the wand is owned by the hero, but not in their inventory, it must be in the staff
        if (charger != null
                && charger.target == Dungeon.hero
                && !Dungeon.hero.belongings.contains(this)) {
            if (curCharges == 0 && Dungeon.hero.hasTalent(Talent.BACKUP_BARRIER)) {
                //grants 3/5 shielding
                Buff.affect(Dungeon.hero, Barrier.class).setShield(1 + 2 * Dungeon.hero.pointsInTalent(Talent.BACKUP_BARRIER));
            }
            if (Dungeon.hero.hasTalent(Talent.EMPOWERED_STRIKE)){
                Buff.prolong(Dungeon.hero, Talent.EmpoweredStrikeTracker.class, 10f);
            }
        }

        Invisibility.dispel();
        updateQuickslot();

        curUser.spendAndNext( TIME_TO_ZAP );
    }

    public void onZap( Ballistica beam ) {
        Char ch = Actor.findChar(beam.collisionPos);

        if (ch != null){

            if (!(ch instanceof Mob)){
                return;
            }

            Mob enemy = (Mob) ch;

            AllyBuff.affectAndLoot(enemy, curUser, Corruption.class);
            TIME_TO_DIED = 30;

            Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, 0.8f * Random.Float(0.87f, 1.15f) );
        }
    }

    protected int chargesPerCast() {
        return 2;
    }

    protected void wandProc(Char target, int chargesUsed){
        wandProc(target, buffedLvl(), chargesUsed);
    }

    //TODO Consider externalizing char awareness buff
    protected static void wandProc(Char target, int wandLevel, int chargesUsed){
        if (Dungeon.hero.hasTalent(Talent.ARCANE_VISION)) {
            int dur = 5 + 5*Dungeon.hero.pointsInTalent(Talent.ARCANE_VISION);
            Buff.append(Dungeon.hero, TalismanOfForesight.CharAwareness.class, dur).charID = target.id();
        }

        if (target != Dungeon.hero &&
                Dungeon.hero.subClass == HeroSubClass.WARLOCK &&
                //standard 1 - 0.92^x chance, plus 7%. Starts at 15%
                Random.Float() > (Math.pow(0.92f, (wandLevel*chargesUsed)+1) - 0.07f)){
            SoulMark.prolong(target, SoulMark.class, SoulMark.DURATION + wandLevel);
        }
    }

    private void corruptEnemy( Mob enemy ){
        //cannot re-corrupt or doom an enemy, so give them a major debuff instead
        if(enemy.buff(Corruption.class) != null || enemy.buff(Doom.class) != null){
            GLog.w( Messages.get(this, "already_corrupted") );
            return;
        }

        if (!enemy.isImmune(Corruption.class)){
            Corruption.corruptionHeal(enemy);

            AllyBuff.affectAndLoot(enemy, curUser, Corruption.class);
        } else {
            Buff.affect(enemy, Doom.class);
        }
    }

    public void fxs(Ballistica beam, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.SHADOW,
                curUser.sprite,
                beam.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
        //buildBeams(beam);
        callback.call();
    }

    @Override
    public int STRReq(int C) {
        return 15+fireenergy/100;
    }

    public int proc(Char attacker, Char defender, int damage ) {
        //常规加+1浊焰能量
        if(level >= 10) {
            fireenergy = 1000;
        } else {
            ++fireenergy;
        }
        int dmg;
        tier+= fireenergy/100;

        if(TIME_TO_DIED != 0){
            TIME_TO_DIED--;
        }
            //fixme
            //todo
        if(level >= 10){
            fireenergy += 0;
            //武器最高级
        } else if(defender.properties().contains(Char.Property.BOSS) && defender.HP <= damage){
            //目标Boss血量小于实际伤害判定为死亡,+20浊焰能量
            fireenergy+=40;
        } else if(defender.properties().contains(Char.Property.MINIBOSS) && defender.HP <= damage){
            //目标迷你Boss血量小于实际伤害判定为死亡,+10浊焰能量
            fireenergy+=20;
        } else if (defender.HP <= damage){
            //目标血量小于实际伤害判定为死亡,+5浊焰能量
            fireenergy+=10;
        }

        /*
         * 10级 死神附魔
         * 8级  紊乱附魔
         * 6级  电击附魔
         * 4级  烈焰附魔
         * 2级  恒动附魔
         */
        if(level>= 10){
            dmg = (new Grim()).proc(this, attacker, defender, damage) + 5;
            damage = dmg;
        } else if (level>= 8) {
            dmg = (new Unstable()).proc(this, attacker, defender, damage) + 4;
            damage = dmg;
        } else if (level>= 6){
            dmg = (new Shocking()).proc(this, attacker, defender, damage) + 3;
            damage = dmg;
        } else if (level>= 4){
            dmg = (new Blazing()).proc(this, attacker, defender, damage) + 2;
            damage = dmg;
        } else if (level>= 2){
            dmg = (new Kinetic()).proc(this, attacker, defender, damage) + 1;
            damage = dmg;
        }

        return super.proc(attacker, defender, damage);


    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    public int tier;

    @Override
    public String info() {

        String info = desc();

        if (levelKnown) {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", 4+fireenergy/100,
                    augment.damageFactor(min()),
                    augment.damageFactor(max()), STRReq());
            if (STRReq() > Dungeon.hero.STR()) {
                info += " " + Messages.get(Weapon.class, "too_heavy");
            } else if (Dungeon.hero.STR() > STRReq()){
                info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
            }
        } else {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown",  4+fireenergy/100, min(0), max(0), STRReq(0));
            if (STRReq(0) > Dungeon.hero.STR()) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
            }
        }

        switch (augment) {
            case SPEED:
                info += " " + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += " " + Messages.get(Weapon.class, "stronger");
                break;
            case NONE:
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())){
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }

        if (cursed && isEquipped( Dungeon.hero )) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown){
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }

        return info;
    }

    @Override
    public int min(int lvl) {
        return 10+(lvl*2)*(Dungeon.hero.lvl/10);
        //10+20x3=70
    }

    @Override
    public int max(int lvl) {
        return 15+(lvl*3)*(Dungeon.hero.lvl/10);
        //15+30x3=15+90=105
    }

    //Max 70~105
}
