import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class UserInterface extends JFrame {
    private JTabbedPane tabbedPane;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem deleteProduct;
    private JMenuItem deleteGroupOfProducts;

    private JMenuItem addProduct;
    private JMenuItem addGroupOfProducts;

    private JMenuItem editProduct;
    private JMenuItem editGroupOfProducts;
    private JMenuItem lookFor;
    private JMenuItem showStaticData;
    String[] one = {"Непродовольчі товари", "Класні товари"};
    String[] two = {"Продовольчі товари", "Хороші товари"};


    //заповнюємо масив прод і непрод СТРІЧОК

    Products allGroup = new Products();
    ProductGroup neProd = new ProductGroup("C:\\Users\\vika\\Desktop\\Shopping\\NeProd", one);
    ProductGroup prod = new ProductGroup("C:\\Users\\vika\\Desktop\\Shopping\\Prod", two);

    UserInterface() throws IOException {
        super("Головна сторінка");
        super.setBounds(200, 200, 984, 576);
        super.setLayout(new BorderLayout()); // Зміна FlowLayout на BorderLayout
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
/*****
 В цій чатисні коду ми ініцілазізуємо масиви даними з файлів початковими значеннями, групи продовольчі, непродовольчі
 *****/
        BufferedReader readerNeprod = new BufferedReader(new FileReader("C:\\Users\\vika\\Desktop\\Shopping\\NeProd\\NeProd.txt"));
        String str = "";
        while ((str = readerNeprod.readLine()) != null) {
            neProd.addProductToGroup(new Product(str.split(";")), allGroup);
        }
        readerNeprod.close();

        BufferedReader readerProd = new BufferedReader(new FileReader("C:\\Users\\vika\\Desktop\\Shopping\\Prod\\Prod.txt"));
        str = "";
        while ((str = readerProd.readLine()) != null) {
            prod.addProductToGroup(new Product(str.trim().split(";")), allGroup);
        }

        readerProd.close();


        allGroup.addGroupToAllGroup(neProd);
        allGroup.addGroupToAllGroup(prod);


        menuBar = new JMenuBar();
        menu = new JMenu("Меню");

        deleteProduct = new JMenuItem("Видалити товар");
        deleteGroupOfProducts = new JMenuItem("Видалити групу товарів");
        addProduct = new JMenuItem("Додати товар");
        addGroupOfProducts = new JMenuItem("Додати групу товарів");
        editProduct = new JMenuItem("Редагувати товар");
        editGroupOfProducts = new JMenuItem("Редагувати групу товарів");
        lookFor = new JMenuItem("Пошук товару");
        showStaticData = new JMenuItem("Виведення статичних даних");

        menu.add(deleteProduct);
        menu.add(deleteGroupOfProducts);
        menu.add(addProduct);
        menu.add(addGroupOfProducts);
        menu.add(editProduct);
        menu.add(editGroupOfProducts);
        menu.add(lookFor);
        //Вивід статистичних даних: вивід всіх товарів з інформацією по складу,
        // вивід усіх товарів по групі товарів з інформацією,
        // загальна вартість товару на складі (кількість * на ціну),
        // загальна вартість товарів в групі товарів.
        menu.add(showStaticData);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER); // Додавання JTabbedPane до вмісту

        // Ініціалізація слухача подій для меню
        MenuListener menuListener = new MenuListener();

        deleteProduct.addActionListener(menuListener);
        deleteGroupOfProducts.addActionListener(menuListener);
        addProduct.addActionListener(menuListener);
        addGroupOfProducts.addActionListener(menuListener);
        editProduct.addActionListener(menuListener);
        editGroupOfProducts.addActionListener(menuListener);
        lookFor.addActionListener(menuListener);
        showStaticData.addActionListener(menuListener);

        // Додавання слухача подій миші для вкладок (подвійне клацання == закриття вкладки)
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Перевірка подвійного кліку
                    int tabIndex = tabbedPane.getSelectedIndex();
                    if (tabIndex != -1) {
                        closeTab(tabIndex);
                    }
                }
            }
        });

        setVisible(true);
    }

    class MenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {


/**видалити продукт з якоїсь групи
 *
 */
            if (e.getSource() == deleteProduct) {
                String tabTitle = deleteProduct.getText();
                int tabIndex = tabbedPane.indexOfTab(tabTitle);

                if (tabIndex == -1) {
                    JPanel newPanel = new JPanel(new GridLayout(17, 1));

                    JButton showAllGroupsbtn = new JButton("Показати всі групи: ");
                    newPanel.add(showAllGroupsbtn);

                    showAllGroupsbtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            newPanel.removeAll();

                            for (ProductGroup group : allGroup.getAllGroupArray()) {
                                newPanel.add(new JLabel(String.valueOf(group)));
                            }

                            JTextField textField = new JTextField(20);
                            newPanel.add(new JLabel());
                            newPanel.add(new JLabel("Введіть назву групи, з якої хочете видалити продукт: "));
                            newPanel.add(textField);

                            textField.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String groupName = textField.getText();
                                    ProductGroup groupToDeleteFrom = null;

                                    // Знаходимо групу за введеною назвою
                                    for (ProductGroup group : allGroup.getAllGroupArray()) {
                                        if (groupName.equals(group.getGroupName())) {
                                            groupToDeleteFrom = group;
                                            break;
                                        }
                                    }

                                    if (groupToDeleteFrom != null) {
                                        JLabel nameLabel = new JLabel("Введіть назву продукту, який хочете видалити: ");
                                        JTextField nameField = new JTextField(20);
                                        JButton deleteButton = new JButton("Видалити");

                                        ProductGroup finalGroupToDeleteFrom = groupToDeleteFrom;
                                        deleteButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String productName = nameField.getText();

                                                // Перевіряємо, чи існує продукт з введеною назвою у групі
                                                Product productToDelete = finalGroupToDeleteFrom.getProductByName(productName);
                                                if (productToDelete != null) {
                                                    try {
                                                        finalGroupToDeleteFrom.removeProductFromGroup(productToDelete);
                                                    } catch (IOException ex) {
                                                        new ErrorProductDeleting();
                                                    }
                                                } else {
                                                    new ErrorProductDeleting();
                                                }
                                            }
                                        });

                                        newPanel.add(nameLabel);
                                        newPanel.add(nameField);
                                        newPanel.add(deleteButton);

                                        newPanel.revalidate();
                                        newPanel.repaint();
                                    } else {
                                        new ErrorGroupDeleting();
                                    }
                                }
                            });
                        }
                    });

                    tabbedPane.addTab(tabTitle, newPanel);
                }
            }


/**метод для видалення групи продуктів
 *
 */
            else if (e.getSource() == deleteGroupOfProducts) {
                String tabTitle = deleteGroupOfProducts.getText();
                int tabIndex = tabbedPane.indexOfTab(tabTitle);

                if (tabIndex == -1) {
                    // Вкладка ще не існує, створюємо нову вкладку
                    JPanel newPanel = new JPanel(new GridLayout(17, 1));

                    // Створюємо кнопку
                    JButton showAllGroupsbtn = new JButton("Показати всі групи: ");

                    // Додаємо кнопку на панель
                    newPanel.add(showAllGroupsbtn);

                    showAllGroupsbtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Очищаємо вміст панелі перед додаванням нових елементів
                            newPanel.removeAll();

                            for (ProductGroup group : allGroup.getAllGroupArray()) {
                                newPanel.add(new JLabel(String.valueOf(group)));
                            }

                            JTextField textField = new JTextField(20);
                            newPanel.add(new JLabel());
                            newPanel.add(new JLabel("Введіть назву групи, яку хочете видалити: "));
                            newPanel.add(textField);

                            textField.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    boolean found = false;

                                    for (ProductGroup group : allGroup.getAllGroupArray()) {
                                        if (textField.getText().equals(group.getGroupName())) {
                                            found = true;
                                            break;
                                        }
                                    }

                                    if (found) {

                                        JButton delete = new JButton("Видалити");
                                        delete.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                if (e.getSource() == delete) {

                                                    allGroup.removeGroup(textField.getText());

                                                    newPanel.revalidate();
                                                    newPanel.repaint();
                                                }
                                            }
                                        });
                                        newPanel.add(delete);

                                        // Оновлюємо вміст панелі
                                        newPanel.revalidate();
                                        newPanel.repaint();
                                    } else {
                                        new ErrorGroupDeleting();
                                    }
                                }
                            });
                        }
                    });

                    // Додаємо нову панель на вкладку
                    tabbedPane.addTab(tabTitle, newPanel);
                }
            }
/**додавання продукту в якусь групу
 *
 */
            else if (e.getSource() == addProduct) {
                String tabTitle = addProduct.getText();
                int tabIndex = tabbedPane.indexOfTab(tabTitle);

                if (tabIndex == -1) {
                    // Вкладка ще не існує, створюємо нову вкладку
                    JPanel newPanel = new JPanel(new GridLayout(17, 1));

                    // Створюємо кнопку
                    JButton showAllGroupsbtn = new JButton("Показати всі групи: ");

                    // Додаємо кнопку на панель
                    newPanel.add(showAllGroupsbtn);

                    showAllGroupsbtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Очищаємо вміст панелі перед додаванням нових елементів
                            newPanel.removeAll();

                            for (ProductGroup group : allGroup.getAllGroupArray()) {
                                newPanel.add(new JLabel(String.valueOf(group)));
                            }

                            JTextField textField = new JTextField(20);
                            newPanel.add(new JLabel());
                            newPanel.add(new JLabel("Введіть назву групи, В яку хочете додати продукт: "));
                            newPanel.add(textField);

                            textField.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    boolean found = false;

                                    for (ProductGroup group : allGroup.getAllGroupArray()) {
                                        if (textField.getText().equals(group.getGroupName())) {
                                            found = true;
                                            break;
                                        }
                                    }

                                    if (!found) {
                                        JOptionPane.showMessageDialog(null, "Групу не знайдено.", "Помилка", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        // Додати додаткові компоненти для введення даних продукту
                                        newPanel.add(new JLabel("Введіть назву продукта: "));
                                        JTextField nameField = new JTextField(20);
                                        newPanel.add(nameField);

                                        newPanel.add(new JLabel("Введіть опис продукта: "));
                                        JTextField descriptionField = new JTextField(20);
                                        newPanel.add(descriptionField);

                                        newPanel.add(new JLabel("Введіть назву країни виробника: "));
                                        JTextField countryNameField = new JTextField(20);
                                        newPanel.add(countryNameField);

                                        newPanel.add(new JLabel("Введіть кількість: "));
                                        JTextField countField = new JTextField(20);
                                        newPanel.add(countField);

                                        newPanel.add(new JLabel("Введіть ціну: "));
                                        JTextField priceField = new JTextField(20);
                                        newPanel.add(priceField);

                                        JButton create = new JButton("Створити");
                                        create.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                if (e.getSource() == create) {
                                                    // Збираємо дані для створення продукту
                                                    String[] productData = {
                                                            nameField.getText(),
                                                            descriptionField.getText(),
                                                            countryNameField.getText(),
                                                            countField.getText(),
                                                            priceField.getText()
                                                    };
                                                    // Створюємо новий продукт
                                                    Product product = new Product(productData);

                                                    // Додаємо продукт до відповідної групи
                                                    for (ProductGroup group : allGroup.getAllGroupArray()) {
                                                        if (textField.getText().equals(group.getGroupName())) {
                                                            try {
                                                                group.addProductToGroup(product, allGroup);
                                                            } catch (IOException ex) {
                                                                throw new RuntimeException(ex);
                                                            }

                                                            break;
                                                        }
                                                    }
                                                    newPanel.revalidate();
                                                    newPanel.repaint();
                                                }
                                            }
                                        });
                                        newPanel.add(create);

                                        // Оновлюємо вміст панелі
                                        newPanel.revalidate();
                                        newPanel.repaint();
                                    }
                                }
                            });
                        }
                    });

                    // Додаємо нову панель на вкладку
                    tabbedPane.addTab(tabTitle, newPanel);
                }
            }


/**додавання групи до усіх груп продуктів
 *
 */
            else if (e.getSource() == addGroupOfProducts) {
                String tabTitle = addGroupOfProducts.getText();
                int tabIndex = tabbedPane.indexOfTab(tabTitle);

                if (tabIndex == -1) {
                    // Вкладка ще не існує, створюємо нову вкладку
                    JPanel newPanel = new JPanel(new GridLayout(17, 1));

                    // Створюємо кнопку
                    JButton showAllGroupsbtn = new JButton("Показати всі групи: ");

                    // Додаємо кнопку на панель
                    newPanel.add(showAllGroupsbtn);

                    showAllGroupsbtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Очищаємо вміст панелі перед додаванням нових елементів
                            newPanel.removeAll();

                            for (ProductGroup group : allGroup.getAllGroupArray()) {
                                newPanel.add(new JLabel(String.valueOf(group)));
                            }

                            JTextField textField = new JTextField(20);
                            newPanel.add(new JLabel());
                            newPanel.add(new JLabel("Введіть назву групи, яку хочете додати: "));
                            newPanel.add(textField);

                            textField.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    boolean found = false;

                                    for (ProductGroup group : allGroup.getAllGroupArray()) {
                                        if (textField.getText().equals(group.getGroupName())) {
                                            found = true;
                                            break;
                                        }
                                    }

                                    if (found) {
                                        new ErrorGroupAdding();
                                    } else {
                                        // Додати додаткові компоненти для введення даних продукту
                                        newPanel.add(new JLabel("Введіть опис групи: "));
                                        JTextField descriptionField = new JTextField(20);
                                        newPanel.add(descriptionField);


                                        JButton create = new JButton("Створити");
                                        create.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                if (e.getSource() == create) {
                                                    // Збираємо дані для створення продукту
                                                    String[] productData = {
                                                            textField.getText(),
                                                            descriptionField.getText()
                                                    };
                                                    // Створюємо новий продукт

                                                    ProductGroup newGroup = new ProductGroup("C:\\Users\\vika\\Desktop\\Shopping\\" + textField.getText(), productData);
                                                    // Додаємо продукт до відповідної групи
                                                    allGroup.addGroupToAllGroup(newGroup);

                                                    newPanel.revalidate();
                                                    newPanel.repaint();
                                                }
                                            }
                                        });
                                        newPanel.add(create);

                                        // Оновлюємо вміст панелі
                                        newPanel.revalidate();
                                        newPanel.repaint();
                                    }
                                }
                            });
                        }
                    });

                    // Додаємо нову панель на вкладку
                    tabbedPane.addTab(tabTitle, newPanel);
                }
            }

/**редагування інформації про продукт
 *
 */
            else if (e.getSource() == editProduct) {
                String tabTitle = editProduct.getText();
                int tabIndex = tabbedPane.indexOfTab(tabTitle);

                if (tabIndex == -1) {
                    JPanel newPanel = new JPanel(new GridLayout(17, 1));

                    JButton showAllGroupsbtn = new JButton("Показати всі групи: ");
                    newPanel.add(showAllGroupsbtn);

                    showAllGroupsbtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            newPanel.removeAll();

                            for (ProductGroup group : allGroup.getAllGroupArray()) {
                                newPanel.add(new JLabel(String.valueOf(group)));
                            }

                            JTextField textField = new JTextField(20);
                            newPanel.add(new JLabel());
                            newPanel.add(new JLabel("Введіть назву групи, в якій хочете відредагувати продукт: "));
                            newPanel.add(textField);

                            textField.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String groupName = textField.getText();
                                    ProductGroup groupToEditFrom = null;

                                    // Знаходимо групу за введеною назвою
                                    for (ProductGroup group : allGroup.getAllGroupArray()) {
                                        if (groupName.equals(group.getGroupName())) {
                                            groupToEditFrom = group;
                                            break;
                                        }
                                    }

                                    if (groupToEditFrom != null) {
                                        newPanel.removeAll(); // Очистка панелі перед виведенням наступних компонентів

                                        JLabel nameLabel = new JLabel("Введіть назву продукту, який хочете відредагувати: ");
                                        JTextField nameField = new JTextField(20);
                                        JButton editbtn = new JButton("Відредагувати");
                                        newPanel.add(nameLabel);
                                        newPanel.add(nameField);
                                        newPanel.add(editbtn);

                                        ProductGroup finalGroupToEditFrom = groupToEditFrom;
                                        editbtn.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String productName = nameField.getText();

                                                // Перевіряємо, чи існує продукт з введеною назвою у групі
                                                Product productToEdit = finalGroupToEditFrom.getProductByName(productName);
                                                if (productToEdit != null) {
                                                    try {
                                                        finalGroupToEditFrom.removeProductFromGroup(productToEdit);
                                                        // Додати додаткові компоненти для введення даних продукту
                                                        newPanel.removeAll(); // Очистка панелі перед виведенням наступних компонентів
                                                        newPanel.add(new JLabel("Введіть нову назву продукта: "));
                                                        JTextField newNameField = new JTextField(20);
                                                        newPanel.add(newNameField);

                                                        newPanel.add(new JLabel("Введіть новий опис продукта: "));
                                                        JTextField newDescriptionField = new JTextField(20);
                                                        newPanel.add(newDescriptionField);

                                                        newPanel.add(new JLabel("Введіть нову назву країни виробника: "));
                                                        JTextField newCountryNameField = new JTextField(20);
                                                        newPanel.add(newCountryNameField);

                                                        newPanel.add(new JLabel("Введіть нову кількість: "));
                                                        JTextField newCountField = new JTextField(20);
                                                        newPanel.add(newCountField);

                                                        newPanel.add(new JLabel("Введіть нову ціну: "));
                                                        JTextField newPriceField = new JTextField(20);
                                                        newPanel.add(newPriceField);

                                                        JButton saveButton = new JButton("Зберегти зміни");
                                                        newPanel.add(saveButton);

                                                        saveButton.addActionListener(new ActionListener() {
                                                            @Override
                                                            public void actionPerformed(ActionEvent e) {
                                                                String newName = newNameField.getText();
                                                                String newDescription = newDescriptionField.getText();
                                                                String newCountryName = newCountryNameField.getText();
                                                                int newCount = Integer.parseInt(newCountField.getText());
                                                                double newPrice = Double.parseDouble(newPriceField.getText());

                                                                // Оновити дані продукту
                                                                productToEdit.setProductName(newName);
                                                                productToEdit.setDescription(newDescription);
                                                                productToEdit.setManufacturer(newCountryName);
                                                                productToEdit.setQuantity(newCount);
                                                                productToEdit.setPrice(newPrice);

                                                                // Додати оновлений продукт до групи
                                                                try {
                                                                    finalGroupToEditFrom.addProductToGroup(productToEdit, allGroup);
                                                                    new SuccessProductEditing();
                                                                } catch (IOException ex) {
                                                                    throw new RuntimeException(ex);
                                                                }
                                                            }
                                                        });
                                                    } catch (IOException ex) {
                                                        throw new RuntimeException(ex);
                                                    }
                                                } else {
                                                    new ErrorGroupEditing();
                                                }
                                            }
                                        });
                                    } else {
                                        new ErrorGroupEditing();
                                    }
                                }
                            });
                            newPanel.revalidate(); // Оновлення панелі після додавання компонентів
                            newPanel.repaint();
                        }
                    });

                    tabbedPane.addTab(tabTitle, newPanel);
                }
            }

/**редагування групи товарів (назва і опис)
 *
 */
            else if (e.getSource() == editGroupOfProducts) {
                String tabTitle = editGroupOfProducts.getText();
                int tabIndex = tabbedPane.indexOfTab(tabTitle);

                if (tabIndex == -1) {
                    // Вкладка ще не існує, створюємо нову вкладку
                    JPanel newPanel = new JPanel(new GridLayout(17, 1));

                    // Створюємо кнопку
                    JButton showAllGroupsbtn = new JButton("Показати всі групи: ");

                    // Додаємо кнопку на панель
                    newPanel.add(showAllGroupsbtn);

                    showAllGroupsbtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Очищаємо вміст панелі перед додаванням нових елементів
                            newPanel.removeAll();

                            for (ProductGroup group : allGroup.getAllGroupArray()) {
                                newPanel.add(new JLabel(String.valueOf(group)));
                            }

                            newPanel.add(new JLabel("Введіть назву групи, яку хочете редагувати: "));
                            JTextField textField = new JTextField(20);
                            newPanel.add(textField);

                            textField.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    boolean found = false;
                                    ProductGroup groupToEdit = null;

                                    for (ProductGroup group : allGroup.getAllGroupArray()) {
                                        if (textField.getText().equals(group.getGroupName())) {
                                            found = true;
                                            groupToEdit = group;
                                            break;
                                        }
                                    }

                                    if (found) {

                                        JLabel nameLabel = new JLabel("Введіть нову назву групи");
                                        JTextField textField1 = new JTextField();
                                        newPanel.add(nameLabel);
                                        newPanel.add(textField1);
                                        newPanel.add(new JLabel("\n"));

                                        JLabel descLabel = new JLabel("Введіть новий опис групи");
                                        JTextField textField2 = new JTextField();
                                        newPanel.add(descLabel);
                                        newPanel.add(textField2);
                                        newPanel.add(new JLabel("\n"));


                                        JButton edit = new JButton("Редагувати");
                                        newPanel.add(edit);
                                        ProductGroup finalGroupToEdit = groupToEdit;
                                        edit.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                if (e.getSource() == edit) {
                                                    finalGroupToEdit.editGroup(textField1.getText(), textField2.getText());
                                                }
                                            }
                                        });

                                        // Оновлюємо вміст панелі
                                        newPanel.revalidate();
                                        newPanel.repaint();
                                    } else {
                                        new ErrorGroupEditing();
                                    }
                                }
                            });
                        }
                    });

                    // Додаємо нову панель на вкладку
                    tabbedPane.addTab(tabTitle, newPanel);
                }
            }


/**пошук якогось продукту
 *
 */
            else if (e.getSource() == lookFor) {
                String tabTitle = lookFor.getText();
                int tabIndex = tabbedPane.indexOfTab(tabTitle);
                if (tabIndex == -1) {
                    // Вкладка ще не існує, створюємо нову вкладку і додаємо на неї текстове поле
                    JPanel newPanel = new JPanel();
                    JLabel label = new JLabel("Введіть назву товару");
                    newPanel.setLayout(new GridLayout(17, 1));
                    JTextField textField = new JTextField(); // Створення текстового поля

                    newPanel.add(label);
                    newPanel.add(textField); // Додавання текстового поля на нову панель
                    tabbedPane.addTab(tabTitle, newPanel); // Додавання нової вкладки разом з панеллю

                    // Логіка з пошуком товару
                    textField.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            String productNamePattern = textField.getText().replace("?", ".{1}").replace("*", ".*");
                            Pattern pattern = Pattern.compile(productNamePattern, Pattern.CASE_INSENSITIVE);
                            ArrayList<Product> matchingProducts = new ArrayList<>();

                            // Проходження по всіх продуктах у всіх групах
                            for (ProductGroup group : allGroup.getAllGroupArray()) {
                                for (Product product : group.getArrayOfProducts()) {
                                    if (pattern.matcher(product.getProductName()).matches()) {
                                        matchingProducts.add(product);
                                    }
                                }
                            }
                            newPanel.add(new JScrollPane());

                            if (!matchingProducts.isEmpty()) {

                                // Додавання інформації про знайдені продукти
                                for (Product product : matchingProducts) {
                                    newPanel.add(new JLabel(product.toString() + "\n"));
                                }


                            } else {
                                JOptionPane.showMessageDialog(null, "Товари не знайдено.", "Помилка", JOptionPane.ERROR_MESSAGE);
                            }
                            // Перемальовуємо панель для оновлення вмісту
                            newPanel.revalidate();
                            newPanel.repaint();
                        }
                    });
                }
            }

/**
 * Виводимо статичні дані на екран
 */
            else if (e.getSource() == showStaticData) {
                String tabTitle = showStaticData.getText();
                int tabIndex = tabbedPane.indexOfTab(tabTitle);
                if (tabIndex == -1) {
                    // Вкладка ще не існує, створюємо нову вкладку і додаємо на неї текстове поле
                    JPanel newPanel = new JPanel();
                    newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS)); // Встановлюємо LayoutManager
                    newPanel.add(new JLabel("назва, опис, виробник, кількість на складі, ціна за одиницю" + "\n"));
                    // Перебираємо всі групи товарів
                    for (ProductGroup group : allGroup.getAllGroupArray()) {
                        // Створюємо JLabel для назви групи
                        JLabel groupLabel = new JLabel("Група: " + group.toString());
                        newPanel.add(new JLabel("\n"));
                        newPanel.add(new JLabel("***"));
                        newPanel.add(groupLabel);

                        // Отримуємо загальну вартість товарів у групі
                        double totalGroupValue = Products.getTotalGroupValue(group);
                        newPanel.add(new JLabel("\n"));
                        JLabel totalGroupValueLabel = new JLabel("Загальна вартість у групі: " + totalGroupValue);

                        newPanel.add(totalGroupValueLabel);

                        // Перебираємо всі продукти у групі і створюємо для них JLabel
                        for (Product product : group.getArrayOfProducts()) {
                            JLabel productLabel = new JLabel("Продукт: " + product.toString());
                            newPanel.add(productLabel);
                        }
                    }

                    // Отримуємо загальну вартість товару на складі
                    double totalStockValue = Products.getTotalStockValue(allGroup);
                    JLabel totalStockValueLabel = new JLabel("Загальна вартість на складі: " + totalStockValue);
                    newPanel.add(new JLabel("\n"));
                    newPanel.add(totalStockValueLabel);

                    // Додаємо нову вкладку з панеллю у табпенел
                    tabbedPane.addTab(tabTitle, newPanel);
                }
            }


        }
    }

    public void closeTab(int tabIndex) {
        tabbedPane.remove(tabIndex);
    }
}