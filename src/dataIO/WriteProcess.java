package dataIO;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class WriteProcess {

	public static void appendFile(String fileName, String content) {  
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件  
            FileWriter writer = new FileWriter(fileName, true);  
            writer.write(content+"\n");  
            writer.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 
}
