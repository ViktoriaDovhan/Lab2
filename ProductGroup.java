import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProductGroup {

    private ArrayList<Product> arrayOfProducts = new ArrayList<>();
    private String description;
    private String groupName;
    File file;

    ProductGroup(String path, String description, String groupName) {
        this.description = description;
        this.groupName = groupName;
        file = new File(path);
    }

    public ArrayList<Product> getSomeGroup() {
        return arrayOfProducts;
    }

    public ArrayList<Product> getArrayOfProducts() {
        return arrayOfProducts;
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setSomeGroup(ArrayList<Product> someGroup) {
        this.arrayOfProducts = someGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void addProductToGroup(Product product) {
        boolean productExists = false;
        for (Product p : arrayOfProducts) {
            if (p.getProductName().equals(product.getProductName())) {
                productExists = true;
                break;
            }
        }
        if (productExists) {
            new ErrorProductExists();
        } else {
            arrayOfProducts.add(product);
        }
    }

    public void setArrayOfProducts(ArrayList<Product> arrayOfProducts) {
        this.arrayOfProducts = arrayOfProducts;
    }

    public void removeProductFromGroup(Product product) {
        boolean removed = arrayOfProducts.remove(product);
        if (!removed) {
            new ErrorProductExists();
        }
    }

    public void editGroup(String groupName, String newGroupName, String newDescription) {
        if (this.groupName.equals(groupName)) {
            this.groupName = newGroupName;
            this.description = newDescription;
        }
    }


    public void createDirectory() {
        String directoryPath = "C:\\Users\\vika\\Desktop\\Shopping";
        String fileName = groupName;

        File directory = new File(directoryPath);
        File file = new File(directory, fileName);

        try {
            if (!file.exists()) {
                boolean isCreated = file.createNewFile();

                if (isCreated) {
                    System.out.println("Файл успішно створено.");
                } else {
                    System.out.println("Не вдалося створити файл.");
                }
            } else {
                System.out.println("Файл вже існує.");
            }
        } catch (IOException e) {
            System.out.println("Помилка при спробі створити файл: " + e.getMessage());
        }
    }

    /*
    видаляємо директорію з пролуктами
     */
    public static void deleteDirectory(File directory) {
        if (directory.exists()) {
            ArrayList<File> files = new ArrayList<>();
            for (File file : directory.listFiles()) {
                files.add(file);
            }

            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file); // Рекурсивно видаляємо піддиректорії
                } else {
                    file.delete(); // Видаляємо файл
                }
            }
            directory.delete(); // Видаляємо саму директорію
        }
    }

    //видаляємо продукт з групи
    public void deleteProductFile(String productName) {
        String directoryPath = "C:\\Users\\vika\\Desktop\\Shopping";
        String fileName = productName + ".txt"; // Припускаємо, що файли продуктів мають розширення ".txt"

        File directory = new File(directoryPath);
        File file = new File(directory, fileName);

        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Файл " + fileName + " успішно видалено.");
            } else {
                System.out.println("Не вдалося видалити файл " + fileName + ".");
            }
        } else {
            System.out.println("Файл " + fileName + " не існує.");
        }
    }

    @Override
    public String toString() {
        return "Опис = " + description +
                ", Назва групи = " + groupName + "\n";
    }
}
