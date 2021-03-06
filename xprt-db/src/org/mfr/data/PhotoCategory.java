package org.mfr.data;

// Generated 2011.09.19. 22:50:48 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.mfr.data.Category;
import org.mfr.data.Photo;

/**
 * PhotoCategory generated by hbm2java
 */
@Entity
@Table(name = "photo_category")
public class PhotoCategory implements java.io.Serializable {

	private Integer id;
	private Photo photo;
	private Category category;
	private Date modifyDt;
	private Integer cover;

	public PhotoCategory() {
	}

	public PhotoCategory(Photo photo, Category category) {
		this.photo = photo;
		this.category = category;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "photo")
	@Fetch(FetchMode.JOIN)
	public Photo getPhoto() {
		return this.photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category")
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_dt", length = 8)
	public Date getModifyDt() {
		return modifyDt;
	}

	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
	}

	public Integer getCover() {
		return cover;
	}

	public void setCover(Integer cover) {
		this.cover = cover;
	}

}
