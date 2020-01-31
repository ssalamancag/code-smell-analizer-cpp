import CodeBlocks.Clase;
import CodeBlocks.Funcion;

import java.util.*;

public class VisitorCPP <T> extends  CPPBaseVisitor {

    //////PARAMETROS

    int maxMethod = 30; ///numero maximo de instrucciones que puede tener un metodo para no ser considerado un code Smell
    int minMethod = 3; ///numero minimo de instrucciones que puede tener un metodo para no ser considerado un code Smell
    int maximumNesting = 2; ///numero maximo de condicionales anidados permitidos antes de considerarse un code Smell
    int maxLenghtIdentifiers = 16; ///longitud maxima de los identificadores
    int minLenghtIdentifiers = 2; ///longitud minima de los identificadores
    int maxNumberOperators = 5;  ///maximo numero de operadores en una expresion
    int maxParams = 4; //maximo numero de parametros antes de ser code smell

    //nuestro objeto detector
    public CodeSmellDetector detector;

    public List<Clase> clases = new ArrayList<Clase>();
    public List<Funcion> funciones = new ArrayList<Funcion>();
    int classCount = -1;

    VisitorCPP() {
        detector = new CodeSmellDetector();
    }

    // CYCLOMATIC COMPLEXITY------------------------------------------------------------------------------------
    public int nestingCount(String statement, String stmType) {
        int count = 0, index = 0;
        while (index >= 0) {
            count++;
            index = statement.indexOf(stmType, index + 1);
        }
        return count;
    }

    @Override
    public T visitSelectionstatement(CPPParser.SelectionstatementContext ctx) {
        int nesting = 0;
        if (ctx.getChild(0) == ctx.If()) {
            nesting = nestingCount(ctx.statement().get(0).getText(), "if");
        } else if (ctx.getChild(0) == ctx.Switch()) {
            nesting = nestingCount(ctx.statement().get(0).getText(), "switch");
        }

        if (nesting > maximumNesting) {
            detector.AddCodeSmell(SMELL.CyclomaticComplexy, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        return (T) super.visitChildren(ctx);
    }

    @Override
    public T visitIterationstatement(CPPParser.IterationstatementContext ctx) {
        int nesting = 0;
        if (ctx.getChild(0) == ctx.Do()) {
            nesting = nestingCount(ctx.statement().getText(), "do");
        } else if (ctx.getChild(0) == ctx.For()) {
            nesting = nestingCount(ctx.statement().getText(), "for");
        } else if (ctx.getChild(0) == ctx.While()) {
            nesting = nestingCount(ctx.statement().getText(), "while");
        }

        if (nesting > maximumNesting) {
            detector.AddCodeSmell(SMELL.CyclomaticComplexy, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        return (T) super.visitChildren(ctx);
    }

    // LONG-SHORT METHOD------------------------------------------------------------------------------------
    @Override
    public T visitFunctionbody(CPPParser.FunctionbodyContext ctx) {
        String[] arr = ctx.getText().split(";");
        if (arr.length > maxMethod) {
            detector.AddCodeSmell(SMELL.LongMethod, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        } else if ((arr.length < minMethod) && (arr.length != 1)) {
            detector.AddCodeSmell(SMELL.ShortMethod, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        return (T) super.visitChildren(ctx);
    }

    // LAZY CLASS ------------------------------------------------------------------------------------
    @Override
    public T visitClassspecifier(CPPParser.ClassspecifierContext ctx) {
        clases.add(new Clase(ctx.classhead().classheadname().getText(), ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine()));
        classCount++;

        if (ctx.getText().equals(ctx.classhead().getText() + "{}")) {
            detector.AddCodeSmell(SMELL.EmptyBlock, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        } else {
            int childs = ctx.memberspecification().getChildCount();
            boolean isPublic = true;

            if (childs == 1) {
                //caso en el que solo existen metodos dentro de la clase
                if (ctx.memberspecification().getChild(0).getClass().getName().equals("CPPParser$MemberdeclarationContext")) {
                    detector.AddCodeSmell(SMELL.LazyClass, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
                }
            } else {
                //se verifica si tiene el modificador de acceso "private"
                for (int i = 0; i < childs; i++) {
                    if (ctx.memberspecification().getChild(i).getClass().getName().equals("CPPParser$AccessspecifierContext")) {
                        if (ctx.memberspecification().getChild(i).getText().equals("private")) {
                            isPublic = false;
                        }
                    }
                    //se verifica que no tenga modificador a ""publico""
                    if (ctx.memberspecification().getChild(i).getClass().getName().equals("CPPParser$MemberspecificationContext") && !isPublic) {
                        for (int k = 0; k < ctx.memberspecification().getChild(i).getChildCount(); k++) {
                            if (ctx.memberspecification().getChild(i).getChild(k).getClass().getName().equals("CPPParser$MemberspecificationContext")) {
                                CPPParser.MemberspecificationContext child = (CPPParser.MemberspecificationContext) ctx.memberspecification().getChild(i).getChild(k);
                                for (int j = 0; j < child.getChildCount(); j++) {
                                    if (child.getChild(j).getClass().getName().equals("CPPParser$MemberspecificationContext")) {
                                        if (!child.getChild(j).getText().contains("public:")) {
                                            detector.AddCodeSmell(SMELL.LazyClass, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return (T) visitChildren(ctx);
    }


    // LONG SHORT IDENTIFIERS------------------------------------------------------------------------------------
    @Override
    public T visitDeclaratorid(CPPParser.DeclaratoridContext ctx) {
        String identifier = ctx.idexpression().unqualifiedid().Identifier().getText();
        if (identifier.length() < minLenghtIdentifiers) {
            detector.AddCodeSmell(SMELL.ShortIdentifiers, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        } else if (identifier.length() > maxLenghtIdentifiers) {
            detector.AddCodeSmell(SMELL.LongIdentifiers, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        return (T) visitChildren(ctx);
    }

    // COMPLICATED EXPRESSION------------------------------------------------------------------------------------
    @Override
    public T visitExpression(CPPParser.ExpressionContext ctx) {
        String expression = ctx.getText();
        if(!expression.contains("<<") || !expression.contains(">>")){
            String[] exprarr = expression.split("&&|<|>|==|!|\\|\\|");
            if (exprarr.length > maxNumberOperators) {
                detector.AddCodeSmell(SMELL.TooComplicatedExpression, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
            }
        }
        return (T) visitChildren(ctx);
    }

    // MANY PARAMETERS------------------------------------------------------------------------------------
    public int parameterCounter;
    public boolean parameterListSmelled;

    @Override
    public T visitParameterdeclarationlist(CPPParser.ParameterdeclarationlistContext ctx) {
        //se cuenta cada uno de los parametros, si sobrepasa el maximo se genera un code smell
        parameterCounter++;
        if (parameterCounter > maxParams && !parameterListSmelled) {
            detector.AddCodeSmell(SMELL.ManyParameters, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
            parameterListSmelled = true;
        }
        return (T) visitChildren(ctx);
    }

    @Override
    public T visitParameterdeclarationclause(CPPParser.ParameterdeclarationclauseContext ctx) {
        //se entra a una nueva lista de parametros
        parameterCounter = 0;
        parameterListSmelled = false;
        return (T) visitChildren(ctx);
    }

    // EMPTY BLOCK------------------------------------------------------------------------------------
    @Override
    public T visitBracedinitlist(CPPParser.BracedinitlistContext ctx) {
        if (ctx.getText().equals("{}")) {
            detector.AddCodeSmell(SMELL.EmptyBlock, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        return (T) visitChildren(ctx);
    }

    @Override
    public T visitCompoundstatement(CPPParser.CompoundstatementContext ctx) {
        if (ctx.getText().equals("{}")) {
            detector.AddCodeSmell(SMELL.EmptyBlock, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        }
        return (T) visitChildren(ctx);
    }

    // MULTIPLES MAIN---------------------------------------------------------------------------------------------
    @Override public T visitMemberdeclaration(CPPParser.MemberdeclarationContext ctx) {
        if (ctx.getChild(0) == ctx.functiondefinition()) {
            String name = ctx.functiondefinition().declarator().ptrdeclarator().noptrdeclarator().noptrdeclarator().getText();
            clases.get(classCount).addMetodo(new Funcion(name, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), true));
            if (ctx.functiondefinition().declarator().getText().contains("main")) {
                detector.AddCodeSmell(SMELL.MultipleMains, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
            }
        }
        return (T) visitChildren(ctx);
    }

    // CLASES NO INSTANCIADAS------------------------------------------------------------------------------------
    @Override
    public T visitThetypename(CPPParser.ThetypenameContext ctx) {
        if (ctx.getChild(0) == ctx.classname()) {
            for (int i = 0; i < clases.size(); i++) {
                if (clases.get(i).getName().equals(ctx.classname().getText())) {
                    clases.get(i).instance();
                }
            }
        }
        return (T) visitChildren(ctx);
    }

    public void nonInstancedClasses() {
        for (Clase clase : clases) {
            if (!clase.isInstanced()) {
                detector.AddCodeSmell(SMELL.NonInstancedClass, clase.getLine(), clase.getCol());
            }
        }
    }

    // FUNCIONES NO EJECUTADAS -----------------------------------------------------------------------------------
    @Override public T visitDeclaration(CPPParser.DeclarationContext ctx) {
        if (ctx.getChild(0) == ctx.functiondefinition()) {
            String name = ctx.functiondefinition().declarator().ptrdeclarator().noptrdeclarator().noptrdeclarator().getText();
            funciones.add(new Funcion(name, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), false));
        }
        return (T) visitChildren(ctx);
    }

    @Override public T visitPostfixexpression(CPPParser.PostfixexpressionContext ctx){
        if (ctx.getChild(0) == ctx.postfixexpression() && ctx.getChild(1) == ctx.LeftParen()) {
            for (int i = 0; i < funciones.size(); i++) {
                if (funciones.get(i).getName().equals(ctx.postfixexpression().getText())) {
                    funciones.get(i).execute();
                }
            }
        }
        return (T) visitChildren(ctx);
    }

    public void nonExecutedFunctions() {
        for (Funcion funcion : funciones) {
            if (!funcion.isExecuted()){
                detector.AddCodeSmell(SMELL.NonExecutedFunction, funcion.getLine(), funcion.getCol());
            }
        }
    }


}


