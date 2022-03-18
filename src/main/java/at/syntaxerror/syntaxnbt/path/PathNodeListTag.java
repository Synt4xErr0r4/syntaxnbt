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

import at.syntaxerror.syntaxnbt.internal.SNBTStringifyer;
import at.syntaxerror.syntaxnbt.tag.Tag;
import at.syntaxerror.syntaxnbt.tag.TagCompound;
import at.syntaxerror.syntaxnbt.tag.TagList;
import at.syntaxerror.syntaxnbt.tag.TagType;
import lombok.NonNull;

/**
 * An NBT path node specifying Compound Elements of a Named List Tag 
 * 
 * @author SyntaxError404
 * 
 */
public class PathNodeListTag extends PathNode {

	private final String name;
	private final TagCompound tag;

	/**
	 * Constructs a new "Compound Elements of Named List Tag" path
	 * 
	 * @param name name of the list/array tag
	 * @param tag tag to search for
	 */
	@NonNull
	public PathNodeListTag(String name, TagCompound tag) {
		this.name = name;
		this.tag = tag;
	}
	
	/**
	 * internal use only
	 * 
	 * @param tag tag to search for
	 */
	protected PathNodeListTag(TagCompound tag) {
		this.name = null;
		this.tag = tag;
	}
	
	@Override
	public List<Tag<?>> traverseSelf(Tag<?> tag) {
		if(name != null) {
			tag = getNamed(name, tag);
			
			if(tag == null)
				return List.of();
		}
		
		if(tag.isList() && tag.<TagList<?>>cast().getComponentType() == TagType.COMPOUND) {
			
			TagList<TagCompound> list = tag.cast();
			List<Tag<?>> result = new ArrayList<>();
			
			for(TagCompound compound : list)
				if(compound.contains(this.tag))
					result.add(compound);
			
			return result;
		}
		
		return List.of();
	}
	
	@Override
	protected String stringify() {
		return (name == null ? "" : SNBTStringifyer.quote(name)) + "[" + tag + "]";
	}
	
}
