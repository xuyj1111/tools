package xu.tools.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileTool {

    /**
     * @Description: 用FileWriter写入内容
     */
    public static void createAndWriteFile(String file, String content) throws Exception {
        Path path = Paths.get(file);
        Files.createFile(path);
        if (!Files.exists(path)) {
            throw new Exception("文件未创建成功");
        }
        FileWriter writer = new FileWriter(path.toFile());
        writer.write(content);
        writer.flush();
        writer.close();
    }

    /**
     * @Description: 用FileReader读取内容
     */
    public static String readTxt(String file) throws Exception {
        Path path = Paths.get(file);
        if (!file.endsWith(".txt") || !Files.exists(path)) {
            throw new Exception("文件不是txt格式或已存在");
        }
        FileReader reader = new FileReader(path.toFile());
        char[] cache = new char[50];
        StringBuilder stringBuilder = new StringBuilder();
        for (int read = 0; read != -1; read = reader.read(cache)) {
            stringBuilder.append(Arrays.copyOf(cache, read));
        }
        reader.close();
        return stringBuilder.toString();
    }

    /**
     * @Description: 删除文件夹，若文件夹内有文件或文件夹，遍历删除
     */
    public static void deleteFolder(File folder) throws Exception {
        if (!folder.exists()) {
            throw new Exception("文件不存在");
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    //递归直到目录下没有文件
                    deleteFolder(file);
                } else {
                    file.delete();
                }
            }
        }
        folder.delete();
    }
}
