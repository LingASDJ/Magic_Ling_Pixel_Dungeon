package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.HalomethaneFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ArcaneBomb extends Bomb {

	{
		image = ItemSpriteSheet.ARCANE_BOMB;
	}

	@Override
	public String desc() {
		int depth = Dungeon.hero == null ? 1 : Dungeon.scalingDepth();
		String desc = Messages.get(this, "desc", 5 + depth, 14 + 3*depth);
		if (fuse == null) {
			return desc + "\n\n" + Messages.get(Bomb.class, "desc_fuse");
		} else {
			return desc + "\n\n" + Messages.get(Bomb.class, "desc_burning");
		}
	}

	@Override
	public boolean explodesDestructively() {
		return false;
	}

	@Override
	protected int explosionRange() {
		return 2;
	}

	@Override
	public Fuse createFuse() {
		return new ArcaneBombFuse();
	}

	@Override
	public void explode(int cell) {
		super.explode(cell);

		ArrayList<Char> affected = new ArrayList<>();

		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), explosionRange() );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				CellEmitter.get(i).burst(ElmoParticle.FACTORY, 10);
				CellEmitter.get(i).burst(HalomethaneFlameParticle.FACTORY, 3);
				GameScene.add(Blob.seed(i, 1, HalomethaneFire.class));
				Char ch = Actor.findChar(i);
				if (ch != null){
					affected.add(ch);
				}
			}
		}

		//爆炸音效
		Sample.INSTANCE.play(Assets.Sounds.BURNING);

		for (Char ch : affected){
			//奥术伤害（穿透护甲）
			int damage = Math.round(Random.NormalIntRange( 5 + Dungeon.scalingDepth(), 14 + 3*Dungeon.scalingDepth() ));
			ch.damage(damage, this);
			if (ch == Dungeon.hero && !ch.isAlive()){
				Badges.validateDeathFromFriendlyMagic();
				Dungeon.fail(this);
			}
		}
	}

	@Override
	public int value() {
		//prices of ingredients
		return quantity * (20 + 30);
	}

	//正常引信，保留奥术预警粒子
	public static class ArcaneBombFuse extends Fuse {

		private ArrayList<Emitter> gooWarnEmitters = new ArrayList<>();

		@Override
		public Fuse ignite(Bomb bomb) {
			super.ignite(bomb);
			Actor.add(new Actor() {
				{ actPriority = VFX_PRIO; }
				@Override
				protected boolean act() {
					int bombPos = -1;
					for (Heap heap : Dungeon.level.heaps.valueList()) {
						if (heap.items.contains(bomb)) {
							bombPos = heap.pos;
						}
					}
					if (bombPos != -1) {
						PathFinder.buildDistanceMap(bombPos, BArray.not(Dungeon.level.solid, null), bomb.explosionRange());
						for (int i = 0; i < PathFinder.distance.length; i++) {
							if (PathFinder.distance[i] < Integer.MAX_VALUE) {
								Emitter e = CellEmitter.get(i);
								if (e != null) {
									e.pour(GooSprite.GooParticle.FACTORY, 0.03f);
									gooWarnEmitters.add(e);
								}
							}
						}
					}
					Actor.remove(this);
					return true;
				}
			});
			return this;
		}

		@Override
		public void snuff() {
			super.snuff();
			for (Emitter e : gooWarnEmitters) {
				e.on = false;
			}
			gooWarnEmitters.clear();
		}
	}
}