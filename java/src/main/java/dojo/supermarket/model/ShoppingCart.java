package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    private final Map<Product, Double> productQuantities = new HashMap<>();

    List<ProductQuantity> getItems() {
        return Collections.unmodifiableList(items);
    }

    void addItem(Product product) {
        addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
        return Collections.unmodifiableMap(productQuantities);
    }

    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product p : productQuantities().keySet()) {
            double quantity = productQuantities.get(p);
            if (offers.containsKey(p)) {
                Offer offer = offers.get(p);
                double unitPrice = catalog.getUnitPrice(p);
                int quantityAsInt = (int) quantity;
                Discount discount = null;
                switch (offer.offerType) {
                    case TWO_FOR_AMOUNT:
                        discount = getTwoForAmountDiscount(p, quantity, offer, unitPrice, discount);
                        break;
                    case THREE_FOR_TWO:
                        if (quantityAsInt > 2) {
                            discount = getThreeForTwoDiscount(p, quantity, unitPrice, quantityAsInt);
                        }
                        break;
                    case TEN_PERCENT_DISCOUNT:
                        discount = getTenPercentDiscount(p, quantity, offer, unitPrice);
                        break;
                    case FIVE_FOR_AMOUNT:
                        if (quantityAsInt >= 5) {
                            discount = getFiveForAmountDiscount(p, quantity, offer, unitPrice, quantityAsInt);
                        }
                        break;
                    default:
                        break;
                }
                if (discount != null) {
                    receipt.addDiscount(discount);
                }
            }
        }
    }

    private Discount getFiveForAmountDiscount(Product p, double quantity, Offer offer, double unitPrice,
            int quantityAsInt) {
        Discount discount;
        double discountTotal = unitPrice * quantity
                - (offer.argument * quantityAsInt + quantityAsInt % 5 * unitPrice);
        discount = new Discount(p, 1 + " for " + offer.argument, -discountTotal);
        return discount;
    }

    private Discount getTenPercentDiscount(Product p, double quantity, Offer offer, double unitPrice) {
        Discount discount;
        discount = new Discount(p, offer.argument + "% off",
                -quantity * unitPrice * offer.argument / 100.0);
        return discount;
    }

    private Discount getThreeForTwoDiscount(Product p, double quantity, double unitPrice, int quantityAsInt) {
        Discount discount;
        double discountAmount = quantity * unitPrice
                - ((quantityAsInt * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
        discount = new Discount(p, "3 for 2", -discountAmount);
        return discount;
    }

    private Discount getTwoForAmountDiscount(Product p, double quantity, Offer offer, double unitPrice,
            Discount discount) {
        int quantityAsInt = (int) quantity;
        if (quantityAsInt >= 2) {
            int intDivision = quantityAsInt / 2;
            double pricePerUnit = offer.argument * intDivision;
            double theTotal = (quantityAsInt % 2) * unitPrice;
            double total = pricePerUnit + theTotal;
            double discountN = unitPrice * quantity - total;
            return new Discount(p, "2 for " + offer.argument, -discountN);
        }
        return discount;
    }

}
