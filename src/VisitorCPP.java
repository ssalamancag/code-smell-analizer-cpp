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


    @Override public T visitClassspecifier(CPPParser.ClassspecifierContext ctx) {
        int childs = ctx.memberspecification().getChildCount();
        boolean isPublic = true;

        if(childs == 1 ){
            //caso en el que solo existen metodos dentro de la clase
            if(ctx.memberspecification().getChild(0).getClass().getName().equals("CPPParser$MemberdeclarationContext")){
                detector.AddCodeSmell(SMELL.LazyClass, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine() );
            }
        }else{
            //se verifica si tiene el modificador de acceso "private"
            for (int i = 0; i  < childs; i++){
                if(ctx.memberspecification().getChild(i).getClass().getName().equals("CPPParser$AccessspecifierContext")){
                    if(ctx.memberspecification().getChild(i).getText().equals("private")){
                        isPublic = false;
                    }
                }
                //se verifica que no tenga modificador a ""publico""
                if(ctx.memberspecification().getChild(i).getClass().getName().equals("CPPParser$MemberspecificationContext") && !isPublic){
                    for(int k = 0; k < ctx.memberspecification().getChild(i).getChildCount(); k++){
                        if(ctx.memberspecification().getChild(i).getChild(k).getClass().getName().equals("CPPParser$MemberspecificationContext")){
                            CPPParser.MemberspecificationContext child = (CPPParser.MemberspecificationContext) ctx.memberspecification().getChild(i).getChild(k);
                            for (int j = 0; j < child.getChildCount(); j++){
                                if(child.getChild(j).getClass().getName().equals("CPPParser$MemberspecificationContext")){
                                    if(!child.getChild(j).getText().contains("public:")){
                                        detector.AddCodeSmell(SMELL.LazyClass, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine() );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return (T) visitChildren(ctx); }

}
