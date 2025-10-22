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

import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder;

import org.teavm.tooling.TeaVMTool;
import org.teavm.vm.TeaVMOptimizationLevel;

import java.io.File;
import java.io.IOException;

public class Compile {

  public static void main(String[] args) throws IOException {
    File webappDir = new File("../release/webapp");
    com.shatteredpixel.shatteredpixeldungeon.html.Configure.deleteDir(webappDir);
    com.shatteredpixel.shatteredpixeldungeon.html.Configure.configure();

    TeaVMTool tool = new TeaVMTool();
    tool.setMainClass(com.shatteredpixel.shatteredpixeldungeon.html.TeaVMLauncher.class.getName());
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

}
