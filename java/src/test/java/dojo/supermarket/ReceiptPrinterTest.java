package dojo.supermarket;

import dojo.supermarket.model.*;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

public class ReceiptPrinterTest {

    Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
    Product apples = new Product("apples", ProductUnit.KILO);
    Receipt receipt = new Receipt();

    @Test
    public void oneLineItem() {
        ReceiptItem receiptItem = new ReceiptItem(toothbrush, 1, 0.99, 0.99);
        receipt.addProduct(receiptItem);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void quantityTwo() {
        ReceiptItem receiptItem = new ReceiptItem(toothbrush, 2, 0.99, 0.99 * 2);
        receipt.addProduct(receiptItem);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void looseWeight() {
        ReceiptItem receiptItem = new ReceiptItem(apples, 2.3, 1.99,1.99 * 2.3);
        receipt.addProduct(receiptItem);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void total() {
        ReceiptItem receiptItem1 = new ReceiptItem(toothbrush, 1, 0.99, 2*0.99);
        ReceiptItem receiptItem2 = new ReceiptItem(apples, 0.75, 1.99, 1.99*0.75);
        receipt.addProduct(receiptItem1);
        receipt.addProduct(receiptItem2);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void discounts() {
        receipt.addDiscount(new Discount(apples, "3 for 2", -0.99));
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void printWholeReceipt() {
        ReceiptItem receiptItem1 = new ReceiptItem(toothbrush, 1, 0.99, 0.99);
        ReceiptItem receiptItem2 = new ReceiptItem(toothbrush, 2, 0.99, 2*0.99);
        ReceiptItem receiptItem3 = new ReceiptItem(apples, 0.75, 1.99, 1.99*0.75);

        receipt.addProduct(receiptItem1);
        receipt.addProduct(receiptItem2);
        receipt.addProduct(receiptItem3);
        receipt.addDiscount(new Discount(toothbrush, "3 for 2", -0.99));
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

}
