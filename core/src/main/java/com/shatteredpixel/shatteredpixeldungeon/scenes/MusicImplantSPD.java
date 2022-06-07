package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PlatformSupport;

/*
https://github.com/CatAzreal/Jinkeloid-MISPD/
 */
public class MusicImplantSPD extends Game {

    //variable constants for specific older versions of shattered, used for data conversion
    //versions older than v0.7.5e are no longer supported, and data from them is ignored

    public MusicImplantSPD(PlatformSupport platform ) {
        super( sceneClass == null ? TitleScene.class : sceneClass, platform );
    }

    @Override
    public void create() {
        super.create();

        updateSystemUI();
        SPDAction.loadBindings();

        Music.INSTANCE.enable( SPDSettings.music() );
        Music.INSTANCE.volume( SPDSettings.musicVol()* SPDSettings.musicVol()/100f );
        Sample.INSTANCE.enable( SPDSettings.soundFx() );
        Sample.INSTANCE.volume( SPDSettings.SFXVol()* SPDSettings.SFXVol()/100f );

        Sample.INSTANCE.load( Assets.Sounds.all );

    }

    public static void switchNoFade(Class<? extends PixelScene> c){
        switchNoFade(c, null);
    }

    public static void switchNoFade(Class<? extends PixelScene> c, SceneChangeCallback callback) {
        PixelScene.noFade = true;
        switchScene( c, callback );
    }

    @Override
    protected void switchScene() {
        super.switchScene();
        if (scene instanceof PixelScene){
            ((PixelScene) scene).restoreWindows();
        }
    }

    @Override
    public void resize( int width, int height ) {
        if (width == 0 || height == 0){
            return;
        }

        if (scene instanceof PixelScene &&
                (height != Game.height || width != Game.width)) {
            PixelScene.noFade = true;
            ((PixelScene) scene).saveWindows();
        }

        super.resize( width, height );

        updateDisplaySize();

    }

    @Override
    public void destroy(){
        super.destroy();
        GameScene.endActorThread();
    }

    public void updateDisplaySize(){
        platform.updateDisplaySize();
    }

    public static void updateSystemUI() {
        platform.updateSystemUI();
    }
}
