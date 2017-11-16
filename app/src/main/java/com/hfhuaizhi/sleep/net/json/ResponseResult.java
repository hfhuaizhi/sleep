package com.hfhuaizhi.sleep.net.json;

public class ResponseResult {
	public static final String SUCCESS = "success";
	public static final String FAIL = "fail";
	public String state;
	public String msg;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "Result [state=" + state + ", msg=" + msg + "]";
	}
	
}
