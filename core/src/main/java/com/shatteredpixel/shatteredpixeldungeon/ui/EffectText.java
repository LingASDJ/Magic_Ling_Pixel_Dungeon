package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.ui.shader.TextEffectShader;
import com.watabou.noosa.Game;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.RenderedText;

import java.util.Random;

/**
 * 带 shader 艺术字效果的文本单词。
 *
 * 默认 NORMAL：直接走原版 NoosaScript 管线，行为、性能与普通 RenderedText 完全一致。
 * GALAXY / FIRE：使用 TextEffectShader 绘制，并按帧推进时间实现动画。
 *
 * 每个单词都会获得一个随机 seed，避免逐字排列的文字出现重复的星空图案。
 */
public class EffectText extends RenderedText {

	// 使用独立的随机源，避免影响游戏本体的随机数序列
	private static final Random SEED_RANDOM = new Random();

	private TextEffect effect;

	private float time;
	private float seed;

	public EffectText( String text, int size, TextEffect effect ) {
		this( text, size, effect, SEED_RANDOM.nextFloat() * 100f );
	}

	public EffectText( String text, int size, TextEffect effect, float seed ) {
		super( text, size );

		this.effect = effect == null ? TextEffect.NORMAL : effect;
		this.seed = seed;
	}

	public TextEffect effect() {
		return effect;
	}

	public void setEffect( TextEffect effect ) {
		this.effect = effect == null ? TextEffect.NORMAL : effect;
	}

	public void setSeed( float seed ) {
		this.seed = seed;
	}

	@Override
	protected NoosaScript script() {
		if (effect == TextEffect.NORMAL) {
			return NoosaScript.get();
		}
		return TextEffectShader.get();
	}

	@Override
	protected void prepareShader( NoosaScript script ) {
		if (script instanceof TextEffectShader) {
			TextEffectShader shader = (TextEffectShader) script;
			shader.setEffect( effect.shaderId() );
			shader.setTime( time );
			shader.setSeed( seed );
			shader.setLocalSize( width(), height() );
			shader.setScale( scale.y );

			// 把当前单词的位置换算回未缩放的本地坐标，
			// 让同一段文字里的多个单词共享一片连续的星空/火焰。
			float sx = scale.x != 0 ? scale.x : 1f;
			float sy = scale.y != 0 ? scale.y : 1f;
			shader.setOffset( x / sx, y / sy );
		}
	}

	@Override
	public void update() {
		super.update();

		if (effect != TextEffect.NORMAL) {
			time += Game.elapsed;
			if (time > 1000f) {
				time -= 1000f;
			}
		}
	}
}
