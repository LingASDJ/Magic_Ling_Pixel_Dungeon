package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class DiamondKnight extends Boss  {
    private int abilitiesUsed = 0;
    private final int OneHP = 179;
    private final int TwoHP = 99;
    private final int ThreeHP = 49;
    private static Char throwingChar;

    {
        spriteClass = DimandKingSprite.class;

        initProperty();
        initBaseStatus(14, 23, 33, 22, 420, 5, 12);
        initStatus(80);

        maxLvl = 30;
        HUNTING = new Hunting();

        flying = true; //doesn't literally fly, but he is fleet-of-foot enough to avoid hazards
        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.LARGE);
    }

    private int ventCooldown = 0;
    private int phase = 1;

    private static final float TIME_TO_ZAP	= 5f;

    private static final String PHASE = "phase";
    private boolean teleporting = false;
    private int selfTeleCooldown = 0;
    private int enemyTeleCooldown = 0;
    private static final String TELEPORTING = "teleporting";
    private static final String ABILITIES_USED   = "abilities_used";
    private static final String SELF_COOLDOWN = "self_cooldown";
    private static final String ENEMY_COOLDOWN = "enemy_cooldown";

    private static final String LAST_ABILITY     = "last_ability";

        @Override
        public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, phase);
        bundle.put( ABILITIES_USED, abilitiesUsed );
        bundle.put(TELEPORTING, teleporting);
        bundle.put(SELF_COOLDOWN, selfTeleCooldown);
        bundle.put(ENEMY_COOLDOWN, enemyTeleCooldown);
        bundle.put( LAST_ABILITY, lastAbility );
        bundle.put( ABILITIES_USED, abilitiesUsed );

        }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt(PHASE);
        abilitiesUsed = bundle.getInt( ABILITIES_USED );

        lastAbility = bundle.getInt( LAST_ABILITY );
        abilitiesUsed = bundle.getInt( ABILITIES_USED );

        BossHealthBar.assignBoss(this);
        if (HP <= HT/2) BossHealthBar.bleed(true);
    }


    @Override
    protected boolean act() {
        switch (phase){
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }

        return super.act();
    }

    //*****************************************************************************************
    //***** Boss abilities. These are expressed in game logic as buffs, blobs, and items *****
    //*****************************************************************************************

    //so that mobs can also use this

    private int lastAbility = -1;

    //starts at 2, so one turn and then first ability

    private static final int BOMB_ABILITY    = 0;
    private static final int FIRE_ABILITY    = 1;
    private static final int SHOCKER_ABILITY = 2;

    public boolean useAbility(){
        boolean abilityUsed = false;
        int abilityToUse = -1;

        while (!abilityUsed){

            if (abilitiesUsed == 0){
                abilityToUse = BOMB_ABILITY;
            } else if (abilitiesUsed == 1){
                abilityToUse = SHOCKER_ABILITY;
            } else if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
                abilityToUse = Random.Int(2)*2; //0 or 2, can't roll fire ability with challenge
            } else {
                abilityToUse = Random.Int(3);
            }

            //If we roll the same ability as last time, 9/10 chance to reroll
            if (abilityToUse != lastAbility || Random.Int(10) == 0){
                switch (abilityToUse){
                    case BOMB_ABILITY : default:
                        abilityUsed = throwBomb(this, enemy);
                        //if Tengu cannot use his bomb ability first, use fire instead.
                        if (abilitiesUsed == 0 && !abilityUsed){
                            abilityToUse = FIRE_ABILITY;
                            abilityUsed = throwBomb(this, enemy);
                        }
                        break;
                    case FIRE_ABILITY:
                        abilityUsed =throwBomb(this, enemy);
                        break;
                    case SHOCKER_ABILITY:
                        abilityUsed = throwBomb(this, enemy);
                        //if Tengu cannot use his shocker ability second, use fire instead.
                        if (abilitiesUsed == 1 && !abilityUsed){
                            abilityToUse = FIRE_ABILITY;
                            abilityUsed =throwBomb(this, enemy);
                        }
                        break;
                }
                //always use the fire ability with the bosses challenge
                if (abilityUsed && abilityToUse != FIRE_ABILITY && Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
                    throwBomb(this, enemy);
                }
            }

        }

        lastAbility = abilityToUse;
        abilitiesUsed++;
        return lastAbility == FIRE_ABILITY;
    }

    private static final float ATTACSWORD_DELAY	= 2f;

    public static class DarkBolt{}

    //the actual affected cells
    private HashSet<Integer> affectedCells;
    //the cells to trace fire shots to, for visual effects.
    private HashSet<Integer> visualCells;
    private int direction = 0;

    protected void fx(Ballistica bolt, Char ch ) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.
        affectedCells = new HashSet<>();
        visualCells = new HashSet<>();

        int maxDist = 30;
        int dist = Math.min(bolt.dist, maxDist);

        for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
            if (bolt.sourcePos+PathFinder.CIRCLE8[i] == bolt.path.get(1)){
                direction = i;
                break;
            }
        }

        float strength = maxDist;
        for (int c : bolt.subPath(1, dist)) {
            strength--; //as we start at dist 1, not 0.
            affectedCells.add(c);
            if (strength > 1) {
                spreadFlames(c + PathFinder.CIRCLE8[direction], strength - 1);
            } else {
                visualCells.add(c);
            }
        }

        //going to call this one manually
        visualCells.remove(bolt.path.get(dist));

        for (int cell : visualCells){
            //this way we only get the cells at the tip, much better performance.
            ((MagicMissile)ch.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.SWORDLING,
                    ch.sprite,
                    cell,
                    null
            );
        }
        if(Dungeon.level.heroFOV[bolt.sourcePos] || Dungeon.level.heroFOV[bolt.collisionPos]){
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
        }
    }

    private void spreadFlames(int cell, float strength){
        if (strength >= 0 && (Dungeon.level.passable[cell] || Dungeon.level.flamable[cell])){
            affectedCells.add(cell);
            if (strength >= 1.5f) {
                visualCells.remove(cell);
                spreadFlames(cell + PathFinder.CIRCLE8[direction], strength - 1.5f);
            } else {
                visualCells.add(cell);
            }
        } else if (!Dungeon.level.passable[cell])
            visualCells.add(cell);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10,20 );
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.distance(pos, enemy.pos) <= 1) {

            return super.doAttack(enemy);

        } else {

            boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
            if (visible) {
                sprite.attack(enemy.pos);
                spend(ATTACSWORD_DELAY);
                shoot(this, enemy.pos);
            }

            return !visible;
        }
    }

    public void teleportEnemy(){
        spend(3f);

        int bestPos = enemy.pos;
        for (int i : PathFinder.NEIGHBOURS8){
            if (Dungeon.level.passable[pos + i]
                    && Actor.findChar(pos+i) == null
                    && Dungeon.level.trueDistance(pos+i, enemy.pos) > Dungeon.level.trueDistance(bestPos, enemy.pos)){
                bestPos = pos+i;
            }
        }

        if (enemy.buff(MagicImmune.class) != null){
            bestPos = enemy.pos;
        }

        if (bestPos != enemy.pos){
            //ScrollOfTeleportation.appear(enemy, bestPos);
            if (enemy instanceof Hero){
                ((Hero) enemy).interrupt();
                Dungeon.observe();
            }
        }
        enemyTeleCooldown = 20;
    }

    public void onZapComplete(){
        teleportEnemy();
        next();
    }

    public void damage(int dmg, Object src) {
        if (!Dungeon.level.mobs.contains(this)){
            return;
        }

        ColdChestBossLevel.State state = ((ColdChestBossLevel)Dungeon.level).pro();

        int hpBracket = HT / 8;

        int beforeHitHP = HP;
        super.damage(dmg, src);
        dmg = beforeHitHP - HP;

        if ((beforeHitHP/hpBracket - HP/hpBracket) >= 2){
            HP = hpBracket * ((beforeHitHP/hpBracket)-1) + 1;
        }

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = state == ColdChestBossLevel.State.MAZE_START ? 1 : 4;
            lock.addTime(dmg*multiple);
        }

        if (HP == 0 && state == ColdChestBossLevel.State.VSBOSS_START) {
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);
                    ((ColdChestBossLevel)Dungeon.level).progress();
                    return true;
                }
            });
            return;
        }

        if (state == ColdChestBossLevel.State.MAZE_START && HP <= OneHP) {
            HP = OneHP;
            yell(Messages.get(this, "interesting"));
            ((ColdChestBossLevel) Dungeon.level).progress();
            BossHealthBar.bleed(true);
        }
    }

    protected void onZap( Ballistica bolt ) {

        for( int cell : affectedCells){

            //ignore caster cell
            if (cell == bolt.sourcePos){
                continue;
            }

            //only ignite cells directly near caster if they are flammable
            if (!Dungeon.level.adjacent(bolt.sourcePos, cell)
                    || Dungeon.level.flamable[cell]){
                //GameScene.add( Blob.seed( cell, 1+2, Fire.class ) );
            }
        }
    }

    public void shoot(Char ch, int pos){
        final Ballistica shot = new Ballistica( ch.pos, pos, Ballistica.PROJECTILE);
        fx(shot, ch);
    }

    public static boolean throwBomb(final Char thrower, final Char target){

        int targetCell = -1;

        //Targets closest cell which is adjacent to target
        for (int i : PathFinder.NEIGHBOURS8){
            int cell = target.pos + i;
            if (targetCell == -1 ||
                    Dungeon.level.trueDistance(cell, thrower.pos) < Dungeon.level.trueDistance(targetCell, thrower.pos)){
                targetCell = cell;
            }
        }

        if (targetCell == -1){
            return false;
        }

        final int finalTargetCell = targetCell;
        throwingChar = thrower;
        final BombAbility.BombItem item = new BombAbility.BombItem();
        thrower.sprite.zap(finalTargetCell);
        ((MissileSprite) thrower.sprite.parent.recycle(MissileSprite.class)).
                reset(thrower.sprite,
                        finalTargetCell,
                        item,
                        new Callback() {
                            @Override
                            public void call() {
                                //item.onThrow(finalTargetCell);
                                thrower.next();
                            }
                        });
        return true;
    }

    public static class BombAbility extends Buff {

        public int bombPos = -1;
        private int timer = 3;

        private ArrayList<Emitter> smokeEmitters = new ArrayList<>();

        @Override
        public boolean act() {

            if (smokeEmitters.isEmpty()){
                fx(true);
            }

            PointF p = DungeonTilemap.raisedTileCenterToWorld(bombPos);
            if (timer == 3) {
                FloatingText.show(p.x, p.y, bombPos, "3...", CharSprite.NEUTRAL);
            } else if (timer == 2){
                FloatingText.show(p.x, p.y, bombPos, "2...", CharSprite.WARNING);
            } else if (timer == 1){
                FloatingText.show(p.x, p.y, bombPos, "1...", CharSprite.NEGATIVE);
            } else {
                PathFinder.buildDistanceMap( bombPos, BArray.not( Dungeon.level.solid, null ), 2 );
                Sample.INSTANCE.play(Assets.Sounds.BLAST);
                detach();
                return true;
            }

            timer--;
            spend(TICK);
            return true;
        }

        @Override
        public void fx(boolean on) {
            if (on && bombPos != -1){
                PathFinder.buildDistanceMap( bombPos, BArray.not( Dungeon.level.solid, null ), 2 );
                for (int i = 0; i < PathFinder.distance.length; i++) {
                    if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                        Emitter e = CellEmitter.get(i);
                        e.pour( SmokeParticle.FACTORY, 0.25f );
                        smokeEmitters.add(e);
                    }
                }
            } else if (!on) {
                for (Emitter e : smokeEmitters){
                    e.burst(BlastParticle.FACTORY, 2);
                }
            }
        }

        private static final String BOMB_POS = "bomb_pos";
        private static final String TIMER = "timer";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put( BOMB_POS, bombPos );
            bundle.put( TIMER, timer );
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            bombPos = bundle.getInt( BOMB_POS );
            timer = bundle.getInt( TIMER );
        }

        public static class BombItem extends Item {

            {
                dropsDownHeap = true;
                unique = true;

                image = ItemSpriteSheet.TENGU_BOMB;
            }

            @Override
            public boolean doPickUp( Hero hero ) {
                GLog.w( Messages.get(this, "cant_pickup") );
                return false;
            }

            @Override
            protected void onThrow(int cell) {
                super.onThrow(cell);
                if (throwingChar != null){
                    Buff.append(throwingChar, BombAbility.class).bombPos = cell;
                    throwingChar = null;
                } else {
                    Buff.append(curUser, BombAbility.class).bombPos = cell;
                }
            }

            @Override
            public Emitter emitter() {
                Emitter emitter = new Emitter();
                emitter.pos(7.5f, 3.5f);
                emitter.fillTarget = false;
                emitter.pour(SmokeParticle.SPEW, 0.05f);
                return emitter;
            }
        }
    }

    private class Hunting extends Mob.Hunting{

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {

            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {
                return useAbility();
            } else {

                if (!enemyInFOV) {
                    chooseEnemy();
                    if (enemy == null) {
                        //if nothing else can be targeted, target hero
                        enemy = Dungeon.hero;
                    }
                }
                target = enemy.pos;

                spend( 6f );
                return true;

            }
        }
    }

}
