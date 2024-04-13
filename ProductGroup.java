import java.io.*;
import java.util.ArrayList;

public class ProductGroup {

    private ArrayList<Product> arrayOfProducts = new ArrayList<>();
    private String description;
    private String groupName;
    File file;

    ProductGroup(String path, String[] data) {
        this.groupName = data[0];
        this.description = data[1];
        file = new File(path);
        file.mkdir();
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

    public void removeProductFromGroup(Product product) throws IOException {
        boolean removed = arrayOfProducts.remove(product);
        removeSomeProductInFile(product);
        if (!removed) {
            new SuccessProductAdding();
        }
    }

    public void editGroup(String groupName, String newGroupName, String newDescription) {
        if (this.groupName.equals(groupName)) {
            this.groupName = newGroupName;
            this.description = newDescription;
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

    public void addProductToGroup(Product product, Products allGroups) throws IOException {
        boolean productExists = false;

        // Перебираємо всі групи товарів
        for (ProductGroup group : allGroups.getAllGroupArray()) {
            // Перебираємо всі продукти в групі
            for (Product p : group.getArrayOfProducts()) {
                if (p.getProductName().equals(product.getProductName())) {
                    // Якщо знайдено товар з такою ж назвою, встановлюємо прапорець, що товар вже існує
                    productExists = true;
                    break;
                }
            }
            // Якщо знайдено товар з такою ж назвою, виходимо з циклу
            if (productExists) {
                break;
            }
        }

        // Якщо товар не існує, додаємо його до групи
        if (productExists) {
            new ErrorProductAdding();
        } else {
            arrayOfProducts.add(product);
            addProductToFile(product, file.getPath());
            new SuccessProductAdding();
        }
    }


    public void addProductToFile(Product product, String directoryPath) throws IOException {
        File file = new File(directoryPath, this.file.getName() + ".txt");
        file.createNewFile();

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


    public void removeSomeProductInFile(Product product) throws IOException {
        // Отримуємо шлях до файлу
        String filePath = file.getPath() + "\\" + file.getName() + ".txt";
        System.out.println(filePath);

        // Перевіряємо, чи файл існує
        File originalFile = new File(filePath);

        // Створюємо об'єкт для читання з файлу
        BufferedReader reader = new BufferedReader(new FileReader(originalFile));

        // Створюємо тимчасовий файл, в який будемо записувати вміст без видаленого продукту
        File tempFile = new File(file.getPath() + "\\" + "temp.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        // Зчитуємо вміст файлу по рядках
        String line;
        while ((line = reader.readLine()) != null) {
            // Розбиваємо рядок на токени
            String[] tokens = line.split(";");
            // Перевіряємо, чи перший токен відповідає назві продукту
            if (!tokens[0].equals(product.getProductName())) {
                // Якщо ні, то записуємо рядок у тимчасовий файл
                writer.write(line);
                writer.newLine();
            }
        }

        // Закриваємо ридера та письменника
        reader.close();
        writer.close();

        // Видаляємо оригінальний файл
        originalFile.delete();

        // Переписуємо вміст тимчасового файлу в початковий файл
        tempFile.renameTo(originalFile);


        // Видаляємо тимчасовий файл
        tempFile.delete();
    }


    public Product getProductByName(String productName) {
        for (Product product : arrayOfProducts) {
            if (product.getProductName().equals(productName)) {
                return product;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Назва групи = " + groupName +
                ", Опис = " + description + "\n";
    }
}
