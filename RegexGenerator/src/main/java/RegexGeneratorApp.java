import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RegexGeneratorApp {
    private JFrame frame;
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton generateButton;
    private OpenAiService openAiService;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                RegexGeneratorApp app = new RegexGeneratorApp();
                app.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public RegexGeneratorApp() {
        openAiService = new OpenAiService();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Oracle Regex Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout());

        inputField = new JTextField();
        frame.add(inputField, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        generateButton = new JButton("Generate Regex");
        frame.add(generateButton, BorderLayout.SOUTH);
        generateButton.addActionListener(new GenerateButtonListener());
    }

    private class GenerateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userInput = inputField.getText();
            try {
                String regex = openAiService.generateRegex(userInput);
                outputArea.setText(regex);
            } catch (IOException ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        }
    }
}
