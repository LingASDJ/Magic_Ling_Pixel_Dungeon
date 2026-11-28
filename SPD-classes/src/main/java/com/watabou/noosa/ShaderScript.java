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

package com.watabou.noosa;

import com.watabou.glwrap.Uniform;
import com.watabou.utils.DeviceCompat;

import java.util.HashMap;

/**
 * Shader 底层基类。
 *
 * 在 {@link NoosaScript} 的标准管线之上做了三件事：
 * 1. 保留标准顶点属性 (aXYZW / aUV) 与标准 uniform (uCamera / uModel / uTex / uColorM / uColorA)，
 *    因此可以直接复用 NoosaScript 的 drawQuad / drawQuadSet / camera / lighting 等逻辑；
 * 2. 额外向片元着色器传递 vXY（当前顶点在 Visual 本地坐标下的像素位置），
 *    便于实现按位置变化的程序化效果（星空、火焰、扫描线等）；
 * 3. 自动生成桌面 GLSL 1.20 与 Web GLSL 3.00 ES 两套顶点/片元外壳，
 *    子类只需要提供与版本无关的 effectSource()。
 *
 * 子类的 effectSource() 必须定义如下函数（着色器内部已自动调用）：
 *
 * <pre>
 * vec4 textEffect(vec4 texel, vec2 uv, vec2 local)
 * </pre>
 *
 * texel 为采样到的原始纹理像素，uv 为纹理坐标，local 为本地像素坐标。
 * 返回值的 alpha 建议保留 texel.a，rgb 可自由改写。
 *
 * 注意：Script.use(Class) 通过无参构造创建实例，因此子类必须有无参构造器；
 * effectSource() 会在父类构造器执行期间被调用（用于编译着色器），
 * 所以它只能依赖静态常量，不能依赖子类的实例字段。
 */
public abstract class ShaderScript extends NoosaScript {

	private final HashMap<String, Uniform> customUniforms = new HashMap<>();

	public ShaderScript() {
		super();
	}

	/**
	 * 与 GLSL 版本无关的效果源码，必须定义
	 * {@code vec4 textEffect(vec4 texel, vec2 uv, vec2 local)}。
	 */
	protected abstract String effectSource();

	@Override
	protected String shader() {
		return vertexSource() + "//\n" + fragmentSource();
	}

	/**
	 * 取得并缓存一个自定义 uniform。
	 * 只能在本类的构造器（即 super() 执行完毕之后）或绘制阶段调用。
	 */
	protected final Uniform customUniform( String name ) {
		Uniform u = customUniforms.get( name );
		if (u == null) {
			u = uniform( name );
			customUniforms.put( name, u );
		}
		return u;
	}

	private static String vertexSource() {
		if (DeviceCompat.isWeb()) {
			return "#version 300 es\n" +
					"uniform mat4 uCamera;\n" +
					"uniform mat4 uModel;\n" +
					"in vec4 aXYZW;\n" +
					"in vec2 aUV;\n" +
					"out vec2 vUV;\n" +
					"out vec2 vXY;\n" +
					"void main() {\n" +
					"  gl_Position = uCamera * uModel * aXYZW;\n" +
					"  vUV = aUV;\n" +
					"  vXY = aXYZW.xy;\n" +
					"}\n";
		} else {
			return "uniform mat4 uCamera;\n" +
					"uniform mat4 uModel;\n" +
					"attribute vec4 aXYZW;\n" +
					"attribute vec2 aUV;\n" +
					"varying vec2 vUV;\n" +
					"varying vec2 vXY;\n" +
					"void main() {\n" +
					"  gl_Position = uCamera * uModel * aXYZW;\n" +
					"  vUV = aUV;\n" +
					"  vXY = aXYZW.xy;\n" +
					"}\n";
		}
	}

	private String fragmentSource() {
		String effect = effectSource();

		if (DeviceCompat.isWeb()) {
			return "#version 300 es\n" +
					"precision highp float;\n" +
					"in vec2 vUV;\n" +
					"in vec2 vXY;\n" +
					"uniform sampler2D uTex;\n" +
					"uniform vec4 uColorM;\n" +
					"uniform vec4 uColorA;\n" +
					"out vec4 fragColor;\n" +
					effect + "\n" +
					"void main() {\n" +
					"  fragColor = textEffect(texture(uTex, vUV), vUV, vXY) * uColorM + uColorA;\n" +
					"}\n";
		} else {
			return "#ifdef GL_ES\n" +
					"#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
					"precision highp float;\n" +
					"#else\n" +
					"precision mediump float;\n" +
					"#endif\n" +
					"#endif\n" +
					"varying vec2 vUV;\n" +
					"varying vec2 vXY;\n" +
					"uniform sampler2D uTex;\n" +
					"uniform vec4 uColorM;\n" +
					"uniform vec4 uColorA;\n" +
					effect + "\n" +
					"void main() {\n" +
					"  gl_FragColor = textEffect(texture2D(uTex, vUV), vUV, vXY) * uColorM + uColorA;\n" +
					"}\n";
		}
	}
}
