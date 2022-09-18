.class public Lcom/shatteredpixel/shatteredpixeldungeon/items/quest/GoldBAo;
.super Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;
.source "GoldBAo.java"


# direct methods
.method public constructor <init>()V
    .registers 2

    .prologue
    .line 28
    invoke-direct {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;-><init>()V

    .line 31
    sget v0, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/ItemSpriteSheet;->DG21:I

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/quest/GoldBAo;->image:I

    .line 32
    return-void
.end method


# virtual methods
.method public isIdentified()Z
    .registers 2

    .prologue
    .line 41
    const/4 v0, 0x1

    return v0
.end method

.method public isUpgradable()Z
    .registers 2

    .prologue
    .line 36
    const/4 v0, 0x0

    return v0
.end method

.method public value()I
    .registers 4

    .prologue
    .line 45
    iget v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/quest/GoldBAo;->quantity:I

    const/16 v1, 0x9c4

    const/16 v2, 0x157c

    invoke-static {v1, v2}, Lcom/watabou/utils/Random;->Int(II)I

    move-result v1

    mul-int/2addr v0, v1

    return v0
.end method
