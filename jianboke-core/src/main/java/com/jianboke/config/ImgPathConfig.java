package com.jianboke.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "data.imgConfig")
public class ImgPathConfig {
	// 图片存储的根路径
	private String baseUrl;

	// markdown图片返回的根路径： 域名
	private String mdImgRootPath;

	public String getMdImgRootPath() {
		return mdImgRootPath;
	}

	public void setMdImgRootPath(String mdImgRootPath) {
		this.mdImgRootPath = mdImgRootPath;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
}
