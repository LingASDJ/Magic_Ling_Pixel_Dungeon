package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.bossrush;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.RivalSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor.Glyph;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTenacity;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorrosion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFirebolt;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand.WandOfVenom;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon.Enchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.nosync.DeepShadowLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Rival extends Mob implements Callback {

    private static final float TIME_TO_ZAP	= 1f;

    @Override
    public String name() {

        if(Dungeon.hero != null){
            return Messages.get(this,"name",hero.name());
        } else {
            return Messages.get(this,"namex");
        }
    }


    {
        spriteClass = RivalSprite.class;
        properties.add(Property.BOSS);
        HUNTING = new Hunting();
        WANDERING = new Wandering();

        if(Statistics.bossRushMode){
            immunities.add(Corrosion.class);
            immunities.add(Chill.class);
            immunities.add(FrostBurning.class);
        }
    }

    public MeleeWeapon weapon;
    public Armor armor;
    public KindofMisc misc1;
    public KindofMisc misc2;
    public Wand wand;
    public MissileWeapon missile;

    private int blinkCooldown = 0;

    private boolean blink( int target ) {

        Ballistica route = new Ballistica( pos, target, Ballistica.PROJECTILE);
        int cell = route.collisionPos;

        BlobImmunity mb = buff(BlobImmunity.class);{
            if(mb == null && Random.Int(10)<4){
                GLog.w( Messages.get(this, "protected",name()) );
                Buff.prolong( this, BlobImmunity.class, BlobImmunity.DURATION*2 );
                SpellSprite.show(this, SpellSprite.PURITY);
            }
        }

        //can't occupy the same cell as another char, so move back one.
        if (Actor.findChar( cell ) != null && cell != this.pos)
            cell = route.path.get(route.dist-1);

        if (level.avoid[ cell ] || (properties().contains(Property.LARGE) && !level.openSpace[cell])){
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int n : PathFinder.NEIGHBOURS8) {
                cell = route.collisionPos + n;
                if (level.passable[cell]
                        && Actor.findChar( cell ) == null
                        && (!properties().contains(Property.LARGE) || level.openSpace[cell])) {
                    candidates.add( cell );
                }
            }
            if (candidates.size() > 0)
                cell = Random.element(candidates);
            else {
                blinkCooldown = Random.IntRange(4, 6);
                return false;
            }
        }

        ScrollOfTeleportation.appear( this, cell );

        blinkCooldown = Random.IntRange(4, 6);
        return true;
    }

    public Rival() {
        super();

        int lvl = Dungeon.hero == null ? 30 : hero.lvl;

        if(hero != null){
            //melee
            do {
                weapon = (MeleeWeapon)Generator.random(Generator.Category.WEAPON);
            } while (weapon.cursed);
            weapon.enchant(Enchantment.random());
            weapon.identify();

            flying = true;

            //armor
            do {
                armor = (Armor)Generator.random(Generator.Category.ARMOR);
            } while (armor.cursed);
            armor.inscribe(Glyph.random());
            armor.identify();

            //misc1
            do {
                misc1 = (KindofMisc)Generator.random(Generator.Category.RING);
            } while (misc1.cursed);
            misc1.identify();

            //misc2
            do {
                misc2 = (KindofMisc)Generator.random(Generator.Category.RING);
            } while (misc2.cursed);
            misc2.identify();

            //wand
            do {
                wand = RandomWand();
            } while (wand.cursed);
            wand.updateLevel();
            wand.curCharges = 20;
            wand.identify();

            //missile
            do {
                missile = (MissileWeapon)Generator.random(Generator.Category.MISSILE);
            } while (missile.cursed);

            defenseSkill = (int)(armor.evasionFactor( this, 7 + lvl ));
        } else {
            defenseSkill = 7 + lvl;
        }


        HP = HT = 50 + (Statistics.bossRushMode ? lvl * 5 : lvl * 2);


        EXP = lvl * 17;

    }

    private static final String WEAPON	= "weapon";
    private static final String ARMOR	= "armor";
    private static final String MISC1	= "misc1";
    private static final String MISC2	= "misc2";
    private static final String WAND	= "wand";
    private static final String MISSILE	= "missile";
    private static final String BLINK_CD = "blink_cd";
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( WEAPON, weapon );
        bundle.put( ARMOR, armor );
        bundle.put( MISC1, misc1 );
        bundle.put( MISC2, misc2 );
        bundle.put( WAND, wand );
        bundle.put( MISSILE, missile );
        bundle.put(BLINK_CD, blinkCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        weapon		= (MeleeWeapon)		bundle.get( WEAPON );
        armor		= (Armor)			bundle.get( ARMOR );
        misc1		= (KindofMisc)		bundle.get( MISC1 );
        misc2		= (KindofMisc)		bundle.get( MISC2 );
        wand		= (Wand)			bundle.get( WAND );
        blinkCooldown = bundle.getInt(BLINK_CD);
        missile		= (MissileWeapon)	bundle.get( MISSILE );
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);
    }

    @Override
    public int damageRoll() {
        int dmg = 0;
        dmg += weapon.damageRoll( this );
        if (dmg < 0) dmg = 0;
        return dmg;
    }

    @Override
    public int drRoll() {
        int dr = 0;
        dr += Random.NormalIntRange( armor.DRMin(), armor.DRMax() );
        dr += Random.NormalIntRange( 0, weapon.defenseFactor( this ) );
        if (dr < 0) dr = 0;
        return dr;
    }

    @Override
    public int attackSkill( Char target ) {
        return (int)((16 + Dungeon.depth) * weapon.accuracyFactor( this,target ));
    }

    @Override
    public float attackDelay() {
        return super.attackDelay() * weapon.speedFactor( this );
    }

    @Override
    public float speed() {
        float speed = 0;
        if(misc1 instanceof RingOfHaste || misc2 instanceof RingOfHaste){
            speed += RingOfHaste.speedMultiplier(this);
        }
        if(armor != null){
            speed += armor.speedFactor( this, super.speed() );
        }
        return speed;
    }

    @Override
    protected boolean getCloser(int target) {
        // 判断远程攻击能力是否失效
        boolean hasRangedOption;
        if (HP < HT / 2) {
            // 半血以下时，投掷武器是否可用？
            hasRangedOption = missile != null && missile.quantity > 0;
        } else {
            // 半血以上时，法杖充能是否可用？
            hasRangedOption = wand != null && wand.curCharges > 0;
        }

        // 如果没有远程攻击手段，强制逼近玩家
        if (!hasRangedOption) {
            return super.getCloser(target);
        }

        // 如果有远程攻击手段，执行原有逻辑
        if (HP < HT / 2 && state == HUNTING) {
            // 半血以下且处于 HUNTING 状态时，远离玩家
            return enemySeen && getFurther(target);
        } else if (fieldOfView[target] && level.distance(pos, target) > 2 && blinkCooldown <= 0 && !rooted) {
            // 使用闪烁技能逼近玩家
            if (blink(target)) {
                spend(-1 / speed());
                return true;
            } else {
                return false;
            }
        } else {
            // 其他情况，正常逼近玩家
            blinkCooldown--;
            return super.getCloser(target);
        }
    }

    @Override
    protected boolean canAttack(Char enemy) {
        boolean canRanged = (HP < HT/2 && missile.quantity > 0)
                || (HP >= HT/2 && wand.curCharges > 0);
        boolean canMelee = level.adjacent(pos, enemy.pos) || weapon.canReach(this, enemy.pos);
        return canRanged || canMelee || super.canAttack(enemy);
    }

    @Override
    protected boolean doAttack(Char enemy) {
        // 检查是否在近战范围内
        boolean inMeleeRange = level.adjacent(pos, enemy.pos) || weapon.canReach(this, enemy.pos);

        // 如果不在近战范围内，尝试远程攻击
        if (!inMeleeRange) {
            boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];

            // 半血以下时，优先使用投掷武器
            if (HP < HT / 2 && missile != null && missile.quantity > 0) {
                if (visible) {
                    sprite.toss(enemy.pos); // 可见时播放投掷动画
                } else {
                    toss(); // 不可见时直接投掷
                }
                missile.quantity--; // 消耗投掷武器弹药
                return !visible; // 返回是否不可见
            }

            // 半血以上时，优先使用法杖
            if (wand != null && wand.curCharges > 0) {
                if (visible) {
                    sprite.zap(enemy.pos); // 可见时播放法杖动画
                } else {
                    zap(); // 不可见时直接施法
                }
                wand.curCharges--; // 消耗法杖充能
                return !visible; // 返回是否不可见
            }
        }

        // 如果没有远程攻击条件，或者已经在近战范围内，切换到近战攻击
        return super.doAttack(enemy);
    }

    private void zap() {
        spend( TIME_TO_ZAP );

        final Ballistica shot = new Ballistica( pos, enemy == null ? 0 :enemy.pos, wand.collisionProperties);

        wand.rivalOnZap( shot, this );
    }

    private void toss() {
        spend( TIME_TO_ZAP );

        if (hit( this, enemy, true )) {
            enemy.damage( this.missile.damageRoll(this), this.missile.getClass() );
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL, enemy.defenseVerb() );
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    public void onTossComplete() {
        toss();
        next();
    }

    @Override
    public void call() {
        next();
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        return weapon.proc( this, enemy, damage );
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {
        damage = super.defenseProc( enemy, damage );
        return armor.proc( enemy, this, damage );
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        super.damage( dmg, src, type);
        if (HP <= 0) {
            spend( TICK );
        }
    }

    private int SummonMob() {
        int order;
        DeepShadowLevel.State state = ((DeepShadowLevel) level).state();
        switch(state) {
            case PHASE_3:
                order = Random.NormalIntRange(16, 19);
                break;
            case PHASE_4:
                order = Random.NormalIntRange(21, 24);
                break;
            case PHASE_2:case PHASE_1:
                order =  Random.NormalIntRange(11, 14);
                break;
            default:
                order = Random.NormalIntRange(6, 9);;
                break;
        }
        return order;
    }

    public void summon(int Rpos) {
        if(Statistics.bossRushMode){
            sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
            int numberOfMobs = Random.Int(2, 6);
            for(int i = 0;i <= numberOfMobs ; i++){
                Mob syncmob = Reflection.newInstance(MobSpawner.getMobRotation(SummonMob()).get(0));
                syncmob.state = syncmob.WANDERING;
                syncmob.pos = Rpos;
                if (SummonMob() > 20) {
                    syncmob.HP = (int) (syncmob.HT * 0.75f);
                    syncmob.defenseSkill = syncmob.defenseSkill / 2;
                }
                syncmob.immunities.add(Corrosion.class);
                syncmob.immunities.add(Chill.class);
                GameScene.add(syncmob);
                syncmob.beckon(hero.pos);
            }

            yell( Messages.get(this, "arise") );
        }
    }

    @Override
    public void die( Object cause ) {

        DeepShadowLevel.State state = ((DeepShadowLevel) level).state();

        if (state != DeepShadowLevel.State.WON) {

            //cures doom and drops missile weapons
            for (Buff buff : buffs()) {
                if (buff instanceof Doom || buff instanceof PinCushion) {
                    buff.detach();
                }
            }


            switch(state) {
                case BRIDGE:
                    HP = 1;
                    PotionOfHealing.cure(this);
                    Buff.detach(this, Paralysis.class);

                    ((DeepShadowLevel) level).progress();

                    yell( Messages.get(this, "interrobang") );
                    return;
                case PHASE_1:
                case PHASE_2:
                case PHASE_3:
                case PHASE_4:
                    HP = HT;
                    for (Mob mob : level.mobs.toArray(new Mob[0])){
                        if(mob instanceof Rival){
                            Buff.affect(mob, Dread.class);
                        }
                    }

                    PotionOfHealing.cure(this);
                    Buff.detach(this, Paralysis.class);

                    if (level.heroFOV[pos] && hero.isAlive()) {
                        new Flare(8, 32).color(0xFFFF66, true).show(sprite, 2f);
                        CellEmitter.get(this.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
                        Sample.INSTANCE.play( Assets.Sounds.TELEPORT );
                        GLog.w( Messages.get(this, "revive") );
                    }

                    ((DeepShadowLevel) level).progress();

                    yell( Messages.get(this, "exclamation") );

                    switch (state) {
                        case PHASE_1:
                            wand = new WandOfFrost();
                            wand.curCharges = 20;
                            wand.level(3);
                            wand.updateLevel();
                            missile.quantity = 2;
                            break;
                        case PHASE_2:
                            wand = new WandOfBlastWave();
                            wand.curCharges = 40;
                            misc1 = new RingOfHaste();
                            wand.level(2);
                            wand.updateLevel();
                            missile.quantity = 2;
                            break;
                        case PHASE_3:
                            wand = new WandOfFirebolt();
                            misc1 = new RingOfHaste();
                            wand.level(1);
                            wand.updateLevel();
                            wand.curCharges = 80;
                            missile.quantity = 3;
                            break;
                        case PHASE_4:
                            wand = new WandOfVenom();
                            wand.curCharges = 10;
                            wand.level(4);
                            wand.updateLevel();
                            missile.quantity = Random.IntRange(2, 4);
                            break;
                    }
                    HP = HT;
                    missile = (MissileWeapon)Generator.random(Generator.Category.MISSILE);
                    return;
                case PHASE_5:
                    ((DeepShadowLevel) level).progress();
                    super.die( cause );
                    wand = new WandOfMagicMissile();
                    misc1 = new RingOfTenacity();
                    wand.curCharges = 4;
                    Statistics.doNotLookLing = true;
                    GameScene.bossSlain();

                    yell( Messages.get(this, "ellipsis") );
                    level.unseal();
                    return;
                case WON:
                default:
            }
        } else {
            super.die( cause );

            GameScene.bossSlain();

            yell( Messages.get(this, "ellipsis") );
        }
    }

    @Override
    public void notice() {
        super.notice();
        level.seal();

        //BGMPlayer.playBGM(Assets.BGM_YOU, true);

        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "question"));

        }
    }

    @Override
    public String description() {
        String desc = super.description();

        if(Dungeon.hero != null){
            desc += Messages.get(this, "weapon", weapon.toString() );
            desc += Messages.get(this, "armor", armor.toString() );
            desc += Messages.get(this, "ring", misc1.toString() );
            desc += Messages.get(this, "ring", misc2.toString() );
            desc += Messages.get(this, "wand", wand.toString() );
            desc += Messages.get(this, "missile", missile.toString() );
            desc += Messages.get(this, "ankhs");
        } else {
            desc += "";
        }



        return desc;
    }

    {
        resistances.add( Grim.class );
        resistances.add( GrimTrap.class );
        resistances.add( ScrollOfRetribution.class );
        resistances.add( ScrollOfPsionicBlast.class );
        immunities.add( Amok.class );
        immunities.add( Corruption.class );
        immunities.add( Terror.class );
    }

    private Wand RandomWand() {
        wand = new WandOfCorrosion();
        wand.curCharges = 20;
        return wand;
    }

    public class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            enemySeen = enemyInFOV;
            if (!enemyInFOV) {
                PotionOfHealing.cure(Rival.this);
            }
            if(Random.Int(10)==1){
                Buff.affect(Rival.this, Healing.class).setHeal(5, 0f, 6);
            }
            return super.act( enemyInFOV, justAlerted );
        }

    }

    protected class Wandering extends Mob.Wandering{
        @Override
        protected int randomDestination() {
            //of two potential wander positions, picks the one closest to the hero
            int pos1 = super.randomDestination();
            int pos2 = super.randomDestination();
            PathFinder.buildDistanceMap(Dungeon.hero.pos, level.passable);
            if (PathFinder.distance[pos2] < PathFinder.distance[pos1]){
                return pos2;
            } else {
                return pos1;
            }
        }
    }

}
