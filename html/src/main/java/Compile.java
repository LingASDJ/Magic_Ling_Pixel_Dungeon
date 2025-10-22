/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This file is derived from micronaut-libgdx-teavm (https://github.com/hollingsworthd/micronaut-libgdx-teavm),
 * originally licensed under the Apache License, Version 2.0.
 *
 * Modifications made by Konsthol on 13/4/25:
 * - Adjusted to compile Shattered Pixel Dungeon
 * Modifications made by Konsthol on 27/9/25:
 * - Adjusted to be used with gdx-teavm 1.2.4
 *
 * Copyright 2022 Daniel Hollingsworth
 */

package com.shatteredpixel.shatteredpixeldungeon.html;

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder;
import com.github.xpenatan.gdx.backends.teavm.gen.SkipClass;
import java.io.File;
import java.io.IOException;
import org.teavm.tooling.TeaVMTool;
import org.teavm.vm.TeaVMOptimizationLevel;

@SkipClass
public class Compile {

  public static void main(String[] args) throws IOException {
    deleteDir(new File("../release/webapp"));

    TeaBuildConfiguration conf = new TeaBuildConfiguration();

    conf.webappPath = new File("../release").getAbsolutePath();
    conf.assetsPath.add(new AssetFileHandle("../core/src/main/assets"));

    TeaVMTool tool = TeaBuilder.config(conf);
    tool.setMainClass(TeaVMLauncher.class.getName());
    tool.setOptimizationLevel(TeaVMOptimizationLevel.ADVANCED);
    tool.setObfuscated(true);
    tool.setShortFileNames(true);
    tool.setSourceFilesCopied(false);
    tool.setStrict(false);
    tool.setSourceMapsFileGenerated(false);
    tool.setDebugInformationGenerated(false);
    tool.setIncremental(false);

    TeaBuilder.build(tool);
  }

  private static void deleteDir(File dir) {
    File[] files = dir.listFiles();
    if (files != null) {
      for (File file : files) {
        deleteDir(file);
      }
    }
    dir.delete();
  }
}