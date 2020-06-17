package com.lcy.miaosha.vo;

import com.lcy.miaosha.validator.IsMobile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@ApiModel(value="登录参数类",description="登录参数类" )
public class LoginVo {

    @NotNull
    @IsMobile
    @ApiModelProperty(example="13012400230")
    private String mobile;
    @NotNull
    @Length(min = 32)
    @ApiModelProperty(example="d3b1294a61a07da9b49b6e22b2cbd7f9")
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
