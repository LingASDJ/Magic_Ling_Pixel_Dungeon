package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ignite;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Inebriation;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * <div style="color:orange;font-weight:bold" >
 *     <h3>酒气</h3>
 *     <ul>
 *       <li>处于其中的单位每回合积累10%醉意。</li>
 *       <li>遇到火时，与火相连的所有酒气立刻变成爆燃区域。</li>
 *       <li>持续时间：基础3回合，每100点气体量再+1回合。</li>
 *       <li>只炸和火相连的那一团，隔着墙的另一团酒气会留着继续熏。</li>
 *     </ul>
 * </div>
 *
 * <p>分散实现的效果如下
 * <ul>
 *   <li>醉意— {@link Inebriation} 自己检测脚下有没有酒气并每回合+10%，
 *       {@link #attract()} 只负责把buff挂到人身上</li>
 *   <li>遇火爆燃— {@link #igniteIfTouchingFire()} 洪水填充出连通区域，
 *       再交给 {@link BlazeZone#ignite(java.util.List, int)} 铺成爆燃区域</li>
 *   <li>持续时间— {@link #seed(Level, int, int)} 里按气体量算，{@link #act()} 里倒计时</li>
 *   <li>文本与粒子— {@code actors.properties} 的{@code actors.blobs.alcoholgas.*}，
 *       粒子还是占位（{@code Speck.CONFUSION}）</li>
 * </ul>
 *
 * @see BlazeZone   爆燃区域
 * @see Inebriation 醉意
 */
//酒气：处于其中的单位每回合积累10%醉意。酒气在遇到火时将所有所有相连的酒气变为爆燃区域。每有100气体量时持续时间+1，基础时间3回合。
public class AlcoholGas extends Blob {

	//基础持续时间（回合）
	public static final int BASE_LIFE = 3;
	//每多少气体量额外+1回合
	public static final int VOLUME_PER_TURN = 100;

	private int lifeTime = 0;

	// ---- 生成：对外只暴露这一个入口 ----
	public static void spawn(int cell, int amount) {
		GameScene.add( Blob.seed(cell, amount, AlcoholGas.class) );
	}

	// ---- 叠加时维护持续时间：3 + 气体量/100 ----
	@Override
	public void seed(Level level, int cell, int amount) {
		super.seed(level, cell, amount);
		lifeTime = BASE_LIFE + volume / VOLUME_PER_TURN;
	}

	@Override
	public boolean act() {
		boolean cont = super.act();      //花掉这一回合，并跑基类的扩散

		if (lifeTime <= 0) {             //已经散掉了，空转
			fullyClear();
			return cont;
		}

		//遇火：这一回合不再熏人，整团酒气直接转成爆燃区域
		if (igniteIfTouchingFire()) return cont;

		if (volume > 0) attract();

		//走完剩余回合就散
		if (--lifeTime <= 0) fullyClear();
		return cont;
	}

	// ---- 每回合给站在酒气里的单位挂上醉意 ----
	//注意：+10%那一笔是 Inebriation 自己结算的，这里只保证buff存在
	private void attract() {
		for (int cell : getActiveCells()) {
			Char ch = Actor.findChar(cell);
			if (ch != null) {
				Buff.affect(ch, Inebriation.class);
			}
		}
	}

	// ---- 遇火：相连的酒气整体爆燃 ----

	/**
	 * 检查酒气有没有碰到火，碰到就把连通的那一团全部换成爆燃区域。
	 * @return 是否发生了爆燃（发生了的话这一回合的酒气效果就不再结算）
	 */
	private boolean igniteIfTouchingFire() {
		if (volume <= 0) return false;

		List<Integer> active = getActiveCells();
		if (active.isEmpty()) return false;

		boolean[] inGas = new boolean[cur.length];
		for (int cell : active) inGas[cell] = true;

		ArrayList<Integer> touched = new ArrayList<>();
		for (int cell : active) {
			if (fireNear(cell)) touched.add(cell);
		}
		if (touched.isEmpty()) return false;

		ArrayList<Integer> burning = connected(inGas, touched);
		if (burning.isEmpty()) return false;

		//剩多少回合就烧多少回合：气体量已经换成时长，爆燃自然烧完即止
		BlazeZone.ignite(burning, Math.max(1, lifeTime));

		//转成火焰的格子不再有酒气，没连上的其他酒气继续留着
		for (int cell : burning) clear(cell);
		if (volume <= 0) fullyClear();

		return true;
	}

	/** 这一格或相邻八格上有没有火：火焰blob、爆燃区域，或者正在燃烧的单位 */
	private static boolean fireNear(int cell) {
		for (int ofs : PathFinder.NEIGHBOURS9) {
			int c = cell + ofs;
			if (!Dungeon.level.insideMap(c)) continue;

			if (Blob.volumeAt(c, Fire.class) > 0) return true;
			if (Blob.volumeAt(c, BlazeZone.class) > 0) return true;

			Char ch = Actor.findChar(c);
			if (ch != null && Ignite.hasFireBuff(ch)) return true;
		}
		return false;
	}

	/** 从着火的格子出发，把八方向连成一片的酒气都收集出来 */
	private ArrayList<Integer> connected(boolean[] inGas, ArrayList<Integer> seeds) {
		boolean[] seen = new boolean[inGas.length];
		ArrayList<Integer> result = new ArrayList<>();
		ArrayDeque<Integer> queue = new ArrayDeque<>();

		for (int cell : seeds) {
			if (inGas[cell] && !seen[cell]) {
				seen[cell] = true;
				queue.add(cell);
			}
		}

		while (!queue.isEmpty()) {
			int cell = queue.poll();
			result.add(cell);
			for (int ofs : PathFinder.NEIGHBOURS8) {
				int c = cell + ofs;
				if (c < 0 || c >= inGas.length) continue;
				if (inGas[c] && !seen[c]) {
					seen[c] = true;
					queue.add(c);
				}
			}
		}
		return result;
	}

	// 视觉
	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);                       // 必须调，负责给 this.emitter 赋值
		emitter.pour(Speck.factory(Speck.CONFUSION, true), 0.4f);  // TODO 占位ing……
	}

	// 文本
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}

	// 持久化
	private static final String LIFE_TIME = "lifeTime";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LIFE_TIME, lifeTime);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		lifeTime = bundle.getInt(LIFE_TIME);
	}
}
