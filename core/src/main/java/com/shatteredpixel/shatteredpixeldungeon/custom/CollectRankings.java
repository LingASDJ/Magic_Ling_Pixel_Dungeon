package com.shatteredpixel.shatteredpixeldungeon.custom;

import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import org.luaj.vm2.ast.Str;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public enum CollectRankings {

    INSTANCE;

    public static final int TABLE_SIZE = 20;

    public static final String COLLECT_RANKINGS_FILE = "collect_rankings.dat";

    private static final String RECORDS = "records";
    private static final String LATEST = "latest";
    private static final String TOTAL = "total";
    public LinkedHashMap<String,Rankings.Record> records;;
    public String lastRecord;
    public int totalNumber;

    public void collectRecord(Rankings.Record record){
        if (records == null) {
            records = new LinkedHashMap<String, Rankings.Record>();
        }

        if( records.containsKey( record.gameID ) )
            return;

        if( records.size() >= TABLE_SIZE ){
            Iterator< String > iterator = records.keySet().iterator();
            String lastKey = null;
            while ( iterator.hasNext() ) {
                lastKey = iterator.next();
            }
            records.remove( lastKey );
        }

        records.put( record.gameID, record );
        lastRecord = record.gameID;

        save();
    }

    public void removeRecord(Rankings.Record record){
        if (records == null) {
            return;
        }

        records.remove(record.gameID);
    }

    public boolean isRecordCollected(Rankings.Record record){
        return records.containsKey( record.gameID );
    }

    public void save() {
        Bundle bundle = new Bundle();

        if (records != null) {
            for (Rankings.Record record : records.values()) {
                bundle.put(RECORDS, record);
            }
        }
        bundle.put(LATEST, lastRecord);
        bundle.put(TOTAL, totalNumber);

        try {
            FileUtils.bundleToFile(COLLECT_RANKINGS_FILE, bundle);
        } catch (IOException e) {
            ShatteredPixelDungeon.reportException(e);
        }

    }

    public void load() {

        if (records != null) {
            return;
        }

        //if( !FileUtils.getFileHandle (COLLECT_RANKINGS_FILE ).exists() )
        //    save();

        records = new LinkedHashMap<String, Rankings.Record>();

        try {
            Bundle bundle = FileUtils.bundleFromFile( COLLECT_RANKINGS_FILE );

            for (Bundlable bundlable : bundle.getCollection( RECORDS )) {
                Rankings.Record record = (Rankings.Record) bundlable;
                records.put( record.gameID, record );
            }
            lastRecord = bundle.getString( LATEST );

            totalNumber = bundle.getInt( TOTAL );
            if (totalNumber == 0) {
                totalNumber = records.size();
            }

        } catch (IOException e) {
        }
    }
}
