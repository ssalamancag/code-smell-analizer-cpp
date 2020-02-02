enum SMELL {
    LongMethod,
    ShortMethod,
    ManyParameters,
    LongIdentifiers,
    ShortIdentifiers,
    CyclomaticComplexy,
    LazyClass,
    ComplicatedExpression,
    EmptyBlock,
    NonInstancedClass,
    NonExecutedFunction,
    MultipleMains
}

public class CodeSmell {
    public SMELL type;
    public int line;
    public int colum;
}

