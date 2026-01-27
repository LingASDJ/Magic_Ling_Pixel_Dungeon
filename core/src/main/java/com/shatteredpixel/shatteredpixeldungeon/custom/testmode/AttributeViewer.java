package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
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

        String properties = mobProperties( ch );
        if( !properties.isEmpty() ) {
            desc += M.L(AttributeViewer.class, "properties", mobProperties(ch));
            desc += "\n";
        }

        desc += getImmunitiesText(ch);

        return desc;
    }

    private String getImmunitiesText(Char ch) {
        // 1. 创建一个集合来存放所有的免疫 Class
        HashSet<Class> allImmunities = new HashSet<>();

        // 2. 添加角色自身的免疫
        if (ch.immunities != null) {
            allImmunities.addAll(ch.immunities);
        }

        // 3. 遍历角色的所有属性，添加属性自带的免疫
        for (Char.Property p : ch.properties()) {
            if (p != null && p.immunities() != null) {
                allImmunities.addAll(p.immunities());
            }
        }

        // 4. 如果没有免疫，返回空字符串
        if (allImmunities.isEmpty()) {
            return "";
        }

        // 5. 生成文本
        String immuneText = "";
        for (Class c : allImmunities) {
            immuneText += getImmunityName(c);
            immuneText += ", ";
        }

        // 移除末尾的 ", "
        if (!immuneText.isEmpty()) {
            immuneText = immuneText.substring(0, immuneText.length() - 2);
        }

        // 6. 添加标签
        return "\n" + Messages.get(AttributeViewer.class, "immunities", immuneText);
    }

    // 【完善方法】将 Class 或 Property 转换为可读的名称
    private String getImmunityName(Object obj) {
        if (obj == null) return "Unknown";

        // 如果是 Char.Property 枚举
        if (obj instanceof Char.Property) {
            Char.Property property = (Char.Property) obj;
            // 复用之前的 getMobProperties 逻辑来获取属性名称
            return Messages.get(AttributeViewer.class, getMobProperties(property));
        }

        // 如果是 Class 对象
        if (obj instanceof Class) {
            Class c = (Class) obj;

            // 特殊处理：如果 Class 是 Char.Property 的子类（虽然通常 Enum 不会这样传，但为了健壮性）
            if (Char.Property.class.isAssignableFrom(c)) {
                // 这里不太可能走到，因为 Enum 是单例，通常传的是枚举实例
                return c.getSimpleName();
            }

            // 尝试使用 Messages 获取本地化名称
            String name = Messages.get(c, "name");
            // 如果获取失败（例如返回的是类名），则使用简单类名
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
