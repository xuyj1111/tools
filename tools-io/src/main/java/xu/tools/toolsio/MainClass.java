package xu.tools.toolsio;

import java.io.IOException;

public class MainClass {

    public static void main(String[] args) {
        try {
            String s = PropertyTool.readProperty("application.properties", "port");
            System.out.println(s);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
