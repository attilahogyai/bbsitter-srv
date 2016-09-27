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
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
@TypeDefs(
		{
			@TypeDef(name="String[]", typeClass=StringArrayType.class), 
			@TypeDef(name="Integer[]", typeClass=IntegerArrayType.class)
		}
)


@Entity
@Table(name = "x_xprt_detail")
public class XXprtDetail {
	private Integer id;
	private Useracc useracc;
	private String locationStr;
	private String name;
	private String email;
	private String phone;
	private Integer birthYear;
	private Integer sex;
	
	private Country country;
	
	private Integer[] targetLocations;
	
	private Integer educationLevel;
	//private Integer educationType;
	private String education;
	
	private String description;
	private Integer experience;
	
	private Integer[] languages;
	
	private Integer unitPrice;
	private String currency;
	
	private Integer serviceLocationType;
	private City location;
	private String address;
	private String zip;
	
	private Float minHour;
	private Float maxHour;
	
	
	private XProfession profession;
	private Integer intProp1;
	private Integer top;
	
	private Integer[] checkedIds;
	
	private Integer driveLicense;
	private Integer ownCar;
	private Integer status;
	
	private Date modifyDt;
	private Date createDt;
	private transient Float rank;
	
	
	
	public XXprtDetail(){
	}	
	public XXprtDetail(String id){
		this.id=Integer.parseInt(id);
	}
	public XXprtDetail(Integer id){
		this.id=id;
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
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "useracc")
	public Useracc getUseracc() {
		return useracc;
	}
	public void setUseracc(Useracc useracc) {
		this.useracc = useracc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Type(type="Integer[]")
	@Column(name = "languages")
	public Integer[] getLanguages() {
		return languages;
	}
	public void setLanguages(Integer[]  language) {
		this.languages = language;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public int hashCode() {
		if(useracc!=null){
			return useracc.getId();
		}else{
			return 0;
		}
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XXprtDetail){
			return ((XXprtDetail)obj).id.equals(this.id);
		}else{
			return false;
		}
	}
	@Column(name = "location_str")

	public String getLocationStr() {
		return locationStr;
	}
	public void setLocationStr(String locationStr) {
		this.locationStr = locationStr;
	}
	@Column(name = "unit_price")
	public Integer getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Integer unitPrice) {
		this.unitPrice = unitPrice;
	}
	@Column(name = "min_hour")
	public Float getMinHour() {
		return minHour;
	}
	public void setMinHour(Float minHour) {
		this.minHour = minHour;
	}
	@Column(name = "max_hour")
	public Float getMaxHour() {
		return maxHour;
	}
	public void setMaxHour(Float maxHour) {
		this.maxHour = maxHour;
	}
	@Column(name = "service_location_type")
	public Integer getServiceLocationType() {
		return serviceLocationType;
	}
	public void setServiceLocationType(Integer serviceLocationType) {
		this.serviceLocationType = serviceLocationType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country")

	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public void setLocation(City location) {
		this.location = location;
	}	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "location")
	public City getLocation(){
		return location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	@Column(name = "birth_year")
	public Integer getBirthYear() {
		return birthYear;
	}
	
	public void setBirthYear(Integer birthYear) {
		this.birthYear = birthYear;
	}
	@Column(name = "education_level")
	public Integer getEducationLevel() {
		return educationLevel;
	}
	public void setEducationLevel(Integer educationLevel) {
		this.educationLevel = educationLevel;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
//	@Column(name = "education_type")
//	public Integer getEducationType() {
//		return educationType;
//	}
//	public void setEducationType(Integer educationType) {
//		this.educationType = educationType;
//	}
	public Integer getExperience() {
		return experience;
	}
	public void setExperience(Integer exprience) {
		this.experience = exprience;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "profession")
	public XProfession getProfession() {
		return profession;
	}
	public void setProfession(XProfession profession) {
		this.profession = profession;
	}
	
	@PersistentObject(clazz=City.class)
	@Type(type="Integer[]")
	@Column(name = "target_locations")
	public Integer[] getTargetLocations() {
		return targetLocations;
	}

	public void setTargetLocations(Integer[] scopes) {
		this.targetLocations = scopes;
	}
	@Column(name = "int_prop_1")
	public Integer getIntProp1() {
		return intProp1;
	}
	public void setIntProp1(Integer intProp1) {
		this.intProp1 = intProp1;
	}
	public Integer getTop() {
		return top;
	}
	public void setTop(Integer top) {
		this.top = top;
	}
	@Type(type="Integer[]")
	@Column(name = "checked_ids")
	public Integer[] getCheckedIds() {
		return checkedIds;
	}
	public void setCheckedIds(Integer[] checkedIds) {
		this.checkedIds = checkedIds;
	}
	@Transient
	public Float getRank() {
		return rank;
	}
	public void setRank(Float rank) {
		this.rank = rank;
	}
	@Column(name = "drive_license")
	public Integer getDriveLicense() {
		return driveLicense;
	}
	public void setDriveLicense(Integer driveLicense) {
		this.driveLicense = driveLicense;
	}
	@Column(name = "own_car")
	public Integer getOwnCar() {
		return ownCar;
	}
	public void setOwnCar(Integer ownCar) {
		this.ownCar = ownCar;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "modify_dt")
	public Date getModifyDt() {
		return modifyDt;
	}
	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
	}
	@Column(name = "create_dt")
	public Date getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
}
