import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import entry.Builder;
import entry.JsonStructBuilder;
import org.jetbrains.annotations.NotNull;

public class ConvertJSONAction extends AnAction {

    @Override
    public void update(AnActionEvent e) {
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Builder builder = new JsonStructBuilder("Gen");
        new Runner().run(e, builder);
    }

}