import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.FrameWrapper;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.ex.WindowManagerEx;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class TextCopyForm {
    private static JFrame frame;
    private JPanel panel1;
    private JTextArea t1TextArea;
    private JTextArea t2TextArea;
    private JButton copyButton;

    public JTextField getTagTextField() {
        return tagTextField;
    }

    private JTextField tagTextField;
    private JCheckBox CRUDFunctionsCheckBox;
    private JCheckBox withCommentsCheckBox;

    TextCopyForm() {
        frame = new JFrame();
        copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(t2TextArea.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);

            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        tagTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
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

    JPanel getPanel1() {
        return panel1;
    }
}
