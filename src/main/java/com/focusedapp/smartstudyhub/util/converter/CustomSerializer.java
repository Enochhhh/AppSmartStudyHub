package com.focusedapp.smartstudyhub.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;

import com.focusedapp.smartstudyhub.exception.ISException;

public class CustomSerializer {

	private Converter<Object, byte[]> serializer = new SerializingConverter();
	private Converter<byte[], Object> deserializer = new DeserializingConverter();
	
	static final byte[] EMPTY_ARRAY = new byte[0];

	public Object deserialize(byte[] bytes) {
	    if (isEmpty(bytes)) {
	        return null;
	    }

	    try {
	        return deserializer.convert(bytes);
	    } catch (Exception ex) {
	        throw new ISException(ex);
	    }
	}

	public byte[] serialize(Object object) {
	    if (object == null) {
	        return EMPTY_ARRAY;
	    }

	    try {
	        return serializer.convert(object);
	    } catch (Exception ex) {
	        return EMPTY_ARRAY;

	    }
	}

	private boolean isEmpty(byte[] data) {
	    return (data == null || data.length == 0);
	}
	
}
