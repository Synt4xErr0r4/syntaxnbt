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

import java.lang.reflect.Array;

import at.syntaxerror.syntaxnbt.NBTException;
import lombok.Getter;

/**
 * Parent class for {@link TagByteArray}, {@link TagIntArray}, {@link TagLongArray}
 * 
 * @author SyntaxError404
 * 
 * @param <T> type of the number stored in this array
 * @param <N> type of the array stored in this tag
 */
@Getter
@SuppressWarnings("unchecked")
public abstract class TagArray<N extends Number, T> extends Tag<T> {
	
	/**
	 * Returns the type of the array
	 * 
	 * @return the type of the array
	 */
	private final Class<T> arrayType;

	/**
	 * Constructs an array of the specified type
	 * 
	 * @param type type of elements in this array
	 * @param array array of elements
	 */
	protected TagArray(TagType type, T array) {
		super(type, array);
		
		arrayType = (Class<T>) array.getClass();
		
		if(!arrayType.isArray())
			throw new NBTException("Type for array tag must be an array");
	}

	/**
	 * Checks if the index lies within the bounds of the list (for {@link #set(int, Number)})
	 * 
	 * @param index the index
	 */
	protected void checkBounds(int index) {
		if(index < 0 || index >= size())
			throw new ArrayIndexOutOfBoundsException(index);
	}
	
	/**
	 * Checks if the index lies within the bounds of the list (for {@link #add(int, Number)})
	 * 
	 * @param index the index
	 */
	protected void checkBoundsExclusive(int index) {
		if(index < 0 || index > size())
			throw new ArrayIndexOutOfBoundsException(index);
	}

	/**
	 * Replaces an element at the specified position in the array
	 * 
	 * @param index index of the element to replace
	 * @param value the element to be stored at the specified position
	 * @return this array
	 */
	public abstract TagArray<N, T> set(int index, Number value);

	/**
	 * Replaces an element at the specified position in the array
	 * 
	 * @param index index of the element to replace
	 * @param value the element to be stored at the specified position
	 * @return this array
	 */
	public TagArray<N, T> setTag(int index, TagNumber<?> value) {
		return set(index, value.getValue());
	}

	/**
	 * Add an element to the array
	 * 
	 * @param value the element to be added
	 * @return this array
	 */
	public TagArray<N, T> add(Number value) {
		return add(size(), value);
	}

	/**
	 * Add an element to the array
	 * 
	 * @param value the element to be added
	 * @return this array
	 */
	public TagArray<N, T> addTag(TagNumber<?> value) {
		return addTag(size(), value);
	}
	
	private void expand(int off) {
		checkBoundsExclusive(off);
		
		int sz = size();
		
		Object newArray = Array.newInstance(arrayType.getComponentType(), sz + 1);
		
		System.arraycopy(getValue(), 0, newArray, 0, off);
		System.arraycopy(getValue(), off, newArray, off + 1, sz - off);
		
		setValue((T) newArray);
	}

	/**
	 * Inserts an element at the specified position in the array
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be added
	 * @return this array
	 */
	public TagArray<N, T> add(int index, Number value) {
		expand(index);
		return set(index, value);
	}

	/**
	 * Inserts an element at the specified position in the array
	 * 
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be added
	 * @return this array
	 */
	public TagArray<N, T> addTag(int index, TagNumber<?> value) {
		expand(index);
		return setTag(index, value);
	}

	/**
	 * Returns the element at the specified position in this array
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this array
	 */
	public abstract N get(int index);

	/**
	 * Returns the tag at the specified position in this array as a {@link TagNumber}
	 * 
	 * @param index index of the element to be returned
	 * @return the element at the specified position in this array
	 */
	public abstract TagNumber<N> getTag(int index);

	/**
	 * Returns the number of elements in this array
	 * 
	 * @return the number of elements in this array
	 */
	public int size() {
		return Array.getLength(getValue());
	}

	/**
	 * Removes the element at the specified position in this array
	 * 
	 * @param index index of the element to be removed
	 * @return this array
	 */
	public TagArray<N, T> remove(int index) {
		checkBounds(index);
		
		int sz = size();
		
		Object newArray = Array.newInstance(arrayType.getComponentType(), sz - 1);
		
		System.arraycopy(getValue(), 0, newArray, 0, index);
		System.arraycopy(getValue(), index + 1, newArray, index, sz - index - 1);
		
		setValue((T) newArray);
		
		return this;
	}
	
	/**
	 * Removes all of the elements from this array
	 * 
	 * @return this array
	 */
	public TagArray<N, T> clear() {
		setValue((T) Array.newInstance(arrayType.componentType(), 0));
		return this;
	}
	
}
