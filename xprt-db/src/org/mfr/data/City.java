package org.mfr.data;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity(name="city")
@Table(name = "weblocations")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class City {
	private Integer id;
	private String countryCode;
	private String stateCode;
	private String cityName;
	private String timezoneid;
	public City(){
	}	
	public City(Integer id){
		this.id=id;
	}
	public City(String id){
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
	@Column(name = "country_code", length = 3)

	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	@Column(name = "state_code", length = 3)

	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String startCode) {
		this.stateCode = startCode;
	}
	@Column(name = "city_name", length = 3)
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getTimezoneid() {
		return timezoneid;
	}
	public void setTimezoneid(String timezoneid) {
		this.timezoneid = timezoneid;
	}

}
