package com.shatteredpixel.shatteredpixeldungeon;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.branch;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Random;

public class BGMPlayer {


    //解决电脑端高质量ogg的线程安全闪退问题
    public static void playBGM(String name, boolean loop) {
        Game.runOnRenderThread(() -> Music.INSTANCE.play(name, loop));
    }

    public static void endBGM(){
        Music.INSTANCE.end();
    }

    public static void playBGMWithDepth() {
        if (Dungeon.hero != null) {
            if (Dungeon.hero.buff(LockedFloor.class) != null) {
                BGMPlayer.playBoss();
                return;
            }
        }
        int d = depth;
        int s = branch;

        if (Statistics.bossRushMode) {

            if( d == 13 && s == 4 ){
                Music.INSTANCE.playTracks(
                        new String[]{Assets.Music.DIAMAND_KING_INTRO,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                        },
                        new float[]{1
                                ,1 ,1 ,1 ,1 ,1
                                ,1 ,1 ,1 ,1 ,1
                                ,1 ,1 ,1 ,1 ,1
                                ,1 ,1 ,1 ,1 ,1
                        },
                        false);
            } else if (d == 1) {
                playBGM(Assets.TOWN, true);
            }  else if(depth>=40) {
                playBGM(Assets.BGM_5, true);
            } else if(depth>=31){
                playBGM(Assets.BGM_4, true);
            } else if(depth>=21){
                playBGM(Assets.BGM_3, true);
            } else if(depth>=11){
                playBGM(Assets.BGM_2, true);
            } else {
                playBGM(Assets.BGM_1, true);
            }
        } else {
            if(s == 10){
                if(d == 26){
                    playBGM(Assets.YOGALXY, true);
                }
            } else if(s == 5){
                if(d == 17){
                    playBGM(Assets.FL_CITY, true);
                }
                if(d == 25){
                    playBGM(Assets.Music.THEME_FINALE, true);
                }
            } else if(s == 2 || s == 3 || s == 4){
                if(d == 25){
                    playBGM(Assets.Music.THEME_FINALE, true);
                }
                if(d == 10 ||d == 11 || d == 13 ){
                    Music.INSTANCE.playTracks(
                            new String[]{Assets.Music.DIAMAND_KING_INTRO,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                            },
                            new float[]{1
                                    ,1 ,1 ,1 ,1 ,1
                                    ,1 ,1 ,1 ,1 ,1
                                    ,1 ,1 ,1 ,1 ,1
                                    ,1 ,1 ,1 ,1 ,1
                            },
                            false);
                }
                if (d == 11 || d == 12 || d == 13 || d == 14) {
                    if(level.locked){
                        playBGM(Assets.Music.CAVES_BOSS_FINALE, true);
                    } else {
                        playBGM(Assets.Music.CAVES_TENSE, true);
                    }
                }
                if(d == 16 ||d == 17 || d == 18 || d == 19 ){
                    playBGM(Assets.Music.ANCITY, true);
                }
                if(d == 5 ){
                    playBGM(Assets.BGM_1A, true);
                }
            } else if(s == 1){
                if(d == 0) {
                    playBGM(Assets.TOWN, true);
                } else if(d == 5 ){
                    playBGM(Assets.BGM_1A, true);
                } else if(d == 16 || d == 17 || d == 18 || d == 19  ){
                    playBGM(Assets.Music.ANCITY, true);
                } else if(d == 20 ) {
                    playBGM(Assets.BGM_4, true);
                }
                if (d == 11 || d == 12 || d == 13 || d == 14) {
                    if(level.locked){
                        playBGM(Assets.Music.CAVES_BOSS_FINALE, true);
                    } else {
                        playBGM(Assets.Music.CAVES_TENSE, true);
                    }
                }
            } else {
                if (d == -1) {
                    playBGM(Assets.TOWN, true);
                }else if (d == 0) {
                    playBGM(Assets.TOWN, true);
                } else if (d > 0 && d <= 5) {
                    playBGM(Assets.BGM_1, true);
                } else if (d > 5 && d <= 10) {
                    playBGM(Assets.BGM_2, true);
                } else if (d > 10 && d <= 15) {
                    playBGM(Assets.BGM_3, true);
                } else if (d > 15 && d <= 20) {
                    playBGM(Assets.BGM_4, true);
                } else if (d > 20 && d <= 25) {
                    playBGM(Assets.BGM_5, true);
                } else if (d == 26 && Statistics.Hollow_Holiday) {
                    playBGM(Assets.HOLLOW_CITY, true);
                } else if (d >= 29 && d < 33) {
                    playBGM(Assets.HOLLOW_CITY_HARD, true);
                } else if (d > 26 && d < 29) {
                    playBGM(Assets.HOLLOW_CITY, true);
                } else if (d ==-5||d ==-15) {
                    playBGM(Assets.SNOWCYON, true);
                } else
                    //default
                    playBGM(Assets.Music.THEME, true);
            }
        }


    }

    private static String[] MUISC_RANDOM = {
            Assets.BGM_BOSSA, Assets.BGM_BOSSC,
            Assets.BGM_BOSSD,

            Assets.Music.CITY_BOSS_FINALE,
            Assets.Music.CAVES_BOSS_FINALE, Assets.BGM_YOU,

            Assets.Music.DRAGON_LING,
            Assets.BOSSDOG,
    };

    public static void playBoss() {
        int t = depth;
        int s = branch;
        if (Statistics.bossRushMode) {
            if (t == 42 &&  !Statistics.NoTime) {
                playBGM(Assets.BGM_BOSSE3, true);
            } else if(t == 42) {
                playBGM(Assets.BGM_BOSSE4, true);
            } else if(depth == 33) {
                playBGM(Assets.BGM_BOSSD2, true);
            } else if(depth == 13){
                Music.INSTANCE.playTracks(
                        new String[]{Assets.Music.DIAMAND_KING_INTRO,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                        },
                        new float[]{1
                                ,1 ,1 ,1 ,1 ,1
                                ,1 ,1 ,1 ,1 ,1
                                ,1 ,1 ,1 ,1 ,1
                                ,1 ,1 ,1 ,1 ,1
                        },
                        false);
            } else {
                if(!Statistics.NightDreamLoop){
                    playBGM(MUISC_RANDOM[Random.Int(MUISC_RANDOM.length)],true);
                    Statistics.NightDreamLoop = true;
                }

            }
        } else {
            if(s == 10) {
                if (t == 26 && !Statistics.TrueYogNoDied) {
                    playBGM(Assets.STARLXY, true);
                } else {
                    playBGM(Assets.YOGGOD, true);
                }
            } else if(s == 7){
                playBGM(Assets.BGM_SHOP, true);
            }
            if(s == 3 && t == 16 ||s == 3 && t == 17 || s == 3 && t == 18) {
                playBGM(Assets.SKBJY, true);
            } else if(t == 5 && s == 3){
                playBGM(Assets.Music.DRAGON_LING, true);
            } else if (Dungeon.bossLevel() && t == 5 && s == 0 || t == 4 && s == 2) {
                playBGM(Assets.BGM_BOSSA, true);
            } else if (Dungeon.bossLevel() && t == 10) {
                if(((Statistics.boss_enhance & 0x2) != 0 || Statistics.mimicking) && !Statistics.mustTengu){
                    Music.INSTANCE.playTracks(
                            new String[]{Assets.Music.DIAMAND_KING_INTRO,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                                    Assets.Music.DIAMAND_KING_LOOP,Assets.Music.DIAMAND_KING_LOOP,
                            },
                            new float[]{1
                                    ,1 ,1 ,1 ,1 ,1
                                    ,1 ,1 ,1 ,1 ,1
                                    ,1 ,1 ,1 ,1 ,1
                                    ,1 ,1 ,1 ,1 ,1
                            },
                            false);
                } else {
                    playBGM(Assets.BGM_BOSSB2, true);
                }
            } else if (t == 14) {
                playBGM(Assets.BGM_BOSSC, true);
            } else if (Dungeon.bossLevel() && t == 15) {
                if(Statistics.RandMode){
                    playBGM(Assets.BGM_YOU, true);
                } else if((Statistics.boss_enhance & 0x4) != 0) {
                    playBGM(Assets.BGM_BOSSC, true);
                } else {
                    playBGM(Assets.Music.CAVES_BOSS, true);
                }
            } else if (Dungeon.bossLevel() && t == 20) {
                if(s == 1) playBGM(Assets.BGM_BOSSD2, true);
                else  playBGM(Assets.BGM_BOSSD, true);
            } else if (Dungeon.bossLevel() && t == 25 && Dungeon.isChallenged(CS) && !Statistics.NoTime) {
                playBGM(Assets.BGM_BOSSE3, true);
            } else if(Dungeon.bossLevel() && t == 25 && Dungeon.isChallenged(CS) && Statistics.NoTime) {
                playBGM(Assets.BGM_BOSSE4, true);
            } else if (Dungeon.bossLevel() && t == 25){
                if(Statistics.RandMode)playBGM( Assets.BGM_BOSSE4, true );
                level.playLevelMusic();
            } else if (Dungeon.bossLevel() && t == 31) {
                playBGM(Assets.BOSSDOG, true);
            } else if (Dungeon.bossLevel() && t == -15) {
                playBGM(Assets.BGM_FRBOSS, true);
            }   else if (Dungeon.bossLevel() && t == -31) {
                playBGM(Assets.SKBJY, true);
            }
        }
    }
}
