import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class VisitorCPP <T> extends  CPPBaseVisitor{

    //nuestro objeto detector
    public CodeSmellDetector detector;

    VisitorCPP (){
        detector = new CodeSmellDetector();
    }

    @Override public T visitFunctionbody(CPPParser.FunctionbodyContext ctx) {
        String[] arr = ctx.getText().split(";");

        if(arr.length > 30){
            detector.AddCodeSmell(SMELL.LongMethod,ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        return (T) super.visitChildren(ctx);
    }
}
