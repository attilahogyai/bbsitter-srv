package org.mfr.data;

// Generated 2011.09.19. 22:50:48 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * Useracc generated by hbm2java
 */
@TypeDef(name="String[]", typeClass=StringArrayType.class)
@Entity
@Table(name = "useracc")
public class Useracc implements Sensible {
	private static final long serialVersionUID = -7130151346959727437L;
	@Transient
	private transient boolean sensible=true;
	private Integer id;
	@Hidden
	private String login;
	private String name;
	@Hidden
	private String email;
	@Hidden
	private String password;
	@Hidden
	private Integer status=0;
	@Hidden
	private String privateCode;
	@Hidden
	private Date lastLogin;
	@Hidden
	private Integer gender;
	@Hidden
	private Integer provider=0;
	@Hidden
	private Long storageLimit=0L;
	@Hidden
	private Long usedStorage;
	@Hidden
	private Integer newsletter;
	@Hidden
	private String language;
	@Hidden
	private Date modifyDt;
	@Hidden
	private Boolean image;
	@Hidden
	private String phone;
	
	private Set<UseraccData> useraccDatas = new HashSet<UseraccData>(0);
	private UseraccPrefs useraccPrefs;
	
	private String[] scopes;
	
	private String pwChangeRequest;
	
	private Integer imagec;
	
	private Integer profession;
	
	@Type(type="String[]")
	@Column(name = "scopes")
	public String[] getScopes() {
		return scopes;
	}

	public void setScopes(String[] scopes) {
		this.scopes = scopes;
	}

	public Useracc() {
	}
	public Useracc(String id) {
		this.id=Integer.parseInt(id);
	}

	public Useracc(String login, String name, String email) {
		this.login = login;
		this.name = name;
		this.email = email;
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

	@Column(name = "login", length = 32)
	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Column(name = "name", length = 64, nullable= false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "private_code")
	public String getPrivateCode() {
		return privateCode;
	}

	public void setPrivateCode(String privateCode) {
		this.privateCode = privateCode;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "useracc")
	public Set<UseraccData> getUseraccDatas() {
		return this.useraccDatas;
	}
	
	public void setUseraccDatas(Set<UseraccData> exifDatas) {
		this.useraccDatas = exifDatas;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login", length = 8)
	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	@Column(name = "gender")
	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}
	@Column(name = "provider")
	public Integer getProvider() {
		return provider;
	}

	public void setProvider(Integer provider) {
		this.provider = provider;
	}
	@Column(name = "storage_limit",length=64)
	public Long getstorageLimit() {
		return storageLimit;
	}

	public void setstorageLimit(Long limit) {
		this.storageLimit = limit;
	}
	@Column(name = "newsletter")
	public Integer getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(Integer newletter) {
		this.newsletter = newletter;
	}
	@Column(name = "language")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	@Column(name = "used_storage")
	public Long getUsedStorage() {
		return usedStorage;
	}

	public void setUsedStorage(Long usedStorage) {
		this.usedStorage = usedStorage;
	}
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "useracc")
	public UseraccPrefs getUseraccPrefs() {
		return useraccPrefs;
	}

	public void setUseraccPrefs(UseraccPrefs useraccPrefs) {
		this.useraccPrefs = useraccPrefs;
	}
	@Transient
	public boolean isDisableEmailOwner(){
		return isDisableEmailOwner(getUseraccPrefs());
	}
	@Transient
	public boolean isDisableEmailOwner(UseraccPrefs prefs){
		if(prefs!=null && prefs.getDisableEmail()!=null && prefs.getDisableEmail().equals(1)){
			return true;
		}else{
			return false;
		}
	}
	@Column(name = "modify_dt")
	public Date getModifyDt() {
		return modifyDt;
	}

	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
	}

	public Boolean getImage() {
		return image;
	}

	public void setImage(Boolean image) {
		this.image = image;
	}
	@Transient
	public boolean isSensible() {
		return sensible;
	}
	public void setSensible(boolean sensible) {
		this.sensible = sensible;
	}
	@Column(name="pw_change_request")
	public String getPwChangeRequest() {
		return pwChangeRequest;
	}

	public void setPwChangeRequest(String pwChangeRequest) {
		this.pwChangeRequest = pwChangeRequest;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getImagec() {
		return imagec;
	}

	public void setImagec(Integer imagec) {
		this.imagec = imagec;
	}

	public Integer getProfession() {
		return profession;
	}

	public void setProfession(Integer profession) {
		this.profession = profession;
	}	
	
}
