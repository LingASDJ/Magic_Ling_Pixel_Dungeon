package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Annoying;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Dazzling;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Displacing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Explosive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Friendly;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Polarized;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Sacrificial;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Wayward;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blooming;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Chilling;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Corrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Crushing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.HaloBlazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.TimeReset;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.WndTextNumberInput;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CustomWeapon extends MeleeWeapon {
    {
        image = ItemSpriteSheet.WEAPON_HOLDER;
        unique = true;
        bones = false;
        adjustStatus();
        defaultAction = AC_SETTING;
    }

    //basic modifiers
    private int s_tier = 1;
    private int s_str_req = 10;
    private boolean use_default_strength = true;
    private boolean use_default_base = true;
    private boolean use_default_scaling = true;
    private int base_min = 1;
    private int base_max = 10;
    private float scaling_min = 1f;
    private float scaling_max = 2f;
    private float s_accuracy = 1f;
    private float s_delay = 1f;
    private boolean s_delay_swap = false;
    private int s_reach = 1;

    private long enchOn = 0L;
    public static ArrayList<Enchantment> enchList = new ArrayList<>();
    public static LinkedHashMap<Class<? extends Enchantment> , Integer> enchPrio = new LinkedHashMap<>();
    static{
        enchPrio.put(Blazing.class,1);
        enchPrio.put(Blocking.class, 0);
        enchPrio.put(Blooming.class, 0);
        enchPrio.put(Chilling.class, 1);
        enchPrio.put(Corrupting.class, 0);
        enchPrio.put(Elastic.class, 1);
        enchPrio.put(Grim.class, 0);
        enchPrio.put(Kinetic.class, 0);
        enchPrio.put(Lucky.class, 0);
        enchPrio.put(Projecting.class, 2);
        enchPrio.put(Shocking.class, 1);
        enchPrio.put(Vampiric.class, 1);
        enchPrio.put(HaloBlazing.class,1);
        enchPrio.put(Crushing.class,1);
        enchPrio.put(TimeReset.class,1);

        enchPrio.put(Annoying.class, 0);
        enchPrio.put(Displacing.class, 1);
        enchPrio.put(Dazzling.class, 0);
        enchPrio.put(Explosive.class, 2);
        enchPrio.put(Friendly.class, 0);
        enchPrio.put(Polarized.class, 2);
        enchPrio.put(Sacrificial.class, 1);
        enchPrio.put(Wayward.class, 2);
    }

    @Override
    public Weapon enchant( Enchantment ench ) {
        if(ench!=null){
            if(!enchList.contains(ench)){
                enchList.add(ench);
                int i = 0;
                for(Class<? extends Enchantment> e: enchPrio.keySet()){
                    if (ench.getClass() == e){
                        enchOn += 1L<<i;
                        break;
                    }
                    ++i;
                }
            }
        }
        return this;
    }

    @Override
    public boolean hasEnchant(Class<?extends Enchantment> type, Char owner) {
        for(Enchantment e: enchList){
            if(e.getClass() == type) return true;
        }
        return false;
    }

    @Override
    public int proc( Char attacker, Char defender, int damage ) {
        if (!enchList.isEmpty() && attacker.buff(MagicImmune.class) == null) {
            //Actually a rearrange is needed, but
            //1. the scale of ench is large, 2. there are only 3 prio values,
            //so scan 3 times instead.
            for(int i=2;i>=0;--i){
                for(Enchantment e: enchList){
                    if(enchPrio.get(e.getClass())==i) damage = e.proc(this, attacker, defender, damage);
                }
            }

        }

        return damage;
    }

    public boolean dispelEnchant(Class<? extends Enchantment> ench){
        if(ench!=null){
            int id = 0;
            for(Enchantment e: enchList){
                if (e.getClass() == ench){
                    enchList.remove(e);
                    enchOn -= 1<<id;
                    return true;
                }
                ++id;
            }
        }
        return false;
    }

    public void dispelAllEnch(){
        enchList.clear();
        enchOn = 0L;
    }

    public void setEnchant(long index){
        dispelAllEnch();
        int id = 0;
        for(Class<? extends Enchantment> ench : enchPrio.keySet()){
            if(((index >> id)&1)!=0){
                enchant(Reflection.newInstance(ench));
            }
            ++id;
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("s_tier", s_tier);
        bundle.put("s_str_req", s_str_req);
        bundle.put("use_default_strength", use_default_strength);
        bundle.put("use_default_base", use_default_base);
        bundle.put("use_default_scaling", use_default_scaling);
        bundle.put("base_min", base_min);
        bundle.put("base_max", base_max);
        bundle.put("scaling_min", scaling_min);
        bundle.put("scaling_max", scaling_max);
        bundle.put("s_accuracy", s_accuracy);
        bundle.put("s_delay", s_delay);
        bundle.put("s_delay_swap", s_delay_swap);
        bundle.put("s_reach", s_reach);
        bundle.put("ench_on", enchOn);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        s_tier = bundle.getInt("s_tier");
        s_str_req = bundle.getInt("s_str_req");
        use_default_strength = bundle.getBoolean("use_default_strength");
        use_default_base = bundle.getBoolean("use_default_base");
        use_default_scaling = bundle.getBoolean("use_default_scaling");
        base_min = bundle.getInt("base_min");
        base_max = bundle.getInt("base_max");
        scaling_min = bundle.getFloat("scaling_min");
        scaling_max = bundle.getFloat("scaling_max");
        s_accuracy = bundle.getFloat("s_accuracy");
        s_delay = bundle.getFloat("s_delay");
        s_delay_swap = bundle.getBoolean("s_delay_swap");
        s_reach = bundle.getInt("s_reach");
        enchOn = bundle.getLong("ench_on");
        adjustStatus();
        setEnchant(enchOn);
    }

    private static final String AC_SETTING = "setting";

    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SETTING);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SETTING)) {
            adjustStatus();
            GameScene.show(new SettingsWindow());
        }
    }

    public void adjustStatus() {
        tier = s_tier;
        ACC = s_accuracy;
        DLY = s_delay;
        RCH = s_reach;
    }

    @Override
    public int STRReq(int lvl) {
        lvl = Math.max(0, lvl);
        return (use_default_strength?(2*tier+8):(s_str_req)) - (int) (Math.sqrt(8 * lvl + 1) - 1) / 2;
    }

    @Override
    public int min(int lvl) {
        return Math.round(minBase() + level() * minScaling());
    }

    @Override
    public int max(int lvl) {
        return Math.round(maxBase() + level() * maxScaling());
    }

    private float defaultDamageModifier(){
        float modifier = 1f;
        if(s_accuracy>=1f){
            modifier *= 0.5f+0.5f/s_accuracy;
        }else if(s_accuracy<1f){
            modifier *= 2f-s_accuracy;
        }

        modifier *= 0.16f +  0.84f*s_delay;

        if (s_reach <= 8) {
            modifier *= (s_reach+3f)/(3f*s_reach+1f);
        }else {
            modifier *= 0.44f;
        }

        return modifier;
    }

    private int minBase() {
        return (use_default_base ? Math.round(tier*defaultDamageModifier()) : base_min);
    }

    private int maxBase() {
        return (use_default_base ? Math.round((5 * tier + 5)*defaultDamageModifier()) : base_max);
    }

    private float minScaling() {
        return (use_default_scaling ? 1f*defaultDamageModifier() : scaling_min);
    }

    private float maxScaling() {
        return (use_default_scaling ? (tier + 1)*defaultDamageModifier() : scaling_max);
    }

    private void resetStatus() {
        s_tier = 1;
        s_str_req = 10;
        use_default_strength = true;
        use_default_base = true;
        use_default_scaling = true;
        base_min = 1;
        base_max = 10;
        scaling_min = 1f;
        scaling_max = 2f;
        s_accuracy = 1f;
        s_delay = 1f;
        s_delay_swap = false;
        s_reach = 1;
        enchOn = 0L;
        adjustStatus();
        setEnchant(enchOn);
    }

    protected class SettingsWindow extends Window {
        private static final int WIDTH = 120;
        private static final int GAP = 2;
        private static final int SLIDER_HEIGHT = 24;
        private RedButton tierButton;
        private RedButton strengthButton;
        private RedButton baseDamageButton;
        private RedButton damageScalingButton;
        private RedButton accuracyButton;
        private RedButton reachBottom;
        private RedButton delayButton;
        private RedButton resetButton;
        private RedButton enchButton;

        public SettingsWindow() {
            tierButton = new RedButton(Messages.get(this, "tier_button", s_tier), 7) {
                @Override
                protected void onClick() {
                    GameScene.show(new TierWindow());
                }
            };
            tierButton.setRect(0, GAP, WIDTH / 2 - 1, 16);
            add(tierButton);

            strengthButton = new RedButton(Messages.get(this, "str_button", (use_default_strength?(2*s_tier+8):(s_str_req))), 7) {
                @Override
                protected void onClick() {
                    GameScene.show(new StrengthWindow());
                }
            };
            strengthButton.setRect(WIDTH / 2 + 1, GAP, WIDTH / 2 - 1, 16);
            add(strengthButton);

            baseDamageButton = new RedButton(Messages.get(this, "base_damage_button", minBase(), maxBase()), 7) {
                @Override
                protected void onClick() {
                    GameScene.show(new BaseDamageWindow());
                }
            };
            baseDamageButton.setRect(0, tierButton.bottom() + GAP, WIDTH / 2 - 1, 16);
            add(baseDamageButton);

            damageScalingButton = new RedButton(Messages.get(this, "damage_scaling_button", minScaling(), maxScaling()), 7) {
                @Override
                protected void onClick() {
                    GameScene.show(new DamageScalingWindow());
                }
            };
            damageScalingButton.setRect(WIDTH / 2 + 1, tierButton.bottom() + GAP, WIDTH / 2 - 1, 16);
            add(damageScalingButton);

            accuracyButton = new RedButton(Messages.get(this, "accuracy_button", s_accuracy), 7) {
                @Override
                protected void onClick() {
                    GameScene.show(new AccuracyWindow());
                }
            };
            accuracyButton.setRect(0, baseDamageButton.bottom() + GAP, WIDTH / 2 - 1, 16);
            add(accuracyButton);

            reachBottom = new RedButton(Messages.get(this, "reach_button", (s_reach > 6 ? "Inf" : String.valueOf(s_reach))), 7) {
                @Override
                protected void onClick() {
                    GameScene.show(new ReachWindow());
                }
            };
            reachBottom.setRect(WIDTH / 2 + 1, damageScalingButton.bottom() + GAP, WIDTH / 2 - 1, 16);
            add(reachBottom);

            delayButton = new RedButton(Messages.get(this, "delay_button", s_delay, 1 / s_delay), 7) {
                @Override
                protected void onClick() {
                    GameScene.show(new DelayWindow());
                }
            };
            delayButton.setRect(0, accuracyButton.bottom() + GAP, WIDTH, 16);
            add(delayButton);

            enchButton = new RedButton(Messages.get(this, "ench_button"), 7) {
                @Override
                protected void onClick() {
                    GameScene.show(new EnchantWindow());
                }
            };
            enchButton.setRect(0, delayButton.bottom() + GAP, WIDTH, 16);
            add(enchButton);

            resetButton = new RedButton(Messages.get(this, "reset"), 7) {
                @Override
                protected void onClick() {
                    GameScene.show(
                            new WndOptions(Messages.titleCase(Messages.get(SettingsWindow.class, "reset_title")),
                                    Messages.get(SettingsWindow.class, "reset_warn"),
                                    Messages.get(SettingsWindow.class, "reset_yes"),
                                    Messages.get(SettingsWindow.class, "reset_no")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        resetStatus();
                                        updateAllButtonText();
                                    }
                                }
                            }
                    );
                }
            };
            resetButton.setRect(0, enchButton.bottom() + 10 * GAP, WIDTH, 16);
            add(resetButton);

            resize(WIDTH, (int) resetButton.bottom());
        }

        public void updateAllButtonText(){
            tierButton.text(Messages.get(this, "tier_button", s_tier));
            strengthButton.text(Messages.get(this, "str_button", (use_default_strength?(2*s_tier+8):(s_str_req))));
            baseDamageButton.text(Messages.get(this, "base_damage_button", minBase(), maxBase()));
            damageScalingButton.text(Messages.get(this, "damage_scaling_button", minScaling(), maxScaling()));
            accuracyButton.text(Messages.get(this, "accuracy_button", s_accuracy));
            reachBottom.text(Messages.get(this, "reach_button", (s_reach > 6 ? "Inf" : String.valueOf(s_reach))));
            delayButton.text(Messages.get(this, "delay_button", s_delay, 1 / s_delay));
            resetButton.text(Messages.get(this, "reset"));
        }

        private class TierWindow extends Window {
            OptionSlider tierSlider;

            public TierWindow() {
                tierSlider = new OptionSlider(Messages.get(this, "tier"), "1", "5", 1, 5) {
                    @Override
                    protected void onChange() {
                        s_tier = getSelectedValue();
                    }
                };
                tierSlider.setSelectedValue(s_tier);
                tierSlider.setRect(0, GAP, WIDTH, SLIDER_HEIGHT);
                add(tierSlider);
                resize(WIDTH, (int) tierSlider.bottom() + GAP);
            }

            @Override
            public void onBackPressed(){
                adjustStatus();
                updateAllButtonText();
                super.onBackPressed();
            }
        }

        private class StrengthWindow extends Window {
            CheckBox c_default;
            RedButton strSet1;
            RenderedTextBlock strText;

            public StrengthWindow() {
                strText = PixelScene.renderTextBlock("", 8);
                strText.text(Messages.get(SettingsWindow.class, "str_button", (use_default_strength?(2*tier+8):(s_str_req))));
                strText.setPos(0, GAP);
                add(strText);

                c_default = new CheckBox(Messages.get(SettingsWindow.class, "default")){
                    @Override
                    protected void onClick(){
                        super.onClick();
                        use_default_strength = checked();
                        updateText();
                        strSet1.active = !checked();
                        strSet1.alpha(checked()?0.4f:1.0f);
                    }
                };
                c_default.checked(use_default_strength);
                c_default.setRect(0, GAP + strText.bottom(), WIDTH, 18);
                add(c_default);

                strSet1 = new RedButton(Messages.get(SettingsWindow.class,"str_button",s_str_req)){
                    @Override
                    protected void onClick() {
                        Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                                Messages.get(SettingsWindow.class, "level_title"), Messages.get(SettingsWindow.class, "level_title_desc"),
                                Integer.toString(s_str_req),
                                4, false, Messages.get(SettingsWindow.class, "confirm"),
                                Messages.get(SettingsWindow.class, "cancel"),false) {
                            @Override
                            public void onSelect(boolean check, String text) {
                                if (check && text.matches("\\d+")) {
                                    int level = Integer.parseInt(text);
                                    s_str_req = Math.min(level, 6666);
                                    updateText();
                                    strengthButton.text(Messages.get(SettingsWindow.class, "str_button", s_str_req));
                                    adjustStatus();
                                }
                            }
                        }));
                        super.onClick();
                    }
                };
                strSet1.setRect(0, GAP + c_default.bottom(), WIDTH, 18);
                add(strSet1);

                strSet1.active = !use_default_strength;
                strSet1.alpha(use_default_strength?0.4f:1.0f);

                updateText();

                layout();
            }

            private void updateText() {
                strengthButton.text(Messages.get(SettingsWindow.class, "str_button", (use_default_strength?(2*s_tier+8):(s_str_req))));
                strText.text(Messages.get(SettingsWindow.class, "str_button", (use_default_strength?(2*tier+8):(s_str_req))));
                strSet1.text(Messages.get(SettingsWindow.class, "str_button", (use_default_strength?(2*tier+8):(s_str_req))));
            }

            private void layout() {
                strText.setPos(0, GAP);
                c_default.setRect(0, GAP + strText.bottom(), WIDTH, 18);
                strSet1.setRect(0, GAP + c_default.bottom(), WIDTH, 18);
                resize(WIDTH, (int) strSet1.bottom() + GAP);
            }

            @Override
            public void onBackPressed(){
                adjustStatus();
                updateAllButtonText();
                super.onBackPressed();
            }
        }

        private class BaseDamageWindow extends Window {
            private RenderedTextBlock damageText;
            private CheckBox useDefault;
            private RedButton mindmg,maxdmg;

            public BaseDamageWindow() {
                damageText = PixelScene.renderTextBlock("", 8);
                damageText.text(Messages.get(SettingsWindow.class, "base_damage_button", minBase(), maxBase()));
                damageText.setPos(0, GAP);
                add(damageText);

                useDefault = new CheckBox(Messages.get(SettingsWindow.class, "default")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        use_default_base = checked();
                        updateText();
                        mindmg.active = !checked();
                        mindmg.alpha(checked()?0.4f:1.0f);
                        maxdmg.active = !checked();
                        maxdmg.alpha(checked()?0.4f:1.0f);
                    }
                };
                useDefault.checked(use_default_base);
                useDefault.setRect(0, damageText.bottom() + 2 * GAP, WIDTH, 18);
                add(useDefault);

                mindmg = new RedButton(Messages.get(SettingsWindow.class,"base_dmg_min",base_min)){
                    @Override
                    protected void onClick() {
                        Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                                Messages.get(SettingsWindow.class, "level_title"), Messages.get(SettingsWindow.class, "level_title_desc"),
                                Integer.toString(base_min),
                                4, false, Messages.get(SettingsWindow.class, "confirm"),
                                Messages.get(SettingsWindow.class, "cancel"),false) {
                            @Override
                            public void onSelect(boolean check, String text) {
                                if (check && text.matches("\\d+")) {
                                    int level = Integer.parseInt(text);
                                    base_min = Math.min(level, 6666);
                                    suppressMin();
                                    updateText();
                                    baseDamageButton.text(Messages.get(SettingsWindow.class, "base_damage_button", base_min,base_max));
                                }
                            }
                        }));
                        super.onClick();
                    }
                };
                mindmg.setRect(0, GAP + useDefault.bottom(), WIDTH, 18);
                add(mindmg);

                mindmg.active = !use_default_base;
                mindmg.alpha(use_default_base?0.4f:1.0f);

                maxdmg = new RedButton(Messages.get(SettingsWindow.class,"base_dmg_max",base_max)){
                    @Override
                    protected void onClick() {
                        Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                                Messages.get(SettingsWindow.class, "level_title"), Messages.get(SettingsWindow.class, "level_title_desc"),
                                Integer.toString(base_max),
                                4, false, Messages.get(SettingsWindow.class, "confirm"),
                                Messages.get(SettingsWindow.class, "cancel"),false) {
                            @Override
                            public void onSelect(boolean check, String text) {
                                if (check && text.matches("\\d+")) {
                                    int level = Integer.parseInt(text);
                                    base_max = Math.min(level, 6666);
                                    suppressMin();
                                    updateText();
                                    baseDamageButton.text(Messages.get(SettingsWindow.class, "base_damage_button", base_min,base_max));
                                }
                            }
                        }));
                        super.onClick();
                    }
                };
                maxdmg.setRect(0, GAP + mindmg.bottom(), WIDTH, 18);
                add(maxdmg);

                maxdmg.active = !use_default_base;
                maxdmg.alpha(use_default_base?0.4f:1.0f);

                updateText();

                resize(WIDTH, (int) maxdmg.bottom() + GAP);
            }

            private void updateText() {
                mindmg.text(Messages.get(SettingsWindow.class,"base_dmg_min",base_min));
                maxdmg.text(Messages.get(SettingsWindow.class,"base_dmg_max",base_max));
                damageText.text(Messages.get(SettingsWindow.class, "base_damage_button", minBase(), maxBase()));
            }

            private void suppressMin() {
                if (base_max < base_min) {
                    base_min = base_max;
                }
            }

            @Override
            public void onBackPressed(){
                updateAllButtonText();
                super.onBackPressed();
            }
        }

        private class DamageScalingWindow extends Window {
            private RenderedTextBlock damageScaleText;
            private CheckBox useDefault;
            private RedButton minDmgScale,maxDmgScale;

            public DamageScalingWindow() {
                damageScaleText = PixelScene.renderTextBlock("", 8);
                damageScaleText.text(Messages.get(SettingsWindow.class, "damage_scaling_button", minScaling(), maxScaling()));
                damageScaleText.setPos(0, GAP);
                add(damageScaleText);

                useDefault = new CheckBox(Messages.get(SettingsWindow.class, "default")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        use_default_scaling = checked();
                        updateText();
                        minDmgScale.active = !checked();
                        minDmgScale.alpha(checked()?0.4f:1.0f);
                        maxDmgScale.active = !checked();
                        maxDmgScale.alpha(checked()?0.4f:1.0f);
                    }
                };
                useDefault.checked(use_default_scaling);
                useDefault.setRect(0, damageScaleText.bottom() + 2 * GAP, WIDTH, 18);
                add(useDefault);

                minDmgScale = new RedButton(Messages.get(SettingsWindow.class,"scale_dmg_min",scaling_min)){
                    @Override
                    protected void onClick() {
                        Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                                Messages.get(SettingsWindow.class, "level_title"), Messages.get(SettingsWindow.class, "level_title_desc"),
                                Float.toString(scaling_min),
                                4, false, Messages.get(SettingsWindow.class, "confirm"),
                                Messages.get(SettingsWindow.class, "cancel"),true) {
                            @Override
                            public void onSelect(boolean check, String text) {
                                if (check && text.matches("^\\d+\\.?\\d{0,1}")) {
                                    float level = Float.parseFloat(text);
                                    scaling_min = Math.min(level, 6666);
                                    suppressMin();
                                    updateText();
                                    damageScalingButton.text(Messages.get(SettingsWindow.class, "damage_scaling_button", scaling_min,scaling_max));
                                }
                            }
                        }));
                        super.onClick();
                    }
                };
                minDmgScale.setRect(0, GAP + useDefault.bottom(), WIDTH, 18);
                add(minDmgScale);

                minDmgScale.active = !use_default_scaling;
                minDmgScale.alpha(use_default_scaling?0.4f:1.0f);

                maxDmgScale = new RedButton(Messages.get(SettingsWindow.class,"scale_dmg_max",scaling_max)){
                    @Override
                    protected void onClick() {
                        Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                                Messages.get(SettingsWindow.class, "level_title"), Messages.get(SettingsWindow.class, "level_title_desc"),
                                Float.toString(scaling_max),
                                4, false, Messages.get(SettingsWindow.class, "confirm"),
                                Messages.get(SettingsWindow.class, "cancel"),true) {
                            @Override
                            public void onSelect(boolean check, String text) {
                                if (check && text.matches("^\\d+\\.?\\d{0,1}")) {
                                    float level = Float.parseFloat(text);
                                    scaling_max = Math.min(level, 6666);
                                    suppressMin();
                                    updateText();
                                    damageScalingButton.text(Messages.get(SettingsWindow.class, "damage_scaling_button", scaling_min,scaling_max));
                                }
                            }
                        }));
                        super.onClick();
                    }
                };
                maxDmgScale.setRect(0, GAP + minDmgScale.bottom(), WIDTH, 18);
                add(maxDmgScale);

                maxDmgScale.active = !use_default_scaling;
                maxDmgScale.alpha(use_default_scaling?0.4f:1.0f);

                updateText();

                resize(WIDTH, (int) maxDmgScale.bottom() + GAP);
            }

            private void updateText() {
                damageScaleText.text(Messages.get(SettingsWindow.class, "damage_scaling_button", minScaling(), maxScaling()));
                minDmgScale.text(Messages.get(SettingsWindow.class, "scale_dmg_min", scaling_min));
                maxDmgScale.text(Messages.get(SettingsWindow.class, "scale_dmg_max", scaling_max));
            }

            private void suppressMin() {
                if (scaling_max < scaling_min) {
                    scaling_min = scaling_max;
                }
            }

            @Override
            public void onBackPressed(){
                updateAllButtonText();
                super.onBackPressed();
            }
        }

        private class AccuracyWindow extends Window {
            private RedButton accuracy;
            private RenderedTextBlock t_acc;

            public AccuracyWindow() {
                t_acc = PixelScene.renderTextBlock("", 8);
                t_acc.text(Messages.get(SettingsWindow.class, "accuracy_button", s_accuracy));
                t_acc.setPos(0, GAP);
                add(t_acc);

                accuracy = new RedButton(Messages.get(SettingsWindow.class,"accuracy_button",s_accuracy)){
                    @Override
                    protected void onClick() {
                        Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                                Messages.get(SettingsWindow.class, "level_title"), Messages.get(SettingsWindow.class, "level_title_desc"),
                                Float.toString(s_accuracy),
                                4, false, Messages.get(SettingsWindow.class, "confirm"),
                                Messages.get(SettingsWindow.class, "cancel"),true) {
                            @Override
                            public void onSelect(boolean check, String text) {
                                if (check && text.matches("^\\d+\\.?\\d{0,1}")) {
                                    float level = Float.parseFloat(text);
                                    s_accuracy = Math.min(level, 6666);
                                    updateText();
                                    accuracyButton.text(Messages.get(SettingsWindow.class, "accuracy_button", s_accuracy));
                                }
                            }
                        }));
                        super.onClick();
                    }
                };
                accuracy.setRect(0, GAP + t_acc.bottom(), WIDTH, 18);
                add(accuracy);

                updateText();

                resize(WIDTH, (int) accuracy.bottom() + GAP);
            }

            private void updateText() {
                t_acc.text(Messages.get(SettingsWindow.class, "accuracy_button", s_accuracy));
                accuracy.text(Messages.get(SettingsWindow.class, "accuracy_button", s_accuracy));
            }

            @Override
            public void onBackPressed(){
                adjustStatus();
                updateAllButtonText();
                super.onBackPressed();
            }
        }

        private class DelayWindow extends Window {
            private OptionSlider dly1;
            private OptionSlider dly2;
            private RenderedTextBlock t_dly;
            private CheckBox c_reverse;

            public DelayWindow() {
                t_dly = PixelScene.renderTextBlock("", 8);
                t_dly.setPos(0, GAP);
                updateText();
                add(t_dly);
                //0.5x
                dly1 = new OptionSlider(Messages.get(this, "dly1"), "0", "4.5", 0, 9) {
                    @Override
                    protected void onChange() {
                        setValue();
                        updateText();
                    }
                };
                dly1.setSelectedValue((int) ((s_delay_swap ? 1 / s_delay : s_delay) / 0.5f));
                dly1.setRect(0, t_dly.bottom() + GAP, WIDTH, SLIDER_HEIGHT);
                add(dly1);
                //0.05x
                dly2 = new OptionSlider(Messages.get(this, "dly2"), "0", "0.45", 0, 9) {
                    @Override
                    protected void onChange() {
                        setValue();
                        updateText();
                    }
                };
                dly2.setSelectedValue((int) (((s_delay_swap ? 1 / s_delay : s_delay) - dly1.getSelectedValue() * 0.5f) / 0.05f));
                dly2.setRect(0, dly1.bottom() + GAP, WIDTH, SLIDER_HEIGHT);
                add(dly2);

                c_reverse = new CheckBox(Messages.get(this, "delay_swap")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        s_delay_swap = checked();
                        setValue();
                        updateText();
                    }
                };
                c_reverse.checked(s_delay_swap);
                c_reverse.setRect(0, dly2.bottom() + GAP, WIDTH, 18);
                add(c_reverse);

                resize(WIDTH, (int) c_reverse.bottom() + GAP);
            }

            private void updateText() {
                t_dly.text(Messages.get(this, "delay", s_delay, 1 / s_delay));
            }

            private void setValue() {
                s_delay = dly1.getSelectedValue() * 0.5f + dly2.getSelectedValue() * 0.05f;
                if (s_delay > 0.01f && s_delay_swap) s_delay = 1f / s_delay;
                else handleZero();
            }

            private void handleZero() {
                if (s_delay < 0.01f) {
                    dly1.setSelectedValue(0);
                    dly2.setSelectedValue(1);
                    setValue();
                }
            }

            @Override
            public void onBackPressed(){
                adjustStatus();
                updateAllButtonText();
                super.onBackPressed();
            }
        }

        private class ReachWindow extends Window {
            OptionSlider reachSlider;

            public ReachWindow() {
                reachSlider = new OptionSlider("", "1", Messages.get(SettingsWindow.class,"infinity"), 1, 7) {
                    @Override
                    protected void onChange() {
                        s_reach = (getSelectedValue() > 6 ? 1000 : getSelectedValue());
                    }
                };
                reachSlider.setSelectedValue((s_reach > 6 ? 7 : s_reach));
                reachSlider.setRect(0, GAP, WIDTH, SLIDER_HEIGHT);
                add(reachSlider);
                resize(WIDTH, (int) reachSlider.bottom() + GAP);
            }

            @Override
            public void onBackPressed(){
                adjustStatus();
                updateAllButtonText();
                super.onBackPressed();
            }
        }

        private class EnchantWindow extends Window{
            private ArrayList<canScrollCheckBox> checkBoxes = new ArrayList<>();
            public EnchantWindow(){

                super();
                resize(120, 108 + 36);
                int placed = 0;
                ScrollPane list = new ScrollPane(new Component()) {

                    @Override
                    public void onClick(float x, float y) {
                        int max_size = checkBoxes.size();
                        for (int i = 0; i < max_size; ++i) {
                            if (checkBoxes.get(i).onClick(x, y))
                                break;
                        }
                    }

                };
                add(list);
                Component content = list.content();
                for(Class<? extends Enchantment> ench : enchPrio.keySet()){
                    canScrollCheckBox cb = new canScrollCheckBox(Reflection.newInstance(ench).name()){
                        protected boolean onClick(float x, float y){
                            if(!inside(x,y)) return false;
                            onClick();

                            return true;
                        }

                        @Override
                        protected void onClick(){
                            super.onClick();
                            checked(!checked());
                        }

                        @Override
                        protected void layout(){
                            super.layout();
                            hotArea.width = hotArea.height = 0;
                        }
                    };
                    cb.checked((enchOn&(1L<<placed)) > 0 );
                    cb.setRect(0, 18*placed, 120, 16);
                    PixelScene.align(cb);
                    placed ++;
                    content.add(cb);
                    checkBoxes.add(cb);
                }
                content.setSize(120, checkBoxes.get(checkBoxes.size()-1).bottom());
                list.setSize( list.width(), list.height() );
                list.setRect(0, 0, 120, 108);
                list.scrollTo(0,0);

                RedButton allOn = new RedButton(Messages.get(this, "all_on")) {
                    @Override
                    protected void onClick() {
                        allEnchOn();
                    }
                };
                allOn.setRect(0, 110, 59, 16);
                add(allOn);

                RedButton allOff = new RedButton(Messages.get(this, "all_off")) {
                    @Override
                    protected void onClick() {
                        allEnchOff();
                    }
                };
                allOff.setRect(61, 110, 59, 16);
                add(allOff);

                RedButton posOn = new RedButton(Messages.get(this, "positive_on")) {
                    @Override
                    protected void onClick() {
                        kindEnchOn(false);
                    }
                };
                posOn.setRect(0, 128, 59, 16);
                add(posOn);

                RedButton negOn = new RedButton(Messages.get(this, "negative_on")) {
                    @Override
                    protected void onClick() {
                        kindEnchOn(true);
                    }
                };
                negOn.setRect(61, 128, 59, 16);
                add(negOn);
            }

            private void allEnchOn(){
                enchOn = 0L;
                int len = enchPrio.size();
                enchOn = (1L<<len) - 1L;
                updateCheckBox();
                setEnchant(enchOn);
            }

            private void allEnchOff(){
                enchOn = 0L;
                updateCheckBox();
                setEnchant(enchOn);
            }

            private void kindEnchOn(boolean isCurse){
                enchOn = 0L;
                int i=0;
                for(Class<? extends Enchantment> ench : enchPrio.keySet()){
                    if(Reflection.newInstance(ench).curse() == isCurse){
                        enchOn += 1L<<i;
                    }
                    ++i;
                }
                updateCheckBox();
                setEnchant(enchOn);
            }

            private void updateCheckBox(){
                int i=0;
                for(CheckBox cb: checkBoxes){
                    cb.checked((enchOn&(1L<<i)) > 0 );
                    ++i;
                }
            }

            @Override
            public void onBackPressed() {
                int max_size = checkBoxes.size();
                long ench = 0L;
                for(int i=0;i<max_size;i++){
                    ench += (checkBoxes.get(i).checked()?1L<<i:0);
                }
                setEnchant(ench);
                super.onBackPressed();
            }
        }
    }

    private static class canScrollCheckBox extends CheckBox {

        public canScrollCheckBox(String label) {
            super(label);
        }

        protected boolean onClick(float x, float y){
            if(!inside(x,y)) return false;
            onClick();

            return true;
        }
        @Override
        protected void onClick(){
            super.onClick();
            checked(!checked());
        }

        @Override
        protected void layout(){
            super.layout();
            hotArea.width = hotArea.height = 0;
        }
    }


    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x000000);
    }
}
