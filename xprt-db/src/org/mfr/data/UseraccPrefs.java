package org.mfr.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


@Entity
@Table(name = "useracc_prefs")
public class UseraccPrefs implements Serializable{
	
	private static final long serialVersionUID = -6889234804306241357L;

	private Integer disableEmail=0;
	
	private Integer useraccId;
	private Useracc useracc;
	private String googleAccessToken;
	private String googleRefreshToken;
	private String facebookAccessToken;
	private String facebookRefreshToken;
	
	@Column(name = "disable_email")
	public Integer getDisableEmail() {
		return disableEmail;
	}
	public void setDisableEmail(Integer disableEmail) {
		this.disableEmail = disableEmail;
	}
	
	
	@GenericGenerator(name = "generator", strategy = "foreign", 
	parameters = @Parameter(name = "property", value = "useracc"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "useracc_id", unique = true, nullable = false)
	
	public Integer getUseraccId() {
		return useraccId;
	}
	public void setUseraccId(Integer id) {
		this.useraccId=id;
	}
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Useracc getUseracc() {
		return useracc;
	}
	public void setUseracc(Useracc useracc) {
		this.useracc = useracc;
	}
	@Column(name = "google_access_token")
	public String getGoogleAccessToken() {
		return googleAccessToken;
	}
	public void setGoogleAccessToken(String googleAccessToken) {
		this.googleAccessToken = googleAccessToken;
	}
	@Column(name = "google_refresh_token")
	public String getGoogleRefreshToken() {
		return googleRefreshToken;
	}
	public void setGoogleRefreshToken(String googleRefreshToken) {
		this.googleRefreshToken = googleRefreshToken;
	}
	@Column(name = "facebook_refresh_token")
	public String getFacebookRefreshToken() {
		return facebookRefreshToken;
	}
	public void setFacebookRefreshToken(String facebookRefreshToken) {
		this.facebookRefreshToken = facebookRefreshToken;
	}
	@Column(name = "facebook_access_token")
	public String getFacebookAccessToken() {
		return facebookAccessToken;
	}
	public void setFacebookAccessToken(String facebookAccessToken) {
		this.facebookAccessToken = facebookAccessToken;
	}
	
	
	
}
