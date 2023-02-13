package com.jiny.community.infra.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UploadController {

    private final FileStore fileStore;

        @PostMapping(value = "/upload")
        @ResponseBody
        public String uploadFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request){


            try {
                String url="";
                UploadFile uploadFile = fileStore.storeFile(multipartFile);
            }catch (IOException e){
                log.info("업로드 에러");
                new RuntimeException(e);
            }




            return a;

        }


}
