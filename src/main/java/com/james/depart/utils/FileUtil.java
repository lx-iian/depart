package com.james.depart.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.james.depart.domain.Department;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author james
 * @description
 * @date 2021-01-07
 */
public class FileUtil {

    /**
     * 通过本地文件访问json并读取
     *
     * @param path：json.json
     * @return：json文件的内容
     */
    public static String ReadFile(String path) {
        StringBuilder lastStr = new StringBuilder();
        File file = new File(path);// 打开文件
        BufferedReader reader = null;
        try {
            FileInputStream in = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));// 读取文件
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                lastStr.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException el) {
                }
            }
        }
        return lastStr.toString();
    }

    private static Properties getProps(String propertiesFileName) throws IOException {

        Properties properties = new Properties();
        properties.load(FileUtil.class.getResourceAsStream("/META-INF/" + propertiesFileName + ".json"));

        return properties;
    }

    /**
     * 获取WEB-INF目录下面server.xml文件的路径
     *
     * @return
     */
    public static String getXmlPath(String resource, String fileName) {
        //file:/D:/JavaWeb/.metadata/.me_tcat/webapps/TestBeanUtils/WEB-INF/classes/
        String path = FileUtil.class.getClassLoader().getResource(resource).toString();
        path = path.replace('/', '\\'); // 将/换成\
        path = path.replace("file:", ""); //去掉file:
        path = path.replace("classes\\", ""); //去掉class\
        path = path.substring(1); //去掉第一个\,如 \D:\JavaWeb...
        path += fileName;
        //System.out.println(path);
        return path;
    }

}
