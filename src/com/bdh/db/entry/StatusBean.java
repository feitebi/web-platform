package com.bdh.db.entry;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "status")
public class StatusBean {

    private Integer flag = 1;

    private String errorMsg = "";

    private Long targetId = -1L;

    private String otherCode = "";
    
    private String OtherCode1="";

    public StatusBean() {

    }

    public String getOtherCode1() {
		return OtherCode1;
	}

	public void SetOtherCode1(String setOtherCode1) {
		this.OtherCode1 = setOtherCode1;
	}

	public StatusBean(int flag, String errorMsg) {
        this.flag = flag;
        this.errorMsg = errorMsg;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * @return the targetId
     */
    public Long getTargetId() {
        return targetId;
    }

    /**
     * @param targetId the targetId to set
     */
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getOtherCode() {
        return otherCode;
    }

    public void setOtherCode(String otherCode) {
        this.otherCode = otherCode;
    }

	

}
