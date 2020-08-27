package com.spring.sample.model;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.spring.sample.uploader.ImageUpload;
import com.spring.sample.uploader.cloudinary.CloudinaryImageUpload;

public class MicropostModel extends BaseModel {
	private Integer id;
	private Integer userId;
	@NotEmpty(message = "{micropost.validation.content.required}")
	@Size(max = 128, message = "{micropost.validation.content.length}")
	private String content;
	private String image;
	private Date createdAt;

	private UserModel user;
	private MultipartFile file;

	public MicropostModel() {
//		Sort.by("price").descending()
	}

	public MicropostModel(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	public boolean isAttached() {
		return StringUtils.hasText(image);
	}

	public ImageUpload getUpload() {
		ImageUpload file = new CloudinaryImageUpload();
		if (StringUtils.hasText(image)) {
			file.setStoredPath(image);
		}
		return file;
	}

	public void setUpload(ImageUpload file) {
		this.image = file.getStoredPath();
	}

}
