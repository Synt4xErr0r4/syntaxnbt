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

import java.util.Objects;

import at.syntaxerror.syntaxnbt.internal.SNBTStringifyer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * An NBT Tag consisting of a type and a value
 * 
 * @author SyntaxError404
 * 
 * @param <T> type of the value stored in this tag
 */
@Getter
public abstract class Tag<T> {
	
	/**
	 * Returns the type of this tag
	 * 
	 * @return the type of this tag
	 */
	@NonNull
	private final TagType type;

	/**
	 * -- GETTER --
	 * Returns the value stored in this tag
	 * 
	 * @return the value stored in this tag
	 * 
	 * -- SETTER --
	 * Overrides the value stored in this tag
	 * 
	 * @param value value to be stored
	 */
	@NonNull
	@Setter
	private T value;

	/**
	 * Constructs a new tag with a type and an initial value
	 * 
	 * @param type type of this tag
	 * @param value initial value
	 */
	protected Tag(TagType type, T value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Tests if the type of this tag matches the type specified
	 * 
	 * @param type the type to be checked
	 * @return whether the types match
	 */
	public boolean is(TagType type) {
		return this.type == type;
	}
	
	/**
	 * Tests if this tag is a {@link TagCompound}
	 * 
	 * @return whether this tag is a compound
	 */
	public boolean isCompound() {
		return is(TagType.COMPOUND);
	}
	
	/**
	 * Tests if this tag is a {@link TagList}
	 * 
	 * @return whether this tag is a list
	 * @see #isList(TagType)
	 */
	public boolean isList() {
		return is(TagType.LIST);
	}
	
	/**
	 * Tests if this tag is a {@link TagList} containing
	 * only elements of type {@code componentType}
	 * 
	 * @param componentType expected type for list elements
	 * @return whether this tag is a list with children of type {@code componentType}
	 * @see #isList()
	 */
	public boolean isList(TagType componentType) {
		return isList()
			&& ((TagList<?>) this).getComponentType() == componentType;
	}
	
	/**
	 * Tests if this tag is a {@link TagArray} ({@link TagType#BYTE_ARRAY},
	 * {@link TagType#INT_ARRAY}, or {@link TagType#LONG_ARRAY})
	 * 
	 * @return whether this tag is an array
	 */
	public boolean isArray() {
		return is(TagType.BYTE_ARRAY)
			|| is(TagType.INT_ARRAY)
			|| is(TagType.LONG_ARRAY);
	}
	
	/**
	 * Tests if this tag is a {@link TagString}
	 * 
	 * @return whether this tag is a string
	 */
	public boolean isString() {
		return is(TagType.STRING);
	}

	/**
	 * Tests if this tag is a {@link TagNumber} ({@link TagType#BYTE},
	 * {@link TagType#SHORT}, {@link TagType#INT}, {@link TagType#LONG},
	 * {@link TagType#FLOAT}, or {@link TagType#DOUBLE})
	 * 
	 * @return whether this tag is a number
	 */
	public boolean isNumber() {
		return isInteger() || isFloat();
	}

	/**
	 * Tests if this tag is a {@link TagNumber} denoting an integer
	 * ({@link TagType#BYTE}, {@link TagType#SHORT}, {@link TagType#INT},
	 * or {@link TagType#LONG})
	 * 
	 * @return whether this tag is an integer
	 */
	public boolean isInteger() {
		return is(TagType.BYTE)
			|| is(TagType.SHORT)
			|| is(TagType.INT)
			|| is(TagType.LONG);
	}

	/**
	 * Tests if this tag is a {@link TagNumber} denoting a floating point number
	 * ({@link TagType#FLOAT} or {@link TagType#DOUBLE})
	 * 
	 * @return whether this tag is a float
	 */
	public boolean isFloat() {
		return is(TagType.FLOAT)
			|| is(TagType.DOUBLE);
	}
	
	/**
	 * Casts this tag to the requested type, if possible
	 * 
	 * @param <N> the type of the tag
	 * @return this tag
	 * @throws ClassCastException if casting failed
	 */
	@SuppressWarnings("unchecked")
	public <N extends Tag<?>> N cast() {
		return (N) this;
	}

	@Override
	public String toString() {
		return SNBTStringifyer.stringify(this, false);
	}
	
	/**
	 * Compares two tags for equality
	 * 
	 * @param tag tag to compare to
	 * @return whether the tags are equal
	 */
	protected boolean compare(Tag<T> tag) {
		return Objects.equals(getValue(), tag.getValue());
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Tag<?> tag
			&& tag.getType() == getType()
			&& compare(tag.cast());
	}
	
}
