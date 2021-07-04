package org.wangfeng.panda.app.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;

import java.io.*;

@Slf4j
public class FileUtils {

    /**
     * file转bytes
     * @param filePath
     * @return
     */
    public static byte[] fileToBytes(String filePath) {
        byte[] buffer = null;
        File file = new File(filePath);

        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];

            int n;

            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            buffer = bos.toByteArray();
        } catch (FileNotFoundException ex) {
            log.error("");
        } catch (IOException ex) {
            log.error("");
        } finally {
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException ex) {
                log.error("");
            } finally{
                try {
                    if(null!=fis){
                        fis.close();
                    }
                } catch (IOException ex) {
                    log.error("");
                }
            }
        }

        return buffer;
    }


    /**
     * bytes转file
     * @param buffer
     * @param filePath
     */
    public static void bytesToFile(byte[] buffer, final String filePath){

        File file = new File(filePath);

        OutputStream output = null;
        BufferedOutputStream bufferedOutput = null;

        try {
            output = new FileOutputStream(file);

            bufferedOutput = new BufferedOutputStream(output);

            bufferedOutput.write(buffer);
        } catch (FileNotFoundException e) {
            log.error("");
        } catch (IOException e) {
            log.error("");
        } finally{
            if(null!=bufferedOutput){
                try {
                    bufferedOutput.close();
                } catch (IOException e) {
                    log.error("");
                }
            }

            if(null != output){
                try {
                    output.close();
                } catch (IOException e) {
                    log.error("");
                }
            }
        }

    }


//    /**
//     * file 转 multipartFile
//     * @param file
//     * @return
//     * @throws IOException
//     */
//    public static MultipartFile fileToMultipartFile(File file) throws IOException {
//        try {
//            // File转换成MutipartFile
//            FileInputStream inputStream = new FileInputStream(file);
//            MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
//            //注意这里面填啥，MultipartFile里面对应的参数就有啥，比如我只填了name，则
//            //MultipartFile.getName()只能拿到name参数，但是originalFilename是空。
//            return multipartFile;
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        }
//    }


    /**
     * multipartFile 转 file
     * @param multipartFile
     * @return
     */

    public static File multipartFileToFile(MultipartFile multipartFile) {
        if (multipartFile == null){
            throw new RuleRuntimeException("读取不到文件");
        }
        File file = null;
        try {
            InputStream is = multipartFile.getInputStream();
            file = new File( multipartFile.getOriginalFilename());
            OutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }



}
