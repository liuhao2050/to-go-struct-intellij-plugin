import entry.Builder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowEvent;

public class TextCopyForm {


    private Builder builder;
    private static JFrame frame;
    private JPanel panel1;
    private JTextArea t1TextArea;
    private JTextArea t2TextArea;
    private JButton copyButton;

    public JTextField getTagTextField() {
        return tagTextField;
    }

    private JTextField tagTextField;
    private JCheckBox withCRUDCheckBox;


    TextCopyForm() {
        frame = new JFrame();
        copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(t2TextArea.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);

            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        tagTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                gen();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                gen();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                gen();
            }
        });
        withCRUDCheckBox.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                gen();
            }
        });
    }

    public void gen() {
        String tpl = tagTextField.getText();
        builder.setConfig(tpl, withCRUDCheckBox.isSelected());
        String selectedText = t1TextArea.getText();
        String result = builder.gen(selectedText);
        this.t2TextArea.setText(result);
    }

    public static void main(String[] args) {
        frame.notify();
        frame.setContentPane(new TextCopyForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    static JFrame getFrame() {
        return frame;
    }

    JTextArea getT1TextArea() {
        return t1TextArea;
    }

    JTextArea getT2TextArea() {
        return t2TextArea;
    }
    void setBuilder(Builder builder) {
        this.builder = builder;
    }
    JPanel getPanel1() {
        return panel1;
    }
}
