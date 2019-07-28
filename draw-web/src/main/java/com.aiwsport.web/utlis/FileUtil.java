package com.aiwsport.web.utlis;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 文件工具类
 * @author yangjian9
 *
 */
public class FileUtil {

    private FileUtil(){}

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static boolean writeFile(String path, String name, InputStream input){
        // 设置数据缓冲
        byte[] bs = new byte[1024 * 2];
        // 读取到的数据长度
        int len;
        // 输出的文件流保存图片至本地
        OutputStream os = null;

        String inputPath = "";

        try {

            File sf=new File(path);
            if(!sf.exists()){
                sf.mkdirs();
            }

            inputPath = path+"/"+name;
            os = new FileOutputStream(inputPath);
            while ((len = input.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException : ", e);
            return false;
        } catch (IOException e) {
            logger.error("IOException : ", e);
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (input != null) {
                    input.close();
                }
            }catch (IOException e){
                logger.error("writeFile close is error: " + e);
            }
        }


        String imgType = name.substring(name.lastIndexOf(".")+1);
        try {
            File sf=new File("/data1/drawsimple");
            if(!sf.exists()){
                sf.mkdirs();
            }
            Thumbnails.of(inputPath).scale(0.3f).outputQuality(0.3f).outputFormat(imgType).toFile("/data1/drawsimple" + "/"+name);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("Thumbnails is error: " + e1);
        }

        return true;
    }

    /**
     * 删除单个文件
     *
     * @param path 要删除的文件的路径与名称
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String path) {
        try {
            File file = new File(path);
            // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
            if (file.exists()) {
                if (file.isFile()) {
                    return file.delete();
                } else {
                    return false;
                }
            }
        }catch (Exception e){
            logger.error("deleteFile is error: " + e);
            return false;
        }
        return true;
    }

}
