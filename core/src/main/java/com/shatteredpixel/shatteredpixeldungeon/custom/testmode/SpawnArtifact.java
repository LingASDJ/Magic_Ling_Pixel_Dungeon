package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Objects;

public class SpawnArtifact extends TestItem {
    {
        image = ItemSpriteSheet.ARTIFACT_HOLDER;
        defaultAction = AC_SPAWN;
    }

    private static final String AC_SPAWN = "spawn";
    private int artifact_level;
    private int artifact_id;
    private boolean cursed;

    public SpawnArtifact(){
        this.artifact_level = 0;
        this.artifact_id = 0;
        this.cursed = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SPAWN);
        return actions;
    }
    @Override
    public void execute(Hero hero, String action ) {
        super.execute( hero, action );
        if(action.equals(AC_SPAWN)){
            GameScene.show(new ArtifactSetting());
        }
    }
    private void modifyArtifact(Artifact a){
        int max = Math.min(artifact_level, maxLevel(artifact_id));
        for(int i=0;i<max; ++i){
            a.upgrade();
        }
        a.cursed = cursed;
    }
    public void createArtifact(){
        Artifact artifact = (Artifact) Reflection.newInstance(getArtifact(artifact_id));
        if(artifact != null){
            if(Challenges.isItemBlocked(artifact))
                return;

            for(int i=0;i<Math.min(artifact_level, maxLevel(artifact_id)); ++i){
                artifact.upgrade();
            }
            artifact.cursed = cursed;
            artifact.identify();
            if(artifact.collect()){
                GLog.i(Messages.get(hero, "you_now_have", artifact.name()));
                Sample.INSTANCE.play( Assets.Sounds.ITEM );
                GameScene.pickUp( artifact, hero.pos );
            }else{
                artifact.doDrop(curUser);
            }
        }
    }
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("artifact_id", artifact_id);
        bundle.put("cursed", cursed);
        bundle.put("artifact_level", artifact_level);
    }
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        artifact_id = bundle.getInt("artifact_id");
        cursed = bundle.getBoolean("cursed");
        artifact_level = bundle.getInt("artifact_level");
    }

    private Class getArtifact(int index){
        return Generator.Category.ARTIFACT.classes[index];
    }

    private int maxLevel(int id){
        switch (id){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 6:
            case 8:
            case 9:
            case 10:
            case 13:
            default:
                return 10;
            case 7:
            case 12:
                return 5;
            case 5:
            case 11:
                return 3;
        }
    }

    private static ArrayList<Class<? extends Artifact>> artifactList = new ArrayList<Class<? extends Artifact>>();
    private void buildArtifactArray(){
        if(!artifactList.isEmpty()) return;
        for(int i=0;i<Generator.Category.ARTIFACT.classes.length;++i){
            artifactList.add(getArtifact(i));
        }
    }

    private class ArtifactSetting extends Window {
        private static final int WIDTH = 120;
        private static final int BTN_SIZE = 17;
        private static final int GAP = 2;
        private RenderedTextBlock RenderedTextBlock_selected;
        private OptionSlider OptionSlider_level;
        private CheckBox CheckBox_curse;
        private RedButton RedButton_create;
        private ArrayList<IconButton> artifactSprites = new ArrayList<>();

        public ArtifactSetting(){
            buildArtifactArray();
            createArtifactImage();
            RenderedTextBlock_selected = PixelScene.renderTextBlock("", 6);
            add((RenderedTextBlock_selected));

            OptionSlider_level = new OptionSlider(Messages.get(this, "artifact_level"), "0", "10", 0, 10) {
                @Override
                protected void onChange() {
                    artifact_level = getSelectedValue();
                }
            };
            OptionSlider_level.setSelectedValue(artifact_level);
            add(OptionSlider_level);

            CheckBox_curse = new CheckBox(Messages.get(this, "cursed")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    cursed = checked();
                }
            };
            CheckBox_curse.checked(cursed);
            add(CheckBox_curse);

            RedButton_create = new RedButton(Messages.get(this, "create")) {
                @Override
                protected void onClick() {
                    createArtifact();
                }
            };
            add(RedButton_create);

            updateText();
        }

        private void layout(){
            RenderedTextBlock_selected.setPos(0, 3*GAP + BTN_SIZE *2);
            OptionSlider_level.setRect(0, RenderedTextBlock_selected.bottom() + GAP, WIDTH, 24);
            CheckBox_curse.setRect(0, OptionSlider_level.bottom() + GAP, WIDTH, 18);
            RedButton_create.setRect(0, CheckBox_curse.bottom() + GAP, WIDTH, 16);
            resize(WIDTH, (int)RedButton_create.bottom() + GAP);
        }

        private void createArtifactImage(){
            float left;
            float top = GAP;
            int placed = 0;
            int length = artifactList.size();
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        artifact_id = j;
                        updateText();
                        super.onClick();
                    }
                };
                Image im = new Image(Assets.Sprites.ITEMS);
                im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(artifactList.get(i))).image));
                im.scale.set(1f);
                btn.icon(im);
                if(i<7) {
                    left = (WIDTH - BTN_SIZE * 7) / 2f;
                    btn.setRect(left + placed * BTN_SIZE, top, BTN_SIZE, BTN_SIZE);
                }
                else {
                    left = (WIDTH - BTN_SIZE * 7) / 2f;
                    btn.setRect(left + (placed-7) * BTN_SIZE, top + GAP + BTN_SIZE, BTN_SIZE, BTN_SIZE);
                }
                add(btn);
                placed++;
                artifactSprites.add(btn);
            }
        }

        private void updateText(){
            Artifact artifact = (Artifact) Reflection.newInstance(getArtifact(artifact_id));
            RenderedTextBlock_selected.text(Messages.get(this, "current_artifact",artifact.name()));
            layout();
        }
    }
}
