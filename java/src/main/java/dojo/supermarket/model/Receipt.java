package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Receipt {

    private final List<ReceiptItem> items = new ArrayList<>();
    private final List<Discount> discounts = new ArrayList<>();

    public double getTotalPrice() {
        double total = items.stream().mapToDouble(ReceiptItem::getTotalPrice).sum()
                + discounts.stream().mapToDouble(Discount::getDiscountAmount).sum();
        return total;
    }

    public void addProduct(ReceiptItem receiptItem) {
        items.add(receiptItem);
    }

    public List<ReceiptItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }
}
