package at.foobartech.wheremymoneyat.model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * @author Thomas Feichtinger
 */

public class Tag extends SugarRecord {

    @Unique
    private String name;

    public Tag() {
        //sugarorm wants an empty constructor
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return name != null ? name.equals(tag.name) : tag.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
