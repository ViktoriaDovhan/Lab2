import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorAdding extends JFrame {
    JButton okButton;

    ErrorAdding() {
        super();
        super.setBounds(400, 200, 250, 100);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLayout(new FlowLayout());
        super.setVisible(true);

        JLabel errorLabel = new JLabel("Продукт не було додано, адже такий вже існує");
        okButton = new JButton("OK");
        okButton.addActionListener(new WrongListener());


        Container container = super.getContentPane();
        container.add(errorLabel);
        container.add(okButton);

    }

    class WrongListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == okButton) {
                dispose();
            }
        }
    }
}