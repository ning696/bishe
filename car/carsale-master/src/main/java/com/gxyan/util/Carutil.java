package com.gxyan.util;

import com.gxyan.pojo.Car;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;

public class Carutil {
    public static Car parseCarJson(String carJson) {
        // 可以使用Jackson或Gson来转换JSON
        // 这里示范用Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(carJson, Car.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static File getImgDirFile(String carpath) {
        // 构建上传文件的存放 "文件夹" 路径
        String fileDirPath = "src/main/resources/static/upload/imgs/"+carpath;
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            // 递归生成文件夹
            fileDir.mkdirs();
        }
        return fileDir;

    }
}
