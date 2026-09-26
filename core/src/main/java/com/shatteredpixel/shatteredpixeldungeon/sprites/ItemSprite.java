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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Matrix;
import com.watabou.glwrap.Vertexbuffer;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.RectF;

import java.nio.Buffer;

public class ItemSprite extends MovieClip {

	public static final int SIZE	= 16;
	
	private static final float DROP_INTERVAL = 0.4f;
	
	public Heap heap;
	
	private Glowing glowing;
	// 精灵尚未挂载到父节点时暂存的动态物品(AnimationItem)，
	// 挂载后首个 update() 会自动补播其动画（如 ChangeButton 构造时的 ItemSprite）
	private Item pendingAnimItem;
	//FIXME: a lot of this emitter functionality isn't very well implemented.
	//right now I want to ship 0.3.0, but should refactor in the future.
	protected Emitter emitter;
	private float phase;
	private boolean glowUp;
	
	private float dropInterval;

	//the amount the sprite is raised from flat when viewed in a raised perspective
	protected float perspectiveRaise    = 5 / 16f; //5 pixels

	//the width and height of the shadow are a percentage of sprite size
	//offset is the number of pixels the shadow is moved down or up (handy for some animations)
	protected boolean renderShadow  = false;
	protected float shadowWidth     = 1f;
	protected float shadowHeight    = 0.25f;
	protected float shadowOffset    = 0.5f;
	
	public ItemSprite() {
		this( ItemSpriteSheet.SOMETHING, null );
	}
	
	public ItemSprite( Heap heap ){
		super(Assets.Sprites.ITEMS);
		view( heap );
	}
	
	public ItemSprite( Item item ) {
		super(Assets.Sprites.ITEMS);
		view( item );
	}
	
	public ItemSprite( int image ){
		this( image, null );
	}
	
	public ItemSprite( int image, Glowing glowing ) {
		super( Assets.Sprites.ITEMS );
		view(image, glowing);
	}
	
	public void link() {
		link(heap);
	}
	
	public void link( Heap heap ) {
		this.heap = heap;
		view(heap);
		renderShadow = true;
		visible = heap.seen;
		place(heap.pos);
	}
	
	@Override
	public void revive() {
		super.revive();

		pendingAnimItem = null;

		speed.set( 0 );
		acc.set( 0 );
		dropInterval = 0;

		// 复用池里的精灵不携带任何残留的动画/图集状态，
		// 否则下次被分配渲染其他物品时会出现整张图集闪现、图标错乱
		curAnim = null;
		resetColor();
		scale.set(1);
		angle = 0;
		texture( Assets.Sprites.ITEMS );
		// 关键：texture() 会把 UV 重置为 (0,0,1,1)（整张 items.png）。
		// 若不立刻 frame() 一个合法小图，一旦在 link()/view() 之前被绘制，
		// 或 view() 链路因动态物品 frames() 异常中断，就会渲染出整张 items.png。
		// 这里先锚定到占位图标作为安全网，后续 link()→view() 会覆盖为真正的物品图标。
		frame( ItemSpriteSheet.SOMETHING );

		heap = null;
		if (emitter != null) {
			emitter.killAndErase();
			emitter = null;
		}
	}

	@Override
	public void copy(Image other) {
		super.copy(other);

		if (other instanceof ItemSprite && ((ItemSprite) other).glowing != null){
			glow(((ItemSprite) other).glowing);
		}

	}

	/**
	 * 生成保留动画状态的副本。
	 * Image.copy() 只复制纹理/帧/颜色，不会复制 MovieClip 的动画状态
	 * （curAnim、帧进度、待播物品），动态物品(AnimationItem)需要专用复制，
	 * 否则在网格/弹窗等 UI 中会退化为静态图标。
	 */
	public ItemSprite copySprite(){
		ItemSprite copy = new ItemSprite();
		copy.copy( this );
		copy.curAnim = curAnim;
		copy.curFrame = curFrame;
		copy.frameTimer = frameTimer;
		copy.finished = finished;
		copy.paused = paused;
		copy.pendingAnimItem = pendingAnimItem;
		return copy;
	}

	/**
	 * 动画帧自动水平垂直对齐到静态图标矩形。
	 * 静态图标(items.png)通过 assignItemRect 定义实际显示尺寸（如 15×16、12×14），
	 * 而动画图集统一按 16×16 网格切帧。若不处理，动画开播后精灵尺寸会从
	 * 静态尺寸变为 16×16，导致已按静态尺寸布局居中的图标水平垂直偏移，
	 * 与图鉴/快捷栏/按钮里的静态图标不一致。
	 * 此方法把当前动画的每一帧裁剪到与静态图标一致的像素尺寸并居中，
	 * 尺寸不再跳变，动画与静态图标完全对齐。已与静态尺寸一致的帧（16×16）
	 * 不做任何处理。
	 */
	public void alignAnimationToStaticSize(Item item){
		if (curAnim == null || curAnim.frames == null || item == null) return;
		RectF staticRect = ItemSpriteSheet.film.get(item.image());
		if (staticRect == null) return;
		float sw = ItemSpriteSheet.film.width(staticRect);
		float sh = ItemSpriteSheet.film.height(staticRect);
		if (sw <= 0 || sh <= 0) return;

		float texW = texture.width;
		float texH = texture.height;

		boolean changed = false;
		RectF[] frames = curAnim.frames;
		for (int i = 0; i < frames.length; i++){
			RectF f = frames[i];
			if (f == null) continue;
			float fw = f.width() * texW;
			float fh = f.height() * texH;
			if (Math.abs(fw - sw) < 0.01f && Math.abs(fh - sh) < 0.01f) continue;
			float dx = (fw - sw) / 3f / texW;
			float dy = (fh - sh) / 3f / texH;
			frames[i] = new RectF(f.left + dx, f.top + dy, f.right - dx, f.bottom - dy);
			changed = true;
		}
		if (changed){
			play(curAnim, true);
		}
	}

	public void visible(boolean value){
		this.visible = value;
		if (emitter != null && !visible){
			emitter.killAndErase();
			emitter = null;
		}
	}
	
	public PointF worldToCamera( int cell ) {
		final int csize = DungeonTilemap.SIZE;
		
		return new PointF(
				PixelScene.align(Camera.main, ((cell % Dungeon.level.width()) + 0.5f) * csize - width() * 0.5f),
				PixelScene.align(Camera.main, ((cell / Dungeon.level.width()) + 1.0f) * csize - height() - csize * perspectiveRaise)
		);
	}
	
	public void place( int p ) {
		if (Dungeon.level != null) {
			point(worldToCamera(p));
			shadowOffset = 0.5f;
		}
	}
	
	public void drop() {

		if (heap.isEmpty()) {
			return;
		} else if (heap.size() == 1){
			// normally this would happen for any heap, however this is not applied to heaps greater than 1 in size
			// in order to preserve an amusing visual bug/feature that used to trigger for heaps with size > 1
			// where as long as the player continually taps, the heap sails up into the air.
			place(heap.pos);
		}
			
		dropInterval = DROP_INTERVAL;
		
		speed.set( 0, -100 );
		acc.set(0, -speed.y / DROP_INTERVAL * 2);
		
		if (heap != null && heap.seen && heap.peek() instanceof Gold) {
			CellEmitter.center( heap.pos ).burst( Speck.factory( Speck.COIN ), 5 );
			Sample.INSTANCE.play( Assets.Sounds.GOLD, 1, 1, Random.Float( 0.9f, 1.1f ) );
		}
	}
	
	public void drop( int from ) {

		if (heap.pos == from) {
			drop();
		} else {
			
			float px = x;
			float py = y;
			drop();
			
			place(from);
	
			speed.offset((px - x) / DROP_INTERVAL, (py - y) / DROP_INTERVAL);
		}
	}

	public ItemSprite view(Item item){
		return view(item,false);
	}

	public ItemSprite view( Item item, boolean b){
		if (this.emitter != null) {
			this.emitter.killAndErase();
			this.emitter = null;
		}

		// 精灵状态重置统一在 view(int, Glowing) 中完成，这里不再重复重置；
		// 动态物品(AnimationItem)的动画由下方 frames() 按需启用。
		view(item.image(), item.glowing());

		Emitter emitter = item.emitter();
		if (emitter != null && parent != null) {
			emitter.pos( this );
			parent.add( emitter );
			this.emitter = emitter;
		}

		// 动态物品(AnimationItem)的动画要求 parent != null：
		// 精灵已挂载时直接播放；尚未挂载（如 ChangeButton 构造时的 ItemSprite）
		// 则暂存物品，等挂载后由 update() 自动补播。
		if (!b && item.animation && item instanceof Item.AnimationItem) {
			if (parent != null) {
				item.frames(this);
				alignAnimationToStaticSize(item);
				pendingAnimItem = null;
			} else {
				pendingAnimItem = item;
			}
		} else {
			pendingAnimItem = null;
		}

		return this;
	}

	public ItemSprite view( Heap heap ){
		if (heap.size() <= 0 || heap.items == null){
			return view( 0, null );
		}

		switch (heap.type) {
			case HEAP: case FOR_SALE:case FOR_ICE:case FOR_RUSH:
				// 先获取精灵，再设置透明度，最后返回
				ItemSprite sprite = view( heap.peek(), false );
				sprite.alpha( heap.hidden ? 0.15f : 1f ); // 关键：给返回的精灵设置alpha
				return sprite;

			case CHEST:
				return view( ItemSpriteSheet.CHEST, null );
			case LOCKED_CHEST:
			case GOLDEN_CHEST:
				return view( ItemSpriteSheet.LOCKED_CHEST, null );
			case GREEN_CHSET:
				return view( ItemSpriteSheet.SHPD_CHEST, null );
			case CRYSTAL_CHEST:case TELECRYSTL:
				return view( ItemSpriteSheet.CRYSTAL_CHEST, null );
			case TOMB:
				return view( ItemSpriteSheet.TOMB, null );
			case WHITETOMB:
				return view( ItemSpriteSheet.GRAVE, null );
			case SKELETON:
				return view( ItemSpriteSheet.BONES, null );
			case REMAINS:
				if (Dungeon.level.diedname == null) {
					return view(ItemSpriteSheet.REMAINS, null);
				}
				return view(ItemSpriteSheet.RIP, null);

			case BLACK:
				return view( ItemSpriteSheet.EBONY_CHEST, null );
			default:
				return view( 0, null );
		}
	}
	
	public ItemSprite view( int image, Glowing glowing ) {
		if (this.emitter != null) this.emitter.killAndErase();
		emitter = null;

		// 所有物品渲染最终都汇聚到此方法。统一切断残留的动画引用并重置精灵
		// 状态（纹理/颜色/缩放/角度），防止动态物品(AnimationItem)的动画图集
		// 纹理与循环动画泄漏到之后渲染的其他物品上（如丢出动态物品后，快捷栏、
		// 背包、地面物品的图标错乱）。
		curAnim = null;
		resetColor();
		scale.set(1);
		angle = 0;
		texture( Assets.Sprites.ITEMS );

		frame( image );
		glow( glowing );
		return this;
	}

	public void frame( int image ){
		RectF f = ItemSpriteSheet.film.get( image );
		if (f == null) {
			// 防御：图集索引越界时回退到占位图标。
			// 若直接 frame(null) 会抛 NPE，并让精灵停留在 texture() 刚设置的
			// 整张图集(0,0,1,1)状态上，表现为"整个 items.png 显示出来"。
			f = ItemSpriteSheet.film.get( ItemSpriteSheet.SOMETHING );
		}
		if (f == null && texture != null && texture.width > 0) {
			// 最终兜底：即使 SOMETHING 也未注册（极端时序下类初始化不完整），
			// 也不要 frame(null)——那会 NPE 并让精灵停留在整张图集状态。
			// 钉到图集左上角 16x16 的一小块，避免渲染整张 items.png。
			f = new RectF( 0, 0, SIZE / (float)texture.width, SIZE / (float)texture.height );
		}
		if (f != null) {
			frame( f );
			float height = ItemSpriteSheet.film.height( f );
			//adds extra raise to very short items, so they are visible
			if (height < 8f){
				perspectiveRaise =  (5 + 8 - height) / 16f;
		}
	}
	}
	
	public synchronized void glow( Glowing glowing ){
		this.glowing = glowing;
		if (glowing == null) resetColor();
	}

	@Override
	public void kill() {
		super.kill();
		pendingAnimItem = null;
		if (emitter != null) {
			emitter.on = false;
			emitter.autoKill = true;
		}
		emitter = null;
	}

	private float[] shadowMatrix = new float[16];

	@Override
	protected void updateMatrix() {
		super.updateMatrix();
		Matrix.copy(matrix, shadowMatrix);
		Matrix.translate(shadowMatrix,
				(width() * (1f - shadowWidth)) / 2f,
				(height() * (1f - shadowHeight)) + shadowOffset);
		Matrix.scale(shadowMatrix, shadowWidth, shadowHeight);
	}

	@Override
	public void draw() {
		if (texture == null || (!dirty && buffer == null))
			return;

		if (renderShadow) {
			if (dirty) {
				((Buffer)verticesBuffer).position(0);
				verticesBuffer.put(vertices);
				if (buffer == null)
					buffer = new Vertexbuffer(verticesBuffer);
				else
					buffer.updateVertices(verticesBuffer);
				dirty = false;
			}

			NoosaScript script = script();

			texture.bind();

			script.camera(camera());

			updateMatrix();

			script.uModel.valueM4(shadowMatrix);
			script.lighting(
					0, 0, 0, am * .6f,
					0, 0, 0, aa * .6f);

			script.drawQuad(buffer);
		}

		super.draw();

	}

	@Override
	public synchronized void update() {
		super.update();

		// 补播：精灵挂载到父节点后，启动之前暂存的动态物品动画
		if (pendingAnimItem != null && parent != null) {
			Item i = pendingAnimItem;
			pendingAnimItem = null;
			if (i.animation && i instanceof Item.AnimationItem) {
				i.frames(this);
				alignAnimationToStaticSize(i);
			}
		}

		visible = (heap == null || heap.seen);

		if (emitter != null){
			emitter.visible = visible;
		}

		if (dropInterval > 0){
			shadowOffset -= speed.y * Game.elapsed * 0.8f;

			if ((dropInterval -= Game.elapsed) <= 0){

				speed.set(0);
				acc.set(0);
				shadowOffset = 0.25f;
				place(heap.pos);

				if (visible) {

					if (Dungeon.level.water[heap.pos]) {
						GameScene.ripple(heap.pos);
					}

					if (Dungeon.level.water[heap.pos]) {
						Sample.INSTANCE.play( Assets.Sounds.WATER, 0.8f, Random.Float( 1f, 1.45f ) );
					} else if (Dungeon.level.map[heap.pos] == Terrain.EMPTY_SP) {
						Sample.INSTANCE.play( Assets.Sounds.STURDY, 0.8f, Random.Float( 1.16f, 1.25f ) );
					} else if (Dungeon.level.map[heap.pos] == Terrain.GRASS
							|| Dungeon.level.map[heap.pos] == Terrain.EMBERS
							|| Dungeon.level.map[heap.pos] == Terrain.FURROWED_GRASS){
						Sample.INSTANCE.play( Assets.Sounds.GRASS, 0.8f, Random.Float( 1.16f, 1.25f ) );
					} else if (Dungeon.level.map[heap.pos] == Terrain.HIGH_GRASS) {
						Sample.INSTANCE.play( Assets.Sounds.STEP, 0.8f, Random.Float( 1.16f, 1.25f ) );
					} else {
						Sample.INSTANCE.play( Assets.Sounds.STEP, 0.8f, Random.Float( 1.16f, 1.25f ));
					}
				}
			}
		}

		if (visible && glowing != null) {
			if (glowUp && (phase += Game.elapsed) > glowing.period) {
				
				glowUp = false;
				phase = glowing.period;
				
			} else if (!glowUp && (phase -= Game.elapsed) < 0) {
				
				glowUp = true;
				phase = 0;
				
			}
			
			float value = phase / glowing.period * 0.6f;
			
			rm = gm = bm = 1 - value;
			ra = glowing.red * value;
			ga = glowing.green * value;
			ba = glowing.blue * value;
		}
	}

	public static int pick( int index, int x, int y ) {
		SmartTexture tx = TextureCache.get( Assets.Sprites.ITEMS );
		int rows = tx.width / SIZE;
		int row = index / rows;
		int col = index % rows;
		return tx.getPixel( col * SIZE + x, row * SIZE + y );
	}
	
	public static class Glowing {
		
		public int color;
		public float red;
		public float green;
		public float blue;
		public float period;
		
		public Glowing( int color ) {
			this( color, 1f );
		}
		
		public Glowing( int color, float period ) {

			this.color = color;

			red = (color >> 16) / 255f;
			green = ((color >> 8) & 0xFF) / 255f;
			blue = (color & 0xFF) / 255f;
			
			this.period = period;
		}
	}
}
