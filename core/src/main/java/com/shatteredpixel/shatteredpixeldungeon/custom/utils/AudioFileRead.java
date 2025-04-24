//package com.shatteredpixel.shatteredpixeldungeon.custom.utils;
//
//import org.jaudiotagger.audio.AudioFile;
//import org.jaudiotagger.audio.AudioFileIO;
//import org.jaudiotagger.tag.FieldKey;
//import org.jaudiotagger.tag.Tag;
//
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.StandardCopyOption;
//
////TODO 实现读取音频文件的元数据 未完成
//public class AudioFileRead {
//    public static long[] main(String args) {
//        try {
//            // 获取资源输入流
//            InputStream inputStream = AudioFileRead.class.getClassLoader()
//                    .getResourceAsStream("assets/audio.mp3");
//
//            if (inputStream == null) {
//                System.err.println("文件未找到！");
//                return null;
//            }
//
//            // 创建临时文件
//            java.io.File tempFile = java.io.File.createTempFile("tempAudio", ".mp3");
//            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//            // 使用JAudiotagger读取标签
//            AudioFile audioFile = AudioFileIO.read(tempFile);
//            Tag tag = audioFile.getTag();
//
//            // 输出元数据
//            System.out.println("标题: " + tag.getFirst(FieldKey.TITLE));
//            System.out.println("艺术家: " + tag.getFirst(FieldKey.ARTIST));
//            System.out.println("专辑: " + tag.getFirst(FieldKey.ALBUM));
//
//            // 删除临时文件
//            tempFile.deleteOnExit();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
