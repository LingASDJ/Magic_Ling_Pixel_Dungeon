package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.WndTextNumberInput;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class CustomPlayer extends TestItem{
    {
        image = ItemSpriteSheet.KIT;
        defaultAction = AC_SETTING;
    }

    private static final String AC_SETTING = "setting";
    private static final String AC_ENABLE = "enable";
    private static final String AC_CLOSE = "close";
    public static boolean overrideGame;
    public static boolean shouldOverride;
    public static int baseDamage;
    public static int baseArmor;
    public static float baseAttackDelay;
    public static int baseAccuracy;
    public static int baseEvasion;
    public static float baseSpeed;
    public static int baseCurrentHealth;
    public static int baseMaxHealth;
    public static int baseStrength;
    public static int baseLevel;

    public CustomPlayer(){
        overrideGame = false;
        shouldOverride = false;
        baseCurrentHealth = baseMaxHealth = 20;
        baseDamage = 0;
        baseArmor = 0;
        baseAttackDelay = 1;
        baseAccuracy = 1;
        baseEvasion = 5;
        baseSpeed = 1;
        baseStrength = 10;
        baseLevel = 1;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SETTING);
        actions.add(AC_ENABLE);
        actions.add(AC_CLOSE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if(action.equals(AC_SETTING)){
            GameScene.show(new SettingsWindow());
        }else if(action.equals(AC_ENABLE)){
            overrideGame = true;
        }else if(action.equals(AC_CLOSE)){
            overrideGame = false;
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("overrideGame",overrideGame);
        bundle.put("shouldOverride",shouldOverride);
        bundle.put("baseCurrentHealth",baseCurrentHealth);
        bundle.put("baseMaxHealth",baseMaxHealth);
        bundle.put("baseDamage",baseDamage);
        bundle.put("baseArmor",baseArmor);
        bundle.put("baseAttackDelay",baseAttackDelay);
        bundle.put("baseAccuracy",baseAccuracy);
        bundle.put("baseEvasion",baseEvasion);
        bundle.put("baseSpeed",baseSpeed);
        bundle.put("baseStrength",baseStrength);
        bundle.put("baseLevel",baseLevel);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        overrideGame = bundle.getBoolean("overrideGame");
        shouldOverride = bundle.getBoolean("shouldOverride");
        baseCurrentHealth = bundle.getInt("baseCurrentHealth");
        baseMaxHealth = bundle.getInt("baseMaxHealth");
        baseDamage = bundle.getInt("baseDamage");
        baseArmor = bundle.getInt("baseArmor");
        baseAttackDelay = bundle.getFloat("attackDelay");
        baseAccuracy = bundle.getInt("accuracy");
        baseEvasion = bundle.getInt("evasion");
        baseSpeed = bundle.getInt("baseSpeed");
        baseStrength = bundle.getInt("baseStrength");
        baseLevel = bundle.getInt("baseLevel");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc")+Messages.get(this, overrideGame?"apply":"not_apply");
    }

    protected class SettingsWindow extends Window {
        private static final int WIDTH = 120;
        private static final int GAP = 2;
        private RedButton currentHealthButton;
        private RedButton maxHealthButton;
        private RedButton baseDamageButton;
        private RedButton baseArmorButton;
        private RedButton attackDelayButton;
        private RedButton accuracyButton;
        private RedButton evasionBotton;
        private RedButton speedButton;
        private RedButton strengthButton;
        private RedButton levelButton;
        private CheckBox overrideResultButton;
        private RedButton resetButton;

        public SettingsWindow() {
            currentHealthButton = new RedButton(Messages.get(CustomPlayer.class, "currenthealth_button", hero.HP), 7) {
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                            Messages.get(CustomPlayer.class, "custom_title"),
                            Messages.get(CustomPlayer.class, "currenthealth_desc"),
                            Integer.toString(baseCurrentHealth),
                            6, false, Messages.get(CustomPlayer.class, "confirm"),
                            Messages.get(CustomPlayer.class, "cancel"),false) {
                        @Override
                        public void onSelect(boolean check, String text) {
                            if (check && text.matches("\\d+")) {
                                int value = Integer.parseInt(text);
                                if(value>0)
                                    baseCurrentHealth = hero.HP = Math.min( value > hero.HT ? hero.HT : value, Integer.MAX_VALUE);
                                updateAllButtonText();
                            }
                        }
                    }));
                }
            };
            currentHealthButton.setRect(0, GAP, WIDTH, 16);
            add(currentHealthButton);

            maxHealthButton = new RedButton(Messages.get(CustomPlayer.class, "maxhealth_button", hero.HT), 7) {
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                            Messages.get(CustomPlayer.class, "custom_title"),
                            Messages.get(CustomPlayer.class, "maxhealth_desc"),
                            Integer.toString(baseMaxHealth),
                            6, false, Messages.get(CustomPlayer.class, "confirm"),
                            Messages.get(CustomPlayer.class, "cancel"),false) {
                        @Override
                        public void onSelect(boolean check, String text) {
                            if (check && text.matches("\\d+")) {
                                int value = Integer.parseInt(text);
                                if(value>0) {
                                    value = Math.min(value, 666666);
                                    if(value < hero.HP)
                                        baseCurrentHealth = hero.HP = value;
                                    baseMaxHealth = hero.HT = value;
                                }
                                updateAllButtonText();
                            }
                        }
                    }));
                }
            };
            maxHealthButton.setRect(0, currentHealthButton.bottom()+GAP, WIDTH, 16);
            add(maxHealthButton);

            float[] baseDamageResult = CalculateBaseDamage();
            baseDamageButton = new RedButton(Messages.get(CustomPlayer.class, "basedamage_button", baseDamageResult[0],baseDamageResult[1]), 7) {
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                            Messages.get(CustomPlayer.class, "custom_title"),
                            Messages.get(CustomPlayer.class, "basedamage_desc"),
                            Integer.toString(baseDamage),
                            5, false, Messages.get(CustomPlayer.class, "confirm"),
                            Messages.get(CustomPlayer.class, "cancel"),false) {
                        @Override
                        public void onSelect(boolean check, String text) {
                            if (check && text.matches("\\d+")) {
                                int value = Integer.parseInt(text);
                                baseDamage = Math.min( value, 66666);
                                updateAllButtonText();
                            }
                        }
                    }));
                }
            };
            baseDamageButton.setRect(0, maxHealthButton.bottom() + GAP, WIDTH, 16);
            add(baseDamageButton);

            float[] baseArmorResult = CalculateBaseArmor();
            baseArmorButton = new RedButton(Messages.get(CustomPlayer.class, "basearmor_button", baseDamageResult[0],baseDamageResult[1]), 7) {
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                            Messages.get(CustomPlayer.class, "custom_title"),
                            Messages.get(CustomPlayer.class, "basearmor_desc"),
                            Integer.toString(baseArmor),
                            5, false, Messages.get(CustomPlayer.class, "confirm"),
                            Messages.get(CustomPlayer.class, "cancel"),false) {
                        @Override
                        public void onSelect(boolean check, String text) {
                            if (check && text.matches("\\d+")) {
                                int value = Integer.parseInt(text);
                                baseArmor = Math.min( value, 66666);
                                updateAllButtonText();
                            }
                        }
                    }));
                }
            };
            baseArmorButton.setRect(0, baseDamageButton.bottom() + GAP, WIDTH, 16);
            add(baseArmorButton);

            attackDelayButton = new RedButton(Messages.get(CustomPlayer.class, "accuracy_button", hero.attackDelay()), 7) {
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                            Messages.get(CustomPlayer.class, "custom_title"),
                            Messages.get(CustomPlayer.class, "accuracy_desc"),
                            Float.toString(baseAttackDelay),
                            4, false, Messages.get(CustomPlayer.class, "confirm"),
                            Messages.get(CustomPlayer.class, "cancel"),true) {
                        @Override
                        public void onSelect(boolean check, String text) {
                            if (check && text.matches("\\d+")) {
                                float value = Float.parseFloat(text);
                                if(value>0)
                                    baseAttackDelay = Math.min( value, 6666);
                                updateAllButtonText();
                            }
                        }
                    }));
                }
            };
            attackDelayButton.setRect(0, baseArmorButton.bottom()+GAP, WIDTH, 16);
            add(attackDelayButton);

            accuracyButton = new RedButton(Messages.get(CustomPlayer.class, "accuracy_button", hero.attackSkill(null)), 7) {
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                            Messages.get(CustomPlayer.class, "custom_title"),
                            Messages.get(CustomPlayer.class, "accuracy_desc"),
                            Integer.toString(baseAccuracy),
                            4, false, Messages.get(CustomPlayer.class, "confirm"),
                            Messages.get(CustomPlayer.class, "cancel"),false) {
                        @Override
                        public void onSelect(boolean check, String text) {
                            if (check && text.matches("\\d+")) {
                                int value = Integer.parseInt(text);
                                if(value>=1)
                                    baseAccuracy = Math.min( value, 6666);
                                updateAllButtonText();
                            }
                        }
                    }));
                }
            };
            accuracyButton.setRect(0, attackDelayButton.bottom()+GAP, WIDTH / 2 - 1, 16);
            add(accuracyButton);

            evasionBotton = new RedButton(Messages.get(CustomPlayer.class, "evasion_button", hero.defenseSkill(null)), 7) {
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                            Messages.get(CustomPlayer.class, "custom_title"),
                            Messages.get(CustomPlayer.class, "evasion_desc"),
                            Integer.toString(baseEvasion),
                            4, false, Messages.get(CustomPlayer.class, "confirm"),
                            Messages.get(CustomPlayer.class, "cancel"),false) {
                        @Override
                        public void onSelect(boolean check, String text) {
                            if (check && text.matches("\\d+")) {
                                int value = Integer.parseInt(text);
                                if(value>=5)
                                    baseEvasion = Math.min( value, 6666);
                                updateAllButtonText();
                            }
                        }
                    }));
                }
            };
            evasionBotton.setRect(accuracyButton.right()+GAP, attackDelayButton.bottom()+GAP, WIDTH / 2 - 1, 16);
            add(evasionBotton);

            speedButton = new RedButton(Messages.get(CustomPlayer.class, "speed_button", hero.speed()), 7) {
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                            Messages.get(CustomPlayer.class, "custom_title"),
                            Messages.get(CustomPlayer.class, "speed_desc"),
                            Float.toString(baseSpeed),
                            4, false, Messages.get(CustomPlayer.class, "confirm"),
                            Messages.get(CustomPlayer.class, "cancel"),false) {
                        @Override
                        public void onSelect(boolean check, String text) {
                            if (check && text.matches("\\d+")) {
                                int value = Integer.parseInt(text);
                                if(value>=1)
                                    baseSpeed = Math.min( value, 6666);
                                updateAllButtonText();
                            }
                        }
                    }));
                }
            };
            speedButton.setRect(0, evasionBotton.bottom()+GAP, WIDTH, 16);
            add(speedButton);

            strengthButton = new RedButton(Messages.get(CustomPlayer.class, "strength_button", hero.STR), 7) {
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                            Messages.get(CustomPlayer.class, "custom_title"),
                            Messages.get(CustomPlayer.class, "strength_desc"),
                            Integer.toString(baseStrength),
                            4, false, Messages.get(CustomPlayer.class, "confirm"),
                            Messages.get(CustomPlayer.class, "cancel"),false) {
                        @Override
                        public void onSelect(boolean check, String text) {
                            if (check && text.matches("\\d+")) {
                                int value = Integer.parseInt(text);
                                if(value>=10)
                                    baseStrength = hero.STR = Math.min( value, 6666);
                                updateAllButtonText();
                            }
                        }
                    }));
                }
            };
            strengthButton.setRect(0, speedButton.bottom()+GAP, WIDTH / 2 - 1, 16);
            add(strengthButton);

            levelButton = new RedButton(Messages.get(CustomPlayer.class, "level_button", hero.lvl), 7) {
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                            Messages.get(CustomPlayer.class, "custom_title"),
                            Messages.get(CustomPlayer.class, "level_desc"),
                            Integer.toString(baseLevel),
                            2, false, Messages.get(CustomPlayer.class, "confirm"),
                            Messages.get(CustomPlayer.class, "cancel"),false) {
                        @Override
                        public void onSelect(boolean check, String text) {
                            if (check && text.matches("\\d+")) {
                                int value = Integer.parseInt(text);
                                if(value>=1)
                                    baseLevel = hero.lvl = Math.min( value, 30);
                                updateAllButtonText();
                            }
                        }
                    }));
                }
            };
            levelButton.setRect(strengthButton.right() + GAP, speedButton.bottom()+GAP, WIDTH / 2 - 1, 16);
            add(levelButton);

            overrideResultButton = new CheckBox(Messages.get(CustomPlayer.class, "override")) {
                @Override
                public void checked(boolean value) {
                    if (checked != value) {
                        checked = value;
                        icon.copy( Icons.get( checked ? Icons.CHECKED : Icons.UNCHECKED ) );
                        shouldOverride = checked;
                        updateAllButtonText();
                    }
                }
            };
            overrideResultButton.setRect(0, levelButton.bottom() + GAP, WIDTH, 16);
            add(overrideResultButton);

            resetButton = new RedButton(Messages.get(CustomPlayer.class, "reset"), 7) {
                @Override
                protected void onClick() {
                    GameScene.show(
                            new WndOptions(Messages.titleCase(Messages.get(CustomPlayer.class, "reset_title")),
                                    Messages.get(CustomPlayer.class, "reset_warn"),
                                    Messages.get(CustomPlayer.class, "reset_yes"),
                                    Messages.get(CustomPlayer.class, "reset_no")) {
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
            resetButton.setRect(0, overrideResultButton.bottom() + GAP, WIDTH, 16);
            add(resetButton);

            updateAllButtonText();

            resize(WIDTH, (int) (resetButton.bottom() + GAP));
        }

        private void updateAllButtonText(){
            currentHealthButton.text(Messages.get(CustomPlayer.class, "currenthealth_button", hero.HP));
            maxHealthButton.text(Messages.get(CustomPlayer.class, "maxhealth_button", hero.HT));

            float[] baseDamageResult = CalculateBaseDamage();
            baseDamageButton.text(Messages.get(CustomPlayer.class, "basedamage_button", baseDamageResult[0],baseDamageResult[1]));
            float[] baseArmorResult = CalculateBaseArmor();
            baseArmorButton.text(Messages.get(CustomPlayer.class, "basearmor_button", baseArmorResult[0],baseArmorResult[1]));

            attackDelayButton.text(Messages.get(CustomPlayer.class, "attackdelay_button", hero.attackDelay()));
            accuracyButton.text(Messages.get(CustomPlayer.class, "accuracy_button", hero.attackSkill(null)));
            evasionBotton.text(Messages.get(CustomPlayer.class, "evasion_button", hero.defenseSkill(null)));
            speedButton.text(Messages.get(CustomPlayer.class, "speed_button", hero.speed()));
            strengthButton.text(Messages.get(CustomPlayer.class, "strength_button", hero.STR));
            levelButton.text(Messages.get(CustomPlayer.class, "level_button", hero.lvl));
        }

        private void resetStatus(){
            shouldOverride = false;
            baseDamage = 0;
            baseArmor = 0;
            baseAttackDelay = 1;
            baseAccuracy = 1;
            baseEvasion = 5;
            baseSpeed = 1;
            baseCurrentHealth = baseMaxHealth = hero.HP = hero.HT = 20;
            baseStrength = hero.STR = 10;
            baseLevel = hero.lvl = 1;
            updateAllButtonText();
        }

        public float[] CalculateBaseDamage(){
            int tries = 500;

            int[] damage = new int[tries];
            float[] result = new float[2];
            for (int i = 0; i < tries; ++i) {
                damage[i] = hero.damageRoll();
            }
            float variance = 0f;
            float average = 0f;
            for (int i = 0; i < tries; ++i) {
                average += damage[i];
            }
            average /= tries;
            result[0] = average;
            for (int i = 0; i < tries; ++i) {
                variance += (damage[i] - average) * (damage[i] - average);
            }
            variance = (float) Math.sqrt(variance/tries);
            result[1] = variance;
            return result;
        }

        public float[] CalculateBaseArmor(){
            int tries = 500;

            int[] defense = new int[tries];
            float[] result = new float[2];
            for (int i = 0; i < tries; ++i) {
                defense[i] = hero.drRoll();
            }
            float variance = 0f;
            float average = 0f;
            for (int i = 0; i < tries; ++i) {
                average += defense[i];
            }
            average /= tries;
            result[0] = average;
            for (int i = 0; i < tries; ++i) {
                variance += (defense[i] - average) * (defense[i] - average);
            }
            variance = (float) Math.sqrt(variance/tries);
            result[1] = variance;
            return result;
        }
    }
}
