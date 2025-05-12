package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NullDiedTO;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nyz;
import com.shatteredpixel.shatteredpixeldungeon.items.KingGold;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfMastery;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Random;

public class AncityLevel extends Level {
    private static final int[] pre_map = {
            64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,
            64,4,0,0,0,0,0,0,4,0,0,0,0,0,0,4,64,
            64,0,4,0,0,0,0,0,4,0,0,0,0,0,4,0,64,
            64,0,0,4,0,4,4,4,4,4,4,4,0,4,0,0,64,
            64,0,0,0,4,0,0,0,4,0,0,0,4,0,0,0,64,
            64,0,0,4,0,4,0,0,4,0,0,4,0,4,0,0,64,
            64,0,0,4,0,0,4,0,4,0,4,0,0,4,0,0,64,
            64,0,0,4,0,0,0,4,4,4,0,0,0,4,0,0,64,
            64,4,4,4,4,4,4,4,17,4,4,4,4,4,4,4,64,
            64,0,0,4,0,0,0,4,4,4,0,0,0,4,0,0,64,
            64,0,0,4,0,0,4,0,83,0,4,0,0,4,0,0,64,
            64,0,0,4,0,4,0,0,4,0,0,4,0,4,0,0,64,
            64,0,0,0,4,0,0,0,4,0,0,0,4,0,0,0,64,
            64,0,0,4,0,4,4,4,4,4,4,4,0,4,0,0,64,
            64,0,4,0,0,0,0,0,4,0,0,0,0,0,4,0,64,
            64,4,0,0,0,0,0,0,4,0,0,0,0,0,0,4,64,
            64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,
    };

    {
        color1 = 5459774;
        color2 = 12179041;
    }

    @Override
    public void playLevelMusic() {
        Music.playModeBGM(Assets.Music.ANCITY, true);
    }

    public AncityLevel() {
        this.viewDistance = 34;
    }

    private int mapToTerrain(int code) {
        switch (code){
            case 1:
            default:
                return Terrain.EMPTY;
            case 64:
                return Terrain.WALL;
            case 4: case 11:
                return Terrain.EMPTY_SP;
            case 2:
                return Terrain.EMPTY_DECO;
            case 21:
                return Terrain.PEDESTAL;
            case 0:
                return Terrain.WATER;
            case 66:
                return Terrain.WALL_DECO;
            case 85:
                return Terrain.LOCKED_EXIT;
            case 17:
                return Dungeon.depth == 27 ? Terrain.EMPTY_SP :  Terrain.EXIT;
            case 83:
                return Dungeon.depth == 17 ? Terrain.ENTRANCE : 14;
            case 99:
                return Terrain.STATUE_SP;
            case 20:
                return Terrain.EMPTY_WELL;
        }
    }

    protected boolean build() {
        setSize(17, 17);


        int entranceCell =  (this.width * 8 + 8);
        int exitCell =  59;

        LevelTransition enter = new LevelTransition(this, entranceCell, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(enter);

        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(exit);


        for (int var1 = 0; var1 < this.map.length; var1++) {
            this.map[var1] = mapToTerrain(pre_map[var1]);
        }
        return true;
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) {
            return false;
        } else {
            return super.activateTransition(hero,transition);
        }
    }

    public Mob createMob() {
        return null;
    }

    protected void createMobs() {
        Nyz n = new Nyz();
        n.pos = (this.width * 8 + 3);
        mobs.add(n);

        NullDiedTO npc1 = new NullDiedTO();
        npc1.pos = (this.width * 8 + 13);
        mobs.add(npc1);
    }

    @Override
    protected void createItems() {
        drop( new PotionOfExperience().identify(), 143 );
        drop( new PotionOfExperience().identify(), 145 );
        drop( new PotionOfExperience().identify(), 161 );
        drop( new PotionOfExperience().identify(), 127 );
        drop( new KingGold(Random.NormalIntRange(1,5)), 144 );
        drop( new PotionOfMastery(), 59 );
    }

    public int randomRespawnCell() {
        return this.entrance - width();
    }

    public String tilesTex() {
        return Assets.Environment.TILES_CAVES;
    }

    public String waterTex() {
        return Assets.Environment.WATER_PRISON;
    }

}
