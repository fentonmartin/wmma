package at.foobartech.wheremymoneyat.view.viewmodel;

/**
 * @author Thomas Feichtinger
 */

public class OverviewItem {
    private final String name;
    private final int totalAmount;

    public OverviewItem(String name, int totalAmount) {
        this.name = name;
        this.totalAmount = totalAmount;
    }

    public String getName() {
        return name;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}
