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
import java.util.Optional;

import at.syntaxerror.syntaxnbt.tag.Tag;
import at.syntaxerror.syntaxnbt.tag.TagCompound;
import lombok.NonNull;

/**
 * @author SyntaxError404
 * 
 */
public class PathNodeSubList extends PathNode {

	private final String name;
	
	private final List<PathNode> indices;
	
	@NonNull
	private PathNodeSubList(String name, List<Optional<Integer>> indices) {
		this.name = name;
		
		this.indices = indices
			.stream()
			.map(o -> o.<PathNode>map(PathNodeIndex::new))
			.map(o -> o.orElse(new PathNodeList()))
			.toList();
	}
	
	/**
	 * Constructs a new Sub-List/Array path for Named List Tag
	 * 
	 * @param name name of the list tag
	 * @param indices list of indices
	 * @param tag final tag to search for (optional)
	 */
	public PathNodeSubList(String name, List<Optional<Integer>> indices, TagCompound tag) {
		this(name, indices);
		
		if(tag != null)
			this.indices.add(new PathNodeListTag(tag));
	}
	
	@Override
	public List<Tag<?>> traverseSelf(Tag<?> tag) {
		if(!tag.isCompound())
			return List.of();
		
		TagCompound compound = tag.cast();
		
		if(!compound.has(name))
			return List.of();
		
		List<Tag<?>> tags = List.of(compound.get(name));
		
		for(PathNode node : indices)
			tags = node.traverseAll(tags);
		
		return tags;
	}
	
	@Override
	protected String stringify() {
		return indices
			.stream()
			.map(PathNode::toString)
			.reduce("", String::concat);
	}
	
}
