package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.S_Changes;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.WndChanges;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.WndChangesTabbed;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd.vM0_5_X_Changes;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd.vM0_6_4_P_Changes;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd.vM0_6_6_Changes;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd.vM0_6_7_X_Changes;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd.vM0_7_X_Changes;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd.vm0_8_X_Changes;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd.vm0_9_X_Changes;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.TextInput;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class NewChangesScene extends PixelScene {

    public static int changesSelected = 0;

    public static boolean fromChangesScene = true;

    private NinePatch rightPanel;
    private ScrollPane rightScroll;
    private IconTitle changeTitle;
    private RenderedTextBlock changeBody;

    // searchable changelog state
    private NinePatch panel;
    private ScrollPane list;
    private Component content;
    private TextInput textBox;
    private StyledButton[] tabButtons;
    private IconButton toggleSearchBtn;

    private RenderedTextBlock searchBody;
    /** 搜索栏是否展开。默认关闭，点左边箭头按钮展开。 */
    private boolean searchOpen = false;

    /** All change entries, grouped by version tab (index matches the version switch buttons). */
    private final ArrayList<ArrayList<ChangeInfo>> allTabs = new ArrayList<>();
    /** The entries currently shown in the left-hand list (filtered by search query). */
    private final ArrayList<ChangeInfo> displayedInfos = new ArrayList<>();

    private static final float SEARCH_HEIGHT = 18;

    @Override
    public void create() {
        super.create();

        int w = Camera.main.width;
        int h = Camera.main.height;

        RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(ChangesScene.class, "title"), 9 );
        title.hardlight(Window.TITLE_COLOR);
        title.setPos(
                (w - title.width()) / 2f,
                (20 - title.height()) / 2f
        );
        align(title);
        add(title);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );

        panel = Chrome.get(Chrome.Type.TOAST);

        int pw = 135 + panel.marginLeft() + panel.marginRight() - 2;
        int ph = h - 36;

        if (h >= PixelScene.MIN_HEIGHT_FULL && w >= 300) {
            panel.size( pw, ph );
            panel.x = (w - pw) / 2f - pw/2 - 1;
            panel.y = 20;

            rightPanel = Chrome.get(Chrome.Type.TOAST);
            rightPanel.size( pw, ph );
            rightPanel.x = (w - pw) / 2f + pw/2 + 1;
            rightPanel.y = 20;
            add(rightPanel);

            rightScroll = new ScrollPane(new Component());
            add(rightScroll);
            rightScroll.setRect(
                    rightPanel.x + rightPanel.marginLeft(),
                    rightPanel.y + rightPanel.marginTop()-1,
                    rightPanel.innerWidth() + 2,
                    rightPanel.innerHeight() + 2);
            rightScroll.scrollTo(0, 0);

            changeTitle = new IconTitle(Icons.get(Icons.CHANGES), Messages.get(ChangesScene.class, "right_title"));
            changeTitle.setPos(0, 1);
            changeTitle.setSize(pw, 20);
            rightScroll.content().add(changeTitle);

            String body = Messages.get(ChangesScene.class, "right_body");

            changeBody = PixelScene.renderTextBlock(body, 6);
            changeBody.maxWidth(pw - panel.marginHor());
            changeBody.setPos(0, changeTitle.bottom()+2);
            rightScroll.content().add(changeBody);
        } else {
            panel.size(pw, ph);
            panel.x = (w - pw) / 2f;
            panel.y = title.bottom() + 5;
        }

        align(panel);
        add(panel);

        // Pre-build every version tab's entries up-front so search can span all of them.
        allTabs.clear();
        for (int i = 0; i <= 5; i++) {
            ArrayList<ChangeInfo> tab = new ArrayList<>();
            switch (i) {
                case 0: default:
                    vm0_9_X_Changes.addAllChanges(tab);
                    break;
                case 1:
                    vm0_8_X_Changes.addAllChanges(tab);
                    break;
                case 2:
                    vM0_7_X_Changes.addAllChanges(tab);
                    break;
                case 3:
                    vM0_6_6_Changes.addAllChanges(tab);
                    vM0_6_4_P_Changes.addAllChanges(tab);
                    vM0_6_7_X_Changes.addAllChanges(tab);
                    break;
                case 4:
                    vM0_5_X_Changes.addAllChanges(tab);
                    break;
                case 5:
                    S_Changes.addAllChanges(tab);
                    break;
            }
            allTabs.add(tab);
        }

        // 搜索栏左侧的展开/关闭按钮（Icons.ARROW 箭头）。
        // 默认 searchOpen=false：搜索栏隐藏，列表顶到面板顶部；点击按钮展开搜索栏。
        toggleSearchBtn = new IconButton(Icons.RIGHTARROW.get()){
            @Override
            protected void onClick() {
                searchOpen = !searchOpen;
                textBox.visible = searchOpen;
                textBox.active = searchOpen;
                // 关闭时箭头朝右（→，提示右侧可展开），展开后朝左（←，提示可收起）
                toggleSearchBtn.icon( searchOpen ? Icons.LEFTARROW.get() : Icons.RIGHTARROW.get());
                layoutSearch(searchOpen);
                searchBody.visible = !searchOpen;
            }
        };
        add(toggleSearchBtn);


        searchBody = PixelScene.renderTextBlock(Messages.get(ChangesScene.class,"searchbody"), 8);
        add(searchBody);

        // Search box pinned to the top of the changelog list panel.
        textBox = new TextInput(Chrome.get(Chrome.Type.TOAST_WHITE), false, (int) PixelScene.uiCamera.zoom * 9) {
            @Override
            public void onChanged() {
                applyFilter();
            }
        };
        textBox.setMaxLength(40);
        textBox.visible = false;
        textBox.active = false;
        add(textBox);

        list = new ScrollPane( new Component() ){

            @Override
            public void onClick(float x, float y) {
                for (ChangeInfo info : displayedInfos){
                    if (info.onClick( x, y )){
                        return;
                    }
                }
            }

        };
        add( list );

        content = list.content();

        layoutSearch(false);

        applyFilter();
        list.scrollTo(0, fromChangesScene ? content.height() - list.height() : 0);

        // Version switch buttons. Switching tabs no longer rebuilds the scene, so the
        // search query survives; picking a tab clears the query to show that version fully.
        String[] labels = { "0.9", "0.8", "0.7", "0.6", "0.5-", "Old" };
        float[] widths  = { 28,   28,   28,   24,   18,    10  };
        tabButtons = new StyledButton[labels.length];
        float bx = list.left() - 4f;
        for (int i = 0; i < labels.length; i++) {
            final int idx = i;
            final float bw = widths[i];
            tabButtons[i] = new StyledButton(Chrome.Type.TOAST, labels[i]){
                @Override
                protected void onClick() {
                    super.onClick();
                    if (changesSelected != idx) {
                        changesSelected = idx;
                        textBox.setText("");
                        updateTabButtons();
                        applyFilter();
                        list.scrollTo(0, 0);
                    }
                }
            };
            float bh = changesSelected == idx ? 19 : 15;
            if (i == labels.length - 1) {
                // "Old" sits on the same row as the button before it (mirrors original layout).
                tabButtons[i].setRect(bx, tabButtons[i - 1].top(), bw, bh);
            } else {
                tabButtons[i].setRect(bx, list.bottom(), bw, bh);
            }
            addToBack(tabButtons[i]);
            bx = tabButtons[i].right() + 1;
        }
        updateTabButtons();

        Archs archs = new Archs();
        archs.setSize( Camera.main.width, Camera.main.height );
        addToBack( archs );

        fadeIn();
    }

    /**
     * 根据 {@link #searchOpen} 状态重排搜索栏按钮、输入框和列表位置。
     * 列表始终从搜索栏行下方开始，避免和左上角的按钮重叠；
     * 展开与否只影响 textBox 是否可见。
     */
    private void layoutSearch(boolean c) {
        float bx = panel.x + panel.marginLeft();
        float by = panel.y + panel.marginTop() - 1;

        toggleSearchBtn.setRect(bx, by, SEARCH_HEIGHT, SEARCH_HEIGHT);

        textBox.setRect(
                bx + SEARCH_HEIGHT + 1,
                c? by : by+5000,
                panel.innerWidth() - SEARCH_HEIGHT - 1,
                SEARCH_HEIGHT);

        searchBody.setPos(bx+20, by+6);

        list.setRect(
                bx,
                by + SEARCH_HEIGHT + 2,
                panel.innerWidth() + 2,
                panel.innerHeight() + 2 - SEARCH_HEIGHT - 2);

        if (tabButtons != null) {
            updateTabButtons();
        }
    }

    /** Refill the changelog list from allTabs, filtered by the current search query. */
    private void applyFilter() {
        if (content == null || textBox == null) return;

        content.clear();
        displayedInfos.clear();

        String query = textBox.getText() == null ? "" : textBox.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            displayedInfos.addAll(allTabs.get(changesSelected));
        } else {
            for (ArrayList<ChangeInfo> tab : allTabs) {
                for (ChangeInfo info : tab) {
                    if (info.searchText().toLowerCase().contains(query)) {
                        displayedInfos.add(info);
                    }
                }
            }
        }

        float posY = 0;
        float nextPosY = 0;
        boolean second = false;
        for (ChangeInfo info : displayedInfos) {
            if (info.major) {
                posY = nextPosY;
                second = false;
                info.setRect(0, posY, panel.innerWidth(), 0);
                content.add(info);
                posY = nextPosY = info.bottom();
            } else {
                if (!second){
                    second = true;
                    info.setRect(0, posY, panel.innerWidth()/2f, 0);
                    content.add(info);
                    nextPosY = info.bottom();
                } else {
                    second = false;
                    info.setRect(panel.innerWidth()/2f, posY, panel.innerWidth()/2f, 0);
                    content.add(info);
                    nextPosY = Math.max(info.bottom(), nextPosY);
                    posY = nextPosY;
                }
            }
        }

        content.setSize( panel.innerWidth(), (int)Math.ceil(posY) );
        list.setSize( list.width(), list.height() );
    }

    /** Grey out every tab button except the currently selected one, and raise the selected one. */
    private void updateTabButtons() {
        for (int i = 0; i < tabButtons.length; i++) {
            boolean selected = changesSelected == i;
            float bh = selected ? 19 : 15;
            // buttons sit on a row aligned to list.bottom(); the last "Old" button aligns to the previous one's top.
            float by = (i == tabButtons.length - 1) ? tabButtons[i - 1].top() : list.bottom();
            tabButtons[i].setRect(tabButtons[i].left(), by, tabButtons[i].width(), bh);
            tabButtons[i].textColor(selected ? 0xFFFFFF : 0xBBBBBB);
        }
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(TitleScene.class);
    }

    public void updateMLPDChangesText(Image icon, String title, String... messages){
        if (changeTitle != null){
            changeTitle.icon(icon);
            changeTitle.label(title);
            changeTitle.setPos(changeTitle.left(), changeTitle.top());

            String message = "";
            for (int i = 0; i < messages.length; i++){
                message += messages[i];
                if (i != messages.length-1){
                    message += "\n\n";
                }
            }
            changeBody.text(message);
            rightScroll.content().setSize(rightScroll.width(), changeBody.bottom()+2);
            rightScroll.setSize(rightScroll.width(), rightScroll.height());
            rightScroll.scrollTo(0, 0);

        } else {
            if (messages.length == 1) {
                addToFront(new WndChanges(icon, title, messages[0]));
            } else {
                addToFront(new WndChangesTabbed(icon, title, messages));
            }
        }
    }

}
