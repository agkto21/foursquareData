package dataIO;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class WriteProcess {

	public static void appendFile(String fileName, String content) {  
        try {
            // ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�  
            FileWriter writer = new FileWriter(fileName, true);  
            writer.write(content+"\n");  
            writer.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 
}
