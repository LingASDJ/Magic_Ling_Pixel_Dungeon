package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WhiteBlastSword extends MeleeWeapon implements ActionIndicator.Action {


    private int attack_Teleology;

    {
        image = ItemSpriteSheet.WHITE_BAST;
        tier = 5;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        attack_Teleology++;
        if(attack_Teleology >= 14 - level()/5){
            ActionIndicator.setAction(this);
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    public int STRReq(int lvl) {
        int req = (7 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    @Override
    public int min(int lvl) {
        return 7 + lvl * 3;
    }
    @Override
    public int max(int lvl) {
        return 20 + lvl * 7;
    }

    private static final String INTERVAL    = "acs";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, attack_Teleology );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        attack_Teleology = bundle.getInt( INTERVAL );
    }

    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.WEAPON_SWAP;
    }

    @Override
    public int indicatorColor() {
        return Window.SKYBULE_COLOR;
    }

    private static String[] TXT_RANDOM = {
            Messages.get(WhiteBlastSword.class,"roll1"),
            Messages.get(WhiteBlastSword.class,"roll2"),
            Messages.get(WhiteBlastSword.class,"roll3"),
    };

    @Override
    public void doAction() {
        int poisonLevel;
        if(hero != null){
            poisonLevel = 2 + level();
        } else {
            poisonLevel = 2;
        }
        if (attack_Teleology >= 14 - level()/5) {
            GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.WHITE_BAST),
                    Messages.titleCase(Messages.get(this, "action_name")),
                    Messages.get(this, "quest_start_prompt",poisonLevel),
                    Messages.get(this, "open_attack"),
                    Messages.get(this, "not")) {
                @Override
                protected void onSelect(int index) {
                    if (index == 0) {
                        whiteBlast_Sword();
                        hero.sprite.showStatus(CharSprite.NEGATIVE,TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
                    }
                }
            });
        }
    }

    private void whiteBlast_Sword() {
        ActionIndicator.clearAction(this);
        for (int i : PathFinder.NEIGHBOURS13) {
            int targetingPos = hero.pos + i;
            if (!Dungeon.level.solid[targetingPos]) {
                CellEmitter.get(targetingPos).burst(ElmoParticle.FACTORY, 5);
                GameScene.add(Blob.seed(targetingPos, 1, LastBlobs.class));
            }
        }
        hero.spend( Actor.TICK );
        hero.busy();
        hero.sprite.operate( curUser.pos );
        Sample.INSTANCE.play( Assets.Sounds.HIT_SLASH );
        BuffIndicator.refreshHero();
        attack_Teleology = 0;
    }

    public static class LastBlobs extends Blob implements Hero.Doom {
        private static ArrayList<Class> affectedBlobs;
        {
            affectedBlobs = new ArrayList<>(new BlobImmunity().immunities());
        }

        @Override
        public String tileDesc() {
            return "";
        }
        @Override
        protected void evolve() {
            super.evolve();

            int damage = (int) (hero.damageRoll()*1.2f);

            Char ch;
            int cell;

            ArrayList<Blob> blobs = new ArrayList<>();
            for (Class c : affectedBlobs){
                Blob b = Dungeon.level.blobs.get(c);
                if (b != null && b.volume > 0){
                    blobs.add(b);
                }
            }

            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null && ch != hero) {
                        if (!ch.isImmune(this.getClass())) {
                            ch.damage(damage, this);
                            Buff.affect( ch, Vertigo.class,8f);
                            int poisonLevel = 2 + (hero.belongings.weapon != null ? hero.belongings.weapon.level() : 4);
                            Buff.affect( ch, Poison.class ).set(poisonLevel);
                            for (Blob blob : blobs) {
                                blob.clear(i);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            emitter.pour( Speck.factory( Speck.DISCOVER ), 0.4f );
        }

        @Override
        public void onDeath() {
        }
    }
}
