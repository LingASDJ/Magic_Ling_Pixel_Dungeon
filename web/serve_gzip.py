# -*- coding: utf-8 -*-
"""
Magic Ling Pixel Dungeon — 本地 Web 伺服（带 gzip 压缩）

用法：
    python web/serve_gzip.py [端口，默认 8080]

作用：
    把 web/build/dist/webapp 静态伺服到 http://127.0.0.1:<port>/，
    并对文本类资源（app.js、fnt、json、atlas、txt、html 等）实时 gzip 压缩，
    浏览器无需改动即可享受 ~10 倍体积削减（app.js 66MB → 传输 6.2MB）。

正式部署时请在 nginx / caddy / IIS 上开启 gzip 或 brotli，效果相同。
"""
import gzip
import io
import os
import sys
from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "build", "dist", "webapp")

# 这些类型 gzip 后收益显著；已压缩的格式（音频/位图）不再压缩
GZIP_TYPES = {
    ".js", ".json", ".txt", ".html", ".css", ".svg", ".xml",
    ".fnt", ".atlas", ".mf", ".properties", ".dat",
}

class GzipHandler(SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=ROOT, **kwargs)

    def end_headers(self):
        # 允许跨域，便于后续调试/外链
        self.send_header("Access-Control-Allow-Origin", "*")
        super().end_headers()

    def send_head(self):
        path = self.translate_path(self.path)
        if not (os.path.isfile(path) and path.endswith(tuple(GZIP_TYPES))):
            return super().send_head()
        try:
            with open(path, "rb") as f:
                raw = f.read()
        except OSError:
            return super().send_head()

        gz = gzip.compress(raw, compresslevel=9)
        if len(gz) >= len(raw):  # 压缩无收益则原样返回
            return super().send_head()

        self.send_response(200)
        self.send_header("Content-Type", self.guess_type(path))
        self.send_header("Content-Encoding", "gzip")
        self.send_header("Content-Length", str(len(gz)))
        self.send_header("Vary", "Accept-Encoding")
        self.send_header("Cache-Control", "no-cache")
        self.end_headers()
        return io.BytesIO(gz)

    def log_message(self, fmt, *args):
        pass  # 保持终端安静


if __name__ == "__main__":
    port = int(sys.argv[1]) if len(sys.argv) > 1 else 8080
    server = ThreadingHTTPServer(("127.0.0.1", port), GzipHandler)
    print(f"Serving {ROOT} with gzip at http://127.0.0.1:{port}/")
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass
