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
import java.util.Arrays;

/**
 * A length-prefixed array of <b>signed</b> bytes. The prefix is a <b>signed</b> integer (thus 4 bytes)
 * 
 * @author SyntaxError404
 * 
 */
public class TagByteArray extends TagArray<Byte, byte[]> {

	/**
	 * Constructs a new tag with an initial empty array
	 */
	public TagByteArray() {
		this(new byte[0]);
	}

	/**
	 * Constructs a new tag with an initial value
	 * 
	 * @param value initial value
	 */
	public TagByteArray(byte[] value) {
		super(TagType.BYTE_ARRAY, value);
	}

	@Override
	public TagByteArray set(int index, Number value) {
		checkBounds(index);
		Array.setByte(getValue(), index, value.byteValue());
		return this;
	}

	@Override
	public Byte get(int index) {
		checkBounds(index);
		return Array.getByte(getValue(), index);
	}

	@Override
	public TagByte getTag(int index) {
		return new TagByte(get(index));
	}
	
	@Override
	protected boolean compare(Tag<byte[]> tag) {
		return Arrays.equals(getValue(), tag.getValue());
	}
	
}
