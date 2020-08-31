import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import entry.Builder;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class TextCopyForm extends DialogWrapper {


    private JPanel panel1;
    private JTextArea t1TextArea;
    private JTextArea t2TextArea;
    private JButton copyButton;
    private JTextField tagTextField;
    private JCheckBox withCRUDCheckBox;

    private Builder builder;
    private String tagTplKey = "go-tag-tpl";
    private String defaultTag = "json:\"%s\" gorm:\"column:%s\"";

    protected TextCopyForm(@Nullable Project project) {
        super(project, null, false, IdeModalityType.IDE, false);
        setTitle("Convert To Go");
        init();
    }

    @Nullable
    @Override
    protected String getDimensionServiceKey() {
        return TextCopyForm.class.getName();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(t2TextArea.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            dispose();
        });
        tagTextField.setText(getTpl());
        tagTextField.getDocument().
                addDocumentListener(new DocumentListener() {
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

        withCRUDCheckBox.addChangeListener(e -> gen());
        return panel1;
    }

    private String getTpl() {
        return PropertiesComponent.getInstance().getValue(tagTplKey, defaultTag);
    }

    private void setTpl(String tpl) {
        PropertiesComponent.getInstance().setValue(tagTplKey, tpl, defaultTag);
    }

    public void gen() {
        String tpl = tagTextField.getText();
        builder.setConfig(tpl, withCRUDCheckBox.isSelected());
        String selectedText = t1TextArea.getText();
        String result = builder.gen(selectedText);
        this.t2TextArea.setText(result);
        setTpl(tpl);
    }

    void setBuilder(Builder builder, String text) {
        this.builder = builder;
        this.t1TextArea.setText(text);
    }
}

