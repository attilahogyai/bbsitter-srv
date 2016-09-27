package org.mfr.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "x_language")
public class XLanguage {
	private Integer id;
	private String code;
	private String description;
	public XLanguage(){
		super();
	}
	public XLanguage(String id){
		this.id=Integer.parseInt(id);
	}
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String descriptiuon) {
		this.description = descriptiuon;
	}
	
}
