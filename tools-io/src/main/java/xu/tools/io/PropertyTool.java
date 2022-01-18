package xu.tools.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyTool {

    public static String readProperty(String fileName, String key) throws IOException, ClassNotFoundException {
        //获取一个表示该线程堆栈转储的堆栈跟踪元素数组
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        //获取调用方的类
        Class<?> callClass = Class.forName(stackTrace[2].getClassName());

        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = callClass.getClassLoader().getResourceAsStream(fileName);
        // 使用properties对象加载输入流
        properties.load(in);
        return properties.getProperty(key);
    }
}
