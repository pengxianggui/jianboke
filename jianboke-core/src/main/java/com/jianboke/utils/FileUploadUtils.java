package com.jianboke.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.jianboke.config.ImgPathConfig;
import com.jianboke.enumeration.HttpReturnCode;
import com.jianboke.model.RequestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("FileUploadUtils")
public class FileUploadUtils extends ImgPathConfig {
	private static final Logger log = LoggerFactory.getLogger(FileUploadUtils.class);
	
	/**
	 * 上传book封面图片
	 * @param file
	 */
	public Map<String, String> uploadBookCover(MultipartFile file) {
		Map<String, String> resultMap = new HashMap<String, String>();
		System.out.println("文件名---------------：" + file.getOriginalFilename());
		String rootPath = this.getBaseUrl();
		String filename = file.getOriginalFilename();
		String bookCoverPath = "img/bookCover/" + filename; // 返回给前端
		File saveFile = new File(rootPath + "/" + bookCoverPath);
		OutputStream os = null;
		try {
			os = new FileOutputStream(saveFile);
			if (!saveFile.exists()) {
				saveFile.createNewFile();
			}
			InputStream in = file.getInputStream();
			byte buffer[] = new byte[4 * 1024];
			int length = 0;
			while((length = in.read(buffer)) != -1) {
				os.write(buffer, 0, length);
			}
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		resultMap.put("path", bookCoverPath);
		return resultMap;
	}
	
	private String generatorBookCoverName() {
		return null;
	}

	/**
	 * 上传用户头像
	 * @param file
	 * @return
	 */
	public ResponseEntity<RequestResult> uploadAvator(MultipartFile file) {
		String rootPath = this.getBaseUrl();
		String filename = file.getOriginalFilename();
		String avatorPath = "img/avator/" + filename; // 返回给前端
		File saveFile = new File(rootPath + "/" + avatorPath);
		OutputStream os = null;
		try {
			os = new FileOutputStream(saveFile);
			if (!saveFile.exists()) {
				saveFile.createNewFile();
			}
			InputStream in = file.getInputStream();
			byte buffer[] = new byte[4 * 1024];
			int length = 0;
			while((length = in.read(buffer)) != -1) {
				os.write(buffer, 0, length);
			}
			os.flush();
			return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, avatorPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_UPLOAD_FAIL));
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
