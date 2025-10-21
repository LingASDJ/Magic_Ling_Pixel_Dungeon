package com.shatteredpixel.shatteredpixeldungeon.html;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.xpenatan.gdx.backends.teavm.assetloader.AssetInstance;
import com.github.xpenatan.gdx.backends.teavm.assetloader.AssetLoader;
import com.github.xpenatan.gdx.backends.teavm.assetloader.AssetLoaderListener;
import com.github.xpenatan.gdx.backends.teavm.assetloader.AssetType;
import com.github.xpenatan.gdx.backends.teavm.assetloader.TeaBlob;
import com.github.xpenatan.gdx.backends.teavm.TeaApplication;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Two-stage preload:
 * - stage 1: sequentially load explicit frame list and build animation
 * - stage 2: preload assets.txt and show progress
 *
 * This variant keeps the border unchanged and reduces the inner progress bar height
 * by barInset at top and bottom so the gray gap between the white border and the green fill
 * appears larger. The fills are laid out using the computed innerHeight and the table
 * is invalidated/validated so the change is applied immediately.
 */
public class CustomPreloadScreen extends ApplicationAdapter {

    public String framesFolder = "frames";

    private static final String[] FIXED_FILENAMES = new String[] {
        "frame_00_delay-0.04s.gif","frame_01_delay-0.04s.gif","frame_02_delay-0.04s.gif","frame_03_delay-0.04s.gif",
        "frame_04_delay-0.05s.gif","frame_05_delay-0.03s.gif","frame_06_delay-0.04s.gif","frame_07_delay-0.04s.gif",
        "frame_08_delay-0.04s.gif","frame_09_delay-0.04s.gif","frame_10_delay-0.04s.gif","frame_11_delay-0.04s.gif",
        "frame_12_delay-0.04s.gif","frame_13_delay-0.04s.gif","frame_14_delay-0.04s.gif","frame_15_delay-0.04s.gif",
        "frame_16_delay-0.04s.gif","frame_17_delay-0.04s.gif","frame_18_delay-0.04s.gif","frame_19_delay-0.04s.gif",
        "frame_20_delay-0.04s.gif","frame_21_delay-0.04s.gif","frame_22_delay-0.04s.gif","frame_23_delay-0.04s.gif",
        "frame_24_delay-0.04s.gif","frame_25_delay-0.04s.gif","frame_26_delay-0.04s.gif","frame_27_delay-0.04s.gif",
        "frame_28_delay-0.05s.gif","frame_29_delay-0.04s.gif","frame_30_delay-0.04s.gif","frame_31_delay-0.04s.gif",
        "frame_32_delay-0.04s.gif","frame_33_delay-0.04s.gif","frame_34_delay-0.05s.gif","frame_35_delay-0.04s.gif",
        "frame_36_delay-0.04s.gif","frame_37_delay-0.05s.gif","frame_38_delay-0.04s.gif","frame_39_delay-0.04s.gif",
        "frame_40_delay-0.04s.gif","frame_41_delay-0.04s.gif","frame_42_delay-0.04s.gif","frame_43_delay-0.04s.gif",
        "frame_44_delay-0.04s.gif","frame_45_delay-0.04s.gif","frame_46_delay-0.04s.gif","frame_47_delay-0.04s.gif",
        "frame_48_delay-0.04s.gif","frame_49_delay-0.05s.gif","frame_50_delay-0.04s.gif","frame_51_delay-0.04s.gif",
        "frame_52_delay-0.05s.gif","frame_53_delay-0.03s.gif","frame_54_delay-0.04s.gif","frame_55_delay-0.04s.gif",
        "frame_56_delay-0.04s.gif","frame_57_delay-0.04s.gif","frame_58_delay-0.04s.gif","frame_59_delay-0.04s.gif",
        "frame_60_delay-0.04s.gif","frame_61_delay-0.04s.gif","frame_62_delay-0.04s.gif","frame_63_delay-0.05s.gif",
        "frame_64_delay-0.04s.gif","frame_65_delay-0.04s.gif","frame_66_delay-0.04s.gif","frame_67_delay-0.04s.gif",
        "frame_68_delay-0.04s.gif","frame_69_delay-0.04s.gif","frame_70_delay-0.04s.gif","frame_71_delay-0.04s.gif",
        "frame_72_delay-0.04s.gif","frame_73_delay-0.04s.gif","frame_74_delay-0.04s.gif","frame_75_delay-0.04s.gif",
        "frame_76_delay-0.04s.gif","frame_77_delay-0.05s.gif","frame_78_delay-0.04s.gif","frame_79_delay-0.04s.gif",
        "frame_80_delay-0.04s.gif","frame_81_delay-0.04s.gif","frame_82_delay-0.04s.gif","frame_83_delay-0.04s.gif",
        "frame_84_delay-0.04s.gif","frame_85_delay-0.04s.gif","frame_86_delay-0.04s.gif","frame_87_delay-0.04s.gif",
        "frame_88_delay-0.04s.gif","frame_89_delay-0.04s.gif","frame_90_delay-0.04s.gif","frame_91_delay-0.04s.gif",
        "frame_92_delay-0.04s.gif","frame_93_delay-0.04s.gif","frame_94_delay-0.04s.gif","frame_95_delay-0.04s.gif"
    };

    private Stage stage;
    private Table table;
    private Image gifImage;
    private Image leftFill, rightFill;
    private float barMaxWidth = 600f;
    private final float progressBoxHeight = 35f;
    private final float innerBarHeight = 20f;
    private final float borderPadding = 4f; // keep border unchanged
    private final float barInset = 8f; // increased inset so gray gap doubles

    private Animation<TextureRegion> gifAnimation;
    private TextureRegionDrawable gifDrawable;
    private float gifTime = 0f;
    private int framesCount = 0;
    private float[] perFrameDelays = null;
    private float[] perFramePrefix = null;
    private float totalAnimationDuration = 0f;
    private float originalWidth = 128f;
    private float originalHeight = 128f;

    protected AssetLoader assetLoader;
    private TeaApplication teaApplication;
    private int initQueue = 0;
    private int assetsInitial = -1;
    private float displayedProgress = 0f;
    private float targetProgress = 0f;

    private final List<TextureRegion> loadedRegions = new ArrayList<>();
    private final Map<String, Texture> textures = new HashMap<>();

    private static final Pattern DELAY_PATTERN = Pattern.compile("_delay-([0-9]+\\.?[0-9]*)s", Pattern.CASE_INSENSITIVE);

    @Override
    public void create() {
        teaApplication = TeaApplication.get();
        assetLoader = AssetInstance.getLoaderInstance();
        startLoadFrames(framesFolder, FIXED_FILENAMES);
    }

    private void startLoadFrames(final String folder, final String[] names) {
        loadedRegions.clear();
        textures.clear();
        framesCount = names.length;
        perFrameDelays = new float[framesCount];
        for (int i = 0; i < framesCount; i++) {
            float d = parseDelay(names[i]);
            perFrameDelays[i] = d > 0f ? d : 0.04f;
        }
        loadFrame(folder, names, 0);
    }

    private void loadFrame(final String folder, final String[] names, final int idx) {
        if (idx >= names.length) {
            Gdx.app.postRunnable(() -> {
                if (!buildAnimationFromLoaded()) buildFallback();
                Gdx.app.postRunnable(this::startAssetsPreload);
            });
            return;
        }
        addQueue();
        final String filename = names[idx];
        final String path = folder + "/" + filename;
        assetLoader.loadAsset(path, AssetType.Binary, Files.FileType.Internal, new AssetLoaderListener<TeaBlob>() {
            public void onSuccess(String url, TeaBlob blob) {
                try {
                    Texture tex = tryTextureFromInternal(path);
                    if (tex == null && blob != null) tex = tryTextureFromBlob(path, blob);
                    if (tex != null) {
                        tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
                        textures.put(path, tex);
                        loadedRegions.add(new TextureRegion(tex));
                    } else {
                        Texture p = createPlaceholder(128, 128);
                        textures.put("ph_" + idx, p);
                        loadedRegions.add(new TextureRegion(p));
                    }
                } catch (Throwable t) {
                    Texture p = createPlaceholder(128, 128);
                    textures.put("ph_exc_" + idx, p);
                    loadedRegions.add(new TextureRegion(p));
                } finally { subtractQueue(); }
                loadFrame(folder, names, idx + 1);
            }
            public void onFailure(String url, Throwable t) {
                Texture p = createPlaceholder(128, 128);
                textures.put("ph_404_" + idx, p);
                loadedRegions.add(new TextureRegion(p));
                subtractQueue();
                loadFrame(folder, names, idx + 1);
            }
        });
    }

    private Texture tryTextureFromInternal(String path) {
        try {
            FileHandle fh = Gdx.files.internal(path);
            if (!fh.exists()) {
                FileHandle fh2 = Gdx.files.internal(path.startsWith("/") ? path.substring(1) : path);
                if (fh2.exists()) fh = fh2;
            }
            if (fh.exists()) {
                return new Texture(fh);
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private Texture tryTextureFromBlob(String path, TeaBlob blob) {
        try {
            byte[] bytes = blobToBytes(blob);
            if (bytes != null && bytes.length > 0) {
                FileHandle tmp = Gdx.files.local(path);
                tmp.writeBytes(bytes, false);
                return new Texture(tmp);
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private float parseDelay(String name) {
        if (name == null) return 0f;
        try {
            Matcher m = DELAY_PATTERN.matcher(name);
            if (m.find()) return Float.parseFloat(m.group(1));
        } catch (Throwable ignored) {}
        return 0f;
    }

    private boolean buildAnimationFromLoaded() {
        if (loadedRegions.isEmpty()) return false;
        Array<TextureRegion> frames = new Array<>();
        for (TextureRegion tr : loadedRegions) frames.add(tr);
        framesCount = frames.size;
        if (perFrameDelays == null || perFrameDelays.length != framesCount) {
            perFrameDelays = new float[framesCount];
            for (int i = 0; i < framesCount; i++) perFrameDelays[i] = 0.04f;
        }
        perFramePrefix = new float[framesCount];
        totalAnimationDuration = 0f;
        for (int i = 0; i < framesCount; i++) {
            totalAnimationDuration += perFrameDelays[i];
            perFramePrefix[i] = totalAnimationDuration;
        }
        if (totalAnimationDuration <= 0f) totalAnimationDuration = 0.0001f;
        gifAnimation = new Animation<>(1f / 100f, frames, Animation.PlayMode.LOOP);
        gifTime = 0f;
        setupStageMinimal();
        gifDrawable.setRegion(frames.get(0));
        return true;
    }

    private void buildFallback() {
        Texture p = createPlaceholder(128, 128);
        Array<TextureRegion> a = new Array<>();
        a.add(new TextureRegion(p));
        gifAnimation = new Animation<>(1f / 100f, a, Animation.PlayMode.LOOP);
        framesCount = 1;
        perFrameDelays = new float[] { 0.04f };
        perFramePrefix = new float[] { 0.04f };
        totalAnimationDuration = 0.04f;
        gifTime = 0f;
        setupStageMinimal();
        gifDrawable.setRegion(a.get(0));
    }

    private void startAssetsPreload() {
        if (stage == null) setupStageMinimal();
        assetLoader.preload("assets.txt", new AssetLoaderListener<Void>() {
            public void onSuccess(String url, Void result) {
                Gdx.app.postRunnable(() -> assetsInitial = Math.max(0, assetLoader.getQueue()));
            }
            public void onFailure(String url, Throwable t) {
                assetsInitial = 0;
                if (teaApplication != null) teaApplication.setPreloadReady();
            }
        });
    }

    private void setupStageMinimal() {
        if (stage == null) stage = new Stage(new ScreenViewport());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        if (gifDrawable == null) {
            TextureRegion first = gifAnimation != null ? gifAnimation.getKeyFrames()[0] : new TextureRegion(createPlaceholder(128, 128));
            gifDrawable = new TextureRegionDrawable(first);
        }
        if (gifImage == null) {
            gifImage = new Image(gifDrawable);
            gifImage.setScaling(Scaling.fit);
        }

        if (leftFill == null) {
            Texture t = solidTexture(1, 1, 0x44, 0xFF, 0x82);
            TextureRegionDrawable r = new TextureRegionDrawable(new TextureRegion(t));
            leftFill = new Image(r);
            rightFill = new Image(r);
            leftFill.setScaleX(-1f);
            textures.put("__ui_fill__", t);
        }

        if (table == null) {
            table = new Table();
            table.setFillParent(true);
            table.align(Align.center);
            stage.addActor(table);
        }

        TextureRegion tr = gifDrawable.getRegion();
        if (tr != null) {
            originalWidth = Math.max(1, tr.getRegionWidth());
            originalHeight = Math.max(1, tr.getRegionHeight());
        }

        updateLayout(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void updateLayout(int width, int height) {
        if (table == null) return;
        table.clear();

        float screenPad = 0.05f;
        float maxImgWidth = Math.min(850f, width * (1f - screenPad * 2f));
        float targetWidth = Math.max(50f, Math.min(maxImgWidth, width * 0.9f));
        float aspect = (originalHeight > 0 && originalWidth > 0) ? (originalHeight / originalWidth) : 1f;
        float targetHeight = targetWidth * aspect;

        Stack s = new Stack();
        s.add(gifImage);
        s.setSize(targetWidth, targetHeight);
        table.add(s).width(targetWidth).height(targetHeight).padBottom(10f).row();

        barMaxWidth = Math.min(600f, width * 0.8f);
        float barHeight = progressBoxHeight;

        // doubled padding value used for visual framing (border unchanged)
        float doubledPadding = borderPadding * 2f;

        float innerWidth = Math.max(0f, barMaxWidth - (doubledPadding * 2f));
        float innerHeight = Math.max(0f, barHeight - (doubledPadding * 2f));
        // subtract barInset on top and bottom to increase gray gap
        innerHeight = Math.max(innerBarHeight, innerHeight - (barInset * 2f));

        Table fills = new Table();
        fills.align(Align.center);
        // IMPORTANT: use computed innerHeight so layout reserves the smaller inner bar and larger gaps
        fills.add(leftFill).width(0).height(innerHeight);
        fills.add(rightFill).width(0).height(innerHeight);

        Stack progressStack = new Stack();

        Texture bg = textures.get("__ui_bg__");
        if (bg == null) {
            bg = solidTexture(1,1,26,26,26);
            textures.put("__ui_bg__", bg);
        }
        Image bgImg = new Image(new TextureRegionDrawable(new TextureRegion(bg)));
        bgImg.setScaling(Scaling.stretch);
        progressStack.add(bgImg);

        Table border = new Table();
        // BorderDrawable still draws the same border; pass doubledPadding so insets align visually
        border.setBackground(new BorderDrawable(2f, doubledPadding, barHeight));
        border.center();
        // explicitly pad top/bottom by doubledPadding so border visually frames the smaller inner bar
        border.add(fills).width(innerWidth).height(innerHeight).padTop(doubledPadding).padBottom(doubledPadding);

        progressStack.add(border);

        table.add(progressStack).width(barMaxWidth).height(barHeight);

        // force layout to recompute immediately so changes are visible
        table.invalidateHierarchy();
        table.validate();

        if (stage != null) stage.getViewport().update(width, height, true);
    }

    private Texture createPlaceholder(int w, int h) {
        Pixmap p = new Pixmap(Math.max(1,w), Math.max(1,h), Format.RGBA8888);
        p.setColor(0.25f,0.25f,0.25f,1f);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    private Texture solidTexture(int w, int h, int r, int g, int b) {
        Pixmap p = new Pixmap(Math.max(1,w), Math.max(1,h), Format.RGBA8888);
        p.setColor(r/255f, g/255f, b/255f, 1f);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    private byte[] blobToBytes(TeaBlob blob) {
        if (blob == null) return null;
        String[] names = new String[] {"getBytes","getByteArray","toArray","getData","asArray"};
        for (String n : names) {
            try {
                Method m = blob.getClass().getMethod(n);
                Object r = m.invoke(blob);
                if (r instanceof byte[]) return (byte[]) r;
                if (r instanceof int[]) {
                    int[] a = (int[]) r; byte[] out = new byte[a.length];
                    for (int i=0;i<a.length;i++) out[i] = (byte)(a[i]&0xFF);
                    return out;
                }
            } catch (Throwable ignored) {}
        }
        try { String s = blob.toString(); if (s!=null && !s.isEmpty()) return s.getBytes(StandardCharsets.UTF_8); } catch (Throwable ignored){}
        return null;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f,0f,0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float dt = Gdx.graphics.getDeltaTime();

        if (gifAnimation != null && perFramePrefix != null && perFramePrefix.length == framesCount) {
            gifTime += dt;
            float t = gifTime % totalAnimationDuration;
            int idx = 0;
            for (int i=0;i<perFramePrefix.length;i++) { if (t <= perFramePrefix[i]) { idx = i; break; } }
            TextureRegion tr = gifAnimation.getKeyFrames()[Math.max(0, Math.min(idx, framesCount-1))];
            if (tr != null) gifDrawable.setRegion(tr);
        } else if (gifAnimation != null) {
            gifTime += dt;
            TextureRegion tr = gifAnimation.getKeyFrame(gifTime, true);
            if (tr != null) gifDrawable.setRegion(tr);
        }

        if (assetsInitial > 0) {
            int queue = Math.max(0, assetLoader.getQueue());
            int done = Math.max(0, assetsInitial - queue);
            targetProgress = (float) done / (float) assetsInitial;
        } else {
            targetProgress = 0.3f + 0.5f * (float)Math.abs(Math.sin(System.currentTimeMillis()/800.0));
        }

        float maxDelta = dt * 0.9f;
        if (displayedProgress < targetProgress) displayedProgress = Math.min(targetProgress, displayedProgress + maxDelta);
        else displayedProgress = targetProgress;

        // use same doubledPadding and barInset as layout
        float doubledPadding = borderPadding * 2f;
        float innerW = Math.max(0f, barMaxWidth - (doubledPadding * 2f));
        float half = (displayedProgress * innerW) / 2f;
        if (leftFill != null) leftFill.setWidth(half);
        if (rightFill != null) rightFill.setWidth(half);

        float innerH = Math.max(0f, progressBoxHeight - (doubledPadding * 2f));
        innerH = Math.max(innerBarHeight, innerH - (barInset * 2f)); // subtract inset top+bottom
        if (leftFill != null) leftFill.setHeight(innerH);
        if (rightFill != null) rightFill.setHeight(innerH);

        if (stage != null) {
            stage.act(dt);
            stage.draw();
        }

        if (assetsInitial > 0 && assetLoader.getQueue() == 0 && displayedProgress >= 0.9999f && initQueue == 0) {
            if (teaApplication != null) teaApplication.setPreloadReady();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) updateLayout(width,height);
    }

    @Override
    public void dispose() {
        try { if (stage != null) stage.dispose(); } catch (Throwable ignored) {}
        for (Texture t : textures.values()) { try { if (t!=null) t.dispose(); } catch (Throwable ignored) {} }
        textures.clear();
        loadedRegions.clear();
    }

    final protected void addQueue() { initQueue++; }
    final protected void subtractQueue() { initQueue--; }

    // Border drawable draws the white border lines; it is not changed in size by barInset
    private static class BorderDrawable extends BaseDrawable {
        private final Texture pixel;
        private final float thickness;
        private final float padding;
        private final float minHeight;

        public BorderDrawable(float thickness, float padding, float minHeight) {
            this.thickness = thickness;
            this.padding = padding;
            this.minHeight = minHeight;
            Pixmap pm = new Pixmap(1, 1, Format.RGBA8888);
            pm.setColor(Color.WHITE);
            pm.fill();
            pixel = new Texture(pm);
            pm.dispose();
            pixel.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }

        @Override
        public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float x, float y, float width, float height) {
            float t = thickness;
            batch.draw(pixel, x, y + height - t, width, t);
            batch.draw(pixel, x, y, width, t);
            batch.draw(pixel, x, y, t, height);
            batch.draw(pixel, x + width - t, y, t, height);
        }

        @Override public float getLeftWidth() { return thickness + padding; }
        @Override public float getRightWidth() { return thickness + padding; }
        @Override public float getTopHeight() { return thickness + padding; }
        @Override public float getBottomHeight() { return thickness + padding; }
        @Override public float getMinHeight() { return minHeight; }
    }
}
