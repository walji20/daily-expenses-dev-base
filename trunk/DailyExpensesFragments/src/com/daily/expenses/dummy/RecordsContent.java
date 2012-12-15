package com.daily.expenses.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class RecordsContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Record> ITEMS = new ArrayList<Record>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Record> ITEM_MAP = new HashMap<String, Record>();

    static {
        // Add 3 sample items.
        addRecord(new Record("1", "Item 1", "An item is a real complex thing. it can be little or small .. or long .. or ............................................................asd a sd asd as"));
        addRecord(new Record("2", "Item 2", "Android :) "));
        addRecord(new Record("3", "Item 3"));
        addRecord(new Record("1", "Item 1", "An item is a real complex thing. it can be little or small .. or long .. or ............................................................asd a sd asd as"));
        addRecord(new Record("2", "Item 2", "Android :) "));
    }
    private static void addRecord(Record record) {
        ITEMS.add(record);
        ITEM_MAP.put(record.id, record);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Record {
        public String id;
        public String title;
        public String text;

        public Record(String id, String content) {
            this.id = id;
            this.title = content;
            this.text = null;
        }
        
        public Record(String id, String content, String text) {
        	this.id = id;
        	this.title = content;
        	this.text = text;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
