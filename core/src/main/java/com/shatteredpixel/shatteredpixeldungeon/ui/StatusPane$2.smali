.class Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane$2;
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
    .line 156
    iput-object p1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane$2;->this$0:Lcom/shatteredpixel/shatteredpixeldungeon/ui/StatusPane;

    invoke-direct {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/Button;-><init>()V

    return-void
.end method


# virtual methods
.method protected onClick()V
    .registers 4

    .prologue
    .line 159
    sget-object v0, Lcom/watabou/noosa/Camera;->main:Lcom/watabou/noosa/Camera;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    iget-object v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;->center()Lcom/watabou/utils/PointF;

    move-result-object v1

    const/high16 v2, 0x40a00000    # 5.0f

    invoke-virtual {v0, v1, v2}, Lcom/watabou/noosa/Camera;->panTo(Lcom/watabou/utils/PointF;F)V

    .line 160
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/windows/WndHero;

    invoke-direct {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/windows/WndHero;-><init>()V

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/scenes/GameScene;->show(Lcom/shatteredpixel/shatteredpixeldungeon/ui/Window;)V

    .line 161
    return-void
.end method
