enum SMELL {
    LongMethod, //sebastian
    ShortMethod,
    ManyParameters, //mauro
    LongIdentifiers, // jhonatan
    ShortIdentifiers,
    BooleanExpressions,
    CyclomaticComplexy, //sebastian
    FeatureEnvy,
    RefusedBequest,
    LazyClass,
    TooComplicatedExpression,
    EmptyBlock
}

public class CodeSmell {
    public SMELL type;
    public int line;
    public int colum;
}
