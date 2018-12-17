package com.autoreco.controller;

public class Info {
	private String region;
	private String[] licenseNo;
	private String username;
	private String birth;
	private String licenseType;
	private String startDate;
	private String endDate;
	
	public String getRegion(){
		return region;
	}
	
	public void setRegion(String region) {
		
		switch(region) {
		
		case "서울" : this.region="11"; break;
		case "부산" : this.region="12"; break;
		case "경기" : this.region="13"; break;
		case "경기남부" : this.region="13"; break;
		case "강원" : this.region="14"; break;
		case "충북" : this.region="15"; break;
		case "충남" : this.region="16"; break;
		case "전북" : this.region="17"; break;
		case "전남" : this.region="18"; break;
		case "경북" : this.region="19"; break;
		case "경남" : this.region="20"; break;
		case "제주" : this.region="21"; break;
		case "대구" : this.region="22"; break;
		case "인천" : this.region="23"; break;
		case "광주" : this.region="24"; break;
		case "대전" : this.region="25"; break;
		case "울산" : this.region="26"; break;
		case "경기북부" : this.region="28"; break;
		
		default : this.region=region; break;
		}	
	}
	
	public String[] getLicenseNo() {
		return licenseNo;
	}
	
	public void setLicenseNo(String[] licenseNo) {	
		this.licenseNo=licenseNo;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		birth = birth.replace("-", "");
		this.birth=birth;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
	
		switch(licenseType) {
		
			case "1종대형" : this.licenseType="11"; break;
			case "1종보통" : this.licenseType="12"; break;
			case "1종소형" : this.licenseType="13"; break;
			case "대형견인차(트레일러)" : this.licenseType="14"; break;
			case "구난차(레커)" : this.licenseType="15"; break;
			case "소형견인차" : this.licenseType="16"; break;
			case "2종보통" : this.licenseType="32"; break;
			case "2종소형" : this.licenseType="33"; break;
			case "2종원자" : this.licenseType="38"; break;
			default : this.licenseType=licenseType; break;
		}
		
	
		this.licenseType=licenseType;
		
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate.replace("-", "");
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate.replace("-", "");
	}

}
