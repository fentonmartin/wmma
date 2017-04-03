package at.foobartech.wheremymoneyat.view.viewmodel;

/**
 * @author Thomas Feichtinger
 */

public class OverviewItem {

    private final String name;
    private final long categoryId;
    private final int totalAmount;

    public OverviewItem(String name, int totalAmount, long categoryId) {
        this.name = name;
        this.totalAmount = totalAmount;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public long getCategoryId() {
        return categoryId;
    }
}
