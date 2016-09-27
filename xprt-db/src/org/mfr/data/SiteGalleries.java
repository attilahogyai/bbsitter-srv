package org.mfr.data;

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

@Entity
@Table(name = "site_galleries")
public class SiteGalleries implements java.io.Serializable {
	private Integer id;
	private Category category;
	private Site site;
	private Integer type;
	private Photo photo;
	private Date modifyDt;
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category")
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "site")	
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	
	@Column(name = "type", length = 64)
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "photo")
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_dt", length = 8)
	public Date getModifyDt() {
		return modifyDt;
	}

	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
	}
}
