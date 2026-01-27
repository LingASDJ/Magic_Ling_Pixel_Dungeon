package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.ApprenticeWitch;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Butcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Crumb;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Pumking_Ghost;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.BallisticaFloat;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.GME;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.MissileSpriteCustom;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MindCoreSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerMindSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TowerMind extends Boss {

    public int stealCounter = 0;
    public boolean conditionMet = false;
    private int summonedMobs = 1;

    {
        initProperty();
        initBaseStatus(10, 45, 33, 45, 400, 0, 0);
        initStatus(120);
        noDropIceCoin = true;
        spriteClass = TowerMindSprite.class;

        viewDistance = 100;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);

        immunities.add(FrostBurning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(Burning.class);
        immunities.add(Ooze.class);
        immunities.add(Terror.class);
        immunities.add(Hex.class);
        immunities.add(Vertigo.class);
        immunities.add(Blindness.class);
        immunities.add(TowerMachine.DeadAlive.class);
        immunities.add(Blob.class);
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        if(src == TowerMachine.class){
            return;
        }
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 2;
            lock.addTime(dmg*multiple);
        }
        BossHealthBar.assignBoss(this);
        super.damage(dmg, src, type);
    }


    private static final String STEAL_COUNTER = "steal_counter";
    private static final String CONDITION_MET = "condition_met";
    private static final String SUMMONED_MOBS = "summoned_mobs";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put(STEAL_COUNTER, stealCounter);
        bundle.put(CONDITION_MET, conditionMet);
        bundle.put(SUMMONED_MOBS, summonedMobs);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        stealCounter = bundle.getInt(STEAL_COUNTER);
        conditionMet = bundle.getBoolean(CONDITION_MET);
        summonedMobs = bundle.getInt(SUMMONED_MOBS);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 45 );
    }

    // 生成散射角度的方法
    private float[] generateScatterAngles(int count) {
        float[] angles = new float[count];
        float step = (float) 22.5 * 2 / (count-1);
        for(int i=0; i<count; i++){
            angles[i] = (float) 270 - (float) 22.5 + step*i + Random.Float(-2.5f,2.5f);
        }
        return angles;
    }

    private Mob getSummonTimeMobs() {
        List<Class<? extends Mob>> mobTypes = new ArrayList<>();
        mobTypes.add(Butcher.class);
        mobTypes.add(ApprenticeWitch.class);
        mobTypes.add(Pumking_Ghost.class);
        mobTypes.add(Crumb.class);

        Random.shuffle(mobTypes);
        Class<? extends Mob> selectedMobType = mobTypes.get(0);
        Mob mob = null;
        try {
            mob = selectedMobType.getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {}
        return mob;
    }

    public void TryGetSummonedMobs() {
        ArrayList<Integer> positions = new ArrayList<>();
        if (buff(MindSummonColdDown.class) == null && summonedMobs <= 4) {
            Mob testActor = getSummonTimeMobs();
            testActor.state = testActor.HUNTING;
            GameScene.add(testActor);
            /****************/
            positions.add(343);
            positions.add(245);
            positions.add(347);
            positions.add(370);
            /****************/
            Random.shuffle(positions);
            ScrollOfTeleportation.appear(testActor,positions.get(0));
            Mob testActor2 = getSummonTimeMobs();
            testActor2.state = testActor2.HUNTING;
            GameScene.add(testActor2);
            /****************/
            positions.add(343);
            positions.add(245);
            positions.add(347);
            positions.add(370);
            /****************/
            Random.shuffle(positions);
            ScrollOfTeleportation.appear(testActor2,positions.get(0));
            Buff.affect(this, Barrier.class).setShield(100);
            Buff.affect(this,MindSummonColdDown.class, 50f);
            summonedMobs+=2;
        }
    }

    public static class MindSummonColdDown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION	= 10f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }

    public boolean act() {
        alerted = false;
        state = PASSIVE;
        TowerParalysis towerParalysis = buff(TowerParalysis.class);
        if (towerParalysis == null) {
            TryGetSummonedMobs();

            if(buff(MindGetItemsColdDown.class) == null) {

                //检查是否有损坏的核心
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob instanceof MindCore && mob.HP == 0) {
                        if (HP > HT * 0.1f) {
                            HP -= (int) (HT * 0.1f);
                            mob.HP = mob.HT;
                            ((MindCore) mob).dropped = false;
                        }
                    }
                }

                // 先检测存活核心数量
                ArrayList<MindCore> aliveCores = new ArrayList<>();
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob instanceof MindCore && mob.isAlive()
                            && ((MindCore) mob).items.isEmpty()) {
                        aliveCores.add((MindCore) mob);
                    }
                }

                Buff.affect(this, MindGetItemsColdDown.class, 50f);

                // 动态检测条件
                boolean enraged = HP < HT * 0.5f;
                // 掠夺物品（包含诅咒物品处理）
                ArrayList<Item> stolenItems = new ArrayList<>();
                ArrayList<Item> allItems = hero.belongings.getAllItems(Item.class);

                ITEM_LOOP:
                for (Item item : allItems) {
                    if (stealCounter >= 3) break; // 最多缴械3件

                    if (enraged) {
                        conditionMet = item.level() >= 6;
                    } else {
                        conditionMet = item.level() >= 3 && !item.cursed;
                    }

                    if (conditionMet) {
                        // 执行缴械操作
                        stolenItems.add(item);

                        // 创建基础弹道轨迹
                        new BallisticaFloat(
                                hero.pos,
                                GME.angle(pos, hero.pos),
                                6,
                                Ballistica.PROJECTILE
                        );

                        // 生成散射角度队列
                        float[] angles = generateScatterAngles(
                                stolenItems.size()
                        );

                        // 创建粒子发射器
                        ParticleEmitter pe = new ParticleEmitter();

                        // 批量投射动画
                        for(int i=0; i<stolenItems.size(); i++){
                            Item currentItem = stolenItems.get(i);
                            BallisticaFloat traj = new BallisticaFloat(
                                    hero.pos,
                                    angles[i],
                                    6,
                                    Ballistica.PROJECTILE
                            );

                            // 为每个物品创建独立动画
                            MissileSpriteCustom msc = (MissileSpriteCustom) hero.sprite.parent.recycle(MissileSpriteCustom.class);
                            msc.reset(
                                    hero.sprite,
                                    traj.collisionPosI,
                                    currentItem,
                                    0.6f,
                                    1.25f + i*0.1f, // 添加序列延迟
                                    new Callback() {
                                        @Override
                                        public void call() {

                                        }
                                    }
                            );
                        }


                        if (item.isEquipped(hero)) {
                            ((EquipableItem) item).doUnequip(hero, false);
                        }
                        item.detachAll(hero.belongings.backpack);
                        yell(Messages.get(this, "yell_steal_item", item.name()));
                        stealCounter++;
                        if (aliveCores.isEmpty()) break ITEM_LOOP;
                    }
                }

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob instanceof MindCore && mob.isAlive()
                            && ((MindCore) mob).items.size() < 3) { // 允许未满的核心
                        aliveCores.add((MindCore) mob);
                    }
                }

                // 在物品分配循环中添加类型检查和核心更新
                for (Item item : stolenItems) {
                    // 寻找匹配类型的核心
                    MindCore target = null;
                    for (MindCore core : aliveCores) {
                        if (!core.items.isEmpty()
                                && core.items.get(0).getClass() == item.getClass()) {
                            target = core;
                            break;
                        }
                    }

                    // 没有匹配核心则找空核心
                    if (target == null) {
                        for (MindCore core : aliveCores) {
                            if (core.items.isEmpty()) {
                                target = core;
                                break;
                            }
                        }
                    }

                    // 分配物品
                    if (target != null) {
                        target.items.add(item);
                        // 移除已满的核心
                        if (target.items.size() >= 3) {
                            aliveCores.remove(target);
                        }
                    } else {
                        // 回退到默认处理（可选记录日志）
                        Dungeon.level.drop(item, pos);
                    }
                }
            }
        }
        return super.act();
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof TowerMind.MindCore) {
                mob.die(true);
            }
        }

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof TowerMind || mob instanceof TowerTime||mob instanceof TowerGods||mob instanceof TowerMachine) {
                Buff.affect(mob, TowerParalysis.class).set((21), 1);
            }
            if(mob instanceof Morphs){
                ((Morphs) mob).phase+=0.25f;
            }
            if(mob instanceof Butcher || mob instanceof ApprenticeWitch || mob instanceof Pumking_Ghost || mob instanceof Crumb ){
                mob.die(true);
            }
        }


    }

    public static class MindCore extends Mob {

        public ArrayList<Item> items = new ArrayList<>();
        public boolean dropped = false;

        {
            spriteClass = MindCoreSprite.class;
            immunities.add(Blob.class);

            properties.add(Property.IMMOVABLE);
            HP = HT = 200;
            properties.add(Property.MINIBOSS);
            state = PASSIVE;

            immunities.add(Healing.class);
            immunities.add(Barrier.class);
        }

        @Override
        public boolean act() {
            alerted = false;
            state = PASSIVE;

            if (HP == 0 && !dropped) {

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if(mob instanceof TowerMind){
                        ((TowerMind) mob).stealCounter--;
                        if (((TowerMind) mob).stealCounter < 0) {
                            ((TowerMind) mob).stealCounter = 0;
                        }
                    }
                }


                ArrayList<Item> toDrop = new ArrayList<>(items);
                for (int i = 0; i < toDrop.size(); i++) {

                    Item item = toDrop.get(i);


                    float delay = i * 0.2f;

                    MissileSpriteCustom msc = (MissileSpriteCustom) hero.sprite.parent.recycle(MissileSpriteCustom.class);
                    msc.reset(
                            sprite,
                            387,
                            item,
                            0.6f,
                            1.25f + delay,
                            new Callback() {
                                final Item finalItem = item;

                                @Override
                                public void call() {
                                    Dungeon.level.drop(finalItem, hero.pos);
                                }
                            }
                    );
                }

                if (!toDrop.isEmpty()) {
                    items.clear();
                }

                for(Buff buff : buffs()){
                    buff.detach();
                }
                dropped = true;
            }

            return super.act();
        }

        @Override
        public boolean isAlive() {
            boolean isAlive = false;
            ArrayList<Mob> mobsCopy = new ArrayList<>(Dungeon.level.mobs);
            for (Mob mob : mobsCopy){
                if (mob instanceof TowerMind) {
                    isAlive = true;
                    break;
                }
            }
            return isAlive;
        }


        private static final String ITEMS	= "items";
        private static final String ITEMS_DROPPED	= "items_dropped";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            if (items != null) bundle.put( ITEMS, items );
            bundle.put(ITEMS_DROPPED, dropped);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void restoreFromBundle( Bundle bundle ) {
            if (bundle.contains( ITEMS )) {
                items = new ArrayList<>((Collection<Item>) ((Collection<?>) bundle.getCollection(ITEMS)));
            }
            dropped = bundle.getBoolean(ITEMS_DROPPED);
            super.restoreFromBundle(bundle);
        }

        @Override
        public int drRoll() {
            return 15;
        }

        @Override
        public String description() {
            String desc = super.description();
            if (items != null && !items.isEmpty()) {
                StringBuilder itemNames = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    if (i > 0) itemNames.append(", ");
                    itemNames.append(items.get(i).name());
                }
                desc += "\n\n" + Messages.get(this, "desc_items", itemNames.toString());
            }
            return desc;
        }

        @Override
        public boolean isInvulnerable(Class effect) {
            return HP == 0;
        }

        @Override
        public void die(Object cause) {
            super.die(cause);
            if(!dropped){
                if(items.isEmpty()){
                    items.add(new Gold(1));
                }

                ArrayList<Item> toDrop = new ArrayList<>(items);

                for (int i = 0; i < toDrop.size(); i++) {
                    Item item = toDrop.get(i);
                    float delay = i * 0.2f;

                    MissileSpriteCustom msc = (MissileSpriteCustom) hero.sprite.parent.recycle(MissileSpriteCustom.class);
                    msc.reset(
                            sprite,
                            387,
                            item,
                            0.6f,
                            1.25f + delay,
                            new Callback() {
                                final Item finalItem = item;

                                @Override
                                public void call() {
                                    Dungeon.level.drop(finalItem, pos);
                                }
                            }
                    );
                }

                if (!toDrop.isEmpty()) {
                    items.clear();
                }

                for(Buff buff : buffs()){
                    buff.detach();
                }
                dropped = true;
            }

        }
    }

    public static class MindGetItemsColdDown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION	= 10f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        return false;
    }


}