package junstech.model;

import java.io.Serializable;

public class TableProperty implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1037644917672881741L;

	private String key;
	
	private Object value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public TableProperty() {

	}

	public TableProperty(String key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}

}
