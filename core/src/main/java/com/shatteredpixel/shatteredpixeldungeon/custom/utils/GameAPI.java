package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameAPI {
    @FunctionalInterface
    public interface Function{
        void func(Object object);
    }

    /*
        Usage Example:
        GameAPI.AddCallbackOnWeaponCreation((Object weapon) -> ((Weapon)weapon).cursed = true);
    */

    private static Map<String , ArrayList<Function>> functions = new HashMap<>();

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

    public static void AddCallbackOnWeaponCreation( Function function ){
        if( !functions.get( "Weapon" ).contains( function ) )
            functions.get( "Weapon" ).add( function );
    }

    public static void AddCallbackOnArmorCreation( Function function ){
        if( !functions.get( "Armor" ).contains( function ) )
            functions.get( "Armor" ).add( function );
    }

    public static void AddCallbackOnArtifactCreation( Function function ){
        if( !functions.get( "Artifact" ).contains( function ) )
            functions.get( "Artifact" ).add( function );
    }

    public static void AddCallbackOnBombCreation( Function function ){
        if( !functions.get( "Bomb" ).contains( function ) )
            functions.get( "Bomb" ).add( function );
    }

    public static void AddCallbackOnGoldCreation( Function function ){
        if( !functions.get( "Gold" ).contains( function ) )
            functions.get( "Gold" ).add( function );
    }

    public static void AddCallbackOnRingCreation( Function function ){
        if( !functions.get( "Ring" ).contains( function ) )
            functions.get( "Ring" ).add( function );
    }

    public static void AddCallbackOnWandCreation( Function function ){
        if( !functions.get( "Wand" ).contains( function ) )
            functions.get( "Wand" ).add( function );
    }

    public static void AddCallbackOnMissileWeaponCreation( Function function ){
        if( !functions.get( "MissileWeapon" ).contains( function ) )
            functions.get( "MissileWeapon" ).add( function );
    }

    public static void AddCallbackOnItemCreation( Function function ){
        if( !functions.get( "Item" ).contains( function ) )
            functions.get( "Item" ).add( function );
    }

    public static void AddCallbackOnLevelPreCreate( Function function ){
        if( !functions.get( "LevelPreCreate" ).contains( function ) )
            functions.get( "LevelPreCreate" ).add( function );
    }

    public static void AddCallbackOnLevelFeelingFinished( Function function ){
        if( !functions.get( "LevelFeelingFinished" ).contains( function ) )
            functions.get( "LevelFeelingFinished" ).add( function );
    }

    public static void AddCallbackOnLevelCreation( Function function ){
        if( !functions.get( "LevelCreation" ).contains( function ) )
            functions.get( "LevelCreation" ).add( function );
    }

    public static void AddCallbackOnActorCreation( Function function ){
        if( !functions.get( "Actor" ).contains( function ) )
            functions.get( "Actor" ).add( function );
    }

    public static void CodeCallback_OnItemCreation( Object item ){
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
        for ( Function f : functions.get( "LevelPreCreate" ) )
            f.func( level );
    }

    public static void CodeCallback_OnLevelFeelingFinished( Object level ){
        for ( Function f : functions.get( "LevelFeelingFinished" ) )
            f.func( level );
    }

    public static void CodeCallback_OnLevelCreation( Object level ){
        for ( Function f : functions.get( "LevelCreation" ) )
            f.func( level );
    }

    public static void CodeCallback_OnActorCreation( Object actor ){
        for ( Function f : functions.get( "Actor" ) )
            f.func( actor );
    }
}
