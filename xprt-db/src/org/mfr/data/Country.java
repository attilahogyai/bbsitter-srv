package org.mfr.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "country")
public class Country {
	private String id;
	//private String countryCode;
	private String countryName;
	public Country(){
	}	
	public Country(String code){
		this.id=code;
	}
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
//	@Column(name = "country_code", length = 3)
//	public String getCountryCode() {
//		return countryCode;
//	}
//	public void setCountryCode(String countryCode) {
//		this.countryCode = countryCode;
//	}
	
	@Column(name = "country_name", length = 3)
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

}
