package org.mfr.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "x_setup")
public class XSetup {
	private Useracc useracc;
	
	private String settings; 
	
	private String name;
	
	private XXprtDetail xprtDetail;
	
	private Integer id;
	
	private Integer notiGap;
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSettings() {
		return settings;
	}
	public void setSettings(String settings) {
		this.settings = settings;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "useracc")
	@Fetch(FetchMode.JOIN)
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
	@OneToOne
	@JoinColumn(name = "xprt_detail")
	@Fetch(FetchMode.JOIN)
	public XXprtDetail getXprtDetail() {
		return xprtDetail;
	}
	public void setXprtDetail(XXprtDetail xprtDetail) {
		this.xprtDetail = xprtDetail;
	}
	@Column(name = "noti_gap")
	public Integer getNotiGap() {
		return notiGap;
	}
	public void setNotiGap(Integer notiGap) {
		this.notiGap = notiGap;
	}
	
}
