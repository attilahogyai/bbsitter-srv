package org.mfr.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "dictionary")
@NamedQueries({
@NamedQuery(name="Dictionary.findByKey",query="from Dictionary where key=:key"),
@NamedQuery(name="Dictionary.findByKeySiteTemplate",query="from Dictionary where key=:key and ((site.id=:site and template=null) or (site=null and template=:template))"),
@NamedQuery(name="Dictionary.findBySite",query="from Dictionary where site=:site")
		}
		)
public class Dictionary {
	private Integer id;
	private String key;
	private String value;
	private String valueRo;
	private String valueEn;
	private Site site;
	private Integer template;
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
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	@Column(name = "value_ro", length = 10000)
	public String getValueRo() {
		return valueRo;
	}
	public void setValueRo(String valueRo) {
		this.valueRo = valueRo;
	}
	@Column(name = "value_en", length = 10000)
	public String getValueEn() {
		return valueEn;
	}
	public void setValueEn(String valueEn) {
		this.valueEn = valueEn;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site")
	@Fetch(FetchMode.JOIN)	
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	public Integer getTemplate() {
		return template;
	}
	public void setTemplate(Integer template) {
		this.template = template;
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
