import java.io.*;
import java.util.ArrayList;

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


    public void setArrayOfProducts(ArrayList<Product> arrayOfProducts) {
        this.arrayOfProducts = arrayOfProducts;
    }

    public void removeProductFromGroup(Product product) {
        boolean removed = arrayOfProducts.remove(product);
        if (!removed) {
            new SuccessAdding();
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

    public void addProductToGroup(Product product) {
        boolean productExists = false;
        for (Product p : arrayOfProducts) {
            if (p.getProductName().equals(product.getProductName())) {
                productExists = true;
                break;
            }
        }
        if (productExists) {
            new ErrorAdding();
        } else {
            arrayOfProducts.add(product);
            addProductToFile(product, file.getPath());
            new SuccessAdding();
        }
    }


    public void addProductToFile(Product product, String directoryPath) {
        File file = new File(directoryPath, this.file.getName()+".txt");

        try {
            // Створюємо вхідний потік для зчитування з файлу
            BufferedReader reader = new BufferedReader(new FileReader(file));
            // Створюємо тимчасовий файл
            File tempFile = new File("tempFile.txt");
            // Створюємо вихідний потік для запису в тимчасовий файл
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean productAdded = false; // Флаг, що показує, чи був вже доданий продукт до файлу

            // Копіюємо вміст файлу в тимчасовий файл, пропускаючи дубльовані дані
            while ((line = reader.readLine()) != null) {
                if (!productAdded && line.equals(product.toString())) {
                    // Якщо стрічка вже існує в файлі, пропускаємо її
                    productAdded = true;
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
            reader.close();

            // Додаємо новий продукт до тимчасового файлу, якщо він ще не був доданий
            if (!productAdded) {
                writer.write(product.toString());
                writer.newLine();
            }

            // Закриваємо потоки
            writer.close();

            // Перезаписуємо вміст основного файлу з тимчасового файлу
            file.delete(); // Видаляємо старий файл
            tempFile.renameTo(file); // Переіменовуємо тимчасовий файл на основний
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return "Назва групи = " + groupName +
                ", Опис = " + description + "\n";
    }
}
