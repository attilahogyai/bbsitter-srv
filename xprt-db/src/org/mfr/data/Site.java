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

import org.zkoss.util.resource.Labels;

@Entity
@Table(name = "site")
public class Site implements java.io.Serializable {
	private Integer id;
	private String name;
	private String url;
	private String adminEmail;
	private Useracc owner;
	private String description;
	private String descriptionEn;
	
	private String about;
	private String aboutEn;
	
	private String contact;
	private String contactEn;
		
	private Integer enableAbout;
	private Integer enableContact;
	
	private Date modifyDt;
	
	private Integer state;
	
	private Integer style=0;
	
	private String password;
	
	private Integer listed;
	
	private Integer rank;
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "name", length = 255)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "url", length = 255)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name = "admin_email", length = 255)
	public String getAdminEmail() {
		return adminEmail;
	}
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "owner")
	public Useracc getOwner() {
		return owner;
	}
	public void setOwner(Useracc useracc) {
		this.owner = useracc;
	}
	@Column(name = "description", length = 10000)
	public String getDescription() {
		if(description==null){
			return Labels.getLabel("portfolio.description.empty");
		}
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "description_en", length = 10000)
	public String getDescriptionEn() {
		if(descriptionEn==null){
			return Labels.getLabel("portfolio.description.empty");
		}
		return descriptionEn;
	}
	public void setDescriptionEn(String description_en) {
		this.descriptionEn = description_en;
	}
	@Column(name = "about", length = 10000)
	public String getAbout() {
		if(about==null){
			return Labels.getLabel("portfolio.about.empty");
		}
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	@Column(name = "about_en", length = 10000)
	public String getAboutEn() {
		if(aboutEn==null){
			return Labels.getLabel("portfolio.about.empty");
		}
		return aboutEn;
	}
	public void setAboutEn(String aboutEn) {
		this.aboutEn = aboutEn;
	}
	@Column(name = "contact", length = 10000)
	public String getContact() {
		if(contact==null){
			return Labels.getLabel("portfolio.contact.empty");
		}
		return contact;
	}
	
	public void setContact(String contact) {
		this.contact = contact;
	}
	@Column(name = "contact_en", length = 10000)
	public String getContactEn() {
		if(contactEn==null){
			return Labels.getLabel("portfolio.contact.empty");
		}
		return contactEn;
	}
	public void setContactEn(String contactEn) {
		this.contactEn = contactEn;
	}
	
	@Column(name = "enable_about", length = 255)
	public Integer getEnableAbout() {
		return enableAbout;
	}
	
	public void setEnableAbout(Integer disableAbout) {
		this.enableAbout = disableAbout;
	}
	@Column(name = "enable_contact", length = 255)
	public Integer getEnableContact() {
		return enableContact;
	}
	public void setEnableContact(Integer disableContact) {
		this.enableContact = disableContact;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_dt", length = 8)
	public Date getModifyDt() {
		return modifyDt;
	}

	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
	}

	@Column(name = "state", length = 255)
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getStyle() {
		return style;
	}
	public void setStyle(Integer style) {
		this.style = style;
	}
	@Column(name = "password", length = 32)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name = "listed", length = 255)
	public Integer getListed() {
		return listed;
	}
	public void setListed(Integer listed) {
		this.listed = listed;
	}
	@Column(name = "rank", length = 255)
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
}
