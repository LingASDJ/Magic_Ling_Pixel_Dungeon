package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameAPI {
    @FunctionalInterface
    public interface Function{
        void func(Object object);
    }

    /*
        简单用例:
            GameAPI.Function curseWeaponFunction = (Object weapon) -> ((Weapon) weapon).cursed = true;
            GameAPI.AddCallbackOnWeaponCreation(curseWeaponFunction);
            GameAPI.RemoveCallbackOnWeaponCreation(curseWeaponFunction);
    */

    private static Map<String , ArrayList<Function>> functions = new HashMap<>();

    private ArrayList<Class<?>> bannedItemList = new ArrayList<>();
    private boolean shouldWeaponCursed;
    private boolean shouldArmorCursed;
    private ArrayList<Class<?>> bannedWeaponEnchantList = new ArrayList<>();
    private ArrayList<Class<?>> bannedCurseWeaponEnchantList = new ArrayList<>();
    private ArrayList<Class<?>> bannedArmorEnchantList = new ArrayList<>();
    private ArrayList<Class<?>> bannedCurseArmorEnchantList = new ArrayList<>();
    private Level.Feeling customLevelFeeling;
    private ArrayList<Class<?>> bannedMobList = new ArrayList<>();
    private ArrayList<Class<?>> bannedTrapList = new ArrayList<>();
    private ArrayList<Class<?>> bannedNpcList = new ArrayList<>();
    private boolean customLevelNoWater;
    private boolean customLevelNoGrass;
    private boolean customLevelNoChasm;

    public static void initGameAPI(){
        functions.put("Weapon",new ArrayList<>());
        functions.put("Armor",new ArrayList<>());
        functions.put("Artifact",new ArrayList<>());
        functions.put("Bomb",new ArrayList<>());
        functions.put("Gold",new ArrayList<>());
        functions.put("Ring",new ArrayList<>());
        functions.put("Wand",new ArrayList<>());
        functions.put("MissileWeapon",new ArrayList<>());
        functions.put("Item",new ArrayList<>());
        functions.put("LevelPreCreate",new ArrayList<>());
        functions.put("LevelFeelingFinished",new ArrayList<>());
        functions.put("LevelCreation",new ArrayList<>());
        functions.put("Actor",new ArrayList<>());
    }

    public ArrayList<Class<?>> getBanedItemList() {
        return bannedItemList;
    }

    public void setBanedItemList( ArrayList<Class<?>> BannedItemList ) {
        for ( Class<?> item : BannedItemList ) {
            if ( bannedItemList.contains( item ) )
                continue;
            bannedItemList.add( item );
        }
    }

    public boolean getShouldWeaponCursed() {
        return shouldWeaponCursed;
    }

    public void setShouldWeaponCursed(boolean ShouldWeaponCursed ) {
        shouldWeaponCursed = ShouldWeaponCursed;
    }

    public boolean getShouldArmorCursed() {
        return shouldArmorCursed;
    }

    public void setBanedItemList(boolean ShouldArmorCursed ) {
        shouldArmorCursed = ShouldArmorCursed;
    }

    public ArrayList<Class<?>> getBannedWeaponEnchantList() {
        return bannedWeaponEnchantList;
    }

    public void setBanedWeaponEnchantList( ArrayList<Class<?>> BannedWeaponEnchantList ) {
        for ( Class<?> enchat : BannedWeaponEnchantList ) {
            if ( bannedWeaponEnchantList.contains( enchat ) )
                continue;
            bannedWeaponEnchantList.add(enchat);
        }
    }

    public ArrayList<Class<?>> getBannedCurseWeaponEnchantList() {
        return bannedCurseWeaponEnchantList;
    }

    public void setBanedCurseWeaponEnchantList( ArrayList<Class<?>> BannedCurseWeaponEnchantList ) {
        for ( Class<?> enchat : BannedCurseWeaponEnchantList ) {
            if ( bannedCurseWeaponEnchantList.contains( enchat ) )
                continue;
            bannedCurseWeaponEnchantList.add(enchat);
        }
    }

    public ArrayList<Class<?>> getBannedArmorEnchantList() {
        return bannedArmorEnchantList;
    }

    public void setBanedArmorEnchantList( ArrayList<Class<?>> BannedArmorEnchantList ) {
        for ( Class<?> enchat : BannedArmorEnchantList ) {
            if ( bannedArmorEnchantList.contains( enchat ) )
                continue;
            bannedArmorEnchantList.add( enchat );
        }
    }

    public ArrayList<Class<?>> getBannedCurseArmorEnchantList() {
        return bannedCurseArmorEnchantList;
    }

    public void setBanedCurseArmorEnchantList( ArrayList<Class<?>> BannedCurseArmorEnchantList ) {
        for ( Class<?> enchat : BannedCurseArmorEnchantList ) {
            if ( bannedCurseArmorEnchantList.contains( enchat ) )
                continue;
            bannedCurseArmorEnchantList.add( enchat );
        }
    }

    public Level.Feeling getCustomLevelFeeling() {
        return customLevelFeeling;
    }

    public void setCustomLevelFeeling(Level.Feeling CustomLevelFeeling ) {
        customLevelFeeling = CustomLevelFeeling;
    }

    public ArrayList<Class<?>> getBannedMobList() {
        return bannedMobList;
    }

    public void setBanedMobList( ArrayList<Class<?>> BanedMobList ) {
        for ( Class<?> mob : BanedMobList ) {
            if ( bannedMobList.contains( mob ) )
                continue;
            bannedMobList.add( mob );
        }
    }

    public ArrayList<Class<?>> getBannedTrapList() {
        return bannedTrapList;
    }

    public void setBanedTrapList( ArrayList<Class<?>> BanedTrapList ) {
        for ( Class<?> trap : BanedTrapList ) {
            if ( bannedTrapList.contains( trap ) )
                continue;
            bannedTrapList.add( trap );
        }
    }

    public ArrayList<Class<?>> getBannedNpcList() {
        return bannedNpcList;
    }

    public void setBanedNpcList( ArrayList<Class<?>> BanedNpcList ) {
        for ( Class<?> npc : BanedNpcList ) {
            if ( bannedNpcList.contains( npc ) )
                continue;
            bannedNpcList.add( npc );
        }
    }

    public boolean getCustomLevelNoWater() {
        return customLevelNoWater;
    }

    public void setCustomLevelNoWater(boolean CustomLevelNoWater ) {
        customLevelNoWater = CustomLevelNoWater;
    }

    public boolean getCustomLevelNoChasm() {
        return customLevelNoChasm;
    }

    public void setCustomLevelNoChasm(boolean CustomLevelNoChasm ) {
        customLevelNoChasm = CustomLevelNoChasm;
    }

    public boolean getCustomLevelNoGrass() {
        return customLevelNoGrass;
    }

    public void setCustomLevelNoGrass(boolean CustomLevelNoGrass ) {
        customLevelNoGrass = CustomLevelNoGrass;
    }

    public void storeInBundle(Bundle bundle){
        bundle.put( "customseed_banneditemlist", bannedItemList.toArray(new Class[0]) );
        bundle.put( "customseed_shouldweaponcursed", shouldWeaponCursed );
        bundle.put( "customseed_shouldarmorcursed", shouldArmorCursed );
        bundle.put( "customseed_bannedweaponenchantlist", bannedWeaponEnchantList.toArray( new Class[0] ) );
        bundle.put( "customseed_bannedcurseweaponenchantlist", bannedCurseWeaponEnchantList.toArray( new Class[0] ) );
        bundle.put( "customseed_bannedarmorenchantlist", bannedArmorEnchantList.toArray( new Class[0] ) );
        bundle.put( "customseed_bannedcursearmorenchantlist", bannedCurseArmorEnchantList.toArray( new Class[0] ) );
        bundle.put( "customseed_customlevelfeeling", customLevelFeeling );
        bundle.put( "customseed_bannedmoblist", bannedMobList.toArray( new Class[0] ) );
        bundle.put( "customseed_bannedtraplist", bannedTrapList.toArray( new Class[0] ) );
        bundle.put( "customseed_bannednpclist", bannedNpcList.toArray( new Class[0] ) );
        bundle.put( "customseed_customlevelnowater", customLevelNoWater );
        bundle.put( "customseed_customlevelnograss", customLevelNoGrass );
        bundle.put( "customseed_customlevelnochasm", customLevelNoGrass );
    }

    public void restoreFromBundle( Bundle bundle ){
        for (Class<?> BannedItem : bundle.getClassArray("customseed_banneditemlist"))
            bannedItemList.add(BannedItem);

        shouldWeaponCursed = bundle.getBoolean( "customseed_shouldweaponcursed" );

        shouldArmorCursed = bundle.getBoolean( "customseed_shouldarmorcursed" );

        for (Class<?> BannedWeaponEnchant : bundle.getClassArray("customseed_bannedweaponenchantlist"))
            bannedWeaponEnchantList.add(BannedWeaponEnchant);

        for (Class<?> BannedCurseWeaponEnchant : bundle.getClassArray("customseed_bannedcurseweaponenchantlist"))
            bannedCurseWeaponEnchantList.add(BannedCurseWeaponEnchant);

        for (Class<?> BannedArmorEnchant : bundle.getClassArray("customseed_bannedarmorenchantlist"))
            bannedArmorEnchantList.add(BannedArmorEnchant);

        for (Class<?> BannedCurseArmorEnchant : bundle.getClassArray("customseed_bannedcursearmorenchantlist"))
            bannedCurseArmorEnchantList.add(BannedCurseArmorEnchant);

        customLevelFeeling = bundle.getEnum( "customseed_customlevelfeeling", Level.Feeling.class );

        for (Class<?> BannedMob : bundle.getClassArray("customseed_bannedmoblist"))
            bannedMobList.add(BannedMob);

        for (Class<?> BannedItemList : bundle.getClassArray("customseed_bannedtraplist"))
            bannedTrapList.add(BannedItemList);

        for (Class<?> BannedItemList : bundle.getClassArray("customseed_bannednpclist"))
            bannedNpcList.add(BannedItemList);

        customLevelNoWater = bundle.getBoolean( "customseed_customlevelnowater" );

        customLevelNoGrass = bundle.getBoolean( "customseed_customlevelnograss" );

        customLevelNoGrass = bundle.getBoolean( "customseed_customlevelnochasm" );
    }

    public static void AddCallbackOnWeaponCreation( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "Weapon" ).contains( function ) )
            functions.get( "Weapon" ).add( function );
    }

    public static void RemoveCallbackOnWeaponCreation( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "Weapon" ).remove( function );
    }

    public static void AddCallbackOnArmorCreation( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "Armor" ).contains( function ) )
            functions.get( "Armor" ).add( function );
    }

    public static void RemoveCallbackOnArmorCreation( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "Armor" ).remove( function );
    }

    public static void AddCallbackOnArtifactCreation( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "Artifact" ).contains( function ) )
            functions.get( "Artifact" ).add( function );
    }

    public static void RemoveCallbackOnArtifactCreation( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "Artifact" ).remove( function );
    }

    public static void AddCallbackOnBombCreation( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "Bomb" ).contains( function ) )
            functions.get( "Bomb" ).add( function );
    }

    public static void RemoveCallbackOnBombCreation( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "Bomb" ).remove( function );
    }

    public static void AddCallbackOnGoldCreation( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "Gold" ).contains( function ) )
            functions.get( "Gold" ).add( function );
    }

    public static void RemoveCallbackOnGoldCreation( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "Gold" ).remove( function );
    }

    public static void AddCallbackOnRingCreation( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "Ring" ).contains( function ) )
            functions.get( "Ring" ).add( function );
    }

    public static void RemoveCallbackOnRingCreation( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "Ring" ).remove( function );
    }

    public static void AddCallbackOnWandCreation( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "Wand" ).contains( function ) )
            functions.get( "Wand" ).add( function );
    }

    public static void RemoveCallbackOnWandCreation( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "Wand" ).remove( function );
    }

    public static void AddCallbackOnMissileWeaponCreation( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "MissileWeapon" ).contains( function ) )
            functions.get( "MissileWeapon" ).add( function );
    }

    public static void RemoveCallbackOnMissileWeaponCreation( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "MissileWeapon" ).remove( function );
    }

    public static void AddCallbackOnItemCreation( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "Item" ).contains( function ) )
            functions.get( "Item" ).add( function );
    }

    public static void RemoveCallbackOnItemCreation( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "Item" ).remove( function );
    }

    public static void AddCallbackOnLevelPreCreate( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "LevelPreCreate" ).contains( function ) )
            functions.get( "LevelPreCreate" ).add( function );
    }

    public static void RemoveCallbackOnLevelPreCreate( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "LevelPreCreate" ).remove( function );
    }

    public static void AddCallbackOnLevelFeelingFinished( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "LevelFeelingFinished" ).contains( function ) )
            functions.get( "LevelFeelingFinished" ).add( function );
    }

    public static void RemoveCallbackOnLevelFeelingFinished( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "LevelFeelingFinished" ).remove( function );
    }

    public static void AddCallbackOnLevelCreation( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "LevelCreation" ).contains( function ) )
            functions.get( "LevelCreation" ).add( function );
    }

    public static void RemoveCallbackOnLevelCreation( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "LevelCreation" ).remove( function );
    }

    public static void AddCallbackOnActorCreation( Function function ){
        if( functions.isEmpty() )
            return;

        if( !functions.get( "Actor" ).contains( function ) )
            functions.get( "Actor" ).add( function );
    }

    public static void RemoveCallbackOnActorCreation( Function function ){
        if( functions.isEmpty() )
            return;

        functions.get( "Actor" ).remove( function );
    }

    public static void CodeCallback_OnItemCreation( Object item ){
        if( functions.isEmpty() )
            return;

        if( item instanceof MissileWeapon ){
            for ( Function f : functions.get( "MissileWeapon" ) )
                f.func( item );
        }else if( item instanceof Armor){
            for ( Function f : functions.get( "Armor" ) )
                f.func( item );
        }else if( item instanceof Artifact){
            for ( Function f : functions.get( "Artifact" ) )
                f.func( item );
        }else if( item instanceof Bomb){
            for ( Function f : functions.get( "Bomb" ) )
                f.func( item );
        }else if( item instanceof Gold){
            for ( Function f : functions.get( "Gold" ) )
                f.func( item );
        }else if( item instanceof Ring){
            for ( Function f : functions.get( "Ring" ) )
                f.func( item );
        }else if( item instanceof Wand){
            for ( Function f : functions.get( "Wand" ) )
                f.func( item );
        }else if( item instanceof Weapon){
            for ( Function f : functions.get( "Weapon" ) )
                f.func( item );
        }else {
            for ( Function f : functions.get( "Item" ) )
                f.func( item );
        }
    }

    public static void CodeCallback_OnLevelPreCreate( Object level ){
        if( functions.isEmpty() )
            return;

        for ( Function f : functions.get( "LevelPreCreate" ) )
            f.func( level );
    }

    public static void CodeCallback_OnLevelFeelingFinished( Object level ){
        if( functions.isEmpty() )
            return;

        for ( Function f : functions.get( "LevelFeelingFinished" ) )
            f.func( level );
    }

    public static void CodeCallback_OnLevelCreation( Object level ){
        if( functions.isEmpty() )
            return;

        for ( Function f : functions.get( "LevelCreation" ) )
            f.func( level );
    }

    public static void CodeCallback_OnActorCreation( Object actor ){
        if( functions.isEmpty() )
            return;

        for ( Function f : functions.get( "Actor" ) )
            f.func( actor );
    }
}
