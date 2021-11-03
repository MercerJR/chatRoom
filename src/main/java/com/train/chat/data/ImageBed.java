package com.train.chat.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


/**
 * @Author MercerJR
 * @Data 2021/4/25 14:57
 */
@Data
@AllArgsConstructor
public class ImageBed {

    MultipartFile image;

    String apiType = "xiaomi";

    String token = "a31355f1b987e24424f7629cc019";

    public ImageBed(MultipartFile image){
        this.image = image;
    }

}
