package com.shatteredpixel.shatteredpixeldungeon.ui;

/**
 * 文本 shader 效果类型。
 *
 * NORMAL 走原版渲染管线（无额外开销，表现和普通文字完全一致）；
 * GALAXY / FIRE 使用 {@link com.shatteredpixel.shatteredpixeldungeon.ui.shader.TextEffectShader} 绘制。
 *
 * 默认使用 NORMAL，需要艺术字时由调用方显式切换（见 EffectTextBlock#setEffect）。
 */
public enum TextEffect {
	NORMAL,
	GALAXY,
	FIRE;

	/** 传给着色器的编号，与 {@link #ordinal()} 保持一致 */
	public int shaderId() {
		return ordinal();
	}
}
