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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.input.PointerEvent;
import com.watabou.input.ScrollEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ScrollArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

public class ScrollPane extends Component {

	protected static final int THUMB_COLOR		= 0xFF7b8073;
	protected static final float THUMB_ALPHA	= 0.5f;

	//Seems everyone's happy about the thumb bar just being there?
	//Oh,That insteresting! Ling
	public void disableThumb(){
		thumb.visible = false;
	}

	protected PointerController controller;
	protected Component content;
	protected ColorBlock thumb;

	public ScrollPane( Component content ) {
		super();

		this.content = content;
		addToBack( content );

		width = content.width();
		height = content.height();

		content.camera = new Camera( 0, 0, 1, 1, PixelScene.defaultZoom );
		Camera.add( content.camera );
	}

	@Override
	public void destroy() {
		super.destroy();
		Camera.remove( content.camera );
	}

	public void scrollTo( float x, float y ) {
		content.camera.scroll.set( x, y );
		thumb.y = this.y + height * content.camera.scroll.y / content.height();
	}

	@Override
	protected void createChildren() {
		controller = new PointerController();
		add( controller );

		thumb = new ColorBlock( 1, 1, THUMB_COLOR );
		thumb.am = THUMB_ALPHA;
		add( thumb );
	}

	@Override
	protected void layout() {

		content.setPos( 0, 0 );
		controller.x = x;
		controller.y = y;
		controller.width = width;
		controller.height = height;

		Point p = camera().cameraToScreen( x, y );
		Camera cs = content.camera;
		cs.x = p.x;
		cs.y = p.y;
		cs.resize( (int)width, (int)height );

		//内容尺寸变化后把滚动位置钳制回合法区间，避免内容缩小时停留在越界位置
		if (content.height() > height) {
			if (content.camera.scroll.y > content.height() - height) {
				content.camera.scroll.y = content.height() - height;
			}
			if (content.camera.scroll.y < 0) {
				content.camera.scroll.y = 0;
			}
		} else {
			content.camera.scroll.y = 0;
		}
		if (content.width() > width) {
			if (content.camera.scroll.x > content.width() - width) {
				content.camera.scroll.x = content.width() - width;
			}
			if (content.camera.scroll.x < 0) {
				content.camera.scroll.x = 0;
			}
		} else {
			content.camera.scroll.x = 0;
		}

		thumb.visible = height < content.height();
		if (thumb.visible) {
			thumb.scale.set( 2, height * height / content.height() );
			thumb.x = right() - thumb.width();
			thumb.y = y + height * content.camera.scroll.y / content.height();
		}
	}

	//确保滚动控制器始终位于指针事件监听队列最前，
	//即使背包槽位/按钮等子元素消费了 DOWN，拖动滚动仍能生效
	public void refreshPointerPriority(){
		controller.givePointerPriority();
	}

	/** 查询并清除"刚刚拖动过"标志（转发给控制器），拖动结束后不误触发下方按钮的点击 */
	public boolean wasDragging(){
		return controller.wasDragging();
	}

	/** 当前是否正在拖拽滚动（转发给控制器），用于抑制拖拽期间的长按 */
	public boolean isDragging(){
		return controller.isDragging();
	}

	public Component content() {
		return content;
	}

	public void onClick( float x, float y ) {
	}

	public class PointerController extends ScrollArea {

		private float dragThreshold;

		public PointerController() {
			super( 0, 0, 0, 0 );
			dragThreshold = PixelScene.defaultZoom * 8;
			//置于指针事件监听队列最前：即使下方槽位/按钮消费了 DOWN，
			//本控制器也能先收到 DOWN 并跟踪拖动（否则有物品的槽位无法拖拽滚动）
			givePointerPriority();
		}

		//记录"上一次指针抬起时正处于拖动"，供下方按钮判断本次点击是否应被忽略
		private boolean justDragged = false;

		/** 查询并清除"刚刚拖动过"标志；背包槽位在 onClick 前调用，拖动结束后不误触发物品点击 */
		public boolean wasDragging() {
			boolean result = justDragged;
			justDragged = false;
			return result;
		}

		/** 当前是否正在拖拽滚动（用于抑制拖拽期间的长按） */
		public boolean isDragging() {
			return dragging;
		}

		@Override
		public boolean onSignal( PointerEvent event ) {
			//重写：DOWN/UP 只跟踪不消费，让下方槽位/按钮的点击逻辑照常；
			//拖动统一由本控制器处理（scroll()）；HOVER 不处理以免干扰子元素 hover/tooltip
			boolean hit = event != null && target.overlapsScreenPoint( (int)event.current.x, (int)event.current.y );

			if (!isActive()) {
				return (hit && blockLevel == ALWAYS_BLOCK);
			}

			if (hit) {

				if (event.type == PointerEvent.Type.DOWN) {
					//新一次按压开始，清除上一次可能残留的"拖动过"标志
					justDragged = false;
					if (curEvent == null) {
						curEvent = event;
					}
					onPointerDown( event );
					return false;

				} else if (event.type == PointerEvent.Type.UP) {
					if (event == curEvent) {
						onPointerUp( event );
						curEvent = null;
					}
					return false;
				}
				return false;

			} else {

				if (event == null && curEvent != null) {
					onDrag( curEvent );

				} else if (curEvent != null && event.type == PointerEvent.Type.UP) {
					onPointerUp( event );
					curEvent = null;
				}
				return false;

			}
		}

		@Override
		protected void onScroll(ScrollEvent event) {
			PointF newPt = new PointF(lastPos);
			newPt.y -= event.amount * content.camera.zoom * 10;
			scroll(newPt);
			dragging = false;
		}

		@Override
		protected void onPointerUp( PointerEvent event ) {
			if (dragging) {

				dragging = false;
				thumb.am = THUMB_ALPHA;
				//标记"本次指针抬起前处于拖动"，下方按钮应忽略这次点击
				justDragged = true;

			} else {

				PointF p = content.camera.screenToCamera( (int) event.current.x, (int) event.current.y );
				ScrollPane.this.onClick( p.x, p.y );

			}
		}

		private boolean dragging = false;
		private PointF lastPos = new PointF();

		@Override
		protected void onDrag( PointerEvent event ) {
			if (dragging) {

				scroll(event.current);

			} else if (PointF.distance( event.current, event.start ) > dragThreshold) {

				dragging = true;
				lastPos.set( event.current );
				thumb.am = 1;

			}
		}

		private void scroll( PointF current ){

			Camera c = content.camera;

			c.shift( PointF.diff( lastPos, current ).invScale( c.zoom ) );
			if (c.scroll.x + width > content.width()) {
				c.scroll.x = content.width() - width;
			}
			if (c.scroll.x < 0) {
				c.scroll.x = 0;
			}
			if (c.scroll.y + height > content.height()) {
				c.scroll.y = content.height() - height;
			}
			if (c.scroll.y < 0) {
				c.scroll.y = 0;
			}

			thumb.y = y + height * c.scroll.y / content.height();

			lastPos.set( current );

		}

	}
}
