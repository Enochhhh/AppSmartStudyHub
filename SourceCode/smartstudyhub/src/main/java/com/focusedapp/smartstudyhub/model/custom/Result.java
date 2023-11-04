package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.util.enumerate.StatusCode;

import net.minidev.json.JSONObject;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private MetaInfo meta;
	private T data;
	private String logInfo;
	private String url;
	private JSONObject extendProp;
	
	public Result() {
		meta = new MetaInfo();
		data = null;
	}
	
	public Result(MetaInfo meta, T data) {
		this.meta = meta;
		this.data = data;
	}
	
	public Result(Result<T>.MetaInfo meta, T data, String url, String logInfo, JSONObject extendProp) {
		super();
		this.meta = meta;
		this.data = data;
		this.url = url;
		this.logInfo = logInfo;
		this.extendProp = extendProp;
	}

	public MetaInfo getMeta() {
		return meta;
	}
	
	public void setValueExtendProp(String name, Object value) {
		if (this.extendProp == null) {
			this.extendProp = new JSONObject();
		}
		this.extendProp.put(name, value);
	}

	public void setMeta(MetaInfo meta) {
		this.meta = meta;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public JSONObject getExtendProp() {
		return extendProp;
	}

	public void setExtendProp(JSONObject extendProp) {
		this.extendProp = extendProp;
	}
	

	public class MetaInfo implements Serializable {
		
		private static final long serialVersionUID = 1L;
		private String statusCode;
		private String message;
		
		public MetaInfo() {
			this.statusCode = StatusCode.SUCCESS.getCode();
			this.message = StatusCode.SUCCESS.getMessage();
		}
		
		public MetaInfo(String statusCode, String message) {
			this.statusCode = statusCode;
			this.message = message;
		}
		
		@JsonProperty("code")
		public String getStatusCode() {
			return statusCode;
		}
		
		@JsonProperty("code")
		public void setStatusCode(String statusCode) {
			this.statusCode = statusCode;
		}
		
		public String getMessage() {
			return message;
		}
		
		public void setMessage(String message) {
			this.message = message;
		}

	}

}
