/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

public class WndTitledMessage extends Window {

	protected static int maxHeight() {
		return (int) (PixelScene.uiCamera.height * 0.9);
	}

	protected static final int WIDTH_MIN = 120;
	protected static final int WIDTH_MAX = 220;
	protected static final int GAP = 2;

	public WndTitledMessage(Image icon, String title, String message) {

		this(icon, title, message, WIDTH_MAX);

	}

	public WndTitledMessage(Image icon, String title, String message, int maxWidth) {

		this(new IconTitle(icon, title), message, maxWidth);

	}

	ScrollPane sp;

	public WndTitledMessage(Component titlebar, String message) {
		this(titlebar, message, WIDTH_MAX);
	}

	public WndTitledMessage(Component titlebar, String message, int maxWidth) {

		super();

		int width = WIDTH_MIN;

		titlebar.setRect(0, 0, width, 0);
		add(titlebar);

		RenderedTextBlock text = PixelScene.renderTextBlock(6);
		text.text(message, width);
		text.setPos(titlebar.left(), titlebar.bottom() + 2 * GAP);

		while (PixelScene.landscape()
				&& text.bottom() > (PixelScene.MIN_HEIGHT_L - 10)
				&& width < maxWidth) {
			width += 20;
			titlebar.setRect(0, 0, width, 0);
			text.setPos( titlebar.left(), titlebar.bottom() + 2*GAP );
			text.maxWidth(width);

			titlebar.setWidth(width);
			text.setPos(titlebar.left(), titlebar.bottom() + 2 * GAP);
		}
		Component comp = new Component();
		comp.add(text);
		text.setPos(0, GAP);
		comp.setSize(text.width(), text.height() + GAP * 2);
		resize(width, (int) Math.min((int) comp.bottom() + 2 + titlebar.height() + GAP, maxHeight()));

		add(sp = new ScrollPane(comp));
		sp.setRect(titlebar.left(), titlebar.bottom() + GAP, comp.width(), Math.min((int) comp.bottom() + 2, maxHeight() - titlebar.bottom() - GAP));

		bringToFront(titlebar);
	}

	@Override
	public void offset(int xOffset, int yOffset) {
		super.offset(xOffset, yOffset);
		// refresh the scrollbar pane
		sp.setPos(sp.left(), sp.top());
	}

	// adds to the bottom of a titled message, below the message itself.
	// this only works ONCE currently.
	public final void addToBottom(Component c) {
		addToBottom(c, GAP);
	}

	public final void addToBottom(Component c, int gap) {
		addToBottom(c, gap, 0);
	}

	public void addToBottom(Component c, int gapBefore, int gapAfter) {
		// attempt to place normally.
		c.setRect(0, height + gapBefore, width, c.height() + gapAfter); // assumes there is space at the bottom first. note that I'm baking in the bottom spacing into the component itself
		add(c);
		// in order to make things fit, we need to change the size of the scrollbar to make the component fit properly.
		// fixme even though it *feels* like I should be able to stop here if everything fits, I can't. why?
		setHeight((int) Math.min(c.bottom(), maxHeight()));
		float y; c.setY(y = height - c.height()); // put the component into its final position.
		// scrollbar height is reduced to respect top spacing
		sp.setRect(0, sp.top(), width, y - sp.top() - gapBefore);
	}

	// fixme this wrapper component is very unable to moved after it's placed. If I need to move it again, I won't be able to.
	// should be able to simulate the previous behavior...
	public Component addToBottom(int gapBefore, int gapAfter, Component... components) {
		// this ensures that things are formatted correctly vertically.
		Component wrapper = new Component();
		if(components.length == 0) return wrapper;
		float top=Float.MAX_VALUE, bottom=Float.MIN_VALUE;
		for(Component c : components) {
			top = Math.min(top, c.top());
			bottom = Math.max(bottom, c.bottom());
			wrapper.add(c);
		}
		wrapper.setRect(0, top, width, bottom-top);
		addToBottom(wrapper, gapBefore, gapAfter);

		top -= wrapper.top();

		for(Component c : components) c.setY(c.top() - top);

		return wrapper;
	}
	// yes the order is different. deal with it.
	public Component addToBottom(int gap, Component... components) { return addToBottom(gap, 0, components); }
	public Component addToBottom(Component... components) { return addToBottom(GAP, components); }
}