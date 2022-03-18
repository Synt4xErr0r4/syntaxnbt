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
 * A length-prefixed array of <b>signed</b> integers. The prefix is a <b>signed</b> integer (thus 4 bytes)
 * and indicates the number of 4 byte integers.
 * 
 * @author SyntaxError404
 * 
 */
public class TagIntArray extends TagArray<Integer, int[]> {

	/**
	 * Constructs a new tag with an initial empty array
	 */
	public TagIntArray() {
		this(new int[0]);
	}

	/**
	 * Constructs a new tag with an initial value
	 * 
	 * @param value initial value
	 */
	public TagIntArray(int[] value) {
		super(TagType.INT_ARRAY, value);
	}

	@Override
	public TagIntArray set(int index, Number value) {
		checkBounds(index);
		Array.setInt(getValue(), index, value.intValue());
		return this;
	}

	@Override
	public Integer get(int index) {
		checkBounds(index);
		return Array.getInt(getValue(), index);
	}

	@Override
	public TagInt getTag(int index) {
		return new TagInt(get(index));
	}
	
	@Override
	protected boolean compare(Tag<int[]> tag) {
		return Arrays.equals(getValue(), tag.getValue());
	}

}
