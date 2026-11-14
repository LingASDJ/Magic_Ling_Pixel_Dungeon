package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.StartScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 联机房间：房间号、玩家列表（英雄+名字）、房主选存档/开始、双方选英雄。
 */
public class MpRoomScene extends PixelScene {

    private int margin;
    private int gap;
    private int btnHeight;
    private int rowHeight;
    /** 房间内容（玩家列表/底部按钮）使用合适宽度并居中。 */
    private float contentWidth;

    private RenderedTextBlock codeText;
    private RenderedTextBlock saveText;
    private RenderedTextBlock rosterTitle;
    private RenderedTextBlock statusText;
    private Component roster;
    private RedButton btnSave;
    private RedButton btnStart;
    private RedButton btnHero;
    private RedButton btnPassword;
    private RedButton btnWait;
    private StyledButton btnChat;
    private MpGameLog roomLog;
    private int lastUiVersion = -1;
    private String lastCode = null;
    private String lastSave = null;
    private float contentX;
    private float contentW;
    /** 房间号文本的起始 y：竖屏时放到返回/聊天按钮下方，避免遮挡。 */
    private float topTextY;

    @Override
    public void create() {
        super.create();
        PixelScene.uiCamera.visible = false;
        // 从对局返回房间时切回大厅主题音乐；房间内继续沿用大厅音乐
        Music.INSTANCE.play(Assets.Music.THEME, true);

        int w = Camera.main.width;
        int h = Camera.main.height;
        margin = Math.max(1, Math.round(MpUi.sideMargin()));
        gap = MpUi.i(4);
        btnHeight = MpUi.i(30);
        rowHeight = MpUi.i(54);
        // 竖屏铺满屏幕宽度，横屏保持原设计宽度
        contentWidth = MpUi.contentWidth(260f);

        Archs archs = new Archs();
        archs.setSize(w, h);
        addToBack(archs);

        ExitButton back = new ExitButton() {
            @Override
            protected void onClick() {
                backPressed();
            }
        };
        back.setPos(0, 0);
        add(back);

        // 房间内聊天入口：放在返回键右侧，房间内没有其他玩家时置灰
        btnChat = new StyledButton(Chrome.Type.GREY_BUTTON_TR, "") {
            @Override
            protected void onClick() {
                Mp.get().openChat();
            }
        };
        btnChat.icon(MpChatWindow.chatIcon());
        btnChat.setRect(back.right() + 2f, 0f, 20f, 20f);
        add(btnChat);

        // 竖屏缩放后房间号文本会变高，放到顶部按钮下方，避免遮住返回/聊天按钮；
        // 横屏保持原来的 26 设计像素位置。
        topTextY = Math.max(MpUi.px(26f), back.bottom() + MpUi.px(2f));

        contentW = contentWidth;
        contentX = (w - contentW) / 2f;

        codeText = PixelScene.renderTextBlock(MpUi.font(14));
        codeText.maxWidth((int) contentW);
        add(codeText);

        saveText = PixelScene.renderTextBlock(MpUi.font(11));
        saveText.maxWidth((int) contentW);
        add(saveText);

        rosterTitle = PixelScene.renderTextBlock(MpUi.font(11));
        rosterTitle.text(Messages.get(MpRoomScene.class, "roster_title"));
        rosterTitle.alpha(0.85f);
        add(rosterTitle);

        roster = new Component();
        add(roster);

        float buttonsY = h - btnHeight - MpUi.px(8f);
        float inner = contentW;

        if (Mp.get().isHost()) {
            float bw = (inner - gap * 3) / 4f;
            btnSave = new RedButton(Messages.get(MpRoomScene.class, "choose_save"), MpUi.font(10)) {
                @Override
                protected void onClick() {
                    Mp.get().beginRoomSaveSelect();
                    StartScene.openRoomSelect();
                }
            };
            add(btnSave);
            btnSave.setRect(contentX, buttonsY, bw, btnHeight);

            btnStart = new RedButton(Messages.get(MpRoomScene.class, "start_game"), MpUi.font(12)) {
                @Override
                protected void onClick() {
                    if (!Mp.get().startGameFromRoom()) {
                        ShatteredPixelDungeon.scene().addToFront(new WndMessage(Mp.get().status));
                    }
                }
            };
            add(btnStart);
            btnStart.setRect(contentX + bw + gap, buttonsY, bw, btnHeight);

            btnHero = new RedButton(Messages.get(MpRoomScene.class, "choose_hero"), MpUi.font(10)) {
                @Override
                protected void onClick() {
                    chooseHero();
                }
            };
            add(btnHero);
            btnHero.setRect(contentX + (bw + gap) * 2, buttonsY, bw, btnHeight);

            btnPassword = new RedButton(Messages.get(MpRoomScene.class, "set_password"), MpUi.font(10)) {
                @Override
                protected void onClick() {
                    openPasswordDialog();
                }
            };
            add(btnPassword);
            btnPassword.setRect(contentX + (bw + gap) * 3, buttonsY, bw, btnHeight);
        } else {
            float heroW = Math.min(inner * 0.40f, MpUi.px(110f));
            btnWait = new RedButton(Messages.get(MpRoomScene.class, "waiting_start"), MpUi.font(11)) {
                @Override
                protected void onClick() {
                    if (Mp.get().canGuestEnterFromRoom()) {
                        Mp.get().guestEnterFromRoom();
                    }
                }
            };
            btnWait.enable(false);
            add(btnWait);
            btnWait.setRect(contentX, buttonsY, inner - heroW - gap, btnHeight);

            btnHero = new RedButton(Messages.get(MpRoomScene.class, "choose_hero"), MpUi.font(11)) {
                @Override
                protected void onClick() {
                    chooseHero();
                }
            };
            add(btnHero);
            btnHero.setRect(contentX + inner - heroW, buttonsY, heroW, btnHeight);
        }

        statusText = PixelScene.renderTextBlock(MpUi.font(9));
        statusText.maxWidth((int) contentW);
        statusText.setPos(contentX, buttonsY - MpUi.px(22f));
        statusText.alpha(0.75f);
        add(statusText);

        // 房间左下角联机日志：放在按钮行上方、状态文字上方，最新消息在底部
        roomLog = new MpGameLog(0f, buttonsY - MpUi.px(30f), 3);
        addToFront(roomLog);

        refreshAll();
        fadeIn();
    }

    @Override
    public void update() {
        super.update();
        if (!Mp.get().isInRoom()) {
            if (Mp.get().role == Mp.Role.NONE) {
                ShatteredPixelDungeon.switchNoFade(MpLobbyScene.class);
            }
            return;
        }
        Mp.get().tickRoom();
        if (Mp.get().getRoomUiVersion() != lastUiVersion) {
            lastUiVersion = Mp.get().getRoomUiVersion();
            refreshAll();
        } else {
            refreshButtons();
        }
        if (statusText != null && !Mp.get().status.equals(statusText.text())) {
            statusText.text(Mp.get().status);
        }
    }

    @Override
    protected void onBackPressed() {
        backPressed();
    }

    private void backPressed() {
        if (Mp.get().isHost()) {
            ShatteredPixelDungeon.scene().addToFront(new WndOptions(
                    Messages.get(MpRoomScene.class, "leave_title"),
                    Messages.get(MpRoomScene.class, "leave_body"),
                    Messages.get(MpRoomScene.class, "leave_ok"),
                    Messages.get(Mp.class, "cancel")) {
                @Override
                protected void onSelect(int index) {
                    if (index == 0) {
                        leaveRoom();
                    }
                }
            });
        } else {
            leaveRoom();
        }
    }

    private void leaveRoom() {
        Mp.get().clearReconnect();
        Mp.get().disconnect();
        ShatteredPixelDungeon.switchNoFade(MpLobbyScene.class);
    }

    private void refreshAll() {
        lastCode = Mp.get().getRoomCode();
        refreshCode();
        refreshSave();
        rebuildRoster();
        refreshButtons();
    }

    private void refreshCode() {
        if (codeText == null) return;
        String code = lastCode;
        if (code == null) {
            codeText.text(Messages.get(MpRoomScene.class, "room_code_creating"));
        } else {
            codeText.text(Messages.get(MpRoomScene.class, "room_code", code));
        }
        // RenderedTextBlock 的 width 在首次 layout 后才有效；先 setPos 触发 layout 再居中
        codeText.setPos(0, topTextY);
        codeText.setPos(contentX + (contentW - codeText.width()) / 2f, topTextY);
        align(codeText);
    }

    private void refreshSave() {
        if (saveText == null) return;
        Mp.RoomSaveInfo save = Mp.get().getRoomSave();
        String text;
        if (!save.selected) {
            text = Messages.get(MpRoomScene.class, "save_none");
        } else if (save.isNew) {
            text = Messages.get(MpRoomScene.class, "save_new", seedText(save.seed));
        } else {
            text = Messages.get(MpRoomScene.class, "save_existing",
                    save.slot, save.depth, classTitle(save.hostClass));
        }
        lastSave = text;
        saveText.text(text);
        saveText.setPos(0, codeText.bottom() + MpUi.px(7f));
        saveText.setPos(contentX + (contentW - saveText.width()) / 2f,
                codeText.bottom() + MpUi.px(7f));
        align(saveText);
    }

    private void rebuildRoster() {
        if (roster != null) {
            remove(roster);
            roster.destroy();   // 同时销毁行内的 PointerArea，避免关闭/刷新后残留点击区
        }
        roster = new Component();
        add(roster);
        List<Mp.RoomPlayerInfo> list = Mp.get().getRoomRoster();
        rosterTitle.setPos(0, 0);   // 触发 layout 计算 width/height
        rosterTitle.setPos(contentX + (contentW - rosterTitle.width()) / 2f,
                saveText.bottom() + MpUi.px(14f));
        align(rosterTitle);
        float top = rosterTitle.bottom() + MpUi.px(8f);
        float y = top;
        for (Mp.RoomPlayerInfo info : list) {
            RosterRow row = new RosterRow(info);
            // noosa 的 Group 不会用自身 x/y 平移子元素，行必须用绝对坐标
            row.setRect(contentX, y, contentW, rowHeight);
            roster.add(row);
            y += rowHeight + gap;
        }
    }

    private void refreshButtons() {
        if (btnChat != null) {
            btnChat.enable(Mp.get().connected);
        }
        Mp.RoomSaveInfo save = Mp.get().getRoomSave();
        if (btnStart != null) {
            boolean can = Mp.get().canHostStartGame();
            btnStart.enable(can);
            String text = Messages.get(MpRoomScene.class,
                    (save.started || (!save.isNew && save.depth > 0))
                            ? "continue_game" : "start_game");
            if (!text.equals(btnStart.text())) {
                btnStart.text(text);
            }
        }
        if (btnWait != null) {
            boolean canEnter = Mp.get().canGuestEnterFromRoom();
            btnWait.enable(canEnter);
            String text;
            if (canEnter) {
                text = Messages.get(MpRoomScene.class, "enter_game");
            } else if (!save.selected) {
                text = Messages.get(MpRoomScene.class, "waiting_save");
            } else if (save.started || (!save.isNew && save.depth > 0)) {
                text = Messages.get(MpRoomScene.class, "waiting_continue");
            } else {
                text = Messages.get(MpRoomScene.class, "waiting_start");
            }
            if (!text.equals(btnWait.text())) {
                btnWait.text(text);
            }
        }
    }

    private void chooseHero() {
        if (Mp.get().isHost()) {
            Mp.RoomSaveInfo save = Mp.get().getRoomSave();
            if (save.selected && !save.isNew) {
                ShatteredPixelDungeon.scene().addToFront(new WndMessage(
                        Messages.get(MpRoomScene.class, "hero_locked")));
                return;
            }
            WndHeroClassSelect w = new WndHeroClassSelect(cls -> {
                if (cls != null) Mp.get().hostChooseRoomHero(cls);
            });
            ShatteredPixelDungeon.scene().addToFront(w);
        } else {
            if (Mp.get().isGuestHeroLocked()) {
                ShatteredPixelDungeon.scene().addToFront(new WndMessage(
                        Messages.get(MpRoomScene.class, "hero_locked")));
                return;
            }
            WndHeroClassSelect w = new WndHeroClassSelect(cls -> {
                if (cls != null) Mp.get().guestChooseRoomHero(cls);
            });
            ShatteredPixelDungeon.scene().addToFront(w);
        }
    }

    /** 房主设置/清除房间密码（留空=无密码）。 */
    private void openPasswordDialog() {
        if (!Mp.get().isHost()) return;
        ShatteredPixelDungeon.scene().addToFront(new WndTextInput(
                Messages.get(MpRoomScene.class, "password_title"),
                Messages.get(MpRoomScene.class, "password_prompt"),
                Mp.get().getRoomPassword(), 32, false,
                Messages.get(Mp.class, "ok"),
                Messages.get(Mp.class, "cancel")) {
            @Override
            public void onSelect(boolean positive, String text) {
                if (!positive) return;
                Mp.get().setRoomPassword(text);
                ShatteredPixelDungeon.scene().addToFront(new WndMessage(
                        Messages.get(MpRoomScene.class,
                                Mp.get().getRoomPassword().isEmpty()
                                        ? "password_cleared" : "password_set")));
            }
        });
    }

    /** 点击玩家行：打开个人主页（自己是可编辑，他人只读）。 */
    private void openProfile(int playerId) {
        if (Mp.get().getRoomPlayerProfile(playerId) == null) return;
        MpProfileScene.open(playerId, Mp.get().isSelfProfileId(playerId), MpRoomScene.class);
    }

    private static String classTitle(HeroClass cls) {
        return cls == null
                ? Messages.get(MpRoomScene.class, "no_hero")
                : Messages.titleCase(cls.title());
    }

    private static String seedText(long seed) {
        try {
            return DungeonSeed.convertToCode(seed);
        } catch (IllegalArgumentException e) {
            return String.valueOf(seed);
        }
    }

    /** 玩家列表一行：职业小图 + 名字 + 佩戴徽章 + 管理员/房主标记；整行可点击。 */
    private class RosterRow extends Component {
        private final Mp.RoomPlayerInfo info;
        private NinePatch bg;
        private Image hero;
        private final List<Image> badgeIcons = new ArrayList<>();
        private RenderedTextBlock name;
        private RenderedTextBlock tag;
        private RenderedTextBlock adminTag;
        private RenderedTextBlock cls;
        private PointerArea clickArea;

        RosterRow(Mp.RoomPlayerInfo info) {
            this.info = info;
            // Component 的父类构造会先调用 createChildren()，此时 info 尚未赋值；
            // 英雄小图/名字/职业/徽章/标记在构造体里创建和填充。
            name.text(info.name);
            name.maxWidth(MpUi.i(220f));
            cls.text(classTitle(info.heroClass));
            if (info.heroClass != null) {
                hero = new Image(info.heroClass.spritesheet(), 0, 0, 12, 15);
                hero.scale.set(MpUi.scale());
                add(hero);
            }
            if (info.badges != null) {
                for (Badges.Badge badge : info.badges) {
                    if (badge == null) continue;
                    Image icon = BadgeBanner.image(badge.image);
                    icon.scale.set(1.25f * MpUi.scale());
                    add(icon);
                    badgeIcons.add(icon);
                }
            }
            if (info.isHost) {
                tag = PixelScene.renderTextBlock(MpUi.font(11));
                tag.text(Messages.get(MpRoomScene.class, "host_tag"));
                tag.maxWidth(MpUi.i(64f));
                tag.hardlight(0xFF7777);
                add(tag);
            }
            if (info.isAdmin && !info.isHost) {
                adminTag = PixelScene.renderTextBlock(MpUi.font(11));
                adminTag.text(Messages.get(MpRoomScene.class, "admin_tag"));
                adminTag.maxWidth(MpUi.i(64f));
                adminTag.hardlight(0xFFD700);
                add(adminTag);
            }
            clickArea = new PointerArea(0, 0, 0, 0) {
                @Override
                protected void onClick(PointerEvent event) {
                    openProfile(info.id);
                }
            };
            add(clickArea);
        }

        @Override
        protected void createChildren() {
            bg = Chrome.get(Chrome.Type.GEM);
            add(bg);

            name = PixelScene.renderTextBlock(MpUi.font(12));
            add(name);

            cls = PixelScene.renderTextBlock(MpUi.font(11));
            add(cls);
        }

        @Override
        protected void layout() {
            bg.x = x;
            bg.y = y;
            bg.size(width, height);
            clickArea.x = x;
            clickArea.y = y;
            clickArea.width = width;
            clickArea.height = height;

            float textX = x + (hero == null ? MpUi.px(10f) : MpUi.px(38f));
            if (hero != null) {
                hero.x = x + MpUi.px(10f);
                hero.y = y + (height - hero.height()) / 2f;
                PixelScene.align(hero);
            }

            // 右侧固定槽位：徽章模块 3 格 + 身份标记槽，保证所有玩家行徽章起点一致
            float rightEdge = x + width - MpUi.px(8f);
            float tagSlotW = MpUi.px(64f);
            float tagSlotLeft = rightEdge - tagSlotW;
            RenderedTextBlock roleTag = tag != null ? tag : adminTag;
            if (roleTag != null) {
                roleTag.setPos(0, 0);   // 触发 layout 计算 width/height
                roleTag.setPos(tagSlotLeft + (tagSlotW - roleTag.width()) / 2f,
                        y + (height - roleTag.height()) / 2f);
            }

            float badgeW = MpUi.px(20f);
            float badgeGap = MpUi.px(4f);
            float badgeModuleW = badgeW * 3 + badgeGap * 2;
            float badgeStart = tagSlotLeft - MpUi.px(8f) - badgeModuleW;
            for (int i = 0; i < badgeIcons.size() && i < 3; i++) {
                Image icon = badgeIcons.get(i);
                icon.x = badgeStart + i * (badgeW + badgeGap);
                icon.y = y + (height - icon.height()) / 2f;
                PixelScene.align(icon);
            }

            float reserved = rightEdge - badgeStart;
            name.maxWidth((int) Math.max(MpUi.px(40f), width - (textX - x) - reserved - MpUi.px(6f)));
            name.setPos(0, 0);
            cls.setPos(0, 0);
            float groupHeight = name.height() + MpUi.px(3f) + cls.height();
            float top = y + Math.max(MpUi.px(2f), (height - groupHeight) / 2f);
            name.setPos(textX, top);
            cls.setPos(textX, name.bottom() + MpUi.px(3f));
        }
    }
}
