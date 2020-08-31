import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import entry.Builder

internal class Runner {
    fun run(e: AnActionEvent, builder: Builder) {
        val project =
            e.getData(PlatformDataKeys.PROJECT)
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val selectedText = editor.selectionModel.selectedText
        val tf = TextCopyForm(project)
        tf.setBuilder(builder,selectedText)
        tf.gen()
        tf.pack()
        tf.show()
    }
}