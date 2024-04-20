/**
 * цей клас був створений для того, щоб задавати поведінку об'єкту і створювати його
 */
public class Product {

    private String productName;
    private String description;
    private String manufacturer;
    private int quantity;
    private double price;
    /*
     назва, опис, виробник, кількість на складі, ціна за одиницю
     */

    public Product(String[] productData) {
        this.productName = productData[0];
        this.description = productData[1];
        this.manufacturer = productData[2];
        this.quantity = Integer.parseInt(productData[3]);
        this.price = Double.parseDouble(productData[4]);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return productName + ";" +
                description + ";" +
                manufacturer + ";" +
                quantity + ";" +
                price;
    }

    public String toSting(String str) {
        return "Назва продукта: " + productName + ", " +
                "Опис: " + description + "\n" + ", " +
                "Назва виробника: " + manufacturer + "\n" + ", " +
                "Кількість на складі: " + quantity + "\n" + ", " +
                "Ціна продукта: " + price + " грн " + "\n";
    }
}