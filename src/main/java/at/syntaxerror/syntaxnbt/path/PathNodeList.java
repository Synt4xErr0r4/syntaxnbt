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
package at.syntaxerror.syntaxnbt.path;

import java.util.List;

import at.syntaxerror.syntaxnbt.internal.SNBTStringifyer;
import at.syntaxerror.syntaxnbt.tag.Tag;
import at.syntaxerror.syntaxnbt.tag.TagList;
import lombok.NonNull;

/**
 * An NBT path node specifying all elements of a Named List or Array Tag
 * 
 * @author SyntaxError404
 * 
 */
public class PathNodeList extends PathNode {

	private final String name;
	
	/**
	 * Constructs a new "All Elements of Named List or Array Tag" path
	 * 
	 * @param name name of the list/array tag
	 */
	@NonNull
	public PathNodeList(String name) {
		this.name = name;
	}
	
	/**
	 * internal use only
	 */
	protected PathNodeList() {
		this.name = null;
	}
	
	@Override
	public List<Tag<?>> traverseSelf(Tag<?> tag) {
		if(name != null) {
			tag = getNamed(name, tag);
			
			if(tag == null)
				return List.of();
		}
		
		return tag.isList() || tag.isArray()
			? tag.<TagList<Tag<?>>>cast().getValue()
			: List.of();
	}
	
	@Override
	protected String stringify() {
		return (name == null ? "" : SNBTStringifyer.quote(name)) + "[]";
	}
	
}
