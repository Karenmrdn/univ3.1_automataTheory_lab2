import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;

public class TestClass {
    public static void main(String[] args) {
        String text = "day good day";
        Pattern pattern = Pattern.compile("day");
        Pattern pattern1 = Pattern.compile("good");

        Matcher matcher = pattern.matcher(text);
        Matcher matcher1 = pattern1.matcher(text);
        while (matcher.find()) {
                System.out.println("Найдено совпадение " + "day" + " с " + matcher.start() + " по " + matcher.end() + " позицию");
        }
    }
}
