import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import entry.Builder;

import javax.swing.*;
import java.awt.*;
class Runner {
    void run(AnActionEvent e, Builder builder) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (null == editor) {
            return;
        }

        String selectedText = editor.getSelectionModel().getSelectedText();
        String result = builder.gen(selectedText);
        TextCopyForm tf = new TextCopyForm();
        JFrame frame = TextCopyForm.getFrame();

        tf.getT1TextArea().setText(selectedText);
        tf.getT2TextArea().setText(result);

        frame.setContentPane(tf.getPanel1());
        frame.setPreferredSize(new Dimension(1100, 600));
        frame.setExtendedState(JFrame.NORMAL);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
