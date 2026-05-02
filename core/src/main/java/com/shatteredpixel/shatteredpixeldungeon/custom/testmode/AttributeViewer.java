package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class AttributeViewer extends TestItem{
    {
        image = ItemSpriteSheet.DEV_2;
        defaultAction = AC_INSPECT;
    }

    private static final String AC_INSPECT = "inspect";

    private CellSelector.Listener selector = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer cell ) {
            if( cell == null ) return;

            Char ch = Actor.findChar( cell );
            if( ch == null )
                GLog.w( M.L( AttributeViewer.class, "no_char" ) );
            else
                GameScene.show( new WndMobInfo( ch, ch instanceof Mob ? mobAttribute( ch ) : heroAttribute( ch ) ) );
        }

        @Override
        public String prompt() {
            return M.L( AttributeViewer.class, "select" );
        }
    };

    private String mobAttribute( Char ch ){
        String desc = "";
        if( !( ch instanceof Mob ) )
            return desc;

        desc += ((Mob) ch).description();

        desc += "\n\n";
        desc += M.L( AttributeViewer.class, "health", ch.HP, ch.HT );
        desc += "\n";

        int tries = 500;

        int[] damage = new int[ tries ];
        for ( int i = 0; i < tries; ++i ) {
            damage[ i ] = ch.damageRoll();
        }
        float variance = 0f;
        float average = 0f;
        for ( int i = 0; i < tries; ++i ) {
            average += damage[ i ];
        }
        average /= tries;
        for ( int i = 0; i < tries; ++i ) {
            variance += ( damage[ i ] - average ) * ( damage[ i ] - average );
        }
        variance = (float) Math.sqrt( variance / tries );
        desc += M.L( AttributeViewer.class, "damage", average, variance );
        desc += "\n";

        int[] defense = new int[ tries ];
        for ( int i = 0; i < tries; ++i ) {
            defense[ i ] = ch.drRoll();
        }
        variance = 0f;
        average = 0f;
        for ( int i = 0; i < tries; ++i ) {
            average += defense[ i ];
        }
        average /= tries;
        for ( int i = 0; i < tries; ++i ) {
            variance += ( defense[ i ] - average ) * ( defense[ i ] - average );
        }
        variance = (float) Math.sqrt( variance / tries );
        desc += M.L( AttributeViewer.class, "defense", average, variance );
        desc += "\n";

        try {
            desc += M.L( AttributeViewer.class, "accuracy", ch.attackSkill(null) );
            desc += "\n";
        }catch ( NullPointerException ignored ){

        }

        try {
            desc += M.L( AttributeViewer.class, "evasion", ch.defenseSkill(null) );
            desc += "\n";
        }catch ( NullPointerException ignored ){

        }

        desc += M.L( AttributeViewer.class, "move_speed", ch.speed() );
        desc += "\n";


        desc += M.L( AttributeViewer.class, "attack_delay", ((Mob) ch).attackDelay() );
        desc += "\n";

        desc += M.L( AttributeViewer.class, "view_distance", ch.viewDistance );
        desc += "\n";


        desc += M.L( AttributeViewer.class, "exp", ((Mob) ch).EXP, ((Mob) ch).maxLvl );
        desc += "\n";
        desc += M.L(AttributeViewer.class, "lootchance",
                String.format(Locale.ROOT, "%.2f", ((Mob) ch).getLootChance() * 100f));
        desc += "\n";
        desc += M.L(AttributeViewer.class, "total_lootchance",
                String.format(Locale.ROOT, "%.2f", ((Mob) ch).lootChance() * 100f));
        desc += "\n";

        if(((Mob) ch).setLootItem() != null){
            String fullText = ((Mob) ch).setLootItem().toString();
            String noClass = fullText.replace("class ", "");
            int lastDot = noClass.lastIndexOf('.');
            String simpleName = noClass.substring(lastDot + 1);
            desc += M.L(AttributeViewer.class, "lootchance_item", isAllUppercaseRegex(simpleName) ? lootDisplayRules(simpleName) : ((Mob) ch).createLoot());
            desc += "\n";
        }

        String properties = mobProperties( ch );
        if( !properties.isEmpty() ) {
            desc += M.L(AttributeViewer.class, "properties", mobProperties(ch));
            desc += "\n";
        }

        desc += getImmunitiesText(ch);

        return desc;
    }

    public static boolean isAllUppercaseRegex(String str) {
        return str != null && str.matches("[A-Z]+");
    }

    private String lootDisplayRules(String text) {
        switch (text){
            case "RING":
                return Messages.get(this,"random_ring");
            case "POTION":
                return Messages.get(this,"random_potion");
            case "WEAPON":
                return Messages.get(this,"random_weapon");
            case "ARMOR":
                return Messages.get(this,"random_armor");
            case "WAND":
                return Messages.get(this,"random_wand");
            case "SEED":
                return Messages.get(this,"random_seed");
            case "SCROLL":
                return Messages.get(this,"random_scroll");
            case "ARTIFACT":
                return Messages.get(this,"random_artifact");
            case "GOLD":
                return Messages.get(Gold.class,"name");
        }
        return text;
    }


    private String getImmunitiesText(Char ch) {
        HashSet<Class> allImmunities = new HashSet<>();

        if (ch.immunities != null) {
            allImmunities.addAll(ch.immunities);
        }

        for (Char.Property p : ch.properties()) {
            if (p != null && p.immunities() != null) {
                allImmunities.addAll(p.immunities());
            }
        }

        if (allImmunities.isEmpty()) {
            return "";
        }

        String immuneText = "";
        for (Class c : allImmunities) {
            immuneText += getImmunityName(c);
            immuneText += ", ";
        }

        if (!immuneText.isEmpty()) {
            immuneText = immuneText.substring(0, immuneText.length() - 2);
        }

        return "\n" + Messages.get(AttributeViewer.class, "immunities", immuneText);
    }

    private String getImmunityName(Object obj) {
        if (obj == null) return "Unknown";

        if (obj instanceof Char.Property) {
            Char.Property property = (Char.Property) obj;
            return Messages.get(AttributeViewer.class, getMobProperties(property));
        }

        if (obj instanceof Class) {
            Class c = (Class) obj;

            if (Char.Property.class.isAssignableFrom(c)) {
                return c.getSimpleName();
            }

            String name = Messages.get(c, "name");
            if (name == null || name.isEmpty() || name.equals(c.getSimpleName())) {
                return c.getSimpleName();
            }
            return name;
        }

        return obj.toString();
    }

    private String heroAttribute( Char ch ){
        String desc = "";
        if( !( ch instanceof Hero ) )
            return desc;

        desc += "\n\n";
        desc += M.L( AttributeViewer.class, "health", ch.HP, ch.HT );
        desc += "\n";

        int tries = 500;

        int[] damage = new int[ tries ];
        for ( int i = 0; i < tries; ++i ) {
            damage[ i ] = ch.damageRoll();
        }
        float variance = 0f;
        float average = 0f;
        for ( int i = 0; i < tries; ++i ) {
            average += damage[ i ];
        }
        average /= tries;
        for ( int i = 0; i < tries; ++i ) {
            variance += ( damage[ i ] - average ) * ( damage[ i ] - average );
        }
        variance = (float) Math.sqrt( variance / tries );
        desc += M.L( AttributeViewer.class, "damage", average, variance );
        desc += "\n";

        int[] defense = new int[ tries ];
        for ( int i = 0; i < tries; ++i ) {
            defense[ i ] = ch.drRoll();
        }
        variance = 0f;
        average = 0f;
        for ( int i = 0; i < tries; ++i ) {
            average += defense[ i ];
        }
        average /= tries;
        for ( int i = 0; i < tries; ++i ) {
            variance += ( defense[ i ] - average ) * ( defense[ i ] - average );
        }
        variance = (float) Math.sqrt( variance / tries );
        desc += M.L( AttributeViewer.class, "defense", average, variance );
        desc += "\n";

        try {
            desc += M.L( AttributeViewer.class, "accuracy", ch.attackSkill(null) );
            desc += "\n";
        }catch ( NullPointerException ignored ){

        }

        try {
            desc += M.L( AttributeViewer.class, "evasion", ch.defenseSkill(null) );
            desc += "\n";
        }catch ( NullPointerException ignored ){

        }

        desc += M.L( AttributeViewer.class, "move_speed", ch.speed() );
        desc += "\n";

        desc += M.L( AttributeViewer.class, "attack_delay", ((Hero) ch).attackDelay() );
        desc += "\n";

        desc += M.L( AttributeViewer.class, "view_distance", ch.viewDistance );
        desc += "\n";

        return desc;
    }

    private String mobProperties( Char ch ){
        String text = "";
        for( Char.Property p : ch.properties() ) {
            text += Messages.get( AttributeViewer.class, getMobProperties( p ) );
            text += " , ";
        }

        return text.isEmpty() ? text : text.substring( 0, text.length() - 3 );
    }

    private String getMobProperties( Char.Property property ){
        switch ( property ){
            case BOSS:
                return "boss";
            case MINIBOSS:
                return "miniboss";
            case BOSS_MINION:
                return "boss_minion";
            case UNDEAD:
                return "undead";
            case DEMONIC:
                return "demonic";
            case INORGANIC:
                return "inorganic";
            case FIERY:
                return "fiery";
            case ICY:
                return "icy";
            case ACIDIC:
                return "acidic";
            case ELECTRIC:
                return "electric";
            case LARGE:
                return "large";
            case IMMOVABLE:
                return "immovable";
            case NOBIG:
                return "nobig";
            case HUNTER:
                return "hunter";
            case MIMIC:
                return "mimic";
            case HOLLOW:
                return "hollow";
            case PETS:
                return "pets";
            case ABYSS:
                return "abyss";
            case UNKNOWN:
                return "unknown";
            case STATIC:
                return "static";
            case GODCRACK:
                return "godcrack";
            case TUMULUS:
                return "tumulus";
            case UNLESS:
                return "booksoucre";
            default:
                return "";
        }
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_INSPECT);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if(action.equals(AC_INSPECT)){
            GameScene.selectCell(selector);
        }
    }

    public static class WndMobInfo extends WndTitledMessage {
        public WndMobInfo(){
            super(new CharTitle(new Rat()), null);
        }

        public WndMobInfo(Char ch, String message){
            super(new CharTitle(ch), message);
        }
    }

    public static class CharTitle extends Component {

        private static final int GAP = 2;

        private RenderedTextBlock name;
        private HealthBar health;

        public CharTitle(Char ch) {

            name = PixelScene.renderTextBlock(Messages.titleCase(ch.name()), 9);
            name.hardlight(0xFFFF00);
            add(name);

            health = new HealthBar();
            health.level(ch);
            add(health);
        }

        @Override
        protected void layout() {

            name.maxWidth((int) width - GAP*2);
            name.setPos( GAP, GAP);

            health.setRect( GAP, name.bottom() + GAP, width - GAP * 2, health.height());

            height = health.bottom();
        }
    }
}
