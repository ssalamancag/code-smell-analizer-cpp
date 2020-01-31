enum SMELL {
    LongMethod,
    ShortMethod,
    ManyParameters,
    LongIdentifiers,
    ShortIdentifiers,
    CyclomaticComplexy,
    LazyClass,
    TooComplicatedExpression,
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

