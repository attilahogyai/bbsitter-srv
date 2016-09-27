package org.mfr.data;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "x_log")
public class XLog {
	private Integer id;
	private Useracc useracc;
	private Integer event;
	private Integer type;
	private Timestamp createDt;
	private String comment;
	private String log;
	private String objectData;
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name = "useracc")
	@Fetch(FetchMode.JOIN)
	public Useracc getUseracc() {
		return useracc;
	}
	public void setUseracc(Useracc useracc) {
		this.useracc = useracc;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column(name = "create_dt")
	public Timestamp getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public Integer getEvent() {
		return event;
	}
	public void setEvent(Integer event) {
		this.event = event;
	}
	@Column(name = "object_data")
	public String getObjectData() {
		return objectData;
	}
	public void setObjectData(String objectData) {
		this.objectData = objectData;
	}
	
}
