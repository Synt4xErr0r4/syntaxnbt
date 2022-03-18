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
 * A single, big endian <a href="http://en.wikipedia.org/wiki/IEEE_754-2008">IEEE-754</a> single-precision
 * floating point number (<a href="http://en.wikipedia.org/wiki/NaN">NaN</a> possible)
 * 
 * @author SyntaxError404
 * 
 */
public class TagFloat extends TagNumber<Float> {

	/**
	 * Constructs a new tag with an initial value of {@code 0}
	 */
	public TagFloat() {
		this(0f);
	}

	/**
	 * Constructs a new tag with an initial value
	 * 
	 * @param value initial value
	 */
	public TagFloat(Float value) {
		super(TagType.FLOAT, value);
	}
	
}
