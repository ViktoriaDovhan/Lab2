import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorGroupEditing extends JFrame {
    JButton okButton;

    ErrorGroupEditing() {
        super();
        super.setBounds(500, 300, 250, 100);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLayout(new FlowLayout());
        super.setVisible(true);

        JLabel errorLabel = new JLabel("Групу не можна редагувати, адже такої не існує");
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