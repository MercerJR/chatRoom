package com.train.chat.pojo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @Author MercerJR
 * @Data 2021/4/20 15:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String userId;

    @NotEmpty(message = "用户名不能为空")
    @Size(min = 4,max = 15,message = "用户名的长度应在4到15个字符以内")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8,message = "密码长度必须在8位及以上")
    private String password;

    private static final long serialVersionUID = 1L;
}