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
 * A length-prefixed <a href="https://docs.oracle.com/javase/8/docs/api/java/io/DataInput.html#modified-utf-8">modified UTF-8</a>
 * string. The prefix is an <b>unsigned</b> short (thus 2 bytes) signifying the length of the string in bytes
 * 
 * @author SyntaxError404
 * 
 */
public class TagString extends Tag<String> {

	/**
	 * Constructs a new tag with an initial value of an empty string
	 */
	public TagString() {
		this("");
	}
	
	/**
	 * Constructs a new tag with an initial value
	 * 
	 * @param value initial value
	 */
	public TagString(String value) {
		super(TagType.STRING, value);
	}
	
}
