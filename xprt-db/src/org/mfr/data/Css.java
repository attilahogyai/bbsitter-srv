package org.mfr.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "css")
public class Css {
	private Integer id;
	private String url;
	private String name;
	private String nameEn;
	private String apply;
	private String customStyle;
	private String targetObject;
	private Integer group;
	private Integer def;
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "url", length = 255)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name = "name", length = 255)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "name_en", length = 255)
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String name_en) {
		this.nameEn = name_en;
	}
	@Column(name = "apply", length = 255)
	public String getApply() {
		return apply;
	}
	public void setApply(String apply) {
		this.apply = apply;
	}
	@Column(name = "custom_style", length = 2000)
	public String getCustomStyle() {
		return customStyle;
	}
	public void setCustomStyle(String customStyle) {
		this.customStyle = customStyle;
	}
	@Column(name = "target_object", length = 2000)
	public String getTargetObject() {
		return targetObject;
	}
	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}
	@Column(name = "group", length = 255)
	public Integer getGroup() {
		return group;
	}
	public void setGroup(Integer group) {
		this.group = group;
	}
	@Column(name = "def", length = 255)
	public Integer getDef() {
		return def;
	}
	public void setDef(Integer def) {
		this.def = def;
	}
}
