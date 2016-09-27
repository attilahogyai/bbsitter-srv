package org.mfr.data;

import java.sql.Timestamp;

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
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@TypeDef(name="String[]", typeClass=StringArrayType.class)
@Entity
@Table(name = "x_session")
public class XSession {
	private Useracc useracc;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "useracc")
	
	private String[] scopes;
	private String token;
	private Integer valid;
	private String remoteAddress;
	private String userAgent;
	private Timestamp expireDate;
	private String language;
	private Integer id;
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Type(type="String[]")
	@Column(name = "scopes")
	public String[] getScopes() {
		return scopes;
	}
	public void setScopes(String[] scopes) {
		this.scopes = scopes;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	
	@Column(name = "remote_address")
	public String getRemoteAddress() {
		return remoteAddress;
	}
	
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	@Column(name = "user_agent")
	public String getUserAgent() {
		return userAgent;
	}
	
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	@Column(name = "expire_date")
	public Timestamp getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Timestamp expireDate) {
		this.expireDate = expireDate;
	}
	@ManyToOne
	@JoinColumn(name = "useracc")
	@Fetch(FetchMode.JOIN)
	public Useracc getUseracc() {
		return useracc;
	}
	public void setUseracc(Useracc useracc) {
		this.useracc = useracc;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
}
