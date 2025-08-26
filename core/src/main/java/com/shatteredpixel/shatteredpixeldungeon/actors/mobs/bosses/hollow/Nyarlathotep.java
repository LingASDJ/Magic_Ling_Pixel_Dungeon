package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NyarlathotepSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class Nyarlathotep extends Boss {

    {
        initProperty();
        initBaseStatus(0, 0, 33, 0, 700, 0, 0);
        initStatus(20);
        spriteClass = NyarlathotepSprite.class;

        properties.add(Property.BOSS);
        properties.add(Property.ACIDIC);
        properties.add(Property.INORGANIC);

        noDropIceCoin = true;
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if(mob instanceof Morphs){
                ((Morphs) mob).phase+=0.30f;
            }
        }
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 25 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 33;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, 0);

        enemy.damage( damageRoll(), new DM100.LightningBolt() );

        if(Random.Int(100)<25){
            int pos = Dungeon.level.randomDestination(this);
            createRandomTrap(pos);
        }

        return damage;
    }

    private static HashMap<Integer, Class<? extends Trap>> trapLib = new HashMap<>(40);
    static {
        trapLib.put(0, AlarmTrap.class);
        trapLib.put(48, DisarmingTrap.class);
        trapLib.put(24, GuardianTrap.class);
        trapLib.put(32, PitfallTrap.class);
        trapLib.put(25, BlazingTrap.class);
        trapLib.put(1, BurningTrap.class);
        trapLib.put(33, ExplosiveTrap.class);
        trapLib.put(2, ShockingTrap.class);
        trapLib.put(26, StormTrap.class);
        trapLib.put(3, OozeTrap.class);
        trapLib.put(43, PoisonDartTrap.class);
        trapLib.put(19, ToxicTrap.class);
        trapLib.put(11, WeakeningTrap.class);
        trapLib.put(20, ConfusionTrap.class);
        trapLib.put(52, DistortionTrap.class);
        trapLib.put(12, SummoningTrap.class);
        trapLib.put(4, TeleportationTrap.class);
        trapLib.put(28, WarpingTrap.class);
        trapLib.put(13, CursingTrap.class);
        trapLib.put(45, DisintegrationTrap.class);
        trapLib.put(6, ChillingTrap.class);
        trapLib.put(14, FlockTrap.class);
        trapLib.put(30, FrostTrap.class);
        trapLib.put(23, CorrosionTrap.class);
        trapLib.put(31, FlashingTrap.class);
        trapLib.put(55, GrimTrap.class);
        trapLib.put(7, GrippingTrap.class);
        trapLib.put(39, RockfallTrap.class);
        trapLib.put(47, WornDartTrap.class);
        trapLib.put(36, GeyserTrap.class);
        trapLib.put(44, GatewayTrap.class);
    }

    @Override
    public void damage(int dmg, Object src) {
        if(src == Trap.class){
            return;
        }
        BossHealthBar.assignBoss(this);
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 2;
            lock.addTime(dmg*multiple);
        }
        super.damage(dmg, src);
    }

    private void createRandomTrap(int pos) {
        if (Dungeon.level.map[pos] == Terrain.EMPTY || Dungeon.level.map[pos] == Terrain.EMPTY_SP) {
            ArrayList<Class<? extends Trap>> trapClasses = new ArrayList<>(trapLib.values());
            Class<? extends Trap> trapClass = Random.element(trapClasses);
            try {
                Trap trap = trapClass.newInstance();
                trap.pos = pos;
                Dungeon.level.setTrap(trap.reveal(), pos);
                Level.set(pos, Terrain.TRAP);
            } catch (Exception ignored) {}
        }
    }
}
