package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
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
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.utils.PreviewMode;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHero;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Bundlable;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.FileUtils;
import net.iharder.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

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
    /** 正式版固定服务器地址：玩家不需要手动输入。 */
    public static final String DEFAULT_SERVER_ADDRESS = "47.109.62.104";

    /**
     * 默认服务器地址；可用 {@code -Dmlpd.server=...} 覆盖（仅供本地测试）。
     *
     * @return 服务器地址。
     */
    public static String defaultServerAddress() {
        String override = System.getProperty("mlpd.server");
        if (override != null && !override.trim().isEmpty()) return override.trim();
        return DEFAULT_SERVER_ADDRESS;
    }

    /**
     * 联机模块版本号：显示在联机菜单右下角，并记录在根目录《更新日志.txt》。
     * 不得随意迭代：仅在用户明确指示“更新至某某版本”时修改，同时追加更新日志。
     */
    public static final String MP_VERSION = "v0.2.0";
    /** 最大人数：1 房主 + 3 访客（推荐上限，理由见 PROGRESS §10 多人适配） */
    public static final int MAX_PLAYERS = 4;

    /**
     * 联机协议版本。线协议（帧类型/载荷格式）改动时必须递增；
     * 双方版本不一致会拒绝连接，避免旧版本用不兼容的存档/协议混连。
     */
    public static final int MP_PROTOCOL_VERSION = 14;

    /** 连续多少个 PING 收不到 PONG 就判定连接中断（每 3 秒一次，约 15 秒） */
    private static final int PING_MISS_LIMIT = 5;

    /** 版本握手超时（毫秒）：超时视为对方版本过旧或未完成握手。 */
    private static final long HANDSHAKE_TIMEOUT_MS = 5000L;

    /** 他人最高分对局详情请求超时（毫秒）。 */
    private static final long PROFILE_REQUEST_TIMEOUT_MS = 5000L;

    /** 个人资料/房间状态中携带的徽章数量上限（防御异常数据）。 */
    private static final int MAX_PROFILE_BADGES = 256;

    /** 幽灵头顶血量条尺寸。 */
    private static final float HP_BAR_WIDTH = 16f;
    private static final float HP_BAR_HEIGHT = 2f;

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
        /** 是否已完成版本握手且双方兼容。 */
        volatile boolean versionOk = false;
        /** 是否已处理过 VERSION（成功或失败）。 */
        volatile boolean versionChecked = false;
        /** 版本不兼容/握手超时的具体原因（断开时保留给玩家看）。 */
        volatile String versionError = null;
        /** 对端当前血量/上限（-1=尚未收到 STATS）。 */
        volatile int hp = -1;
        volatile int ht = -1;
        /** 对端最近一次上报的完整英雄 bundle（/list 点击与放大镜查看详情用；null=尚未收到）。 */
        // skipcq: JAVA-E0027  只需引用跨线程可见：数组整体只在接收线程写入、渲染线程替换读取，元素发布后不再修改
        volatile byte[] heroInfo = null;
        /** 对端是否为管理员（房主授予）。 */
        volatile boolean admin = false;
        /** 对端资料：UUID（入房后由 PROFILE 帧上报）。 */
        volatile String uuid = "";
        /** 对端佩戴的徽章（最多 3 枚，入房后由 PROFILE 帧上报）。 */
        volatile List<Badges.Badge> badges = Collections.emptyList();
        /** 对端已拥有的全部徽章（个人主页徽章页用；由 PROFILE 帧上报）。 */
        volatile List<Badges.Badge> allBadges = Collections.emptyList();
        /** 对端历史最高分（含每日挑战；由 PROFILE 帧上报）。 */
        volatile int highestScore = 0;
        /** 房主开始新游戏导致的断开（不重复播报普通断线）。 */
        volatile boolean worldEnded = false;
        /** 房主换档后等待该访客保存旧英雄并回 WORLD_READY。 */
        volatile boolean worldResetPending = false;
        volatile long worldResetAt = 0L;
        /** 幽灵头顶的血量条（背景 + 填充）。 */
        ColorBlock hpBg;
        ColorBlock hpFill;
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
        /** 所在层数（-1=未知）。 */
        public int depth = -1;
        /** 分支（0=主线）。 */
        public int branch;
        /** 当前血量（-1=未知）。 */
        public int hp = -1;
        /** 血量上限（-1=未知）。 */
        public int ht = -1;
        /** 是否管理员。 */
        public boolean isAdmin;
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

    /** 房间内一名玩家的只读快照（开局前使用，不依赖 Dungeon.hero）。 */
    public static class RoomPlayerInfo {
        /** 玩家 id（房主为 0）。 */
        public final int id;
        /** 玩家名。 */
        public final String name;
        /** 房间内选择的职业；null=尚未选择。 */
        public final HeroClass heroClass;
        /** 是否房主。 */
        public final boolean isHost;
        /** 展示用 UUID（与每日挑战一致）。 */
        public final String uuid;
        /** 佩戴的徽章（0~3 枚）。 */
        public final List<Badges.Badge> badges;
        /** 已拥有的全部徽章（个人主页徽章页用）。 */
        public final List<Badges.Badge> allBadges;
        /** 是否管理员（房主也可由 localAdmin 标记；通常访客使用）。 */
        public final boolean isAdmin;
        /** 历史最高分（含每日挑战）。 */
        public final int highestScore;

        RoomPlayerInfo(int id, String name, HeroClass heroClass, boolean isHost,
                       String uuid, List<Badges.Badge> badges, List<Badges.Badge> allBadges,
                       boolean isAdmin, int highestScore) {
            this.id = id;
            this.name = name;
            this.heroClass = heroClass;
            this.isHost = isHost;
            this.uuid = uuid == null ? "" : uuid;
            this.badges = badges == null
                    ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(badges));
            this.allBadges = allBadges == null
                    ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(allBadges));
            this.isAdmin = isAdmin;
            this.highestScore = highestScore;
        }

        RoomPlayerInfo(int id, String name, HeroClass heroClass, boolean isHost) {
            this(id, name, heroClass, isHost, "", Collections.emptyList(),
                    Collections.emptyList(), false, 0);
        }
    }

    /** 房间当前选择的存档。 */
    public static class RoomSaveInfo {
        /** 是否已选择存档。 */
        public final boolean selected;
        /** 存档槽位（未选择时为 -1）。 */
        public final int slot;
        /** 世界种子（未选择时为 Long.MIN_VALUE）。 */
        public final long seed;
        /** 是否新档（尚未写入磁盘）。 */
        public final boolean isNew;
        /** 房主在该房间使用的职业；已有存档时来自存档。 */
        public final HeroClass hostClass;
        /** 层数（已有存档时来自存档）。 */
        public final int depth;
        /** 英雄等级（已有存档时来自存档）。 */
        public final int level;
        /** 是否已经开过一局（用于「继续对局」文案）。 */
        public final boolean started;

        RoomSaveInfo(boolean selected, int slot, long seed, boolean isNew,
                     HeroClass hostClass, int depth, int level, boolean started) {
            this.selected = selected;
            this.slot = slot;
            this.seed = seed;
            this.isNew = isNew;
            this.hostClass = hostClass;
            this.depth = depth;
            this.level = level;
            this.started = started;
        }
    }

    /** 大厅房间列表的一行。 */
    public static class RoomListing {
        /** 6 位房间号。 */
        public final String code;
        /** 当前人数（含房主）。 */
        public final int players;
        /** 人数上限。 */
        public final int maxPlayers;
        /** 房主名。 */
        public final String hostName;
        /** 房间是否设置了密码。 */
        public final boolean locked;

        RoomListing(String code, int players, int maxPlayers, String hostName,
                    boolean locked) {
            this.code = code;
            this.players = players;
            this.maxPlayers = maxPlayers;
            this.hostName = hostName;
            this.locked = locked;
        }

        RoomListing(String code, int players, int maxPlayers, String hostName) {
            this(code, players, maxPlayers, hostName, false);
        }
    }

    private static final RoomSaveInfo EMPTY_ROOM_SAVE =
            new RoomSaveInfo(false, -1, Long.MIN_VALUE, false, null, 0, 1, false);

    private final List<Peer> peers = new ArrayList<>();

    private GameScene lastScene;

    private int lastSentPos = -1;
    private long lastSendTime = 0L;
    private long lastHeroSave = 0L;
    private long lastPingTime = 0L;
    private long lastStatsTime = 0L;
    private long lastHeroInfoTime = 0L;
    private volatile boolean kickedByHost = false;
    /** 访客本机是否被房主授予管理员权限。 */
    private volatile boolean localAdmin = false;
    /** 房主当前所在世界的标识（curSlot:seed），用于检测死亡后“重新开始”换档。 */
    private volatile String hostWorldKey = null;
    /** 本机这次死亡是否已经播报过（复活/进新档后复位）。 */
    private volatile boolean localDeathAnnounced = false;
    /** 访客正在从旧世界切到房主新世界（防止旧场景暂停时把旧英雄写到新种子）。 */
    private volatile boolean enteringNewWorld = false;

    private StatePayload pendingState;        // 选职业期间暂存的最新 STATE
    private WndHeroClassSelect classWindow;   // 职业选择窗口（断连时自动关掉）
    private volatile WndMessage hostDeathPopup;   // 房主死亡弹窗（进入新档时关掉）
        /** 玩家名持久化文件名。 */
    public static final String MP_NAME_FILE = "mp_name.txt";

    private long worldSeed = Long.MIN_VALUE;  // 当前世界的种子：访客英雄按世界分档

    /** 访客联机临时世界槽位：故意放在普通存档 1..MAX_SLOTS 之外，主界面永远不会列出 */
    private static final int GUEST_TEMP_SLOT = 99;
    /** 访客英雄档里英雄子 bundle 的键：嵌套写入（自带类名），可直接放回游戏存档 */
    private static final String MP_HERO = "hero";
    /** 访客英雄档里记录的所在层/分支（重连时校验上次位置是否仍可用） */
    private static final String MP_DEPTH = "mp_depth";
    private static final String MP_BRANCH = "mp_branch";
    /** 访客英雄档里保存的快捷栏快照（占位物品 + 槽位技能选择） */
    private static final String MP_QUICKSLOT = "mp_quickslot";
    /** 访客英雄档里保存的探图数据（visited/mapped 按层分键）：临时世界退出即删，探图必须自己留档 */
    private static final String MP_EXPLORED = "mp_explored_";
    /** STATE 的 game bundle 里附带的“初始世界包”：第0层初始 game.dat + 各层初始层档。 */
    private static final String MP_INIT_WORLD = "mp_init_world";
    /** 房主存档目录下的初始快照子目录。 */
    private static final String MP_INIT_DIR = "mp_init";
    /** 访客自己的持久世界目录前缀（按房主世界种子分档）。 */
    private static final String MP_WORLD_PREFIX = "mp_world_";
    private static final String MP_INIT_GAME = "game";
    private static final String MP_INIT_LEVELS = "levels";
    /** 当前 Dungeon.hero 是否属于已进入的联机世界（防止把单人或过期英雄写入访客档） */
    private boolean guestWorldActive = false;
    /** 访客当前使用的是否为“从房主初始快照创建的持久世界”（断线不删，存到 mp_world_<seed>/）。 */
    private boolean guestWorldPersistent = false;
    /** 房主首次生成楼层时暂存深度/分支；switchLevel 完成后由 onLevelSwitched 消费。 */
    private int pendingFreshDepth = -1;
    private int pendingFreshBranch = -1;
    /** 访客英雄档存在但读不出来：首次加入前先备份旧档，避免直接覆盖丢数据 */
    private boolean guestHeroUnreadable = false;
    /** 访客最近一次在本机世界中的位置（内存缓存；房主退出/重进时优先于可能滞后的英雄档） */
    private volatile int lastGuestPos = -1;
    private volatile int lastGuestDepth = -1;
    private volatile int lastGuestBranch = -1;
    private volatile long lastGuestWorldSeed = Long.MIN_VALUE;
    /** 访客本次会话每层探图缓存：depth_branch -> Bundle{visited,mapped}。 */
    private final HashMap<Integer, Bundle> guestExplored = new HashMap<>();
    private long lastExploredSnapshot = 0L;
    /** Android 端最后一次看到的本地英雄血量（用于兜底触发受击红屏）。 */
    private int lastHeroHp = -1;

    private String myName;                    // 本机玩家名（联机窗口可设置，mp_name.txt 持久化）
    /** 是否服务器模式（A 模式：经大厅配对，房主权威不变）。 */
    private volatile boolean serverMode = false;
    /** 服务器地址（不含端口）。 */
    private volatile String serverAddress = null;
    /** 服务器数据端口（房主从 ROOM 响应获得；初值用字面量避免单例早于常量初始化）。 */
    private volatile int serverDataPort = 28772;
    /** 当前房间号（房主开房成功后 / 访客加入时）。 */
    private volatile String roomCode = null;
    /** 房主设置的房间密码；空串=无密码（只影响之后加入的访客）。 */
    private volatile String roomPassword = "";
    /** 服务器模式：大厅控制连接（房主）或等待配对连接（访客）。 */
    private volatile Socket lobbySocket;
    /** 房主控制连接的输出流（与心跳共用，避免并发写字节交错）。 */
    private volatile DataOutputStream lobbyOut;

    // ---------------------------------------------------------------- 房间预开局状态（大厅/房间界面）

    /** 房间当前存档快照（直接 new，不能引用靠后的 static 常量：Mp 单例在类顶部先初始化）。 */
    private volatile RoomSaveInfo roomSave =
            new RoomSaveInfo(false, -1, Long.MIN_VALUE, false, null, 0, 1, false);
    /** 房间玩家列表快照（房主端权威，访客端来自 ROOM_STATE）。 */
    private volatile List<RoomPlayerInfo> roomRoster = Collections.emptyList();
    /** 房间 UI 版本号：变化时场景重建列表/刷新按钮。 */
    private final AtomicInteger roomUiVersion = new AtomicInteger();
    /** 房主端：roster/存档变化后需要广播。 */
    private volatile boolean roomStateDirty = false;
    private volatile long lastRoomStateSent = 0L;
    /** 房间场景 PING 计时（GameScene 之外也要保活）。 */
    private volatile long lastRoomPingTime = 0L;
    /** 房主在房间内选择的职业（新档用）。 */
    private volatile HeroClass hostRoomClass = null;
    /** 访客自己在房间内选择的职业（新档/无存档时用）。 */
    private volatile HeroClass guestRoomClass = null;
    /** guestRoomClass 对应的存档种子，避免误用到别的世界。 */
    private volatile long guestRoomClassSeed = Long.MIN_VALUE;
    /** 访客在该世界种子下已有英雄档：锁定选择英雄。 */
    private volatile boolean guestRoomHeroLocked = false;
    private volatile long guestLockCacheSeed = Long.MIN_VALUE;
    private volatile boolean guestLockCacheValid = false;
    private volatile HeroClass guestLockCacheClass = null;
    /** 最近一次发给房主的房间职业，避免 ROOM_STATE 周期重发时刷屏。 */
    private volatile long lastRoomHeroSentSeed = Long.MIN_VALUE;
    private volatile HeroClass lastRoomHeroSent = null;
    /** 房间模式：正在选择存档（StartScene）。 */
    private volatile boolean selectingRoomSave = false;
    /** 房间模式：正在为新档配置职业/种子；>=0 表示待写入的存档槽。 */
    private volatile int roomNewGameSlot = -1;
    /** 新建档在 Dungeon.init() 后需要复位的 overrideSeed。 */
    private volatile boolean roomSeedPending = false;
    /** 新建档首次进入 GameScene 时需要落盘一次。 */
    private volatile boolean pendingRoomSaveWrite = false;
    /** 房间列表缓存与请求状态。 */
    private volatile List<RoomListing> roomList = Collections.emptyList();
    private final AtomicInteger roomListVersion = new AtomicInteger();
    private volatile boolean roomListLoading = false;
    private volatile String roomListError = null;
    private volatile long lastRoomListRequest = 0L;
    /** 访客端：上一次 ROOM_STATE 的内容签名，避免每 2 秒重建 UI。 */
    private volatile String roomStateSignature = "";

    // ---------------------------------------------------------------- 个人资料（个人设置/个人主页）

    /** 本机资料（懒加载；大厅与房间共用）。 */
    private volatile MpProfile myProfile = null;
    /** 访客端：本机资料有变化，等待发给房主。 */
    private volatile boolean profileDirty = true;
    private volatile long lastProfileSent = 0L;
    /** 按需请求的最高分对局记录（targetId → Record；null 表示已确认无记录）。 */
    private final Object profileLock = new Object();
    private final HashMap<Integer, Rankings.Record> profileRecords = new HashMap<>();
    private final HashSet<Integer> profileRecordsLoaded = new HashSet<>();
    private final HashMap<Integer, Long> profileRequestAt = new HashMap<>();
    /** 待联机大厅显示的提示（避免场景切换竞态导致弹窗丢失）。 */
    private volatile String pendingLobbyMessage = null;
    /** 聊天窗口是否打开（防止按键重入）。 */
    public volatile boolean chatOpen = false;
    private volatile MpChatWindow chatWindow;
    private StyledButton chatButton;          // 局内聊天按钮（Mp 自己挂在 GameScene 上）

    // ---------------------------------------------------------------- 聊天记录（仅本机内存）

    /** 聊天记录最多保留的条数。 */
    public static final int CHAT_MAX_LINES = 200;
    /** 自己发送的聊天。 */
    public static final int CHAT_TYPE_SELF = 0;
    /** 其他玩家发送的聊天。 */
    public static final int CHAT_TYPE_OTHER = 1;
    /** 系统/特殊事件（命令反馈等）。 */
    public static final int CHAT_TYPE_SYSTEM = 2;

    /** 一条聊天记录。 */
    public static final class ChatLine {
        /** 单调递增的序号（用于窗口增量刷新）。 */
        public final long id;
        /** 发送者名字。 */
        public final String name;
        /** 聊天内容。 */
        public final String text;
        /** 本机时间 HH:mm:ss。 */
        public final String time;
        /** 类型：{@link #CHAT_TYPE_SELF}/{@link #CHAT_TYPE_OTHER}/{@link #CHAT_TYPE_SYSTEM}。 */
        public final int type;

        ChatLine(long id, String name, String text, String time, int type) {
            this.id = id;
            this.name = name;
            this.text = text;
            this.time = time;
            this.type = type;
        }
    }

    private final ArrayList<ChatLine> chatLines = new ArrayList<>();
    private long chatNextId = 1L;
    /** 聊天记录版本号：窗口每帧比较它决定是否刷新。 */
    private volatile long chatVersion = 0L;

    // ---------------------------------------------------------------- 左上角联机日志（聊天 + 联机消息）

    /** 左上角联机日志最多保留的条数。 */
    public static final int MPLOG_MAX_LINES = 5;
    /** 普通联机信息（白色）。 */
    public static final int MPLOG_INFO = 0;
    /** 特殊联机事件（金色）。 */
    public static final int MPLOG_EVENT = 1;
    /** 错误/负面消息（红色）。 */
    public static final int MPLOG_NEGATIVE = 2;
    /** 正面消息（绿色）。 */
    public static final int MPLOG_POSITIVE = 3;
    /** 警告（橙色）。 */
    public static final int MPLOG_WARNING = 4;
    /** 自己发送的聊天（粉色）。 */
    public static final int MPLOG_CHAT_SELF = 10;
    /** 其他玩家的聊天（紫色）。 */
    public static final int MPLOG_CHAT_OTHER = 11;
    /** 系统聊天（金色）。 */
    public static final int MPLOG_CHAT_SYSTEM = 12;

    /** 一条左上角联机日志。 */
    public static final class MpLogLine {
        /** 单调递增序号。 */
        public final long id;
        /** 显示文本。 */
        public final String text;
        /** 颜色类型。 */
        public final int type;

        MpLogLine(long id, String text, int type) {
            this.id = id;
            this.text = text;
            this.type = type;
        }
    }

    private final ArrayList<MpLogLine> mpLogLines = new ArrayList<>();
    private long mpLogNextId = 1L;
    private volatile long mpLogVersion = 0L;
    private MpGameLog mpLogPane;
    /** ROOM_EVENT 事件：某玩家已返回房间。 */
    static final int ROOM_EVENT_RETURNED = 1;

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
                        try {
                            addHostedPeer(s, nextGuestId);
                            nextGuestId++;
                        } catch (IOException e) {
                            try { s.close(); } catch (IOException ignored) {}
                            status = Messages.get(Mp.class, "status_host_failed", e.getMessage());
                        }
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
        localAdmin = false;
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
                addClientPeer(s);
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

    // ---------------------------------------------------------------- 服务器模式（A 模式）

    /**
     * 服务器模式开房：连接大厅服务器，创建房间并等待访客。
     * 游戏世界仍由本机（房主）模拟，现有帧协议不变。
     *
     * @param address 服务器 IP/域名，可带端口（默认 28771）。
     */
    public synchronized void hostViaServer(String address) {
        final String raw = address == null ? "" : address.trim();
        if (raw.isEmpty()) {
            status = Messages.get(Mp.class, "error_empty_address");
            return;
        }
        final String[] hp = parseServerAddress(raw, DEFAULT_PORT);
        final String host = hp[0];
        final int port = Integer.parseInt(hp[1]);
        disconnect();
        serverMode = true;
        serverAddress = host;
        roomCode = null;
        role = Role.HOST;
        running = true;
        status = Messages.get(Mp.class, "status_server_creating");
        worker = new Thread(() -> runHostViaServer(host, port), "mp-lobby-host");
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * 服务器模式加入：连接大厅服务器，输入 6 位房间号等待房主配对。
     *
     * @param address 服务器 IP/域名，可带端口（默认 28771）。
     * @param code 6 位房间号。
     */
    public synchronized void joinViaServer(String address, String code) {
        joinViaServer(address, code, "");
    }

    /**
     * 服务器模式加入（带房间密码；空字符串=无密码）。
     *
     * @param address 服务器 IP/域名，可带端口（默认 28771）。
     * @param code 6 位房间号。
     * @param password 房间密码，可为空。
     */
    public synchronized void joinViaServer(String address, String code, String password) {
        final String raw = address == null ? "" : address.trim();
        final String room = code == null ? "" : code.trim();
        final String pass = password == null ? "" : password.trim();
        if (raw.isEmpty()) {
            status = Messages.get(Mp.class, "error_empty_address");
            return;
        }
        if (!room.matches("\\d{6}")) {
            status = Messages.get(Mp.class, "room_code_invalid");
            return;
        }
        final String[] hp = parseServerAddress(raw, DEFAULT_PORT);
        final String host = hp[0];
        final int port = Integer.parseInt(hp[1]);
        disconnect();
        serverMode = true;
        serverAddress = host;
        roomCode = room;
        role = Role.CLIENT;
        localAdmin = false;
        running = true;
        status = Messages.get(Mp.class, "status_server_join", room);
        final String joinPassword = pass;
        worker = new Thread(() -> runJoinViaServer(host, port, room, joinPassword), "mp-lobby-join");
        worker.setDaemon(true);
        worker.start();
    }

    /**
     * 当前是否处于服务器模式（A 模式大厅配对）。
     *
     * @return 是否服务器模式。
     */
    public boolean isServerMode() {
        return serverMode;
    }

    /**
     * 当前房间号（房主开房成功 / 访客加入后）。
     *
     * @return 6 位房间号；未进入服务器模式时为 null。
     */
    public String getRoomCode() {
        return roomCode;
    }

    /**
     * 当前是否在一个已建立的服务器房间里（大厅/房间/对局中均可）。
     *
     * @return 是否在房间中。
     */
    public boolean isInRoom() {
        return serverMode && roomCode != null && role != Role.NONE;
    }

    /**
     * 当前房间密码（仅房主有效；空串=无密码）。
     *
     * @return 房间密码。
     */
    public String getRoomPassword() {
        return roomPassword;
    }

    /**
     * 房主设置/清除房间密码；空字符串表示无密码。只影响之后加入的访客。
     *
     * @param password 新密码。
     */
    public void setRoomPassword(String password) {
        if (!isHost()) return;
        String clean = password == null ? "" : password.replace('\r', ' ').replace('\n', ' ').trim();
        if (clean.length() > 32) clean = clean.substring(0, 32);
        roomPassword = clean;
        Socket control = lobbySocket;
        DataOutputStream out = lobbyOut;
        if (control != null && !control.isClosed() && out != null) {
            try {
                writeControlLine(out, clean.isEmpty() ? "PASSWORD" : "PASSWORD " + clean);
            } catch (Throwable ignored) {}
        }
        roomUiVersion.incrementAndGet();
    }

    // ---------------------------------------------------------------- 大厅房间列表

    /**
     * 请求大厅房间列表；2 秒内重复调用会被忽略，内部单线程请求，不阻塞 UI。
     */
    public void requestRoomList() {
        requestRoomList(defaultServerAddress());
    }

    /**
     * 请求指定服务器的房间列表。
     *
     * @param address 服务器地址（可带端口）。
     */
    public void requestRoomList(String address) {
        final String raw = address == null ? "" : address.trim();
        if (raw.isEmpty()) return;
        long now = System.currentTimeMillis();
        synchronized (this) {
            if (roomListLoading || now - lastRoomListRequest < 2000L) return;
            roomListLoading = true;
            lastRoomListRequest = now;
        }
        Thread t = new Thread(() -> fetchRoomList(raw), "mp-room-list");
        t.setDaemon(true);
        t.start();
    }

    /** 后台线程：一次 LIST 查询，解析 ROOMS/ROOM/END。 */
    private void fetchRoomList(String address) {
        List<RoomListing> result = new ArrayList<>();
        String error = null;
        Socket s = null;
        try {
            String[] hp = parseServerAddress(address, DEFAULT_PORT);
            s = new Socket();
            s.connect(new InetSocketAddress(hp[0], Integer.parseInt(hp[1])), 3000);
            s.setSoTimeout(3000);
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            writeControlLine(out, "LIST");
            InputStream in = s.getInputStream();
            String header = readControlLine(in);
            if (header == null) throw new IOException("no response");
            if (header.startsWith("ERR ")) {
                error = "BAD_REQUEST".equals(header.substring(4).trim())
                        ? Messages.get(Mp.class, "room_list_failed")
                        : serverErrorText(header);
            } else if (header.startsWith("ROOMS ")) {
                int count;
                try {
                    count = Integer.parseInt(header.substring("ROOMS ".length()).trim());
                } catch (NumberFormatException e) {
                    throw new IOException("bad room count");
                }
                if (count < 0 || count > 10000) throw new IOException("bad room count");
                for (int i = 0; i < count; i++) {
                    String line = readControlLine(in);
                    if (line == null || "END".equals(line)) break;
                    if (!line.startsWith("ROOM ")) continue;
                    String[] p = line.split("\\s+", 6);
                    if (p.length < 5) continue;
                    try {
                        int players = Integer.parseInt(p[2]);
                        int max = Integer.parseInt(p[3]);
                        if (p.length >= 6) {
                            result.add(new RoomListing(p[1], players, max, p[5],
                                    "1".equals(p[4])));
                        } else {
                            // 兼容旧服务器：没有 locked 字段
                            result.add(new RoomListing(p[1], players, max, p[4]));
                        }
                    } catch (NumberFormatException ignored) {
                        // 跳过异常行
                    }
                }
            } else {
                throw new IOException(header);
            }
        } catch (Throwable t) {
            error = Messages.get(Mp.class, "room_list_failed");
        } finally {
            if (s != null) {
                try { s.close(); } catch (IOException ignored) {}
            }
            roomList = Collections.unmodifiableList(result);
            roomListError = error;
            roomListLoading = false;
            roomListVersion.incrementAndGet();
        }
    }

    /**
     * 最近一次房间列表快照。
     *
     * @return 只读列表。
     */
    public List<RoomListing> getRoomList() {
        return roomList;
    }

    /**
     * 房间列表版本号（每次请求完成递增）。
     *
     * @return 版本号。
     */
    public int getRoomListVersion() {
        return roomListVersion.get();
    }

    /**
     * 房间列表是否正在请求。
     *
     * @return 是否请求中。
     */
    public boolean isRoomListLoading() {
        return roomListLoading;
    }

    /**
     * 房间列表最近一次错误（null=成功）。
     *
     * @return 错误文案。
     */
    public String getRoomListError() {
        return roomListError;
    }

    // ---------------------------------------------------------------- 房间预开局状态

    /**
     * 房间当前存档快照。
     *
     * @return 存档快照。
     */
    public RoomSaveInfo getRoomSave() {
        return roomSave;
    }

    /**
     * 房间玩家列表快照。
     *
     * @return 只读玩家列表。
     */
    public List<RoomPlayerInfo> getRoomRoster() {
        return roomRoster;
    }

    /**
     * 房间 UI 版本号：roster/存档/职业变化时递增，场景据此重建。
     *
     * @return 版本号。
     */
    public int getRoomUiVersion() {
        return roomUiVersion.get();
    }

    // ---------------------------------------------------------------- 个人资料（个人设置/个人主页）

    /**
     * 本机资料（懒加载；大厅与房间共用）。
     *
     * @return 本机资料。
     */
    public MpProfile getMyProfile() {
        MpProfile profile = myProfile;
        if (profile == null) {
            synchronized (this) {
                profile = myProfile;
                if (profile == null) {
                    profile = MpProfile.load();
                    myProfile = profile;
                }
            }
        }
        return profile;
    }

    /** 本机在房间/大厅的个人主页快照。 */
    public RoomPlayerInfo getLocalProfileInfo() {
        MpProfile profile = getMyProfile();
        return new RoomPlayerInfo(role == Role.CLIENT && myId >= 0 ? myId : HOST_ID,
                getMyName(), getMyRoomHeroClass(), role == Role.HOST,
                profile.getUuid(), profile.getBadges(), profile.getAllBadges(),
                localAdmin, profile.getHighestScore());
    }

    /** 是否某 id 就是本机玩家（大厅未联机时视为自己，仅用于个人资料窗口）。 */
    public boolean isSelfProfileId(int id) {
        if (role == Role.HOST) return id == HOST_ID;
        if (role == Role.CLIENT) return myId >= 0 && id == myId;
        return true;
    }

    /**
     * 房间内某玩家的资料快照；自己始终读取本机最新资料。
     *
     * @param id 玩家 id。
     * @return 资料；不在房间内/未知 id 返回 null。
     */
    public RoomPlayerInfo getRoomPlayerProfile(int id) {
        if (isSelfProfileId(id)) {
            MpProfile profile = getMyProfile();
            return new RoomPlayerInfo(id, getMyName(), getMyRoomHeroClass(),
                    role == Role.HOST, profile.getUuid(), profile.getBadges(),
                    profile.getAllBadges(), localAdmin, profile.getHighestScore());
        }
        for (RoomPlayerInfo p : roomRoster) {
            if (p.id == id) return p;
        }
        return null;
    }

    /** 本机资料变化（改徽章等）：房主刷新房间状态，访客等待下次 tick 上报。 */
    public void onLocalProfileChanged() {
        profileDirty = true;
        if (isHost()) {
            roomStateDirty = true;
            roomUiVersion.incrementAndGet();
            if (isInRoom()) flushRoomStateIfNeeded();
        }
    }

    /** 访客把本机资料同步给房主（最多每 2 秒或资料变化时）。 */
    public void flushProfileIfNeeded() {
        if (role != Role.CLIENT || !isInRoom()) return;
        long now = System.currentTimeMillis();
        if (!profileDirty && now - lastProfileSent < 2000L) return;
        MpProfile profile = getMyProfile();
        try {
            sendToAll(Proto.PROFILE, encodeProfile(myId, profile.getUuid(),
                    profile.getBadges(), profile.getAllBadges(), profile.getHighestScore()));
            profileDirty = false;
            lastProfileSent = now;
        } catch (Throwable ignored) {}
    }

    /**
     * 按需请求某玩家的历史最高分对局详情。
     *
     * @param targetId 目标玩家 id（不能是自己）。
     */
    public void requestProfileRecord(int targetId) {
        if (isSelfProfileId(targetId)) return;
        long now = System.currentTimeMillis();
        synchronized (profileLock) {
            Long started = profileRequestAt.get(targetId);
            if (profileRecordsLoaded.contains(targetId) && started == null) return;
            if (started != null && now - started < PROFILE_REQUEST_TIMEOUT_MS) return;
            profileRecords.remove(targetId);
            profileRecordsLoaded.remove(targetId);
            profileRequestAt.put(targetId, now);
        }
        try {
            if (isHost()) {
                Peer target = findPeerById(targetId);
                if (target != null) {
                    sendTo(target, Proto.PROFILE_REQ, encodeProfileReq(targetId, HOST_ID));
                }
            } else {
                sendToAll(Proto.PROFILE_REQ, encodeProfileReq(targetId, myId));
            }
        } catch (Throwable ignored) {}
    }

    /** 某玩家的最高分记录是否已返回（含“无记录”结果）。 */
    public boolean isProfileRecordLoaded(int targetId) {
        synchronized (profileLock) {
            return profileRecordsLoaded.contains(targetId);
        }
    }

    /** 已返回的最高分记录；null 表示该玩家没有历史记录或尚未返回。 */
    public Rankings.Record getProfileRecord(int targetId) {
        synchronized (profileLock) {
            return profileRecords.get(targetId);
        }
    }

    /** 请求是否已超过 5 秒仍未返回。 */
    public boolean isProfileRequestTimedOut(int targetId) {
        synchronized (profileLock) {
            Long started = profileRequestAt.get(targetId);
            return started != null && System.currentTimeMillis() - started >= PROFILE_REQUEST_TIMEOUT_MS;
        }
    }

    /** 关闭详情窗口时取消未完成的请求。 */
    public void cancelProfileRequest(int targetId) {
        synchronized (profileLock) {
            profileRequestAt.remove(targetId);
        }
    }

    /** 房主：收到访客的 PROFILE，更新该 peer 的资料并刷新房间状态。 */
    private void applyPeerProfile(Peer p, byte[] data) {
        try {
            ProfilePayload pp = decodeProfile(data);
            p.uuid = pp.uuid;
            p.badges = Collections.unmodifiableList(new ArrayList<>(pp.badges));
            p.allBadges = Collections.unmodifiableList(new ArrayList<>(pp.allBadges));
            p.highestScore = pp.highestScore;
            roomStateDirty = true;
            roomUiVersion.incrementAndGet();
        } catch (IOException ignored) {}
    }

    /** 房主：处理最高分详情请求（自己直接回；访客请求转发给目标访客）。 */
    private void handleProfileRequest(Peer from, ProfileReqPayload req) {
        int requester = from == null ? HOST_ID : from.id;
        int target = req.targetId;
        if (target == HOST_ID) {
            sendProfileRecord(requester, HOST_ID, ownRecordBytes());
            return;
        }
        Peer targetPeer = findPeerById(target);
        if (targetPeer == null) return;   // 目标不在房间，忽略
        try {
            sendTo(targetPeer, Proto.PROFILE_REQ, encodeProfileReq(target, requester));
        } catch (Throwable ignored) {}
    }

    /** 访客：房主转发来的详情请求；只有目标是自己时才回应。 */
    private void handleGuestProfileRequest(Peer from, ProfileReqPayload req) {
        if (from == null || from.id != HOST_ID) return;   // 只接受房主转发的请求
        if (req.targetId != myId) return;
        try {
            sendTo(from, Proto.PROFILE_DATA,
                    encodeProfileData(myId, req.requesterId, ownRecordBytes()));
        } catch (Throwable ignored) {}
    }

    /** PROFILE_DATA 路由：房主回给请求者，请求者保存结果。 */
    private void handleProfileData(Peer from, ProfileDataPayload pd, byte[] raw) {
        if (isHost()) {
            // 只接受目标玩家本人发来的记录，防止伪造他人资料
            if (from == null || from.id != pd.targetId) return;
            if (pd.requesterId == HOST_ID) {
                storeProfileRecord(pd.targetId, pd.data);
            } else {
                Peer requester = findPeerById(pd.requesterId);
                if (requester != null) {
                    try { sendTo(requester, Proto.PROFILE_DATA, raw); } catch (Throwable ignored) {}
                }
            }
        } else {
            if (from == null || from.id != HOST_ID) return;   // 只信任房主转发的数据
            if (pd.requesterId == myId) {
                storeProfileRecord(pd.targetId, pd.data);
            }
        }
    }

    /** 把某玩家的最高分记录发给请求者；请求者=房主时直接本地保存。 */
    private void sendProfileRecord(int requesterId, int targetId, byte[] data) {
        if (requesterId == HOST_ID) {
            storeProfileRecord(targetId, data);
            return;
        }
        Peer requester = findPeerById(requesterId);
        if (requester == null) return;
        try {
            sendTo(requester, Proto.PROFILE_DATA,
                    encodeProfileData(targetId, requesterId, data));
        } catch (Throwable ignored) {}
    }

    /** 本机最高分记录的网络字节（无记录时为空数组）。 */
    private byte[] ownRecordBytes() {
        Rankings.Record record = MpProfile.bestRecord();
        if (record == null) return new byte[0];
        try {
            return MpProfile.encodeRecord(record);
        } catch (IOException e) {
            return new byte[0];
        }
    }

    /** 保存按需取回的最高分记录（空字节=确认无记录）。 */
    private void storeProfileRecord(int targetId, byte[] data) {
        Rankings.Record record = null;
        if (data != null && data.length > 0) {
            try { record = MpProfile.decodeRecord(data); } catch (Throwable ignored) {}
        }
        synchronized (profileLock) {
            profileRecords.put(targetId, record);
            profileRecordsLoaded.add(targetId);
            profileRequestAt.remove(targetId);
        }
    }

    /**
     * 本机在房间内使用的职业（房主=存档职业或自选；访客=存档职业或自选）。
     *
     * @return 职业；null=未选择。
     */
    public HeroClass getMyRoomHeroClass() {
        if (isHost()) return effectiveHostClass();
        return guestRoomHeroLocked ? guestLockCacheClass : guestRoomClass;
    }

    /**
     * 访客是否因该世界已有英雄档而无法改职业。
     *
     * @return 是否锁定。
     */
    public boolean isGuestHeroLocked() {
        return role == Role.CLIENT && guestRoomHeroLocked;
    }

    /**
     * 房主「开始游戏」是否可用。
     *
     * @return 是否可用。
     */
    public boolean canHostStartGame() {
        return isHost() && isInRoom() && roomSave.selected
                && roomSave.seed != Long.MIN_VALUE
                && (!roomSave.isNew || hostRoomClass != null);
    }

    /**
     * 是否正在房间的存档选择界面。
     *
     * @return 是否选档中。
     */
    public boolean isSelectingRoomSave() {
        return selectingRoomSave;
    }

    /** 进入房间选档模式（StartScene 读取）。 */
    public void beginRoomSaveSelect() {
        selectingRoomSave = true;
    }

    /** 取消房间选档（返回房间）。 */
    public void cancelRoomSaveSelect() {
        selectingRoomSave = false;
        roomNewGameSlot = -1;
    }

    /**
     * StartScene 房间模式：选择某个存档槽位。
     *
     * <p>已有档读取 slot/seed/职业/层数；空档生成随机种子并记录为待创建新档。</p>
     *
     * @param slot 存档槽位。
     */
    public void selectRoomSave(int slot) {
        if (!isHost() || !serverMode) return;
        roomNewGameSlot = -1;
        GamesInProgress.setUnknown(slot);
        GamesInProgress.Info info = GamesInProgress.check(slot);
        if (info != null) {
            if (info.seed < 0) {
                status = Messages.get(Mp.class, "room_save_invalid");
                selectingRoomSave = false;
                return;
            }
            hostSelectExistingRoomSave(info);
        } else {
            hostSelectNewRoomSave(slot);
        }
        selectingRoomSave = false;
    }

    /**
     * 房主选择已有存档（由 StartScene 调用）。
     *
     * @param info 存档信息。
     */
    public void hostSelectExistingRoomSave(GamesInProgress.Info info) {
        if (!isHost() || info == null) return;
        long oldSeed = roomSave.selected ? roomSave.seed : Long.MIN_VALUE;
        roomSave = new RoomSaveInfo(true, info.slot, info.seed, false,
                info.heroClass, info.depth, info.level, false);
        if (oldSeed != info.seed) clearPeerRoomClasses();
        roomStateDirty = true;
        roomUiVersion.incrementAndGet();
    }

    /**
     * 房主选择空存档位，生成随机种子（真正写盘在首次进入 GameScene 时）。
     *
     * @param slot 空槽位。
     */
    public void hostSelectNewRoomSave(int slot) {
        if (!isHost()) return;
        long seed = DungeonSeed.randomSeed();
        long oldSeed = roomSave.selected ? roomSave.seed : Long.MIN_VALUE;
        roomSave = new RoomSaveInfo(true, slot, seed, true,
                hostRoomClass, 0, 1, false);
        if (oldSeed != seed) clearPeerRoomClasses();
        roomStateDirty = true;
        roomUiVersion.incrementAndGet();
    }

    /**
     * 房间「选择存档」中点了空槽：进入原版新建游戏界面配置职业/种子/挑战。
     *
     * @param slot 空槽位。
     */
    public void beginRoomNewGameConfig(int slot) {
        if (!isHost() || !serverMode) return;
        roomNewGameSlot = slot;
        selectingRoomSave = true;
    }

    /** 是否正在房间的新档配置界面（HeroSelectScene）。 */
    public boolean isRoomNewGameConfig() {
        return roomNewGameSlot >= 0;
    }

    /** 取消房间新档配置（从新建界面返回选档界面时调用）。 */
    public void cancelRoomNewGameConfig() {
        roomNewGameSlot = -1;
    }

    /**
     * 原版新建游戏界面点开始时调用：记录职业与种子并返回房间，而不是直接开局。
     *
     * <p>挑战/难度/娱乐模式等设置由 SPDSettings 保存，真正开局（房间「开始游戏」）
     * 时 {@code Dungeon.init()} 会读取当前值。</p>
     *
     * @param cls 房主选择的职业。
     * @return 是否成功记录。
     */
    public boolean finishRoomNewGameConfig(HeroClass cls) {
        if (!isHost() || !serverMode || cls == null || roomNewGameSlot < 0) return false;
        int slot = roomNewGameSlot;
        String custom = SPDSettings.customSeed();
        long seed;
        if (custom != null && !custom.trim().isEmpty()) {
            seed = DungeonSeed.convertFromText(custom.trim());
        } else {
            seed = DungeonSeed.randomSeed();
        }
        if (seed < 0) seed = DungeonSeed.randomSeed();
        long oldSeed = roomSave.selected ? roomSave.seed : Long.MIN_VALUE;
        hostRoomClass = cls;
        roomSave = new RoomSaveInfo(true, slot, seed, true, cls, 0, 1, false);
        if (oldSeed != seed) clearPeerRoomClasses();
        roomNewGameSlot = -1;
        selectingRoomSave = false;
        roomStateDirty = true;
        roomUiVersion.incrementAndGet();
        flushRoomStateIfNeeded();
        return true;
    }

    /**
     * 房主在房间内选择职业（已有存档时锁定）。
     *
     * @param cls 职业。
     */
    public void hostChooseRoomHero(HeroClass cls) {
        if (!isHost() || cls == null) return;
        if (roomSave.selected && !roomSave.isNew) {
            status = Messages.get(Mp.class, "room_hero_locked");
            return;
        }
        hostRoomClass = cls;
        roomStateDirty = true;
        roomUiVersion.incrementAndGet();
        flushRoomStateIfNeeded();
    }

    /**
     * 访客在房间内选择职业（已有该世界英雄档时锁定）。
     *
     * @param cls 职业。
     */
    public void guestChooseRoomHero(HeroClass cls) {
        if (role != Role.CLIENT || cls == null) return;
        if (guestRoomHeroLocked) {
            status = Messages.get(Mp.class, "room_hero_locked");
            return;
        }
        guestRoomClass = cls;
        if (roomSave.selected) guestRoomClassSeed = roomSave.seed;
        lastRoomHeroSentSeed = Long.MIN_VALUE;
        lastRoomHeroSent = null;
        roomUiVersion.incrementAndGet();
        guestSyncHeroForRoom();
    }

    /**
     * 访客是否可以从房间直接进入已有对局（房主仍在游戏中且有本地世界存档）。
     *
     * @return 是否可进入。
     */
    public boolean canGuestEnterFromRoom() {
        if (role != Role.CLIENT || !isInRoom()) return false;
        RoomSaveInfo save = roomSave;
        return save.selected && !save.isNew && save.seed != Long.MIN_VALUE
                && hasGuestWorldForSeed(save.seed);
    }

    /**
     * 访客在房主仍对局时从房间进入自己的持久世界（不等待房主重新发 STATE）。
     */
    public void guestEnterFromRoom() {
        if (role != Role.CLIENT || !isInRoom()) return;
        RoomSaveInfo save = roomSave;
        if (!save.selected || save.isNew || save.seed == Long.MIN_VALUE) {
            status = Messages.get(Mp.class, "room_start_no_save");
            return;
        }
        if (!hasGuestWorldForSeed(save.seed)) {
            status = Messages.get(Mp.class, "room_enter_no_local_world");
            return;
        }
        long previousSeed = worldSeed;
        worldSeed = save.seed;
        Bundle guest = loadGuestHeroBundle();
        if (guest == null) guest = loadGuestHeroFromWorld();
        if (guest == null) {
            worldSeed = previousSeed;
            status = Messages.get(Mp.class, "room_enter_no_local_world");
            return;
        }
        if (!enterGuestWorld(guest, false)) {
            worldSeed = previousSeed;
        }
    }

    /**
     * 访客根据当前房间存档同步英雄：已有该世界英雄档则锁定，否则把房间选择发给房主。
     */
    public void guestSyncHeroForRoom() {
        if (role != Role.CLIENT || !serverMode) return;
        RoomSaveInfo save = roomSave;
        if (!save.selected || save.seed == Long.MIN_VALUE) {
            if (guestRoomHeroLocked) {
                guestRoomHeroLocked = false;
                guestLockCacheValid = false;
                roomUiVersion.incrementAndGet();
            }
            // 还没选存档也可以先把房间内选的职业发给房主显示
            if (guestRoomClass != null && myId >= 0
                    && (lastRoomHeroSentSeed != Long.MIN_VALUE || lastRoomHeroSent != guestRoomClass)) {
                try {
                    sendToAll(Proto.HERO, encodeHero(myId, guestRoomClass));
                } catch (Throwable ignored) {}
                lastRoomHeroSentSeed = Long.MIN_VALUE;
                lastRoomHeroSent = guestRoomClass;
            }
            return;
        }
        long seed = save.seed;
        if (!guestLockCacheValid || guestLockCacheSeed != seed) {
            guestLockCacheSeed = seed;
            guestLockCacheValid = true;
            boolean has = hasGuestHeroFile(seed);
            guestLockCacheClass = has ? guestHeroClassForSeed(seed) : null;
            guestRoomHeroLocked = has;
            guestRoomClassSeed = seed;
            if (has) {
                lastRoomHeroSentSeed = Long.MIN_VALUE;
                lastRoomHeroSent = null;
            }
            roomUiVersion.incrementAndGet();
        }
        HeroClass cls = guestRoomHeroLocked ? guestLockCacheClass : guestRoomClass;
        if (cls != null && myId >= 0
                && (lastRoomHeroSentSeed != seed || lastRoomHeroSent != cls)) {
            try {
                sendToAll(Proto.HERO, encodeHero(myId, cls));
            } catch (Throwable ignored) {}
            lastRoomHeroSentSeed = seed;
            lastRoomHeroSent = cls;
        }
    }

    /** 访客本机在该世界种子下是否有英雄档（含 .bak）。 */
    private boolean hasGuestHeroFile(long seed) {
        String file = mpHeroFile(seed);
        if (file == null) return false;
        try {
            FileHandle f = FileUtils.getFileHandle(file);
            if (f.exists() && f.length() > 1) return true;
            FileHandle bak = FileUtils.getFileHandle(file + ".bak");
            return bak.exists() && bak.length() > 1;
        } catch (Throwable t) {
            return false;
        }
    }

    /** 只读取指定世界种子的访客英雄职业（不修改 worldSeed）。 */
    private HeroClass guestHeroClassForSeed(long seed) {
        Bundle b = loadGuestHeroBundleForSeed(seed);
        if (b == null) return null;
        try {
            Bundle hero = guestHeroPart(b);
            if (hero != null && hero.contains("class")) {
                return hero.getEnum("class", HeroClass.class);
            }
        } catch (Throwable ignored) {}
        return null;
    }

    /** 只读指定种子的访客英雄档（主档损坏时回退 .bak）。 */
    private Bundle loadGuestHeroBundleForSeed(long seed) {
        String file = mpHeroFile(seed);
        if (file == null) return null;
        try {
            FileHandle f = FileUtils.getFileHandle(file);
            if (f.exists()) {
                Bundle b = FileUtils.bundleFromFile(file);
                if (b != null && guestHeroPart(b) != null) return b;
            }
        } catch (Throwable ignored) {}
        try {
            FileHandle bak = FileUtils.getFileHandle(file + ".bak");
            if (bak.exists()) {
                Bundle b = FileUtils.bundleFromFile(file + ".bak");
                if (b != null && guestHeroPart(b) != null) return b;
            }
        } catch (Throwable ignored) {}
        return null;
    }

    /** 房主清除所有 peer 的旧职业（换存档种子后由 ROOM_STATE 触发重发）。 */
    private void clearPeerRoomClasses() {
        synchronized (peers) {
            for (Peer p : peers) {
                p.heroClass = null;
            }
        }
        roomUiVersion.incrementAndGet();
    }

    /** 房主实际生效的职业：已有存档=存档职业，新档=房间选择。 */
    private HeroClass effectiveHostClass() {
        if (roomSave.selected && !roomSave.isNew) return roomSave.hostClass;
        return hostRoomClass;
    }

    /** 房主构建权威 roster（自己 + 所有 peer）。 */
    private List<RoomPlayerInfo> buildRoomRoster() {
        List<RoomPlayerInfo> list = new ArrayList<>();
        MpProfile profile = getMyProfile();
        list.add(new RoomPlayerInfo(HOST_ID, getMyName(), effectiveHostClass(), true,
                profile.getUuid(), profile.getBadges(), profile.getAllBadges(),
                localAdmin, profile.getHighestScore()));
        List<Peer> snapshot;
        synchronized (peers) {
            snapshot = new ArrayList<>(peers);
        }
        for (Peer p : snapshot) {
            if (p.id <= 0) continue;
            list.add(new RoomPlayerInfo(p.id, displayName(p.name), p.heroClass, false,
                    p.uuid, p.badges, p.allBadges, p.admin, p.highestScore));
        }
        return list;
    }

    /** 房主：把当前 roster/存档广播给所有已完成握手的访客。 */
    private void broadcastRoomState() {
        if (role != Role.HOST || !serverMode) return;
        List<RoomPlayerInfo> roster = buildRoomRoster();
        roomRoster = Collections.unmodifiableList(roster);
        byte[] payload;
        try {
            payload = encodeRoomState(roomSave, roster);
        } catch (IOException e) {
            status = Messages.get(Mp.class, "room_state_encode_failed");
            return;
        }
        List<Peer> snapshot;
        synchronized (peers) {
            snapshot = new ArrayList<>(peers);
        }
        for (Peer p : snapshot) {
            if (!p.versionOk) continue;
            try { sendTo(p, Proto.ROOM_STATE, payload); } catch (Throwable ignored) {}
        }
        roomStateDirty = false;
        lastRoomStateSent = System.currentTimeMillis();
    }

    /** 房主：有变化或每 2 秒补发房间状态。 */
    public void flushRoomStateIfNeeded() {
        if (role != Role.HOST || !serverMode) return;
        long now = System.currentTimeMillis();
        if (roomStateDirty || now - lastRoomStateSent >= 2000L) {
            broadcastRoomState();
        }
    }

    /** 房间场景每帧调用：房间状态补发 + 访客英雄同步 + PING 保活。 */
    public void tickRoom() {
        if (!serverMode) return;
        if (role == Role.HOST) flushRoomStateIfNeeded();
        if (role == Role.CLIENT) {
            guestSyncHeroForRoom();
            flushProfileIfNeeded();
        }
        tickPing();
    }

    /**
     * 房主开始/继续对局（从房间场景调用）。
     *
     * @return 是否成功发起。
     */
    public boolean startGameFromRoom() {
        if (!isHost() || !isInRoom()) return false;
        RoomSaveInfo save = roomSave;
        if (!save.selected || save.seed == Long.MIN_VALUE) {
            status = Messages.get(Mp.class, "room_start_no_save");
            return false;
        }
        HeroClass hostCls = effectiveHostClass();
        if (save.isNew && hostCls == null) {
            status = Messages.get(Mp.class, "room_start_no_hero");
            return false;
        }
        roomSave = new RoomSaveInfo(true, save.slot, save.seed, save.isNew,
                save.hostClass, save.depth, save.level, true);
        synchronized (peers) {
            for (Peer p : peers) {
                p.gotState = false;
                p.worldResetPending = false;
            }
        }
        GamesInProgress.curSlot = save.slot;
        GamesInProgress.selectedClass = hostCls;
        Dungeon.hero = null;
        ActionIndicator.action = null;
        if (save.isNew) {
            Dungeon.daily = false;
            Dungeon.dailyReplay = false;
            Dungeon.overrideSeed = save.seed;
            roomSeedPending = true;
            pendingRoomSaveWrite = true;
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
        } else {
            Dungeon.overrideSeed = -1;
            roomSeedPending = false;
            pendingRoomSaveWrite = false;
            InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
        }
        roomStateDirty = true;
        roomUiVersion.incrementAndGet();
        broadcastRoomState();
        Game.switchScene(InterlevelScene.class);
        return true;
    }

    /** InterlevelScene 在 Dungeon.init() 之后调用：复位一次性新档种子覆盖。 */
    public void onHostSeedConsumed() {
        if (roomSeedPending) {
            Dungeon.overrideSeed = -1;
            roomSeedPending = false;
        }
    }

    /** 房主侧：用磁盘存档刷新房间存档快照（新档首次落盘后 isNew→false）。 */
    private void refreshRoomSaveAfterGame() {
        RoomSaveInfo save = roomSave;
        if (!save.selected) return;
        GamesInProgress.setUnknown(save.slot);
        GamesInProgress.Info info = GamesInProgress.check(save.slot);
        if (info != null) {
            roomSave = new RoomSaveInfo(true, save.slot, info.seed, false,
                    info.heroClass, info.depth, info.level, true);
        } else {
            roomSave = new RoomSaveInfo(true, save.slot, save.seed, save.isNew,
                    save.hostClass, save.depth, save.level, true);
        }
        roomStateDirty = true;
        roomUiVersion.incrementAndGet();
    }

    /** 房主从对局返回房间：保存、通知访客回房间、重置 STATE 标记。 */
    public void hostReturnToRoom() {
        if (!isHost() || !isInRoom()) return;
        systemNotice(Messages.get(Mp.class, "notice_returned_room", getMyName()));
        try {
            if (Dungeon.hero != null && Dungeon.level != null) Dungeon.saveAll();
        } catch (Throwable ignored) {}
        refreshRoomSaveAfterGame();
        List<Peer> snapshot;
        synchronized (peers) {
            snapshot = new ArrayList<>(peers);
            for (Peer p : peers) {
                p.worldResetPending = false;
                p.remote = null;
                p.remoteSprite = null;
                p.nameLabel = null;
                p.hpBg = null;
                p.hpFill = null;
                p.remotePos = -1;
                p.remoteDepth = -1;
                p.remoteBranch = -1;
                p.remoteStale = false;
            }
        }
        for (Peer p : snapshot) {
            try { sendTo(p, Proto.KICK, new byte[0]); } catch (Throwable ignored) {}
        }
        hostWorldKey = null;
        lastScene = null;
        lastSentPos = -1;
        roomStateDirty = true;
        broadcastRoomState();
        if (!(Game.scene() instanceof MpRoomScene)) {
            ShatteredPixelDungeon.switchScene(MpRoomScene.class);
        }
    }

    /** 访客从对局返回房间：保存英雄/世界、清理临时槽，然后回房间等待房主。 */
    public void clientReturnToRoom() {
        clientReturnToRoom(true);
    }

    /**
     * 访客从对局返回房间。
     *
     * @param announce 是否在事件栏播报「已返回房间」（房主已统一播报时传 false）。
     */
    private void clientReturnToRoom(boolean announce) {
        if (role != Role.CLIENT || !isInRoom()) {
            if (Game.scene() instanceof GameScene) {
                ShatteredPixelDungeon.switchScene(TitleScene.class);
            }
            return;
        }
        if (Game.scene() instanceof MpRoomScene) {
            guestSyncHeroForRoom();
            return;
        }
        // 通知房主/其他玩家：自己已回到房间（房主会转发）
        try {
            sendToAll(Proto.ROOM_EVENT, encodeRoomEvent(myId, ROOM_EVENT_RETURNED));
        } catch (Throwable ignored) {}
        if (announce) {
            systemNotice(Messages.get(Mp.class, "notice_returned_room", getMyName()));
        }
        pendingState = null;
        final WndHeroClassSelect w = classWindow;
        classWindow = null;
        if (w != null) w.hide();
        boolean ok = !guestWorldActive || saveGuestHero();
        if (ok) {
            saveGuestWorldFromSlot();
            if (!enteringNewWorld) {
                clearGuestTempSlot();
                guestWorldActive = false;
                guestWorldPersistent = false;
                worldSeed = Long.MIN_VALUE;
            }
        } else {
            status = Messages.get(Mp.class, "status_client_exit_save_failed");
        }
        enteringNewWorld = false;
        lastScene = null;
        lastSentPos = -1;
        // 刚保存了该世界的英雄档：强制重新检查，否则同种子下会继续沿用“无存档”缓存
        guestLockCacheValid = false;
        guestLockCacheSeed = Long.MIN_VALUE;
        guestSyncHeroForRoom();
        ShatteredPixelDungeon.switchScene(MpRoomScene.class);
    }

    /** ROOM_STATE 编解码：房主 roster + 当前存档。 */
    static byte[] encodeRoomState(RoomSaveInfo save, List<RoomPlayerInfo> players) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(3);
        dos.writeBoolean(save.selected);
        dos.writeBoolean(save.isNew);
        dos.writeBoolean(save.started);
        dos.writeInt(save.slot);
        dos.writeLong(save.seed);
        dos.writeInt(save.depth);
        dos.writeInt(save.level);
        dos.writeInt(players.size());
        for (RoomPlayerInfo p : players) {
            dos.writeInt(p.id);
            dos.writeBoolean(p.isHost);
            dos.writeInt(p.heroClass == null ? -1 : p.heroClass.ordinal());
            writeNetString(dos, p.name, 128);
            writeNetString(dos, p.uuid, 64);
            int badgeCount = p.badges == null ? 0
                    : Math.min(MpProfile.MAX_BADGES, p.badges.size());
            dos.writeInt(badgeCount);
            for (int i = 0; i < badgeCount; i++) {
                writeNetString(dos, p.badges.get(i).name(), 64);
            }
            int allBadgeCount = p.allBadges == null ? 0
                    : Math.min(MAX_PROFILE_BADGES, p.allBadges.size());
            dos.writeInt(allBadgeCount);
            for (int i = 0; i < allBadgeCount; i++) {
                writeNetString(dos, p.allBadges.get(i).name(), 64);
            }
            dos.writeBoolean(p.isAdmin);
            dos.writeInt(Math.max(0, p.highestScore));
        }
        dos.flush();
        return bos.toByteArray();
    }

    /** 写带长度前缀的 UTF-8 字符串（超长截断）。 */
    static void writeNetString(DataOutputStream dos, String value, int maxBytes) throws IOException {
        byte[] bytes = (value == null ? "" : value).getBytes("UTF-8");
        if (bytes.length > maxBytes) {
            byte[] cut = new byte[maxBytes];
            System.arraycopy(bytes, 0, cut, 0, maxBytes);
            bytes = cut;
        }
        dos.writeInt(bytes.length);
        dos.write(bytes);
    }

    /** 读带长度前缀的 UTF-8 字符串（长度上限防御）。 */
    static String readNetString(DataInputStream di, int maxBytes) throws IOException {
        int length = di.readInt();
        if (length < 0 || length > maxBytes || length > di.available()) {
            throw new IOException("bad string length");
        }
        byte[] bytes = new byte[length];
        di.readFully(bytes);
        return new String(bytes, "UTF-8");
    }

    /** ROOM_STATE 解码结果。 */
    static class RoomStatePayload {
        RoomSaveInfo save;
        List<RoomPlayerInfo> players;
    }

    /** ROOM_EVENT 帧载荷：谁（id）做了什么（event）。 */
    static class RoomEventPayload {
        int id;
        int event;
    }

    /** 编码 ROOM_EVENT 帧：玩家 id + 事件类型。 */
    static byte[] encodeRoomEvent(int id, int event) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(id);
        dos.writeInt(event);
        dos.flush();
        return bos.toByteArray();
    }

    /** 解码 ROOM_EVENT 帧。 */
    static RoomEventPayload decodeRoomEvent(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        RoomEventPayload p = new RoomEventPayload();
        p.id = di.readInt();
        p.event = di.readInt();
        return p;
    }

    /** PROFILE 帧载荷：个人资料摘要（UUID/徽章/最高分）。 */
    static class ProfilePayload {
        int id;
        String uuid = "";
        List<Badges.Badge> badges = Collections.emptyList();
        List<Badges.Badge> allBadges = Collections.emptyList();
        int highestScore;
    }

    /** PROFILE_REQ 帧载荷：目标玩家 + 请求者。 */
    static class ProfileReqPayload {
        int targetId;
        int requesterId;
    }

    /** PROFILE_DATA 帧载荷：目标玩家 + 请求者 + 排行榜记录 bundle 字节（0 长度=无记录）。 */
    static class ProfileDataPayload {
        int targetId;
        int requesterId;
        byte[] data = new byte[0];
    }

    /** 编码 PROFILE 帧。 */
    static byte[] encodeProfile(int id, String uuid, List<Badges.Badge> badges,
                                List<Badges.Badge> allBadges, int highestScore)
            throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(id);
        writeNetString(dos, uuid, 64);
        int count = badges == null ? 0 : Math.min(MpProfile.MAX_BADGES, badges.size());
        dos.writeInt(count);
        for (int i = 0; i < count; i++) {
            writeNetString(dos, badges.get(i).name(), 64);
        }
        int allCount = allBadges == null ? 0 : Math.min(MAX_PROFILE_BADGES, allBadges.size());
        dos.writeInt(allCount);
        for (int i = 0; i < allCount; i++) {
            writeNetString(dos, allBadges.get(i).name(), 64);
        }
        dos.writeInt(Math.max(0, highestScore));
        dos.flush();
        return bos.toByteArray();
    }

    /** 解码 PROFILE 帧。 */
    static ProfilePayload decodeProfile(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        ProfilePayload p = new ProfilePayload();
        p.id = di.readInt();
        p.uuid = readNetString(di, 64);
        int count = di.readInt();
        if (count < 0 || count > MpProfile.MAX_BADGES) {
            throw new IOException("bad profile badge count");
        }
        List<Badges.Badge> badges = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String name = readNetString(di, 64);
            try {
                badges.add(Badges.Badge.valueOf(name));
            } catch (IllegalArgumentException ignored) {}
        }
        p.badges = badges;
        int allCount = di.readInt();
        if (allCount < 0 || allCount > MAX_PROFILE_BADGES) {
            throw new IOException("bad profile all badge count");
        }
        List<Badges.Badge> all = new ArrayList<>();
        for (int i = 0; i < allCount; i++) {
            String name = readNetString(di, 64);
            try {
                all.add(Badges.Badge.valueOf(name));
            } catch (IllegalArgumentException ignored) {}
        }
        p.allBadges = all;
        p.highestScore = Math.max(0, di.readInt());
        return p;
    }

    /** 编码 PROFILE_REQ 帧。 */
    static byte[] encodeProfileReq(int targetId, int requesterId) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(targetId);
        dos.writeInt(requesterId);
        dos.flush();
        return bos.toByteArray();
    }

    /** 解码 PROFILE_REQ 帧。 */
    static ProfileReqPayload decodeProfileReq(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        ProfileReqPayload p = new ProfileReqPayload();
        p.targetId = di.readInt();
        p.requesterId = di.readInt();
        return p;
    }

    /** 编码 PROFILE_DATA 帧。 */
    static byte[] encodeProfileData(int targetId, int requesterId, byte[] record)
            throws IOException {
        byte[] body = record == null ? new byte[0] : record;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(12 + body.length);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(targetId);
        dos.writeInt(requesterId);
        dos.writeInt(body.length);
        if (body.length > 0) dos.write(body);
        dos.flush();
        return bos.toByteArray();
    }

    /** 解码 PROFILE_DATA 帧。 */
    static ProfileDataPayload decodeProfileData(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        ProfileDataPayload p = new ProfileDataPayload();
        p.targetId = di.readInt();
        p.requesterId = di.readInt();
        int length = di.readInt();
        if (length < 0 || length > 4 * 1024 * 1024 || length > di.available()) {
            throw new IOException("bad profile record length");
        }
        byte[] body = new byte[length];
        if (length > 0) di.readFully(body);
        p.data = body;
        return p;
    }

    static RoomStatePayload decodeRoomState(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        int format = di.readInt();
        if (format != 1 && format != 2 && format != 3) {
            throw new IOException("bad room state format");
        }
        boolean selected = di.readBoolean();
        boolean isNew = di.readBoolean();
        boolean started = di.readBoolean();
        int slot = di.readInt();
        long seed = di.readLong();
        int depth = di.readInt();
        int level = di.readInt();
        int count = di.readInt();
        if (count < 1 || count > MAX_PLAYERS) throw new IOException("bad room player count");
        List<RoomPlayerInfo> players = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int id = di.readInt();
            boolean isHost = di.readBoolean();
            int ord = di.readInt();
            HeroClass cls = null;
            if (ord >= 0) {
                if (ord >= HeroClass.values().length) throw new IOException("bad class ordinal");
                cls = HeroClass.values()[ord];
            }
            String name = readNetString(di, 128);
            String uuid = "";
            List<Badges.Badge> badges = Collections.emptyList();
            List<Badges.Badge> allBadges = Collections.emptyList();
            boolean isAdmin = false;
            int highestScore = 0;
            if (format >= 2) {
                uuid = readNetString(di, 64);
                int badgeCount = di.readInt();
                if (badgeCount < 0 || badgeCount > MpProfile.MAX_BADGES) {
                    throw new IOException("bad badge count");
                }
                List<Badges.Badge> list = new ArrayList<>();
                for (int b = 0; b < badgeCount; b++) {
                    String badgeName = readNetString(di, 64);
                    try {
                        list.add(Badges.Badge.valueOf(badgeName));
                    } catch (IllegalArgumentException ignored) {}
                }
                badges = list;
                if (format >= 3) {
                    int allCount = di.readInt();
                    if (allCount < 0 || allCount > MAX_PROFILE_BADGES) {
                        throw new IOException("bad all badge count");
                    }
                    List<Badges.Badge> all = new ArrayList<>();
                    for (int b = 0; b < allCount; b++) {
                        String badgeName = readNetString(di, 64);
                        try {
                            all.add(Badges.Badge.valueOf(badgeName));
                        } catch (IllegalArgumentException ignored) {}
                    }
                    allBadges = all;
                }
                isAdmin = di.readBoolean();
                highestScore = Math.max(0, di.readInt());
            }
            players.add(new RoomPlayerInfo(id, name, cls, isHost,
                    uuid, badges, allBadges, isAdmin, highestScore));
        }
        HeroClass hostCls = null;
        for (RoomPlayerInfo p : players) {
            if (p.isHost) {
                hostCls = p.heroClass;
                break;
            }
        }
        RoomStatePayload out = new RoomStatePayload();
        out.save = new RoomSaveInfo(selected, slot, seed, isNew, hostCls, depth, level, started);
        out.players = players;
        return out;
    }

    /** 访客：应用房主广播的房间状态。 */
    private void applyRoomState(byte[] data) {
        RoomStatePayload p;
        try {
            p = decodeRoomState(data);
        } catch (IOException e) {
            status = Messages.get(Mp.class, "room_state_decode_failed", e.getMessage());
            return;
        }
        long oldSeed = roomSave.selected ? roomSave.seed : Long.MIN_VALUE;
        roomSave = p.save;
        roomRoster = Collections.unmodifiableList(p.players);
        if (oldSeed != roomSave.seed) {
            guestLockCacheValid = false;
            lastRoomHeroSentSeed = Long.MIN_VALUE;
            lastRoomHeroSent = null;
            if (guestRoomHeroLocked) guestRoomHeroLocked = false;
        }
        if (myId >= 0) {
            boolean mineHasClass = false;
            for (RoomPlayerInfo r : p.players) {
                if (r.id == myId) {
                    mineHasClass = r.heroClass != null;
                    break;
                }
            }
            if (!mineHasClass) {
                lastRoomHeroSentSeed = Long.MIN_VALUE;
                lastRoomHeroSent = null;
            }
        }
        String sig = roomStateSignature(p.save, p.players);
        if (!sig.equals(roomStateSignature)) {
            roomStateSignature = sig;
            roomUiVersion.incrementAndGet();
        }
        guestSyncHeroForRoom();
    }

    /** 房间状态内容签名（用于判断是否需要刷新 UI）。 */
    private static String roomStateSignature(RoomSaveInfo save, List<RoomPlayerInfo> players) {
        StringBuilder sb = new StringBuilder();
        sb.append(save.selected).append('|').append(save.isNew).append('|')
                .append(save.slot).append('|').append(save.seed).append('|')
                .append(save.depth).append('|').append(save.level).append('|')
                .append(save.started).append('|')
                .append(save.hostClass == null ? -1 : save.hostClass.ordinal()).append('\n');
        for (RoomPlayerInfo p : players) {
            sb.append(p.id).append('|').append(p.isHost).append('|')
                    .append(p.heroClass == null ? -1 : p.heroClass.ordinal()).append('|')
                    .append(p.name).append('|').append(p.uuid).append('|')
                    .append(p.isAdmin).append('|').append(p.highestScore).append('|');
            if (p.badges != null) {
                for (Badges.Badge badge : p.badges) sb.append(badge.name()).append(',');
            }
            sb.append('|');
            if (p.allBadges != null) {
                for (Badges.Badge badge : p.allBadges) sb.append(badge.name()).append(',');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /** 访客：本机在房间内选择的职业（已被存档锁定时返回 null，走存档优先逻辑）。 */
    private HeroClass roomHeroClassForWorld(long seed) {
        if (role != Role.CLIENT) return null;
        if (guestRoomHeroLocked) return null;
        return guestRoomClass;
    }

    /** 房主侧：连接大厅、创建房间、接收 GUEST 并建立数据连接。 */
    private void runHostViaServer(String host, int port) {
        Socket control = null;
        try {
            control = new Socket();
            control.connect(new InetSocketAddress(host, port), 8000);
            control.setTcpNoDelay(true);
            lobbySocket = control;
            DataOutputStream out = new DataOutputStream(control.getOutputStream());
            lobbyOut = out;
            String name = getMyName().replace('\r', ' ').replace('\n', ' ').trim();
            if (name.isEmpty()) name = "host";
            writeControlLine(out, "CREATE " + name);
            control.setSoTimeout(10000);
            String response = readControlLine(control.getInputStream());
            if (response == null) throw new IOException(Messages.get(Mp.class, "server_room_closed"));
            if (response.startsWith("ERR ")) throw new IOException(response);
            if (!response.startsWith("ROOM ")) {
                throw new IOException(Messages.get(Mp.class, "server_err_protocol", response));
            }
            String[] parts = response.split("\\s+");
            if (parts.length < 3) {
                throw new IOException(Messages.get(Mp.class, "server_err_protocol", response));
            }
            roomCode = parts[1];
            serverDataPort = Integer.parseInt(parts[2]);
            roomUiVersion.incrementAndGet();
            control.setSoTimeout(0);
            status = Messages.get(Mp.class, "status_server_room", roomCode);
            startLobbyHeartbeat(control, out);
            while (running && lobbySocket == control) {
                String line = readControlLine(control.getInputStream());
                if (line == null) break;
                if (line.startsWith("GUEST ")) {
                    String[] guest = line.split("\\s+");
                    if (guest.length >= 3) {
                        try {
                            int id = Integer.parseInt(guest[1]);
                            final String token = guest[2];
                            Thread t = new Thread(() -> connectHostData(id, token), "mp-server-data");
                            t.setDaemon(true);
                            t.start();
                        } catch (NumberFormatException ignored) {}
                    }
                } else if ("PONG".equals(line)) {
                    // 心跳回应，无需处理。
                } else if (line.startsWith("ERR ")) {
                    status = serverErrorText(line);
                }
            }
            if (running && lobbySocket == control) {
                lobbySocket = null;
                lobbyOut = null;
                handleServerRoomLost(Messages.get(Mp.class, "server_room_closed"));
            }
        } catch (Throwable t) {
            if (running) {
                String detail = (t.getMessage() != null && !t.getMessage().isEmpty())
                        ? t.getMessage() : t.getClass().getSimpleName();
                final String msg = detail.startsWith("ERR ")
                        ? serverErrorText(detail)
                        : Messages.get(Mp.class, "server_err_connect");
                lobbySocket = null;
                lobbyOut = null;
                handleServerRoomLost(msg);
            }
        } finally {
            if (lobbySocket == control) { lobbySocket = null; lobbyOut = null; }
            if (control != null) {
                try { control.close(); } catch (IOException ignored) {}
            }
        }
    }

    /** 访客侧：连接大厅、等待 WAIT/PAIRED，然后按直连流程接入房主 peer。 */
    private void runJoinViaServer(String host, int port, String room, String password) {
        Socket s = null;
        try {
            s = new Socket();
            s.connect(new InetSocketAddress(host, port), 8000);
            s.setTcpNoDelay(true);
            lobbySocket = s;
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            writeControlLine(out, password == null || password.isEmpty()
                    ? "JOIN " + room : "JOIN " + room + " " + password);
            s.setSoTimeout(40000);   // 服务器等待房主建数据连接默认 30 秒，客户端留一点余量
            boolean paired = false;
            while (running && lobbySocket == s) {
                String line = readControlLine(s.getInputStream());
                if (line == null) throw new IOException(Messages.get(Mp.class, "server_room_closed"));
                if ("WAIT".equals(line)) {
                    status = Messages.get(Mp.class, "status_server_waiting_host");
                } else if ("PAIRED".equals(line)) {
                    paired = true;
                    break;
                } else if (line.startsWith("ERR ")) {
                    throw new IOException(line);
                }
            }
            if (!running || lobbySocket != s) return;
            if (!paired) throw new IOException(Messages.get(Mp.class, "server_room_closed"));
            s.setSoTimeout(0);
            addClientPeer(s);
            lobbySocket = null;
            s = null;   // 连接所有权已交给房主 peer
        } catch (Throwable t) {
            if (running) {
                String detail = (t.getMessage() != null && !t.getMessage().isEmpty())
                        ? t.getMessage() : t.getClass().getSimpleName();
                final String msg = detail.startsWith("ERR ")
                        ? serverErrorText(detail)
                        : Messages.get(Mp.class, "server_err_connect");
                status = msg;
                connected = false;
                running = false;   // 与直连 join 一致：避免版本握手超时覆盖失败原因
                Game.runOnRenderThread(() -> {
                    disconnect();
                    showLobbyMessage(msg);
                });
            }
        } finally {
            if (lobbySocket == s) lobbySocket = null;
            if (s != null) {
                try { s.close(); } catch (IOException ignored) {}
            }
        }
    }

    /** 房主侧：收到 GUEST 后连接服务器数据端口，配对成功后登记为访客 peer。 */
    private void connectHostData(int id, String token) {
        Socket data = null;
        try {
            if (!serverMode || !running || serverAddress == null || roomCode == null) return;
            data = new Socket();
            data.connect(new InetSocketAddress(serverAddress, serverDataPort), 8000);
            data.setTcpNoDelay(true);
            DataOutputStream out = new DataOutputStream(data.getOutputStream());
            writeControlLine(out, "DATA " + roomCode + " " + id + " " + token);
            data.setSoTimeout(10000);
            String response = readControlLine(data.getInputStream());
            if (!"OK".equals(response)) {
                throw new IOException(response == null ? "no response" : response);
            }
            data.setSoTimeout(0);
            synchronized (peers) {
                if (!running || !serverMode || peers.size() >= MAX_PLAYERS - 1) {
                    try { data.close(); } catch (IOException ignored) {}
                    return;
                }
            }
            addHostedPeer(data, id);
            data = null;   // 连接所有权已交给访客 peer
        } catch (Throwable t) {
            if (running && serverMode) {
                String detail = (t.getMessage() != null && !t.getMessage().isEmpty())
                        ? t.getMessage() : t.getClass().getSimpleName();
                status = detail.startsWith("ERR ")
                        ? serverErrorText(detail)
                        : Messages.get(Mp.class, "server_err_data");
            }
        } finally {
            if (data != null) {
                try { data.close(); } catch (IOException ignored) {}
            }
        }
    }

    /** 房主控制连接心跳：每 15 秒发 PING；发送失败则关闭控制连接，由读线程收尾。 */
    private void startLobbyHeartbeat(final Socket control, final DataOutputStream out) {
        Thread t = new Thread(() -> {
            while (running && lobbySocket == control) {
                try {
                    Thread.sleep(15000L);
                } catch (InterruptedException e) {
                    return;
                }
                if (!running || lobbySocket != control) return;
                try {
                    writeControlLine(out, "PING");
                } catch (IOException e) {
                    try { control.close(); } catch (IOException ignored) {}
                    return;
                }
            }
        }, "mp-lobby-heartbeat");
        t.setDaemon(true);
        t.start();
    }

    /** 服务器房间/控制连接丢失：房主回到未联机状态并提示。 */
    private void handleServerRoomLost(final String msg) {
        if (!serverMode) return;
        running = false;
        final String reason = msg;
        Game.runOnRenderThread(() -> {
            if (role != Role.HOST) return;
            try {
                if (Dungeon.hero != null && Dungeon.level != null) Dungeon.saveAll();
            } catch (Throwable ignored) {}
            disconnect();
            showLobbyMessage(reason);
        });
    }

    private static String serverErrorText(String line) {
        String code = line == null ? "" : line.trim();
        if (code.startsWith("ERR ")) code = code.substring(4).trim();
        switch (code) {
            case "NOT_FOUND":
                return Messages.get(Mp.class, "server_err_not_found");
            case "FULL":
                return Messages.get(Mp.class, "server_err_full", MAX_PLAYERS);
            case "ROOM_CLOSED":
                return Messages.get(Mp.class, "server_err_closed");
            case "TIMEOUT":
                return Messages.get(Mp.class, "server_err_timeout");
            case "BUSY":
                return Messages.get(Mp.class, "server_err_busy");
            case "RATE_LIMIT":
                return Messages.get(Mp.class, "server_err_rate");
            case "PASSWORD":
                return Messages.get(Mp.class, "server_err_password");
            default:
                return Messages.get(Mp.class, "server_err_protocol", line == null ? "" : line);
        }
    }

    /**
     * 解析服务器地址，支持 {@code host} 或 {@code host:port}。
     *
     * @param address 原始地址。
     * @param defaultPort 未写端口时的默认端口。
     * @return 下标 0=主机名，1=端口字符串。
     */
    private static String[] parseServerAddress(String address, int defaultPort) {
        String host = address == null ? "" : address.trim();
        int port = defaultPort;
        int idx = host.lastIndexOf(':');
        if (idx > 0 && idx < host.length() - 1) {
            String p = host.substring(idx + 1).trim();
            if (p.matches("\\d{1,5}")) {
                int value = Integer.parseInt(p);
                if (value > 0 && value <= 65535) {
                    port = value;
                    host = host.substring(0, idx).trim();
                }
            }
        }
        return new String[]{host, Integer.toString(port)};
    }

    private static void writeControlLine(DataOutputStream out, String line) throws IOException {
        synchronized (out) {
            out.write((line + "\n").getBytes("UTF-8"));
            out.flush();
        }
    }

    private static String readControlLine(InputStream in) throws IOException {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream(64);
        while (true) {
            int b = in.read();
            if (b < 0) return buffer.size() == 0 ? null : buffer.toString("UTF-8");
            if (b == '\n') {
                String s = buffer.toString("UTF-8");
                return s.endsWith("\r") ? s.substring(0, s.length() - 1) : s;
            }
            if (buffer.size() > 1024) throw new IOException("control line too long");
            buffer.write(b);
        }
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

    /**
     * 房主侧：把一条已建立的连接登记为访客 peer，并发送 HELLO/ASSIGN/VERSION。
     * 局域网 accept 与服务器模式 DATA 配对共用。
     *
     * @param s 已建立的对端连接。
     * @param id 房主分配的访客 id。
     * @throws IOException 初始化或发送握手帧失败。
     */
    private void addHostedPeer(Socket s, int id) throws IOException {
        Peer p = setupPeer(s);
        p.id = id;
        synchronized (peers) {
            peers.add(p);
        }
        try {
            sendTo(p, Proto.HELLO, getMyName().getBytes("UTF-8"));   // 房主也把自己的名字发过去
            sendTo(p, Proto.ASSIGN, encodeInt(p.id));
            sendTo(p, Proto.VERSION, encodeVersion());
        } catch (IOException e) {
            synchronized (peers) {
                peers.remove(p);
            }
            try { s.close(); } catch (IOException ignored) {}
            throw e;
        }
        scheduleHandshakeTimeout(p);
        connected = true;
        roomStateDirty = true;
        roomUiVersion.incrementAndGet();
        status = Messages.get(Mp.class, "status_players_online", peers.size());
    }

    /**
     * 访客侧：把与房主（直连或服务器配对）的连接登记为 peer(0)，并发送 HELLO/VERSION。
     *
     * @param s 已建立的房主连接。
     * @throws IOException 初始化或发送握手帧失败。
     */
    private void addClientPeer(Socket s) throws IOException {
        Peer hp = setupPeer(s);
        hp.id = HOST_ID;   // 访客侧唯一固定的房主 peer
        synchronized (peers) {
            peers.add(hp);
        }
        scheduleHandshakeTimeout(hp);
        connected = true;
        profileDirty = true;
        lastProfileSent = 0L;
        sendToAll(Proto.HELLO, getMyName().getBytes("UTF-8"));
        sendToAll(Proto.VERSION, encodeVersion());
        status = Messages.get(Mp.class, "status_connected_waiting");
    }

    /** 版本握手超时：对方在 HANDSHAKE_TIMEOUT_MS 内没有发来兼容的 VERSION 就断开。 */
    private void scheduleHandshakeTimeout(final Peer p) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(HANDSHAKE_TIMEOUT_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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
            // 持久世界先写回 mp_world_<seed>/，再清理隐藏临时槽
            if (guestWorldActive) saveGuestWorldNow();
            // 断开前先保存访客英雄；只有确认保存成功，才清理临时世界
            boolean ok = !guestWorldActive || saveGuestHero();
            if (ok) clearGuestTempSlot();
            guestWorldActive = false;
            guestWorldPersistent = false;
            guestHeroUnreadable = false;
            worldSeed = Long.MIN_VALUE;
        }
        kickedByHost = false;
        lastPingTime = 0L;
        lastStatsTime = 0L;
        lastHeroInfoTime = 0L;
        localAdmin = false;
        hostWorldKey = null;
        localDeathAnnounced = false;
        enteringNewWorld = false;
        synchronized (guestExplored) {
            guestExplored.clear();
        }
        lastExploredSnapshot = 0L;
        lastHeroHp = -1;
        closeChatWindow();
        clearChatHistory();
        clearMpLog();
        pendingState = null;
        final WndHeroClassSelect w = classWindow;
        classWindow = null;
        if (w != null) {
            Game.runOnRenderThread(w::hide);        // 选职业期间断连，顺手把窗口关掉
        }
        final WndMessage popup = hostDeathPopup;
        hostDeathPopup = null;
        if (popup != null) {
            Game.runOnRenderThread(popup::hide);
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
        try { if (lobbySocket != null) lobbySocket.close(); } catch (IOException ignored) {}
        try { if (worker != null) worker.interrupt(); } catch (Throwable ignored) {}
        server = null;
        lobbySocket = null;
        lobbyOut = null;
        serverMode = false;
        serverAddress = null;
        serverDataPort = DEFAULT_PORT + 1;
        roomCode = null;
        roomPassword = "";
        if (roomSeedPending) {
            Dungeon.overrideSeed = -1;
            roomSeedPending = false;
        }
        pendingRoomSaveWrite = false;
        selectingRoomSave = false;
        roomNewGameSlot = -1;
        roomSave = EMPTY_ROOM_SAVE;
        roomRoster = Collections.emptyList();
        roomStateDirty = false;
        lastRoomStateSent = 0L;
        hostRoomClass = null;
        guestRoomClass = null;
        guestRoomClassSeed = Long.MIN_VALUE;
        guestRoomHeroLocked = false;
        guestLockCacheValid = false;
        guestLockCacheSeed = Long.MIN_VALUE;
        guestLockCacheClass = null;
        lastRoomHeroSentSeed = Long.MIN_VALUE;
        lastRoomHeroSent = null;
        roomStateSignature = "";
        roomUiVersion.incrementAndGet();
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
                if (isHost()) {
                    roomStateDirty = true;
                    roomUiVersion.incrementAndGet();
                }
                if (p.worldEnded) {
                    // 房主开始新游戏导致的断开：保留房主侧的状态提示，不逐人播报普通断线
                } else if (p.versionError != null) {
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
                        if (p.hpBg != null) {
                            p.hpBg.killAndErase();
                            p.hpBg = null;
                        }
                        if (p.hpFill != null) {
                            p.hpFill.killAndErase();
                            p.hpFill = null;
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
            showLobbyMessage(reason);
        });
    }

    /** 回到联机大厅并弹出提示（若已经在大厅则直接弹；待显示提示由大厅场景消费）。 */
    private void showLobbyMessage(final String msg) {
        status = msg;
        pendingLobbyMessage = msg;
        if (Game.scene() != null && !(Game.scene() instanceof MpLobbyScene)) {
            ShatteredPixelDungeon.switchNoFade(MpLobbyScene.class);
        } else if (Game.scene() != null) {
            String m = consumeLobbyMessage();
            if (m != null) {
                ShatteredPixelDungeon.scene().add(new WndMessage(m));
            }
        }
    }

    /**
     * 取出待显示的大厅提示（由 MpLobbyScene 每帧调用，显示后清空）。
     *
     * @return 提示文案；null=没有。
     */
    public String consumeLobbyMessage() {
        String msg = pendingLobbyMessage;
        pendingLobbyMessage = null;
        return msg;
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

    /** 系统事件提示：写入联机状态栏 + 左上角联机日志（金色），任意线程可用 */
    private void systemNotice(final String text) {
        status = text;
        addMpLogLine(text, MPLOG_EVENT);
    }

    /** 死亡提示：写入联机状态栏 + 左上角联机日志（红色），任意线程可用 */
    private void deathNotice(final String text) {
        status = text;
        addMpLogLine(text, MPLOG_NEGATIVE);
    }

    /** 房主进入新档：让旧访客先保存旧英雄，回 WORLD_READY 后再发新世界 STATE（自动跟随）。 */
    private void restartWorldForGuests() {
        final String msg = Messages.get(Mp.class, "host_new_game_sync");
        status = msg;
        addMpLogLine(msg, MPLOG_WARNING);
        List<Peer> snapshot;
        synchronized (peers) {
            snapshot = new ArrayList<>(peers);
        }
        long now = System.currentTimeMillis();
        for (Peer p : snapshot) {
            // 尚未拿到旧世界状态的新连接不用重置，它们会直接收到新世界 STATE
            if (!p.gotState || p.worldResetPending) continue;
            p.worldResetPending = true;
            p.worldResetAt = now;
            try { sendTo(p, Proto.WORLD_RESET, new byte[0]); } catch (Throwable ignored) {}
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
                            roomStateDirty = true;
                            roomUiVersion.incrementAndGet();
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
                    if (vp.protocolVersion == MP_PROTOCOL_VERSION
                            && vp.gameVersionCode == Game.versionCode) {
                        p.versionOk = true;
                        if (isHost()) {
                            roomStateDirty = true;
                            roomUiVersion.incrementAndGet();
                        }
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
                    if (!isHost()) applyState(data);   // 仅访客会处理
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
                            systemNotice(Messages.get(Mp.class, "notice_entered_game", displayName(p.name)));
                        }
                    } else {
                        Peer pp2 = peerFor(pp.id);
                        pp2.remoteDepth = pp.depth;
                        pp2.remoteBranch = pp.branch;
                        if (pp.pos > 0) pp2.remotePos = pp.pos;
                        // 其他访客的第一条位置包（房主转发的 POS）= 他进入了游戏
                        if (pp.pos > 0 && pp.id != HOST_ID && pp.id != myId && !pp2.joinedGame) {
                            pp2.joinedGame = true;
                            systemNotice(Messages.get(Mp.class, "notice_entered_game", displayName(pp2.name)));
                        }
                    }
                    break;
                }
                case Proto.HERO: {
                    HeroPayload hp = decodeHero(data);
                    if (isHost()) {
                        p.heroClass = hp.cls;
                        sendToOthers(p, Proto.HERO, data);   // 转发职业给其他访客
                        roomStateDirty = true;
                        roomUiVersion.incrementAndGet();
                    } else {
                        peerFor(hp.id).heroClass = hp.cls;
                    }
                    break;
                }
                case Proto.ROOM_STATE: {
                    if (role == Role.CLIENT) applyRoomState(data);
                    break;
                }
                case Proto.ROOM_EVENT: {
                    RoomEventPayload ev = decodeRoomEvent(data);
                    String evName;
                    if (isHost()) {
                        // 房主以发送方 peer 为准，防止伪造；并转发给其他访客
                        evName = displayName(p.name);
                        ev.id = p.id;
                        try {
                            sendToOthers(p, Proto.ROOM_EVENT, encodeRoomEvent(p.id, ev.event));
                        } catch (Throwable ignored) {}
                    } else {
                        evName = displayName(peerFor(ev.id).name);
                    }
                    if (ev.event == ROOM_EVENT_RETURNED) {
                        systemNotice(Messages.get(Mp.class, "notice_returned_room", evName));
                    }
                    break;
                }
                case Proto.PROFILE: {
                    if (isHost()) applyPeerProfile(p, data);
                    break;
                }
                case Proto.PROFILE_REQ: {
                    ProfileReqPayload req = decodeProfileReq(data);
                    if (isHost()) {
                        handleProfileRequest(p, req);
                    } else {
                        handleGuestProfileRequest(p, req);
                    }
                    break;
                }
                case Proto.PROFILE_DATA: {
                    ProfileDataPayload pd = decodeProfileData(data);
                    handleProfileData(p, pd, data);
                    break;
                }
                case Proto.CHAT: {
                    ChatPayload cp = decodeChat(data);
                    addChatLine(cp.name, cp.text,
                            isServerChatName(cp.name) ? CHAT_TYPE_SYSTEM : CHAT_TYPE_OTHER);
                    if (isHost()) {
                        sendToOthers(p, Proto.CHAT, data);   // 房主把聊天转发给其他访客
                    }
                    break;
                }
                case Proto.KICK: {
                    // 房主退出对局：访客保存后回房间，连接保持；房主再点「继续对局」时随 STATE 重进
                    Game.runOnRenderThread(() -> {
                        Peer hp = peerFor(HOST_ID);
                        systemNotice(Messages.get(Mp.class, "notice_returned_room",
                                displayName(hp.name)));
                        clientReturnToRoom(false);
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
                    // 被房主 /kick：断开并回联机大厅提示
                    kickedByHost = true;
                    Game.runOnRenderThread(() -> {
                        disconnect();
                        showLobbyMessage(Messages.get(Mp.class, "reason_kicked"));
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
                case Proto.STATS: {
                    StatsPayload sp = decodeStats(data);
                    if (isHost()) {
                        // 房主以发送方 id 为准，防止访客伪造其他人的血量
                        p.hp = clampStat(sp.hp);
                        p.ht = clampStat(sp.ht);
                        if (p.remote != null) {
                            p.remote.HP = p.hp;
                            p.remote.HT = p.ht;
                        }
                        sendToOthers(p, Proto.STATS, encodeStats(p.id, p.hp, p.ht));
                    } else if (sp.id != myId) {
                        Peer tp = peerFor(sp.id);
                        tp.hp = clampStat(sp.hp);
                        tp.ht = clampStat(sp.ht);
                        if (tp.remote != null) {
                            tp.remote.HP = tp.hp;
                            tp.remote.HT = tp.ht;
                        }
                    }
                    break;
                }
                case Proto.HERO_INFO: {
                    HeroInfoPayload hi = decodeHeroInfo(data);
                    if (isHost()) {
                        // 房主以发送方 id 为准，防止访客伪造他人的英雄信息
                        p.heroInfo = hi.bundle;
                        sendToOthers(p, Proto.HERO_INFO, encodeHeroInfo(p.id, hi.bundle));
                    } else if (hi.id != myId) {
                        peerFor(hi.id).heroInfo = hi.bundle;
                    }
                    break;
                }
                case Proto.ADMIN: {
                    AdminPayload ap = decodeAdmin(data);
                    if (role == Role.CLIENT) {
                        if (ap.id == myId) {
                            localAdmin = ap.admin;
                            systemNotice(Messages.get(Mp.class,
                                    ap.admin ? "admin_granted_self" : "admin_revoked_self"));
                        } else {
                            peerFor(ap.id).admin = ap.admin;
                        }
                    }
                    break;
                }
                case Proto.CMD: {
                    if (isHost() && p.admin) {
                        handleAdminCommand(p, new String(data, "UTF-8"));
                    }
                    break;
                }
                case Proto.DEATH: {
                    int did = decodeInt(data);
                    if (isHost()) {
                        deathNotice(Messages.get(Mp.class, "notice_player_died", displayName(p.name)));
                        sendToOthers(p, Proto.DEATH, encodeInt(p.id));
                    } else if (did != myId) {
                        Peer dp = peerFor(did);
                        deathNotice(Messages.get(Mp.class, "notice_player_died", displayName(dp.name)));
                        if (did == HOST_ID) {
                            final String popup = Messages.get(Mp.class, "host_died_popup");
                            Game.runOnRenderThread(() -> {
                                if (ShatteredPixelDungeon.scene() != null) {
                                    hostDeathPopup = new WndMessage(popup);
                                    ShatteredPixelDungeon.scene().add(hostDeathPopup);
                                }
                            });
                        }
                    }
                    break;
                }
                case Proto.WORLD_RESET: {
                    enteringNewWorld = true;   // recv 线程立刻停止旧世界同步；保存旧英雄交给渲染线程
                    Game.runOnRenderThread(() -> {
                        // 先保存旧世界的访客英雄；临时槽由新 STATE 的 mergeAndEnter 覆盖/清理，
                        // 这里不能清槽（会重置 curSlot，旧场景可能误存到普通槽）
                        if (hostDeathPopup != null) {
                            hostDeathPopup.hide();
                            hostDeathPopup = null;
                        }
                        if (guestWorldActive && worldSeed != Long.MIN_VALUE) {
                            saveGuestHero();
                            saveGuestWorldNow();   // 房主换档前把旧世界完整写回
                        }
                        enteringNewWorld = true;
                        guestWorldActive = false;
                        worldSeed = Long.MIN_VALUE;
                        status = Messages.get(Mp.class, "host_new_game_sync");
                        addMpLogLine(status, MPLOG_WARNING);
                        try { sendToAll(Proto.WORLD_READY, new byte[0]); } catch (Throwable ignored) {}
                    });
                    break;
                }
                case Proto.WORLD_READY: {
                    if (isHost()) {
                        p.worldResetPending = false;
                        p.gotState = false;   // 下一帧发送新世界 STATE
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
                                if (pp.hpBg != null) {
                                    pp.hpBg.killAndErase();
                                    pp.hpBg = null;
                                }
                                if (pp.hpFill != null) {
                                    pp.hpFill.killAndErase();
                                    pp.hpFill = null;
                                }
                                pp.remote = null;
                                systemNotice(Messages.get(Mp.class, "notice_player_left", displayName(pp.name)));
                                if (isHost()) {
                                    roomStateDirty = true;
                                    roomUiVersion.incrementAndGet();
                                }
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

    /** 按 id 查找已有 peer（不存在时返回 null，不创建占位 peer）。 */
    private Peer findPeerById(int id) {
        synchronized (peers) {
            for (Peer p : peers) {
                if (p.id == id) return p;
            }
        }
        return null;
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
            // 自动跟随新房主世界：先把旧世界英雄/持久世界落盘，再切种子；旧场景暂停时不再重复保存
            if (guestWorldActive && worldSeed != Long.MIN_VALUE) {
                saveGuestHero();
                saveGuestWorldFromSlot();   // 只复制已落盘的世界，避免网络线程调用 Dungeon.saveAll
            }
            guestWorldActive = false;
            guestWorldPersistent = false;
            enteringNewWorld = true;
            Bundle game = Bundle.read(new ByteArrayInputStream(p.game));
            worldSeed = game.getLong("seed");   // 房主世界的种子（访客英雄按世界分档）
            Bundle hostHero = game.getBundle("hero");
            if (hostHero.contains("class")) {
                // 远端幽灵用房主的外观（Hero.CLASS = "class"）；访客侧 id=0 的 peer 就是房主
                peerFor(HOST_ID).heroClass = hostHero.getEnum("class", HeroClass.class);
            }
            int hostPos = hostHero.getInt("pos");                             // 访客英雄出生在房主当前位置

            Bundle guest = loadGuestHeroBundle();
            if (guest == null && hasGuestWorld()) {
                // 持久世界还在，但英雄档丢了：从世界档的 game.dat 里恢复
                guest = loadGuestHeroFromWorld();
            }
            Bundle guestHero = guestHeroPart(guest);
            if (guestHero != null && guestHero.contains("class")) {
                // 已有英雄存档：直接告诉房主自己的职业（房主渲染访客幽灵外观用，并转发给其他访客）
                try {
                    sendToAll(Proto.HERO, encodeHero(myId, guestHero.getEnum("class", HeroClass.class)));
                } catch (Throwable ignored) {}
            }
            if (guest == null) {
                // 房间内已选职业：直接用该职业创建访客英雄，不再弹职业窗口
                final HeroClass roomCls = roomHeroClassForWorld(worldSeed);
                if (roomCls != null) {
                    if (pendingState == null) {
                        pendingState = p;
                        status = Messages.get(Mp.class, "status_choose_class");
                        final StatePayload fp = p;
                        final int fpos = hostPos;
                        Game.runOnRenderThread(() -> {
                            if (!connected) return;
                            StatePayload latest = pendingState != null ? pendingState : fp;
                            pendingState = null;
                            try {
                                int latestPos = fpos;
                                try {
                                    latestPos = Bundle.read(new ByteArrayInputStream(latest.game))
                                            .getBundle("hero").getInt("pos");
                                } catch (Throwable ignored) {}
                                backupUnreadableGuestHero();
                                Bundle b = newGuestHeroBundle(roomCls);
                                if (!saveGuestHeroBundle(b)) {
                                    status = Messages.get(Mp.class, "status_save_new_hero_failed");
                                }
                                try {
                                    sendToAll(Proto.HERO, encodeHero(myId, roomCls));
                                } catch (Throwable ignored) {}
                                mergeAndEnter(latest, latestPos, b);
                            } catch (Throwable t) {
                                status = Messages.get(Mp.class, "status_create_hero_failed", t.getMessage());
                            }
                        });
                    } else {
                        pendingState = p;
                    }
                    return;
                }
                // 没有房间职业（房主随时开始、访客还没选）：弹职业选择窗口兜底
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
                                    // 房间内取消：留在房间等待房主，不退出联机
                                    if (Game.scene() instanceof MpRoomScene) {
                                        return;
                                    }
                                    disconnect();               // 取消选择 = 取消本次加入
                                    Game.runOnRenderThread(() -> {
                                        if (Game.scene() instanceof GameScene) {
                                            ShatteredPixelDungeon.switchScene(TitleScene.class);
                                        }
                                    });
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
            enteringNewWorld = false;
            status = Messages.get(Mp.class, "status_data_error", t.getMessage());
        }
    }

    /** 把房主世界 + 访客英雄合并写进存档槽并切场景（渲染线程/网络线程均可调用） */
    private void mergeAndEnter(StatePayload p, int hostPos, Bundle guest) { // skipcq: JAVA-R1000 合并访客英雄与房主世界：复杂度高但已实测稳定
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

            // 用访客英雄整体覆盖 game bundle 里嵌套的 "hero"。
            // 新格式英雄档的子 bundle 自带类名，直接嵌套回去，不做 restore→store 往返：
            // 往返会经 Item.restoreFromBundle 重链快捷栏，而它只在 Dungeon.hero == null 时生效，
            // 同进程重连/房主换档跟随时 Dungeon.hero 还是旧英雄，往返会把快捷栏物品位置全部丢掉。
            Bundle game = Bundle.read(new ByteArrayInputStream(p.game));

            // 已有自己的持久世界：直接进入，不再用房主当前层覆盖
            if (initSaveEnabled() && hasGuestWorld() && enterGuestWorld(guest, false)) {
                return;
            }
            // 新房客/旧世界档：如果房主带有第0层初始世界包，就从第0层开始自己玩
            // 已有持久世界但本次进入失败时不要用它覆盖，避免丢档
            Bundle initWorld = game.getBundle(MP_INIT_WORLD);
            if (initSaveEnabled() && !hasGuestWorld() && !initWorld.isNull()
                    && createGuestWorldFromInit(initWorld, guest)
                    && enterGuestWorld(guest, true)) {
                return;
            }
            game.remove(MP_INIT_WORLD);

            Bundle heroBundle = guestHeroPart(guest);
            loadGuestExplored(guest);
            keepSafeBuffs(heroBundle);                   // 只保留饥饿/自然回血，其余 buff 可能引用上个世界的对象

            // 英雄与其 buff 的 Actor 时间归零：访客档保存的是未归一的实时时间，
            // 重连后英雄会排在房主世界所有怪物之后几万回合，表现为“定住、回合一直转、被连招打死”。
            heroBundle.put("time", 0f);
            ArrayList<Bundlable> buffs = new ArrayList<>(heroBundle.getCollection("buffs"));
            for (Bundlable x : buffs) {
                if (x instanceof Buff) ((Buff) x).clearTime();
            }
            heroBundle.put("buffs", buffs);

            // 位置：本层有访客存档才恢复自己的位置；本层没有存档（或新访客）则用本层入口
            int pos = -1;
            boolean hasPosInfo = guest.contains(MP_DEPTH) && guest.contains(MP_BRANCH);
            if (hasPosInfo && guest.getInt(MP_DEPTH) == depth && guest.getInt(MP_BRANCH) == branch) {
                int savedPos = heroBundle.getInt("pos");
                if (savedPos >= 0) pos = savedPos;
            }
            // 同进程内房主退出再进入时，内存里的最后位置比可能滞后的英雄档更可靠
            if (role == Role.CLIENT && lastGuestWorldSeed == worldSeed
                    && lastGuestDepth == depth && lastGuestBranch == branch
                    && lastGuestPos >= 0) {
                pos = lastGuestPos;
            }
            heroBundle.put("pos", pos);

            if (heroBundle != guest) {
                game.put("hero", heroBundle);            // 新格式：嵌套子 bundle 自带类名，直接放回
            } else {
                // 旧格式平铺档：往返一次补类名（快捷栏物品位置可能丢一次）。
                // 只读恢复访客档，不能触发房主本机的徽章/技能按钮副作用。
                // 这里可能运行在网络线程，只用按线程隔离的 PreviewMode，
                // 不交换全局 Badges.local（避免与渲染线程的徽章更新竞争）。
                Hero g = new Hero();
                PreviewMode.begin();
                try {
                    g.restoreFromBundle(guest);
                } finally {
                    PreviewMode.end();
                }
                game.put("hero", g);
            }

            // 房主快捷栏数据指向房主背包物品，全部清掉，换成访客自己的
            game.remove("placeholders");
            game.remove("placements");
            game.remove("slot_actions");
            if (guest.contains(MP_QUICKSLOT)) {
                Bundle guestQuickSlot = guest.getBundle(MP_QUICKSLOT);
                game.put("placeholders", guestQuickSlot.getCollection("placeholders"));
                game.put("placements", guestQuickSlot.getBooleanArray("placements"));
                if (guestQuickSlot.contains("slot_actions")) {
                    game.put("slot_actions", guestQuickSlot.getStringArray("slot_actions"));
                }
            }

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

            // 房主层档里只有房主的探图记录：先清空，再合并访客自己的 visited/mapped。
            // 这里必须直接操作嵌套 bundle，不能 (Level) levelFile.get("level")：
            // 反序列化 Level 会同时恢复 mobs/blobs 等，某些自定义怪物的 restore 流程会访问
            // Dungeon.level，而访客此时还没有进入世界（Dungeon.level == null），
            // 手机上曾复现为 "Attempt to invoke virtual method Level.length() on a null object reference"。
            Bundle levelFile = Bundle.read(new ByteArrayInputStream(p.level));
            Bundle levelBundle = levelFile.getBundle("level");
            if (levelBundle != null && !levelBundle.isNull()) {
                boolean[] visited = levelBundle.getBooleanArray("visited");
                boolean[] mapped = levelBundle.getBooleanArray("mapped");
                if (visited != null) Arrays.fill(visited, false);
                if (mapped != null) Arrays.fill(mapped, false);
                if (guest.contains(MP_EXPLORED + depth + "_" + branch)) {
                    Bundle vis = guest.getBundle(MP_EXPLORED + depth + "_" + branch);
                    boolean[] v = vis.getBooleanArray("visited");
                    if (v != null && visited != null) {
                        for (int i = 0; i < v.length && i < visited.length; i++) {
                            if (v[i]) visited[i] = true;
                        }
                    }
                    boolean[] m = vis.getBooleanArray("mapped");
                    if (m != null && mapped != null) {
                        for (int i = 0; i < m.length && i < mapped.length; i++) {
                            if (m[i]) mapped[i] = true;
                        }
                    }
                }
                if (visited != null) levelBundle.put("visited", visited);
                if (mapped != null) levelBundle.put("mapped", mapped);
            }
            FileUtils.bundleToFile(GamesInProgress.depthFile(slot, depth, branch), levelFile);

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
            enteringNewWorld = false;
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
        if (isHost()) {
            roomStateDirty = true;
            roomUiVersion.incrementAndGet();
            if (isInRoom()) flushRoomStateIfNeeded();
        } else {
            profileDirty = true;
        }
    }

    /**
     * 发送聊天消息（先写入本机聊天记录，再发给其他玩家）；"/" 开头交给指令处理
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
        addChatLine(getMyName(), msg, CHAT_TYPE_SELF);
        try { sendToAll(Proto.CHAT, encodeChat(getMyName(), msg)); } catch (Throwable ignored) {}
    }

    /** 追加一条聊天记录（线程安全；超过上限丢弃最旧的）。 */
    private void addChatLine(String name, String text, int type) {
        if (text == null) return;
        String msg = text.trim();
        if (msg.isEmpty()) return;
        String displayName = name == null || name.trim().isEmpty() ? "?" : name.trim();
        String time = chatLocalTime();
        synchronized (chatLines) {
            chatLines.add(new ChatLine(chatNextId++, displayName, msg, time, type));
            while (chatLines.size() > CHAT_MAX_LINES) {
                chatLines.remove(0);
            }
            chatVersion++;
        }
        // 左上角联机日志同时显示聊天（时间只取 HH:mm，避免占用过多宽度）
        int logType = type == CHAT_TYPE_SELF ? MPLOG_CHAT_SELF
                : type == CHAT_TYPE_SYSTEM ? MPLOG_CHAT_SYSTEM : MPLOG_CHAT_OTHER;
        String shortTime = time.length() > 5 ? time.substring(0, 5) : time;
        addMpLogLine("[" + shortTime + "] " + displayName + ": " + msg, logType);
    }

    /** 追加一条左上角联机日志（线程安全；超过上限丢弃最旧的）。 */
    private void addMpLogLine(String text, int type) {
        if (text == null) return;
        String msg = text.trim();
        if (msg.isEmpty()) return;
        // 左上角区域较小，长消息截断；完整内容仍在聊天窗口里
        if (msg.length() > 90) {
            msg = msg.substring(0, 87) + "...";
        }
        synchronized (mpLogLines) {
            mpLogLines.add(new MpLogLine(mpLogNextId++, msg, type));
            while (mpLogLines.size() > MPLOG_MAX_LINES) {
                mpLogLines.remove(0);
            }
            mpLogVersion++;
        }
    }

    /**
     * 获取左上角联机日志快照。
     *
     * @return 返回值。
     */
    public List<MpLogLine> getMpLogLines() {
        synchronized (mpLogLines) {
            return new ArrayList<>(mpLogLines);
        }
    }

    /** 左上角联机日志版本号。 */
    public long getMpLogVersion() {
        return mpLogVersion;
    }

    /** 清空左上角联机日志。 */
    public void clearMpLog() {
        synchronized (mpLogLines) {
            mpLogLines.clear();
            mpLogVersion++;
        }
    }

    /**
     * 获取聊天记录快照（窗口渲染用）。
     *
     * @return 返回值。
     */
    public List<ChatLine> getChatLines() {
        synchronized (chatLines) {
            return new ArrayList<>(chatLines);
        }
    }

    /** 聊天记录版本号；窗口用它判断是否需要刷新。 */
    public long getChatVersion() {
        return chatVersion;
    }

    /** 清空本机聊天记录（离开房间/断开连接时）。 */
    public void clearChatHistory() {
        synchronized (chatLines) {
            chatLines.clear();
            chatVersion++;
        }
    }

    /** 本机时间，格式 HH:mm:ss。 */
    private static String chatLocalTime() {
        Calendar c = Calendar.getInstance();
        return String.format(Locale.US, "%02d:%02d:%02d",
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
    }

    /** 是否是房主代发的系统聊天（用于显示为金色）。 */
    private static boolean isServerChatName(String name) {
        if (name == null) return false;
        String n = name.trim();
        return n.equals(Messages.get(Mp.class, "server_name"))
                || n.equals("系统") || n.equals("Server") || n.equals("系統");
    }

    /** 聊天指令：/list 所有人可用；/kick 仅房主可用 */
    private void handleCommand(String cmd) { // skipcq: JAVA-R1000 聊天指令分支多：复杂度高但已实测稳定
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
            if (!canUseHostCommands()) {
                addMpLogLine(Messages.get(Mp.class, "cmd_host_only"), MPLOG_NEGATIVE);
                return;
            }
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                addMpLogLine(Messages.get(Mp.class, "cmd_kick_usage"), MPLOG_NEGATIVE);
                return;
            }
            if (role != Role.HOST) {
                // 管理员访客没有直连其他访客的 socket，把指令转给房主执行
                try { sendToAll(Proto.CMD, cmd.trim().getBytes("UTF-8")); } catch (Throwable ignored) {}
                addMpLogLine(Messages.get(Mp.class, "cmd_sent_to_host", cmd.trim()), MPLOG_EVENT);
                return;
            }
            String targetName = parts[1].trim();
            Peer target = findPeerByName(targetName);
            if (target == null) {
                addMpLogLine(Messages.get(Mp.class, "cmd_kick_not_found", targetName), MPLOG_NEGATIVE);
                return;
            }
            kickPeer(target);
            return;
        }
        if (name.equalsIgnoreCase("/op") || name.equalsIgnoreCase("/dop")) {
            if (role != Role.HOST) {
                addMpLogLine(Messages.get(Mp.class, "cmd_host_only"), MPLOG_NEGATIVE);
                return;
            }
            boolean grant = name.equalsIgnoreCase("/op");
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                addMpLogLine(Messages.get(Mp.class, grant ? "cmd_op_usage" : "cmd_dop_usage"), MPLOG_NEGATIVE);
                return;
            }
            String targetName = parts[1].trim();
            Peer target = findPeerByName(targetName);
            if (target == null) {
                addMpLogLine(Messages.get(Mp.class, "cmd_kick_not_found", targetName), MPLOG_NEGATIVE);
                return;
            }
            setPeerAdmin(target, grant);
            return;
        }
        addMpLogLine(Messages.get(Mp.class,
                role == Role.HOST || localAdmin ? "cmd_unknown_host" : "cmd_unknown_client"),
                MPLOG_NEGATIVE);
    }

    /** 本机是否可以使用房主命令（房主或房主授予的管理员）。 */
    private boolean canUseHostCommands() {
        return role == Role.HOST || localAdmin;
    }

    /** 按名字找访客 peer（不含房主自己）。 */
    private Peer findPeerByName(String targetName) {
        synchronized (peers) {
            for (Peer p : peers) {
                if (p.id > 0 && targetName.equals(p.name)) return p;
            }
        }
        return null;
    }

    /** 房主授予/撤销管理员并广播给所有客户端。 */
    private void setPeerAdmin(Peer target, boolean grant) {
        if (target.admin == grant) {
            addMpLogLine(Messages.get(Mp.class,
                    grant ? "cmd_op_already" : "cmd_dop_not_admin", displayName(target.name)),
                    MPLOG_WARNING);
            return;
        }
        target.admin = grant;
        try { sendToAll(Proto.ADMIN, encodeAdmin(target.id, grant)); } catch (Throwable ignored) {}
        String msg = Messages.get(Mp.class,
                grant ? "cmd_op_done" : "cmd_dop_done", displayName(target.name));
        addMpLogLine(msg, MPLOG_EVENT);
        status = msg;
    }

    /** 处理管理员访客转给房主的指令（目前只有 /kick）。 */
    private void handleAdminCommand(Peer from, String cmd) {
        String[] parts = cmd.trim().split("\\s+", 2);
        if (parts.length == 0 || !parts[0].equalsIgnoreCase("/kick")) {
            sendServerMessage(from, Messages.get(Mp.class, "cmd_no_permission"));
            return;
        }
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            sendServerMessage(from, Messages.get(Mp.class, "cmd_kick_usage"));
            return;
        }
        String targetName = parts[1].trim();
        Peer target = findPeerByName(targetName);
        if (target == null) {
            sendServerMessage(from, Messages.get(Mp.class, "cmd_kick_not_found", targetName));
            return;
        }
        if (target == from) {
            sendServerMessage(from, Messages.get(Mp.class, "cmd_kick_self"));
            return;
        }
        kickPeer(target);
    }

    /** 房主给指定访客发送一条系统聊天（用于管理员指令的反馈）。 */
    private void sendServerMessage(Peer to, String text) {
        try {
            sendTo(to, Proto.CHAT, encodeChat(Messages.get(Mp.class, "server_name"), text));
        } catch (IOException ignored) {}
    }

    /** 房主请离一名访客：发 KICKED 后断开 socket；清理由 recvLoop 统一完成 */
    private void kickPeer(Peer p) {
        p.kicked = true;
        try { sendTo(p, Proto.KICKED, new byte[0]); } catch (Throwable ignored) {}
        try { if (p.sock != null) p.sock.close(); } catch (Throwable ignored) {}
        final String msg = Messages.get(Mp.class, "cmd_kick_done", displayName(p.name));
        status = msg;
        addMpLogLine(msg, MPLOG_EVENT);
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
                fillSelfInfo(self);
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
                fillSelfInfo(self);
                out.add(self);
                for (Peer p : peers) {
                    if (p.id > 0 && p.id != myId) out.add(toInfo(p, false));
                }
            }
        }
        return out;
    }

    /** 填充本机玩家在 /list 用的层数/血量/身份。 */
    private void fillSelfInfo(PlayerInfo info) {
        info.depth = Dungeon.hero != null ? Dungeon.depth : -1;
        info.branch = Dungeon.hero != null ? Dungeon.branch : 0;
        info.hp = Dungeon.hero != null ? Dungeon.hero.HP : -1;
        info.ht = Dungeon.hero != null ? Dungeon.hero.HT : -1;
        info.isAdmin = role == Role.CLIENT && localAdmin;
    }

    private PlayerInfo toInfo(Peer p, boolean isHost) {
        PlayerInfo info = new PlayerInfo();
        info.id = p.id;
        info.name = p.name;
        info.heroClass = p.heroClass;
        info.isHost = isHost || p.id == HOST_ID;
        info.rttMs = p.rttMs;
        info.timeout = p.pingTimeout;
        info.depth = p.remoteDepth;
        info.branch = p.remoteBranch;
        info.hp = p.hp;
        info.ht = p.ht;
        info.isAdmin = p.admin;
        return info;
    }

    /**
     * 打开某玩家的英雄详情：自己 → 本机窗口；其他人 → 从最近同步的 bundle 还原出只读副本。
     *
     * @param id 参数。
     */
    public void showRemoteHero(int id) {
        try {
            if (id == (isHost() ? HOST_ID : myId)) {
                if (Dungeon.hero != null) {
                    GameScene.show(new WndHero());
                }
                return;
            }
            Peer tp = peerFor(id);
            byte[] data = tp.heroInfo;
            if (data == null) {
                GameScene.show(new WndMessage(Messages.get(Mp.class, "hero_info_pending")));
                return;
            }
            Bundle b = Bundle.read(new ByteArrayInputStream(data));
            Hero h = new Hero();
            // 只读预览：恢复远端英雄会重走存储恢复流程，必须屏蔽
            // 徽章解锁/全局皮肤/ActionIndicator 技能按钮/快捷栏等副作用，
            // 否则查看队友详情会给自己发成就，且战士怒气按钮会指向无精灵的副本。
            PreviewMode.begin();
            Badges.beginPreview();
            try {
                h.restoreFromBundle(b);
            } finally {
                Badges.endPreview();
                PreviewMode.end();
            }
            GameScene.show(new WndHero(h));
        } catch (Throwable t) {
            status = Messages.get(Mp.class, "hero_info_failed", t.getMessage());
        }
    }

    /**
     * 放大镜检查：cell 上有可见的远端玩家幽灵时显示其英雄详情。
     *
     * @param cell 参数。
     * @return 是否已处理（true = 该格上是远端玩家，调用方无需继续原版检查）。
     */
    public boolean examineRemote(Integer cell) {
        if (cell == null) return false;
        // 自己脚下交给原版检查：这里可能有重叠的地面物品，不能让远端英雄详情抢走点击
        if (Dungeon.hero != null && cell == Dungeon.hero.pos) return false;
        List<Peer> snapshot;
        synchronized (peers) {
            snapshot = new ArrayList<>(peers);
        }
        for (Peer p : snapshot) {
            if (p.remote == null || p.remoteSprite == null || !p.remoteSprite.visible) continue;
            if (p.remotePos == cell) {
                showRemoteHero(p.id);
                return true;
            }
        }
        return false;
    }

    /** 联机时在 GameScene 左侧挂方形聊天按钮；每个场景只建一次 */
    private void updateChatButton(GameScene gs) {
        if (chatButton == null) {
            chatButton = new StyledButton(Chrome.Type.GREY_BUTTON_TR, "") {
                @Override
                protected void onClick() {
                    openChat();
                }
            };
            chatButton.icon(MpChatWindow.chatIcon());
            chatButton.camera = PixelScene.uiCamera;
            // 左侧菜单/页面指示按钮占到 y≈99，聊天按钮放到其下方并贴住左边缘；
            // 极矮屏幕（大界面模式）下再向上收，避免跑出屏幕
            float chatY = Math.min(104f, Math.max(0f, PixelScene.uiCamera.height - 24f));
            chatButton.setRect(0f, chatY, 20f, 20f);
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

    /** 联机时在 GameScene 左上角挂联机日志；每个场景只建一次 */
    private void updateMpLogPane(GameScene gs) {
        if (mpLogPane == null) {
            mpLogPane = new MpGameLog();
            // GameScene 的 Camera.main 是世界相机；HUD 文本必须走 uiCamera，
            // 否则日志会画在地图坐标系里，表现为“局内看不到联机日志”。
            mpLogPane.camera = PixelScene.uiCamera;
            gs.addToFront(mpLogPane);
        }
        mpLogPane.visible = true;
    }

    private void hideMpLogPane() {
        if (mpLogPane != null) {
            mpLogPane.visible = false;
        }
    }

    /** T 键 / 聊天按钮：打开聊天窗口（房间与对局内都可用） */
    public void openChat() {
        if (!connected || chatOpen) return;
        if (!(Game.scene() instanceof GameScene) && !(Game.scene() instanceof MpRoomScene)) return;
        chatOpen = true;
        Game.runOnRenderThread(() -> {
            MpChatWindow w = null;
            try {
                w = new MpChatWindow(() -> {
                    chatOpen = false;
                    chatWindow = null;
                });
                chatWindow = w;
                ShatteredPixelDungeon.scene().addToFront(w);
                status = Messages.get(Mp.class, "chat_open");
            } catch (Throwable t) {
                if (w != null) w.destroy();
                chatOpen = false;
                chatWindow = null;
                status = Messages.get(Mp.class, "chat_open_failed", t.getMessage());
            }
        });
    }

    /** 关闭聊天窗口（断开连接/离开房间时）。 */
    private void closeChatWindow() {
        final MpChatWindow w = chatWindow;
        chatWindow = null;
        chatOpen = false;
        if (w != null) {
            Game.runOnRenderThread(() -> {
                if (w.parent != null) w.hide();
                else w.destroy();
            });
        }
    }

    // ---------------------------------------------------------------- 初始世界快照 / 访客持久世界（稳定版实验）

    /** 初始快照/访客持久世界开关：本地测试可用 -Dmlpd.initsave=false 退回旧逻辑。 */
    private static boolean initSaveEnabled() {
        return !"false".equalsIgnoreCase(System.getProperty("mlpd.initsave"));
    }

    /** Dungeon.newLevel 首次生成楼层后调用：记录待抓取的层。 */
    public void markFreshLevel(int depth, int branch) {
        pendingFreshDepth = depth;
        pendingFreshBranch = branch;
    }

    /** 读取旧层档/生成失败时调用：避免把旧层误当成新生成层。 */
    public void clearFreshLevel() {
        pendingFreshDepth = -1;
        pendingFreshBranch = -1;
    }

    /** Dungeon.switchLevel 完成后调用：首次生成的新层保存一份“刚进层”的初始档。 */
    public void onLevelSwitched(Level level) {
        int depth = pendingFreshDepth;
        int branch = pendingFreshBranch;
        pendingFreshDepth = -1;
        pendingFreshBranch = -1;
        if (level == null || depth < 0) return;
        if (!initSaveEnabled()) return;
        if (role == Role.CLIENT) return;                       // 访客本地生成的层不写房主快照
        if (Dungeon.depth != depth || Dungeon.branch != branch) return;
        int slot = GamesInProgress.curSlot;
        if (slot <= 0 || slot == GUEST_TEMP_SLOT) return;      // 只抓普通存档槽
        captureInitialLevel(level, depth, branch, slot);
    }

    /** 保存某层刚生成、尚未探索的状态；第0层额外保存初始 game.dat。 */
    private void captureInitialLevel(Level level, int depth, int branch, int slot) {
        try {
            String dir = GamesInProgress.gameFolder(slot) + "/" + MP_INIT_DIR;
            if (depth == 0 && branch == 0 && FileUtils.dirExists(dir)) {
                // 新开档：清掉上一次世界留下的初始快照
                FileUtils.deleteDir(dir);
            }
            FileUtils.getFileHandle(dir).mkdirs();
            if (depth == 0 && branch == 0) {
                Bundle meta = new Bundle();
                meta.put("seed", Dungeon.seed);
                meta.put("version", Game.versionCode);
                FileUtils.bundleToFile(dir + "/meta.dat", meta);
            }

            Bundle b = new Bundle();
            boolean[] visited = level.visited;
            boolean[] mapped = level.mapped;
            // 初始层档不保存房主的探图；访客进入后自己重新探索
            level.visited = visited == null ? null : new boolean[visited.length];
            level.mapped = mapped == null ? null : new boolean[mapped.length];
            try {
                b.put("level", level);
            } finally {
                level.visited = visited;
                level.mapped = mapped;
            }
            FileUtils.bundleToFile(initLevelFile(dir, depth, branch), b);

            if (depth == 0 && branch == 0) {
                FileHandle game = FileUtils.getFileHandle(GamesInProgress.gameFile(slot));
                if (game.exists()) {
                    FileHandle dst = FileUtils.getFileHandle(dir + "/game.dat");
                    if (dst.exists()) dst.delete();
                    game.copyTo(dst);
                }
            }
        } catch (Throwable ignored) {
            // 快照失败不影响正常游戏；房主没有快照时访客会退回原来的“跟随当前层”逻辑
        }
    }

    /** 打包房主的初始世界：初始 game.dat + 所有已抓取的层档；没有第0层快照时返回 null。 */
    private Bundle buildInitWorldPack(int slot) {
        if (!initSaveEnabled()) return null;
        try {
            String dir = GamesInProgress.gameFolder(slot) + "/" + MP_INIT_DIR;
            if (!FileUtils.dirExists(dir)) return null;
            Bundle meta = FileUtils.bundleFromFile(dir + "/meta.dat");
            if (meta == null || meta.getLong("seed") != Dungeon.seed
                    || meta.getInt("version") != Game.versionCode) return null;
            FileHandle initGame = FileUtils.getFileHandle(dir + "/game.dat");
            FileHandle init0 = FileUtils.getFileHandle(initLevelFile(dir, 0, 0));
            if (!initGame.exists() || !init0.exists()) return null;

            String initGameB64 = Base64.encodeBytes(initGame.readBytes());
            long total = initGameB64.length();
            Bundle levels = new Bundle();
            for (String file : FileUtils.filesInDir(dir)) {
                if (!file.startsWith("depth") || !file.endsWith(".dat")) continue;
                int code = levelCodeFromFile(file);
                if (code < 0) continue;
                byte[] bytes = FileUtils.getFileHandle(dir + "/" + file).readBytes();
                String b64 = Base64.encodeBytes(bytes);
                total += b64.length();
                if (total > 40_000_000L) return null;   // 避免 STATE 超过 MAX_FRAME
                levels.put(String.valueOf(code), b64);
            }
            if (levels.getKeys().isEmpty()) return null;

            Bundle pack = new Bundle();
            pack.put(MP_INIT_GAME, initGameB64);
            pack.put(MP_INIT_LEVELS, levels);
            return pack;
        } catch (Throwable t) {
            return null;
        }
    }

    private String initLevelFile(String dir, int depth, int branch) {
        return dir + "/" + levelFileName(depth, branch);
    }

    private String levelFileName(int depth, int branch) {
        return branch == 0 ? "depth" + depth + ".dat"
                : "depth" + depth + "-branch" + branch + ".dat";
    }

    /** 从 depthN.dat / depthN-branchB.dat 解析出 depth + 1000*branch。 */
    private int levelCodeFromFile(String file) {
        try {
            String name = file.substring(0, file.length() - 4);
            int branch = 0;
            int bi = name.indexOf("-branch");
            if (bi >= 0) {
                branch = Integer.parseInt(name.substring(bi + 7));
                name = name.substring(0, bi);
            }
            if (!name.startsWith("depth")) return -1;
            return Integer.parseInt(name.substring(5)) + 1000 * branch;
        } catch (Throwable t) {
            return -1;
        }
    }

    /** 访客自己的持久世界目录（按房主世界种子分档）。 */
    private String guestWorldDirForSeed(long seed) {
        if (seed == Long.MIN_VALUE) return null;
        return MP_WORLD_PREFIX + seed;
    }

    private String guestWorldDir() {
        return guestWorldDirForSeed(worldSeed);
    }

    private boolean hasGuestWorldForSeed(long seed) {
        String dir = guestWorldDirForSeed(seed);
        return dir != null && FileUtils.dirExists(dir)
                && FileUtils.fileLength(dir + "/game.dat") > 1;
    }

    private boolean hasGuestWorld() {
        return hasGuestWorldForSeed(worldSeed);
    }

    /** 持久世界还在、但 mp_hero 丢失时，从世界档的 game.dat 恢复英雄档。 */
    private Bundle loadGuestHeroFromWorld() {
        try {
            String dir = guestWorldDir();
            if (dir == null) return null;
            Bundle world = FileUtils.bundleFromFile(dir + "/game.dat");
            if (world == null || !world.contains("hero")) return null;
            Bundle hero = world.getBundle("hero");
            if (hero.isNull() || !hero.contains("class")) return null;
            Bundle b = new Bundle();
            b.put(MP_HERO, hero);
            b.put(MP_DEPTH, world.getInt("depth"));
            b.put(MP_BRANCH, world.getInt("branch"));
            saveGuestHeroBundle(b);
            return b;
        } catch (Throwable t) {
            return null;
        }
    }

    /** 用房主初始世界包创建访客自己的持久世界，从第0层开始。 */
    private boolean createGuestWorldFromInit(Bundle init, Bundle guest) {
        try {
            if (init == null || init.isNull()) return false;
            String gameB64 = init.getString(MP_INIT_GAME);
            Bundle levels = init.getBundle(MP_INIT_LEVELS);
            if (gameB64 == null || levels.isNull() || !levels.contains("0")) return false;
            if (hasGuestWorld()) return false;   // 不覆盖已有的持久世界
            Bundle world = Bundle.read(new ByteArrayInputStream(Base64.decode(gameB64)));
            Bundle heroBundle = guestHeroPart(guest);
            if (heroBundle == null) return false;

            synchronized (guestExplored) { guestExplored.clear(); }   // 新世界：不继承旧探图
            keepSafeBuffs(heroBundle);
            heroBundle.put("time", 0f);
            ArrayList<Bundlable> buffs = new ArrayList<>(heroBundle.getCollection("buffs"));
            for (Bundlable x : buffs) {
                if (x instanceof Buff) ((Buff) x).clearTime();
            }
            heroBundle.put("buffs", buffs);
            putGuestHero(world, guest, heroBundle, -1);
            world.remove("placeholders");
            world.remove("placements");
            world.remove("slot_actions");
            if (guest.contains(MP_QUICKSLOT)) {
                Bundle qs = guest.getBundle(MP_QUICKSLOT);
                world.put("placeholders", qs.getCollection("placeholders"));
                world.put("placements", qs.getBooleanArray("placements"));
                if (qs.contains("slot_actions")) {
                    world.put("slot_actions", qs.getStringArray("slot_actions"));
                }
            }

            world.put("depth", 0);
            world.put("branch", 0);
            int[] codes = new int[levels.getKeys().size()];
            int n = 0;
            for (String key : levels.getKeys()) {
                codes[n++] = Integer.parseInt(key);
            }
            world.put("generated_levels", codes);
            world.remove(MP_INIT_WORLD);

            String dir = guestWorldDir();
            if (dir == null) return false;
            if (FileUtils.dirExists(dir)) FileUtils.deleteDir(dir);
            FileUtils.getFileHandle(dir).mkdirs();
            FileUtils.bundleToFile(dir + "/game.dat", world);
            for (String key : levels.getKeys()) {
                int code = Integer.parseInt(key);
                byte[] bytes = Base64.decode(levels.getString(key));
                FileUtils.getFileHandle(dir + "/" + levelFileName(code % 1000, code / 1000))
                        .writeBytes(bytes, false);
            }
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /** 把访客英雄写回世界 game bundle；兼容旧版平铺英雄档（没有 __className）。 */
    private void putGuestHero(Bundle world, Bundle guest, Bundle hero, int pos) {
        if (hero == null) return;
        if (hero != guest) {
            hero.put("pos", pos);
            world.put("hero", hero);
        } else {
            // 只读恢复访客档，避免把访客物品当成本机拾取（徽章/天赋/快捷键）。
            // 该路径可能运行在网络线程，只用按线程隔离的 PreviewMode。
            Hero h = new Hero();
            PreviewMode.begin();
            try {
                h.restoreFromBundle(guest);
            } finally {
                PreviewMode.end();
            }
            h.pos = pos;
            world.put("hero", h);
        }
    }

    /** 把访客持久世界复制到隐藏临时槽，并像“继续游戏”一样进入。 */
    private boolean enterGuestWorld(Bundle guest, boolean freshWorld) {
        try {
            String dir = guestWorldDir();
            if (dir == null || !hasGuestWorld()) return false;
            Bundle world = FileUtils.bundleFromFile(dir + "/game.dat");
            if (world == null || !world.contains("hero") || world.getLong("seed") != worldSeed) {
                return false;
            }

            if (freshWorld) {
                synchronized (guestExplored) { guestExplored.clear(); }
            }
            Bundle hero = guestHeroPart(guest);
            if (hero != null) {
                if (!freshWorld) loadGuestExplored(guest);
                keepSafeBuffs(hero);
                hero.put("time", 0f);
                ArrayList<Bundlable> buffs = new ArrayList<>(hero.getCollection("buffs"));
                for (Bundlable x : buffs) {
                    if (x instanceof Buff) ((Buff) x).clearTime();
                }
                hero.put("buffs", buffs);

                int depth = world.getInt("depth");
                int branch = world.getInt("branch");
                boolean depthFromHero = false;
                if (!freshWorld && guest != null
                        && guest.contains(MP_DEPTH) && guest.contains(MP_BRANCH)) {
                    int gd = guest.getInt(MP_DEPTH);
                    int gb = guest.getInt(MP_BRANCH);
                    if ((gd != depth || gb != branch)
                            && FileUtils.getFileHandle(dir + "/" + levelFileName(gd, gb)).exists()) {
                        // 英雄档比 game.dat 新（例如异常退出）：以英雄档的最后楼层为准
                        depth = gd;
                        branch = gb;
                        depthFromHero = true;
                        world.put("depth", depth);
                        world.put("branch", branch);
                    }
                }
                int pos = -1;
                if (!freshWorld && guest != null
                        && guest.contains(MP_DEPTH) && guest.getInt(MP_DEPTH) == depth
                        && guest.getInt(MP_BRANCH) == branch
                        && hero.contains("pos") && hero.getInt("pos") >= 0) {
                    pos = hero.getInt("pos");
                } else if (!freshWorld && !depthFromHero) {
                    Bundle oldHero = world.getBundle("hero");
                    if (!oldHero.isNull() && oldHero.contains("pos")) pos = oldHero.getInt("pos");
                }

                if (guest.contains(MP_QUICKSLOT)) {
                    world.remove("placeholders");
                    world.remove("placements");
                    world.remove("slot_actions");
                    Bundle qs = guest.getBundle(MP_QUICKSLOT);
                    world.put("placeholders", qs.getCollection("placeholders"));
                    world.put("placements", qs.getBooleanArray("placements"));
                    if (qs.contains("slot_actions")) {
                        world.put("slot_actions", qs.getStringArray("slot_actions"));
                    }
                }
                putGuestHero(world, guest, hero, pos);
                FileUtils.bundleToFile(dir + "/game.dat", world);
            }

            copyGuestWorldToSlot(dir);
            guestWorldPersistent = true;
            final int slot = GUEST_TEMP_SLOT;
            Game.runOnRenderThread(() -> {
                GamesInProgress.curSlot = slot;
                ActionIndicator.action = null;
                InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
                ShatteredPixelDungeon.switchScene(InterlevelScene.class);
            });
            status = Messages.get(Mp.class, "status_entered_game");
            return true;
        } catch (Throwable t) {
            status = Messages.get(Mp.class, "status_enter_game_failed", t.getMessage());
            return false;
        }
    }

    /** 持久世界目录 -> 隐藏槽 game99（清空旧槽后复制 game.dat 与所有层档）。 */
    private void copyGuestWorldToSlot(String dir) throws IOException {
        String slotDir = GamesInProgress.gameFolder(GUEST_TEMP_SLOT);
        if (FileUtils.dirExists(slotDir)) FileUtils.deleteDir(slotDir);
        FileUtils.getFileHandle(slotDir).mkdirs();
        for (String file : FileUtils.filesInDir(dir)) {
            if (!file.equals("game.dat") && !file.startsWith("depth")) continue;
            FileHandle src = FileUtils.getFileHandle(dir + "/" + file);
            FileHandle dst = FileUtils.getFileHandle(slotDir + "/" + file);
            if (dst.exists()) dst.delete();
            src.copyTo(dst);
        }
        GamesInProgress.setUnknown(GUEST_TEMP_SLOT);
    }

    /** 隐藏槽 game99 -> 持久世界目录（断线/退出前用）。 */
    private void saveGuestWorldFromSlot() {
        if (!guestWorldPersistent) return;
        if (GamesInProgress.curSlot != GUEST_TEMP_SLOT) return;
        String dir = guestWorldDir();
        if (dir == null) return;
        try {
            String slotDir = GamesInProgress.gameFolder(GUEST_TEMP_SLOT);
            if (!FileUtils.dirExists(slotDir)) return;
            if (FileUtils.dirExists(dir)) FileUtils.deleteDir(dir);
            FileUtils.getFileHandle(dir).mkdirs();
            for (String file : FileUtils.filesInDir(slotDir)) {
                if (!file.equals("game.dat") && !file.startsWith("depth")) continue;
                FileHandle src = FileUtils.getFileHandle(slotDir + "/" + file);
                FileHandle dst = FileUtils.getFileHandle(dir + "/" + file);
                if (dst.exists()) dst.delete();
                src.copyTo(dst);
            }
        } catch (Throwable ignored) {}
    }

    /** 先把当前游戏写盘，再复制到持久世界目录（断线/关窗口前用）。 */
    private void saveGuestWorldNow() {
        if (!guestWorldPersistent) return;
        if (GamesInProgress.curSlot != GUEST_TEMP_SLOT) return;
        try {
            if (Dungeon.hero != null && Dungeon.level != null) Dungeon.saveAll();
        } catch (Throwable ignored) {}
        saveGuestWorldFromSlot();
    }

    // ---------------------------------------------------------------- 访客英雄独立存档

    /** 访客英雄按"世界种子"分档的文件名：房主开新档=新种子=新英雄；回旧档=恢复旧英雄 */
    private String mpHeroFile() {
        if (worldSeed == Long.MIN_VALUE) return null;   // 还没收到房主世界，禁止写"无效种子"档
        return mpHeroFile(worldSeed);
    }

    /** 指定世界种子的访客英雄档文件名（房间阶段只读检查用）。 */
    private static String mpHeroFile(long seed) {
        if (seed == Long.MIN_VALUE || seed < 0) return null;
        return "mp_hero_" + seed + ".dat";
    }

    /** 取访客英雄档里的英雄子 bundle（新格式嵌套带类名；兼容旧版平铺格式） */
    private Bundle guestHeroPart(Bundle file) {
        if (file == null) return null;
        Bundle nested = file.getBundle(MP_HERO);
        if (!nested.isNull() && nested.contains("class")) return nested;
        if (file.contains("class")) return file;   // 旧格式：英雄字段平铺在文件顶层
        return null;
    }

    /** 剔除跨会话不安全的 buff，只保留饥饿与自然回血（决定饱食度与自动回血，缺了补上） */
    private void keepSafeBuffs(Bundle heroBundle) {
        if (heroBundle == null) return;
        ArrayList<Bundlable> keep = new ArrayList<>();
        boolean hasHunger = false;
        boolean hasRegen = false;
        for (Bundlable b : heroBundle.getCollection("buffs")) {
            if (b instanceof Hunger) {
                hasHunger = true;
                keep.add(b);
            } else if (b instanceof Regeneration) {
                hasRegen = true;
                keep.add(b);
            }
        }
        if (!hasHunger) keep.add(new Hunger());
        if (!hasRegen) keep.add(new Regeneration());
        heroBundle.put("buffs", keep);
    }

    /** 访客英雄专用存档：存在就读，不存在返回 null（由 applyState 弹职业选择窗口） */
    private synchronized Bundle loadGuestHeroBundle() {
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
            if (b != null && guestHeroPart(b) != null) return b;
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
            if (b != null && guestHeroPart(b) != null) {
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

        // 与单人新档一致：常驻饥饿与自然回血 buff（访客不走 Dungeon.init/live()，必须自己补）
        Buff.affect(h, Hunger.class);
        Buff.affect(h, Regeneration.class);

        Bundle b = new Bundle();
        b.put(MP_HERO, h);   // 嵌套写入（自带类名），与 saveGuestHero 的文件格式一致
        return b;
    }

    /** 把访客英雄 bundle 落盘（按世界种子分档；剔除 buff，避免跨世界引用）；返回是否成功 */
    private synchronized boolean saveGuestHeroBundle(Bundle b) {
        String file = mpHeroFile();
        if (file == null) {
            status = Messages.get(Mp.class, "guest_hero_not_in_world");
            return false;
        }
        try {
            keepSafeBuffs(guestHeroPart(b));   // 只保留饥饿/自然回血，其余 buff 可能引用上个世界的对象
            FileHandle f = FileUtils.getFileHandle(file);
            // 覆盖前保留上一份**可用**档案：写入中断/校验失败时可回退。
            // 主档本身已损坏时不要拿它覆盖旧的 .bak。
            if (f.exists()) {
                try {
                    Bundle old = FileUtils.bundleFromFile(file);
                    if (old != null && guestHeroPart(old) != null) {
                        FileHandle bak = FileUtils.getFileHandle(file + ".bak");
                        if (bak.exists()) bak.delete();
                        f.copyTo(bak);
                    }
                } catch (Throwable ignored) {}
            }
            FileUtils.bundleToFile(file, b);
            // 读回校验：只有确认落盘的英雄档完整，调用方才允许清理临时世界
            Bundle check = FileUtils.bundleFromFile(file);
            if (check == null || guestHeroPart(check) == null) {
                status = Messages.get(Mp.class, "guest_hero_save_check_failed");
                return false;
            }
            return true;
        } catch (Throwable t) {
            status = Messages.get(Mp.class, "guest_hero_save_failed", t.getMessage());
            return false;
        }
    }

    /**
     * Android 端低血量受击整屏红光兜底。
     * 原版 Hero.takeDamage 在 Actor 线程直接调用 GameScene.flash；部分 Android 环境下
     * 该路径可能不生效。这里在渲染线程按同样的伤害/血量公式补一次红屏。
     */
    private void androidLowHpFlash() {
        if (!DeviceCompat.isAndroid() || Dungeon.hero == null) return;
        int hp = Dungeon.hero.HP;
        int ht = Dungeon.hero.HT;
        if (lastHeroHp > 0 && hp > 0 && hp < lastHeroHp && ht > 0) {
            float percentDMG = (lastHeroHp - hp) / (float) lastHeroHp;
            float percentHP = hp / (float) ht;
            float intensity = 0.25f * (percentDMG * percentDMG) / percentHP;
            if (intensity >= 0.05f) {
                intensity = Math.min(1f / 3f, intensity);
                GameScene.flash((int) (0xFF * intensity) << 16);
            }
        }
        lastHeroHp = hp;
    }

    /** 记录当前楼层的探图到缓存（每层一份；跨楼层/重连保留访客自己的视野）。 */
    private void snapshotExplored() {
        if (role != Role.CLIENT || Dungeon.level == null) return;
        boolean[] v = Dungeon.level.visited;
        boolean[] m = Dungeon.level.mapped;
        if (v == null || m == null) return;
        Bundle vis = new Bundle();
        vis.put("visited", v.clone());
        vis.put("mapped", m.clone());
        synchronized (guestExplored) {
            guestExplored.put(Dungeon.depth + 1000 * Dungeon.branch, vis);
        }
    }

    /** 从访客英雄档恢复各层探图缓存。 */
    private void loadGuestExplored(Bundle guest) {
        synchronized (guestExplored) {
            guestExplored.clear();
            if (guest == null) return;
            for (String key : guest.getKeys()) {
                if (!key.startsWith(MP_EXPLORED)) continue;
                try {
                    String suffix = key.substring(MP_EXPLORED.length());
                    int sep = suffix.indexOf('_');
                    if (sep <= 0) continue;
                    int d = Integer.parseInt(suffix.substring(0, sep));
                    int b = Integer.parseInt(suffix.substring(sep + 1));
                    Bundle vis = guest.getBundle(key);
                    if (vis != null) guestExplored.put(d + 1000 * b, vis);
                } catch (Throwable ignored) {}
            }
        }
    }

    /** 把缓存中当前楼层的探图合并进 Dungeon.level，并刷新迷雾。 */
    private void applyExploredToLevel() {
        if (role != Role.CLIENT || Dungeon.level == null || Dungeon.hero == null) return;
        Bundle vis;
        synchronized (guestExplored) {
            vis = guestExplored.get(Dungeon.depth + 1000 * Dungeon.branch);
        }
        if (vis == null) return;
        boolean[] v = vis.getBooleanArray("visited");
        boolean[] m = vis.getBooleanArray("mapped");
        if (v != null && Dungeon.level.visited != null) {
            for (int i = 0; i < v.length && i < Dungeon.level.visited.length; i++) {
                if (v[i]) Dungeon.level.visited[i] = true;
            }
        }
        if (m != null && Dungeon.level.mapped != null) {
            for (int i = 0; i < m.length && i < Dungeon.level.mapped.length; i++) {
                if (m[i]) Dungeon.level.mapped[i] = true;
            }
        }
        try {
            Dungeon.observe();
            GameScene.updateFog();
        } catch (Throwable ignored) {}
    }

    /** GameScene.onPause 调用：切场景/关闭窗口前保存访客英雄和持久世界（不清理临时世界） */
    public void onGamePause() {
        if (role == Role.CLIENT && guestWorldActive && !enteringNewWorld) {
            saveGuestHero();
            saveGuestWorldNow();
        }
    }

    /** 把当前访客英雄状态落盘（跨会话保留背包/血量/充能）；返回是否成功 */
    private synchronized boolean saveGuestHero() {
        if (!guestWorldActive || worldSeed == Long.MIN_VALUE) return false;
        Hero h = Dungeon.hero;
        if (h == null) return false;
        try {
            Bundle b = new Bundle();
            b.put(MP_HERO, h);                          // 嵌套写入（自带类名）
            b.put(MP_DEPTH, Dungeon.depth);             // 记录所在层：重连时校验上次位置是否仍可用
            b.put(MP_BRANCH, Dungeon.branch);
            Bundle qs = new Bundle();
            Dungeon.quickslot.storePlaceholders(qs);    // 快捷栏占位符 + 槽位技能选择
            b.put(MP_QUICKSLOT, qs);
            // 探图数据（visited/mapped）按层留档：房主存档里只有房主自己的探索记录
            snapshotExplored();
            synchronized (guestExplored) {
                for (Integer code : guestExplored.keySet()) {
                    b.put(MP_EXPLORED + code % 1000 + "_" + code / 1000, guestExplored.get(code));
                }
            }
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

    /** 房主新建档首次进入 GameScene 后落盘，并把房间存档标记为已有档。 */
    private void tickPendingRoomSave() {
        if (!pendingRoomSaveWrite) return;
        if (Dungeon.hero == null || Dungeon.level == null) return;
        try {
            Dungeon.saveAll();
        } catch (Throwable ignored) {}
        pendingRoomSaveWrite = false;
        refreshRoomSaveAfterGame();
        roomStateDirty = true;
        broadcastRoomState();
    }

    /**
     * 由 GameScene.update() 每帧调用（渲染线程）
     *
     * @param gs 参数。
     */
    public void tick(GameScene gs) { // skipcq: JAVA-R1000 联机每帧主循环，复杂度高但已实测稳定
        // 房主新建档首次进入 GameScene：立即落盘一次，房间里的存档从“新档”变成“已有档”。
        // 必须在 !connected 提前返回之前执行——房主可能没有访客，connected 仍为 false。
        if (role == Role.HOST) {
            tickPendingRoomSave();
            flushRoomStateIfNeeded();
        }
        // 房主在死亡后点“重新开始”会直接进入新档（不经过 TitleScene）；
        // 世界标识变化时断开旧访客，避免跨世界同层幽灵。未连接时只更新标识。
        if (role == Role.HOST && Dungeon.hero != null && Dungeon.level != null) {
            String key = GamesInProgress.curSlot + ":" + Dungeon.seed;
            if (hostWorldKey == null) {
                hostWorldKey = key;
            } else if (!hostWorldKey.equals(key)) {
                hostWorldKey = key;
                if (connected) {
                    restartWorldForGuests();
                }
            }
        }
        if (!connected) {
            hideChatButton();
            hideMpLogPane();
            return;
        }

        // T 键 / 局内聊天按钮都收敛在 Mp 里，GameScene 只需每帧调用 tick
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            openChat();
        }

        // 只有真正进入 GameScene 且有英雄时，才认为当前 Dungeon.hero 属于联机世界；
        // 避免在标题/stale 状态下把旧英雄写进访客档。
        if (role == Role.CLIENT && Dungeon.hero != null && Dungeon.level != null
                && !enteringNewWorld) {
            guestWorldActive = true;
            lastGuestPos = Dungeon.hero.pos;
            lastGuestDepth = Dungeon.depth;
            lastGuestBranch = Dungeon.branch;
            lastGuestWorldSeed = worldSeed;
            // 访客在死亡后点“重新开始”会进入本地新档；检测到不在房主世界（槽 99/种子）就退出联机
            if (worldSeed != Long.MIN_VALUE
                    && (GamesInProgress.curSlot != GUEST_TEMP_SLOT || Dungeon.seed != worldSeed)) {
                final String msg = Messages.get(Mp.class, "guest_new_game");
                Game.runOnRenderThread(() -> {
                    // 当前 Dungeon.hero 已属于本地新档，不能按房主世界种子写回访客英雄档；
                    // 只退出联机，保留本地新档继续玩（不强制回标题）
                    guestWorldActive = false;
                    worldSeed = Long.MIN_VALUE;
                    disconnect();
                    systemNotice(msg);
                });
                return;
            }
        }

        if (gs != lastScene) {
            lastScene = gs;
            enteringNewWorld = false;                   // 已进入新场景，恢复正常同步/存档
            lastHeroHp = Dungeon.hero != null ? Dungeon.hero.HP : -1;
            chatButton = null;                          // 新场景重新挂按钮（旧按钮随旧场景销毁）
            mpLogPane = null;                           // 左上角联机日志随旧场景销毁
            if (role == Role.CLIENT) {
                applyExploredToLevel();                 // 先恢复本层自己的探图
                saveGuestHero();                        // 换层/重进场景时落盘访客英雄
                saveGuestWorldFromSlot();               // 持久世界同步到 mp_world_<seed>/
            }
            synchronized (peers) {
                for (Peer p : peers) {
                    p.remote = null;
                    p.remoteSprite = null;
                    p.nameLabel = null;
                    p.hpBg = null;
                    p.hpFill = null;
                    p.remotePos = -1;
                    p.remoteDepth = -1;
                    p.remoteBranch = -1;
                    p.remoteStale = false;
                }
            }
            lastSentPos = -1;
        }

        if (enteringNewWorld) {
            return;   // 等待新世界 STATE/场景切换，暂停旧世界的外发同步
        }

        androidLowHpFlash();

        // 每秒记录一次当前层探图，跨楼层/断线重连时恢复访客自己的视野
        if (role == Role.CLIENT && Dungeon.level != null) {
            long exploreNow = System.currentTimeMillis();
            if (exploreNow - lastExploredSnapshot > 1000L) {
                lastExploredSnapshot = exploreNow;
                snapshotExplored();
            }
        }

        updateChatButton(gs);
        updateMpLogPane(gs);

        // 房主侧：给每个还没收到进度的访客发 STATE（新加入的访客也会在这里补发）
        if (role == Role.HOST && Dungeon.hero != null && Dungeon.level != null) {
            // 换档等待访客确认；超时断开，避免旧连接一直挂着
            long resetNow = System.currentTimeMillis();
            synchronized (peers) {
                for (Peer p : peers) {
                    if (p.worldResetPending && resetNow - p.worldResetAt > 5000L) {
                        p.worldResetPending = false;
                        p.worldEnded = true;
                        try { if (p.sock != null) p.sock.close(); } catch (IOException ignored) {}
                    }
                }
            }
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
                    // 房主有第0层初始快照时，把初始世界包附在 game bundle 里发给新访客
                    try {
                        Bundle initPack = buildInitWorldPack(slot);
                        if (initPack != null) {
                            Bundle gb = Bundle.read(new ByteArrayInputStream(game));
                            gb.put(MP_INIT_WORLD, initPack);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            if (Bundle.write(gb, bos)) game = bos.toByteArray();
                        }
                    } catch (Throwable ignored) {}
                    byte[] payload = encodeState(Dungeon.depth, Dungeon.branch, game, level);
                    synchronized (peers) {
                        for (Peer p : peers) {
                            if (p.versionOk && !p.gotState) {
                                try {
                                    sendTo(p, Proto.STATE, payload);
                                    // 新访客需要知道现有的管理员身份
                                    for (Peer ap : peers) {
                                        if (ap.admin) {
                                            sendTo(p, Proto.ADMIN, encodeAdmin(ap.id, true));
                                        }
                                    }
                                    p.gotState = true;
                                } catch (IOException ignored) {}
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
            // 任何玩家死亡都播报一次（事件栏红色）；复活/进新档后复位，可再次播报
            boolean alive = Dungeon.hero.isAlive() && Dungeon.hero.HP > 0;
            if (alive) {
                localDeathAnnounced = false;
            } else if (!localDeathAnnounced) {
                localDeathAnnounced = true;
                int did = isHost() ? HOST_ID : myId;
                if (did >= 0) {
                    try { sendToAll(Proto.DEATH, encodeInt(did)); } catch (Throwable ignored) {}
                }
                deathNotice(Messages.get(Mp.class, "notice_self_died"));
            }
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
            // 每秒同步一次血量（/list 与幽灵头顶血量条用）
            if (now - lastStatsTime > 1000L) {
                lastStatsTime = now;
                int sid = isHost() ? HOST_ID : myId;
                if (sid >= 0) {
                    try { sendToAll(Proto.STATS, encodeStats(sid, Dungeon.hero.HP, Dungeon.hero.HT)); } catch (Throwable ignored) {}
                }
            }
            // 每 3 秒同步一次完整英雄信息（/list 点击与放大镜查看玩家详情用）
            if (now - lastHeroInfoTime > 3000L) {
                lastHeroInfoTime = now;
                int hid = isHost() ? HOST_ID : myId;
                if (hid >= 0) {
                    try { sendToAll(Proto.HERO_INFO, encodeOwnHeroInfo(hid)); } catch (Throwable ignored) {}
                }
            }
        }

        tickPing();

        syncRemote(gs);
    }

    /** 每 3 秒测一次延迟：房主测所有访客并广播 RTT_INFO；访客测房主。房间/对局共用。 */
    private void tickPing() {
        long pingNow = System.currentTimeMillis();
        if (pingNow - lastPingTime <= 3000L) return;
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
                        // 连续多个心跳无回应：网络状态不佳，回大厅/标题并弹窗说明原因
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

    /**
     * 由 TitleScene.update() 每帧调用（渲染线程）：检测退出存档
     *
     * @param ts 参数。
     */
    public void tickTitle(TitleScene ts) {
        if (!connected || lastScene == null) return;
        // 联机房间存在时，退出对局回房间而不是停在标题（兜底；正常路径在 WndGame 里已经处理）
        if (isInRoom()) {
            if (isHost()) {
                hostReturnToRoom();
            } else {
                clientReturnToRoom();
            }
            return;
        }
        lastScene = null;
        lastSentPos = -1;
        synchronized (peers) {
            for (Peer p : peers) {
                p.remote = null;
                p.remoteSprite = null;
                p.nameLabel = null;
                p.hpBg = null;
                p.hpFill = null;
                p.remotePos = -1;
                p.remoteDepth = -1;
                p.remoteBranch = -1;
                p.remoteStale = false;
                p.gotState = false;   // 下次进档重新发 STATE
            }
        }
        if (role == Role.HOST) {
            // 房主退出存档：踢所有访客回标题；连接保持，房主存档只在本机
            hostWorldKey = null;   // 下次进档重新初始化，避免和新档混淆
            sendToAll(Proto.KICK, new byte[0]);
            status = Messages.get(Mp.class, "status_host_exit_save");
        } else {
            // 访客退出存档（主动或被踢）：先保存自己的英雄和持久世界，再清理临时世界；连接保持
            boolean ok = !guestWorldActive || saveGuestHero();
            if (ok) {
                // 正在应用新房主 STATE 时不要清临时槽/种子，否则可能把刚恢复的世界又清掉
                if (!enteringNewWorld) {
                    saveGuestWorldFromSlot();   // 持久世界写回 mp_world_<seed>/
                    clearGuestTempSlot();
                    guestWorldActive = false;
                    guestWorldPersistent = false;
                    worldSeed = Long.MIN_VALUE;
                }
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
                if (p.hp >= 0 && p.ht > 0) {
                    p.remote.HP = p.hp;
                    p.remote.HT = p.ht;
                }
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

        // 名字在上、血量条在下，都悬在幽灵正上方（不显示自己的名字）
        if (p.nameLabel == null) {
            p.nameLabel = PixelScene.renderTextBlock(6);
            p.nameLabel.text(p.name);
            gs.addToFront(p.nameLabel);
        }
        if (p.hpBg == null) {
            p.hpBg = new ColorBlock(HP_BAR_WIDTH, HP_BAR_HEIGHT, 0xCC330000);
            p.hpFill = new ColorBlock(HP_BAR_WIDTH, HP_BAR_HEIGHT, 0xFF44CC44);
            gs.addToFront(p.hpBg);
            gs.addToFront(p.hpFill);
        }
        float barY = p.remoteSprite.y - HP_BAR_HEIGHT - 2f;
        boolean showHp = inFov && p.hp >= 0 && p.ht > 0;
        p.hpBg.visible = showHp;
        p.hpFill.visible = showHp;
        if (showHp) {
            float barX = p.remoteSprite.x + (p.remoteSprite.width() - HP_BAR_WIDTH) / 2f;
            p.hpBg.setPos(barX, barY);
            p.hpFill.setPos(barX, barY);
            float ratio = Math.max(0f, Math.min(1f, p.hp / (float) p.ht));
            p.hpFill.size(HP_BAR_WIDTH * ratio, HP_BAR_HEIGHT);
        }
        p.nameLabel.setPos(
                p.remoteSprite.x + (p.remoteSprite.width() - p.nameLabel.width()) / 2f,
                barY - p.nameLabel.height() - 1f);
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

        /** STATS 帧载荷：玩家 id + 当前血量 + 血量上限。 */
    public static class StatsPayload {
        /** 玩家 id。 */
        public int id;
        /** 当前血量。 */
        public int hp;
        /** 血量上限。 */
        public int ht;
    }

        /** HERO_INFO 帧载荷：玩家 id + 完整英雄 bundle 字节。 */
    public static class HeroInfoPayload {
        /** 玩家 id。 */
        public int id;
        /** 英雄 bundle 字节（Bundle.read 可还原出完整英雄）。 */
        public byte[] bundle;
    }

        /** ADMIN 帧载荷：玩家 id + 是否管理员。 */
    public static class AdminPayload {
        /** 玩家 id。 */
        public int id;
        /** 是否管理员。 */
        public boolean admin;
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

    /**
     * 编码 STATS 帧：玩家 id + 当前血量 + 血量上限。
     *
     * @param id 参数。
     * @param hp 参数。
     * @param ht 参数。
     * @return 返回值。
     * @throws IOException 参数。
     */
    public static byte[] encodeStats(int id, int hp, int ht) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(12);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(id);
        dos.writeInt(hp);
        dos.writeInt(ht);
        dos.flush();
        return bos.toByteArray();
    }

    /**
     * 解码 STATS 帧。
     *
     * @param data 参数。
     * @return 返回值。
     * @throws IOException 参数。
     */
    public static StatsPayload decodeStats(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        StatsPayload p = new StatsPayload();
        p.id = di.readInt();
        p.hp = di.readInt();
        p.ht = di.readInt();
        return p;
    }

    /**
     * 编码 HERO_INFO 帧：玩家 id + 英雄 bundle 字节。
     *
     * @param id 参数。
     * @param bundle 参数。
     * @return 返回值。
     * @throws IOException 参数。
     */
    public static byte[] encodeHeroInfo(int id, byte[] bundle) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(4 + (bundle == null ? 0 : bundle.length));
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(id);
        if (bundle != null) dos.write(bundle);
        dos.flush();
        return bos.toByteArray();
    }

    /**
     * 解码 HERO_INFO 帧。
     *
     * @param data 参数。
     * @return 返回值。
     * @throws IOException 参数。
     */
    public static HeroInfoPayload decodeHeroInfo(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        HeroInfoPayload p = new HeroInfoPayload();
        p.id = di.readInt();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = di.read(buf)) > 0) bos.write(buf, 0, n);
        p.bundle = bos.toByteArray();
        return p;
    }

    /**
     * 把本机英雄打包成 HERO_INFO 载荷：完整 bundle（血量/力量/天赋/正负面效果都在里面）。
     *
     * @param id 参数。
     * @return 返回值。
     * @throws IOException 参数。
     */
    public static byte[] encodeOwnHeroInfo(int id) throws IOException {
        Bundle b = new Bundle();
        Dungeon.hero.storeInBundle(b);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Bundle.write(b, bos);
        bos.flush();
        return encodeHeroInfo(id, bos.toByteArray());
    }

    /**
     * 编码 ADMIN 帧：玩家 id + 是否管理员。
     *
     * @param id 参数。
     * @param admin 参数。
     * @return 返回值。
     * @throws IOException 参数。
     */
    public static byte[] encodeAdmin(int id, boolean admin) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(5);
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(id);
        dos.writeBoolean(admin);
        dos.flush();
        return bos.toByteArray();
    }

    /**
     * 解码 ADMIN 帧。
     *
     * @param data 参数。
     * @return 返回值。
     * @throws IOException 参数。
     */
    public static AdminPayload decodeAdmin(byte[] data) throws IOException {
        DataInputStream di = new DataInputStream(new ByteArrayInputStream(data));
        AdminPayload p = new AdminPayload();
        p.id = di.readInt();
        p.admin = di.readBoolean();
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

    /** 把网络传来的血量夹到合理范围，避免负血/超长血条。 */
    private static int clampStat(int v) {
        return Math.max(0, Math.min(v, 99999));
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
        /** KICK：房主退出对局，访客回房间（连接保持）。 */
        public static final int KICK = 6;   // 房主 → 访客：房主退出对局，回房间（连接保持）
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
        /** STATS：血量同步（id + HP + HT）；房主转发。 */
        public static final int STATS = 26;    // 双向：血量同步
        /** ADMIN：管理员身份变更（id + 是否管理员）；房主 → 所有人。 */
        public static final int ADMIN = 27;    // 房主 → 所有人：管理员身份
        /** CMD：管理员访客发给房主的指令；房主 → 仅管理员可用。 */
        public static final int CMD = 28;      // 访客 → 房主：管理员指令
        /** DEATH：某玩家死亡（id）；房主转发给其他访客。 */
        public static final int DEATH = 29;    // 双向：死亡播报
        /** WORLD_RESET：房主进入新档，旧访客先保存旧英雄并回 WORLD_READY。 */
        public static final int WORLD_RESET = 30; // 房主 → 访客：准备进入新世界
        /** WORLD_READY：访客已保存旧英雄，可以发新世界 STATE。 */
        public static final int WORLD_READY = 31; // 访客 → 房主：新世界就绪
        /** HERO_INFO：完整英雄 bundle（id + bundle 字节）；用于 /list 点击与放大镜查看玩家详情。 */
        public static final int HERO_INFO = 32;   // 双向：英雄详情同步（房主转发）
        /** ROOM_STATE：房主 → 访客的房间快照（roster + 选中存档）。 */
        public static final int ROOM_STATE = 40;  // 房主 → 访客：开局前房间状态
        /** ROOM_EVENT：房间事件（如某玩家已返回房间）；房主转发。 */
        public static final int ROOM_EVENT = 41;  // 双向：房间事件（房主转发）
        /** PROFILE：个人资料摘要（UUID/佩戴徽章/最高分）；访客 → 房主。 */
        public static final int PROFILE = 42;     // 访客 → 房主：资料摘要
        /** PROFILE_REQ：按需请求某玩家的最高分对局详情。 */
        public static final int PROFILE_REQ = 43; // 请求者 → 房主 → 目标玩家
        /** PROFILE_DATA：目标玩家的最高分对局记录；经房主路由。 */
        public static final int PROFILE_DATA = 44; // 目标玩家 → 房主 → 请求者
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
