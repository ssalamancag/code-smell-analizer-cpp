enum SMELL {
    LongMethod, //sebastian
    ManyParameters, //mauro
    LongShortIdentifiers, // jhonatan
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
