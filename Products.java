/*
клас, який містить в собі всі групи товарів
 */

import java.io.File;
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
            new SuccessGroupAdding();
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
            removeGroupFromFile(groupToRemove);
            allGroup.remove(groupToRemove);
        }
    }

    public void removeGroupFromFile(ProductGroup pg) {
        // Отримуємо файл групи
        File groupFile = pg.getFile();

        // Перевіряємо, чи файл існує та чи є він директорією
        if (groupFile.exists() && groupFile.isDirectory()) {
            // Отримуємо список файлів у директорії
            File[] files = groupFile.listFiles();

            // Перевіряємо кожен файл у директорії
            if (files != null) {
                for (File file : files) {
                    // Перевіряємо, чи файл є текстовим файлом і видаляємо його
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }

            // Видаляємо саму директорію (групу)
            if (groupFile.delete()) {
                new SuccessGroupDeleting();
            } else {
                new ErrorGroupDeleting();
            }
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

    public ProductGroup findGroupByName(String groupName) {
        for (ProductGroup group : allGroup) {
            if (group.getGroupName().equals(groupName)) {
                return group;
            }
        }
        return null; // Якщо групу не знайдено
    }

    public static double getTotalStockValue(Products allGroups) {
        double totalValue = 0.0;

        // Перебираємо всі групи товарів
        for (ProductGroup group : allGroups.getAllGroupArray()) {
            // Перебираємо всі продукти у групі
            for (Product product : group.getArrayOfProducts()) {
                double price = product.getPrice();
                int quantity = product.getQuantity();
                totalValue += price * quantity; // Додаємо вартість цього товару до загальної вартості
            }
        }

        return totalValue;
    }

    // Отримання загальної вартості товарів у заданій групі
    public static double getTotalGroupValue(ProductGroup group) {
        double totalValue = 0.0;

        // Перебираємо всі продукти у групі
        for (Product product : group.getArrayOfProducts()) {
            double price = product.getPrice();
            int quantity = product.getQuantity();
            totalValue += price * quantity; // Додаємо вартість цього товару до загальної вартості
        }

        return totalValue;
    }

}