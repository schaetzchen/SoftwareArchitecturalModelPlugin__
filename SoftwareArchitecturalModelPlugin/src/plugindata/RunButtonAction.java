package plugindata;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;

public class RunButtonAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        addAJWeaverJarFile(getEventProject(e));
        addLoggingAspectFile(getEventProject(e));
        addAOPConfig(getEventProject(e));
    }

    private void addAJWeaverJarFile(Project project) {

        File ajw = new File(getClass().getClassLoader().getResource("aspectjweaver.jar").getFile());
        VirtualFile f = LocalFileSystem.getInstance().findFileByIoFile(ajw);
        VirtualFile dir = LocalFileSystem.getInstance().findFileByIoFile(new File(project.getBasePath()));

        if (! new File(dir.getPath() + "\\aspectjweaver.jar").exists())
            RunnableHelper.runWriteCommand(project, new Runnable() {
                @Override
                public void run() {
                    RunnableHelper.handleFileCopy(this, f, dir);
                }
            });
    }

    private void addLoggingAspectFile(Project project) {

        File aspect = new File(getClass().getClassLoader().getResource("LoggingAspect.java").getFile());
        VirtualFile f = LocalFileSystem.getInstance().findFileByIoFile(aspect);

        RunnableHelper.runWriteCommand(project, new Runnable() {
            @Override
            public void run() {
                RunnableHelper.handleDirCreation(this, project.getBasePath() + "/src/executiondatalogging");
            }
        });

        File d = new File(project.getBasePath() + "/src/executiondatalogging");
        VirtualFile dir = LocalFileSystem.getInstance().findFileByIoFile(d);

        if (!new File(dir.getPath() + "\\LoggingAspect.java").exists())
            RunnableHelper.runWriteCommand(project, new Runnable() {
                @Override
                public void run() {
                    RunnableHelper.handleFileCopy(this, f, dir);
                }
            });
    }

    private void addAOPConfig(Project project) {

        File config = new File(getClass().getClassLoader().getResource("aop.xml").getFile());
        VirtualFile f = LocalFileSystem.getInstance().findFileByIoFile(config);

        RunnableHelper.runWriteCommand(project, new Runnable() {
            @Override
            public void run() {
                RunnableHelper.handleDirCreation(this, project.getBasePath() + "/src/META-INF");
            }
        });

        File d = new File(project.getBasePath() + "/src/META-INF");
        VirtualFile dir = LocalFileSystem.getInstance().findFileByIoFile(d);

        if (!new File(dir.getPath() + "\\aop.xml").exists())
            RunnableHelper.runWriteCommand(project, new Runnable() {
                @Override
                public void run() {
                    RunnableHelper.handleFileCopy(this, f, dir);
                }
            });
    }
}