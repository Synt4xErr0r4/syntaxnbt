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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import at.syntaxerror.syntaxnbt.NBTException;
import lombok.Getter;

/**
 * A list of <b>nameless</b> tags, all of the same type. The list is prefixed with the {@link TagType Type ID} of the items it contains
 * (thus 1 byte), and the length of the list as a <b>signed</b> integer (a further 4 bytes). If the length of the list is 0 or negative,
 * the type may be 0 (TAG_End) but otherwise it must be any other type. (The notchian implementation uses TAG_End in that situation,
 * but another reference implementation by Mojang uses 1 instead; parsers should accept any type if the length is &lt;= 0).
 * 
 * @author SyntaxError404
 * 
 * @param <T> type of the elements stored in this tag
 */
public class TagList<T extends Tag<?>> extends Tag<List<T>> implements Iterable<T> {
	
	/**
	 * Constructs an empty list without a specified type.
	 * The type is determined when an element is added or
	 * the list is casted to a specific type.
	 * 
	 * @return the empty list
	 */
	public static TagList<Tag<?>> emptyList() {
		return new TagList<>();
	}
	
	/**
	 * Returns the type of elements in this list.
	 * May be {@code null} if the list was created via {@link #emptyList()}
	 * 
	 * @return the type of elements in this list
	 */
	@Getter
	private TagType componentType;
	
	/**
	 * Constructs an empty list of the specified type
	 * 
	 * @param typeClass type of elements in this list
	 */
	public TagList(Class<T> typeClass) {
		this(typeClass, List.of());
	}

	/**
	 * Constructs a list of the specified type
	 * 
	 * @param typeClass type of elements in this list
	 * @param elements the collection whose elements are to be placed into this list
	 */
	public TagList(Class<T> typeClass, Collection<T> elements) {
		super(TagType.LIST, new ArrayList<>(elements));
		
		componentType = TagType.getTypeFromClass(typeClass);
		
		if(componentType == TagType.END)
			throw new NBTException("Cannot create TAG_List for TAG_End");
	}
	
	private TagList() {
		super(TagType.LIST, new ArrayList<>());
		
		componentType = null;
	}
	
	private TagType check(Class<?> clazz) {
		TagType type = TagType.getTypeFromClass(clazz);;
		
		if(componentType == null)
			componentType = type;
		
		else if(type != componentType)
			throw new NBTException("Incompatible type " + type + " for TAG_List[" + componentType + "]");
		
		return type;
	}

	@SuppressWarnings("unchecked")
	private <V, X extends Tag<V>> T process(V value, Class<X> clazz) {
		try {
			return (T) check(clazz)
				.getTagClass()
				.getConstructor(value.getClass())
				.newInstance(value);
		} catch(NBTException e) {
			throw e;
		} catch(Exception e) {
			throw new NBTException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private <X extends Tag<?>> T processTag(X value, Class<X> clazz) {
		check(clazz);
		return (T) value;
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public T get(int index) {
		return getValue().get(index);
	}
	
	@SuppressWarnings("unchecked")
	private <X extends Tag<?>> X get(int index, Class<X> clazz) {
		TagType type = TagType.getTypeFromClass(clazz);
		
		/* the list should be empty if componentType is null,
		 * therefore an IndexOutOfBounds exception should be
		 * thrown by the get(int) method instead
		 */
		if(componentType != null && type != componentType)
			throw new NBTException("Cannot get " + type + " from TAG_List[" + componentType + "]");
		
		return (X) get(index);
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public byte getByte(int index) {
		return getByteTag(index).getValue();
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public short getShort(int index) {
		return getShortTag(index).getValue();
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public int getInt(int index) {
		return getIntTag(index).getValue();
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public long getLong(int index) {
		return getLongTag(index).getValue();
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public float getFloat(int index) {
		return getFloatTag(index).getValue();
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public double getDouble(int index) {
		return getDoubleTag(index).getValue();
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public byte[] getByteArray(int index) {
		return getByteArrayTag(index).getValue();
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public int[] getIntArray(int index) {
		return getIntArrayTag(index).getValue();
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public long[] getLongArray(int index) {
		return getLongArrayTag(index).getValue();
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public String getString(int index) {
		return getStringTag(index).getValue();
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public TagByte getByteTag(int index) {
		return get(index, TagByte.class);
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public TagShort getShortTag(int index) {
		return get(index, TagShort.class);
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public TagInt getIntTag(int index) {
		return get(index, TagInt.class);
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public TagLong getLongTag(int index) {
		return get(index, TagLong.class);
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public TagFloat getFloatTag(int index) {
		return get(index, TagFloat.class);
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public TagDouble getDoubleTag(int index) {
		return get(index, TagDouble.class);
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public TagByteArray getByteArrayTag(int index) {
		return get(index, TagByteArray.class);
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public TagIntArray getIntArrayTag(int index) {
		return get(index, TagIntArray.class);
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public TagLongArray getLongArrayTag(int index) {
		return get(index, TagLongArray.class);
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public TagString getStringTag(int index) {
		return get(index, TagString.class);
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public TagCompound getCompound(int index) {
		return get(index, TagCompound.class);
	}

	/**
	 * Returns the element at the specified position in this list
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this list
	 */
	public TagList<?> getList(int index) {
		return get(index, TagList.class);
	}
	
	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	@SuppressWarnings("unchecked")
	public TagList<T> add(T value) {
		addTag(size(), value, (Class<T>) value.getClass());
		return this;
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addByte(byte value) {
		return add(size(), value, TagByte.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addShort(short value) {
		return add(size(), value, TagShort.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addInt(int value) {
		return add(size(), value, TagInt.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addLong(long value) {
		return add(size(), value, TagLong.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addFloat(float value) {
		return add(size(), value, TagFloat.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addDouble(double value) {
		return add(size(), value, TagDouble.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addByteArray(byte[] value) {
		return add(size(), value, TagByteArray.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addIntArray(int[] value) {
		return add(size(), value, TagIntArray.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addLongArray(long[] value) {
		return add(size(), value, TagLongArray.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addString(String value) {
		return add(size(), value, TagString.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addByteTag(TagByte value) {
		return addTag(size(), value, TagByte.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addShortTag(TagShort value) {
		return addTag(size(), value, TagShort.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addIntTag(TagInt value) {
		return addTag(size(), value, TagInt.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addLongTag(TagLong value) {
		return addTag(size(), value, TagLong.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addFloatTag(TagFloat value) {
		return addTag(size(), value, TagFloat.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addDoubleTag(TagDouble value) {
		return addTag(size(), value, TagDouble.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addByteArrayTag(TagByteArray value) {
		return addTag(size(), value, TagByteArray.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addIntArrayTag(TagIntArray value) {
		return addTag(size(), value, TagIntArray.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addLongArrayTag(TagLongArray value) {
		return addTag(size(), value, TagLongArray.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addStringTag(TagString value) {
		return addTag(size(), value, TagString.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addCompound(TagCompound value) {
		return addTag(size(), value, TagCompound.class);
	}

	/**
	 * Add an element to the list
	 * 
	 * @param value element to be added
	 * @return this list
	 */
	public TagList<T> addList(TagList<?> value) {
		return addTag(size(), value, TagList.class);
	}
	
	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	@SuppressWarnings("unchecked")
	public TagList<T> add(int index, T value) {
		addTag(index, value, (Class<T>) value.getClass());
		return this;
	}
	
	private <V, X extends Tag<V>> TagList<T> add(int index, V value, Class<X> clazz) {
		getValue().add(index, process(value, clazz));
		return this;
	}

	private <X extends Tag<?>> TagList<T> addTag(int index, X value, Class<X> clazz) {
		getValue().add(index, processTag(value, clazz));
		return this;
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addByte(int index, byte value) {
		return add(index, value, TagByte.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addShort(int index, short value) {
		return add(index, value, TagShort.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addInt(int index, int value) {
		return add(index, value, TagInt.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addLong(int index, long value) {
		return add(index, value, TagLong.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addFloat(int index, float value) {
		return add(index, value, TagFloat.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addDouble(int index, double value) {
		return add(index, value, TagDouble.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addByteArray(int index, byte[] value) {
		return add(index, value, TagByteArray.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addIntArray(int index, int[] value) {
		return add(index, value, TagIntArray.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addLongArray(int index, long[] value) {
		return add(index, value, TagLongArray.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addString(int index, String value) {
		return add(index, value, TagString.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addByteTag(int index, TagByte value) {
		return addTag(index, value, TagByte.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addShortTag(int index, TagShort value) {
		return addTag(index, value, TagShort.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addIntTag(int index, TagInt value) {
		return addTag(index, value, TagInt.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addLongTag(int index, TagLong value) {
		return addTag(index, value, TagLong.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addFloatTag(int index, TagFloat value) {
		return addTag(index, value, TagFloat.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addDoubleTag(int index, TagDouble value) {
		return addTag(index, value, TagDouble.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addByteArrayTag(int index, TagByteArray value) {
		return addTag(index, value, TagByteArray.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addIntArrayTag(int index, TagIntArray value) {
		return addTag(index, value, TagIntArray.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addLongArrayTag(int index, TagLongArray value) {
		return addTag(index, value, TagLongArray.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addStringTag(int index, TagString value) {
		return addTag(index, value, TagString.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addCompound(int index, TagCompound value) {
		return addTag(index, value, TagCompound.class);
	}

	/**
	 * Inserts an element at the specified position in the list
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this list
	 */
	public TagList<T> addList(int index, TagList<?> value) {
		return addTag(index, value, TagList.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	@SuppressWarnings("unchecked")
	public TagList<T> set(int index, T value) {
		setTag(index, value, (Class<T>) value.getClass());
		return this;
	}
	
	private <V, X extends Tag<V>> TagList<T> set(int index, V value, Class<X> clazz) {
		getValue().set(index, process(value, clazz));
		return this;
	}

	private <X extends Tag<?>> TagList<T> setTag(int index, X value, Class<X> clazz) {
		getValue().set(index, processTag(value, clazz));
		return this;
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setByte(int index, byte value) {
		return set(index, value, TagByte.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setShort(int index, short value) {
		return set(index, value, TagShort.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setInt(int index, int value) {
		return set(index, value, TagInt.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setLong(int index, long value) {
		return set(index, value, TagLong.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setFloat(int index, float value) {
		return set(index, value, TagFloat.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setDouble(int index, double value) {
		return set(index, value, TagDouble.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setByteArray(int index, byte[] value) {
		return set(index, value, TagByteArray.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setIntArray(int index, int[] value) {
		return set(index, value, TagIntArray.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setLongArray(int index, long[] value) {
		return set(index, value, TagLongArray.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setString(int index, String value) {
		return set(index, value, TagString.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setByteTag(int index, TagByte value) {
		return setTag(index, value, TagByte.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setShortTag(int index, TagShort value) {
		return setTag(index, value, TagShort.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setIntTag(int index, TagInt value) {
		return setTag(index, value, TagInt.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setLongTag(int index, TagLong value) {
		return setTag(index, value, TagLong.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setFloatTag(int index, TagFloat value) {
		return setTag(index, value, TagFloat.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setDoubleTag(int index, TagDouble value) {
		return setTag(index, value, TagDouble.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setByteArrayTag(int index, TagByteArray value) {
		return setTag(index, value, TagByteArray.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setIntArrayTag(int index, TagIntArray value) {
		return setTag(index, value, TagIntArray.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setLongArrayTag(int index, TagLongArray value) {
		return setTag(index, value, TagLongArray.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setStringTag(int index, TagString value) {
		return setTag(index, value, TagString.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setCompound(int index, TagCompound value) {
		return setTag(index, value, TagCompound.class);
	}

	/**
	 * Replaces an element at the specified position in the list
	 * 
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this list
	 */
	public TagList<T> setList(int index, TagList<?> value) {
		return setTag(index, value, TagList.class);
	}
	
	/**
	 * Removes the element at the specified position in this list
	 * 
	 * @param index index of the element to be removed
	 * @return this list
	 */
	public TagList<T> remove(int index) {
		getValue().remove(index);
		return this;
	}
	
	/**
	 * Removes all of the elements from this list
	 * 
	 * @return this list
	 */
	public TagList<T> clear() {
		getValue().clear();
		return this;
	}
	
	/**
	 * Returns the number of elements in this list
	 * 
	 * @return the number of elements in this list
	 */
	public int size() {
		return getValue().size();
	}

    /**
     * Returns {@code true} if this list contains no elements.
     *
     * @return {@code true} if this list contains no elements
     */
	public boolean isEmpty() {
		return getValue().isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	private <X extends Tag<?>> TagList<X> cast(Class<X> clazz) {
		check(clazz);
		return (TagList<X>) this;
	}
	
	/**
	 * Returns this list, casted to a list of bytes
	 * 
	 * @return this list
	 */
	public TagList<TagByte> asByteList() {
		return cast(TagByte.class);
	}

	/**
	 * Returns this list, casted to a list of shorts
	 * 
	 * @return this list
	 */
	public TagList<TagShort> asShortList() {
		return cast(TagShort.class);
	}

	/**
	 * Returns this list, casted to a list of ints
	 * 
	 * @return this list
	 */
	public TagList<TagInt> asIntList() {
		return cast(TagInt.class);
	}

	/**
	 * Returns this list, casted to a list of longs
	 * 
	 * @return this list
	 */
	public TagList<TagLong> asLongList() {
		return cast(TagLong.class);
	}

	/**
	 * Returns this list, casted to a list of floats
	 * 
	 * @return this list
	 */
	public TagList<TagFloat> asFloatList() {
		return cast(TagFloat.class);
	}

	/**
	 * Returns this list, casted to a list of doubles
	 * 
	 * @return this list
	 */
	public TagList<TagDouble> asDoubleList() {
		return cast(TagDouble.class);
	}

	/**
	 * Returns this list, casted to a list of byte arrays
	 * 
	 * @return this list
	 */
	public TagList<TagByteArray> asByteArrayList() {
		return cast(TagByteArray.class);
	}

	/**
	 * Returns this list, casted to a list of int arrays
	 * 
	 * @return this list
	 */
	public TagList<TagIntArray> asIntArrayList() {
		return cast(TagIntArray.class);
	}

	/**
	 * Returns this list, casted to a list of long arrays
	 * 
	 * @return this list
	 */
	public TagList<TagLongArray> asLongArrayList() {
		return cast(TagLongArray.class);
	}

	/**
	 * Returns this list, casted to a list of strings
	 * 
	 * @return this list
	 */
	public TagList<TagString> asStringList() {
		return cast(TagString.class);
	}

	/**
	 * Returns this list, casted to a list of compounds
	 * 
	 * @return this list
	 */
	public TagList<TagCompound> asCompoundList() {
		return cast(TagCompound.class);
	}

	/**
	 * Returns this list, casted to a list of lists
	 * 
	 * @return this list
	 */
	@SuppressWarnings("unchecked")
	public TagList<TagList<?>> asListList() {
		check(TagList.class);
		return (TagList<TagList<?>>) this;
	}

	/**
	 * Returns this list, casted to a list of lists, where each list
	 * should only contain elements of type {@code componentType}
	 * 
	 * @param <X> type of the elements of the lists
	 * @param componentType class of the type of the elements of the lists
	 * @return this list
	 */
	@SuppressWarnings("unchecked")
	public <X extends Tag<?>> TagList<TagList<X>> asListList(Class<X> componentType) {
		check(TagList.class);
		
		for(int i = 0; i < size(); ++i)
			getList(i).check(componentType);
		
		return (TagList<TagList<X>>) this;
	}
	
	@Override
	public Iterator<T> iterator() {
		return getValue().iterator();
	}
	
}
