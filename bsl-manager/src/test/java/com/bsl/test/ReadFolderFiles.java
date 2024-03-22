package com.bsl.test;

import java.io.File;

/**
 * @author huangyl
 * @title: ReadFolderFiles
 * @description: 读取文件夹下所有文件名
 * @date 2024/3/21 10:18
 */
public class ReadFolderFiles {
    public static void main(String[] args) {
        String folderPath = "D:\\workspace-person\\bsl\\bsl-manager\\src\\main\\webapp\\WEB-INF\\jsp"; // 将此处替换为要读取的文件夹路径

        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            for (int i=0; i<files.length; i++) {
                System.out.println("INSERT INTO `bsl_code_table` VALUES ('" + files[i].getName().replace(".jsp", "") + "', '00');");
            }
        } else {
            System.out.println("指定的路径不存在或者不是一个文件夹");
        }
    }
}
