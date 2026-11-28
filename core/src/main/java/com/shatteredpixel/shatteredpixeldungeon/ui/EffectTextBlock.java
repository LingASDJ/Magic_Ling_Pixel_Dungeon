package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.watabou.noosa.RenderedText;

import java.util.Random;

/**
 * 支持按枚举切换 shader 艺术字效果的 RenderedTextBlock。
 *
 * 默认 TextEffect.NORMAL，即与普通文本完全相同；
 * 需要星空 / 火焰效果时显式调用 setEffect。
 *
 * 同一个文本块内的所有单词共享一个 seed，并携带各自的位置偏移，
 * 因此整段文字会落在同一片连续的星空/火焰上，而不是每个字各画一份。
 */
public class EffectTextBlock extends RenderedTextBlock {

	private static final Random SEED_RANDOM = new Random();

	private final float seed = SEED_RANDOM.nextFloat() * 100f;
	private TextEffect effect = TextEffect.NORMAL;

	public EffectTextBlock( int size ) {
		super( size );
	}

	public EffectTextBlock( String text, int size ) {
		super( text, size );
		syncWords();
	}

	public EffectTextBlock( String text, int size, TextEffect effect ) {
		super( text, size );

		this.effect = effect == null ? TextEffect.NORMAL : effect;
		syncWords();
	}

	public TextEffect effect() {
		return effect;
	}

	public void setEffect( TextEffect effect ) {
		this.effect = effect == null ? TextEffect.NORMAL : effect;
		syncWords();
	}

	private void syncWords() {
		for (RenderedText word : words) {
			if (word instanceof EffectText) {
				EffectText text = (EffectText) word;
				text.setEffect( effect );
				text.setSeed( seed );
			}
		}
	}

	@Override
	protected RenderedText newWord( String text, int size ) {
		return new EffectText( text, size, effect, seed );
	}
}
