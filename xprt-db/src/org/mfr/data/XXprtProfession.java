package org.mfr.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "x_xprt_profession")
public class XXprtProfession {
	private Integer id;
	private XXprtDetail xprtDetail;
	private XProfession profession;
	private String professionDesc;
	private Integer study;
	
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
	@JoinColumn(name = "xprt_detail")
	public XXprtDetail getXprtDetail() {
		return xprtDetail;
	}
	public void setXprtDetail(XXprtDetail xprtDetail) {
		this.xprtDetail = xprtDetail;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "profession")
	public XProfession getProfession() {
		return profession;
	}
	public void setProfession(XProfession profession) {
		this.profession = profession;
	}
	@Column(name = "profession_desc")
	public String getProfessionDesc() {
		return professionDesc;
	}
	public void setProfessionDesc(String professionDesc) {
		this.professionDesc = professionDesc;
	}
	public Integer getStudy() {
		return study;
	}
	public void setStudy(Integer study) {
		this.study = study;
	}
	@Override
	public int hashCode() {
		if(xprtDetail!=null){
			return xprtDetail.getId();
		}else{
			return 0;
		}
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XXprtProfession){
			return ((XXprtProfession)obj).id.equals(this.id);
		}else{
			return false;
		}
	}
	
}
