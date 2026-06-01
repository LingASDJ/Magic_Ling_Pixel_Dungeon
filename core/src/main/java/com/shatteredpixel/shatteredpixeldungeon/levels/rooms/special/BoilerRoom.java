package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Alchemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.MissileSpriteCustom;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SoulCrack;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Callback;
import com.watabou.utils.Point;

import java.util.ArrayList;

public class BoilerRoom extends SpecialRoom {
    @Override
    public int minWidth() { return  9;}
    @Override
    public int minHeight() {
        return 9;
    }
    @Override
    public int maxWidth() {
         return  9;
    }
    @Override
    public int maxHeight() {
        return 9;
    }

    @Override
    public boolean canConnect(Point p) {
        if (!super.canConnect(p)){
            return false;
        }
        //only place doors in the center
        if (Math.abs(p.x - (right - (width()-1)/2f)) < 1f){
            return true;
        }
        return Math.abs(p.y - (bottom - (height() - 1) / 2f)) < 1f;
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }

    private static final int[] pre_map = {
            64,0,129,0,129,0,64,
            0,4,4,4,4,4,0,
            129,4,49,49,49,4,129,
            0,4,49,4,49,4,0,
            129,4,49,4,49,4,129,
            0,4,4,4,4,4,0,
            64,0,129,0,129,0,64,
    };

    private int codeToTerrain(int code){
        switch (code){
            case 64:
                return Terrain.ALCHEMY;
            case 129:
                return STATUE_SP;
            case 49:
                return Terrain.SIGN;
            default:
                return Terrain.EMPTY_DECO;
        }
    }

    @Override
    public void paint(Level level) {

        Painter.fill(level,this, 0, WALL);

        for (int i = left + 1; i <= right-1; i++) {
            for (int j = top + 1; j <= bottom-1; j++) {
                int dx = i - (left + 1);
                int dy = j - (top + 1);
                int index = dy * (minWidth()-2) + dx;

                if(index >= 0 && index < pre_map.length){
                    set(level, i, j, codeToTerrain(pre_map[index]));
                } else {
                    set(level, i, j, Terrain.EMPTY_DECO);
                }
            }
        }

        entrance().set(Door.Type.REGULAR);

        int centerX = left + width() / 2;
        int centerY = top + height() / 2;
        Point xpos = new Point(centerX+3, centerY+3);
        int RPos = left + right - xpos.x + xpos.y * level.width();

        //击败狗子送碎片
        if(Badges.isUnlocked(Badges.Badge.KILL_DOG) && !(Badges.isUnlocked(Badges.Badge.KILL_MORES))){
            Point Redpos = new Point(centerX+3, centerY);
            int RedXPos = left + right - Redpos.x + Redpos.y * level.width();
            level.drop(new SoulCrack.RedSoulCrack(), RedXPos);

            Point Bluepos = new Point(centerX-3, centerY);
            int BlueXPos = left + right - Bluepos.x + Bluepos.y * level.width();
            level.drop(new SoulCrack.BlueSoulCrack(), BlueXPos);

            Point Yellowpos = new Point(centerX, centerY+3);
            int YellowXPos = left + right - Yellowpos.x + Yellowpos.y * level.width();
            level.drop(new SoulCrack.YellowSoulCrack(), YellowXPos);

            Point Pinkpos = new Point(centerX, centerY-3);
            int PinkXPos = left + right - Pinkpos.x + Pinkpos.y * level.width();
            level.drop(new SoulCrack.PinkSoulCrack(), PinkXPos);

            Point Gpos = new Point(centerX, centerY-2);
            int GXPos = left + right - Gpos.x + Gpos.y * level.width();
            level.drop(new SoulCrack.GreenSoulCrack(), GXPos);
        }

        Blob.seed( RPos, 1, Alchemy.class, level );

        Point vpos = new Point(centerX+3, centerY-3);
        int BPos = left + right - vpos.x + vpos.y * level.width();
        Blob.seed( BPos, 1, Alchemy.class, level );

        Point spos = new Point(centerX-3, centerY+3);
        int TPos = left + right - spos.x + spos.y * level.width();
        Blob.seed( TPos, 1, Alchemy.class, level );

        Point skos = new Point(centerX-3, centerY-3);
        int MPos = left + right - skos.x + skos.y * level.width();
        Blob.seed( MPos, 1, Alchemy.class, level );

        BoilerTilemap vis = new BoilerTilemap();
        Point c = center();
        vis.pos(c.x-1, c.y-1);
        level.customTiles.add(vis);

        Point jpos = new Point(centerX, centerY+1);
        int MiddlePos = left + right - jpos.x + jpos.y * level.width();
        CandleActivePoint ncx = new CandleActivePoint();
        ncx.pos = MiddlePos;
        level.mobs.add(ncx);
    }

    private void set(Level level, int x, int y, int value) {
        level.map[x + y * level.width()] = value;
    }

    public static class BoilerTilemap extends CustomTilemap {

        {
            texture = Assets.Environment.BoilerQuest;
            tileW = tileH = 3;

        }

        final int TEX_WIDTH = 48;

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(mapSimpleImage(0, 0, TEX_WIDTH), 3);
            return v;
        }

        @Override
        public String name(int tileX, int tileY) {
            return Messages.get(this, "name");
        }

        @Override
        public String desc(int tileX, int tileY) {
            return Messages.get(this, "desc");
        }
    }

    public static class CandleActivePoint extends NPC {

        {
            spriteClass = CandleActivePointSprite.class;
            properties.add(Property.IMMOVABLE);
        }

        @Override
        public boolean interact(Char c) {
            boolean one = false;
            boolean two = false;
            boolean three = false;
            boolean four = false;
            boolean five = false;
            if(!Statistics.Hollow_Holiday){
                SoulCrack.RedSoulCrack redSoulCrack = hero.belongings.getItem(SoulCrack.RedSoulCrack.class);
                if(redSoulCrack != null) one = true;
                SoulCrack.BlueSoulCrack blueSoulCrack = hero.belongings.getItem(SoulCrack.BlueSoulCrack.class);
                if(blueSoulCrack != null) two = true;
                SoulCrack.GreenSoulCrack greenSoulCrack = hero.belongings.getItem(SoulCrack.GreenSoulCrack.class);
                if(greenSoulCrack != null) three = true;
                SoulCrack.YellowSoulCrack yellowSoulCrack = hero.belongings.getItem(SoulCrack.YellowSoulCrack.class);
                if(yellowSoulCrack != null) four = true;
                SoulCrack.PinkSoulCrack purpleSoulCrack = hero.belongings.getItem(SoulCrack.PinkSoulCrack.class);
                if(purpleSoulCrack != null) five = true;

                if (Badges.isUnlocked(Badges.Badge.KILL_MORES) && Statistics.AbyssCityRules == 0){
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(new Image(new ItemSprite(ItemSpriteSheet.CASTLE_AIRPORT)),
                                    Messages.titleCase(Messages.get(BoilerRoom.class, "name")),
                                    Messages.get(BoilerRoom.class, "quest_start_prompt",hero.name()),
                                    Messages.get(BoilerRoom.class, "enter_1"),
                                    Messages.get(BoilerRoom.class, "enter_2"),
                                    Messages.get(BoilerRoom.class, "enter_no")) {
                                @Override
                                protected void onSelect(int index) {
                                    if(index == 1){
                                        Statistics.AbyssCityRules = 2;
                                        Statistics.Hollow_Holiday = true;
                                        GameScene.flash(Window.CBLACK, true);
                                        ((CandleActivePointSprite)sprite).activateidle();
                                        yell(Messages.get(BoilerRoom.class, "yell"));
                                    } else if (index == 0) {
                                        Statistics.AbyssCityRules = 1;
                                        Statistics.Hollow_Holiday = true;
                                        GameScene.flash(Window.CBLACK, true);
                                        ((CandleActivePointSprite)sprite).activateidle();
                                        yell(Messages.get(BoilerRoom.class, "yell"));
                                    }
                                }
                            });
                        }

                    });
                } else if(one && two && three && four && five && !(Badges.isUnlocked(Badges.Badge.KILL_MORES))) {

                    MissileSpriteCustom msc = (MissileSpriteCustom) hero.sprite.parent.recycle(MissileSpriteCustom.class);
                    msc.reset(
                            hero.sprite,
                            pos,
                            redSoulCrack,
                            0.18f,
                            0.5f,
                            new Callback() {
                                @Override
                                public void call() {
                                    redSoulCrack.detach(hero.belongings.backpack);
                                    MissileSpriteCustom msc = (MissileSpriteCustom) hero.sprite.parent.recycle(MissileSpriteCustom.class);
                                    msc.reset(
                                            hero.sprite,
                                            pos,
                                            blueSoulCrack,
                                            0.18f,
                                            0.5f,
                                            new Callback() {
                                                @Override
                                                public void call() {
                                                    blueSoulCrack.detach(hero.belongings.backpack);
                                                    MissileSpriteCustom msc = (MissileSpriteCustom) hero.sprite.parent.recycle(MissileSpriteCustom.class);
                                                    msc.reset(
                                                            hero.sprite,
                                                            pos,
                                                            greenSoulCrack,
                                                            0.18f,
                                                            0.5f,
                                                            new Callback() {
                                                                @Override
                                                                public void call() {
                                                                    greenSoulCrack.detach(hero.belongings.backpack);
                                                                    MissileSpriteCustom msc = (MissileSpriteCustom) hero.sprite.parent.recycle(MissileSpriteCustom.class);
                                                                    msc.reset(
                                                                            hero.sprite,
                                                                            pos,
                                                                            yellowSoulCrack,
                                                                            0.18f,
                                                                            0.5f,
                                                                            new Callback() {
                                                                                @Override
                                                                                public void call() {
                                                                                    yellowSoulCrack.detach(hero.belongings.backpack);
                                                                                    MissileSpriteCustom msc = (MissileSpriteCustom) hero.sprite.parent.recycle(MissileSpriteCustom.class);
                                                                                    msc.reset(
                                                                                            hero.sprite,
                                                                                            pos,
                                                                                            purpleSoulCrack,
                                                                                            0.18f,
                                                                                            0.5f,
                                                                                            new Callback() {
                                                                                                @Override
                                                                                                public void call() {
                                                                                                    GameScene.flash(Window.CBLACK, true);
                                                                                                    purpleSoulCrack.detach(hero.belongings.backpack);
                                                                                                    Statistics.Hollow_Holiday = true;
                                                                                                    yell(Messages.get(BoilerRoom.class, "yell"));
                                                                                                    ((CandleActivePointSprite)sprite).activateidle();
                                                                                                    ArrayList<SoulCrack> s = hero.belongings.getAllItems(SoulCrack.class);
                                                                                                    for (SoulCrack w : s.toArray(new SoulCrack[0])){
                                                                                                        w.detach(hero.belongings.backpack);
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                    );
                                                                                }
                                                                            }
                                                                    );
                                                                }
                                                            }
                                                    );

                                                }
                                            }
                                    );
                                }
                            }
                    );

                }  else {
                    yell(Messages.get(BoilerRoom.class, "yell2"));
                }
            }
            return true;
        }

        @Override
        public int defenseSkill(Char enemy) {
            return INFINITE_EVASION;
        }

        @Override
        public void damage(int dmg, Object src, DamageType type) {
        }

        @Override
        public boolean add(Buff buff) {
            return false;
        }

        @Override
        public boolean reset() {
            return true;
        }

    }

    public static class CandleActivePointSprite extends MobSprite {
        private Animation what_up;
        public CandleActivePointSprite(){
            texture( Assets.Sprites.CANDLESTICK );

            TextureFilm frames = new TextureFilm( texture, 22, 22 );

            idle = new Animation( 2, true );
            idle.frames( frames, Statistics.Hollow_Holiday ? 1: 0 );

            what_up = new Animation( 2, true );
            what_up.frames( frames, 1 );

            run = idle.clone();
            attack = idle.clone();
            die = idle.clone();

            play( idle );
        }

        public void activateidle() {
            play( what_up );
        }
    }

}

