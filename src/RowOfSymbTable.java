enum Token{
    ID, KEYWORD, NUM, LITERAL, REALOP
}

public class RowOfSymbTable implements Comparable<RowOfSymbTable> {
    Token token;
    String lexeme;
    int firstPositionOfLexeme;
    int length;
    int position;

    public RowOfSymbTable(){}

    public void setPosition(int position) {
        this.position = position;
    }

    public RowOfSymbTable(Token token, String lexeme, int firstPositionOfLexeme, int length) {
        this.token = token;
        this.lexeme = lexeme;
        this.firstPositionOfLexeme = firstPositionOfLexeme;
        this.length = length;

    }

    @Override
    public int compareTo(RowOfSymbTable o) {
        return this.firstPositionOfLexeme - o.firstPositionOfLexeme;
    }

    @Override
    public String toString() {
        return String.format("|%-12s|%-13s|%-7d|%-7d|%-7d|\n", token,lexeme,firstPositionOfLexeme,length, position);
    }
}
