import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class VisitorCPP <T> extends  CPPBaseVisitor{
    //////PARAMETROS

    int sizeMethod = 30; ///numero maximo de instrucciones que puede tener un metodo para no ser considerado un code Smell
    int maximumNesting = 2; ///numero maximo de condicionales anidados permitidos antes de considerarse un code Smell

    //nuestro objeto detector
    public CodeSmellDetector detector;

    VisitorCPP (){
        detector = new CodeSmellDetector();
    }

    public  int nestingCount(String statement){
        int count = 0, index =0;
        while(index >= 0){
            count++;
            index = statement.indexOf("if",index+1);
        }
        return count;
    }

    @Override public T visitFunctionbody(CPPParser.FunctionbodyContext ctx) {
        String[] arr = ctx.getText().split(";");

        if(arr.length > sizeMethod){
            detector.AddCodeSmell(SMELL.LongMethod,ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        return (T) super.visitChildren(ctx);
    }



    @Override public T visitSelectionstatement(CPPParser.SelectionstatementContext ctx) {
        int nesting = nestingCount(ctx.statement().get(0).getText());
        if (nesting > maximumNesting){
            detector.AddCodeSmell(SMELL.CyclomaticComplexy, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        return (T) super.visitChildren(ctx);
    }


}
