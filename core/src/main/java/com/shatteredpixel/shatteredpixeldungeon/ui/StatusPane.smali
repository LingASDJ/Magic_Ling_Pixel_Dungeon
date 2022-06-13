.class public Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;
.super Lcom/watabou/noosa/ui/Component;
.source "StatusPane.java"


# static fields
.field public static final FLASH_RATE:F = 4.712389f

.field private static asset:Ljava/lang/String;

.field public static talentBlink:F

.field private static final warningColors:[I


# instance fields
.field private avatar:Lcom/watabou/noosa/Image;

.field private bg:Lcom/watabou/noosa/NinePatch;

.field public bossselect:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossSelectIndicator;

.field private buffs:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BuffIndicator;

.field private busy:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;

.field private compass:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;

.field private counter:Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;

.field private exp:Lcom/watabou/noosa/Image;

.field private expText:Lcom/watabou/noosa/BitmapText;

.field private heroInfo:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;

.field private heroInfoOnBar:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;

.field private hg:Lcom/watabou/noosa/Image;

.field private hgText:Lcom/watabou/noosa/BitmapText;

.field private hp:Lcom/watabou/noosa/Image;

.field private hpText:Lcom/watabou/noosa/BitmapText;

.field public joinxxx:Lcom/shatteredpixel/shatteredpixeldungeon/ui/JoinIndicator;

.field private large:Z

.field private lastLvl:I

.field private lastTier:I

.field private level:Lcom/watabou/noosa/BitmapText;

.field public mainhand:Lcom/shatteredpixel/shatteredpixeldungeon/ui/MainHandIndicator;

.field public page:Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicator;

.field public pageb:Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicatorB;

.field private rawShielding:Lcom/watabou/noosa/Image;

.field private shieldedHP:Lcom/watabou/noosa/Image;

.field private warning:F


# direct methods
.method static constructor <clinit>()V
    .registers 1

    .prologue
    .line 90
    const-string v0, "interfaces/status_pane.png"

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    .line 288
    const/4 v0, 0x3

    new-array v0, v0, [I

    fill-array-data v0, :array_e

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->warningColors:[I

    return-void

    nop

    :array_e
    .array-data 4
        0x660000
        0xcc0000
        0x660000
    .end array-data
.end method

.method public constructor <init>(Z)V
    .registers 12
    .param p1, "large"    # Z

    .prologue
    .line 95
    invoke-direct {p0}, Lcom/watabou/noosa/ui/Component;-><init>()V

    .line 60
    const/4 v0, 0x0

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->lastTier:I

    .line 73
    const/4 v0, -0x1

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->lastLvl:I

    .line 97
    iput-boolean p1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->large:Z

    .line 99
    if-eqz p1, :cond_1b8

    new-instance v0, Lcom/watabou/noosa/NinePatch;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    const/4 v2, 0x0

    const/16 v3, 0x40

    const/16 v4, 0x29

    const/16 v5, 0x27

    const/16 v6, 0x21

    const/4 v7, 0x0

    const/4 v8, 0x4

    const/4 v9, 0x0

    invoke-direct/range {v0 .. v9}, Lcom/watabou/noosa/NinePatch;-><init>(Ljava/lang/Object;IIIIIIII)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    .line 101
    :goto_22
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 103
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane$1;

    invoke-direct {v0, p0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane$1;-><init>(Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->heroInfo:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;

    .line 120
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->heroInfo:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 122
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->heroClass:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->lastTier:I

    invoke-static {v0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/HeroSprite;->avatar(Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;I)Lcom/watabou/noosa/Image;

    move-result-object v0

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    .line 123
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 125
    const/4 v0, 0x0

    sput v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->talentBlink:F

    .line 127
    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;

    sget-boolean v0, Lcom/shatteredpixel/shatteredpixeldungeon/Statistics;->amuletObtained:Z

    if-eqz v0, :cond_1cf

    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->entrance:I

    :goto_51
    invoke-direct {v1, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;-><init>(I)V

    iput-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->compass:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;

    .line 128
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->compass:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 130
    if-eqz p1, :cond_1d5

    new-instance v0, Lcom/watabou/noosa/Image;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    const/4 v2, 0x0

    const/16 v3, 0x70

    const/16 v4, 0x80

    const/16 v5, 0x9

    invoke-direct/range {v0 .. v5}, Lcom/watabou/noosa/Image;-><init>(Ljava/lang/Object;IIII)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->rawShielding:Lcom/watabou/noosa/Image;

    .line 132
    :goto_6d
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->rawShielding:Lcom/watabou/noosa/Image;

    const/high16 v1, 0x3f000000    # 0.5f

    invoke-virtual {v0, v1}, Lcom/watabou/noosa/Image;->alpha(F)V

    .line 133
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->rawShielding:Lcom/watabou/noosa/Image;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 135
    if-eqz p1, :cond_1e6

    new-instance v0, Lcom/watabou/noosa/Image;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    const/4 v2, 0x0

    const/16 v3, 0x70

    const/16 v4, 0x80

    const/16 v5, 0x9

    invoke-direct/range {v0 .. v5}, Lcom/watabou/noosa/Image;-><init>(Ljava/lang/Object;IIII)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->shieldedHP:Lcom/watabou/noosa/Image;

    .line 137
    :goto_8b
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->shieldedHP:Lcom/watabou/noosa/Image;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 139
    if-eqz p1, :cond_1f7

    new-instance v0, Lcom/watabou/noosa/Image;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    const/4 v2, 0x0

    const/16 v3, 0x67

    const/16 v4, 0x80

    const/16 v5, 0x9

    invoke-direct/range {v0 .. v5}, Lcom/watabou/noosa/Image;-><init>(Ljava/lang/Object;IIII)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    .line 141
    :goto_a2
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 143
    if-eqz p1, :cond_208

    new-instance v0, Lcom/watabou/noosa/Image;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    const/4 v2, 0x0

    const/16 v3, 0x80

    const/16 v4, 0x80

    const/4 v5, 0x7

    invoke-direct/range {v0 .. v5}, Lcom/watabou/noosa/Image;-><init>(Ljava/lang/Object;IIII)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hg:Lcom/watabou/noosa/Image;

    .line 145
    :goto_b8
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hg:Lcom/watabou/noosa/Image;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 147
    new-instance v0, Lcom/watabou/noosa/BitmapText;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->pixelFont:Lcom/watabou/noosa/BitmapText$Font;

    invoke-direct {v0, v1}, Lcom/watabou/noosa/BitmapText;-><init>(Lcom/watabou/noosa/BitmapText$Font;)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    .line 148
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    const v1, 0x3f19999a    # 0.6f

    invoke-virtual {v0, v1}, Lcom/watabou/noosa/BitmapText;->alpha(F)V

    .line 149
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 152
    new-instance v0, Lcom/watabou/noosa/BitmapText;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->pixelFont:Lcom/watabou/noosa/BitmapText$Font;

    invoke-direct {v0, v1}, Lcom/watabou/noosa/BitmapText;-><init>(Lcom/watabou/noosa/BitmapText$Font;)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    .line 153
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    const v1, 0x3f19999a    # 0.6f

    invoke-virtual {v0, v1}, Lcom/watabou/noosa/BitmapText;->alpha(F)V

    .line 154
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 156
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane$2;

    invoke-direct {v0, p0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane$2;-><init>(Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->heroInfoOnBar:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;

    .line 163
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->heroInfoOnBar:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 165
    if-eqz p1, :cond_219

    new-instance v0, Lcom/watabou/noosa/Image;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    const/4 v2, 0x0

    const/16 v3, 0x79

    const/16 v4, 0x80

    const/4 v5, 0x7

    invoke-direct/range {v0 .. v5}, Lcom/watabou/noosa/Image;-><init>(Ljava/lang/Object;IIII)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    .line 167
    :goto_106
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 169
    if-eqz p1, :cond_12b

    .line 170
    new-instance v0, Lcom/watabou/noosa/BitmapText;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->pixelFont:Lcom/watabou/noosa/BitmapText$Font;

    invoke-direct {v0, v1}, Lcom/watabou/noosa/BitmapText;-><init>(Lcom/watabou/noosa/BitmapText$Font;)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->expText:Lcom/watabou/noosa/BitmapText;

    .line 171
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->expText:Lcom/watabou/noosa/BitmapText;

    const v1, 0xffffaa

    invoke-virtual {v0, v1}, Lcom/watabou/noosa/BitmapText;->hardlight(I)V

    .line 172
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->expText:Lcom/watabou/noosa/BitmapText;

    const v1, 0x3f19999a    # 0.6f

    invoke-virtual {v0, v1}, Lcom/watabou/noosa/BitmapText;->alpha(F)V

    .line 173
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->expText:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 176
    :cond_12b
    new-instance v0, Lcom/watabou/noosa/BitmapText;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->pixelFont:Lcom/watabou/noosa/BitmapText$Font;

    invoke-direct {v0, v1}, Lcom/watabou/noosa/BitmapText;-><init>(Lcom/watabou/noosa/BitmapText$Font;)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    .line 177
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    const v1, 0xffffaa

    invoke-virtual {v0, v1}, Lcom/watabou/noosa/BitmapText;->hardlight(I)V

    .line 178
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 180
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BuffIndicator;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    invoke-direct {v0, v1, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BuffIndicator;-><init>(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;Z)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->buffs:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BuffIndicator;

    .line 181
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->buffs:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BuffIndicator;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 183
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;

    invoke-direct {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;-><init>()V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->busy:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;

    .line 184
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->busy:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 186
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;

    const/16 v1, 0x12

    const/high16 v2, 0x40880000    # 4.25f

    invoke-direct {v0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;-><init>(IF)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->counter:Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;

    .line 187
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->counter:Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;

    const v1, 0x808080

    const/4 v2, 0x1

    invoke-virtual {v0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;->color(IZ)Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;

    .line 188
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->counter:Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->busy:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;->center()Lcom/watabou/utils/PointF;

    move-result-object v1

    const/4 v2, 0x0

    invoke-virtual {v0, p0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;->show(Lcom/watabou/noosa/Group;Lcom/watabou/utils/PointF;F)Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;

    .line 190
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicator;

    invoke-direct {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicator;-><init>()V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->page:Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicator;

    .line 191
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->page:Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicator;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 193
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicatorB;

    invoke-direct {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicatorB;-><init>()V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->pageb:Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicatorB;

    .line 194
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->pageb:Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicatorB;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 196
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/MainHandIndicator;

    invoke-direct {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/MainHandIndicator;-><init>()V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->mainhand:Lcom/shatteredpixel/shatteredpixeldungeon/ui/MainHandIndicator;

    .line 197
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->mainhand:Lcom/shatteredpixel/shatteredpixeldungeon/ui/MainHandIndicator;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 199
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossSelectIndicator;

    invoke-direct {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossSelectIndicator;-><init>()V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bossselect:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossSelectIndicator;

    .line 200
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bossselect:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossSelectIndicator;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 202
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/JoinIndicator;

    invoke-direct {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/JoinIndicator;-><init>()V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->joinxxx:Lcom/shatteredpixel/shatteredpixeldungeon/ui/JoinIndicator;

    .line 203
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->joinxxx:Lcom/shatteredpixel/shatteredpixeldungeon/ui/JoinIndicator;

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->add(Lcom/watabou/noosa/Gizmo;)Lcom/watabou/noosa/Gizmo;

    .line 204
    return-void

    .line 100
    :cond_1b8
    new-instance v0, Lcom/watabou/noosa/NinePatch;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    const/4 v2, 0x0

    const/4 v3, 0x0

    const/16 v4, 0x80

    const/16 v5, 0x24

    const/16 v6, 0x55

    const/4 v7, 0x0

    const/16 v8, 0x2d

    const/4 v9, 0x0

    invoke-direct/range {v0 .. v9}, Lcom/watabou/noosa/NinePatch;-><init>(Ljava/lang/Object;IIIIIIII)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    goto/16 :goto_22

    .line 127
    :cond_1cf
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->exit:I

    goto/16 :goto_51

    .line 131
    :cond_1d5
    new-instance v0, Lcom/watabou/noosa/Image;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    const/4 v2, 0x0

    const/16 v3, 0x28

    const/16 v4, 0x32

    const/4 v5, 0x4

    invoke-direct/range {v0 .. v5}, Lcom/watabou/noosa/Image;-><init>(Ljava/lang/Object;IIII)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->rawShielding:Lcom/watabou/noosa/Image;

    goto/16 :goto_6d

    .line 136
    :cond_1e6
    new-instance v0, Lcom/watabou/noosa/Image;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    const/4 v2, 0x0

    const/16 v3, 0x28

    const/16 v4, 0x32

    const/4 v5, 0x4

    invoke-direct/range {v0 .. v5}, Lcom/watabou/noosa/Image;-><init>(Ljava/lang/Object;IIII)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->shieldedHP:Lcom/watabou/noosa/Image;

    goto/16 :goto_8b

    .line 140
    :cond_1f7
    new-instance v0, Lcom/watabou/noosa/Image;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    const/4 v2, 0x0

    const/16 v3, 0x24

    const/16 v4, 0x32

    const/4 v5, 0x4

    invoke-direct/range {v0 .. v5}, Lcom/watabou/noosa/Image;-><init>(Ljava/lang/Object;IIII)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    goto/16 :goto_a2

    .line 144
    :cond_208
    new-instance v0, Lcom/watabou/noosa/Image;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    const/4 v2, 0x0

    const/16 v3, 0x2d

    const/16 v4, 0x31

    const/4 v5, 0x4

    invoke-direct/range {v0 .. v5}, Lcom/watabou/noosa/Image;-><init>(Ljava/lang/Object;IIII)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hg:Lcom/watabou/noosa/Image;

    goto/16 :goto_b8

    .line 166
    :cond_219
    new-instance v0, Lcom/watabou/noosa/Image;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->asset:Ljava/lang/String;

    const/4 v2, 0x0

    const/16 v3, 0x2c

    const/16 v4, 0x10

    const/4 v5, 0x1

    invoke-direct/range {v0 .. v5}, Lcom/watabou/noosa/Image;-><init>(Ljava/lang/Object;IIII)V

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    goto/16 :goto_106
.end method


# virtual methods
.method protected layout()V
    .registers 11

    .prologue
    const/high16 v9, 0x3f000000    # 0.5f

    const v8, 0x3a83126f    # 0.001f

    const/high16 v7, 0x3f800000    # 1.0f

    const/high16 v1, 0x41f00000    # 30.0f

    const/high16 v6, 0x40000000    # 2.0f

    .line 209
    iget-boolean v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->large:Z

    if-eqz v0, :cond_173

    const/high16 v0, 0x421c0000    # 39.0f

    :goto_11
    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->height:F

    .line 211
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    iput v2, v0, Lcom/watabou/noosa/NinePatch;->x:F

    .line 212
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    iput v2, v0, Lcom/watabou/noosa/NinePatch;->y:F

    .line 213
    iget-boolean v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->large:Z

    if-eqz v0, :cond_177

    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    const/high16 v2, 0x43200000    # 160.0f

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    iget v3, v3, Lcom/watabou/noosa/NinePatch;->height:F

    invoke-virtual {v0, v2, v3}, Lcom/watabou/noosa/NinePatch;->size(FF)V

    .line 216
    :goto_2e
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    iget v2, v2, Lcom/watabou/noosa/NinePatch;->x:F

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    iget v3, v3, Lcom/watabou/noosa/Image;->width:F

    div-float/2addr v3, v6

    sub-float/2addr v2, v3

    const/high16 v3, 0x41700000    # 15.0f

    add-float/2addr v2, v3

    iput v2, v0, Lcom/watabou/noosa/Image;->x:F

    .line 217
    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    iget v0, v0, Lcom/watabou/noosa/NinePatch;->y:F

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    iget v3, v3, Lcom/watabou/noosa/Image;->height:F

    div-float/2addr v3, v6

    sub-float v3, v0, v3

    iget-boolean v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->large:Z

    if-eqz v0, :cond_184

    const/16 v0, 0xf

    :goto_52
    int-to-float v0, v0

    add-float/2addr v0, v3

    iput v0, v2, Lcom/watabou/noosa/Image;->y:F

    .line 218
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->align(Lcom/watabou/noosa/Visual;)V

    .line 220
    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->heroInfo:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;

    iget v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    iget v4, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    iget-boolean v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->large:Z

    if-eqz v0, :cond_188

    const/4 v0, 0x0

    :goto_66
    int-to-float v0, v0

    add-float/2addr v4, v0

    iget-boolean v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->large:Z

    if-eqz v0, :cond_18b

    const/high16 v0, 0x42200000    # 40.0f

    :goto_6e
    invoke-virtual {v2, v3, v4, v1, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;->setRect(FFFF)Lcom/watabou/noosa/ui/Component;

    .line 222
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->compass:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    iget v2, v2, Lcom/watabou/noosa/Image;->x:F

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    iget v3, v3, Lcom/watabou/noosa/Image;->width:F

    div-float/2addr v3, v6

    add-float/2addr v2, v3

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->compass:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;

    iget-object v3, v3, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;->origin:Lcom/watabou/utils/PointF;

    iget v3, v3, Lcom/watabou/utils/PointF;->x:F

    sub-float/2addr v2, v3

    iput v2, v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;->x:F

    .line 223
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->compass:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    iget v2, v2, Lcom/watabou/noosa/Image;->y:F

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    iget v3, v3, Lcom/watabou/noosa/Image;->height:F

    div-float/2addr v3, v6

    add-float/2addr v2, v3

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->compass:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;

    iget-object v3, v3, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;->origin:Lcom/watabou/utils/PointF;

    iget v3, v3, Lcom/watabou/utils/PointF;->y:F

    sub-float/2addr v2, v3

    iput v2, v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;->y:F

    .line 224
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->compass:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Compass;

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->align(Lcom/watabou/noosa/Visual;)V

    .line 226
    iget-boolean v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->large:Z

    if-eqz v0, :cond_18e

    .line 227
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    add-float/2addr v2, v1

    iput v2, v0, Lcom/watabou/noosa/Image;->x:F

    .line 228
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    add-float/2addr v2, v1

    iput v2, v0, Lcom/watabou/noosa/Image;->y:F

    .line 230
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->shieldedHP:Lcom/watabou/noosa/Image;

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->rawShielding:Lcom/watabou/noosa/Image;

    iget v4, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    add-float/2addr v4, v1

    iput v4, v3, Lcom/watabou/noosa/Image;->x:F

    iput v4, v2, Lcom/watabou/noosa/Image;->x:F

    iput v4, v0, Lcom/watabou/noosa/Image;->x:F

    .line 231
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->shieldedHP:Lcom/watabou/noosa/Image;

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->rawShielding:Lcom/watabou/noosa/Image;

    iget v4, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    const/high16 v5, 0x41980000    # 19.0f

    add-float/2addr v4, v5

    iput v4, v3, Lcom/watabou/noosa/Image;->y:F

    iput v4, v2, Lcom/watabou/noosa/Image;->y:F

    iput v4, v0, Lcom/watabou/noosa/Image;->y:F

    .line 233
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget v2, v2, Lcom/watabou/noosa/Image;->x:F

    const/high16 v3, 0x42fa0000    # 125.0f

    iget-object v4, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v4}, Lcom/watabou/noosa/BitmapText;->width()F

    move-result v4

    sub-float/2addr v3, v4

    div-float/2addr v3, v6

    add-float/2addr v2, v3

    iput v2, v0, Lcom/watabou/noosa/BitmapText;->x:F

    .line 234
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget v2, v2, Lcom/watabou/noosa/Image;->y:F

    add-float/2addr v2, v7

    iput v2, v0, Lcom/watabou/noosa/BitmapText;->y:F

    .line 235
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->align(Lcom/watabou/noosa/Visual;)V

    .line 237
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hg:Lcom/watabou/noosa/Image;

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    add-float/2addr v1, v2

    iput v1, v0, Lcom/watabou/noosa/Image;->x:F

    .line 238
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hg:Lcom/watabou/noosa/Image;

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    const/high16 v2, 0x41200000    # 10.0f

    add-float/2addr v1, v2

    iput v1, v0, Lcom/watabou/noosa/Image;->y:F

    .line 240
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    const/high16 v2, 0x42a00000    # 80.0f

    add-float/2addr v1, v2

    iput v1, v0, Lcom/watabou/noosa/BitmapText;->x:F

    .line 241
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hg:Lcom/watabou/noosa/Image;

    iget v1, v1, Lcom/watabou/noosa/Image;->y:F

    iput v1, v0, Lcom/watabou/noosa/BitmapText;->y:F

    .line 242
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->align(Lcom/watabou/noosa/Visual;)V

    .line 244
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->expText:Lcom/watabou/noosa/BitmapText;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    iget v1, v1, Lcom/watabou/noosa/Image;->x:F

    const/high16 v2, 0x43000000    # 128.0f

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->expText:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v3}, Lcom/watabou/noosa/BitmapText;->width()F

    move-result v3

    sub-float/2addr v2, v3

    div-float/2addr v2, v6

    add-float/2addr v1, v2

    iput v1, v0, Lcom/watabou/noosa/BitmapText;->x:F

    .line 245
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->expText:Lcom/watabou/noosa/BitmapText;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    iget v1, v1, Lcom/watabou/noosa/Image;->y:F

    iput v1, v0, Lcom/watabou/noosa/BitmapText;->y:F

    .line 246
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->expText:Lcom/watabou/noosa/BitmapText;

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->align(Lcom/watabou/noosa/Visual;)V

    .line 248
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->heroInfoOnBar:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->heroInfo:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;->right()F

    move-result v1

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    const/high16 v3, 0x41980000    # 19.0f

    add-float/2addr v2, v3

    const/high16 v3, 0x43020000    # 130.0f

    const/high16 v4, 0x41a00000    # 20.0f

    invoke-virtual {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;->setRect(FFFF)Lcom/watabou/noosa/ui/Component;

    .line 252
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->busy:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    iget v2, v2, Lcom/watabou/noosa/NinePatch;->width:F

    add-float/2addr v1, v2

    add-float/2addr v1, v7

    iput v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;->x:F

    .line 253
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->busy:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    iget v2, v2, Lcom/watabou/noosa/NinePatch;->height:F

    add-float/2addr v1, v2

    const/high16 v2, 0x41100000    # 9.0f

    sub-float/2addr v1, v2

    iput v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;->y:F

    .line 285
    :goto_167
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->counter:Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->busy:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;->center()Lcom/watabou/utils/PointF;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;->point(Lcom/watabou/utils/PointF;)Lcom/watabou/utils/PointF;

    .line 286
    return-void

    .line 209
    :cond_173
    const/high16 v0, 0x42000000    # 32.0f

    goto/16 :goto_11

    .line 214
    :cond_177
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->width:F

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    iget v3, v3, Lcom/watabou/noosa/NinePatch;->height:F

    invoke-virtual {v0, v2, v3}, Lcom/watabou/noosa/NinePatch;->size(FF)V

    goto/16 :goto_2e

    .line 217
    :cond_184
    const/16 v0, 0x10

    goto/16 :goto_52

    .line 220
    :cond_188
    const/4 v0, 0x1

    goto/16 :goto_66

    :cond_18b
    move v0, v1

    goto/16 :goto_6e

    .line 255
    :cond_18e
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    iput v2, v0, Lcom/watabou/noosa/Image;->x:F

    .line 256
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    iput v2, v0, Lcom/watabou/noosa/Image;->y:F

    .line 259
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->shieldedHP:Lcom/watabou/noosa/Image;

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->rawShielding:Lcom/watabou/noosa/Image;

    iget v4, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    add-float/2addr v4, v1

    iput v4, v3, Lcom/watabou/noosa/Image;->x:F

    iput v4, v2, Lcom/watabou/noosa/Image;->x:F

    iput v4, v0, Lcom/watabou/noosa/Image;->x:F

    .line 260
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->shieldedHP:Lcom/watabou/noosa/Image;

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->rawShielding:Lcom/watabou/noosa/Image;

    iget v4, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    const/high16 v5, 0x40400000    # 3.0f

    add-float/2addr v4, v5

    iput v4, v3, Lcom/watabou/noosa/Image;->y:F

    iput v4, v2, Lcom/watabou/noosa/Image;->y:F

    iput v4, v0, Lcom/watabou/noosa/Image;->y:F

    .line 262
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    iget-object v0, v0, Lcom/watabou/noosa/BitmapText;->scale:Lcom/watabou/utils/PointF;

    invoke-static {v9}, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->align(F)F

    move-result v2

    invoke-virtual {v0, v2}, Lcom/watabou/utils/PointF;->set(F)Lcom/watabou/utils/PointF;

    .line 263
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget v2, v2, Lcom/watabou/noosa/Image;->x:F

    add-float/2addr v2, v7

    iput v2, v0, Lcom/watabou/noosa/BitmapText;->x:F

    .line 264
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget v2, v2, Lcom/watabou/noosa/Image;->y:F

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget v3, v3, Lcom/watabou/noosa/Image;->height:F

    iget-object v4, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v4}, Lcom/watabou/noosa/BitmapText;->baseLine()F

    move-result v4

    iget-object v5, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    iget-object v5, v5, Lcom/watabou/noosa/BitmapText;->scale:Lcom/watabou/utils/PointF;

    iget v5, v5, Lcom/watabou/utils/PointF;->y:F

    add-float/2addr v4, v5

    sub-float/2addr v3, v4

    div-float/2addr v3, v6

    add-float/2addr v2, v3

    iput v2, v0, Lcom/watabou/noosa/BitmapText;->y:F

    .line 265
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    iget v2, v0, Lcom/watabou/noosa/BitmapText;->y:F

    sub-float/2addr v2, v8

    iput v2, v0, Lcom/watabou/noosa/BitmapText;->y:F

    .line 266
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->align(Lcom/watabou/noosa/Visual;)V

    .line 268
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hg:Lcom/watabou/noosa/Image;

    iput v1, v0, Lcom/watabou/noosa/Image;->x:F

    .line 269
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hg:Lcom/watabou/noosa/Image;

    const/high16 v1, 0x41000000    # 8.0f

    iput v1, v0, Lcom/watabou/noosa/Image;->y:F

    .line 271
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    iget-object v0, v0, Lcom/watabou/noosa/BitmapText;->scale:Lcom/watabou/utils/PointF;

    invoke-static {v9}, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->align(F)F

    move-result v1

    invoke-virtual {v0, v1}, Lcom/watabou/utils/PointF;->set(F)Lcom/watabou/utils/PointF;

    .line 272
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hg:Lcom/watabou/noosa/Image;

    iget v1, v1, Lcom/watabou/noosa/Image;->x:F

    add-float/2addr v1, v7

    iput v1, v0, Lcom/watabou/noosa/BitmapText;->x:F

    .line 273
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hg:Lcom/watabou/noosa/Image;

    iget v1, v1, Lcom/watabou/noosa/Image;->y:F

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget v2, v2, Lcom/watabou/noosa/Image;->height:F

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v3}, Lcom/watabou/noosa/BitmapText;->baseLine()F

    move-result v3

    iget-object v4, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    iget-object v4, v4, Lcom/watabou/noosa/BitmapText;->scale:Lcom/watabou/utils/PointF;

    iget v4, v4, Lcom/watabou/utils/PointF;->y:F

    add-float/2addr v3, v4

    sub-float/2addr v2, v3

    div-float/2addr v2, v6

    add-float/2addr v1, v2

    iput v1, v0, Lcom/watabou/noosa/BitmapText;->y:F

    .line 274
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    iget v1, v0, Lcom/watabou/noosa/BitmapText;->y:F

    sub-float/2addr v1, v8

    iput v1, v0, Lcom/watabou/noosa/BitmapText;->y:F

    .line 275
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->align(Lcom/watabou/noosa/Visual;)V

    .line 277
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->heroInfoOnBar:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->heroInfo:Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;->right()F

    move-result v1

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    const/high16 v3, 0x42480000    # 50.0f

    const/high16 v4, 0x41100000    # 9.0f

    invoke-virtual {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;->setRect(FFFF)Lcom/watabou/noosa/ui/Component;

    .line 279
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->buffs:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BuffIndicator;

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    const/high16 v2, 0x41f80000    # 31.0f

    add-float/2addr v1, v2

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    const/high16 v3, 0x41400000    # 12.0f

    add-float/2addr v2, v3

    invoke-virtual {v0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BuffIndicator;->setPos(FF)Lcom/watabou/noosa/ui/Component;

    .line 281
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->busy:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    add-float/2addr v1, v7

    iput v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;->x:F

    .line 282
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->busy:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    const/high16 v2, 0x42040000    # 33.0f

    add-float/2addr v1, v2

    iput v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BusyIndicator;->y:F

    goto/16 :goto_167
.end method

.method public showStarParticles()V
    .registers 4

    .prologue
    .line 403
    const-class v1, Lcom/watabou/noosa/particles/Emitter;

    invoke-virtual {p0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->recycle(Ljava/lang/Class;)Lcom/watabou/noosa/Gizmo;

    move-result-object v0

    check-cast v0, Lcom/watabou/noosa/particles/Emitter;

    .line 404
    .local v0, "emitter":Lcom/watabou/noosa/particles/Emitter;
    invoke-virtual {v0}, Lcom/watabou/noosa/particles/Emitter;->revive()V

    .line 405
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    invoke-virtual {v1}, Lcom/watabou/noosa/Image;->center()Lcom/watabou/utils/PointF;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/watabou/noosa/particles/Emitter;->pos(Lcom/watabou/utils/PointF;)V

    .line 406
    const/4 v1, 0x1

    invoke-static {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/effects/Speck;->factory(I)Lcom/watabou/noosa/particles/Emitter$Factory;

    move-result-object v1

    const/16 v2, 0xc

    invoke-virtual {v0, v1, v2}, Lcom/watabou/noosa/particles/Emitter;->burst(Lcom/watabou/noosa/particles/Emitter$Factory;I)V

    .line 407
    return-void
.end method

.method public update()V
    .registers 15

    .prologue
    .line 292
    invoke-super {p0}, Lcom/watabou/noosa/ui/Component;->update()V

    .line 294
    const/16 v4, 0x1c2

    .line 295
    .local v4, "maxHunger":I
    sget-object v7, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    iget v0, v7, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->HP:I

    .line 296
    .local v0, "health":I
    sget-object v7, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    invoke-virtual {v7}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->shielding()I

    move-result v5

    .line 297
    .local v5, "shield":I
    sget-object v7, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    iget v3, v7, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->HT:I

    .line 299
    .local v3, "max":I
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/SPDSettings;->ClassUI()Z

    move-result v7

    if-eqz v7, :cond_1e9

    .line 300
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    const-string v8, "interfaces/status_pane_normal.png"

    invoke-static {v8}, Lcom/watabou/gltextures/TextureCache;->get(Ljava/lang/Object;)Lcom/watabou/gltextures/SmartTexture;

    move-result-object v8

    iput-object v8, v7, Lcom/watabou/noosa/NinePatch;->texture:Lcom/watabou/gltextures/SmartTexture;

    .line 305
    :goto_23
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/SPDSettings;->ClassPage()Z

    move-result v7

    if-eqz v7, :cond_1f5

    .line 306
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->page:Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicator;

    const/4 v8, 0x0

    const/high16 v9, 0x42200000    # 40.0f

    invoke-virtual {v7, v8, v9}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicator;->setPos(FF)Lcom/watabou/noosa/ui/Component;

    .line 307
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->pageb:Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicatorB;

    const/4 v8, 0x0

    const v9, 0x461c3c00    # 9999.0f

    invoke-virtual {v7, v8, v9}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicatorB;->setPos(FF)Lcom/watabou/noosa/ui/Component;

    .line 308
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->mainhand:Lcom/shatteredpixel/shatteredpixeldungeon/ui/MainHandIndicator;

    const/4 v8, 0x0

    const/high16 v9, 0x424c0000    # 51.0f

    invoke-virtual {v7, v8, v9}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/MainHandIndicator;->setPos(FF)Lcom/watabou/noosa/ui/Component;

    .line 309
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->joinxxx:Lcom/shatteredpixel/shatteredpixeldungeon/ui/JoinIndicator;

    const/4 v8, 0x0

    const/high16 v9, 0x429c0000    # 78.0f

    invoke-virtual {v7, v8, v9}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/JoinIndicator;->setPos(FF)Lcom/watabou/noosa/ui/Component;

    .line 310
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bossselect:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossSelectIndicator;

    const/4 v8, 0x0

    const/high16 v9, 0x42d00000    # 104.0f

    invoke-virtual {v7, v8, v9}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossSelectIndicator;->setPos(FF)Lcom/watabou/noosa/ui/Component;

    .line 319
    :goto_52
    sget-object v7, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    invoke-virtual {v7}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->isAlive()Z

    move-result v7

    if-nez v7, :cond_223

    .line 320
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    const/4 v8, 0x0

    const/high16 v9, 0x3f000000    # 0.5f

    invoke-virtual {v7, v8, v9}, Lcom/watabou/noosa/Image;->tint(IF)V

    .line 332
    :goto_62
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget-object v7, v7, Lcom/watabou/noosa/Image;->scale:Lcom/watabou/utils/PointF;

    const/4 v8, 0x0

    sub-int v9, v0, v5

    int-to-float v9, v9

    int-to-float v10, v3

    div-float/2addr v9, v10

    invoke-static {v8, v9}, Ljava/lang/Math;->max(FF)F

    move-result v8

    iput v8, v7, Lcom/watabou/utils/PointF;->x:F

    .line 333
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->shieldedHP:Lcom/watabou/noosa/Image;

    iget-object v7, v7, Lcom/watabou/noosa/Image;->scale:Lcom/watabou/utils/PointF;

    int-to-float v8, v0

    int-to-float v9, v3

    div-float/2addr v8, v9

    iput v8, v7, Lcom/watabou/utils/PointF;->x:F

    .line 335
    if-le v5, v0, :cond_28d

    .line 336
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->rawShielding:Lcom/watabou/noosa/Image;

    iget-object v7, v7, Lcom/watabou/noosa/Image;->scale:Lcom/watabou/utils/PointF;

    int-to-float v8, v5

    int-to-float v9, v3

    div-float/2addr v8, v9

    iput v8, v7, Lcom/watabou/utils/PointF;->x:F

    .line 341
    :goto_86
    if-gtz v5, :cond_296

    .line 342
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v8, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v8

    const-string v9, "/"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Lcom/watabou/noosa/BitmapText;->text(Ljava/lang/String;)V

    .line 347
    :goto_a4
    sget-object v7, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    const-class v8, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Hunger;

    invoke-virtual {v7, v8}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->buff(Ljava/lang/Class;)Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Buff;

    move-result-object v2

    check-cast v2, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Hunger;

    .line 348
    .local v2, "hungerBuff":Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Hunger;
    if-eqz v2, :cond_2be

    .line 349
    const/4 v7, 0x0

    invoke-virtual {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Hunger;->hunger()I

    move-result v8

    sub-int v8, v4, v8

    invoke-static {v7, v8}, Ljava/lang/Math;->max(II)I

    move-result v1

    .line 350
    .local v1, "hunger":I
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hg:Lcom/watabou/noosa/Image;

    iget-object v7, v7, Lcom/watabou/noosa/Image;->scale:Lcom/watabou/utils/PointF;

    int-to-float v8, v1

    int-to-float v9, v4

    div-float/2addr v8, v9

    iput v8, v7, Lcom/watabou/utils/PointF;->x:F

    .line 351
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hgText:Lcom/watabou/noosa/BitmapText;

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v8, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v8

    const-string v9, "/"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Lcom/watabou/noosa/BitmapText;->text(Ljava/lang/String;)V

    .line 357
    .end local v1    # "hunger":I
    :cond_e0
    :goto_e0
    iget-boolean v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->large:Z

    if-eqz v7, :cond_2d0

    .line 358
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    iget-object v7, v7, Lcom/watabou/noosa/Image;->scale:Lcom/watabou/utils/PointF;

    const/high16 v8, 0x43000000    # 128.0f

    iget-object v9, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    iget v9, v9, Lcom/watabou/noosa/Image;->width:F

    div-float/2addr v8, v9

    sget-object v9, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    iget v9, v9, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->exp:I

    int-to-float v9, v9

    mul-float/2addr v8, v9

    sget-object v9, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    invoke-virtual {v9}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->maxExp()I

    move-result v9

    int-to-float v9, v9

    div-float/2addr v8, v9

    iput v8, v7, Lcom/watabou/utils/PointF;->x:F

    .line 360
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v7}, Lcom/watabou/noosa/BitmapText;->measure()V

    .line 361
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    iget-object v8, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget v8, v8, Lcom/watabou/noosa/Image;->x:F

    const/high16 v9, 0x43000000    # 128.0f

    iget-object v10, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v10}, Lcom/watabou/noosa/BitmapText;->width()F

    move-result v10

    sub-float/2addr v9, v10

    const/high16 v10, 0x40000000    # 2.0f

    div-float/2addr v9, v10

    add-float/2addr v8, v9

    iput v8, v7, Lcom/watabou/noosa/BitmapText;->x:F

    .line 363
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->expText:Lcom/watabou/noosa/BitmapText;

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v9, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    iget v9, v9, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->exp:I

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v8

    const-string v9, "/"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    sget-object v9, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    invoke-virtual {v9}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->maxExp()I

    move-result v9

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Lcom/watabou/noosa/BitmapText;->text(Ljava/lang/String;)V

    .line 364
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->expText:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v7}, Lcom/watabou/noosa/BitmapText;->measure()V

    .line 365
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->expText:Lcom/watabou/noosa/BitmapText;

    iget-object v8, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hp:Lcom/watabou/noosa/Image;

    iget v8, v8, Lcom/watabou/noosa/Image;->x:F

    const/high16 v9, 0x43000000    # 128.0f

    iget-object v10, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->expText:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v10}, Lcom/watabou/noosa/BitmapText;->width()F

    move-result v10

    sub-float/2addr v9, v10

    const/high16 v10, 0x40000000    # 2.0f

    div-float/2addr v9, v10

    add-float/2addr v8, v9

    iput v8, v7, Lcom/watabou/noosa/BitmapText;->x:F

    .line 371
    :goto_159
    sget-object v7, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    iget v7, v7, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->lvl:I

    iget v8, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->lastLvl:I

    if-eq v7, v8, :cond_1bd

    .line 373
    iget v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->lastLvl:I

    const/4 v8, -0x1

    if-eq v7, v8, :cond_169

    .line 374
    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->showStarParticles()V

    .line 377
    :cond_169
    sget-object v7, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    iget v7, v7, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->lvl:I

    iput v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->lastLvl:I

    .line 379
    iget-boolean v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->large:Z

    if-eqz v7, :cond_2ed

    .line 380
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    const-string v9, "lv. "

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    iget v9, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->lastLvl:I

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Lcom/watabou/noosa/BitmapText;->text(Ljava/lang/String;)V

    .line 381
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v7}, Lcom/watabou/noosa/BitmapText;->measure()V

    .line 382
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    iget v8, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    const/high16 v9, 0x41f00000    # 30.0f

    iget-object v10, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v10}, Lcom/watabou/noosa/BitmapText;->width()F

    move-result v10

    sub-float/2addr v9, v10

    const/high16 v10, 0x40000000    # 2.0f

    div-float/2addr v9, v10

    add-float/2addr v8, v9

    iput v8, v7, Lcom/watabou/noosa/BitmapText;->x:F

    .line 383
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    iget v8, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    const/high16 v9, 0x42040000    # 33.0f

    add-float/2addr v8, v9

    iget-object v9, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v9}, Lcom/watabou/noosa/BitmapText;->baseLine()F

    move-result v9

    const/high16 v10, 0x40000000    # 2.0f

    div-float/2addr v9, v10

    sub-float/2addr v8, v9

    iput v8, v7, Lcom/watabou/noosa/BitmapText;->y:F

    .line 390
    :goto_1b8
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    invoke-static {v7}, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/PixelScene;->align(Lcom/watabou/noosa/Visual;)V

    .line 393
    :cond_1bd
    sget-object v7, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    invoke-virtual {v7}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->tier()I

    move-result v6

    .line 394
    .local v6, "tier":I
    iget v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->lastTier:I

    if-eq v6, v7, :cond_1d6

    .line 395
    iput v6, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->lastTier:I

    .line 396
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    sget-object v8, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    iget-object v8, v8, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->heroClass:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    invoke-static {v8, v6}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/HeroSprite;->avatar(Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;I)Lcom/watabou/noosa/Image;

    move-result-object v8

    invoke-virtual {v7, v8}, Lcom/watabou/noosa/Image;->copy(Lcom/watabou/noosa/Image;)V

    .line 399
    :cond_1d6
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->counter:Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;

    const/high16 v8, 0x3f800000    # 1.0f

    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Actor;->now()F

    move-result v9

    const/high16 v10, 0x3f800000    # 1.0f

    rem-float/2addr v9, v10

    sub-float/2addr v8, v9

    const/high16 v9, 0x3f800000    # 1.0f

    rem-float/2addr v8, v9

    invoke-virtual {v7, v8}, Lcom/shatteredpixel/shatteredpixeldungeon/effects/CircleArc;->setSweep(F)V

    .line 400
    return-void

    .line 302
    .end local v2    # "hungerBuff":Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Hunger;
    .end local v6    # "tier":I
    :cond_1e9
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bg:Lcom/watabou/noosa/NinePatch;

    const-string v8, "interfaces/status_pane.png"

    invoke-static {v8}, Lcom/watabou/gltextures/TextureCache;->get(Ljava/lang/Object;)Lcom/watabou/gltextures/SmartTexture;

    move-result-object v8

    iput-object v8, v7, Lcom/watabou/noosa/NinePatch;->texture:Lcom/watabou/gltextures/SmartTexture;

    goto/16 :goto_23

    .line 312
    :cond_1f5
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->page:Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicator;

    const/4 v8, 0x0

    const v9, 0x461c3c00    # 9999.0f

    invoke-virtual {v7, v8, v9}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicator;->setPos(FF)Lcom/watabou/noosa/ui/Component;

    .line 313
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->pageb:Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicatorB;

    const/4 v8, 0x0

    const/high16 v9, 0x42200000    # 40.0f

    invoke-virtual {v7, v8, v9}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/PageIndicatorB;->setPos(FF)Lcom/watabou/noosa/ui/Component;

    .line 314
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->mainhand:Lcom/shatteredpixel/shatteredpixeldungeon/ui/MainHandIndicator;

    const/4 v8, 0x0

    const v9, 0x461c3c00    # 9999.0f

    invoke-virtual {v7, v8, v9}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/MainHandIndicator;->setPos(FF)Lcom/watabou/noosa/ui/Component;

    .line 315
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->joinxxx:Lcom/shatteredpixel/shatteredpixeldungeon/ui/JoinIndicator;

    const/4 v8, 0x0

    const v9, 0x461c3c00    # 9999.0f

    invoke-virtual {v7, v8, v9}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/JoinIndicator;->setPos(FF)Lcom/watabou/noosa/ui/Component;

    .line 316
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->bossselect:Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossSelectIndicator;

    const/4 v8, 0x0

    const v9, 0x461c3c00    # 9999.0f

    invoke-virtual {v7, v8, v9}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossSelectIndicator;->setPos(FF)Lcom/watabou/noosa/ui/Component;

    goto/16 :goto_52

    .line 321
    :cond_223
    int-to-float v7, v0

    int-to-float v8, v3

    div-float/2addr v7, v8

    const v8, 0x3e99999a    # 0.3f

    cmpg-float v7, v7, v8

    if-gez v7, :cond_257

    .line 322
    iget v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->warning:F

    sget v8, Lcom/watabou/noosa/Game;->elapsed:F

    const/high16 v9, 0x40a00000    # 5.0f

    mul-float/2addr v8, v9

    const v9, 0x3ecccccd    # 0.4f

    int-to-float v10, v0

    int-to-float v11, v3

    div-float/2addr v10, v11

    sub-float/2addr v9, v10

    mul-float/2addr v8, v9

    add-float/2addr v7, v8

    iput v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->warning:F

    .line 323
    iget v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->warning:F

    const/high16 v8, 0x3f800000    # 1.0f

    rem-float/2addr v7, v8

    iput v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->warning:F

    .line 324
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    iget v8, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->warning:F

    sget-object v9, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->warningColors:[I

    invoke-static {v8, v9}, Lcom/watabou/utils/ColorMath;->interpolate(F[I)I

    move-result v8

    const/high16 v9, 0x3f000000    # 0.5f

    invoke-virtual {v7, v8, v9}, Lcom/watabou/noosa/Image;->tint(IF)V

    goto/16 :goto_62

    .line 325
    :cond_257
    sget v7, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->talentBlink:F

    const v8, 0x3ea8f5c3    # 0.33f

    cmpl-float v7, v7, v8

    if-lez v7, :cond_286

    .line 326
    sget v7, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->talentBlink:F

    sget v8, Lcom/watabou/noosa/Game;->elapsed:F

    sub-float/2addr v7, v8

    sput v7, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->talentBlink:F

    .line 327
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    const/high16 v8, 0x3f800000    # 1.0f

    const/high16 v9, 0x3f800000    # 1.0f

    const/4 v10, 0x0

    sget v11, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->talentBlink:F

    const v12, 0x4096cbe4

    mul-float/2addr v11, v12

    float-to-double v12, v11

    invoke-static {v12, v13}, Ljava/lang/Math;->cos(D)D

    move-result-wide v12

    invoke-static {v12, v13}, Ljava/lang/Math;->abs(D)D

    move-result-wide v12

    double-to-float v11, v12

    const/high16 v12, 0x40000000    # 2.0f

    div-float/2addr v11, v12

    invoke-virtual {v7, v8, v9, v10, v11}, Lcom/watabou/noosa/Image;->tint(FFFF)V

    goto/16 :goto_62

    .line 329
    :cond_286
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->avatar:Lcom/watabou/noosa/Image;

    invoke-virtual {v7}, Lcom/watabou/noosa/Image;->resetColor()V

    goto/16 :goto_62

    .line 338
    :cond_28d
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->rawShielding:Lcom/watabou/noosa/Image;

    iget-object v7, v7, Lcom/watabou/noosa/Image;->scale:Lcom/watabou/utils/PointF;

    const/4 v8, 0x0

    iput v8, v7, Lcom/watabou/utils/PointF;->x:F

    goto/16 :goto_86

    .line 344
    :cond_296
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hpText:Lcom/watabou/noosa/BitmapText;

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v8, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v8

    const-string v9, "+"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v8

    const-string v9, "/"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Lcom/watabou/noosa/BitmapText;->text(Ljava/lang/String;)V

    goto/16 :goto_a4

    .line 353
    .restart local v2    # "hungerBuff":Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Hunger;
    :cond_2be
    sget-object v7, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    invoke-virtual {v7}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->isAlive()Z

    move-result v7

    if-eqz v7, :cond_e0

    .line 354
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->hg:Lcom/watabou/noosa/Image;

    iget-object v7, v7, Lcom/watabou/noosa/Image;->scale:Lcom/watabou/utils/PointF;

    const/high16 v8, 0x3f800000    # 1.0f

    iput v8, v7, Lcom/watabou/utils/PointF;->x:F

    goto/16 :goto_e0

    .line 368
    :cond_2d0
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    iget-object v7, v7, Lcom/watabou/noosa/Image;->scale:Lcom/watabou/utils/PointF;

    iget v8, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->width:F

    iget-object v9, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->exp:Lcom/watabou/noosa/Image;

    iget v9, v9, Lcom/watabou/noosa/Image;->width:F

    div-float/2addr v8, v9

    sget-object v9, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    iget v9, v9, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->exp:I

    int-to-float v9, v9

    mul-float/2addr v8, v9

    sget-object v9, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    invoke-virtual {v9}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->maxExp()I

    move-result v9

    int-to-float v9, v9

    div-float/2addr v8, v9

    iput v8, v7, Lcom/watabou/utils/PointF;->x:F

    goto/16 :goto_159

    .line 385
    :cond_2ed
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    iget v8, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->lastLvl:I

    invoke-static {v8}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v7, v8}, Lcom/watabou/noosa/BitmapText;->text(Ljava/lang/String;)V

    .line 386
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v7}, Lcom/watabou/noosa/BitmapText;->measure()V

    .line 387
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    iget v8, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->x:F

    const/high16 v9, 0x41dc0000    # 27.5f

    add-float/2addr v8, v9

    iget-object v9, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v9}, Lcom/watabou/noosa/BitmapText;->width()F

    move-result v9

    const/high16 v10, 0x40000000    # 2.0f

    div-float/2addr v9, v10

    sub-float/2addr v8, v9

    iput v8, v7, Lcom/watabou/noosa/BitmapText;->x:F

    .line 388
    iget-object v7, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    iget v8, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->y:F

    const/high16 v9, 0x41e00000    # 28.0f

    add-float/2addr v8, v9

    iget-object v9, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;->level:Lcom/watabou/noosa/BitmapText;

    invoke-virtual {v9}, Lcom/watabou/noosa/BitmapText;->baseLine()F

    move-result v9

    const/high16 v10, 0x40000000    # 2.0f

    div-float/2addr v9, v10

    sub-float/2addr v8, v9

    iput v8, v7, Lcom/watabou/noosa/BitmapText;->y:F

    goto/16 :goto_1b8
.end method
