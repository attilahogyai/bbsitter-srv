package org.mfr.rest.messages;

@JsonName(name="portfolioNames")
public class PortfolioNamesResponse {
	
	@CopyFrom(fieldName="portfolio")
	private String account;
	
	@CopyFrom(fieldName="portfolioTypeCode")
	private String accountTypeId;
	
	@CopyFrom(fieldName="portfolioTypeDesc")
	private String accountTypeDesc;
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(String accountTypeId) {
		if(accountTypeId!=null){
			accountTypeId=accountTypeId.equals("NORM")?"1":
				(accountTypeId.equals("NYESZ")?"100":"510");
		}
		this.accountTypeId = accountTypeId;
	}

	public String getAccountTypeDesc() {
		return accountTypeDesc;
	}

	public void setAccountTypeDesc(String accountTypeDesc) {
		this.accountTypeDesc = accountTypeDesc;
	}

}
