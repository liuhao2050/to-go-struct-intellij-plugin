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
//        val framee = TextCopyForm.getFrame()
        tf.setBuilder(builder,selectedText)
//        val p = Objects.requireNonNull(WindowManager.getInstance().getFrame(project))
//            ?.locationOnScreen
//        frame.location = project
//        tf.t1TextArea.text = selectedText
        tf.gen()

        tf.pack()
        tf.show()
      //  tf.setBuilder(builder)

       // frame.contentPane = tf.panel1
        //frame.preferredSize = Dimension(1100, 600)
        //frame.extendedState = JFrame.NORMAL
        //frame.pack()
        //frame.isVisible = true
    }
}