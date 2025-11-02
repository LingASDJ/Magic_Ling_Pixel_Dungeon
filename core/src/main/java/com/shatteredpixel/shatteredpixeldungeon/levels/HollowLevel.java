package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.HollowMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Vampire;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.SliceGirl;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.hollow.Gelatin;
import com.shatteredpixel.shatteredpixeldungeon.items.food.hollow.Sugar_Block;
import com.shatteredpixel.shatteredpixeldungeon.items.food.hollow.WhiteSugar_B;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHolyWater;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.HollowPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class HollowLevel extends RegularLevel {

    {
        color1 = 0x6a723d;
        color2 = 0x88924c;
        extraGlass = false;
    }

    @Override
    public void playLevelMusic(){
        if(depth>=29){
            Music.playModeBGM(Assets.Music.HOLLOW_CITY_HARD, true);
        } else {
            Music.playModeBGM(Assets.Music.HOLLOW_CITY, true);
        }
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 6;
        //8 to 9, average 8.33
        return 8+ Random.chances(new float[]{2, 1});
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        if (forceMax) return 4;
        //2 to 3, average 2.5
        return 2 + Random.chances(new float[]{1, 1});
    }

    @Override
    protected void createItems() {
        PotionOfHolyWater potionOfHolyWater = new PotionOfHolyWater();
        potionOfHolyWater.quantity(Random.NormalIntRange(1,3));
        addItemToSpawn(potionOfHolyWater);

        switch (depth){
            case 28: case 30:
                ScrollOfUpgrade scrollOfUpgrade = new ScrollOfUpgrade();
                scrollOfUpgrade.quantity(1);
                addItemToSpawn(scrollOfUpgrade);
            break;
        }

        addItemToSpawn(convert());
        addItemToSpawn(convert());
        addItemToSpawn(convert());

        addItemToSpawn(convert());
        addItemToSpawn(convert());
        addItemToSpawn(convert());

        switch (Random.Int(3)){
            case 0:
                addItemToSpawn(new Gelatin());
            break;
            case 1:
                addItemToSpawn(new Sugar_Block());
            break;
            case 2:
                addItemToSpawn(new WhiteSugar_B());
            break;
        }

        if(Random.Float()<0.25f){
            addItemToSpawn(new WhiteSugar_B());
        }

        if(Random.Float()<0.25f){
            addItemToSpawn(new Gelatin());
        }

        super.createItems();
    }

    public static Item convert(){
        Item w;
        switch (Random.Int(0,8)){
            case 1:
                w = Generator.randomUsingDefaults( Generator.Category.WAND );
                break;
            case 2:
                w = new PotionOfHealing().quantity(1);
                break;
            case 3:
                w = Generator.randomUsingDefaults( Generator.Category.POTION );
                break;
            case 4:
                PotionOfHolyWater potionOfHolyWater = new PotionOfHolyWater();
                potionOfHolyWater.quantity(1);
                w = potionOfHolyWater;
                break;
            case 5:
                w = Generator.randomUsingDefaults( Generator.Category.SCROLL );
                break;
            case 6:
                w = Generator.randomUsingDefaults( Generator.Category.WEP_T5 );
                break;
            case 7:
                switch (Random.Int(4)){
                    case 1: w = new ScrollOfChallenge(); break;
                    case 2: w = new ScrollOfMetamorphosis(); break;
                    case 3: w = new ScrollOfAntiMagic();    break;
                    default:
                        w = new ScrollOfSirensSong(); break;
                }
                break;
            default:
                w = Generator.randomUsingDefaults( Generator.Category.WEP_T3 );
                break;
        }
        return w;
    }

    @Override
    protected Painter painter() {
        return new HollowPainter()
                .setWater(feeling == Level.Feeling.WATER ? 0.55f : 0.50f, 2)
                .setGrass(feeling == Level.Feeling.GRASS ? 0.70f : 0.40f, 0)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_HOLLOW;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HOLLOW;
    }

    protected void createMobs() {
        if(depth>28){
            Vampire n = new Vampire();
            n.pos = randomRespawnCell(n);
            mobs.add(n);

            if(Random.Float()<0.25f){
                Vampire n1 = new Vampire();
                n1.pos = randomRespawnCell(n1);
                mobs.add(n1);
            }
        }

        //古堡第二幕
        if(Statistics.AbyssCityRules == 2 && depth == 27  && Statistics.Hollow_Holiday){
            SliceGirl sliceGirl = new SliceGirl();
            sliceGirl.pos = exit();
            mobs.add(sliceGirl);
        } else if((Badges.isUnlocked(Badges.Badge.KILL_DOG)) && depth == 27 && Statistics.Hollow_Holiday && Statistics.AbyssCityRules != 1) {
            SliceGirl sliceGirl = new SliceGirl();
            sliceGirl.pos = exit();
            mobs.add(sliceGirl);
        }

        super.createMobs();
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{
                ChillingTrap.class, ShockingTrap.class, ToxicTrap.class, BurningTrap.class, PoisonDartTrap.class,
                AlarmTrap.class, OozeTrap.class, GrippingTrap.class,
                ConfusionTrap.class, FlockTrap.class, SummoningTrap.class, TeleportationTrap.class, GatewayTrap.class, GeyserTrap.class };
    }

    @Override
    protected float[] trapChances() {
        return new float[]{
                4, 4, 4, 4, 4,
                2, 2, 2,
                1, 1, 1, 1, 1, 1 };
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(PrisonLevel.class, "water_name");
            case Terrain.CUSTOM_DECO:
                return Messages.get(HollowMimic.class, "cspx_name");
            case Terrain.WALL_DECO:
                return Messages.get(HollowMimic.class, "minames");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.WALL_DECO:
                return Messages.get(HollowMimic.class, "midescs");
            case Terrain.CUSTOM_DECO:
                return Messages.get(HollowMimic.class, "cspx_desc");
            case Terrain.EMPTY_DECO:
                return Messages.get(PrisonLevel.class, "empty_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(PrisonLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addPrisonVisuals(this, visuals);
        return visuals;
    }

    public static void addPrisonVisuals(Level level, Group group){
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                group.add( new PumpLanter( i ) );
            }
        }
    }

    public static class PumpLanter extends Emitter {

        private int pos;

        public PumpLanter( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );

            add( new Halo( 12, 0xFFa500, 0.3f ).point( p.x, p.y + 1 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }
}
