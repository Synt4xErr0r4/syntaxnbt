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

/**
 * A single signed byte
 * 
 * @author SyntaxError404
 * 
 */
public class TagByte extends TagNumber<Byte> {

	/**
	 * Constructs a new tag with an initial value of {@code 0}
	 */
	public TagByte() {
		this((byte) 0);
	}

	/**
	 * Constructs a new tag with an initial value
	 * 
	 * @param value initial value
	 */
	public TagByte(Byte value) {
		super(TagType.BYTE, value);
	}

	/**
	 * Constructs a new tag with an initial value derived from a boolean.
	 * {@link Boolean#TRUE true} is converted to {@code 1},
	 * {@link Boolean#FALSE false} is converted to {@code 0},
	 * 
	 * @param value initial value
	 */
	public TagByte(Boolean value) {
		this((byte) (value ? 1 : 0));
	}

}
