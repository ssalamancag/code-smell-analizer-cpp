import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class VisitorCPP <T> extends  CPPBaseVisitor{

    //nuestro objeto detector
    public CodeSmellDetector detector;

    VisitorCPP (){
        detector = new CodeSmellDetector();
    }



}
