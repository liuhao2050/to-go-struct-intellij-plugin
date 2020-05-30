import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import entry.JsonStructBuilder;
import org.jetbrains.annotations.NotNull;

public class PopupDialogAction extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        System.out.println("hello " + e.toString());
        // Using the event, evaluate the context, and enable or disable the action.
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (null == editor) {
            return;
        }

        String selectedText = editor.getSelectionModel().getSelectedText();
        Messages.showMessageDialog(project, selectedText, "title", Messages.getInformationIcon());

        JsonStructBuilder builder = new JsonStructBuilder("Gen");
        String result = builder.gen(selectedText);


        Messages.showMessageDialog(project, result, "title2", Messages.getInformationIcon());
//        StructGenerateResult structGenerateResult = StructUtil.generateStruct(selectedText);
//        if (!StringUtils.isBlank(structGenerateResult.error)) {
//            Messages.showErrorDialog(project, structGenerateResult.error, "Generate Failed");
//            return;
//        }
//
//        String builderPatternCode = BuilderUtil.generateBuilderPatternCode(structGenerateResult.structEntityList);
//
//        int textLength = editor.getDocument().getTextLength();
//
//        new WriteCommandAction(project) {
//            @Override
//            protected void run(@NotNull Result result) {
//                editor.getDocument().insertString(textLength, builderPatternCode);
//                editor.getCaretModel().moveToOffset(textLength + 2);
//                editor.getScrollingModel().scrollToCaret(ScrollType.CENTER_UP);
//            }
//        }.execute();
    }

}