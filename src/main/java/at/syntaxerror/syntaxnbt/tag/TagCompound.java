/* MIT License
 * 
 * Copyright (c) 2022 SyntaxError404
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package at.syntaxerror.syntaxnbt.tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.syntaxerror.syntaxnbt.NBTException;
import at.syntaxerror.syntaxnbt.internal.SNBTStringifyer;
import lombok.NonNull;

/**
 * Effectively a list of a <b>named</b> tags. Order is not guaranteed.
 * 
 * @author SyntaxError404
 * 
 */
public class TagCompound extends Tag<Map<String, Tag<?>>> {

	/**
	 * Constructs an empty compound tag
	 */
	public TagCompound() {
		this(Map.of());
	}
	
	/**
	 * Constructs a compound tag
	 * 
	 * @param tags the map whose mappings are to be placed in this map
	 */
	public TagCompound(Map<String, Tag<?>> tags) {
		super(TagType.COMPOUND, new HashMap<>(tags));
	}
	
	/**
	 * Returns true if this map contains a mapping for the specified key
	 * 
	 * @param key key whose presence in this map is to be tested
	 * @return true if this map contains a mapping for the specified key
	 */
	public boolean has(@NonNull String key) {
		return getValue().containsKey(key);
	}
	
	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public Tag<?> get(@NonNull String key) {
		if(!has(key))
			throw new NBTException("No such element: TAG_Compound[" + SNBTStringifyer.quote(key) + "]");
		
		return getValue().get(key);
	}
	
	@SuppressWarnings("unchecked")
	private <X extends Tag<?>> X get(String key, Class<X> clazz) {
		TagType type = TagType.getTypeFromClass(clazz);
		
		Tag<?> tag = get(key);
		
		if(!tag.is(type))
			throw new NBTException("TAG_Compound[" + SNBTStringifyer.quote(key) + "] is not a " + type);
		
		return (X) tag;
	}
	
	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public byte getByte(String key) {
		return getByteTag(key).getValue();
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public short getShort(String key) {
		return getShortTag(key).getValue();
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public int getInt(String key) {
		return getIntTag(key).getValue();
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public long getLong(String key) {
		return getLongTag(key).getValue();
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public float getFloat(String key) {
		return getFloatTag(key).getValue();
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public double getDouble(String key) {
		return getDoubleTag(key).getValue();
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public byte[] getByteArray(String key) {
		return getByteArrayTag(key).getValue();
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public int[] getIntArray(String key) {
		return getIntArrayTag(key).getValue();
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public long[] getLongArray(String key) {
		return getLongArrayTag(key).getValue();
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public String getString(String key) {
		return getStringTag(key).getValue();
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public TagByte getByteTag(String key) {
		return get(key, TagByte.class);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public TagShort getShortTag(String key) {
		return get(key, TagShort.class);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public TagInt getIntTag(String key) {
		return get(key, TagInt.class);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public TagLong getLongTag(String key) {
		return get(key, TagLong.class);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public TagFloat getFloatTag(String key) {
		return get(key, TagFloat.class);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public TagDouble getDoubleTag(String key) {
		return get(key, TagDouble.class);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public TagByteArray getByteArrayTag(String key) {
		return get(key, TagByteArray.class);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public TagIntArray getIntArrayTag(String key) {
		return get(key, TagIntArray.class);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public TagLongArray getLongArrayTag(String key) {
		return get(key, TagLongArray.class);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public TagString getStringTag(String key) {
		return get(key, TagString.class);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public TagCompound getCompound(String key) {
		return get(key, TagCompound.class);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or throws a {@link NBTException} if this map contains no mapping for the key
	 * 
	 * @param key key of the mapping to be returned
	 * @return the element at the specified position in this list
	 */
	public TagList<?> getList(String key) {
		return get(key, TagList.class);
	}
	
	/**
	 * Returns the value to which the specified key is mapped,
	 * or {@code defaultValue} if this map contains no mapping for the key.
	 * 
	 * @param key the key whose associated value is to be returned
	 * @param defaultValue the default mapping for the key
	 * @return the value to which the specified key is mapped, or {@code defaultValue} if this map contains no mapping for the key
	 */
	public Tag<?> get(@NonNull String key, Tag<?> defaultValue) {
		return getValue().getOrDefault(key, defaultValue);
	}
	
	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound put(@NonNull String key, @NonNull Tag<?> value) {
		if(value.is(TagType.END))
			throw new NBTException("Cannot add TAG_End to TAG_Compound");
		
		getValue().put(key, value);
		return this;
	}
	
	private <V, X extends Tag<V>> TagCompound put(String key, V value, Class<X> clazz) {
		try {
			getValue().put(key, (Tag<?>) TagType
				.getTypeFromClass(clazz)
				.getTagClass()
				.getConstructor(value.getClass())
				.newInstance(value)
			);
			
			return this;
		} catch(NBTException e) {
			throw e;
		} catch(Exception e) {
			throw new NBTException(e);
		}
	}

	private <X extends Tag<?>> TagCompound putTag(String key, X value) {
		getValue().put(key, value);
		return this;
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putByte(String key, byte value) {
	    return put(key, value, TagByte.class);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putShort(String key, short value) {
	    return put(key, value, TagShort.class);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putInt(String key, int value) {
	    return put(key, value, TagInt.class);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putLong(String key, long value) {
	    return put(key, value, TagLong.class);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putFloat(String key, float value) {
	    return put(key, value, TagFloat.class);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putDouble(String key, double value) {
	    return put(key, value, TagDouble.class);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putByteArray(String key, byte[] value) {
	    return put(key, value, TagByteArray.class);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putIntArray(String key, int[] value) {
	    return put(key, value, TagIntArray.class);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putLongArray(String key, long[] value) {
	    return put(key, value, TagLongArray.class);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putString(String key, String value) {
	    return put(key, value, TagString.class);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putByteTag(String key, TagByte value) {
	    return putTag(key, value);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putShortTag(String key, TagShort value) {
	    return putTag(key, value);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putIntTag(String key, TagInt value) {
	    return putTag(key, value);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putLongTag(String key, TagLong value) {
	    return putTag(key, value);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putFloatTag(String key, TagFloat value) {
	    return putTag(key, value);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putDoubleTag(String key, TagDouble value) {
	    return putTag(key, value);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putByteArrayTag(String key, TagByteArray value) {
	    return putTag(key, value);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putIntArrayTag(String key, TagIntArray value) {
	    return putTag(key, value);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putLongArrayTag(String key, TagLongArray value) {
	    return putTag(key, value);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putStringTag(String key, TagString value) {
	    return putTag(key, value);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putCompound(String key, TagCompound value) {
	    return putTag(key, value);
	}

	/**
	 * Associates the specified value with the specified key in this compound tag
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return this compound tag
	 */
	public TagCompound putList(String key, TagList<?> value) {
	    return putTag(key, value);
	}
	
	/**
	 * Removes the mapping for a key from this compound tag if it is present
	 * 
	 * @param key key whose mapping is to be removed from the compound tag
	 * @return this compound tag
	 */
	public TagCompound remove(@NonNull String key) {
		getValue().remove(key);
		return this;
	}
	
	/**
	 * Returns the number of key-value mappings in this compound tag
	 * 
	 * @return the number of key-value mappings in this compound tag
	 */
	public int size() {
		return getValue().size();
	}

    /**
     * Returns {@code true} if this compound tag contains no key-value mappings.
     *
     * @return {@code true} if this compound tag contains no key-value mappings
     */
	public boolean isEmpty() {
		return getValue().isEmpty();
	}
	
	/**
	 * Returns a set view of the keys contained in this map
	 * 
	 * @return a set view of the keys contained in this map
	 */
	public Set<String> keySet() {
		return getValue().keySet();
	}
	
	/**
	 * Removes all of the mappings from this map
	 * 
	 * @return this compound tag
	 */
	public TagCompound clear() {
		getValue().clear();
		return this;
	}
	
	/**
	 * Checks whether all mappings of the specified compound tag
	 * also exist within this compound tag and are {@link Tag#equals(Object) equal}.
	 * 
	 * @param other compound to be checked
	 * @return whether this compound tag is a superset of the specified compound tag
	 */
	public boolean contains(TagCompound other) {
		for(String key : other.keySet())
			if(!has(key) || !get(key).equals(other.get(key)))
				return false;
		
		return true;
	}

}
