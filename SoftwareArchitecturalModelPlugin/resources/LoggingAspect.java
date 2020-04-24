package executiondatalogging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

@Aspect
public abstract class LoggingAspect {

    private static boolean rewriteFile = true;
    private static DateFormat dateFormat;
    private static Date date;
    private int caseID = new Random().nextInt();

    private ArrayList<JoinPoint> enclosingJoinPoints;

    private void writeToFile(String message, boolean rewrite) {

        try {

            FileWriter csvWriter = new FileWriter("new.csv", !rewrite);
            BufferedWriter bw = new BufferedWriter(csvWriter);

            bw.write(message);
            bw.close();
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    private JoinPoint getEnclosingJoinPoint(String signature) {
        JoinPoint res = null;

        for (JoinPoint jp : enclosingJoinPoints)
            if (jp.getSignature().toString().equals(signature))
                res = jp;

        return res;
    }

    public LoggingAspect() {

        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();

        enclosingJoinPoints = new ArrayList<>();
        if (rewriteFile) {
            writeToFile("Callee method,Callee object,Caller method,Caller object,Time,Case ID\n", rewriteFile);
        }
    }

    public void setRewrite(boolean r) {
        rewriteFile = r;
    }

    private String adjustMethodSignature(JoinPoint joinPoint) {
        String res = joinPoint.getSignature().toLongString();
        res = res.substring(0, res.indexOf('('));
        String[] ar = res.split(" ");
        return ar[ar.length - 1];
    }

    private String adjustMethodSignature(JoinPoint.StaticPart joinPoint) {
        String res = joinPoint.getSignature().toLongString();
        res = res.substring(0, res.indexOf('('));
        String[] ar = res.split(" ");
        return ar[ar.length - 1];
    }

    @Pointcut()
    void methodExecuted() {
    }

    @Before("methodExecuted()")
    public void beforeAction(JoinPoint thisJoinPoint, JoinPoint.EnclosingStaticPart thisEnclosingJoinPointStaticPart) {

        enclosingJoinPoints.add(thisJoinPoint);

        writeToFile(adjustMethodSignature(thisJoinPoint) + ",", false);

        if (thisJoinPoint.getTarget() != null)
            writeToFile(thisJoinPoint.getTarget().hashCode() + ",", false);
        else
            writeToFile("-,", false);

        JoinPoint enclosingJoinPoint = getEnclosingJoinPoint(thisEnclosingJoinPointStaticPart.getSignature().toString());

        String time = dateFormat.format(date);

        if (enclosingJoinPoint == null)
            writeToFile(adjustMethodSignature(thisEnclosingJoinPointStaticPart) + ",-" + time + "," + caseID + "\n", false);
        else {
            writeToFile(adjustMethodSignature(enclosingJoinPoint) + ",", false);
            if (enclosingJoinPoint.getTarget() != null)
                writeToFile(enclosingJoinPoint.getTarget().hashCode() + "," + time + "," + caseID + "\n", false);
            else
                writeToFile("-," + time + "," + caseID + "\n", false);
        }
    }
}