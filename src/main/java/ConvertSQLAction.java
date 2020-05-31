import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import entry.Builder;
import entry.SQLStructBuilder;
import org.jetbrains.annotations.NotNull;

public class ConvertSQLAction extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        // Using the event, evaluate the context, and enable or disable the action.
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Builder builder = new SQLStructBuilder();
        new Runner().run(e, builder);
    }
}
