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

import java.util.ArrayList;
import java.util.List;

import at.syntaxerror.syntaxnbt.NBTException;
import at.syntaxerror.syntaxnbt.tag.Tag;
import at.syntaxerror.syntaxnbt.tag.TagCompound;
import lombok.Getter;

/**
 * The base class for all NBT path nodes
 * 
 * @author SyntaxError404
 * 
 */
public abstract class PathNode {

	/**
	 * Returns the next node in the path sequence
	 * 
	 * @return the next node in the path sequence
	 */
	@Getter
	private PathNode next;
	
	/**
	 * Sets the next node in the path sequence.
	 * Must not be a {@link PathNodeRoot}
	 * 
	 * @param next the next node
	 */
	public void setNext(PathNode next) {
		if(next instanceof PathNodeRoot)
			throw new NBTException("Cannot specify Root Compound Tag Path Node as child");
		
		this.next = next;
	}

	/**
	 * Traverse through the tag's tree structure and return the matching tags.
	 * This does not respect the {@link #getNext() next node}
	 * 
	 * @param tag tag to traverse through
	 * @return the matching tags
	 */
	protected abstract List<Tag<?>> traverseSelf(Tag<?> tag);
	
	/**
	 * Traverse recursively through the tag's tree structure and return the matching tags
	 * 
	 * @param tag tag to traverse through
	 * @return the matching tags
	 */
	public final List<Tag<?>> traverse(Tag<?> tag) {
		List<Tag<?>> tags = traverseSelf(tag);
		
		return next == null
			? tags
			: next.traverseAll(tags);
	}

	/**
	 * Traverse recursively through the tree structures of all tags and return the matching tags
	 * 
	 * @param tags tags to traverse through
	 * @return the matching tags
	 */
	public final List<Tag<?>> traverseAll(List<Tag<?>> tags) {
		List<Tag<?>> result = new ArrayList<>();
		
		for(Tag<?> tag : tags)
			result.addAll(traverse(tag));
		
		return result;
	}
	
	/**
	 * Returns the tag with the specified name in a compound, if present 
	 * 
	 * @param name name of the element
	 * @param tag a compound tag
	 * @return the named tag
	 */
	protected Tag<?> getNamed(String name, Tag<?> tag) {
		if(!tag.isCompound())
			return null;
		
		TagCompound compound = tag.cast();
		
		if(!compound.has(name))
			return null;
		
		return compound.get(name);
	}
	
	/**
	 * Stringifies only this node, without respecting the {@link #getNext() next node}
	 * 
	 * @return the string representation of this node
	 */
	protected abstract String stringify();
	
	@Override
	public String toString() {
		return stringify() + (next == null ? "" : "." + next.toString());
	}
	
}
