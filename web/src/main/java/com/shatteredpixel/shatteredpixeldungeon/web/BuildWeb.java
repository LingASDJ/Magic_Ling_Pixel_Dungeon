/*
 * Magic Ling Pixel Dungeon
 * Web (TeaVM) build entry point.
 *
 * Compiles the game to JavaScript/WebAssembly with TeaVM via the gdx-teavm
 * backend, then applies the same output patches the reference web port uses
 * (constructor aliases for the TeaVM simple-constructor table, a pagehide
 * listener guard, and a data: favicon).
 */

package com.shatteredpixel.shatteredpixeldungeon.web;

import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFileHandle;
import com.github.xpenatan.gdx.teavm.backends.shared.config.compiler.TeaCompiler;
import com.github.xpenatan.gdx.teavm.backends.shared.config.reflection.DefaultReflectionListener;
import com.github.xpenatan.gdx.teavm.backends.web.config.backend.WebBackend;

import org.teavm.vm.TeaVMOptimizationLevel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class BuildWeb {

	private static final File OUTPUT_DIR = new File("web/build/dist");

	public static void main(String[] args) {
		boolean runAfterBuild = args.length > 0 && "run".equals(args[0]);

		WebBackend backend = new WebBackend()
				.setHtmlTitle("Magic Ling Pixel Dungeon")
				.setHtmlWidth(1280)
				.setHtmlHeight(720)
				.setJettyPort(8080)
				.setStartJettyAfterBuild(runAfterBuild);

		new TeaCompiler(backend)
				.addAssets(new AssetFileHandle("core/src/main/assets"))
				.addAssets(new AssetFileHandle("desktop/src/main/assets"))
				.setMainClass(WebLauncher.class.getName())
				.setOutputName("app")
				.setOptimizationLevel(TeaVMOptimizationLevel.ADVANCED)
				// 实测：gdx-teavm 1.5.5 + TeaVM 0.14 下 setObfuscated(true) 对 JS 输出
				// 无实际效果（输出字节几乎不变），故保持 false 以便输出可读、便于排错。
				// 体积优化靠传输层 gzip：app.js 66MB -> 传输 6.2MB（见 web/serve_gzip.py）。
				.setObfuscated(false)
				.setMinHeapSize(64 * 1024 * 1024)
				.setMaxHeapSize(512 * 1024 * 1024)
				.setMinDirectBuffersSize(64 * 1024 * 1024)
				.setReflectionListener(buildReflectionList())
				.build(OUTPUT_DIR);

		patchTeaVmOutput();
		disableMissingFaviconRequest();
	}

	private static DefaultReflectionListener buildReflectionList() {
		DefaultReflectionListener listener = new DefaultReflectionListener();
		for (String className : collectGameClassNames()) {
			if (hasReflectableNoArgConstructor(className)) {
				listener.addClassOrPackage(className);
			}
		}
		return listener;
	}

	private static List<String> collectGameClassNames() {
		ArrayList<String> result = new ArrayList<>();
		String[] entries = System.getProperty("java.class.path", "").split(File.pathSeparator);
		for (String entry : entries) {
			File file = new File(entry);
			if (file.isDirectory()) {
				collectFromDirectory(file.toPath(), result);
			} else if (file.isFile() && file.getName().endsWith(".jar")) {
				collectFromJar(file, result);
			}
		}
		return result;
	}

	private static void collectFromDirectory(Path root, List<String> result) {
		try {
			Files.walk(root)
					.filter(path -> path.toString().endsWith(".class"))
					.map(root::relativize)
					.map(Path::toString)
					.map(BuildWeb::classFileToName)
					.filter(BuildWeb::isGameClass)
					.forEach(result::add);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void collectFromJar(File file, List<String> result) {
		try (JarFile jar = new JarFile(file)) {
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				if (!entry.isDirectory() && name.endsWith(".class")) {
					String className = classFileToName(name);
					if (isGameClass(className)) {
						result.add(className);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String classFileToName(String classFile) {
		return classFile
				.replace(File.separatorChar, '.')
				.replace('/', '.')
				.substring(0, classFile.length() - ".class".length());
	}

	private static boolean isGameClass(String className) {
		return className.startsWith("com.shatteredpixel.shatteredpixeldungeon.")
				|| className.startsWith("com.watabou.");
	}

	private static boolean hasReflectableNoArgConstructor(String className) {
		try {
			Class<?> type = Class.forName(className, false, BuildWeb.class.getClassLoader());
			int modifiers = type.getModifiers();
			if (type.isInterface() || type.isEnum() || type.isAnnotation() || Modifier.isAbstract(modifiers)) {
				return false;
			}
			Constructor<?> constructor = type.getDeclaredConstructor();
			int constructorModifiers = constructor.getModifiers();
			return Modifier.isPublic(constructorModifiers) || Modifier.isProtected(constructorModifiers)
					|| !Modifier.isPrivate(constructorModifiers);
		} catch (ClassNotFoundException | NoClassDefFoundError | NoSuchMethodException e) {
			return false;
		}
	}

	private static void patchTeaVmOutput() {
		File appJs = new File(OUTPUT_DIR, "webapp/app.js");
		try {
			String source = Files.readString(appJs.toPath(), StandardCharsets.UTF_8);
			String original = source;

			Map<String, String> simpleConstructors = readSimpleConstructors(source);
			HashMap<String, String> resolvedConstructors = new HashMap<>();
			LinkedHashMap<String, String> aliases = new LinkedHashMap<>();
			for (Map.Entry<String, String> entry : simpleConstructors.entrySet()) {
				String classSymbol = entry.getKey();
				String constructorSymbol = entry.getValue();
				if (!isConstructorDefined(source, constructorSymbol)) {
					aliases.put(constructorSymbol, resolveConstructorTarget(
							classSymbol, source, simpleConstructors, resolvedConstructors, new HashSet<>()));
				}
			}

			if (!aliases.isEmpty()) {
				String marker = "$rt_simpleConstructors([";
				int markerIndex = source.indexOf(marker);
				if (markerIndex < 0) {
					throw new IllegalStateException("Unable to find TeaVM simple constructor table");
				}
				source = source.substring(0, markerIndex) + buildConstructorAliases(aliases)
						+ source.substring(markerIndex);
			}

			// backend-web clears its listener on pagehide, then visibilitychange
			// tries to pause it again. Guard that generated second callback.
			source = source.replaceAll(
					"(var\\$1 = var\\$1\\.\\$this\\$\\d+\\.\\$curListener;\\n\\s+)(\\$ptr = 7;)",
					"$1if (var\\$1 === null) return;\n        $2");

			if (!source.equals(original)) {
				Files.writeString(appJs.toPath(), source, StandardCharsets.UTF_8);
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to patch TeaVM output", e);
		}
	}

	private static Map<String, String> readSimpleConstructors(String source) {
		String marker = "$rt_simpleConstructors([";
		int start = source.indexOf(marker);
		if (start < 0) {
			throw new IllegalStateException("Unable to find TeaVM simple constructor table");
		}
		start += marker.length();
		int end = source.indexOf("]);", start);
		if (end < 0) {
			throw new IllegalStateException("Unable to find end of TeaVM simple constructor table");
		}

		LinkedHashMap<String, String> constructors = new LinkedHashMap<>();
		String[] tokens = source.substring(start, end).split(",");
		for (int i = 0; i + 1 < tokens.length; i += 2) {
			constructors.put(tokens[i].trim(), tokens[i + 1].trim());
		}
		return constructors;
	}

	private static boolean isConstructorDefined(String source, String constructorSymbol) {
		return source.contains(constructorSymbol + " =");
	}

	private static String resolveConstructorTarget(String classSymbol, String source,
			Map<String, String> simpleConstructors, Map<String, String> resolvedConstructors,
			Set<String> visiting) {
		if (classSymbol == null || classSymbol.isEmpty() || "jl_Object".equals(classSymbol)) {
			return "jl_Object__init_0";
		}
		String memoized = resolvedConstructors.get(classSymbol);
		if (memoized != null) {
			return memoized;
		}

		String listedConstructor = simpleConstructors.get(classSymbol);
		if (listedConstructor != null && isConstructorDefined(source, listedConstructor)) {
			resolvedConstructors.put(classSymbol, listedConstructor);
			return listedConstructor;
		}

		String conventionalConstructor = classSymbol + "__init_";
		if (isConstructorDefined(source, conventionalConstructor)) {
			resolvedConstructors.put(classSymbol, conventionalConstructor);
			return conventionalConstructor;
		}

		if (!visiting.add(classSymbol)) {
			return "jl_Object__init_0";
		}
		String parentSymbol = findParentSymbol(source, classSymbol);
		String target = resolveConstructorTarget(parentSymbol, source, simpleConstructors, resolvedConstructors, visiting);
		visiting.remove(classSymbol);
		resolvedConstructors.put(classSymbol, target);
		return target;
	}

	private static String findParentSymbol(String source, String classSymbol) {
		int start = source.indexOf(classSymbol + ", \"");
		if (start < 0) {
			return "jl_Object";
		}
		int end = source.indexOf('\n', start);
		if (end < 0) {
			end = source.length();
		}
		String[] fields = source.substring(start, end).split(",", 5);
		if (fields.length < 4) {
			return "jl_Object";
		}
		return fields[3].trim();
	}

	private static String buildConstructorAliases(Map<String, String> aliases) {
		StringBuilder builder = new StringBuilder();
		builder.append("let ");
		boolean first = true;
		for (Map.Entry<String, String> alias : aliases.entrySet()) {
			if (!first) {
				builder.append(",\n");
			}
			builder.append(alias.getKey()).append(" = ").append(alias.getValue());
			first = false;
		}
		builder.append(";\n");
		return builder.toString();
	}

	private static void disableMissingFaviconRequest() {
		File index = new File(OUTPUT_DIR, "webapp/index.html");
		try {
			String source = Files.readString(index.toPath(), StandardCharsets.UTF_8);
			if (!source.contains("rel=\"icon\"")) {
				source = source.replace("</head>", "<link rel=\"icon\" href=\"data:,\">\n</head>");
				Files.writeString(index.toPath(), source, StandardCharsets.UTF_8);
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to patch Web favicon", e);
		}
	}
}
