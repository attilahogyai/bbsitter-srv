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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "rank")
public class Rank {
	private Integer id;
	private Float rank;
	private Useracc useracc;
	private Photo photo;
	
	private Integer rankType;
	private XXprtDetail xprtDetail;
	private Comment comment;
	
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
	@Column(name = "rank",length=64)
	public Float getRank() {
		return rank;
	}
	public void setRank(Float rank) {
		this.rank = rank;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "useracc")
	public Useracc getUseracc() {
		return useracc;
	}
	public void setUseracc(Useracc useracc) {
		this.useracc = useracc;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "photo")	
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "xprt_detail")
	@Fetch(FetchMode.JOIN)		
	public XXprtDetail getXprtDetail() {
		return xprtDetail;
	}
	public void setXprtDetail(XXprtDetail xprtDetail) {
		this.xprtDetail = xprtDetail;
	}
	@Column(name = "rank_type",length=64)
	public Integer getRankType() {
		return rankType;
	}
	public void setRankType(Integer rankType) {
		this.rankType = rankType;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "comment")	
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	@Column(name = "modify_dt")	
	public Date getModifyDt() {
		return modifyDt;
	}
	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
	}
}
