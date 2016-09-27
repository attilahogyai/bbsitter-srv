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

@Entity
@Table(name = "Answer")
public class Answer {
	private Integer id;
	private Useracc useracc;
	private City city;
	private String cityName;
	private Question question;
	private QuestionOptions option;
	private float lat;
	private float lon;
	
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
	@JoinColumn(name = "useracc")
	public Useracc getUseracc() {
		return useracc;
	}
	public void setUseracc(Useracc useracc) {
		this.useracc = useracc;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "city")
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	@Column(name = "city_name", length = 3)
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "question")
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "option")
	public QuestionOptions getOption() {
		return option;
	}
	public void setOption(QuestionOptions option) {
		this.option = option;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLon() {
		return lon;
	}
	public void setLon(float lon) {
		this.lon = lon;
	}
	
}
