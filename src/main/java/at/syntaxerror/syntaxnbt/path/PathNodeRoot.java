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

import at.syntaxerror.syntaxnbt.tag.Tag;
import at.syntaxerror.syntaxnbt.tag.TagCompound;

/**
 * An NBT path node specifying a root Compound Tag
 * 
 * @author SyntaxError404
 * 
 */
public class PathNodeRoot extends PathNode {
	
	private final TagCompound tag;
	
	/**
	 * Constructs a new "Root Compound Tag" path
	 * 
	 * @param tag tag to search for
	 */
	public PathNodeRoot(TagCompound tag) {
		this.tag = tag;
	}
	
	@Override
	public List<Tag<?>> traverseSelf(Tag<?> tag) {
		if(!tag.isCompound() || !tag.<TagCompound>cast().contains(this.tag))
			return List.of();
		
		return List.of(tag);
	}
	
	@Override
	protected String stringify() {
		return tag.toString();
	}
	
}
