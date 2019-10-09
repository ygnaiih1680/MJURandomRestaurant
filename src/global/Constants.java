package global;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static Map<String, Integer> categories = new HashMap<>();

    static {
        CategoryButton[] values = CategoryButton.values();
        for (int i = 0; i < values.length; i++) {
            CategoryButton button = values[i];
            categories.put(button.name(), i);
        }
    }

    public static int getNumber(String string){
        return Range.valueOf(string).getNumber();
    }

    public enum MainFrame {
        title("MJU Random Restaurant"), list("<html>전체<br>목록</html>"),
        x(400), y(100), w(500), h(800), icon("data\\icon.png"),
        subtitle("- 인문캠 ver. -"), copyright("Made By. NIBBLE"), feedback("NIBBLE에 의견 보내기->");
        private int value;
        private String text;

        MainFrame(int value) {
            this.value = value;
        }

        MainFrame(String text) {
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    public enum CategoryButton {
        korean("한식"), western("양식"), japanese("일식"), chicken("치킨"),
        chinese("중식"), convenience("<html>간편<br>분식</html>"), etc("기타");
        private String title;
        private static String dir = "data\\", extension = ".jpg";

        CategoryButton(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public String getDir() {
            return dir + this.name() + extension;
        }
    }

    public enum ListDialog {
        title("Restaurant List"), x(400), y(100),
        w(500), h(800), icon("data\\icon.png");
        private int value;
        private String text;

        ListDialog(int value) {
            this.value = value;
        }

        ListDialog(String text) {
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    public enum ResponsiveDialog {
        title("Pick!"), x(400), y(100),
        w(400), h(600), icon("data\\icon.png");
        private int value;
        private String text;

        ResponsiveDialog(int value) {
            this.value = value;
        }

        ResponsiveDialog(String text) {
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    public enum FeedbackDialog {
        title("FEEDBACK"),x(400), y(100),
        w(400), h(700), icon("data\\icon.png");
        private int value;
        private String text;

        FeedbackDialog(int value) {
            this.value = value;
        }

        FeedbackDialog(String text) {
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    private enum Range{
        korean(1, 56), western(57, 66), japanese(67, 80), chicken(81, 94),
        chinese(95, 102), convenience(103, 128), etc(129, 143);

        private int start, end;
        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        int getNumber() {
            return (int) (Math.random() * (end - start + 1)) + start;
        }
    }
}
