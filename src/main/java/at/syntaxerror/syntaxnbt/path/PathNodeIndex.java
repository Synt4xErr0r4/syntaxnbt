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
import at.syntaxerror.syntaxnbt.tag.TagArray;
import at.syntaxerror.syntaxnbt.tag.TagList;

/**
 * An NBT path node specifying an Element of a Named List or Array Tag
 * 
 * @author SyntaxError404
 * 
 */
public class PathNodeIndex extends PathNode {

	private final String name;
	private final int index;
	
	/**
	 * Constructs a new "Element of Named List or Array Tag" path
	 * 
	 * @param name name of the list/array tag
	 * @param index index for the list/array
	 */
	public PathNodeIndex(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	/**
	 * internal use only
	 * 
	 * @param index the index
	 */
	protected PathNodeIndex(int index) {
		this.name = null;
		this.index = index;
	}
	
	private int checkIndex(int size) {
		int idx = index;
		
		if(idx < 0)
			idx += size;
		
		if(idx < 0 || idx >= size)
			return -1;
		
		return idx;
	}
	
	@Override
	public List<Tag<?>> traverseSelf(Tag<?> target) {
		if(name != null) {
			target = getNamed(name, target);
			
			if(target == null)
				return List.of();
		}
		
		if(target.isList()) {
			TagList<?> list = target.cast();
			
			int idx = checkIndex(list.size());
			
			if(idx != -1)
				return List.of(list.get(idx));
		}
		
		else if(target.isArray()) {
			TagArray<?, ?> array = target.cast();
			
			int idx = checkIndex(array.size());
			
			if(idx != -1)
				return List.of(array.getTag(idx));
		}
		
		return List.of();
	}
	
	@Override
	protected String stringify() {
		return (name == null ? "" : SNBTStringifyer.quote(name)) + "[" + index + "]";
	}
	
}
