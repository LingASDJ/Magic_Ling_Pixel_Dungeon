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
import com.watabou.noosa.Game;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RenderedTextBlock extends Component {

	private int maxWidth = Integer.MAX_VALUE;
	public int nLines;

	private static final RenderedText SPACE = new RenderedText();
	private static final RenderedText NEWLINE = new RenderedText();

	protected String text;
	protected String[] tokens = null;
	protected ArrayList<RenderedText> words = new ArrayList<>();
	protected boolean multiline = false;

	private int size;
	private float zoom;
	private int color = -1;

	private int hightlightColor = Window.TITLE_COLOR;
	private int RedColor = Window.R_COLOR;
	private int GreenColor = Window.G_COLOR;
	private int BlueColor = Window.ORAGNECOLOR;
	private int PinkColor = Window.Pink_COLOR;
	private int DeepColor = Window.DeepPK_COLOR;
	private int BLACKColor = Window.CBLACK;

	private boolean highlightingEnabled = true;
	private boolean RedEnabled = true;
	private boolean GreenEnabled = true;
	private boolean BlueEnabled = true;
	private boolean PinkEnabled = true;
	private boolean DeepEnabled = true;
	private boolean BlackEnabled = true;

	public static final int LEFT_ALIGN = 1;
	public static final int CENTER_ALIGN = 2;
	public static final int RIGHT_ALIGN = 3;
	private int alignment = LEFT_ALIGN;

	private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^<#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})>$");
	private static final String COLOR_END_TAG = "<RGB>";

	//for manual text block splitting, a space between each word is assumed
	public void tokens(String... words){
		StringBuilder fullText = new StringBuilder();
		for (String word : words) {
			fullText.append(word);
		}
		text = fullText.toString();

		tokens = words;
		build();
	}

	public RenderedTextBlock(int size){
		this.size = size;
	}

	public RenderedTextBlock(String text, int size){
		this.size = size;
		text(text);
	}

	public void text(String text){
		this.text = text;

		if (text != null && !text.equals("")) {

			tokens = Game.platform.splitforTextBlock(text, multiline);

			build();
		}
	}

	public void text(String text, int maxWidth){
		this.maxWidth = maxWidth;
		multiline = true;
		text(text);
	}

	public String text(){
		return text;
	}

	public void maxWidth(int maxWidth){
		if (this.maxWidth != maxWidth){
			this.maxWidth = maxWidth;
			multiline = true;
			text(text);
		}
	}

	public int maxWidth(){
		return maxWidth;
	}

	private synchronized void build(){
		if (tokens == null) return;

		clear();

		tokens = processColorText( tokens );
		words = new ArrayList<>();
		boolean highlighting = false;
		boolean Redhighlighting = false;
		boolean Greenhighlighting = false;
		boolean Bluehighlighting = false;
		boolean Pinkhighlighting = false;
		boolean Deeppinkhighlighting = false;
		boolean Blackhighlighting = false;

		int currentCustomColor = -1;
		for (String str : tokens){
			if ( HEX_COLOR_PATTERN.matcher( str ).matches() ) {
				String hexColor = str.substring( 2, str.length() - 1 );
				//如果是RGB格式则进行转换
				if ( hexColor.length() == 3 ) {
					// 将 #RGB 转换为 #RRGGBB
					String r = hexColor.substring( 0, 1 );
					String g = hexColor.substring( 1, 2 );
					String b = hexColor.substring( 2, 3 );
					hexColor = r + r + g + g + b + b;
				}
				try {
					currentCustomColor = Integer.parseInt( hexColor, 16 );
				} catch (NumberFormatException e) {
					// 如果解析失败，忽略这个标记
				}
				continue;
			}

			// 检查颜色结束标记
			if ( str.equals( COLOR_END_TAG ) ) {
				currentCustomColor = -1;
				continue;
			}

			if (str.equals("_") && highlightingEnabled){
				highlighting = !highlighting;
			} else if(str.equals("{") && RedEnabled){
				Redhighlighting = !Redhighlighting;
			} else if(str.equals("}") && GreenEnabled){
				Greenhighlighting = !Greenhighlighting;
			} else if(str.equals("*") && BlueEnabled){
				Bluehighlighting = !Bluehighlighting;
			} else if(str.equals("[") && PinkEnabled){
				Pinkhighlighting = !Pinkhighlighting;
			} else if(str.equals("]") && DeepEnabled){
				Deeppinkhighlighting = !Deeppinkhighlighting;
			} else if(str.equals("|") && BlackEnabled){
				Blackhighlighting = !Blackhighlighting;
			} else if (str.equals("\n")){
				words.add(NEWLINE);
			} else if (str.equals(" ")) {
				words.add(SPACE);

			} else {
				RenderedText word = new RenderedText(str, size);

				if ( currentCustomColor != -1 ) word.hardlight(currentCustomColor);
				else if (highlighting) word.hardlight(hightlightColor);
				else if (color != -1) word.hardlight(color);
				else if (Redhighlighting) word.hardlight(RedColor);
				else if (Greenhighlighting) word.hardlight(GreenColor);
				else if (Bluehighlighting) word.hardlight(BlueColor);
				else if (Pinkhighlighting) word.hardlight(PinkColor);
				else if (Deeppinkhighlighting) word.hardlight(DeepColor);
				else if (Blackhighlighting) word.hardlight(BLACKColor);
				word.scale.set(zoom);

				words.add(word);
				add(word);

				if (height < word.height()) height = word.height();
			}
		}
		layout();
	}

	// 处理颜色开始和结束标记
	private synchronized String[] processColorText(String[] text){
        ArrayList<String> newList = new ArrayList<>();
		for ( String current : text ) {
			// 处理颜色开始标记 <#XXXXXX> / <#XXX>
			if ( current.contains( "<#" ) ) {
				int start = current.indexOf( "<#" );
				int end = current.indexOf( ">" );

				// 如果找不到结束符号，直接添加整个字符串
				if ( end == -1 || end < start ) {
					newList.add( current );
					continue;
				}

				if ( start > 0 ) {
					newList.add( current.substring( 0, start ) );
				}

				newList.add( current.substring( start, end + 1 ) );

				if ( end < current.length() - 1 ) {
					newList.add( current.substring(end + 1 ) );
				}

				continue;
			}

			// 处理颜色结束标记 <RGB>
			if (current.contains("<RGB>")) {
				int start = current.indexOf("<RGB>");
				int end = current.indexOf("B>");

				if (start > 0) {
					newList.add(current.substring(0, start));
				}

				newList.add(current.substring(start, end + 2));

				if (end + 2 < current.length()) {
					newList.add(current.substring(end + 2));
				}
				continue;
			}
			newList.add( current );
		}
		return newList.toArray( new String[ 0 ] );
	}

	public synchronized void zoom(float zoom){
		this.zoom = zoom;
		for (RenderedText word : words) {
			if (word != null) word.scale.set(zoom);
		}
		layout();
	}

	public synchronized void hardlight(int color){
		this.color = color;
		for (RenderedText word : words) {
			if (word != null) word.hardlight( color );
		}
	}

	public synchronized void resetColor(){
		this.color = -1;
		for (RenderedText word : words) {
			if (word != null) word.resetColor();
		}
	}

	public synchronized void alpha(float value){
		for (RenderedText word : words) {
			if (word != null) word.alpha( value );
		}
	}

	public synchronized void setHightlighting(boolean enabled){
		setHightlighting(enabled, Window.TITLE_COLOR);
	}

	public synchronized void setHightlighting(boolean enabled, int color){
		if (enabled != highlightingEnabled || color != hightlightColor) {
			hightlightColor = color;
			highlightingEnabled = enabled;
			build();
		}
	}

	public synchronized void RHightlighting(boolean enabled){
		setHightlighting(enabled, Window.ORAGNECOLOR);
	}

	public synchronized void RHightlighting(boolean enabled, int color){
		if (enabled != highlightingEnabled || color != hightlightColor) {
			RedColor = color;
			RedEnabled = enabled;
			build();
		}
	}

	public synchronized void GHightlighting(boolean enabled){
		setHightlighting(enabled, Window.G_COLOR);
	}

	public synchronized void GHightlighting(boolean enabled, int color){
		if (enabled != highlightingEnabled || color != hightlightColor) {
			RedColor = color;
			RedEnabled = enabled;
			build();
		}
	}

	public synchronized void BHightlighting(boolean enabled){
		setHightlighting(enabled, Window.G_COLOR);
	}

	public synchronized void BHightlighting(boolean enabled, int color){
		if (enabled != highlightingEnabled || color != hightlightColor) {
			RedColor = color;
			RedEnabled = enabled;
			build();
		}
	}

	public synchronized void invert(){
		if (words != null) {
			for (RenderedText word : words) {
				if (word != null) {
					word.ra = 0.77f;
					word.ga = 0.73f;
					word.ba = 0.62f;
					word.rm = -0.77f;
					word.gm = -0.73f;
					word.bm = -0.62f;
				}
			}
		}
	}

	public synchronized void align(int align){
		alignment = align;
		layout();
	}

	@Override
	protected synchronized void layout() {
		super.layout();
		float x = this.x;
		float y = this.y;
		float height = 0;
		nLines = 1;

		ArrayList<ArrayList<RenderedText>> lines = new ArrayList<>();
		ArrayList<RenderedText> curLine = new ArrayList<>();
		lines.add(curLine);

		width = 0;
		for (RenderedText word : words){
			if (word == SPACE){
				x += 1.5f;
			} else if (word == NEWLINE) {
				//newline
				y += height+2f;
				x = this.x;
				nLines++;
				curLine = new ArrayList<>();
				lines.add(curLine);
			} else {
				if (word.height() > height) height = word.height();

				if ((x - this.x) + word.width() > maxWidth && !curLine.isEmpty()){
					y += height+2f;
					x = this.x;
					nLines++;
					curLine = new ArrayList<>();
					lines.add(curLine);
				}

				word.x = x;
				word.y = y;
				PixelScene.align(word);
				x += word.width();
				curLine.add(word);

				if ((x - this.x) > width) width = (x - this.x);

				//TODO spacing currently doesn't factor in halfwidth and fullwidth characters
				//(e.g. Ideographic full stop)
				x -= 0.5f;

			}
		}
		this.height = (y - this.y) + height;

		if (alignment != LEFT_ALIGN){
			for (ArrayList<RenderedText> line : lines){
				if (line.size() == 0) continue;
				float lineWidth = line.get(line.size()-1).width() + line.get(line.size()-1).x - this.x;
				if (alignment == CENTER_ALIGN){
					for (RenderedText text : line){
						text.x += (width() - lineWidth)/2f;
						PixelScene.align(text);
					}
				} else if (alignment == RIGHT_ALIGN) {
					for (RenderedText text : line){
						text.x += width() - lineWidth;
						PixelScene.align(text);
					}
				}
			}
		}
	}

    protected void hardlight(float r, float g, float b) {
    }
}
