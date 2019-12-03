enum SMELL {
    LongMethod, //sebastian
    ManyParameters, //mauro
    LongShortIdentifiers, // jhonatan
    BooleanExpressions,
    CyclomaticComplexy,
    FeatureEnvy,
    RefusedBequest,
    LAzyClass
}

public class CodeSmell {
    public SMELL type;
    public int line;
    public int colum;
}
