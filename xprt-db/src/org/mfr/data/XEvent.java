package org.mfr.data;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "x_event")
public class XEvent implements Sensible{
	@Transient
	private transient boolean sensible;
	private Integer id;
	@Hidden
	private String name;
	@Hidden
	private String description;
	private Timestamp startDate;
	private Timestamp endDate;
	@Hidden
	private String location;
	@Hidden
	private Useracc initiator;
	private Useracc host;
	@Hidden
	private Integer status;
	@Hidden
	private Integer notification;
	private XXprtDetail xprtDetail;
	@Hidden
	private Timestamp modifyDt;
	@Hidden
	private Timestamp createDt;
	@Hidden
	private Address address;
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	
	@Column(name="start_date")
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	@Column(name="end_date")
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	@ManyToOne
	@JoinColumn(name = "initiator")
	@Fetch(FetchMode.JOIN)
	public Useracc getInitiator() {
		return initiator;
	}
	public void setInitiator(Useracc initiator) {
		this.initiator = initiator;
	}
	@ManyToOne
	@JoinColumn(name = "host")
	@Fetch(FetchMode.JOIN)
	public Useracc getHost() {
		return host;
	}
	public void setHost(Useracc host) {
		this.host = host;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@ManyToOne
	@JoinColumn(name = "xprt_detail")
	@Fetch(FetchMode.JOIN)
	public XXprtDetail getXprtDetail() {
		return xprtDetail;
	}
	public void setXprtDetail(XXprtDetail xprtDetail) {
		this.xprtDetail = xprtDetail;
	}
	@Transient
	public boolean isSensible() {
		return sensible;
	}
	public void setSensible(boolean sensible) {
		this.sensible = sensible;
	}
	@Override
	public int hashCode() {
		if(host!=null){
			return host.getId();
		}
		return 0;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XEvent){
			return ((XEvent)obj).id.equals(this.id);
		}else{
			return false;
		}
	}
	@Column(name="modify_dt")
	public Timestamp getModifyDt() {
		return modifyDt;
	}
	public void setModifyDt(Timestamp modifyDt) {
		this.modifyDt = modifyDt;
	}
	@Column(name="create_dt")
	public Timestamp getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		return sb.append("{id:\"").append(getId()).append("\" status:\"").append((getStatus()==null?"null":getStatus()))
				.append("\" name:\"").append(getName()).append("\" host:\"").append((getHost()==null?"null":getHost().getId())).
				append("\" initiator:\"").append((getInitiator()==null?"null":getInitiator().getId())).append("\" start:\"").
				append(getStartDate()).append("\" xprt:\"").append(getXprtDetail()==null?"null":getXprtDetail().getId()).append("\"}").toString();
	}
	public Integer getNotification() {
		return notification;
	}
	public void setNotification(Integer notification) {
		this.notification = notification;
	}
	@ManyToOne
	@JoinColumn(name = "address")
	@Fetch(FetchMode.JOIN)	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
}
