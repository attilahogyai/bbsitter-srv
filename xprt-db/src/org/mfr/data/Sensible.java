package org.mfr.data;

import java.io.Serializable;

public interface Sensible extends Serializable{
	public boolean isSensible();
	public void setSensible(boolean b);
}
