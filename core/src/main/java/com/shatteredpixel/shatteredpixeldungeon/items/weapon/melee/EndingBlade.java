package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ScrollEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.BallisticaReal;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.LaserPython;
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
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

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
        return fireenergy/100;
    }
    private int TIME_TO_DIED = 0;
    public void execute( Hero hero, String action ) {
        super.execute( hero, action );

            switch (action) {
                case AC_LASERCRYSTAL:
                    if(level >= 4 && firstx){
                        firstx = false;
                        new LaserPython().quantity(1).identify().collect();
                        GLog.n("你突然感觉你的背包鼓鼓的……");
                    } else if(!firstx) {
                        GLog.n("你尚未使用激光晶柱，无法继续使用");
                    } else {
                        GLog.n("等级不足");
                    }
                    break;
                case AC_DIEDGHOST:
                    if(level >= 5 && TIME_TO_DIED == 0) {
                        curUser = hero;
                        curItem = this;
                        GameScene.selectCell(zapper);
                        TIME_TO_DIED = 20;
                    } else if (TIME_TO_DIED != 0) {
                        GLog.n("道具正在冷却");
                    } else {
                        GLog.n("等级不足");
                    }
                    break;
                case AC_HEALRESET:
                    GLog.w("3");
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

    protected int nReflections(int level){
        //return Math.min(2+level/3, 5);
        return 5;
    }

    protected float reflectionDamageFactor(int reflections){
        return 1f+0.1f*reflections*reflections;
    }

    public void onZap( Ballistica beam ) {
        int count = 0;
        for(BallisticaReal b: beams){
            onZapX(b, count);
            ++count;
        }
        Char ch = Actor.findChar(beam.collisionPos);

        if (ch != null){

            if (!(ch instanceof Mob)){
                return;
            }

            Mob enemy = (Mob) ch;

            float corruptingPower = 3 + buffedLvl()/2f;

            //base enemy resistance is usually based on their exp, but in special cases it is based on other criteria
            float enemyResist = 1 + enemy.EXP;
            if (ch instanceof Mimic || ch instanceof Statue){
                enemyResist = 1 + Dungeon.depth;
            } else if (ch instanceof Piranha || ch instanceof Bee) {
                enemyResist = 1 + Dungeon.depth/2f;
            } else if (ch instanceof Wraith) {
                //divide by 5 as wraiths are always at full HP and are therefore ~5x harder to corrupt
                enemyResist = (1f + Dungeon.depth/3f) / 5f;
            } else if (ch instanceof Swarm){
                //child swarms don't give exp, so we force this here.
                enemyResist = 1 + 3;
            }

            //100% health: 5x resist   75%: 3.25x resist   50%: 2x resist   25%: 1.25x resist
            enemyResist *= 1 + 4*Math.pow(enemy.HP/(float)enemy.HT, 2);

            //debuffs placed on the enemy reduce their resistance
            for (Buff buff : enemy.buffs()){
                if (MAJOR_DEBUFFS.containsKey(buff.getClass()))         enemyResist *= (1f-MAJOR_DEBUFF_WEAKEN);
                else if (MINOR_DEBUFFS.containsKey(buff.getClass()))    enemyResist *= (1f-MINOR_DEBUFF_WEAKEN);
                else if (buff.type == Buff.buffType.NEGATIVE)           enemyResist *= (1f-MINOR_DEBUFF_WEAKEN);
            }

            //cannot re-corrupt or doom an enemy, so give them a major debuff instead
            if(enemy.buff(Corruption.class) != null || enemy.buff(Doom.class) != null){
                corruptingPower = enemyResist - 0.001f;
            }

            if (corruptingPower > enemyResist){
                corruptEnemy( enemy );
            } else {
                float debuffChance = corruptingPower / enemyResist;
                if (Random.Float() < debuffChance){
                    debuffEnemy( enemy, MAJOR_DEBUFFS);
                } else {
                    debuffEnemy( enemy, MINOR_DEBUFFS);
                }
            }

            wandProc(ch, chargesPerCast());
            Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, 0.8f * Random.Float(0.87f, 1.15f) );
        }
    }

    public void onZapX(BallisticaReal beam,int reflection) {
        int level = buffedLvl();

        int maxDistance = beam.dist;

        ArrayList<Char> chars = new ArrayList<>();

        for (int c : beam.subPath((reflection>0?0:1), maxDistance)) {
            //prevent self_damage
            if(c==beam.sourceI && c==curUser.pos) continue;

            Char ch;
            if ((ch = Actor.findChar( c )) != null) {

                chars.add( ch );
            }

            if (Dungeon.level.flamable[c]) {
                Dungeon.level.destroy( c );
                GameScene.updateMap( c );

            }

            CellEmitter.center( c ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );

        }


        Dungeon.observe();

        boolean alive = curUser.isAlive();

        int lvl = level;
        for (Char ch : chars) {
            wandProc(ch, chargesPerCast());
            int damage = Math.round(2*reflectionDamageFactor(reflection));
            if(!ch.equals(curUser)) {
                ch.damage(damage, this);
            }else{
                ch.damage(damage/6, this);
            }
            ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
            ch.sprite.flash();
        }

        if (!curUser.isAlive() && alive) {
            Dungeon.fail( getClass() );
            GLog.n(Messages.get(this, "ondeath"));
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

    private static final float MINOR_DEBUFF_WEAKEN = 1/4f;
    private static final HashMap<Class<? extends Buff>, Float> MINOR_DEBUFFS = new HashMap<>();
    static{
        MINOR_DEBUFFS.put(Weakness.class,       2f);
        MINOR_DEBUFFS.put(Corruption.class,     2f);
        MINOR_DEBUFFS.put(Cripple.class,        1f);
        MINOR_DEBUFFS.put(Blindness.class,      1f);
        MINOR_DEBUFFS.put(Terror.class,         1f);

        MINOR_DEBUFFS.put(Chill.class,          0f);
        MINOR_DEBUFFS.put(Ooze.class,           0f);
        MINOR_DEBUFFS.put(Corruption.class,          4f);
        MINOR_DEBUFFS.put(Vertigo.class,        0f);
        MINOR_DEBUFFS.put(Drowsy.class,         0f);
        MINOR_DEBUFFS.put(Corruption.class,       10f);
        MINOR_DEBUFFS.put(Burning.class,        0f);
        MINOR_DEBUFFS.put(Poison.class,         0f);
    }

    private static final float MAJOR_DEBUFF_WEAKEN = 1/2f;
    private static final HashMap<Class<? extends Buff>, Float> MAJOR_DEBUFFS = new HashMap<>();
    static{
        MAJOR_DEBUFFS.put(Corruption.class,           3f);
        MAJOR_DEBUFFS.put(Slow.class,           2f);
        MAJOR_DEBUFFS.put(Hex.class,            2f);
        MAJOR_DEBUFFS.put(Paralysis.class,      1f);

        MAJOR_DEBUFFS.put(Corruption.class,          0f);
        MAJOR_DEBUFFS.put(Corruption.class,          0f);
        MAJOR_DEBUFFS.put(MagicalSleep.class,   0f);
        MAJOR_DEBUFFS.put(SoulMark.class,       0f);
        MAJOR_DEBUFFS.put(Corrosion.class,      0f);
        MAJOR_DEBUFFS.put(Frost.class,          0f);
        MAJOR_DEBUFFS.put(Doom.class,           0f);
    }



    private void debuffEnemy( Mob enemy, HashMap<Class<? extends Buff>, Float> category ){

        //do not consider buffs which are already assigned, or that the enemy is immune to.
        HashMap<Class<? extends Buff>, Float> debuffs = new HashMap<>(category);
        for (Buff existing : enemy.buffs()){
            if (debuffs.containsKey(existing.getClass())) {
                debuffs.put(existing.getClass(), 0f);
            }
        }
        for (Class<?extends Buff> toAssign : debuffs.keySet()){
            if (debuffs.get(toAssign) > 0 && enemy.isImmune(toAssign)){
                debuffs.put(toAssign, 0f);
            }
        }

        //all buffs with a > 0 chance are flavor buffs
        Class<?extends FlavourBuff> debuffCls = (Class<? extends FlavourBuff>) Random.chances(debuffs);

        if (debuffCls != null){
            Buff.append(enemy, debuffCls, 6 + buffedLvl()*3);
        } else {
            //if no debuff can be applied (all are present), then go up one tier
            if (category == MINOR_DEBUFFS)          debuffEnemy( enemy, MAJOR_DEBUFFS);
            else if (category == MAJOR_DEBUFFS)     corruptEnemy( enemy );
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

    protected static ArrayList<PointF> fxS = new ArrayList<>();
    protected static ArrayList<PointF> fxE = new ArrayList<>();

    public void fxs(Ballistica beam, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.SHADOW,
                curUser.sprite,
                beam.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
        buildBeams(beam);
        int size = fxE.size();
        for(int i=0;i<size;++i){
            //Point p = Dungeon.level.cellToPoint(fxE.get(i));
            //GLog.i("(%d, %d)", p.x, p.y);
            curUser.sprite.parent.add(new Beam.DeathRayS(BallisticaReal.raisedPointToScreen(fxS.get(i)),
                    BallisticaReal.raisedPointToScreen(fxE.get(i)) ));
        }
        callback.call();
    }
    protected static ArrayList<BallisticaReal> beams = new ArrayList<>();
    protected static ArrayList<Float> offset_1 = new ArrayList<>();
    protected void buildOffsetArray(int level){
        offset_1.clear();
        float offPerStep = 45f* (1.9f+level) / (1.9f+level*3f);
        int maxDSB;
        if(level>7){maxDSB = 0;}
        else if(level>3){maxDSB = 0;}
        else{maxDSB = 0;}
        //offset_1.add(0f);
        if(maxDSB>0) {
            for (int i = -maxDSB; i < maxDSB + 1; ++i) {
                offset_1.add(i * offPerStep / maxDSB);
            }
        }else{
            offset_1.add(0f);
        }

        // if(level<9) return base_3;
        //else return base_4;
    }

    protected PointF pointToF(Point p){
        return new PointF(p.x, p.y);
    }

    protected void addBeam(BallisticaReal beam){
        beams.add(beam);
        fxS.add(beam.sourceF);
        fxE.add(beam.collisionF);
    }

    protected void buildBeams(Ballistica beam){
        fxS.clear();
        fxE.clear();
        beams.clear();
        buildOffsetArray(this.level());
        //addBeam(new Ballistica(beam.sourcePos, beam.collisionPos, Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID));
        float angle = PointF.angle(new PointF(pointToF(Dungeon.level.cellToPoint(beam.sourcePos))),
                new PointF(pointToF(Dungeon.level.cellToPoint(beam.collisionPos))));
        angle /= PointF.G2R;
        if(angle<0f) angle += 360f;
        //GLog.i("%f,", angle);
        int scatter = offset_1.size();
        for(int i=0;i<scatter;++i){
            addBeam(new BallisticaReal(beam.sourcePos, angle + offset_1.get(i), 20, BallisticaReal.STOP_SOLID | BallisticaReal.IGNORE_SOFT_SOLID));
        }
        int maxRf = nReflections(this.level());
        for(int ref = 0; ref < maxRf; ++ref) {
            for (int i = 0; i < scatter; ++i) {
                BallisticaReal br = new BallisticaReal(fxE.get(i+ref*scatter), reflectAngle(fxS.get(i+ref*scatter), fxE.get(i+ref*scatter)), 20, BallisticaReal.STOP_SOLID | BallisticaReal.IGNORE_SOFT_SOLID);
                addBeam(br);
            }
        }

    }

    protected float horizontalReflectAngle(float angle){
        angle = angle%360f;
        return 360f-angle;
    }

    protected float verticalReflectAngle(float angle){
        angle = angle%360f;
        if(angle<180f) angle=180f-angle;
        else angle = 540f-angle;
        return angle;
    }

    protected float reflectAngle(PointF s, PointF e){
        //PointF realPoint = nextPF(s,e);
        float angle = PointF.angle(s,e)/PointF.G2R;
        if(angle<0f) angle+= 360f;
        float dx = e.x - s.x;
        float dy = e.y - s.y;
        boolean up = dy>0;
        boolean right = dx>0;
        boolean horizontalWall = false;
        boolean verticalWall = false;

        int xTile=(int)e.x;
        if(e.x-(int)e.x<1e-5 && right){
            xTile-=1;
        }
        int yTile=(int)e.y;
        if(e.y-(int)e.y<1e-5 && up){
            yTile-=1;
        }
        final int[] neigh = new int[]{-1, 1, -Dungeon.level.width(), Dungeon.level.width()};
        boolean[] isWall = new boolean[4];
        for(int i=0; i<4; ++i){
            isWall[i]=Dungeon.level.solid[xTile+yTile*Dungeon.level.width()+neigh[i]];
        }
        if(e.x-(int)e.x<1e-5){
            verticalWall = (right && isWall[1]) || (!right && isWall[0]);
        }
        if(e.y-(int)e.y<1e-5){
            horizontalWall = (up && isWall[3]) || (!up && isWall[2]);
        }

        if(horizontalWall != verticalWall){
            if(horizontalWall) return horizontalReflectAngle(angle);
            else return verticalReflectAngle(angle);
        }else if(horizontalWall && verticalWall){
            if(Math.abs(dx)<Math.abs(dy)){
                return horizontalReflectAngle(angle);
            }else{
                return verticalReflectAngle(angle);
            }
        }else{
            return angle;
        }
    }

    @Override
    public int STRReq(int C) {
        return 15+fireenergy/100;
    }

    public int proc(Char attacker, Char defender, int damage ) {
        //常规加+1浊焰能量
        ++fireenergy;
        int dmg;
        tier+= fireenergy/100;

        if(TIME_TO_DIED != 0){
            TIME_TO_DIED--;
        }

        if(level >= 10){
            fireenergy += 0;
            //武器最高级
        } else if(defender.properties().contains(Char.Property.BOSS) && defender.HP <= damage){
            //目标Boss血量小于实际伤害判定为死亡,+20浊焰能量
            fireenergy+=20;
        } else if(defender.properties().contains(Char.Property.MINIBOSS) && defender.HP <= damage){
            //目标迷你Boss血量小于实际伤害判定为死亡,+10浊焰能量
            fireenergy+=10;
        } else if (defender.HP <= damage){
            //目标血量小于实际伤害判定为死亡,+5浊焰能量
            fireenergy+=5;
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
