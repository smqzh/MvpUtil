package com.qzh.mvputil.entity;

/**
 * 提交参数的格式封装
 * @author ling_cx
 * @date 2017/8/7.
 */

public class SubmitParams {
	private String  ticket;
	private String operateType;
	private String paraType;
	private String paraInfo;
	private boolean soapJson;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getParaType() {
		return paraType;
	}

	public void setParaType(String paraType) {
		this.paraType = paraType;
	}

	public String getParaInfo() {
		return paraInfo;
	}

	public void setParaInfo(String paraInfo) {
		this.paraInfo = paraInfo;
	}

	public boolean getSoapJson() {
		return soapJson;
	}

	public void setSoapJson(boolean soapJson) {
		this.soapJson = soapJson;
	}

	public SubmitParams() {
	}

	public SubmitParams(String ticket, String operateType, String paraType, String paraInfo, boolean soapJson) {
		this.ticket = ticket;
		this.operateType = operateType;
		this.paraType = paraType;
		this.paraInfo = paraInfo;
		this.soapJson = soapJson;
	}

	public SubmitParams(String ticket, String operateType, String paraType, boolean soapJson) {
		this.ticket = ticket;
		this.operateType = operateType;
		this.paraType = paraType;
		this.soapJson = soapJson;
	}

	@Override
	public String toString() {
		return "SubmitParams{" +
				"ticket='" + ticket + '\'' +
				", operateType='" + operateType + '\'' +
				", paraType='" + paraType + '\'' +
				", paraInfo='" + paraInfo + '\'' +
				", soapJson=" + soapJson +
				'}';
	}
}

