package com.train.chat.controller;

import com.train.chat.data.ImageBed;
import com.train.chat.data.Response;
import com.train.chat.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author MercerJR
 * @Data 2020/7/25 14:52
 */
@RestController
@Slf4j
@RequestMapping("/image")
@Validated
public class ImageController {

    @Autowired
    private ImageUtil imageUtil;

    @PostMapping(value = "uploadImg", produces = "application/json")
    public Response uploadImg(@RequestParam("image") MultipartFile image) throws IOException {
        ImageBed imageBed = new ImageBed(image);
        String imageUrl = imageUtil.getImageUrl(imageBed);
        return new Response().success(imageUrl);
    }

}
