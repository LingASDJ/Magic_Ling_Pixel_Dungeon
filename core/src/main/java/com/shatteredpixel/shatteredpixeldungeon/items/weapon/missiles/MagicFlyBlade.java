package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spellsoword.MagicPower;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class MagicFlyBlade extends MissileWeapon {
    boolean circleBackhit = false;

    {
        tier = 1;
        baseUses = Float.MAX_VALUE;
        image = ItemSpriteSheet.MAGIC_FLY_BLADE;
    }

    @Override
    protected void onThrow( int cell ) {

        MagicPower magicPower = Dungeon.hero.buff(MagicPower.class);
        if(magicPower != null){
            if(magicPower.getMagic() >= 1){
                Char enemy = Actor.findChar( cell );
                if (enemy == null || enemy == curUser) {
                    parent = null;
                    Splash.at( cell, 0xCC99FFFF, 1 );
                } else {
                    if (!curUser.shoot( enemy, this )) {
                        Splash.at(cell, 0xCC99FFFF, 1);
                    }
                }
                Buff.append(Dungeon.hero, MagicPowerReBack.class).setup(this, cell, Dungeon.hero.pos, Dungeon.depth, Dungeon.branch);
                switch (Dungeon.hero.pointsInTalent(Talent.GUARDIAN_BLADE)){
                    case 1:
                        if(Random.Float()<=0.35f){
                            //EMPTY METHOD
                        } else {
                            magicPower.usedMagic(1);
                        }
                    break;
                    case 2:
                        if(Random.Float()<=0.70f){
                            //EMPTY METHOD
                        } else {
                            magicPower.usedMagic(1);
                        }
                    break;
                    default:
                        magicPower.usedMagic(1);
                        break;
                }

            } else {
                super.onThrow(cell);
            }
        } else {
            super.onThrow(cell);
        }

    }

    @Override
    public int level() {
        int level = Dungeon.hero == null ? 0 : Dungeon.hero.lvl/5;
        if (curseInfusionBonus) level += 1 + level/6;
        return level;
    }

    @Override
    public int buffedLvl() {
        //level isn't affected by buffs/debuffs
        return level();
    }


    @Override
    public String info() {

        String info = Messages.get(this,"desc");

        info += "\n\n" + Messages.get( this, "stats",
                Math.round(augment.damageFactor(min())),
                Math.round(augment.damageFactor(max())),
                STRReq());

        if (enchantment != null && (cursedKnown || !enchantment.curse())){
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }

        if (cursed && isEquipped( Dungeon.hero )) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown){
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }

        info += "\n\n" + Messages.get(this, "distance") + "\n\n";

        if (Dungeon.hero != null) {
            if (STRReq() > Dungeon.hero.STR()) {
                info += Messages.get(Weapon.class, "too_heavy");
            } else if (Dungeon.hero.STR() > STRReq()) {
                info += Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
            }
        }

        return info;
    }

    @Override
    public int min(int lvl) {
        return 1 + lvl;
    }

    @Override
    public int max(int lvl) {
        return 5 + lvl;
    }

    public static class MagicPowerReBack extends Buff {

        {
            revivePersists = true;
        }

        public MagicFlyBlade boomerang;
        private int thrownPos;
        private int returnPos;
        private int returnDepth;
        private int returnBranch;

        private int left;

        public void setup( MagicFlyBlade boomerang, int thrownPos, int returnPos, int returnDepth, int returnBranch){
            this.boomerang = boomerang;
            this.thrownPos = thrownPos;
            this.returnPos = returnPos;
            this.returnDepth = returnDepth;
            this.returnBranch = returnBranch;
            left = 3;
        }

        public int returnPos(){
            return returnPos;
        }

        public MissileWeapon cancel(){
            detach();
            return boomerang;
        }

        @Override
        public boolean act() {
            if (returnDepth == Dungeon.depth && returnBranch == Dungeon.branch){
                left--;
                if (left <= 0){
                    final Char returnTarget = Actor.findChar(returnPos);
                    final Char target = this.target;
                    MissileSprite visual = ((MissileSprite) Dungeon.hero.sprite.parent.recycle(MissileSprite.class));
                    visual.reset( thrownPos,
                            returnPos,
                            boomerang,
                            new Callback() {
                                @Override
                                public void call() {
                                    if (returnTarget == target){
                                        if (target instanceof Hero && boomerang.doPickUp((Hero) target)) {
                                            //grabbing the boomerang takes no time
                                            ((Hero) target).spend(-TIME_TO_PICK_UP);
                                        } else {
                                            Dungeon.level.drop(boomerang, returnPos).sprite.drop();
                                        }

                                    } else if (returnTarget != null){
                                        boomerang.circleBackhit = true;
                                        if (((Hero)target).shoot( returnTarget, boomerang )) {
                                            boomerang.decrementDurability();
                                        }
                                        if (boomerang.durability > 0) {
                                            Dungeon.level.drop(boomerang, returnPos).sprite.drop();
                                        }

                                    } else {
                                        Dungeon.level.drop(boomerang, returnPos).sprite.drop();
                                    }
                                    MagicPowerReBack.this.next();
                                }
                            });
                    visual.alpha(0f);
                    float duration = Dungeon.level.trueDistance(thrownPos, returnPos) / 20f;
                    target.sprite.parent.add(new AlphaTweener(visual, 1f, duration));
                    detach();
                    return false;
                }
            }
            spend( 0f );
            return true;
        }

        private static final String BOOMERANG = "boomerang";
        private static final String THROWN_POS = "thrown_pos";
        private static final String RETURN_POS = "return_pos";
        private static final String RETURN_DEPTH = "return_depth";
        private static final String RETURN_BRANCH = "return_branch";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(BOOMERANG, boomerang);
            bundle.put(THROWN_POS, thrownPos);
            bundle.put(RETURN_POS, returnPos);
            bundle.put(RETURN_DEPTH, returnDepth);
            bundle.put(RETURN_BRANCH, returnBranch);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            boomerang = (MagicFlyBlade) bundle.get(BOOMERANG);
            thrownPos = bundle.getInt(THROWN_POS);
            returnPos = bundle.getInt(RETURN_POS);
            returnDepth = bundle.getInt(RETURN_DEPTH);
            returnBranch = bundle.contains(RETURN_BRANCH) ? bundle.getInt(RETURN_BRANCH) : 0;
        }
    }

}
