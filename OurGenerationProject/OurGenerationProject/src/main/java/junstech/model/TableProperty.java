package junstech.model;

import java.io.Serializable;

import junstech.util.AESEncryption;

public class TableProperty implements Serializable, Comparable {

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

	public int compareTo(Object other) {
		TableProperty temp = (TableProperty) other;
		if (convert(this.getValue().toString()) > convert(temp.getValue().toString())) {
			return -1;
		}
		return 0;
	}

	public static int convert(String n) {
		return Integer.valueOf(n, 16);
	}
}
