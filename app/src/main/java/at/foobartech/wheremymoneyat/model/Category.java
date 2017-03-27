package at.foobartech.wheremymoneyat.model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * @author Thomas Feichtinger
 */

public class Category extends SugarRecord {

    @Unique
    private String name;

    public Category() {
        //sugarorm wants an empty constructor
    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
