package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public class ExceptionStrings implements Bundlable {

    public String fileName;
    public String result;
    public String exception;
    private static final String FILENAME = "FILENAME";
    private static final String RESULT = "RESULT";
    private static final String EXCEPTION = "EXCEPTION";

    public ExceptionStrings(){
        this(new String(), new String(), new String());
    }

    public ExceptionStrings(String fileName, String result, String exception){
        this.fileName = fileName;
        this.result = result;
        this.exception = exception;
    }
    @Override
    public void restoreFromBundle(Bundle bundle) {
        fileName = bundle.getString(FILENAME);
        result = bundle.getString(RESULT);
        exception = bundle.getString(EXCEPTION);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(FILENAME, fileName);
        bundle.put(RESULT, result);
        bundle.put(EXCEPTION, exception);
    }
}