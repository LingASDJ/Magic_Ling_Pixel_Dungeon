package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.badlogic.gdx.Preferences;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 子进程用的只读 Preferences：直接解析父进程 SharedPreferences 写出的 XML。
 * 子进程不是完整的 Android 应用进程，拿不到 SharedPreferences 对象，
 * 但 settings.xml 位于 filesDir/../shared_prefs/，可直接读取。
 * 写入方法为空实现（子进程不修改设置）。
 */
public final class AndroidSharedPreferences implements Preferences {

    private final HashMap<String, Object> values = new HashMap<>();

    public AndroidSharedPreferences(String filesDir) {
        File prefsFile = new File(filesDir, "../shared_prefs/settings.xml");
        load(prefsFile);
    }

    private void load(File file) {
        if (!file.isFile()) return;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc;
            FileInputStream in = new FileInputStream(file);
            try {
                doc = db.parse(in);
            } finally {
                in.close();
            }
            Element root = doc.getDocumentElement();
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node n = children.item(i);
                if (n.getNodeType() != Node.ELEMENT_NODE) continue;
                Element e = (Element) n;
                String name = e.getAttribute("name");
                if (name == null || name.isEmpty()) continue;
                String tag = e.getTagName();
                if ("boolean".equals(tag)) {
                    values.put(name, Boolean.parseBoolean(e.getAttribute("value")));
                } else if ("int".equals(tag)) {
                    values.put(name, Integer.parseInt(e.getAttribute("value")));
                } else if ("long".equals(tag)) {
                    values.put(name, Long.parseLong(e.getAttribute("value")));
                } else if ("float".equals(tag)) {
                    values.put(name, Float.parseFloat(e.getAttribute("value")));
                } else if ("string".equals(tag)) {
                    values.put(name, e.getTextContent());
                }
            }
        } catch (Throwable ignored) {
            //解析失败则保持空表，getXxx 走默认值
        }
    }

    private Object get(String key) {
        return values.get(key);
    }

    @Override
    public Preferences putBoolean(String key, boolean val) { return this; }

    @Override
    public Preferences putInteger(String key, int val) { return this; }

    @Override
    public Preferences putLong(String key, long val) { return this; }

    @Override
    public Preferences putFloat(String key, float val) { return this; }

    @Override
    public Preferences putString(String key, String val) { return this; }

    @Override
    public Preferences put(Map<String, ?> vals) { return this; }

    @Override
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        Object v = get(key);
        return v instanceof Boolean ? (Boolean) v : defValue;
    }

    @Override
    public int getInteger(String key) {
        return getInteger(key, 0);
    }

    @Override
    public int getInteger(String key, int defValue) {
        Object v = get(key);
        return v instanceof Integer ? (Integer) v : defValue;
    }

    @Override
    public long getLong(String key) {
        return getLong(key, 0L);
    }

    @Override
    public long getLong(String key, long defValue) {
        Object v = get(key);
        return v instanceof Long ? (Long) v : defValue;
    }

    @Override
    public float getFloat(String key) {
        return getFloat(key, 0f);
    }

    @Override
    public float getFloat(String key, float defValue) {
        Object v = get(key);
        return v instanceof Float ? (Float) v : defValue;
    }

    @Override
    public String getString(String key) {
        return getString(key, "");
    }

    @Override
    public String getString(String key, String defValue) {
        Object v = get(key);
        return v instanceof String ? (String) v : defValue;
    }

    @Override
    public Map<String, ?> get() {
        return values;
    }

    @Override
    public boolean contains(String key) {
        return values.containsKey(key);
    }

    @Override
    public void clear() {}

    @Override
    public void remove(String key) {}

    @Override
    public void flush() {}
}
