import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.*;

public class LexicalAnalysis {
    public static void main(String[] args) {
        LexicalAnalysis lexAn = new LexicalAnalysis();

        //REAL strings
        String realString1 = "CREATE TABLE students(id INTEGER, name TEXT(30), age INTEGER);";
        String realString2 = "CREATE TABLE studentsnew(id INTEGER, name TEXT(30), age INTEGER);";
        String realString3 = "SELECT * FROM students INNER JOIN studentsnew ON students.id = studentsnew.id;";
        String realString4 = "INSERT INTO students(3, 'Karen', 20);";

        // FIRST
        ArrayList<RowOfSymbTable> rowsForSymbTable = lexAn.formRowsForTable(realString1);
        ArrayList<RowOfSymbTable> rowsWithPositions = lexAn.formPositions(rowsForSymbTable);
        lexAn.writeToTXT(rowsWithPositions);
        //SECOND
        rowsForSymbTable = lexAn.formRowsForTable(realString2);
        rowsWithPositions = lexAn.formPositions(rowsForSymbTable);
        lexAn.writeToTXT(rowsWithPositions);
        //THIRD
        rowsForSymbTable = lexAn.formRowsForTable(realString3);
        rowsWithPositions = lexAn.formPositions(rowsForSymbTable);
        lexAn.writeToTXT(rowsWithPositions);
        //FOURTH
        rowsForSymbTable = lexAn.formRowsForTable(realString4);
        rowsWithPositions = lexAn.formPositions(rowsForSymbTable);
        lexAn.writeToTXT(rowsWithPositions);
    }

    private ArrayList<RowOfSymbTable> formRowsForTable(String realString){
        ArrayList<RowOfSymbTable> rowsForSymbTable = new ArrayList<>();
        int numOfSpaces;
        Pattern p1 = Pattern.compile("((>=)|(<=)|(<>)|>|<|=)");
        Matcher m1 = p1.matcher(realString);
        while (m1.find()){
            numOfSpaces = (int)realString.substring(0,m1.end()).chars().filter(c -> c == (int)' ').count();
            rowsForSymbTable.add(new RowOfSymbTable(Token.REALOP, m1.group(),m1.start()+1-numOfSpaces,m1.end()-m1.start()));
        }
        Pattern p2 = Pattern.compile("(\\b\\d+)");
        Matcher m2 = p2.matcher(realString);
        while (m2.find()){
            numOfSpaces = (int)realString.substring(0,m2.end()).chars().filter(c -> c == (int)' ').count();
            rowsForSymbTable.add(new RowOfSymbTable(Token.NUM, m2.group(),m2.start()+1-numOfSpaces,m2.end()-m2.start()));
        }
        Pattern p3 = Pattern.compile("('(.*?)')");
        Matcher m3 = p3.matcher(realString);
        while (m3.find()){
            numOfSpaces = (int)realString.substring(0,m3.end()).chars().filter(c -> c == (int)' ').count();
            rowsForSymbTable.add(new RowOfSymbTable(Token.LITERAL, m3.group(),m3.start()+1-numOfSpaces,m3.end()-m3.start()));
            int lenOfStrForReplacement = m3.group().length();
            String strForReplacement = " ";
            for(int i = 0; i < lenOfStrForReplacement; i++){
                strForReplacement = strForReplacement.concat("(");
            }
            realString = realString.replaceAll(m3.group(),strForReplacement);
        }
        Pattern p4 = Pattern.compile("(create|table|integer|text|insert|into|select|from|inner|join|on)",Pattern.CASE_INSENSITIVE);
        Matcher m4 = p4.matcher(realString);
        while (m4.find()){
            numOfSpaces = (int)realString.substring(0,m4.end()).chars().filter(c -> c == (int)' ').count();
            rowsForSymbTable.add(new RowOfSymbTable(Token.KEYWORD, m4.group(),m4.start()+1-numOfSpaces,m4.end()-m4.start()));

        }
        Pattern p5 = Pattern.compile("(\\b(?!create|table|integer|text|insert|into|select|from|inner|join|on|\\d|\\s|\\(|\\)|,|;)\\w*)",Pattern.CASE_INSENSITIVE);
        Matcher m5 = p5.matcher(realString);
        while (m5.find()){
            numOfSpaces = (int)realString.substring(0,m5.end()).chars().filter(c -> c == (int)' ').count();
            rowsForSymbTable.add(new RowOfSymbTable(Token.ID, m5.group(),m5.start()+1-numOfSpaces,m5.end()-m5.start()));
        }
        return rowsForSymbTable;
    }

    private void writeToTXT (ArrayList<RowOfSymbTable> rows){
        try(FileWriter writer = new FileWriter("data.txt", true))
        {
            writer.write("____________________________________________________\n");
            String text = "|    ТОКЕН   |   ЛЕКСЕМА   |ПОЧАТОК|ДОВЖИНА|ПОЗИЦІЯ|\n";
            writer.write(text);
            writer.write("|__________________________________________________|\n");
            for(var row: rows){
                writer.write(row.toString());
            }
            writer.write("|__________________________________________________|\n");
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }

        System.out.println("FINISHED");
    }

    private ArrayList<RowOfSymbTable> formPositions(ArrayList<RowOfSymbTable> rowsForSymbTable){
        Collections.sort(rowsForSymbTable);
        int counter = 1;
        for(var row: rowsForSymbTable){
            row.setPosition(counter);
            counter++;
        }
        return rowsForSymbTable;
    }
}
