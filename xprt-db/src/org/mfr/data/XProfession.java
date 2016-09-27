package org.mfr.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "x_profession")
public class XProfession {
	private Integer id;
	private String name;
	public XProfession(){
		super();
	}
	public XProfession(String id){
		this.id=Integer.parseInt(id);
	}
	
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
	@Override
	public int hashCode() {
		return 0;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof XProfession){
			return ((XProfession)obj).id.equals(this.id);
		}else{
			return false;
		}
	}	
}
