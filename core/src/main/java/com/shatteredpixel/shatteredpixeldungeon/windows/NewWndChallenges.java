//package com.shatteredpixel.shatteredpixeldungeon.windows;
//
//import com.shatteredpixel.shatteredpixeldungeon.Challenges;
//import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
//import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
//import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
//import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
//import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
//import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
//import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
//import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
//import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
//import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
//import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
//import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
//import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
//import com.watabou.noosa.Image;
//import com.watabou.noosa.ui.Component;
//import com.watabou.utils.Callback;
//
//import java.util.ArrayList;
//
//public class NewWndChallenges extends Window {
//
//    private static final int WIDTH		= 120;
//    private static final int TTL_HEIGHT = 16;
//    private static final int BTN_HEIGHT = 16;
//    private static final int GAP        = 1;
//
//    private boolean editable;
//    private ArrayList<NewWndChallenges.ChallengeButton> boxes;
//
//    public int index = 0;
//    private static int boundIndex(int index) {
//        int result = index;
//        while (result < 0) result += 9;
//        while (result >= Challenges.NAME_IDS.length) result -= 9;
//        result = (int) (Math.floor(result / 9.0)) * 9;
//        return result;
//    }
//    protected Callback backPressCallback;
//
//    private int checked;
//    public NewWndChallenges(int checked, boolean editable ) {
//
//        super();
//
//        this.checked = checked;
//        this.editable = editable;
//
//        RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 12 );
//        title.hardlight( TITLE_COLOR );
//        title.setPos(
//                (WIDTH - title.width()) / 2,
//                (TTL_HEIGHT - title.height()) / 2
//        );
//        PixelScene.align(title);
//        add( title );
//
//        boxes = new ArrayList<>();
//
//        float pos = TTL_HEIGHT;
//
//        float infoBottom = 0;
//        index = boundIndex(index);
//        for (int i = index; i < index + 9; i++) {
//
//            if (i >= Challenges.NAME_IDS.length) break;
//            final String challenge = Challenges.NAME_IDS[i];
//
//            if (i > 0 && i % 3 == 0) {
//                pos += GAP;
//            }
//            NewWndChallenges.ChallengeInfo info = new NewWndChallenges.ChallengeInfo(foundChallangeIcon(i) /*挑战的LOGO*/, challenge, false /*是否冲突*/, null
//                    /*冲突按钮的点击回调*/);
//            NewWndChallenges.ChallengeButton cb = info.check;
//            cb.checked((checked & Challenges.MASKS[i]) != 0);
//            cb.active = editable;
//            boxes.add( cb );
//            info.setRect((i % 3) * 40, pos, 40, 0);
//            infoBottom = Math.max(infoBottom, info.bottom());
//
//            add( info );
//
//            if (i % 3 == 2) {
//                pos = infoBottom;
//            }
//        }
//        RedButton btnPrev; RedButton btnNext; RedButton btnFirst; RedButton btnLast;
//        btnPrev = new RedButton("上页" /*需要替换为国际化文本*/) {
//            @Override
//            protected void onClick() {
//                index = boundIndex(index - 9);
//                NewWndChallenges.this.hide();
//                ShatteredPixelDungeon.scene().addToFront(new NewWndChallenges(refreshChecked(checked), editable) {
//                    {
//                        index = NewWndChallenges.this.index;
//                        backPressCallback = NewWndChallenges.this.backPressCallback;
//                    }
//                });
//            }
//        };
//        add( btnPrev );
//        pos += GAP;
//
//        btnPrev.setRect(0, pos, (WIDTH - GAP) * 0.5f, BTN_HEIGHT);
//        btnNext = new RedButton("下页" /*需要替换为国际化文本*/) {
//            @Override
//            protected void onClick() {
//                index = boundIndex(index + 9);
//                NewWndChallenges.this.hide();
//                ShatteredPixelDungeon.scene().addToFront(new NewWndChallenges(refreshChecked(checked), editable) {
//                    {
//                        index = NewWndChallenges.this.index;
//                        backPressCallback = NewWndChallenges.this.backPressCallback;
//                    }
//                });
//            }
//        };
//        add( btnNext );
//        btnNext.setRect(btnPrev.right() + GAP, pos, (WIDTH - GAP) * 0.5f, BTN_HEIGHT);
//
//        pos += BTN_HEIGHT;
//
//        btnFirst = new RedButton("首页" /*需要替换为国际化文本*/) {
//            @Override
//            protected void onClick() {
//                index = 0;
//                NewWndChallenges.this.hide();
//                ShatteredPixelDungeon.scene().addToFront(new NewWndChallenges(refreshChecked(checked), editable) {
//                    {
//                        index = NewWndChallenges.this.index;
//                        backPressCallback = NewWndChallenges.this.backPressCallback;
//                    }
//                });
//            }
//        };
//        add( btnFirst );
//        pos += GAP;
//
//        btnFirst.setRect(0, pos, (WIDTH - GAP) * 0.5f, BTN_HEIGHT);
//        btnLast = new RedButton("末页" /*需要替换为国际化文本*/) {
//            @Override
//            protected void onClick() {
//                index = Challenges.NAME_IDS.length - 1;
//                NewWndChallenges.this.hide();
//                ShatteredPixelDungeon.scene().addToFront(new NewWndChallenges(refreshChecked(checked), editable) {
//                    {
//                        index = NewWndChallenges.this.index;
//                        backPressCallback = NewWndChallenges.this.backPressCallback;
//                    }
//                });
//            }
//        };
//        add( btnLast );
//        btnLast.setRect(btnFirst.right() + GAP, pos, (WIDTH - GAP) * 0.5f, BTN_HEIGHT);
//
//        pos += BTN_HEIGHT;
//
//        resize( WIDTH, (int)pos );
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        if (editable) {
//            int value = 0;
//            for (int i=0; i < boxes.size(); i++) {
//                if (boxes.get( i ).checked()) {
//                    value |= Challenges.MASKS[i];
//                }
//            }
//            SPDSettings.challenges( value );
//        }
//
//        super.onBackPressed();
//    }
//
//    private int refreshChecked(int checked) {
//        int value = checked;
//        for (int i = 0; i < boxes.size(); i ++) {
//            if (index + i >= Challenges.MASKS.length) break;
//            value &= ~Challenges.MASKS[index + i];
//            if (boxes.get( i ).checked()) {
//                value |= Challenges.MASKS[index + i];
//            }
//        }
//        return value;
//    }
//
//    private static class ChallengeButton extends IconButton {
//        private boolean checked = false;
//        public ChallengeButton() {
//            super( Icons.get( Icons.CHALLENGE_OFF ));
//        }
//        @Override
//        protected void onClick() {
//            checked(!checked);
//        }
//        public boolean checked() {
//            return checked;
//        }
//        public void checked( boolean checked ) {
//            this.checked = checked;
//            icon( Icons.get( checked ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF ) );
//        }
//    }
//
//    private static class ChallengeInfo extends Component {
//        Image icon;
//        IconButton info;
//        NewWndChallenges.ChallengeButton check;
//        IconButton conflict;
//        public ChallengeInfo(Image icon, String challenge, boolean conflict, Callback onConflict) {
//            super();
//            this.icon = icon;
//            add( icon );
//            info = new IconButton(Icons.get(Icons.INFO)){
//                @Override
//                protected void onClick() {
//                    ShatteredPixelDungeon.scene().add(
//                            new WndTitledMessage(new Image(NewWndChallenges.ChallengeInfo.this.icon),
//                                    Messages.titleCase(Messages.get(Challenges.class, challenge)),
//                                    Messages.get(Challenges.class, challenge+"_desc"))
//                    );
//                }
//            };
//            add( info );
//            check = new NewWndChallenges.ChallengeButton();
//            add( check );
//            if (conflict) {
//                this.conflict = new IconButton(Icons.get(Icons.WARNING)) {
//                    @Override
//                    protected void onClick() {
//                        if (onConflict != null) onConflict.call();
//                    }
//                };
//                add( this.conflict );
//            }
//            else this.conflict = null;
//        }
//
//        @Override
//        protected void layout() {
//            icon.x = x + (width - 32) / 2;
//            icon.y = y;
//            PixelScene.align( icon );
//            info.setRect(icon.x + icon.width, y, 16, BTN_HEIGHT);
//            PixelScene.align( info );
//            if (conflict == null) {
//                check.setRect(x + (width - 16) / 2, y + BTN_HEIGHT, 16, BTN_HEIGHT);
//                PixelScene.align( check );
//            }
//            else {
//                check.setRect(x + (width - 32) / 2, y + BTN_HEIGHT, 16, BTN_HEIGHT);
//                PixelScene.align( check );
//                conflict.setRect(check.right(), check.top(), 16, BTN_HEIGHT);
//                PixelScene.align( conflict );
//            }
//            height = BTN_HEIGHT * 2;
//        }
//
//    }
//
//    private static Image foundChallangeIcon(int i) {
//
//        if (Challenges.NAME_IDS[i].equals("no_food")) {
//            return new ItemSprite(ItemSpriteSheet.OVERPRICED,new ItemSprite.Glowing( 0xFF0000 ));
//        } else if (Challenges.NAME_IDS[i].equals("no_armor")) {
//            return new ItemSprite(ItemSpriteSheet.ARMOR_MAGE,new ItemSprite.Glowing( 0xFF0000 ));
//        } else if (Challenges.NAME_IDS[i].equals("no_healing")) {
//            return new RatSprite();
//        } else if (Challenges.NAME_IDS[i].equals("no_herbalism")) {
//            return new GnollSprite();
//        } else if (Challenges.NAME_IDS[i].equals("darkness")) {
//            return Icons.get(Icons.NEWS);
//        } else if (Challenges.NAME_IDS[i].equals("no_scrolls")) {
//            return Icons.get(Icons.LANGS);
//        } else if (Challenges.NAME_IDS[i].equals("aquaphobia")) {
//            return Icons.get(Icons.SHPX);
//        } else if (Challenges.NAME_IDS[i].equals("rlpt")) {
//            return Icons.get(Icons.LIBGDX);
//        } else if (Challenges.NAME_IDS[i].equals("sbsg")) {
//            return Icons.get(Icons.WATA);
//        } else if (Challenges.NAME_IDS[i].equals("exsg")) {
//            return Icons.get(Icons.SHPX);
//        } else if (Challenges.NAME_IDS[i].equals("stronger_bosses")) {
//            return Icons.get(Icons.HUNTRESS);
//        } else if (Challenges.NAME_IDS[i].equals("dhxd")) {
//            return Icons.get(Icons.SKULL);
//        } else {
//            return Icons.get(Icons.STATS);
//        }
//
//    }
//
//}
