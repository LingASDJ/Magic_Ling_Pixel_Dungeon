package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spellsoword.MagicPower;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.HerbBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ImperialExorcism;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Rapier;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MagicFlyBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingSpike;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 联机核心：房主/访客状态机、帧协议与多人同步。
 * 房间由房主权威模拟，访客只同步位置/职业/聊天。
 */
public class Mp {

    /** 全局单例（外部通过 {@link #get()} 访问）。 */
    private static final Mp I = new Mp();

    /**
     * 获取联机单例。
     *
     * @return 返回值。
     */
    public static Mp get() {
        // skipcq: JAVA-S1049  联机状态机是全局可变单例，调用方均为本模组内部代码
        return I;
    }

    /** 默认联机端口。 */
    public static final int DEFAULT_PORT = 28771;
    /** 最大人数：1 房主 + 3 访客（推荐上限，理由见 PROGRESS §10 多人适配） */
    public static final int MAX_PLAYERS = 4;

    /**
     * 联机协议版本。线协议（帧类型/载荷格式）改动时必须递增；
     * 双方版本不一致会拒绝连接，避免旧版本用不兼容的存档/协议混连。
     */
    public static final int MP_PROTOCOL_VERSION = 2;

    /** 连续多少个 PING 收不到 PONG 就判定连接中断（每 3 秒一次，约 15 秒） */
    private static final int PING_MISS_LIMIT = 5;

    /** 版本握手超时（毫秒）：超时视为对方版本过旧或未完成握手。 */
    private static final long HANDSHAKE_TIMEOUT_MS = 5000L;

        /** 联机角色：未连接 / 房主 / 访客。 */
    public enum Role { NONE, HOST, CLIENT }

        /** 当前角色。 */
    public volatile Role role = Role.NONE;
        /** 联机状态文本（联机窗口显示）。 */
    public volatile String status = Messages.get(Mp.class, "status_disconnected");
        /** 是否已连接（任一对端在线）。 */
    public volatile boolean connected = false;
        /** 最近一个 HELLO 的对端名字（兼容 1v1 显示）。 */
    public volatile String peer = Messages.get(Mp.class, "default_player_name");   // 兼容 1v1 显示：最近一个 HELLO 的名字

    /** 房主自己的 id；访客 id 由房主连接时分配（1..N） */
    public static final int HOST_ID = 0;

    private ServerSocket server;
    private Thread worker;
    private volatile boolean running = false;
    private int nextGuestId = 1;
    private volatile int myId = -1;         // 访客侧：房主分配的 id

    /** 一个对端连接：房主侧有 N-1 个访客 peer，访客侧有房主 peer(0) 与其他访客 peer（按 id） */
        /** 一个对端连接：房主侧为访客，访客侧为房主与其他访客。 */
    public static class Peer {
        int id = -1;
        Socket sock;
        DataInputStream in;
        DataOutputStream out;
        volatile String name = Messages.get(Mp.class, "default_player_name");
        /** 对端联机协议版本（-1=尚未握手）。 */
        int protocolVersion = -1;
        /** 对端游戏版本号（manifest/Android versionCode）。 */
        int gameVersionCode = 0;
        /** 对端游戏版本名（如 0.9.7.2）。 */
        String gameVersion = "";
        /** 是否已完成版本握手且双方兼容。 */
        volatile boolean versionOk = false;
        /** 是否已处理过 VERSION（成功或失败）。 */
        volatile boolean versionChecked = false;
        /** 版本不兼容/握手超时的具体原因（断开时保留给玩家看）。 */
        volatile String versionError = null;
        HeroClass heroClass;                 // 对端职业（房主侧=访客的 HERO 帧；访客侧=房主 STATE 里捕获）
        boolean gotState = false;            // 房主侧：是否已给该访客发过 STATE
        Hero remote;
        HeroSprite remoteSprite;
        RenderedTextBlock nameLabel;         // 幽灵正上方的名字
        int remotePos = -1;
        int remoteDepth = -1;
        int remoteBranch = -1;
        boolean remoteStale = false;
        volatile boolean kicked = false;     // 房主侧：已被 /kick 请离
        volatile int rttMs = -1;             // 房主侧：到该访客的延迟（-1=未知）
        volatile boolean pingTimeout = false;
        /** 连续多少个 PING 没有回应（用于判定网络断开）。 */
        final AtomicInteger pingMisses = new AtomicInteger(0);
        volatile int pingToken = 0;
        volatile long pingSentAt = 0L;
        boolean joinedGame = false;          // 已播报过「加入了游戏」（首次位置同步时置位）
    }

    /** /list 的一行数据 */
        /** /list 的一行玩家信息。 */
    public static class PlayerInfo {
        /** 标识 id。 */
        public int id;
        /** 玩家名。 */
        public String name;
        /** 职业。 */
        public HeroClass heroClass;
        /** 是否房主。 */
        public boolean isHost;
        /** 是否自己。 */
        public boolean isSelf;
        /** 到该对端的延迟（毫秒，-1=未知）。 */
        public int rttMs = -1;
        /** 是否超时。 */
        public boolean timeout;
    }

    /** 房主广播给访客的延迟信息（访客 /list 用来显示其他访客的延迟） */
        /** RTT_INFO 中的一条延迟记录。 */
    public static class RttRec {
        /** 标识 id。 */
        public int id;
        /** 到该对端的延迟（毫秒，-1=未知）。 */
        public int rttMs = -1;
        /** 是否超时。 */
        public boolean timeout;
    }

        /** RTT_INFO 帧载荷。 */
    public static class RttInfoPayload {
        /** 延迟记录列表。 */
        public List<RttRec> recs;
    }

    private final List<Peer> peers = new ArrayList<>();

    private GameScene lastScene;

    private int lastSentPos = -1;
    private long lastSendTime = 0L;
    private long lastHeroSave = 0L;
    private long lastPingTime = 0L;
    private volatile boolean kickedByHost = false;

    private StatePayload pendingState;        // 选职业期间暂存的最新 STATE
    private WndHeroClassSelect classWindow;   // 职业选择窗口（断连时自动关掉）
        /** 玩家名持久化文件名。 */
    public static final String MP_NAME_FILE = "mp_name.txt";

    private long worldSeed = Long.MIN_VALUE;  // 当前世界的种子：访客英雄按世界分档

    /** 访客联机临时世界槽位：故意放在普通存档 1..MAX_SLOTS 之外，主界面永远不会列出 */
    private static final int GUEST_TEMP_SLOT = 99;
    /** 当前 Dungeon.hero 是否属于已进入的联机世界（防止把单人或过期英雄写入访客档） */
    private boolean guestWorldActive = false;
    /** 访客英雄档存在但读不出来：首次加入前先备份旧档，避免直接覆盖丢数据 */
    private boolean guestHeroUnreadable = false;

    private String myName;                    // 本机玩家名（联机窗口可设置，mp_name.txt 持久化）
        /** 聊天输入框是否打开（防止按键重入）。 */
    public volatile boolean chatOpen = false;
    private StyledButton chatButton;          // 局内聊天按钮（Mp 自己挂在 GameScene 上）

    // ---------------------------------------------------------------- 开房

    /**
     * 开房：在指定端口监听，等待访客加入（房主权威）。
     *
     * @param port 参数。
     */
    public synchronized void host(int port) {
        disconnect();
        role = Role.HOST;
        status = Messages.get(Mp.class, "status_hosting", port);
        running = true;
        worker = new Thread(() -> {
            try {
                // skipcq: JAVA-S1011  局域网游戏数据走明文 TCP，不传账号/敏感信息；TLS 会破坏联机协议兼容性
                server = new ServerSocket(port);
                status = Messages.get(Mp.class, "status_host_waiting", localIp(), port);
                while (running) {
                    Socket s = server.accept();
                    synchronized (peers) {
                        if (peers.size() >= MAX_PLAYERS - 1) {
                            // 先告诉对方"房间已满"，再优雅断开。
                            // 不能直接 close：客户端已发 HELLO，socket 接收缓冲有未读数据时
                            // close 容易发 RST，把刚写的 FULL 帧一起丢掉，客户端只看到"连接中断"。
                            try {
                                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                                writeFrame(out, Proto.FULL, new byte[0]);
                                out.flush();
                                try { s.shutdownOutput(); } catch (IOException ignored) {}
                                try {
                                    s.setSoTimeout(500);
                                    java.io.InputStream in = s.getInputStream();
                                    byte[] buf = new byte[512];
                                    long deadline = System.currentTimeMillis() + 500L;
                                    while (System.currentTimeMillis() < deadline) {
                                        int n;
                                        try { n = in.read(buf); }
                                        catch (java.net.SocketTimeoutException te) { break; }
                                        if (n < 0) break;
                                    }
                                } catch (Throwable ignored) {}
                            } catch (IOException ignored) {}
                            try { s.close(); } catch (IOException ignored) {}
                            status = Messages.get(Mp.class, "status_room_full_host", MAX_PLAYERS);
                            continue;
                        }
                        Peer p = setupPeer(s);
                        p.id = nextGuestId++;
                        peers.add(p);
                        sendTo(p, Proto.HELLO, getMyName().getBytes("UTF-8"));   // 房主也把自己的名字发过去
                        try { sendTo(p, Proto.ASSIGN, encodeInt(p.id)); } catch (IOException ignored) {}
                        try { sendTo(p, Proto.VERSION, encodeVersion()); } catch (IOException ignored) {}
                        scheduleHandshakeTimeout(p);
                        connected = true;
                        status = Messages.get(Mp.class, "status_players_online", peers.size());
                    }
                }
            } catch (Throwable t) {
                if (running) status = Messages.get(Mp.class, "status_host_failed", t.getMessage());
            }
        }, "mp-host");
        worker.setDaemon(true);
        worker.start();
    }

    // ---------------------------------------------------------------- 加入

    /**
     * 加入房间：支持 address 或 address:port，等待房主 STATE 后进入关卡。
     *
     * @param address 参数。
     * @param defaultPort 参数。
     */
    public synchronized void join(String address, int defaultPort) {
        disconnect();
        role = Role.CLIENT;
        status = Messages.get(Mp.class, "status_connecting", address);
        running = true;
        worker = new Thread(() -> {
            try {
                String host = address.trim();
                int port = defaultPort;
                int idx = host.lastIndexOf(':');
                if (idx > 0) {
                    port = Integer.parseInt(host.substring(idx + 1).trim());
                    host = host.substring(0, idx).trim();
                }
                if (host.isEmpty()) throw new IOException(Messages.get(Mp.class, "error_empty_address"));
                // skipcq: JAVA-S1011  同上：联机协议为明文 TCP，与房主端保持一致
                Socket s = new Socket();
                s.connect(new InetSocketAddress(host, port), 8000);
                synchronized (peers) {
                    Peer hp = setupPeer(s);
                    hp.id = HOST_ID;   // 访客侧唯一固定的房主 peer
                    peers.add(hp);
                    scheduleHandshakeTimeout(hp);
                }
                connected = true;
                sendToAll(Proto.HELLO, getMyName().getBytes("UTF-8"));
                sendToAll(Proto.VERSION, encodeVersion());
                status = Messages.get(Mp.class, "status_connected_waiting");
            } catch (Throwable t) {
                String detail = (t.getMessage() != null && !t.getMessage().isEmpty())
                        ? t.getMessage() : t.getClass().getSimpleName();
                final String msg = Messages.get(Mp.class, "msg_connect_failed", detail);
                status = msg;
                connected = false;
                running = false;   // 连接失败：让版本握手超时线程直接退出，避免 5 秒后覆盖失败原因
                Game.runOnRenderThread(() -> {
                    if (ShatteredPixelDungeon.scene() != null) {
                        ShatteredPixelDungeon.scene().add(new WndMessage(msg));
                    }
                });
            }
        }, "mp-join");
        worker.setDaemon(true);
        worker.start();
    }

    private Peer setupPeer(Socket s) throws IOException {
        Peer p = new Peer();
        p.sock = s;
        s.setTcpNoDelay(true);
        p.in = new DataInputStream(s.getInputStream());
        p.out = new DataOutputStream(s.getOutputStream());
        Thread recv = new Thread(() -> recvLoop(p), "mp-recv");
        recv.setDaemon(true);
        recv.start();
        return p;
    }

    /** 版本握手超时：对方在 HANDSHAKE_TIMEOUT_MS 内没有发来兼容的 VERSION 就断开。 */
    private void scheduleHandshakeTimeout(final Peer p) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(HANDSHAKE_TIMEOUT_MS);
            } catch (InterruptedException e) {
                return;
            }
            synchronized (p) {
                if (!running || p.versionChecked) return;
                p.versionChecked = true;   // 超时也标记为已处理，避免重复关闭
            }
            if (role == Role.CLIENT && p.id == HOST_ID) {
                p.versionError = Messages.get(Mp.class, "version_timeout_host");
                handleHostLost(p.versionError);
            } else if (role == Role.HOST && p.id > 0) {
                p.versionError = Messages.get(Mp.class, "version_timeout_peer", displayName(p.name));
                status = p.versionError;
                try { if (p.sock != null) p.sock.close(); } catch (IOException ignored) {}
            }
        }, "mp-handshake");
        t.setDaemon(true);
        t.start();
    }

    /**
     * 断开当前连接，清理所有 peer、远端幽灵与临时状态。
     */
    public synchronized void disconnect() {
        if (role == Role.CLIENT) {
            // 断开前先保存访客英雄；只有确认保存成功，才清理临时世界
            boolean ok = !guestWorldActive || saveGuestHero();
            if (ok) clearGuestTempSlot();
            guestWorldActive = false;
            guestHeroUnreadable = false;
            worldSeed = Long.MIN_VALUE;
        }
        kickedByHost = false;
        lastPingTime = 0L;
        chatOpen = false;
        pendingState = null;
        final WndHeroClassSelect w = classWindow;
        classWindow = null;
        if (w != null) {
            Game.runOnRenderThread(w::hide);        // 选职业期间断连，顺手把窗口关掉
        }
        running = false;
        connected = false;
        myId = -1;
        nextGuestId = 1;
        lastSentPos = -1;
        synchronized (peers) {
            for (Peer p : peers) {
                try { if (p.sock != null) p.sock.close(); } catch (IOException ignored) {}
            }
            peers.clear();
        }
        try { if (server != null) server.close(); } catch (IOException ignored) {}
        try { if (worker != null) worker.interrupt(); } catch (Throwable ignored) {}
        server = null;
        lastScene = null;
        role = Role.NONE;
        status = Messages.get(Mp.class, "status_disconnected");
    }

    // ---------------------------------------------------------------- 收包

    private void recvLoop(Peer p) { // skipcq: JAVA-R1000 联机收包主循环，复杂度高但已实测稳定
        try {
            while (running) {
                Frame f = readFrame(p.in);
                onFrame(p, f.type, f.data);
            }
        } catch (Throwable t) {
            if (running) {
                boolean hostLost = (role == Role.CLIENT && p.id == HOST_ID);
                synchronized (peers) { peers.remove(p); }
                try { if (p.sock != null) p.sock.close(); } catch (IOException ignored) {}
                // 访客侧：host peer 断开就是连接结束，不能被其他访客占位 peer 骗成"还连着"
                connected = hostLost ? false : !peers.isEmpty();
                if (p.versionError != null) {
                    // 版本不匹配/握手超时：保留具体原因，不覆盖成通用的“断线”提示
                    status = p.versionError;
                } else if (hostLost) {
                    status = Messages.get(Mp.class, "reason_host_lost");
                } else if (role == Role.CLIENT && kickedByHost) {
                    status = Messages.get(Mp.class, "reason_kicked");
                } else if (p.kicked) {
                    status = Messages.get(Mp.class, "status_kicked_peer", displayName(p.name));
                } else if (isHost() && p.id > 0) {
                    // 房主侧：有访客断线（主动退出/关游戏/网络中断），所有在场玩家都提示
                    systemNotice(Messages.get(Mp.class, "notice_player_left", displayName(p.name)));
                } else {
                    status = Messages.get(Mp.class,
                            connected ? "status_peer_disconnected" : "status_peer_disconnected_end",
                            displayName(p.name));
                }
                if (isHost() && p.id > 0) {
                    // 房主自己的画面上也要清掉该玩家的幽灵/名牌（渲染线程操作精灵）
                    Game.runOnRenderThread(() -> {
                        if (p.remoteSprite != null) {
                            p.remoteSprite.killAndErase();
                            p.remoteSprite = null;
                        }
                        if (p.nameLabel != null) {
                            p.nameLabel.killAndErase();
                            p.nameLabel = null;
                        }
                        p.remote = null;
                    });
                    // 广播 LEAVE：其他访客清掉这个玩家的幽灵（被踢的玩家也要清）
                    try { sendToAll(Proto.LEAVE, encodeInt(p.id)); } catch (IOException ignored) {}
                }
                if (hostLost && !kickedByHost && p.versionError == null) {
                    // 房主卡退/关游戏/网络中断：访客保存自己并回标题，弹窗说明原因。
                    // pingTimeout=true 说明断线前已经有一轮 PING 无回应，更可能是网络问题。
                    handleHostLost(Messages.get(Mp.class,
                            p.pingTimeout ? "reason_network" : "reason_host_lost"));
                } else if (role == Role.CLIENT && !connected && !kickedByHost) {
                    // 其他断连情况：访客回标题（被 /kick 的由 KICKED 帧统一处理）
                    Game.runOnRenderThread(() -> {
                        if (Game.scene() instanceof GameScene) {
                            ShatteredPixelDungeon.switchScene(TitleScene.class);
                        }
                    });
                }
            }
        }
    }

    /** 访客侧：房主断开（含网络中断）；保存访客英雄、清理临时世界、回标题并弹出原因 */
    private void handleHostLost(final String reason) {
        Game.runOnRenderThread(() -> {
            if (role != Role.CLIENT) return;
            disconnect();
            showTitleMessage(reason);
        });
    }

    /** 回到标题界面并弹出提示（若已经在标题界面则直接弹） */
    private void showTitleMessage(final String msg) {
        status = msg;
        if (Game.scene() != null && !(Game.scene() instanceof TitleScene)) {
            ShatteredPixelDungeon.switchScene(TitleScene.class, new Game.SceneChangeCallback() {
                @Override
                public void beforeCreate() {}

                @Override
                public void afterCreate() {
                    if (ShatteredPixelDungeon.scene() != null) {
                        ShatteredPixelDungeon.scene().add(new WndMessage(msg));
                    }
                }
            });
        } else if (Game.scene() != null) {
            ShatteredPixelDungeon.scene().add(new WndMessage(msg));
        }
    }

    /** 系统事件提示：写入联机状态栏 + 游戏事件栏（白色），任意线程可用 */
    private void systemNotice(final String text) {
        status = text;
        try {
            Game.runOnRenderThread(() -> GLog.info(text));
        } catch (Throwable t) {
            GLog.info(text);
        }
    }

    /**
     * 版本握手完成前仍允许处理的帧（连接控制/握手，不涉及游戏世界状态）。
     *
     * @param type 帧类型。
     * @return 是否放行。
     */
    private static boolean isPreHandshakeFrame(int type) {
        return type == Proto.HELLO || type == Proto.ASSIGN || type == Proto.VERSION
                || type == Proto.FULL || type == Proto.KICKED
                || type == Proto.KICK || type == Proto.PING || type == Proto.PONG;
    }

    private void onFrame(Peer p, int type, byte[] data) { // skipcq: JAVA-R1000 协议帧分发，复杂度高但已实测稳定
        try {
            if (!p.versionOk && !isPreHandshakeFrame(type)) {
                return;   // 版本握手完成前忽略游戏帧；控制帧仍放行
            }
            switch (type) {
                case Proto.HELLO: {
                    String name = new String(data, "UTF-8");
                    if (name != null && !name.isEmpty()) {
                        p.name = name;
                        peer = name;
                        if (isHost() && p.id > 0) {
                            // 把新玩家的 id+名字广播给其他访客（访客据此渲染幽灵与名牌）
                            sendToOthers(p, Proto.JOIN, encodeJoin(p.id, name));
                        }
                    }
                    break;
                }
                case Proto.VERSION: {
                    synchronized (p) {
                        if (p.versionChecked) return;   // 已超时关闭或重复帧
                        p.versionChecked = true;
                    }
                    VersionPayload vp = decodeVersion(data);
                    p.protocolVersion = vp.protocolVersion;
                    p.gameVersionCode = vp.gameVersionCode;
                    p.gameVersion = vp.gameVersion;
                    if (vp.protocolVersion == MP_PROTOCOL_VERSION
                            && vp.gameVersionCode == Game.versionCode) {
                        p.versionOk = true;
                    } else {
                        p.versionError = Messages.get(Mp.class, "version_mismatch",
                                gameVersionName(), MP_PROTOCOL_VERSION,
                                vp.gameVersion == null || vp.gameVersion.isEmpty()
                                        ? "?" : vp.gameVersion,
                                vp.protocolVersion);
                        if (role == Role.CLIENT) {
                            handleHostLost(p.versionError);
                        } else {
                            status = p.versionError;
                            try { if (p.sock != null) p.sock.close(); } catch (IOException ignored) {}
                        }
                    }
                    break;
                }
                case Proto.STATE: {
                    applyState(data);   // 仅访客会收到
                    break;
                }
                case Proto.POS: {
                    PosPayload pp = readPos(data);
                    if (isHost()) {
                        p.remoteDepth = pp.depth;
                        p.remoteBranch = pp.branch;
                        if (pp.pos > 0) p.remotePos = pp.pos;
                        // 关键：房主把访客位置转发给其他访客，否则访客之间互相看不见
                        sendToOthers(p, Proto.POS, data);
                        // 某访客的第一条位置包 = 他已真正进入游戏（选完职业、载入存档）
                        if (pp.pos > 0 && !p.joinedGame) {
                            p.joinedGame = true;
                            systemNotice(Messages.get(Mp.class, "notice_player_joined", displayName(p.name)));
                        }
                    } else {
                        Peer pp2 = peerFor(pp.id);
                        pp2.remoteDepth = pp.depth;
                        pp2.remoteBranch = pp.branch;
                        if (pp.pos > 0) pp2.remotePos = pp.pos;
                        // 其他访客的第一条位置包（房主转发的 POS）= 他进入了游戏
                        if (pp.pos > 0 && pp.id != HOST_ID && pp.id != myId && !pp2.joinedGame) {
                            pp2.joinedGame = true;
                            systemNotice(Messages.get(Mp.class, "notice_player_joined", displayName(pp2.name)));
                        }
                    }
                    break;
                }
                case Proto.HERO: {
                    HeroPayload hp = decodeHero(data);
                    if (isHost()) {
                        p.heroClass = hp.cls;
                        sendToOthers(p, Proto.HERO, data);   // 转发职业给其他访客
                    } else {
                        peerFor(hp.id).heroClass = hp.cls;
                    }
                    break;
                }
                case Proto.CHAT: {
                    ChatPayload cp = decodeChat(data);
                    if (!cp.text.trim().isEmpty()) {
                        final String line = "[" + cp.name + "] " + cp.text.trim();
                        Game.runOnRenderThread(() -> GLog.pink(line));   // 聊天消息进事件栏（亮紫）
                    }
                    if (isHost()) {
                        sendToOthers(p, Proto.CHAT, data);   // 房主把聊天转发给其他访客
                    }
                    break;
                }
                case Proto.KICK: {
                    Game.runOnRenderThread(() -> {
                        if (Game.scene() instanceof GameScene) {
                            status = Messages.get(Mp.class, "status_host_left_save");
                            ShatteredPixelDungeon.switchScene(TitleScene.class);
                        }
                    });
                    break;
                }
                case Proto.FULL: {
                    // 第 5 名加入者：房主已发送"房间已满"，随后断开
                    final String msg = Messages.get(Mp.class, "msg_room_full", MAX_PLAYERS);
                    Game.runOnRenderThread(() -> {
                        status = msg;
                        if (ShatteredPixelDungeon.scene() != null) {
                            ShatteredPixelDungeon.scene().add(new WndMessage(msg));
                        }
                    });
                    disconnect();
                    break;
                }
                case Proto.KICKED: {
                    // 被房主 /kick：先切回标题（让 GameScene 正常收尾），再断开并提示
                    kickedByHost = true;
                    Game.runOnRenderThread(() -> {
                        if (Game.scene() != null && !(Game.scene() instanceof TitleScene)) {
                            ShatteredPixelDungeon.switchScene(TitleScene.class, new Game.SceneChangeCallback() {
                                @Override
                                public void beforeCreate() {}

                                @Override
                                public void afterCreate() {
                                    disconnect();
                                    showTitleMessage(Messages.get(Mp.class, "reason_kicked"));
                                }
                            });
                        } else {
                            disconnect();
                            showTitleMessage(Messages.get(Mp.class, "reason_kicked"));
                        }
                    });
                    break;
                }
                case Proto.PING: {
                    // 双向：房主测访客，访客也测房主；收到就原样回 PONG
                    try { sendTo(p, Proto.PONG, data); } catch (Throwable ignored) {}
                    break;
                }
                case Proto.PONG: {
                    int token = decodeInt(data);
                    if (token == p.pingToken && p.pingSentAt > 0) {
                        p.rttMs = (int) Math.min(Integer.MAX_VALUE, System.currentTimeMillis() - p.pingSentAt);
                        p.pingTimeout = false;
                        p.pingMisses.set(0);
                    }
                    break;
                }
                case Proto.RTT_INFO: {
                    if (role == Role.CLIENT) {
                        RttInfoPayload rp = decodeRttInfo(data);
                        for (RttRec r : rp.recs) {
                            if (r.id == myId) continue;   // 自己的延迟显示"本机"
                            Peer gp = peerFor(r.id);
                            gp.rttMs = r.rttMs;
                            gp.pingTimeout = r.timeout;
                        }
                    }
                    break;
                }
                case Proto.ASSIGN: {
                    myId = decodeInt(data);   // 访客记住房主分配的 id
                    break;
                }
                case Proto.JOIN: {
                    ChatPayload jp = decodeJoin(data);
                    Peer np = peerFor(jp.id);
                    np.name = jp.name;
                    break;
                }
                case Proto.LEAVE: {
                    final int lid = decodeInt(data);
                    Game.runOnRenderThread(() -> {
                        synchronized (peers) {
                            for (Peer pp : new ArrayList<>(peers)) {
                                if (pp.id != lid) continue;
                                peers.remove(pp);
                                if (pp.remoteSprite != null) {
                                    pp.remoteSprite.killAndErase();
                                    pp.remoteSprite = null;
                                }
                                if (pp.nameLabel != null) {
                                    pp.nameLabel.killAndErase();
                                    pp.nameLabel = null;
                                }
                                pp.remote = null;
                                systemNotice(Messages.get(Mp.class, "notice_player_left", displayName(pp.name)));
                            }
                        }
                    });
                    break;
                }
                default:
                    break;
            }
        } catch (Throwable t) {
            status = Messages.get(Mp.class, "status_data_error", t.getMessage());
        }
    }

    // ---------------------------------------------------------------- 发送

    private void sendTo(Peer p, int type, byte[] payload) throws IOException {
        DataOutputStream o = p.out;
        if (o == null) throw new IOException(Messages.get(Mp.class, "error_not_connected"));
        writeFrame(o, type, payload);
    }

    /** 房主发给所有访客；访客发给房主（自己唯一的 peer） */
    private void sendToAll(int type, byte[] payload) {
        synchronized (peers) {
            for (Peer p : peers) {
                try { sendTo(p, type, payload); } catch (IOException ignored) {}
            }
        }
    }

    /** 房主转发给除发送者外的所有访客 */
    private void sendToOthers(Peer from, int type, byte[] payload) {
        synchronized (peers) {
            for (Peer p : peers) {
                if (p == from) continue;
                try { sendTo(p, type, payload); } catch (IOException ignored) {}
            }
        }
    }

    /** 按 id 找 peer（房主/访客通用；访客侧收到转发帧时可能还没有该 peer） */
    private Peer peerFor(int id) {
        synchronized (peers) {
            for (Peer p : peers) {
                if (p.id == id) return p;
            }
            Peer p = new Peer();
            p.id = id;
            peers.add(p);
            return p;
        }
    }

    /** 客户端收到房主的进度：世界来自房主、英雄换成访客自己的，然后像"继续游戏"一样读档 */
    private void applyState(byte[] data) { // skipcq: JAVA-R1000 应用房主 STATE，复杂度高但已实测稳定
        StatePayload p;
        try {
            p = decodeState(data);
        } catch (IOException e) {
            status = Messages.get(Mp.class, "status_data_error", e.getMessage());
            return;
        }
        try {
            Bundle game = Bundle.read(new ByteArrayInputStream(p.game));
            worldSeed = game.getLong("seed");   // 房主世界的种子（访客英雄按世界分档）
            Bundle hostHero = game.getBundle("hero");
            if (hostHero.contains("class")) {
                // 远端幽灵用房主的外观（Hero.CLASS = "class"）；访客侧 id=0 的 peer 就是房主
                peerFor(HOST_ID).heroClass = hostHero.getEnum("class", HeroClass.class);
            }
            int hostPos = hostHero.getInt("pos");                             // 访客英雄出生在房主当前位置

            Bundle guest = loadGuestHeroBundle();
            if (guest != null && guest.contains("class")) {
                // 已有英雄存档：直接告诉房主自己的职业（房主渲染访客幽灵外观用，并转发给其他访客）
                try {
                    sendToAll(Proto.HERO, encodeHero(myId, guest.getEnum("class", HeroClass.class)));
                } catch (Throwable ignored) {}
            }
            if (guest == null) {
                // 首次加入：先弹职业选择窗口，选定后继续
                if (pendingState == null) {
                    pendingState = p;
                    status = Messages.get(Mp.class, guestHeroUnreadable
                            ? "status_guest_hero_unreadable"
                            : "status_choose_class");
                    final StatePayload fp = p;
                    final int fpos = hostPos;
                    Game.runOnRenderThread(() -> {
                        classWindow = new WndHeroClassSelect(cls -> {
                            try {
                                if (!connected) return;         // 选职业期间断开了
                                // 选职业期间可能又收到更新的 STATE，用最新状态进入，避免落到旧层/旧位置
                                StatePayload latest = pendingState != null ? pendingState : fp;
                                pendingState = null;
                                classWindow = null;
                                if (cls == null) {
                                    disconnect();               // 取消选择 = 取消本次加入
                                    return;
                                }
                                int latestPos = fpos;
                                try {
                                    latestPos = Bundle.read(new ByteArrayInputStream(latest.game))
                                            .getBundle("hero").getInt("pos");
                                } catch (Throwable ignored) {}
                                backupUnreadableGuestHero();   // 旧档读不出来时先改名留底，避免被覆盖
                                Bundle b = newGuestHeroBundle(cls);
                                if (!saveGuestHeroBundle(b)) {
                                    status = Messages.get(Mp.class, "status_save_new_hero_failed");
                                }
                                try {
                                    sendToAll(Proto.HERO, encodeHero(myId, cls));   // 通知房主自己的职业
                                } catch (Throwable ignored) {}
                                mergeAndEnter(latest, latestPos, b);
                            } catch (Throwable t) {
                                status = Messages.get(Mp.class, "status_create_hero_failed", t.getMessage());
                            }
                        });
                        ShatteredPixelDungeon.scene().add(classWindow);
                    });
                } else {
                    pendingState = p;   // 窗口已开，只更新为最新状态
                }
                return;
            }
            mergeAndEnter(p, hostPos, guest);
        } catch (Throwable t) {
            status = Messages.get(Mp.class, "status_data_error", t.getMessage());
        }
    }

    /** 把房主世界 + 访客英雄合并写进存档槽并切场景（渲染线程/网络线程均可调用） */
    private void mergeAndEnter(StatePayload p, int hostPos, Bundle guest) {
        try {
            int depth = p.depth;
            int branch = p.branch;

            // 访客永远使用保留槽：不占用、不覆盖主界面的普通存档（1..MAX_SLOTS）
            int slot = GUEST_TEMP_SLOT;

            // 访客走 Mp → InterlevelScene → GameScene，绕过了 StartScene 的 Journal.loadGlobal()。
            // 不在这里加载的话，GameScene.onPause/destroy 的 Journal.saveGlobal() 会用空状态
            // 覆盖 journal.dat，表现为"联机后日志内容全部消失，徽章仍保留"（徽章在 TitleScene 已加载）。
            Badges.loadGlobal();
            Journal.loadGlobal();

            // 访客端没有跑过 Dungeon.init()，读取英雄背包里的卷轴/药水/戒指前必须先建 handler
            ensureItemHandlers();

            // 用访客英雄字段整体覆盖 game bundle 里嵌套的 "hero"
            Bundle game = Bundle.read(new ByteArrayInputStream(p.game));
            Bundle hostHero = game.getBundle("hero");
            guest.remove("buffs");                       // 不携带上次会话的 buff（可能引用上个世界的对象）
            Hero g = new Hero();
            g.restoreFromBundle(guest);
            g.storeInBundle(hostHero);                   // 覆盖嵌套英雄 bundle 的全部英雄字段
            hostHero.put("pos", hostPos);                // 位置取房主当前位置（访客旧坐标属于上个世界）
            game.remove("placeholders");                 // 房主快捷栏占位指向房主背包物品，清掉防悬空
            game.remove("placements");

            // 房主 bundle 的 generated_levels 记录了房主生成过的所有层，而访客槽位只有当前层文件：
            // 不修正的话访客换层时 levelHasBeenGenerated() 为真，会去读不存在的 depth 文件直接失败。
            // 改成只有当前层"已生成"，其他层届时在访客端重新生成（跨层同步是里程碑 2 的事）。
            game.put("generated_levels", new int[]{depth + 1000 * branch});

            // 新机器可能还没有该槽位的目录，先建出来，否则写档会静默失败
            FileUtils.getFileHandle(GamesInProgress.gameFile(slot)).parent().mkdirs();

            // 清掉该槽位所有旧层档：上个会话/上个世界的残留，留着会导致换层加载出错
            String folder = GamesInProgress.gameFolder(slot);
            for (String file : FileUtils.filesInDir(folder)) {
                if (file.contains("depth")) {
                    FileUtils.deleteFile(folder + "/" + file);
                }
            }

            FileUtils.bundleToFile(GamesInProgress.gameFile(slot), game);
            FileUtils.bundleToFile(GamesInProgress.depthFile(slot, depth, branch), Bundle.read(new ByteArrayInputStream(p.level)));

            // GamesInProgress 有静态缓存 slotStates：写入保留槽后必须清缓存，
            // 否则 InterlevelScene.create() 的 check(curSlot) 可能命中旧值。
            GamesInProgress.setUnknown(slot);

            final int fslot = slot;
            Game.runOnRenderThread(() -> {
                GamesInProgress.curSlot = fslot;
                ActionIndicator.action = null;
                InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
                ShatteredPixelDungeon.switchScene(InterlevelScene.class);
            });
            status = Messages.get(Mp.class, "status_entered_game");
        } catch (Throwable t) {
            status = Messages.get(Mp.class, "status_enter_game_failed", t.getMessage());
        }
    }

    // ---------------------------------------------------------------- 名字与聊天

    /**
     * 本机玩家名：mp_name.txt 持久化，没有就用系统用户名
     *
     * @return 返回值。
     */
    public String getMyName() {
        if (myName != null) return myName;
        try {
            FileHandle f = FileUtils.getFileHandle(MP_NAME_FILE);
            if (f.exists()) {
                String s = f.readString("UTF-8");
                if (s != null && !s.trim().isEmpty()) {
                    myName = s.trim();
                    return myName;
                }
            }
        } catch (Throwable ignored) {}
        myName = peername();
        return myName;
    }

    /**
     * 设置本机玩家名（持久化；已连接时重新通知对方）
     *
     * @param name 参数。
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) return;
        myName = name.trim();
        try {
            FileUtils.getFileHandle(MP_NAME_FILE).writeString(myName, false, "UTF-8");
        } catch (Throwable ignored) {}
        if (connected) {
            try { sendToAll(Proto.HELLO, myName.getBytes("UTF-8")); } catch (Throwable ignored) {}
        }
    }

    /**
     * 发送聊天消息（本地也回显到事件栏，亮紫色）；"/" 开头交给指令处理
     *
     * @param text 参数。
     */
    public void sendChat(String text) {
        if (text == null || text.trim().isEmpty()) return;
        String msg = text.trim();
        if (msg.startsWith("/")) {
            handleCommand(msg);
            return;
        }
        GLog.pink("[" + getMyName() + "] " + msg);
        try { sendToAll(Proto.CHAT, encodeChat(getMyName(), msg)); } catch (Throwable ignored) {}
    }

    /** 聊天指令：/list 所有人可用；/kick 仅房主可用 */
    private void handleCommand(String cmd) {
        String[] parts = cmd.split("\\s+", 2);
        String name = parts[0];
        if (name.equalsIgnoreCase("/list")) {
            Game.runOnRenderThread(() -> {
                if (ShatteredPixelDungeon.scene() != null) {
                    ShatteredPixelDungeon.scene().add(new WndPlayerList());
                }
            });
            return;
        }
        if (name.equalsIgnoreCase("/kick")) {
            if (role != Role.HOST) {
                GLog.n(Messages.get(Mp.class, "cmd_host_only"));
                return;
            }
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                GLog.n(Messages.get(Mp.class, "cmd_kick_usage"));
                return;
            }
            String targetName = parts[1].trim();
            Peer target = null;
            synchronized (peers) {
                for (Peer p : peers) {
                    if (p.id > 0 && targetName.equals(p.name)) { target = p; break; }
                }
            }
            if (target == null) {
                GLog.n(Messages.get(Mp.class, "cmd_kick_not_found", targetName));
                return;
            }
            kickPeer(target);
            return;
        }
        GLog.n(Messages.get(Mp.class,
                role == Role.HOST ? "cmd_unknown_host" : "cmd_unknown_client"));
    }

    /** 房主请离一名访客：发 KICKED 后断开 socket；清理由 recvLoop 统一完成 */
    private void kickPeer(Peer p) {
        p.kicked = true;
        try { sendTo(p, Proto.KICKED, new byte[0]); } catch (Throwable ignored) {}
        try { if (p.sock != null) p.sock.close(); } catch (Throwable ignored) {}
        GLog.n(Messages.get(Mp.class, "cmd_kick_done", displayName(p.name)));
        status = Messages.get(Mp.class, "cmd_kick_done", displayName(p.name));
    }

    /**
     * /list 数据：房主端 = 自己 + 所有访客；访客端 = 房主 + 自己 + 其他访客
     *
     * @return 返回值。
     */
    public List<PlayerInfo> getPlayerList() {
        List<PlayerInfo> out = new ArrayList<>();
        synchronized (peers) {
            Peer hostPeer = null;
            for (Peer p : peers) {
                if (p.id == HOST_ID) { hostPeer = p; break; }
            }

            if (role == Role.HOST) {
                PlayerInfo self = new PlayerInfo();
                self.id = HOST_ID;
                self.name = getMyName();
                self.heroClass = Dungeon.hero != null ? Dungeon.hero.heroClass : null;
                self.isHost = true;
                self.isSelf = true;
                out.add(self);
                for (Peer p : peers) {
                    if (p.id > 0) out.add(toInfo(p, false));
                }
            } else {
                if (hostPeer != null) {
                    PlayerInfo host = toInfo(hostPeer, true);
                    host.isHost = true;
                    out.add(host);
                }
                PlayerInfo self = new PlayerInfo();
                self.id = myId;
                self.name = getMyName();
                self.heroClass = Dungeon.hero != null ? Dungeon.hero.heroClass : null;
                self.isSelf = true;
                out.add(self);
                for (Peer p : peers) {
                    if (p.id > 0 && p.id != myId) out.add(toInfo(p, false));
                }
            }
        }
        return out;
    }

    private PlayerInfo toInfo(Peer p, boolean isHost) {
        PlayerInfo info = new PlayerInfo();
        info.id = p.id;
        info.name = p.name;
        info.heroClass = p.heroClass;
        info.isHost = isHost || p.id == HOST_ID;
        info.rttMs = p.rttMs;
        info.timeout = p.pingTimeout;
        return info;
    }

    /** 联机时在 GameScene 右上角挂"聊天"按钮；每个场景只建一次 */
    private void updateChatButton(GameScene gs) {
        if (chatButton == null) {
            chatButton = new StyledButton(Chrome.Type.GREY_BUTTON_TR,
                    Messages.get(Mp.class, "chat_button"), 8) {
                @Override
                protected void onClick() {
                    openChat();
                }
            };
            chatButton.camera = PixelScene.uiCamera;
            chatButton.setRect(PixelScene.uiCamera.width - 32f, 36f, 32f, 16f);
            gs.addToFront(chatButton);
        }
        chatButton.visible = true;
        chatButton.active = true;
    }

    private void hideChatButton() {
        if (chatButton != null) {
            chatButton.visible = false;
            chatButton.active = false;
        }
    }

    /** T 键 / 聊天按钮：打开聊天输入框（仅在 GameScene 有效） */
    public void openChat() {
        if (!connected || chatOpen) return;
        if (!(Game.scene() instanceof GameScene)) return;
        chatOpen = true;
        Game.runOnRenderThread(() -> {
            try {
                WndTextInput w = new WndTextInput(
                        Messages.get(Mp.class, "chat_title"),
                        Messages.get(Mp.class, "chat_prompt", displayName(peer)),
                        "", 200, false,
                        Messages.get(Mp.class, "chat_send"),
                        Messages.get(Mp.class, "chat_cancel")) {
                    @Override
                    public void onSelect(boolean positive, String text) {
                        chatOpen = false;
                        if (positive) sendChat(text);
                    }
                };
                ShatteredPixelDungeon.scene().addToFront(w);
                status = Messages.get(Mp.class, "chat_open");
            } catch (Throwable t) {
                chatOpen = false;
                status = Messages.get(Mp.class, "chat_open_failed", t.getMessage());
            }
        });
    }

    // ---------------------------------------------------------------- 访客英雄独立存档

    /** 访客英雄按"世界种子"分档的文件名：房主开新档=新种子=新英雄；回旧档=恢复旧英雄 */
    private String mpHeroFile() {
        if (worldSeed == Long.MIN_VALUE) return null;   // 还没收到房主世界，禁止写"无效种子"档
        return "mp_hero_" + worldSeed + ".dat";
    }

    /** 访客英雄专用存档：存在就读，不存在返回 null（由 applyState 弹职业选择窗口） */
    private Bundle loadGuestHeroBundle() {
        guestHeroUnreadable = false;
        String file = mpHeroFile();
        if (file == null) return null;
        FileHandle f = FileUtils.getFileHandle(file);
        if (!f.exists()) {
            Bundle bak = loadGuestHeroBackup(file);
            if (bak != null) return bak;
            return null;
        }
        try {
            Bundle b = FileUtils.bundleFromFile(file);
            if (b != null && b.contains("class")) return b;
            guestHeroUnreadable = true;
            status = Messages.get(Mp.class, "guest_hero_missing_class");
        } catch (Throwable t) {
            guestHeroUnreadable = true;
            status = Messages.get(Mp.class, "guest_hero_read_failed", t.getMessage());
        }
        // 主文件坏了就尝试上一次写入前的备份，避免直接把玩家判成"首次加入"
        Bundle bak = loadGuestHeroBackup(file);
        if (bak != null) return bak;
        return null;
    }

    /** 主文件缺失/损坏时尝试 .bak 副本 */
    private Bundle loadGuestHeroBackup(String file) {
        try {
            FileHandle bak = FileUtils.getFileHandle(file + ".bak");
            if (!bak.exists()) return null;
            Bundle b = FileUtils.bundleFromFile(file + ".bak");
            if (b != null && b.contains("class")) {
                status = Messages.get(Mp.class, "guest_hero_restored");
                return b;
            }
        } catch (Throwable ignored) {}
        return null;
    }

    /** 旧档读不出来时，先改名留底，避免新英雄把它直接覆盖 */
    private void backupUnreadableGuestHero() {
        if (!guestHeroUnreadable) return;
        String file = mpHeroFile();
        if (file == null) return;
        try {
            FileHandle f = FileUtils.getFileHandle(file);
            if (f.exists()) {
                f.moveTo(FileUtils.getFileHandle(file + ".corrupt-" + System.currentTimeMillis()));
            }
        } catch (Throwable t) {
            status = Messages.get(Mp.class, "guest_hero_backup_failed", t.getMessage());
        }
        guestHeroUnreadable = false;
    }

    /** 访客端不跑 Dungeon.init()：读/建英雄背包前要保证物品状态 handler 已初始化 */
    private void ensureItemHandlers() {
        if (Scroll.handlerNeedsInit()) Scroll.initLabels();
        if (Potion.handlerNeedsInit()) Potion.initColors();
        if (Ring.handlerNeedsInit()) Ring.initGems();
    }

    /** 按职业新建访客英雄（复刻游戏初始装备，但不碰 Dungeon 全局状态/快捷栏） */
    private Bundle newGuestHeroBundle(HeroClass cls) {
        ensureItemHandlers();

        Hero h = new Hero();
        h.heroClass = cls;
        h.lvl = 1;
        h.STR = 10;
        h.HT = 20;
        h.HP = 20;
        Talent.initClassTalents(h);

        ClothArmor armor = new ClothArmor();
        armor.identify();
        h.belongings.armor = armor;
        new Food().collect(h.belongings.backpack);
        new Waterskin().collect(h.belongings.backpack);
        new VelvetPouch().collect(h.belongings.backpack);
        new HerbBag().quantity(1).identify().collect(h.belongings.backpack);
        new ScrollOfIdentify().identify();

        switch (cls) {
            case WARRIOR: {
                WornShortsword ws = new WornShortsword();
                ws.identify();
                h.belongings.weapon = ws;
                h.belongings.armor.affixSeal(new BrokenSeal());
                new ThrowingStone().quantity(3).collect(h.belongings.backpack);
                new PotionOfHealing().quantity(1).identify().collect(h.belongings.backpack);
                new PotionOfHealing().identify();
                new ScrollOfRage().identify();
                break;
            }
            case MAGE: {
                MagesStaff staff = new MagesStaff(new WandOfMagicMissile());
                staff.upgrade();
                h.belongings.weapon = staff;
                staff.identify().level(1);
                staff.activate(h);
                new ScrollOfUpgrade().identify();
                new PotionOfLiquidFlame().quantity(1).identify().collect(h.belongings.backpack);
                break;
            }
            case ROGUE: {
                Dagger dagger = new Dagger();
                dagger.identify();
                h.belongings.weapon = dagger;
                CloakOfShadows cloak = new CloakOfShadows();
                h.belongings.artifact = cloak;
                cloak.identify();
                cloak.activate(h);
                new ThrowingKnife().quantity(3).collect(h.belongings.backpack);
                new ScrollOfMagicMapping().identify();
                new PotionOfInvisibility().quantity(1).identify().collect(h.belongings.backpack);
                break;
            }
            case HUNTRESS: {
                Gloves gloves = new Gloves();
                gloves.identify();
                h.belongings.weapon = gloves;
                new SpiritBow().identify().collect(h.belongings.backpack);
                new PotionOfMindVision().quantity(1).identify().collect(h.belongings.backpack);
                new ScrollOfLullaby().identify();
                break;
            }
            case DUELIST: {
                Rapier rapier = new Rapier();
                rapier.identify();
                h.belongings.weapon = rapier;
                rapier.activate(h);
                new ThrowingSpike().quantity(2).collect(h.belongings.backpack);
                new PotionOfStrength().identify();
                new ScrollOfMirrorImage().quantity(1).identify().collect(h.belongings.backpack);
                break;
            }
            case SPELLSWORD: {
                ImperialExorcism exo = new ImperialExorcism();
                exo.identify();
                h.belongings.weapon = exo;
                new MagicFlyBlade().quantity(1).collect(h.belongings.backpack);
                Buff.affect(h, MagicPower.class).set(20.0f, 20.0f);
                new PotionOfFrost().identify();
                new ScrollOfRecharging().quantity(1).identify().collect(h.belongings.backpack);
                break;
            }
            default:
                break;
        }

        Bundle b = new Bundle();
        h.storeInBundle(b);
        return b;
    }

    /** 把访客英雄 bundle 落盘（按世界种子分档；剔除 buff，避免跨世界引用）；返回是否成功 */
    private boolean saveGuestHeroBundle(Bundle b) {
        String file = mpHeroFile();
        if (file == null) {
            status = Messages.get(Mp.class, "guest_hero_not_in_world");
            return false;
        }
        try {
            b.remove("buffs");
            FileHandle f = FileUtils.getFileHandle(file);
            // 覆盖前保留上一份**可用**档案：写入中断/校验失败时可回退。
            // 主档本身已损坏时不要拿它覆盖旧的 .bak。
            if (f.exists()) {
                try {
                    Bundle old = FileUtils.bundleFromFile(file);
                    if (old != null && old.contains("class")) {
                        FileHandle bak = FileUtils.getFileHandle(file + ".bak");
                        if (bak.exists()) bak.delete();
                        f.copyTo(bak);
                    }
                } catch (Throwable ignored) {}
            }
            FileUtils.bundleToFile(file, b);
            // 读回校验：只有确认落盘的英雄档完整，调用方才允许清理临时世界
            Bundle check = FileUtils.bundleFromFile(file);
            if (check == null || !check.contains("class")) {
                status = Messages.get(Mp.class, "guest_hero_save_check_failed");
                return false;
            }
            return true;
        } catch (Throwable t) {
            status = Messages.get(Mp.class, "guest_hero_save_failed", t.getMessage());
            return false;
        }
    }

    /** GameScene.onPause 调用：切场景/关闭窗口前保存访客英雄（不清理临时世界） */
    public void onGamePause() {
        if (role == Role.CLIENT && guestWorldActive) saveGuestHero();
    }

    /** 把当前访客英雄状态落盘（跨会话保留背包/血量/充能）；返回是否成功 */
    private boolean saveGuestHero() {
        if (!guestWorldActive || worldSeed == Long.MIN_VALUE) return false;
        Hero h = Dungeon.hero;
        if (h == null) return false;
        try {
            Bundle b = new Bundle();
            h.storeInBundle(b);
            return saveGuestHeroBundle(b);
        } catch (Throwable t) {
            status = Messages.get(Mp.class, "guest_hero_save_failed", t.getMessage());
            return false;
        }
    }

    /** 清理访客专用临时世界槽：只允许删除 game<GUEST_TEMP_SLOT>，绝不碰普通存档/英雄档 */
    private void clearGuestTempSlot() {
        if (role != Role.CLIENT) return;
        try {
            String folder = GamesInProgress.gameFolder(GUEST_TEMP_SLOT);
            if (!("game" + GUEST_TEMP_SLOT).equals(folder)) {   // 路径安全检查，防误删
                status = Messages.get(Mp.class, "temp_slot_path_error", folder);
                return;
            }
            if (FileUtils.dirExists(folder) && !FileUtils.deleteDir(folder)) {
                status = Messages.get(Mp.class, "temp_slot_cleanup_incomplete");
            }
            GamesInProgress.setUnknown(GUEST_TEMP_SLOT);
            GamesInProgress.curSlot = 0;
        } catch (Throwable t) {
            status = Messages.get(Mp.class, "temp_slot_cleanup_failed", t.getMessage());
        }
    }

    // ---------------------------------------------------------------- 每帧

    /**
     * 由 GameScene.update() 每帧调用（渲染线程）
     *
     * @param gs 参数。
     */
    public void tick(GameScene gs) { // skipcq: JAVA-R1000 联机每帧主循环，复杂度高但已实测稳定
        if (!connected) {
            hideChatButton();
            return;
        }

        // T 键 / 局内聊天按钮都收敛在 Mp 里，GameScene 只需每帧调用 tick
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            openChat();
        }

        // 只有真正进入 GameScene 且有英雄时，才认为当前 Dungeon.hero 属于联机世界；
        // 避免在标题/stale 状态下把旧英雄写进访客档。
        if (role == Role.CLIENT && Dungeon.hero != null && Dungeon.level != null) {
            guestWorldActive = true;
        }

        if (gs != lastScene) {
            lastScene = gs;
            chatButton = null;                          // 新场景重新挂按钮（旧按钮随旧场景销毁）
            if (role == Role.CLIENT) saveGuestHero();   // 换层/重进场景时落盘访客英雄
            synchronized (peers) {
                for (Peer p : peers) {
                    p.remote = null;
                    p.remoteSprite = null;
                    p.nameLabel = null;
                    p.remotePos = -1;
                    p.remoteDepth = -1;
                    p.remoteBranch = -1;
                    p.remoteStale = false;
                }
            }
            lastSentPos = -1;
        }

        updateChatButton(gs);

        // 房主侧：给每个还没收到进度的访客发 STATE（新加入的访客也会在这里补发）
        if (role == Role.HOST && Dungeon.hero != null && Dungeon.level != null) {
            boolean needState = false;
            synchronized (peers) {
                for (Peer p : peers) {
                    if (p.versionOk && !p.gotState) { needState = true; break; }
                }
            }
            if (needState) {
                try {
                    Dungeon.saveAll();
                    int slot = GamesInProgress.curSlot;
                    byte[] game = FileUtils.getFileHandle(GamesInProgress.gameFile(slot)).readBytes();
                    byte[] level = FileUtils.getFileHandle(GamesInProgress.depthFile(slot, Dungeon.depth, Dungeon.branch)).readBytes();
                    byte[] payload = encodeState(Dungeon.depth, Dungeon.branch, game, level);
                    synchronized (peers) {
                        for (Peer p : peers) {
                            if (p.versionOk && !p.gotState) {
                                try { sendTo(p, Proto.STATE, payload); p.gotState = true; } catch (IOException ignored) {}
                            }
                        }
                    }
                    lastSentPos = -1;
                    status = Messages.get(Mp.class, "status_state_sent", peers.size());
                } catch (Throwable t) {
                    status = Messages.get(Mp.class, "status_state_failed", t.getMessage());
                }
            }
        }

        if (Dungeon.hero != null) {
            long now = System.currentTimeMillis();
            if (role == Role.CLIENT && now - lastHeroSave > 30_000L) {
                lastHeroSave = now;
                saveGuestHero();     // 每 30 秒自动保存访客英雄（防直接关窗口丢档）
            }
            if (Dungeon.hero.pos != lastSentPos && now - lastSendTime > 30) {
                try {
                    int id = isHost() ? HOST_ID : myId;
                    sendToAll(Proto.POS, encodePos(id, Dungeon.depth, Dungeon.branch, Dungeon.hero.pos));
                    lastSentPos = Dungeon.hero.pos;
                    lastSendTime = now;
                } catch (Throwable ignored) {}
            }
        }

        // 每 3 秒测一次延迟：房主测所有访客并广播 RTT_INFO；访客测房主
        long pingNow = System.currentTimeMillis();
        if (pingNow - lastPingTime > 3000L) {
            lastPingTime = pingNow;
            List<Peer> snapshot;
            synchronized (peers) { snapshot = new ArrayList<>(peers); }
            if (role == Role.HOST) {
                // 先把上一轮缓存的延迟广播给所有访客（访客 /list 显示其他访客用）
                List<RttRec> infos = new ArrayList<>();
                for (Peer p : snapshot) {
                    if (p.id <= 0) continue;
                    RttRec r = new RttRec();
                    r.id = p.id;
                    r.rttMs = p.rttMs;
                    r.timeout = p.pingTimeout;
                    infos.add(r);
                }
                if (!infos.isEmpty()) {
                    try { sendToAll(Proto.RTT_INFO, encodeRttInfo(infos)); } catch (Throwable ignored) {}
                }
                for (Peer p : snapshot) {
                    if (p.id <= 0 || !p.versionOk) continue;
                    if (p.pingSentAt > 0 && pingNow - p.pingSentAt > 2500L) {
                        p.pingTimeout = true;
                        p.rttMs = -1;
                        p.pingMisses.incrementAndGet();
                        if (p.pingMisses.get() >= PING_MISS_LIMIT) {
                            // 连续多个心跳无回应：判定该访客掉线，关闭 socket。
                            // 清理与"断开了连接"提示统一由 recvLoop 完成，避免重复提示。
                            try { if (p.sock != null) p.sock.close(); } catch (IOException ignored) {}
                            continue;
                        }
                    }
                    try {
                        p.pingToken = (int) (pingNow & 0x7fffffff);
                        p.pingSentAt = pingNow;
                        sendTo(p, Proto.PING, encodeInt(p.pingToken));
                    } catch (Throwable ignored) {}
                }
            } else if (role == Role.CLIENT) {
                Peer hp = null;
                for (Peer p : snapshot) {
                    if (p.id == HOST_ID) { hp = p; break; }
                }
                if (hp != null && hp.versionOk) {
                    if (hp.pingSentAt > 0 && pingNow - hp.pingSentAt > 2500L) {
                        hp.pingTimeout = true;
                        hp.rttMs = -1;
                        hp.pingMisses.incrementAndGet();
                        if (hp.pingMisses.get() >= PING_MISS_LIMIT) {
                            // 连续多个心跳无回应：网络状态不佳，回标题并弹窗说明原因
                            handleHostLost(Messages.get(Mp.class, "reason_network"));
                            return;
                        }
                    }
                    try {
                        hp.pingToken = (int) (pingNow & 0x7fffffff);
                        hp.pingSentAt = pingNow;
                        sendTo(hp, Proto.PING, encodeInt(hp.pingToken));
                    } catch (Throwable ignored) {}
                }
            }
        }

        syncRemote(gs);
    }

    /**
     * 由 TitleScene.update() 每帧调用（渲染线程）：检测退出存档
     *
     * @param ts 参数。
     */
    public void tickTitle(TitleScene ts) {
        if (!connected || lastScene == null) return;
        lastScene = null;
        lastSentPos = -1;
        synchronized (peers) {
            for (Peer p : peers) {
                p.remote = null;
                p.remoteSprite = null;
                p.nameLabel = null;
                p.remotePos = -1;
                p.remoteDepth = -1;
                p.remoteBranch = -1;
                p.remoteStale = false;
                p.gotState = false;   // 下次进档重新发 STATE
            }
        }
        if (role == Role.HOST) {
            // 房主退出存档：踢所有访客回标题；连接保持，房主存档只在本机
            sendToAll(Proto.KICK, new byte[0]);
            status = Messages.get(Mp.class, "status_host_exit_save");
        } else {
            // 访客退出存档（主动或被踢）：先保存自己的英雄，再清理临时世界；连接保持
            boolean ok = !guestWorldActive || saveGuestHero();
            if (ok) {
                clearGuestTempSlot();
                guestWorldActive = false;
                worldSeed = Long.MIN_VALUE;
                status = Messages.get(Mp.class, "status_client_exit_save");
            } else {
                status = Messages.get(Mp.class, "status_client_exit_save_failed");
            }
        }
    }

    private void syncRemote(GameScene gs) {
        List<Peer> snapshot;
        synchronized (peers) {
            snapshot = new ArrayList<>(peers);
        }
        for (Peer p : snapshot) {
            syncPeer(gs, p);
        }
    }

    private void syncPeer(GameScene gs, Peer p) { // skipcq: JAVA-R1000 同步远端幽灵，复杂度高但已实测稳定
        if (p.remotePos <= 0) return;

        // 远端坐标若属于另一层/支线，套到本层会穿墙乱跳：不创建也不移动
        boolean sameFloor = Dungeon.level != null
                && p.remoteDepth == Dungeon.depth && p.remoteBranch == Dungeon.branch;

        if (p.remote == null || p.remoteSprite == null) {
            if (!sameFloor) return;
            try {
                p.remote = new Hero() {
                    @Override
                    public void onMotionComplete() {
                        super.onMotionComplete();
                        if (sprite != null) sprite.idle();   // 幽灵没有游戏逻辑，停下时手动切回待机
                    }
                };
                p.remote.heroClass = remoteClass(p);
                p.remote.pos = p.remotePos;
                p.remoteSprite = new HeroSprite(p.remote);
                gs.addToFront(p.remoteSprite);
            } catch (Throwable t) {
                status = Messages.get(Mp.class, "status_spawn_peer_failed", t.getMessage());
                p.remote = null;
                p.remoteSprite = null;
            }
            return;
        }

        // HERO 帧晚于首个 POS 到达时（竞态），把已创建的幽灵换肤成对方真实职业
        if (isHost() && p.heroClass != null && p.remote.heroClass != p.heroClass) {
            p.remote.heroClass = p.heroClass;
            try {
                p.remoteSprite.disguise(p.heroClass);
            } catch (Throwable ignored) {}
        }

        if (sameFloor && p.remote.pos != p.remotePos) {
            int from = p.remote.pos;
            p.remote.pos = p.remotePos;
            try {
                if (p.remoteStale) {
                    p.remoteSprite.place(p.remotePos);   // 跨层回归：瞬移就位，避免长距离滑动
                } else {
                    p.remoteSprite.move(from, p.remotePos);
                }
            } catch (Throwable ignored) {}
        }
        p.remoteStale = !sameFloor;

        // 视野遮挡：远端玩家不在本地英雄视野内时不渲染（heroFOV 随英雄移动逐帧更新）
        boolean inFov = sameFloor && Dungeon.hero != null
                && p.remotePos < Dungeon.level.heroFOV.length
                && Dungeon.level.heroFOV[p.remotePos];
        p.remoteSprite.visible = inFov;

        // 名字名牌：悬在远端幽灵正上方（不显示自己的名字），随幽灵移动
        if (p.nameLabel == null) {
            p.nameLabel = PixelScene.renderTextBlock(6);
            p.nameLabel.text(p.name);
            gs.addToFront(p.nameLabel);
        }
        p.nameLabel.setPos(
                p.remoteSprite.x + (p.remoteSprite.width() - p.nameLabel.width()) / 2f,
                p.remoteSprite.y - p.nameLabel.height() - 3f);
        p.nameLabel.visible = inFov;
    }

    /** 远端幽灵该显示的职业：对端职业优先，兜底本地英雄职业 */
    private HeroClass remoteClass(Peer p) {
        if (p.heroClass != null) return p.heroClass;
        return Dungeon.hero != null ? Dungeon.hero.heroClass : HeroClass.WARRIOR;
    }

    // ---------------------------------------------------------------- 线协议
    // 帧格式: [int 长度][byte 类型][payload]

        /** 协议帧：类型 + payload。 */
    public static class Frame {
        /** 帧类型。 */
        public final int type;
        /** 帧载荷。 */
        public final byte[] data;

        /**
         * 构造一帧。
         *
         * @param type 参数。
         * @param data 参数。
         */
        public Frame(int type, byte[] data) {
            this.type = type;
            this.data = data;
        }
    }

        /** STATE 帧载荷：层数 + 游戏档 + 当前层。 */
    public static class StatePayload {
        /** 层数。 */
        public int depth;
        /** 分支（0=主线）。 */
        public int branch;
        /** 游戏存档字节。 */
        public byte[] game;
        /** 当前层字节。 */
        public byte[] level;
    }

        /** VERSION 帧载荷：联机协议版本 + 游戏版本。 */
    public static class VersionPayload {
        /** 联机协议版本。 */
        public int protocolVersion;
        /** 游戏版本号（manifest/Android versionCode）。 */
        public int gameVersionCode;
        /** 游戏版本名（如 0.9.7.2）。 */
        public String gameVersion;
    }

        /** 单帧最大长度（64 MB），防止非法长度攻击。 */
    public static final int MAX_FRAME = 64 * 1024 * 1024;

    /**
     * 写入一帧：[int32 长度][int8 类型][payload]（大端）。
     *
     * @param o 参数。
     * @param type 参数。
     * @param payload 参数。
     */
    public static void writeFrame(DataOutputStream o, int type, byte[] payload) throws IOException {
        synchronized (o) {
            o.writeInt(payload.length);
            o.writeByte(type);
            o.write(payload);
            o.flush();
        }
    }

    /**
     * 读取一帧并校验长度（单帧上限 MAX_FRAME）。
     *
     * @param in 参数。
     * @return 返回值。
     */
    public static Frame readFrame(DataInputStream in) throws IOException {
        int len = in.readInt();
        if (len < 0 || len > MAX_FRAME) {
            throw new IOException(Messages.get(Mp.class, "error_bad_frame_length", len));
        }
        int type = in.readByte();
        byte[] data = new byte[len];
        in.readFully(data);
        return new Frame(type, data);
    }

    /**
     * 编码 STATE 帧：depth/branch/游戏档/当前层字节。
     *
     * @param depth 参数。
     * @param branch 参数。
     * @param game 参数。
     * @param level 参数。
     * @return 返回值。
     */
    public static byte[] encodeState(int depth, int branch, byte[] game, byte[] level) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(game.length + level.length + 32);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(depth);
        dos.writeInt(branch);
        dos.writeInt(game.length);
        dos.write(game);
        dos.writeInt(level.length);
        dos.write(level);
        dos.flush();
        return bos.toByteArray();
    }

    /**
     * 解码 STATE 帧。
     *
     * @param data 参数。
     * @return 返回值。
     */
    public static StatePayload decodeState(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        StatePayload p = new StatePayload();
        p.depth = di.readInt();
        p.branch = di.readInt();
        int gameLen = di.readInt();
        if (gameLen < 0 || gameLen > di.available()) {
            throw new IOException(Messages.get(Mp.class, "error_bad_state_length", gameLen));
        }
        p.game = new byte[gameLen];
        di.readFully(p.game);
        int levelLen = di.readInt();
        if (levelLen < 0 || levelLen > di.available()) {
            throw new IOException(Messages.get(Mp.class, "error_bad_state_length", levelLen));
        }
        p.level = new byte[levelLen];
        di.readFully(p.level);
        return p;
    }

    /**
     * 编码 VERSION 帧：联机协议版本 + 游戏版本号 + 游戏版本名。
     *
     * @return 返回值。
     * @throws IOException 参数。
     */
    public static byte[] encodeVersion() throws IOException {
        return encodeVersion(MP_PROTOCOL_VERSION, Game.versionCode, gameVersionName());
    }

    /**
     * 编码 VERSION 帧（显式传入版本信息，便于测试）。
     *
     * @param protocolVersion 参数。
     * @param gameVersionCode 参数。
     * @param gameVersion 参数。
     * @return 返回值。
     * @throws IOException 参数。
     */
    public static byte[] encodeVersion(int protocolVersion, int gameVersionCode,
                                       String gameVersion) throws IOException {
        byte[] vb = (gameVersion == null ? "" : gameVersion).getBytes("UTF-8");
        ByteArrayOutputStream bos = new ByteArrayOutputStream(12 + vb.length);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(protocolVersion);
        dos.writeInt(gameVersionCode);
        dos.writeInt(vb.length);
        dos.write(vb);
        dos.flush();
        return bos.toByteArray();
    }

    /**
     * 解码 VERSION 帧。
     *
     * @param data 参数。
     * @return 返回值。
     * @throws IOException 参数。
     */
    public static VersionPayload decodeVersion(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        VersionPayload p = new VersionPayload();
        p.protocolVersion = di.readInt();
        p.gameVersionCode = di.readInt();
        int len = di.readInt();
        if (len < 0 || len > di.available()) {
            throw new IOException(Messages.get(Mp.class, "error_bad_version_length", len));
        }
        byte[] vb = new byte[len];
        di.readFully(vb);
        p.gameVersion = new String(vb, "UTF-8");
        return p;
    }

        /** POS 帧载荷。 */
    public static class PosPayload {
        /** 标识 id。 */
        public int id;
        /** 层数。 */
        public int depth;
        /** 分支（0=主线）。 */
        public int branch;
        /** 格子位置。 */
        public int pos;
    }

    /**
     * 编码 POS 帧：id/depth/branch/cell。
     *
     * @param id 参数。
     * @param depth 参数。
     * @param branch 参数。
     * @param pos 参数。
     * @return 返回值。
     */
    public static byte[] encodePos(int id, int depth, int branch, int pos) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(16);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(id);
        dos.writeInt(depth);
        dos.writeInt(branch);
        dos.writeInt(pos);
        dos.flush();
        return bos.toByteArray();
    }

    /**
     * 解码 POS 帧。
     *
     * @param data 参数。
     * @return 返回值。
     */
    public static PosPayload readPos(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        PosPayload p = new PosPayload();
        p.id = di.readInt();
        p.depth = di.readInt();
        p.branch = di.readInt();
        p.pos = di.readInt();
        return p;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isHost() {
        return role == Role.HOST;
    }

    public boolean isClient() {
        return role == Role.CLIENT;
    }

    private static String peername() {
        try {
            String n = System.getProperty("user.name");
            return n == null || n.isEmpty()
                    ? Messages.get(Mp.class, "default_player_name") : n;
        } catch (Throwable t) {
            return Messages.get(Mp.class, "default_player_name");
        }
    }

    /** 本机游戏版本名（manifest 缺失时用 ?）。 */
    private static String gameVersionName() {
        return Game.version == null || Game.version.isEmpty() ? "?" : Game.version;
    }

    /** 玩家名为空时使用本地化的默认名字，避免 UI 出现 null。 */
    private static String displayName(String name) {
        return name == null || name.trim().isEmpty()
                ? Messages.get(Mp.class, "default_player_name") : name;
    }

    private static String localIp() {
        try {
            java.util.Enumeration<java.net.NetworkInterface> nets = java.net.NetworkInterface.getNetworkInterfaces();
            while (nets.hasMoreElements()) {
                java.net.NetworkInterface ni = nets.nextElement();
                if (!ni.isUp() || ni.isLoopback()) continue;
                java.util.Enumeration<java.net.InetAddress> addrs = ni.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    java.net.InetAddress a = addrs.nextElement();
                    if (a instanceof java.net.Inet4Address && !a.isLoopbackAddress()) {
                        return a.getHostAddress();
                    }
                }
            }
        } catch (Throwable ignored) {}
        return "127.0.0.1";
    }

        /** 协议帧类型常量。 */
    public static class Proto {
        /** HELLO：玩家名。 */
        public static final int HELLO = 1;
        /** STATE：房主存档 + 当前层。 */
        public static final int STATE = 2;
        /** POS：位置 + 层。 */
        public static final int POS = 3;
        /** HERO：职业通知（房主转发给其他访客）。 */
        public static final int HERO = 4;   // 带 id 的职业通知（房主转发给其他访客）
        /** CHAT：聊天（房主转发）。 */
        public static final int CHAT = 5;   // 名字+内容 编码，房主转发
        /** KICK：房主退出存档，访客回标题（连接保持）。 */
        public static final int KICK = 6;   // 房主 → 访客：房主退出存档，回标题（连接保持）
        /** ASSIGN：房主给新访客分配 id。 */
        public static final int ASSIGN = 7; // 房主 → 新访客：分配 id
        /** JOIN：新玩家加入（id + 名字）。 */
        public static final int JOIN = 8;   // 房主 → 其他访客：新玩家加入（id + 名字）
        /** LEAVE：玩家离开（id）。 */
        public static final int LEAVE = 9;  // 房主 → 其他访客：玩家离开（id）
        // 20+ 为稳定线新增；避开 test 线已占用的 11/15
        /** FULL：房间已满。 */
        public static final int FULL = 20;    // 房主 → 第 5 名加入者：房间已满
        /** KICKED：被 /kick 的访客立即断开。 */
        public static final int KICKED = 21;  // 房主 → 被 /kick 的访客：立即断开
        /** PING：延迟测量。 */
        public static final int PING = 22;    // 双向：延迟测量
        /** PONG：延迟回应。 */
        public static final int PONG = 23;    // 双向：延迟回应
        /** RTT_INFO：各访客延迟列表。 */
        public static final int RTT_INFO = 24; // 房主 → 访客：各访客到房主的延迟（访客 /list 用）
        /** VERSION：联机协议版本 + 游戏版本（双向，连接后立即发送）。 */
        public static final int VERSION = 25;  // 双向：版本握手
    }

        /** HERO 帧载荷：id + 职业。 */
    public static class HeroPayload {
        /** 标识 id。 */
        public int id;
        /** 职业。 */
        public HeroClass cls;
    }

    /**
     * 编码 HERO 帧：id + 职业序数。
     *
     * @param id 参数。
     * @param cls 参数。
     * @return 返回值。
     */
    public static byte[] encodeHero(int id, HeroClass cls) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(id);
        dos.writeInt(cls == null ? -1 : cls.ordinal());
        dos.flush();
        return bos.toByteArray();
    }

    /**
     * 解码 HERO 帧。
     *
     * @param data 参数。
     * @return 返回值。
     */
    public static HeroPayload decodeHero(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        HeroPayload p = new HeroPayload();
        p.id = di.readInt();
        int o = di.readInt();
        if (o < 0 || o >= HeroClass.values().length) {
            throw new IOException(Messages.get(Mp.class, "error_bad_class_ordinal", o));
        }
        p.cls = HeroClass.values()[o];
        return p;
    }

    /** 聊天帧：名字长度 + 名字 + 内容（房主原样转发，显示格式全端统一）；JOIN 解码复用 id 字段 */
        /** CHAT 帧载荷：名字 + 内容。 */
    public static class ChatPayload {
        /** 标识 id。 */
        public int id;
        /** 玩家名。 */
        public String name;
        /** 聊天内容。 */
        public String text;
    }

    /**
     * 编码 CHAT 帧：玩家名 + 内容。
     *
     * @param name 参数。
     * @param text 参数。
     * @return 返回值。
     */
    public static byte[] encodeChat(String name, String text) throws IOException {
        byte[] nb = (name == null ? "" : name).getBytes("UTF-8");
        byte[] tb = (text == null ? "" : text).getBytes("UTF-8");
        ByteArrayOutputStream bos = new ByteArrayOutputStream(4 + nb.length + tb.length);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(nb.length);
        dos.write(nb);
        dos.write(tb);
        dos.flush();
        return bos.toByteArray();
    }

    /**
     * 解码 CHAT 帧。
     *
     * @param data 参数。
     * @return 返回值。
     */
    public static ChatPayload decodeChat(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        ChatPayload p = new ChatPayload();
        int nameLen = di.readInt();
        if (nameLen < 0 || nameLen > data.length - 4) {
            throw new IOException(Messages.get(Mp.class, "error_bad_chat_length", nameLen));
        }
        byte[] nb = new byte[nameLen];
        di.readFully(nb);
        p.name = new String(nb, "UTF-8");
        byte[] tb = new byte[data.length - 4 - nb.length];
        di.readFully(tb);
        p.text = new String(tb, "UTF-8");
        return p;
    }

    /**
     * 编码 RTT_INFO 帧：各访客延迟列表。
     *
     * @param recs 参数。
     * @return 返回值。
     */
    public static byte[] encodeRttInfo(List<RttRec> recs) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(4 + recs.size() * 9);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(recs.size());
        for (RttRec r : recs) {
            dos.writeInt(r.id);
            dos.writeInt(r.rttMs);
            dos.writeBoolean(r.timeout);
        }
        dos.flush();
        return bos.toByteArray();
    }

    /**
     * 解码 RTT_INFO 帧。
     *
     * @param data 参数。
     * @return 返回值。
     */
    public static RttInfoPayload decodeRttInfo(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        int count = di.readInt();
        if (count < 0 || count > MAX_PLAYERS) {
            throw new IOException(Messages.get(Mp.class, "error_bad_rtt_count", count));
        }
        RttInfoPayload p = new RttInfoPayload();
        p.recs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            RttRec r = new RttRec();
            r.id = di.readInt();
            r.rttMs = di.readInt();
            r.timeout = di.readBoolean();
            p.recs.add(r);
        }
        return p;
    }

    /**
     * 编码 4 字节大端整数。
     *
     * @param v 参数。
     * @return 返回值。
     */
    public static byte[] encodeInt(int v) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(4);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(v);
        dos.flush();
        return bos.toByteArray();
    }

    /**
     * 解码 4 字节大端整数。
     *
     * @param data 参数。
     * @return 返回值。
     */
    public static int decodeInt(byte[] data) throws IOException {
        return new DataInputStream(new ByteArrayInputStream(data)).readInt();
    }

    /**
     * 编码 JOIN 帧：id + 玩家名。
     *
     * @param id 参数。
     * @param name 参数。
     * @return 返回值。
     */
    public static byte[] encodeJoin(int id, String name) throws IOException {
        byte[] nb = (name == null ? "" : name).getBytes("UTF-8");
        ByteArrayOutputStream bos = new ByteArrayOutputStream(4 + nb.length);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(id);
        dos.write(nb);
        dos.flush();
        return bos.toByteArray();
    }

    /**
     * 解码 JOIN 帧。
     *
     * @param data 参数。
     * @return 返回值。
     */
    public static ChatPayload decodeJoin(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        ChatPayload p = new ChatPayload();
        p.id = di.readInt();   // 复用 id 字段
        byte[] nb = new byte[data.length - 4];
        di.readFully(nb);
        p.name = new String(nb, "UTF-8");
        return p;
    }

}
