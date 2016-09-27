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
@Table(name = "address")
public class Address {
	
	private Integer id;
	private String name;
	private String street;
	private String zip;
	private City city;
	private Useracc useracc;
	
	public Address(){
	}
	
	public Address(String id){
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "city")	
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "useracc")	
	public Useracc getUseracc() {
		return useracc;
	}
	public void setUseracc(Useracc useracc) {
		this.useracc = useracc;
	}
	

}
