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
 * A length-prefixed array of <b>signed</b> longs. The prefix is a <b>signed</b> integer (thus 4 bytes)
 * and indicates the number of 8 byte longs.
 * 
 * @author SyntaxError404
 * 
 */
public class TagLongArray extends TagArray<Long, long[]> {

	/**
	 * Constructs a new tag with an initial empty array
	 */
	public TagLongArray() {
		this(new long[0]);
	}

	/**
	 * Constructs a new tag with an initial value
	 * 
	 * @param value initial value
	 */
	public TagLongArray(long[] value) {
		super(TagType.LONG_ARRAY, value);
	}

	@Override
	public TagLongArray set(int index, Number value) {
		checkBounds(index);
		Array.setLong(getValue(), index, value.longValue());
		return this;
	}

	@Override
	public Long get(int index) {
		checkBounds(index);
		return Array.getLong(getValue(), index);
	}

	@Override
	public TagLong getTag(int index) {
		return new TagLong(get(index));
	}
	
	@Override
	protected boolean compare(Tag<long[]> tag) {
		return Arrays.equals(getValue(), tag.getValue());
	}
	
}
