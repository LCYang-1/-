package com.lcy.miaosha.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * qq 登陆常量配置类
 */
@Component
public class Constants {

	@Value("${constants.qqAppId}")
	private String qqAppId;

	@Value("${constants.qqAppSecret}")
	private String qqAppSecret;

	@Value("${constants.qqRedirectUrl}")
	private String qqRedirectUrl;

	@Value("${constants.weCatAppId}")
	private String weCatAppId;

	@Value("${constants.weCatAppSecret}")
	private String weCatAppSecret;

	@Value("${constants.weCatRedirectUrl}")
	private String weCatRedirectUrl;

	public String getQqAppId() {
		return qqAppId;
	}

	public void setQqAppId(String qqAppId) {
		this.qqAppId = qqAppId;
	}

	public String getQqAppSecret() {
		return qqAppSecret;
	}

	public void setQqAppSecret(String qqAppSecret) {
		this.qqAppSecret = qqAppSecret;
	}

	public String getQqRedirectUrl() {
		return qqRedirectUrl;
	}

	public void setQqRedirectUrl(String qqRedirectUrl) {
		this.qqRedirectUrl = qqRedirectUrl;
	}

	public String getWeCatAppId() {
		return weCatAppId;
	}

	public void setWeCatAppId(String weCatAppId) {
		this.weCatAppId = weCatAppId;
	}

	public String getWeCatAppSecret() {
		return weCatAppSecret;
	}

	public void setWeCatAppSecret(String weCatAppSecret) {
		this.weCatAppSecret = weCatAppSecret;
	}

	public String getWeCatRedirectUrl() {
		return weCatRedirectUrl;
	}

	public void setWeCatRedirectUrl(String weCatRedirectUrl) {
		this.weCatRedirectUrl = weCatRedirectUrl;
	}
}
