package at.foobartech.wheremymoneyat.model;

import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Record extends SugarRecord {

    private int amount;
    private Date date;
    private int month;
    private String note;
    private Category category;

    private List<Tag> tags;

    public Record() {
        //default constructor necessary for SugarORM
    }

    public Record(int amount, Date date, Category category) {
        setAmount(amount);
        setDate(date);
        setCategory(category);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        this.date = date;
        this.month = c.get(Calendar.MONTH);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag) {
        if (this.tags == null) {
            this.tags = new LinkedList<>();
        }
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        if (this.tags == null) {
            return;
        }
        this.tags.remove(tag);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
