package com.train.chat.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.messaging.saaj.packaging.mime.internet.ContentType;
import com.train.chat.data.ImageBed;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Author MercerJR
 * @Data 2020/7/25 15:01
 */
@Component
public class ImageUtil {

    public String getImageUrl(ImageBed imageBed) throws IOException {
        MultipartFile image = imageBed.getImage();
        ByteArrayResource fileAsResource = new ByteArrayResource(image.getBytes()){
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
            @Override
            public long contentLength() {
                return image.getSize();
            }
        };


        String url = "https://images.ac.cn/api/upload";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("image", fileAsResource);
        map.add("apiType", imageBed.getApiType());
        map.add("token", imageBed.getToken());

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        String res = response.getBody();
        return res;
    }

}
