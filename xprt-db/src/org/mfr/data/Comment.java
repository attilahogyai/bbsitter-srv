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
@Table(name = "comment")
public class Comment {
	private Integer id;
	private String comment;
	private Photo photo;
	private XXprtDetail xprtDetail;
	private Useracc useracc;
	private Comment original;
	private Category category;
	private String name;
	
	private Date createDt;
	private Date modifyDt;
	private Integer status;
	private Integer source;
	
	private Useracc addressee;
	
	public Comment(){
		
	}
	public Comment(String id){
		this.id=Integer.parseInt(id);
	}
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "comment")
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "photo")
	@Fetch(FetchMode.JOIN)	
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "original")
	@Fetch(FetchMode.JOIN)	
	public Comment getOriginal() {
		return original;
	}
	public void setOriginal(Comment original) {
		this.original = original;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category")
	@Fetch(FetchMode.JOIN)	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	@Column(name = "create_dt")
	public Date getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "modify_dt")
	public Date getModifyDt() {
		return modifyDt;
	}
	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
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
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "addressee")
	@Fetch(FetchMode.JOIN)	
	public Useracc getAddressee() {
		return addressee;
	}
	public void setAddressee(Useracc addressee) {
		this.addressee = addressee;
	}
}
