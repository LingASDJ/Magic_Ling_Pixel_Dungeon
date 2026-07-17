package com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.RotGas;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;

public class DeadSoulTrap extends Trap {
    {
        color = TOMB;
        shape = CROSSHAIR;
    }

    @Override
    public void activate() {

        GameScene.add( Blob.seed( pos, 3500, RotGas.class ).setStrength(4));
        Sample.INSTANCE.play(Assets.Sounds.GAS);

    }
}
