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

import lombok.Getter;
import lombok.Setter;

/**
 * Parent class for {@link TagByte}, {@link TagShort}, {@link TagInt}, {@link TagLong},
 * {@link TagFloat}, and {@link TagDouble}
 * 
 * @author SyntaxError404
 * 
 * @param <N> type of the number stored in this tag
 */
@Getter
@Setter
public abstract class TagNumber<N extends Number> extends Tag<N> {

	/**
	 * Constructs a new tag with a type and an initial value
	 * 
	 * @param type type of this tag
	 * @param number initial value
	 */
	protected TagNumber(TagType type, N number) {
		super(type, number);
	}
	
	/**
	 * Returns the stored value {@link Number#byteValue() casted to a byte}
	 * 
	 * @return the byte
	 */
	public byte byteValue() {
		return getValue().byteValue();
	}

	/**
	 * Returns the stored value {@link Number#shortValue() casted to a short}
	 * 
	 * @return the short
	 */
	public short shortValue() {
		return getValue().shortValue();
	}

	/**
	 * Returns the stored value {@link Number#intValue() casted to a int}
	 * 
	 * @return the int
	 */
	public int intValue() {
		return getValue().intValue();
	}

	/**
	 * Returns the stored value {@link Number#longValue() casted to a long}
	 * 
	 * @return the long
	 */
	public long longValue() {
		return getValue().longValue();
	}

	/**
	 * Returns the stored value {@link Number#floatValue() casted to a float}
	 * 
	 * @return the float
	 */
	public float floatValue() {
		return getValue().floatValue();
	}

	/**
	 * Returns the stored value {@link Number#doubleValue() casted to a double}
	 * 
	 * @return the double
	 */
	public double doubleValue() {
		return getValue().doubleValue();
	}
	
}
