#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_time;
varying vec2 v_texCoords;

void main() {
    vec2 uv = v_texCoords;
    uv.y += sin(uv.x*100.0 + u_time * 0.5) * 0.01;

    gl_FragColor = texture2D(u_texture, uv);
}
