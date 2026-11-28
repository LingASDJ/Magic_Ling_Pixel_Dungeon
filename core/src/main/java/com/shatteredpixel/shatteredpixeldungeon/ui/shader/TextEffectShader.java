package com.shatteredpixel.shatteredpixeldungeon.ui.shader;

import com.watabou.glscripts.Script;
import com.watabou.glwrap.Uniform;
import com.watabou.noosa.ShaderScript;

/**
 * 文本艺术字 shader。
 *
 * 一个 program 内通过 uEffect 分支支持三种效果，避免频繁切换 program：
 * 0 = NORMAL（原样输出，一般不会走到这里，NORMAL 直接使用默认管线）
 * 1 = GALAXY（星空：星云渐变 + 多层视差星点 + 缓慢流动）
 * 2 = FIRE（火焰：向上翻滚的噪声火焰 + 火星）
 *
 * 所有效果都只改写字形像素的 rgb，alpha 保留字形的 alpha，
 * 因此不会改变文字本身的排版、大小与命中区域。
 */
public class TextEffectShader extends ShaderScript {

	private final Uniform uEffect;
	private final Uniform uTime;
	private final Uniform uSeed;
	private final Uniform uLocalSize;
	private final Uniform uScale;
	private final Uniform uOffset;

	public TextEffectShader() {
		super();

		uEffect = customUniform( "uEffect" );
		uTime = customUniform( "uTime" );
		uSeed = customUniform( "uSeed" );
		uLocalSize = customUniform( "uLocalSize" );
		uScale = customUniform( "uScale" );
		uOffset = customUniform( "uOffset" );
	}

	public static TextEffectShader get() {
		return Script.use( TextEffectShader.class );
	}

	public void setEffect( int effect ) {
		uEffect.value1f( effect );
	}

	public void setTime( float time ) {
		uTime.value1f( time );
	}

	public void setSeed( float seed ) {
		uSeed.value1f( seed );
	}

	public void setLocalSize( float width, float height ) {
		uLocalSize.value2f( width, height );
	}

	public void setScale( float scale ) {
		uScale.value1f( scale );
	}

	/** 当前单词在整段文字中的本地坐标偏移，让整段文字共享同一片星空 */
	public void setOffset( float x, float y ) {
		uOffset.value2f( x, y );
	}

	@Override
	protected String effectSource() {
		return EFFECT_SOURCE;
	}

	private static final String EFFECT_SOURCE =
			"uniform float uEffect;\n" +
			"uniform float uTime;\n" +
			"uniform float uSeed;\n" +
			"uniform vec2 uLocalSize;\n" +
			"uniform float uScale;\n" +
			"uniform vec2 uOffset;\n" +
			"\n" +
			"float effectHash(vec2 p) {\n" +
			"  p = fract(p * vec2(123.34, 456.21));\n" +
			"  p += dot(p, p + 45.32);\n" +
			"  return fract(p.x * p.y);\n" +
			"}\n" +
			"\n" +
			"float effectNoise(vec2 p) {\n" +
			"  vec2 i = floor(p);\n" +
			"  vec2 f = fract(p);\n" +
			"  f = f * f * (3.0 - 2.0 * f);\n" +
			"  float a = effectHash(i);\n" +
			"  float b = effectHash(i + vec2(1.0, 0.0));\n" +
			"  float c = effectHash(i + vec2(0.0, 1.0));\n" +
			"  float d = effectHash(i + vec2(1.0, 1.0));\n" +
			"  return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);\n" +
			"}\n" +
			"\n" +
			"float effectStar(vec2 p, float density, float seed) {\n" +
			"  vec2 cell = floor(p);\n" +
			"  vec2 f = fract(p) - 0.5;\n" +
			"  float h = effectHash(cell + seed);\n" +
			"  vec2 o = vec2(fract(h * 13.73), fract(h * 7.31)) - 0.5;\n" +
			"  float d = length(f - o * 0.62);\n" +
			"  float on = step(1.0 - density, h);\n" +
			"  float twinkle = 0.55 + 0.45 * sin(uTime * (1.6 + h * 2.4) + h * 6.2831);\n" +
			"  float core = 1.0 - smoothstep(0.0, 0.24, d);\n" +
			"  float halo = 1.0 - smoothstep(0.0, 0.42, d);\n" +
			"  return (core + halo * 0.30) * on * twinkle;\n" +
			"}\n" +
			"\n" +
			"vec3 effectGalaxy(vec2 local) {\n" +
			"  float h = max(1.0, uLocalSize.y);\n" +
			"  vec2 p = ((local + uOffset) * uScale + vec2(uSeed * 37.1, uSeed * 19.7)) / h * 4.0;\n" +
			"  p += vec2(uTime * 0.05, -uTime * 0.035);\n" +
			"  float t = clamp(local.y * uScale / h, 0.0, 1.0);\n" +
			"\n" +
			"  // keep the whole glyph bright, only shift the hue\n" +
			"  vec3 col = mix(vec3(1.0, 0.90, 1.0), vec3(0.80, 0.76, 1.0), smoothstep(0.0, 0.55, t));\n" +
			"  col = mix(col, vec3(0.62, 0.84, 1.0), smoothstep(0.55, 1.0, t));\n" +
			"\n" +
			"  // very light nebula, never eat the glyph\n" +
			"  float nebula = effectNoise(p * 0.65 + vec2(uTime * 0.02, 0.0));\n" +
			"  col += vec3(0.04, 0.03, 0.07) * nebula;\n" +
			"\n" +
			"  // white stars are highlights only\n" +
			"  float stars = effectStar(p, 0.14, 0.0)\n" +
			"              + effectStar(p * 1.9, 0.18, 3.1);\n" +
			"  col += stars * vec3(1.0, 1.0, 1.0);\n" +
			"\n" +
			"  return col;\n" +
			"}\n" +
			"\n" +
			"vec3 effectFire(vec2 local) {\n" +
			"  float h = max(1.0, uLocalSize.y);\n" +
			"  vec2 p = ((local + uOffset) * uScale + vec2(uSeed * 41.3, uSeed * 23.7)) / h * 4.0;\n" +
			"  float t = clamp(local.y * uScale / h, 0.0, 1.0);\n" +
			"\n" +
			"  float n1 = effectNoise(p + vec2(0.0, -uTime * 0.85));\n" +
			"  float n2 = effectNoise(p * 2.3 + vec2(uTime * 0.35, -uTime * 1.7));\n" +
			"  float flame = n1 * 0.6 + n2 * 0.4;\n" +
			"  float heat = clamp(flame * (0.70 + 0.45 * t), 0.0, 1.0);\n" +
			"\n" +
			"  vec3 col = mix(vec3(1.0, 0.52, 0.10), vec3(1.0, 0.72, 0.22), smoothstep(0.10, 0.60, heat));\n" +
			"  col = mix(col, vec3(1.0, 0.97, 0.78), smoothstep(0.70, 1.0, heat));\n" +
			"\n" +
			"  float ember = step(0.95, effectNoise(p * 3.1 + vec2(0.0, -uTime * 1.2)));\n" +
			"  col += ember * vec3(0.20, 0.08, 0.01);\n" +
			"\n" +
			"  return col;\n" +
			"}\n" +
			"\n" +
			"vec4 textEffect(vec4 texel, vec2 uv, vec2 local) {\n" +
			"  vec3 rgb = texel.rgb;\n" +
			"  // font texture is white glyph + black border; recolor only the white glyph\n" +
			"  // and keep the black border untouched so the glyph stays readable\n" +
			"  float glyph = clamp(max(texel.r, max(texel.g, texel.b)), 0.0, 1.0);\n" +
			"  if (uEffect > 1.5) {\n" +
			"    rgb = mix(texel.rgb, effectFire(local), glyph);\n" +
			"  } else if (uEffect > 0.5) {\n" +
			"    rgb = mix(texel.rgb, effectGalaxy(local), glyph);\n" +
			"  }\n" +
			"  return vec4(rgb, texel.a);\n" +
			"}\n";
}
