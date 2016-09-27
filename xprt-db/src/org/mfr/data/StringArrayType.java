package org.mfr.data;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

public class StringArrayType implements UserType {

	protected static final int SQLTYPE = java.sql.Types.ARRAY;

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor arg2, Object owner) throws HibernateException,
			SQLException {
		Array array = rs.getArray(names[0]);
		String[] javaArray =null;
		if(array!=null)	javaArray = (String[]) array.getArray();
		return javaArray;
	}

	@Override
	public void nullSafeSet(PreparedStatement statement, Object object, int i,
			SessionImplementor arg3) throws HibernateException, SQLException {
		Connection connection = statement.getConnection();
		String[] strings=new String[0]; 
		if(object!=null){
			strings = (String[]) object;
		}
		Array array = connection.createArrayOf("varchar", strings);
		statement.setArray(i, array);
	}

	
	@Override
	public Object assemble(Serializable cached, Object owner)
	        throws HibernateException {
	    return deepCopy(cached);
	}
	
	@Override
	public Object deepCopy(final Object o) throws HibernateException {
		return o == null ? null : ((String[]) o).clone();
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
	    return (Serializable) deepCopy(value);
	}
	 
	
	
	@Override
	public boolean equals(final Object x, final Object y)
			throws HibernateException {
		return x == null ? y == null : x.equals(y);
	}

	@Override
	public int hashCode(final Object o) throws HibernateException {
		return o == null ? 0 : o.hashCode();
	}

	@Override
	public boolean isMutable() {
		return true;
	}
	 
	
	
	@Override
	public Object replace(Object original, Object target, Object owner)
	        throws HibernateException {
	    return deepCopy(original);
	}

	@Override
	public Class<String[]> returnedClass() {
		return String[].class;
	}

	@Override
	public int[] sqlTypes() {
		return new int[] { SQLTYPE };
	}
}