package org.mfr.data;

import java.io.Serializable;
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

import org.mfr.data.Useracc;

@Entity
@Table(name = "useracc_data")
public class UseraccData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5108219999402818522L;
	private Integer id;
	private String key;
	private String value;
	private Useracc useracc;
	private Date modifyDt;
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "key", length = 64)
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Column(name = "value", length = 2000)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "useracc")
	public Useracc getUseracc() {
		return useracc;
	}
	public void setUseracc(Useracc useracc) {
		this.useracc = useracc;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_dt", length = 8)
	public Date getModifyDt() {
		return modifyDt;
	}
	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
	}
}
