.class Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane$1;
.super Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;
.source "StatusPane.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;-><init>(Z)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;


# direct methods
.method constructor <init>(Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;)V
    .registers 2
    .param p1, "this$0"    # Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;

    .prologue
    .line 103
    iput-object p1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane$1;->this$0:Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;

    invoke-direct {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;-><init>()V

    return-void
.end method


# virtual methods
.method protected hoverText()Ljava/lang/String;
    .registers 4

    .prologue
    .line 117
    const-class v0, Lcom/shatteredpixel/shatteredpixeldungeon/windows/WndKeyBindings;

    const-string v1, "hero_info"

    const/4 v2, 0x0

    new-array v2, v2, [Ljava/lang/Object;

    invoke-static {v0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->titleCase(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    return-object v0
.end method

.method public keyAction()Lcom/watabou/input/GameAction;
    .registers 2

    .prologue
    .line 112
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/SPDAction;->HERO_INFO:Lcom/watabou/input/GameAction;

    return-object v0
.end method

.method protected onClick()V
    .registers 4

    .prologue
    .line 106
    sget-object v0, Lcom/watabou/noosa/Camera;->main:Lcom/watabou/noosa/Camera;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    iget-object v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;->center()Lcom/watabou/utils/PointF;

    move-result-object v1

    const/high16 v2, 0x40a00000    # 5.0f

    invoke-virtual {v0, v1, v2}, Lcom/watabou/noosa/Camera;->panTo(Lcom/watabou/utils/PointF;F)V

    .line 107
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/windows/WndHero;

    invoke-direct {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/windows/WndHero;-><init>()V

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/GameScene;->show(Lcom/shatteredpixel/shatteredpixeldungeon/ui/Window;)V

    .line 108
    return-void
.end method
