/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.AQUAPHOBIA;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.DHXD;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.EXSG;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.MOREROOM;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.anCityQuestLevel;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.anCityQuestProgress;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.branch;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel.Holiday.XMAS;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SacrificialFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AutoRandomBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RandomBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GoldenMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DragonGirlBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.journal.GuidePage;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LockSword;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.FigureEightBuilder;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.LoopBuilder;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.GardenEntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.GardenExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.AutoShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.GoldRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.HealWellRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.IdenityRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.MagicalFireRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.NxhyShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.NyzBombAndBooksRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.OilWellRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.PitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.PumpkinRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.RandomRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.AquariumRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.BigEyeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CoinRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.DreamcatcherRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EyeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.GooRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.HeartRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.LinkRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.LoveRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.MagicDimandRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public abstract class RegularLevel extends Level {


    public static Holiday holiday;

    public static DevBirthday birthday;

    static {

        holiday = Holiday.NONE;

        final Calendar calendar = Calendar.getInstance();
		switch (calendar.get(Calendar.MONTH)) {
			case Calendar.JANUARY:
				if (calendar.get(Calendar.WEEK_OF_MONTH) == 1)
					holiday = Holiday.XMAS;
				break;
			case Calendar.OCTOBER:
				if (calendar.get(Calendar.WEEK_OF_MONTH) >= 2)
					holiday = Holiday.HWEEN;
				break;
			case Calendar.NOVEMBER:
				if (calendar.get(Calendar.DAY_OF_MONTH) == 1)
					holiday = Holiday.HWEEN;
				break;
			case Calendar.DECEMBER:
				if (calendar.get(Calendar.WEEK_OF_MONTH) >= 3)
					holiday = Holiday.XMAS;
				break;
		}
    }

    protected ArrayList<Room> rooms;
    protected Builder builder;
    protected Room roomEntrance;
    protected Room roomExit;

    @Override
    protected boolean build() {

		builder = builder();

		ArrayList<Room> initRooms = initRooms();
		Random.shuffle(initRooms);

		do {
			for (Room r : initRooms){
				r.neigbours.clear();
				r.connected.clear();
			}
			rooms = builder.build((ArrayList<Room>)initRooms.clone());
		} while (rooms == null);

		return painter().paint(this, rooms);

	}
    //开发团队的生日列表
    // S直接参与Calendar类计算
    // L参与Lunar-Java类计算
    public enum DevBirthday {
        DEV_BIRTHDAY,
        //QinYue S-5.13
        CHAPTER_BIRTHDAY,
        //设寄师
        DESIGN_BIRTHDAY,
        //丹尼尔
        ART_DC_BIRTHDAY,
        //冷群
        ART_LQ_BIRTHDAY,
        //小蓝 S-3.26
        ART_LB_BIRTHDAY,
        //清扬 L-12.3
        ART_CY_BIRTHDAY,
    }

    public enum Holiday {
        NONE,
        DWJ,
        ZQJ, //TBD
        HWEEN,//2nd week of october though first day of november
        XMAS,
		CJ,
    }
	
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();

		if(branch == 5 && depth == 17){
			initRooms.add(roomEntrance = new GardenEntranceRoom());
			initRooms.add(roomExit = new GardenExitRoom());
		} else {
			initRooms.add ( roomEntrance = new EntranceRoom());
			initRooms.add( roomExit = new ExitRoom());
		}



		//force max standard rooms and multiple by 1.5x for large levels
		//force max standard rooms and multiple by 1.5x for large levels
		int standards = standardRooms(feeling == Feeling.LARGE || Dungeon.isChallenged(MOREROOM) && !(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH)));
		if (feeling == Feeling.LARGE || Dungeon.isChallenged(MOREROOM) && !(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH))){
			standards = (int)Math.ceil(standards * 1.5f);
		}



		if(feeling == Feeling.DIEDROOM){
			switch (branch){
				case 0:
					switch (depth) {
						case 4:
							initRooms.add(new GooRoom());
						break;
					}
//				case 2:
//				switch (depth) {
//					case 4:
//						//initRooms.add(new GooRoom());
//						GooBossRoom gooRoom = GooBossRoom.randomGooRoom();
//						initRooms.add(gooRoom);
//						((FigureEightBuilder)builder).setLandmarkRoom(gooRoom);
//						break;
//					case 14:
//						initRooms.add(new OldDM300Room());
//						break;
//				}
				break;
			}
		}

		if(RegularLevel.holiday == Holiday.ZQJ ){
			if(Dungeon.depth == 17 && branch == 0){
				initRooms.add(new HeartRoom());
			}
			if(Statistics.findMoon && Dungeon.depth == 18){
				initRooms.add(new LoveRoom());
			}
		}

		//20%
		if (Dungeon.NxhyshopOnLevel() && branch == 0 && Random.Int(0,100) <= 30 || depth == 28) {
			initRooms.add(new NxhyShopRoom());
		}

//		if(Dungeon.FireLevel()){
//			initRooms.add(new LanFireRoom());
//		}

		if(depth>27 && depth <30){
			if(Random.Float() < 0.5f){
				initRooms.add(new PumpkinRoom());
			}
		}


		//		initRooms.add(new RangeMobRoom());

		if (Dungeon.NyzshopOnLevel() && branch == 0) {
			Buff.affect(hero, RandomBuff.class).set( (4 + Random.Int(9)+hero.STR/6+hero.HP/30)/Random.Int(1,2)+5, 1 );
			initRooms.add(new NyzBombAndBooksRoom());
		}

		if (Dungeon.aqiLevel() && (Dungeon.isChallenged(AQUAPHOBIA)))
			initRooms.add(new AquariumRoom());

		if(Statistics.goldchestmazeCollected>=3 && Dungeon.depth == 9 && !Dungeon.isDLC(Conducts.Conduct.BOSSRUSH)){
			initRooms.add(new MagicDimandRoom());
		}

		//圣诞节
		if (holiday == XMAS) {
			if(Dungeon.AutoShopLevel()) {
				initRooms.add(new AutoShopRoom());
				Buff.affect(hero, AutoRandomBuff.class).set((10), 1);
			}
		} else if(Dungeon.isChallenged(EXSG)){
			if(Dungeon.AutoShopLevel()) {
				initRooms.add(new AutoShopRoom());
				Buff.affect(hero, AutoRandomBuff.class).set((10), 1);
			}
		}

		if (Dungeon.depth >= 26 && Random.Int(10) <= 4) {
			initRooms.add(new BigEyeRoom());
			initRooms.add(new CoinRoom());
		} else if(Dungeon.depth<26 && Random.Int(10) == 1) {
			initRooms.add(new EyeRoom());
		}

		if(Dungeon.exgoldLevel()&&Dungeon.isChallenged(CS)) initRooms.add(new GoldRoom());

		if(Dungeon.depth<26 && Random.Int(30) == 1 && (Dungeon.isChallenged(DHXD) || Statistics.lanterfireactive )){
			initRooms.add(new OilWellRoom());
		}

		if(feeling == Feeling.THREEWELL){
			initRooms.add(new HealWellRoom());
			initRooms.add(new RandomRoom());
			initRooms.add(new IdenityRoom());
		}

		if(feeling == Feeling.LINKROOM){
			initRooms.add(new LinkRoom());
		}

		for (int i = 0; i < standards; i++) {
			StandardRoom s;
			do {
				s = StandardRoom.createRoom();
			} while (!s.setSizeCat( standards-i ));
			i += s.sizeFactor()-1;
			initRooms.add(s);
		}

		if (!Badges.isUnlocked(Badges.Badge.ANCITY_THREE)) {
			if (depth == 18 && !anCityQuestProgress) {
				initRooms.add(new DreamcatcherRoom());
				DragonGirlBlue.Quest.spawned = true;
				anCityQuestProgress = true;
			}
		}

		if (Dungeon.shopOnLevel() && branch == 0)
			initRooms.add(new ShopRoom());

		//force max special rooms and add one more for large levels
		int specials = specialRooms(feeling == Feeling.LARGE || Dungeon.isChallenged(MOREROOM) && !(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH)));
		if (feeling == Feeling.LARGE || Dungeon.isChallenged(MOREROOM) && !(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH))){
			specials++;
		}
		SpecialRoom.initForFloor();
		for (int i = 0; i < specials; i++) {
			SpecialRoom s = SpecialRoom.createRoom();
			if (s instanceof PitRoom) specials++;
			initRooms.add(s);
		}

		if(depth<26){
			int secrets = SecretRoom.secretsForFloor(Dungeon.depth);
			if (feeling == Feeling.SECRETS && depth < 26) secrets++;
			for (int i = 0; i < secrets; i++) {
				initRooms.add(SecretRoom.createRoom());
			}
		}


		
		return initRooms;
	}
	
	protected int standardRooms(boolean forceMax){
		return 0;
	}
	
	protected int specialRooms(boolean forceMax){
		return 0;
	}
	
	protected Builder builder(){
		if (Random.Int(2) == 0){
			return new LoopBuilder()
					.setLoopShape( 2 ,
							Random.Float(0f, 0.65f),
							Random.Float(0f, 0.50f));
		} else {
			return new FigureEightBuilder()
					.setLoopShape( 2 ,
							Random.Float(0.3f, 0.8f),
							0f);
		}

	}
	
	protected abstract Painter painter();
	
	protected int nTraps() {
		return Random.NormalIntRange( 2, 3 + (Dungeon.depth/5) );
	}
	
	protected Class<?>[] trapClasses(){
		return new Class<?>[]{WornDartTrap.class};
	}

	protected float[] trapChances() {
		return new float[]{1};
	}
	
	@Override
	public int mobLimit() {
		if (Dungeon.depth <= 1){
			if (!Statistics.amuletObtained) return 0;
			else                            return 10;
		}

		int mobs = 3 + Dungeon.depth % 5 + Random.Int(3);
		if (feeling == Feeling.LARGE){
			mobs = (int)Math.ceil(mobs * 1.33f);
		}

		// 在特定挑战中怪物生成翻倍
		if (Dungeon.isChallenged(MOREROOM) && (!(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH) || !Dungeon.isChallenged(CS)))) {
			mobs += Random.NormalIntRange(1,3);
		}

		if(Dungeon.isChallenged(CS)){
			mobs -= 1;
		}

		return mobs;
	}
	
	@Override
	protected void createMobs() {
		//on floor 1, 10 pre-set mobs are created so the player can get level 2.
		int mobsToSpawn = Dungeon.depth == 1 ? Dungeon.isChallenged(CS) ? 5 : 10 : mobLimit();

		ArrayList<Room> stdRooms = new ArrayList<>();
		for (Room room : rooms) {
			if (room instanceof StandardRoom && room != roomEntrance) {
				for (int i = 0; i < ((StandardRoom) room).mobSpawnWeight(); i++) {
					stdRooms.add(room);
				}
			}
		}
		Random.shuffle(stdRooms);
		Iterator<Room> stdRoomIter = stdRooms.iterator();

		while (mobsToSpawn > 0) {
			Mob mob = createMob();
			Room roomToSpawn;
			
			if (!stdRoomIter.hasNext()) {
				stdRoomIter = stdRooms.iterator();
			}
			roomToSpawn = stdRoomIter.next();

			int tries = 30;
			do {
				mob.pos = pointToCell(roomToSpawn.random());
				tries--;
			} while (tries >= 0 && (findMob(mob.pos) != null
					|| !passable[mob.pos]
					|| solid[mob.pos]
					|| !roomToSpawn.canPlaceCharacter(cellToPoint(mob.pos), this)
					|| mob.pos == exit()
					|| traps.get(mob.pos) != null || plants.get(mob.pos) != null
					|| (!openSpace[mob.pos] && mob.properties().contains(Char.Property.LARGE))));

			if (tries >= 0) {
				mobsToSpawn--;
				mobs.add(mob);

				//chance to add a second mob to this room, except on floor 1
				if (Dungeon.depth > 1 && mobsToSpawn > 0 && Random.Int(4) == 0){
					mob = createMob();

					tries = 30;
					do {
						mob.pos = pointToCell(roomToSpawn.random());
						tries--;
					} while (tries >= 0 && (findMob(mob.pos) != null
							|| !passable[mob.pos]
							|| solid[mob.pos]
							|| !roomToSpawn.canPlaceCharacter(cellToPoint(mob.pos), this)
							|| mob.pos == exit()
							|| traps.get(mob.pos) != null || plants.get(mob.pos) != null
							|| (!openSpace[mob.pos] && mob.properties().contains(Char.Property.LARGE))));

					if (tries >= 0) {
						mobsToSpawn--;
						mobs.add(mob);
					}
				}
			}
		}

		for (Mob m : mobs){
			if (map[m.pos] == Terrain.HIGH_GRASS || map[m.pos] == Terrain.FURROWED_GRASS) {
				map[m.pos] = Terrain.GRASS;
				losBlocking[m.pos] = false;
			}

		}

	}

	@Override
	public int randomRespawnCell( Char ch ) {
		int count = 0;
		int cell = -1;

		while (true) {

			if (++count > 30) {
				return -1;
			}

			Room room = randomRoom( StandardRoom.class );
			if (room == null || room == roomEntrance) {
				continue;
			}

			cell = pointToCell(room.random(1));
			if (!heroFOV[cell]
					&& Actor.findChar( cell ) == null
					&& passable[cell]
					&& !solid[cell]
					&& (!Char.hasProp(ch, Char.Property.LARGE) || openSpace[cell])
					&& room.canPlaceCharacter(cellToPoint(cell), this)
					&& cell != exit()) {
				return cell;
			}

		}
	}
	
	@Override
	public int randomDestination( Char ch ) {
		
		int count = 0;
		int cell = -1;
		
		while (true) {
			
			if (++count > 30) {
				return -1;
			}
			
			Room room = Random.element( rooms );
			if (room == null) {
				continue;
			}

			ArrayList<Point> points = room.charPlaceablePoints(this);
			if (!points.isEmpty()){
				cell = pointToCell(Random.element(points));
				if (passable[cell] && (!Char.hasProp(ch, Char.Property.LARGE) || openSpace[cell])) {
					return cell;
				}
			}
			
		}
	}
	
	@Override
	protected void createItems() {



		// drops 3/4/5 items 60%/30%/10% of the time
		int nItems = 3 + Random.chances(new float[]{6, 3, 1});


		if (feeling == Feeling.LARGE || Dungeon.isChallenged(MOREROOM) && !(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH))){
			nItems += 2;
		}

		//谜城资源量减半
		if(Dungeon.isChallenged(CS)){
			nItems = nItems/2;
		}

		for (int i=0; i < nItems; i++) {
			Item toDrop = Generator.random();
			if (toDrop == null) continue;

			int cell = randomDropCell();
			if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
				map[cell] = Terrain.GRASS;
				losBlocking[cell] = false;
			}

			Heap.Type type = null;
			switch (Random.Int( 20 )) {
			case 0:
				type = Heap.Type.SKELETON;
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				type = Heap.Type.CHEST;
				break;
			case 5:
				if (Dungeon.depth > 1 && findMob(cell) == null){
					mobs.add(Mimic.spawnAt(cell, toDrop));
					continue;
				}
				type = Heap.Type.CHEST;
				break;
			default:
				type = Heap.Type.HEAP;
				break;
			}

			if ((toDrop instanceof Artifact && Random.Int(2) == 0) ||
					(toDrop.isUpgradable() && Random.Int(4 - toDrop.level()) == 0)){

				if (Dungeon.depth > 1 && Random.Int(10) == 0 && findMob(cell) == null){
					mobs.add(Mimic.spawnAt(cell, GoldenMimic.class, toDrop));
				} else {
					Heap dropped = drop(toDrop, cell);
					if (heaps.get(cell) == dropped) {
						dropped.type = Heap.Type.LOCKED_CHEST;
						addItemToSpawn(new GoldenKey(Dungeon.depth));
					}
				}
			} else {
				Heap dropped = drop( toDrop, cell );
				dropped.type = type;
				if (type == Heap.Type.SKELETON){
					dropped.setHauntedIfCursed();
				}
			}

		}

		for (Item item : itemsToSpawn) {
			int cell = randomDropCell();
			drop( item, cell ).type = Heap.Type.HEAP;
			if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
				map[cell] = Terrain.GRASS;
				losBlocking[cell] = false;
			}
		}

		//use separate generator(s) for this to prevent held items, meta progress, and talents from affecting levelgen
		//we can use a random long for these as they will be the same longs every time

		Random.pushGenerator( Random.Long() );
			if (Dungeon.isChallenged(Challenges.DARKNESS)){
				int cell = randomDropCell();
				if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
					map[cell] = Terrain.GRASS;
					losBlocking[cell] = false;
				}
				drop( new Torch(), cell );
				//add a second torch to help with the larger floor
				if (feeling == Feeling.LARGE){
					cell = randomDropCell();
					if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
						map[cell] = Terrain.GRASS;
						losBlocking[cell] = false;
					}
					drop( new Torch(), cell );
				}
			}
		Random.popGenerator();

		Random.pushGenerator( Random.Long() );
			ArrayList<Item> bonesItems = Bones.get();
			if (bonesItems != null) {
				int cell = randomDropCell();
				if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
					map[cell] = Terrain.GRASS;
					losBlocking[cell] = false;
				}
				for (Item i : bonesItems) {
					drop(i, cell).setHauntedIfCursed().type = Heap.Type.REMAINS;
				}
			}
		Random.popGenerator();

		Random.pushGenerator( Random.Long() );
			DriedRose rose = Dungeon.hero.belongings.getItem( DriedRose.class );
			if (rose != null && rose.isIdentified() && !rose.cursed && Ghost.Quest.completed()){
				//aim to drop 1 petal every 2 floors
				int petalsNeeded = (int) Math.ceil((float)((Dungeon.depth / 2) - rose.droppedPetals) / 3);

				for (int i=1; i <= petalsNeeded; i++) {
					//the player may miss a single petal and still max their rose.
					if (rose.droppedPetals < 11) {
						Item item = new DriedRose.Petal();
						int cell = randomDropCell();
						drop( item, cell ).type = Heap.Type.HEAP;
						if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
							map[cell] = Terrain.GRASS;
							losBlocking[cell] = false;
						}
						rose.droppedPetals++;
					}
				}
			}
		Random.popGenerator();

		//cached rations try to drop in a special room on floors 2/3/4/6/7/8, to a max of 4/6
		Random.pushGenerator( Random.Long() );
			if (Dungeon.hero.hasTalent(Talent.CACHED_RATIONS)){
				Talent.CachedRationsDropped dropped = Buff.affect(Dungeon.hero, Talent.CachedRationsDropped.class);
				if (dropped.count() < 2 + 2*Dungeon.hero.pointsInTalent(Talent.CACHED_RATIONS)){
					int cell;
					int tries = 100;
					boolean valid;
					do {
						cell = randomDropCell(SpecialRoom.class);
						valid = cell != -1 && !(room(cell) instanceof SecretRoom)
								&& !(room(cell) instanceof ShopRoom)
								&& map[cell] != Terrain.EMPTY_SP
								&& map[cell] != Terrain.WATER
								&& map[cell] != Terrain.PEDESTAL;
					} while (tries-- > 0 && !valid);
					if (valid) {
						if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
							map[cell] = Terrain.GRASS;
							losBlocking[cell] = false;
						}
						drop(new SmallRation(), cell).type = Heap.Type.CHEST;
						dropped.countUp(1);
					}
				}
			}
		Random.popGenerator();

		//guide pages
		Random.pushGenerator( Random.Long() );
			Collection<String> allPages = Document.ADVENTURERS_GUIDE.pageNames();
			ArrayList<String> missingPages = new ArrayList<>();
			for ( String page : allPages){
				if (!Document.ADVENTURERS_GUIDE.isPageFound(page)){
					missingPages.add(page);
				}
			}

			//a total of 6 pages drop randomly, the rest are specially dropped or are given at the start
			missingPages.remove(Document.GUIDE_SEARCHING);

			//chance to find a page is 0/25/50/75/100% for floors 1/2/3/4/5+
			float dropChance = 0.25f*(Dungeon.depth-1);
			if (!missingPages.isEmpty() && Random.Float() < dropChance){
				GuidePage p = new GuidePage();
				p.page(missingPages.get(0));
				int cell = randomDropCell();
				if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
					map[cell] = Terrain.GRASS;
					losBlocking[cell] = false;
				}
				drop( p, cell );
			}
		Random.popGenerator();
	}


	private static HashMap<Document, Dungeon.LimitedDrops> limitedDocs = new HashMap<>();
	static {
		limitedDocs.put(Document.SEWERS_GUARD, Dungeon.LimitedDrops.LORE_SEWERS);
		limitedDocs.put(Document.PRISON_WARDEN, Dungeon.LimitedDrops.LORE_PRISON);
		limitedDocs.put(Document.CAVES_EXPLORER, Dungeon.LimitedDrops.LORE_CAVES);
		limitedDocs.put(Document.CITY_WARLOCK, Dungeon.LimitedDrops.LORE_CITY);
		limitedDocs.put(Document.HALLS_KING, Dungeon.LimitedDrops.LORE_HALLS);
	}
	
	public ArrayList<Room> rooms() {
		return new ArrayList<>(rooms);
	}
	
	protected Room randomRoom( Class<?extends Room> type ) {
		Random.shuffle( rooms );
		for (Room r : rooms) {
			if (type.isInstance(r)) {
				return r;
			}
		}
		return null;
	}
	
	public Room room( int pos ) {
		for (Room room : rooms) {
			if (room.inside( cellToPoint(pos) )) {
				return room;
			}
		}
		
		return null;
	}

	protected int randomDropCell(){
		return randomDropCell(StandardRoom.class);
	}
	
	protected int randomDropCell( Class<?extends Room> roomType ) {
		int tries = 100;
		while (tries-- > 0) {
			Room room = randomRoom( roomType );
			if (room == null){
				return -1;
			}
			if (room != roomEntrance) {
				int pos = pointToCell(room.random());
				if (passable[pos] && !solid[pos]
						&& pos != exit()
						&& heaps.get(pos) == null
						&& room.canPlaceItem(cellToPoint(pos), this)
						&& findMob(pos) == null) {
					
					Trap t = traps.get(pos);
					
					//items cannot spawn on traps which destroy items
					if (t == null ||
							! (t instanceof BurningTrap || t instanceof BlazingTrap
							|| t instanceof ChillingTrap || t instanceof FrostTrap
							|| t instanceof ExplosiveTrap || t instanceof DisintegrationTrap
							|| t instanceof PitfallTrap)) {
						
						return pos;
					}
				}
			}
		}
		return -1;
	}
	
	@Override
	public int fallCell( boolean fallIntoPit ) {
		if (fallIntoPit) {
			for (Room room : rooms) {
				if (room instanceof PitRoom) {
					ArrayList<Integer> candidates = new ArrayList<>();
					for (Point p : room.getPoints()){
						int cell = pointToCell(p);
						if (passable[cell] &&
								findMob(cell) == null){
							candidates.add(cell);
						}
					}

					if (!candidates.isEmpty()){
						return Random.element(candidates);
					}
				}
			}
		}
		
		return super.fallCell( fallIntoPit );
	}

	@Override
	public boolean isLevelExplored( int depth ) {
		//A level is considered fully explored if:

		//There are no levelgen heaps which are undiscovered, in an openable container, or which contain keys
		for (Heap h : heaps.valueList()){
			if (h.autoExplored) continue;

			if (!h.seen || (h.type != Heap.Type.HEAP && h.type != Heap.Type.FOR_SALE && h.type != Heap.Type.CRYSTAL_CHEST && h.type != Heap.Type.BLACK && h.type != Heap.Type.FOR_ICE)){
				return false;
			}

			for (Item i : h.items){
				if (i instanceof Key ){
					return false;
				}
			}
		}

		//There is no magical fire or sacrificial fire
		for (Blob b : blobs.values()){
			if (b.volume > 0 && (b instanceof MagicalFireRoom.EternalFire || b instanceof SacrificialFire)){
				return false;
			}
		}

		//There are no statues or mimics (unless they were made allies)
		for (Mob m : mobs.toArray(new Mob[0])){
			if (m.alignment != Char.Alignment.ALLY){
				if (m instanceof Statue && ((Statue) m).levelGenStatue){
					return false;
				} else if (m instanceof Mimic){
					return false;
				}
			}
		}

		//There are no barricades, locked doors, or hidden doors
		for (int i = 0; i < length; i++){
			if (map[i] == Terrain.BARRICADE || map[i] == Terrain.LOCKED_DOOR || map[i] == Terrain.SECRET_DOOR){
				return false;
			}
		}

		//There are no unused keys for this depth in the journal
		for (Notes.KeyRecord rec : Notes.getRecords(Notes.KeyRecord.class)){
			if (rec.depth() == depth){
				return false;
			}
		}

		//Note that it is NOT required for the player to see every tile or discover every trap.
		return true;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "rooms", rooms );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		rooms = new ArrayList<>( (Collection<Room>) ((Collection<?>) bundle.getCollection( "rooms" )) );
		for (Room r : rooms) {
			r.onLevelLoad( this );
			if (r instanceof EntranceRoom ){
				roomEntrance = r;
			} else if (r instanceof ExitRoom ){
				roomExit = r;
			}
		}
	}
	
}
