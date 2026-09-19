package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SeedFinderScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeedFindLogScene extends PixelScene {

    public ScrollPane list;
    public static String s;
    public static CreditsBlock txt;
    public static RenderedTextBlock r;
    public static Thread thread;
    public static Component content;
    public WndTextInput wndTextInput;

    public static boolean powerMode = false;
    public static SeedResult lastResult = null;
    public static int searchedFloors = 15;

    public static int safeParseInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 安全校验：当前场景是否仍为SeedFindLogScene，避免操作已销毁的UI
     */
    public static boolean isSceneActive() {
        return ShatteredPixelDungeon.scene() instanceof SeedFindLogScene;
    }

    @Override
    public void create() {
        super.create();

        final float colWidth = 120;
        final float fullWidth = colWidth * (landscape() ? 2 : 1);

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        add(new ColorBlock(w, h, 0x88000000));

        list = new ScrollPane(new Component());
        add(list);
        content = list.content();

        // 查找进行中：恢复显示状态
        if (thread != null && thread.isAlive()) {
            content.clear();
            if (s != null) {
                txt = new CreditsBlock(true, Window.TITLE_COLOR, s);
                txt.setRect((Camera.main.width - colWidth) / 2f, 12, colWidth, 0);
                content.add(txt);
                content.setSize(fullWidth, txt.bottom() + 10);
            }
            list.setRect(0, 0, w, h);
            list.scrollTo(0, 0);

            // 重建进度文本，避免静态引用失效
            if (r == null) {
                r = PixelScene.renderTextBlock(Messages.get(SeedFinder.class, "seedfinder"), 9);
                r.maxWidth(w - 40);
                r.setPos(20, 20);
            }
            addToFront(r);
            return;
        }

        s = null;
        r = null;
        txt = null;

        ShatteredPixelDungeon.scene().addToFront(wndTextInput = new WndTextInput(
                Messages.get(this, "title"),
                Messages.get(this, "body"),
                SPDSettings.seeditemsText(),
                1000,
                true,
                Messages.get(this, "find"),
                Messages.get(this, "format")
        ) {
            @Override
            public void onSelect(boolean positive, String text) {
                int floor = SPDSettings.seedfinderFloors();
                boolean floorOption = false;
                String up_to_floor = "floor end";
                String strFloor = "floor";

                SPDSettings.seeditemsText(text);

                if (text.contains(up_to_floor)) {
                    floorOption = true;
                    String fl = text.split(strFloor)[0].trim();
                    floor = Math.min(safeParseInt(fl, 15), 30);
                }

                if (positive && !text.isEmpty() && floorOption) {
                    String[] itemList = floorOption
                            ? Arrays.copyOfRange(text.split("\n"), 1, text.split("\n").length)
                            : text.split("\n");

                    content.clear();

                    // 初始化进度文本，杜绝abc残留
                    r = PixelScene.renderTextBlock(Messages.get(SeedFinder.class, "seedfinder"), 7);
                    r.maxWidth(w - 40);
                    r.setPos(20, 20);
                    ShatteredPixelDungeon.scene().addToFront(r);

                    list.setRect(0, 0, w, h);
                    list.scrollTo(0, 0);

                    final int finalFloor = floor;
                    searchedFloors = finalFloor;

                    // 启动新线程前先终止旧线程
                    stopSearchThread();

                    thread = new Thread(() -> {
                        SeedResult res;
                        try {
                            // 接收SeedResult对象，修复类型不匹配
                            if(SPDSettings.PlusSearch()){
                                res = new SeedFinder().findSeedParallel(itemList, finalFloor,SPDSettings.PlusThread());
                            } else {
                                res = new SeedFinder().findSeed(itemList, finalFloor);
                            }
                        } catch (Exception e) {
                            Gdx.app.error("SeedFinder", "Search failed", e);
                            // 异常构造失败结果
                            res = new SeedResult("Search Error: " + e.getMessage(), "", null, false);
                        }

                        SeedResult finalRes = res;
                        Gdx.app.postRunnable(() -> {
                            // 核心修复：UI操作前先校验场景是否存活
                            if (!isSceneActive()) return;

                            // 组件销毁则跳过操作
                            if (r != null) {
                                r.destroy();
                                r = null;
                            }

                            if (content == null) return;

                            // 将完整日志赋值给全局字符串s
                            s = finalRes.fullLog;

                            txt = new CreditsBlock(true, Window.TITLE_COLOR, s);
                            txt.setRect((Camera.main.width - colWidth) / 2f, 12, colWidth, 0);

                            // 二次校验：防止content已被销毁导致members空指针
                            try {
                                content.add(txt);
                                content.setSize(fullWidth, txt.bottom() + 10);
                            } catch (NullPointerException e) {
                                Gdx.app.debug("SeedFinder", "UI already destroyed, skip update");
                                return;
                            }

                            if (list != null && list.isActive()) {
                                list.setRect(0, 0, w, h);
                                list.scrollTo(0, 0);
                            }

                            if (finalRes.success) {
                                StringBuilder msg = new StringBuilder();

                                msg.append(Messages.get(SeedFindLogScene.class, "found_seed"))
                                        .append(finalRes.seedStr).append("\n");
                                msg.append(Messages.get(SeedFindLogScene.class, "code"))
                                        .append(SPDSettings.challenges()).append("\n\n");

                                msg.append(Messages.get(SeedFindLogScene.class, "match_floor_list")).append("\n");
                                for (String info : finalRes.matchedInfo) {
                                    msg.append("- ").append(info).append("\n");
                                }

                                msg.append("\n").append(Messages.get(SeedFindLogScene.class, "scroll_full_log"));

                                String winTitle = Messages.get(SeedFindLogScene.class, "window_title");
                                ShatteredPixelDungeon.scene().addToFront(new WndError(Icons.CATALOG, winTitle, msg.toString()));
                            }
                        });
                    });
                    thread.setName("SeedFinder-Worker");
                    thread.setDaemon(true);
                    thread.start();
                } else {
                    SPDSettings.seeditemsText(Messages.get(SeedFindLogScene.class, "initial_value"));
                    ShatteredPixelDungeon.switchScene(SeedFinderScene.class);
                }
            }
        });

        ExitButton btnExit = new ExitButton() {
            @Override
            protected void onClick() {
                stopSearchAndCleanup();
                ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
                System.gc();
            }
        };
        btnExit.setPos(Camera.main.width - btnExit.width(), 0);
        add(btnExit);

        fadeIn();
    }

    /**
     * 停止查找线程并清理UI静态引用
     */
    private void stopSearchAndCleanup() {
        stopSearchThread();
        if (r != null) {
            r.destroy();
            r = null;
        }
        txt = null;
        content = null;
        s = null;
    }

    private static void stopSearchThread() {
        SeedFinder.findingStatus = SeedFinder.FINDING.STOP;
        SeedFinder.parallelFound = true;
        if (thread != null && thread.isAlive()) thread.interrupt();
        thread = null;
    }

    @Override
    public void destroy() {
        // 场景销毁时必须清理所有静态UI引用，杜绝回调空指针
        stopSearchAndCleanup();
        super.destroy();
    }

    @Override
    protected void onBackPressed() {
        stopSearchAndCleanup();
        ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
        System.gc();
    }

    public static class CreditsBlock extends Component {

        boolean large;
        public RenderedTextBlock body;

        public CreditsBlock(boolean large, int highlight, String body) {
            super();
            this.large = large;
            this.body = PixelScene.renderTextBlock(body, 6);
            if (highlight != -1)
                this.body.setHightlighting(true, highlight);
            if (large)
                this.body.align(RenderedTextBlock.CENTER_ALIGN);
            add(this.body);
        }

        @Override
        protected void layout() {
            super.layout();
            float topY = top();
            if (large) {
                body.maxWidth((int) width());
                body.setPos(x + (width() - body.width()) / 2f, topY);
            } else {
                topY += 1;
                body.maxWidth((int) width());
                body.setPos(x, topY);
            }
            topY += body.height();
            height = Math.max(height, topY - top());
        }
    }

    /** 查种成功后询问是否保存记录 */
    private static void promptSaveRecord(final SeedResult res) {
        ShatteredPixelDungeon.scene().addToFront(new WndOptions(
                Messages.get(SeedFindLogScene.class, "save_title"),
                Messages.get(SeedFindLogScene.class, "save_body"),
                Messages.get(SeedFindLogScene.class, "save_yes"),
                Messages.get(SeedFindLogScene.class, "save_no")) {
            @Override protected void onSelect(int index) {
                if (index == 0) {
                    SeedRecord rec = new SeedRecord();
                    rec.seed = res.seedStr;
                    try {
                        rec.seedCode = DungeonSeed.convertToCode(Long.parseLong(res.seedStr));
                    } catch (Exception e) {
                        rec.seedCode = res.seedStr;
                    }
                    rec.challenges = SPDSettings.challenges();
                    rec.floors = searchedFloors;
                    rec.condition = "";
                    rec.time = System.currentTimeMillis();
                    rec.matchedInfo = new ArrayList<>(res.matchedInfo);
                    rec.floorItems = res.floorItems != null ? res.floorItems : new java.util.HashMap<>();
                    SeedRecordManager.saveRecord(rec);
                    ShatteredPixelDungeon.scene().addToFront(new WndError(Icons.CATALOG,
                            Messages.get(SeedFindLogScene.class, "saved_title"),
                            Messages.get(SeedFindLogScene.class, "saved_body")));
                }
            }
        });
    }

    /** 打开记录管理 */
    private static void showRecords() {
        final List<SeedRecord> records = SeedRecordManager.loadAll();
        if (records.isEmpty()) {
            ShatteredPixelDungeon.scene().addToFront(new WndOptions(Icons.get(Icons.CATALOG),
                    Messages.get(SeedFindLogScene.class, "records_title"),
                    Messages.get(SeedFindLogScene.class, "records_empty")));
            return;
        }
        String[] titles = new String[records.size()];
        for (int i = 0; i < records.size(); i++) titles[i] = records.get(i).getDisplayTitle();

        ShatteredPixelDungeon.scene().addToFront(new WndOptions(
                Messages.get(SeedFindLogScene.class, "records_count", records.size()),"",
                titles) {
            @Override protected void onSelect(int index) {
                showRecordActions(records, index);
            }
        });
    }

    private static void showRecordActions(final List<SeedRecord> records, final int index) {
        final SeedRecord rec = records.get(index);
        List<String> opts = new ArrayList<>();
        opts.add(Messages.get(SeedFindLogScene.class, "action_detail"));
        if (lastResult != null) opts.add(Messages.get(SeedFindLogScene.class, "action_compare_last"));
        for (int i = 0; i < records.size(); i++) {
            if (i != index) opts.add(Messages.get(SeedFindLogScene.class, "action_compare_with",
                    records.get(i).getDisplayTitle()));
        }
        opts.add(Messages.get(SeedFindLogScene.class, "action_delete"));

        ShatteredPixelDungeon.scene().addToFront(new WndOptions(rec.getDisplayTitle(),"",
                opts.toArray(new String[0])) {
            @Override protected void onSelect(int sel) {
                if (sel == 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Messages.get(SeedFindLogScene.class, "detail_seed", rec.seedCode)).append("\n");
                    sb.append(Messages.get(SeedFindLogScene.class, "detail_range", rec.floors)).append("\n");
                    sb.append(Messages.get(SeedFindLogScene.class, "detail_matched")).append("\n");
                    for (String m : rec.matchedInfo)
                        sb.append(" - ").append(m).append("\n");
                    ShatteredPixelDungeon.scene().addToFront(new WndError(Icons.CATALOG,
                            Messages.get(SeedFindLogScene.class, "detail_title"), sb.toString()));
                    return;
                }
                int compareBase = 1;
                boolean hasLast = lastResult != null;
                if (hasLast && sel == compareBase) {
                    SeedRecord cur = new SeedRecord();
                    cur.seedCode = Messages.get(SeedFindLogScene.class, "current_result");
                    cur.floorItems = lastResult.floorItems != null ? lastResult.floorItems : new java.util.HashMap<>();
                    String cmp = SeedRecordManager.compare(cur, rec);
                    ShatteredPixelDungeon.scene().addToFront(new WndError(Icons.CATALOG,
                            Messages.get(SeedFindLogScene.class, "compare_result_title"), cmp));
                    return;
                }
                int compareIdx = hasLast ? (sel - compareBase - 1) : (sel - compareBase);
                int otherCount = 0;
                int otherRecordIndex = -1;
                for (int i = 0; i < records.size(); i++) {
                    if (i == index) continue;
                    if (otherCount == compareIdx) { otherRecordIndex = i; break; }
                    otherCount++;
                }
                if (otherRecordIndex >= 0) {
                    String cmp = SeedRecordManager.compare(records.get(otherRecordIndex), rec);
                    ShatteredPixelDungeon.scene().addToFront(new WndError(Icons.CATALOG,
                            Messages.get(SeedFindLogScene.class, "compare_result_title"), cmp));
                } else {
                    SeedRecordManager.deleteRecord(index);
                    ShatteredPixelDungeon.scene().addToFront(new WndError(Icons.CATALOG,
                            Messages.get(SeedFindLogScene.class, "deleted_title"),
                            Messages.get(SeedFindLogScene.class, "deleted_body")));
                }
            }
        });
    }


}