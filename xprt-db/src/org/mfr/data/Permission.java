package org.mfr.data;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
import org.mfr.data.Useracc;

@Entity
@Table(name = "permission")
public class Permission {
	private Integer id;
	private Category category;
	private Useracc useracc;
	private String ticket;
	private Date validTill;
	private String sentTo;
	private Integer accessCount;
	private String name;
	private Date modifyDt;
	private String description;
	
	private Integer allowUpload;
	private Integer allowModify;
	private Integer allowDelete;
	
	private Useracc assignedUseracc;
	
	private String password;
	private Site site;
	
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
	@JoinColumn(name = "useracc")
	public Useracc getUseracc() {
		return useracc;
	}
	public void setUseracc(Useracc useracc) {
		this.useracc = useracc;
	}
	
	@Column(name = "ticket", length = 64)
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "valid_till", length = 8)
	public Date getValidTill() {
		return validTill;
	}
	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}
	@Column(name = "sent_to", length = 512)
	public String getSentTo() {
		return sentTo;
	}
	public void setSentTo(String sentTo) {
		this.sentTo = sentTo;
	}
	@Column(name = "access_count",length=64)
	public Integer getAccessCount() {
		return accessCount;
	}
	public void setAccessCount(Integer accessCount) {
		this.accessCount = accessCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_dt", length = 8)
	public Date getModifyDt() {
		return modifyDt;
	}
	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
	}
	@Column(name = "description", length = 2000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	

	@Column(name = "allow_upload",length=64)
	public Integer getAllowUpload() {
		return allowUpload;
	}
	public void setAllowUpload(Integer allowCreate) {
		this.allowUpload = allowCreate;
	}
	@Column(name = "allow_modify",length=64)
	public Integer getAllowModify() {
		return allowModify;
	}
	public void setAllowModify(Integer allowModify) {
		this.allowModify = allowModify;
	}
	@Column(name = "allow_delete",length=64)
	public Integer getAllowDelete() {
		return allowDelete;
	}
	public void setAllowDelete(Integer allowDelete) {
		this.allowDelete = allowDelete;
	}
	@Column(name = "password",length=64)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site")
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "assigned_useracc")
	@Fetch(FetchMode.JOIN)	
	public Useracc getAssignedUseracc() {
		return assignedUseracc;
	}
	public void setAssignedUseracc(Useracc assignedUseracc) {
		this.assignedUseracc = assignedUseracc;
	}
}
