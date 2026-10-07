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
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Signal;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class GameLog extends Component implements Signal.Listener<String> {

	private static final int MAX_LINES = 3;

	private static final Pattern PUNCTUATION = Pattern.compile( ".*[.,;?! ]$" );

	private RenderedTextBlock lastEntry;
	private Entry lastFoldEntry;
	private int lastColor;

	private static ArrayList<Entry> entries = new ArrayList<>();

	public GameLog() {
		super();
		GLog.update.replace( this );

		recreateLines();
	}

	private static ArrayList<String> textsToAdd = new ArrayList<>();

	@Override
	public synchronized void update() {
		ArrayList<String> toAdd = new ArrayList<>(textsToAdd);
		textsToAdd.clear();

		for (String text : toAdd) {
			if (length != entries.size()) {
				clear();
				recreateLines();
			}

			if (text.equals( GLog.NEW_LINE )) {
				lastEntry = null;
				continue;
			}

			int color = CharSprite.DEFAULT;
			if (text.startsWith( GLog.POSITIVE )) {
				text = text.substring( GLog.POSITIVE.length() );
				color = CharSprite.POSITIVE;
			} else
			if (text.startsWith( GLog.BLUETEXT )) {
				text = text.substring( GLog.BLUETEXT.length() );
				color = CharSprite.BLUETEXT;
			}
			else
			if (text.startsWith( GLog.YELLOWTEXT)) {
				text = text.substring( GLog.YELLOWTEXT.length() );
				color = Window.CYELLOW;
			} else
			if (text.startsWith( GLog.PINKTEXT )) {
				text = text.substring( GLog.PINKTEXT.length() );
				color = CharSprite.PINKTEXT;
			} else
			if (text.startsWith( GLog.NEGATIVE )) {
				text = text.substring( GLog.NEGATIVE.length() );
				color = CharSprite.NEGATIVE;
			} else
			if (text.startsWith( GLog.WARNING )) {
				text = text.substring( GLog.WARNING.length() );
				color = CharSprite.WARNING;
			} else
			if (text.startsWith( GLog.HIGHLIGHT )) {
				text = text.substring( GLog.HIGHLIGHT.length() );
				color = CharSprite.NEUTRAL;
			}

			// 将相同消息进行折叠而不是并列展示，不同消息再并列展示
			// 折叠判断使用 lastFoldEntry，不受 NEW_LINE 影响
			boolean canFold =
					lastFoldEntry != null
							&& color == lastFoldEntry.color
							&& lastFoldEntry.isLastPart(text);

			boolean canAppend = lastEntry != null
					&& color == lastColor
					&& lastFoldEntry != null
					&& lastEntry.nLines < MAX_LINES;

			if (canFold || canAppend) {
				lastFoldEntry.addOrFold(text);
				RenderedTextBlock target = (lastFoldEntry.rendered != null) ? lastFoldEntry.rendered : lastEntry;
				if (target != null) {
					target.text(lastFoldEntry.displayText());
				}
			} else {
				Entry entry = new Entry(text, color);
				entries.add(entry);

				lastFoldEntry = entry;
				lastEntry = PixelScene.renderTextBlock( entry.displayText(), 6 );
				entry.rendered = lastEntry;
				lastEntry.setHightlighting( false );
				lastEntry.hardlight( color );
				lastColor = color;
				add( lastEntry );
			}

			if (length > 0) {
				int nLines;
				do {
					nLines = 0;
					for (int i = 0; i < length-1; i++) {
						nLines += ((RenderedTextBlock) members.get(i)).nLines;
					}

					if (nLines > MAX_LINES) {
						RenderedTextBlock r = ((RenderedTextBlock) members.get(0));
						remove(r);
						r.destroy();

						Entry removed = entries.remove(0);

						if (lastFoldEntry == removed) {
							lastFoldEntry = entries.isEmpty()
									? null
									: entries.get(entries.size() - 1);
						}
					}
				} while (nLines > MAX_LINES);
				if (entries.isEmpty()) {
					lastEntry = null;
				}
			}
		}

		if (!toAdd.isEmpty()) {
			layout();
		}
		super.update();
	}

	private synchronized void recreateLines() {
		// 每个 Entry 重新绑定自己的显示块
		// lastFoldEntry 始终指向最后一个逻辑 Entry
		lastEntry = null;
		lastFoldEntry = null;

		for (Entry entry : entries) {
			entry.rendered = PixelScene.renderTextBlock( entry.displayText(), 6 );
			lastColor = entry.color;
			entry.rendered.hardlight( lastColor );
			add( entry.rendered );
			lastEntry = entry.rendered;
			lastFoldEntry = entry;
		}
	}

	public synchronized void newLine() {
		lastEntry = null;
	}

	@Override
	public synchronized boolean onSignal( String text ) {
		textsToAdd.add(text);
		return false;
	}

	@Override
	protected void layout() {
		float pos = y;
		for (int i=length-1; i >= 0; i--) {
			RenderedTextBlock entry = (RenderedTextBlock)members.get( i );
			entry.maxWidth((int)width);
			entry.setPos(x, pos-entry.height());
			pos -= entry.height()+2;
		}
	}

	@Override
	public void destroy() {
		GLog.update.remove( this );
		super.destroy();
	}

	private static class Entry {
		public String text;
		public int color;
		public int count = 1;
		public RenderedTextBlock rendered;

		public ArrayList<String> extraTexts = new ArrayList<>();
		public ArrayList<Integer> extraCounts = new ArrayList<>();

		public Entry( String text, int color ) {
			this.text = text;
			this.color = color;
		}

		private String getLastPart() {
			if (extraTexts.isEmpty()) {
				return text;
			} else {
				return extraTexts.get(extraTexts.size() - 1);
			}
		}

		public boolean isLastPart(String nextText) {
			return getLastPart().equals(nextText);
		}

		public void addOrFold(String nextText) {
			if (getLastPart().equals(nextText)) {
				if (extraTexts.isEmpty()) {
					count++;
				} else {
					int lastIndex = extraCounts.size() - 1;
					extraCounts.set(lastIndex, extraCounts.get(lastIndex) + 1);
				}
			} else {
				extraTexts.add(nextText);
				extraCounts.add(1);
			}
		}

		private String formatPart(String text, int count) {
			if (count > 1) {
				return text + "(" + count + ")";
			} else {
				return text;
			}
		}

		public String displayText() {
			StringBuilder result = new StringBuilder(
					formatPart(text, count));

			for (int i = 0; i < extraTexts.size(); i++) {
				result.append(" ");
				result.append(formatPart(
						extraTexts.get(i),
						extraCounts.get(i)
				));
			}

			return result.toString();
		}
	}

	public static void wipe() {
		entries.clear();
	}
}