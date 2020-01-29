import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisitorCPP <T> extends  CPPBaseVisitor{

    //////PARAMETROS

    int maxMethod = 30; ///numero maximo de instrucciones que puede tener un metodo para no ser considerado un code Smell
    int minMethod = 3; ///numero minimo de instrucciones que puede tener un metodo para no ser considerado un code Smell
    int maximumNesting = 2; ///numero maximo de condicionales anidados permitidos antes de considerarse un code Smell
    int maxLenghtIdentifiers = 16; ///longitud maxima de los identificadores
    int minLenghtIdentifiers = 2; ///longitud minima de los identificadores
    int maxNumberOperators = 3;  ///maximo numero de operadores en una expresion
    int maxParams = 4; //maximo numero de parametros antes de ser code smell

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
        if(arr.length > maxMethod){
            detector.AddCodeSmell(SMELL.LongMethod,ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }else if((arr.length < minMethod) && (arr.length != 1)){
            detector.AddCodeSmell(SMELL.ShortMethod,ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
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
        if(ctx.getText().equals(ctx.classhead().getText()+"{}")){
            detector.AddCodeSmell(SMELL.EmptyBlock, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine() );
        }else{
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
        }

        return (T) visitChildren(ctx); }

    @Override public T visitDeclaratorid(CPPParser.DeclaratoridContext ctx) {
        String identifier = ctx.idexpression().unqualifiedid().Identifier().getText();
        if (identifier.length()<minLenghtIdentifiers ){
            detector.AddCodeSmell(SMELL.ShortIdentifiers, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }else if(identifier.length()>maxLenghtIdentifiers){
            detector.AddCodeSmell(SMELL.LongIdentifiers, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }

        return (T) visitChildren(ctx); }

    @Override public T visitExpression(CPPParser.ExpressionContext ctx) {
        String expression = ctx.getText();
        int numberOperators = 0;
        Set<String> operators = new HashSet<String>();
        List<String> operatorsList = Arrays.asList("+", "-", "*", "/", "%", "^", "&&", "||", "!", "not", "and", "or", "~");
        operators.addAll(operatorsList);
        for (String operator : operators) {
            if (expression.contains(operator)) {
                numberOperators++;
            }
        }
        if (numberOperators>maxNumberOperators){
            detector.AddCodeSmell(SMELL.TooComplicatedExpression, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }

        return (T) visitChildren(ctx); }

    public int parameterCounter;
    public boolean parameterListSmelled;

    @Override public T visitParameterdeclarationlist(CPPParser.ParameterdeclarationlistContext ctx){
        //se cuenta cada uno de los parametros, si sobrepasa el maximo se genera un code smell
        parameterCounter++;
        if(parameterCounter > maxParams && !parameterListSmelled ){
            detector.AddCodeSmell(SMELL.ManyParameters, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
            parameterListSmelled = true;
        }
        return (T) visitChildren(ctx);
    }

    @Override public T visitParameterdeclarationclause(CPPParser.ParameterdeclarationclauseContext ctx){
        //se entra a una nueva lista de parametros
        parameterCounter = 0;
        parameterListSmelled = false;
        return (T) visitChildren(ctx);
    }

    @Override public T visitBracedinitlist(CPPParser.BracedinitlistContext ctx){
        if(ctx.getText().equals("{}")){
            detector.AddCodeSmell(SMELL.EmptyBlock, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        return (T) visitChildren(ctx);
    }

    @Override public T visitCompoundstatement(CPPParser.CompoundstatementContext ctx){
        if(ctx.getText().equals("{}")){
            detector.AddCodeSmell(SMELL.EmptyBlock, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        return (T) visitChildren(ctx);
    }
}

