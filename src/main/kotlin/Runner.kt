import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.wm.WindowManager
import entry.Builder
import java.awt.Dimension
import java.util.*
import javax.swing.JFrame

internal class Runner {
    fun run(e: AnActionEvent, builder: Builder) {
        val project =
            e.getData(PlatformDataKeys.PROJECT)
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val selectedText = editor.selectionModel.selectedText
        val result = builder.gen(selectedText)
        val tf = TextCopyForm()
        val frame = TextCopyForm.getFrame()
        val p = Objects.requireNonNull(WindowManager.getInstance().getFrame(project))
            ?.locationOnScreen
        frame.location = p
        tf.t1TextArea.text = selectedText
        tf.t2TextArea.text = result
        frame.contentPane = tf.panel1
        frame.preferredSize = Dimension(1100, 600)
        frame.extendedState = JFrame.NORMAL
        frame.pack()
        frame.isVisible = true
    }
}