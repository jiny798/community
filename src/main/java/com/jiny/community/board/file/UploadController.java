package com.jiny.community.board.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/upload")
public class UploadController {

    private final FileStore fileStore;

        @PostMapping(value = "/file")
        @ResponseBody
        public UploadResponseDto uploadFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request){

            log.info("upload");
            UploadResponseDto uploadResponseDto = new UploadResponseDto();
            String url="";
            try {

                UploadFile uploadFile = fileStore.storeFile(multipartFile);
                // 저장과 동시에 abc.png 저장시 uploadFile -> abc.png, (임의의값)qwdoqo-12412.png 로 저장
                uploadResponseDto.setUrl("/upload/"+uploadFile.getStoreFileName());
                uploadResponseDto.setResult("sucess");

            }catch (IOException e){
                log.info("업로드 에러");
                uploadResponseDto.setUrl("");
                uploadResponseDto.setResult("fail");
            }




            return uploadResponseDto;

        }


}
