/*
клас, який містить в собі всі групи товарів
 */

import java.util.ArrayList;

public class Products {

    private ArrayList<ProductGroup> allGroup = new ArrayList<>();

    public void addGroupToAllGroup(ProductGroup productGroup) {
        boolean exists = false;
        for (ProductGroup pg : allGroup) {
            if (pg.getGroupName().equals(productGroup.getGroupName())) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            allGroup.add(productGroup);
        }
    }


    public void removeGroup(String group) {
        ProductGroup groupToRemove = null;
        for (ProductGroup pg : allGroup) {
            if (pg.getGroupName().equals(group)) {
                groupToRemove = pg;
                break;
            }
        }

        if (groupToRemove != null) {
            allGroup.remove(groupToRemove);
        }
    }


    public ArrayList<ProductGroup> getAllGroupArray() {
        return allGroup;
    }

    public void setAllGroup(ArrayList<ProductGroup> allGroup) {
        this.allGroup = allGroup;
    }

    //пошук пролукту за ім'ям і його повернення
    public Product findProductByName(String productName) {
        for (ProductGroup group : allGroup) {
            for (Product product : group.getSomeGroup()) {
                if (product.getProductName().equals(productName)) {
                    return product;
                }
            }
        }
        return null; // Якщо продукт не знайдено
    }
}
