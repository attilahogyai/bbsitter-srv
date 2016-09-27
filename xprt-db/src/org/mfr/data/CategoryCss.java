package org.mfr.data;

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
@Table(name = "category_css")
public class CategoryCss implements java.io.Serializable {
	private Integer id;
	private Category category;
	private Css css;
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "css")
	@Fetch(FetchMode.JOIN)	
	public Css getCss() {
		return css;
	}
	public void setCss(Css css) {
		this.css = css;
	}
}
