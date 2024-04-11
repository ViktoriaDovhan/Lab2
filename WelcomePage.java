import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class WelcomePage extends JFrame {

    JButton enter;
    JLabel welcomeMessage;

    WelcomePage() {
        super("Титульна сторінка");
        super.setBounds(200, 200, 689, 405);
        //штука ерез яку кнопка не додавалась
        super.setLayout(null);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color customColor = new Color(0xDE7171);
        Color buttonColor = new Color(0xD9D9D9);

        getContentPane().setBackground(customColor);

        enter = new JButton("Увійти");
        welcomeMessage = new JLabel("Магазин");


        enter.setBounds(175, 227, 339, 62);
        enter.setBackground(buttonColor); // Встановлюємо колір фону кнопки
        enter.setForeground(Color.BLACK); // Встановлюємо колір тексту кнопки

        Font customFont = new Font("Monotype Corsiva", Font.PLAIN, 50); // Встановлюємо стандартний закручений шрифт

        welcomeMessage.setBounds(259, 102, 303, 48);
        welcomeMessage.setFont(customFont);


        this.add(enter);
        this.add(welcomeMessage);
        enter.addActionListener(new EnterListener());


    }

    class EnterListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //логіка входу в магазин
            if (e.getSource() == enter) {

                UserInterface userInterface = null;
                try {
                    userInterface = new UserInterface();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                userInterface.setVisible(true);
                setVisible(false);
            }
        }
    }

    public static void main(String[] args) {


        WelcomePage welcomePage = new WelcomePage();

        welcomePage.setVisible(true);
    }
}

