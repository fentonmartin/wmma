package at.foobartech.wheremymoneyat.view.viewmodel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Thomas Feichtinger
 */

public class DetailItem {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


    public enum Type {
        ITEM, DIVIDER;
    }

    private Type type;

    private int amount;
    private String title;
    private String note;

    private DetailItem(final String title, final int amount, final Type type) {
        this.amount = amount;
        this.title = title;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static DetailItem createDivider(final Date date, final int amount) {
        return new DetailItem(DATE_FORMAT.format(date), amount, Type.DIVIDER);
    }

    public static DetailItem createItem(final String title, final int amount) {
        return new DetailItem(title, amount, Type.ITEM);
    }

    public static DetailItem createItem(final String title, final int amount, final String note) {
        DetailItem detailItem = new DetailItem(title, amount, Type.ITEM);
        detailItem.setNote(note);
        return detailItem;

    }

}
