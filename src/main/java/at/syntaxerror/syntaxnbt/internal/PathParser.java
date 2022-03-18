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
package at.syntaxerror.syntaxnbt.internal;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.syntaxerror.syntaxnbt.path.PathNode;
import at.syntaxerror.syntaxnbt.path.PathNodeCompound;
import at.syntaxerror.syntaxnbt.path.PathNodeIndex;
import at.syntaxerror.syntaxnbt.path.PathNodeList;
import at.syntaxerror.syntaxnbt.path.PathNodeListTag;
import at.syntaxerror.syntaxnbt.path.PathNodeNamed;
import at.syntaxerror.syntaxnbt.path.PathNodeRoot;
import at.syntaxerror.syntaxnbt.path.PathNodeSubList;
import at.syntaxerror.syntaxnbt.tag.TagCompound;
import at.syntaxerror.syntaxnbt.tag.TagInt;

/**
 * A utility class for parsing NBT paths
 * 
 * @author SyntaxError404
 * 
 */
public class PathParser extends SNBTParser {

	private static final Pattern PATTERN_INT = Pattern.compile("([+-]?(?:0|[1-9]\\d*))");
	
	private static final Range RANGE_INT = new Range(
		BigInteger.valueOf(Integer.MIN_VALUE),
		BigInteger.valueOf(Integer.MAX_VALUE),
		v -> new TagInt(v.intValue())
	);
	
	private PathParser(Reader reader) {
		super(reader);
	}

	private PathNode nextPath() {
		char c = next();
		back();
		
		PathNode node;
		
		if(c == '{')
			node = new PathNodeRoot(nextCompound());
		
		else {
			String name = nextString(true);
			
			c = more() ? next() : 0;
			
			if(c == '{') {
				back();
				
				node = new PathNodeCompound(name, nextCompound());
			}
			else if(c == '[') {
				
				c = next();
				
				boolean checkSub = true;
				Optional<Integer> index = Optional.empty();
				
				if(c == ']')
					node = new PathNodeList(name);
				
				else if(c == '{') {
					back();
					checkSub = false;
					
					node = new PathNodeListTag(name, nextCompound());

					expect(']');
				}
				
				else {
					back();
					int idx = nextInt();
					
					expect(']');
					
					node = new PathNodeIndex(name, idx);
					
					index = Optional.of(idx);
				}

				if(checkSub && peek() == '[') {
					List<Optional<Integer>> indices = new ArrayList<>();
					
					TagCompound tag = null;
					
					boolean valid = index.isPresent();
					
					indices.add(index);
					
					while(more()) {
						c = next();
						
						if(c != '[') {
							if(!valid)
								throw syntaxError("Expected Integer or Compound Tag");
							
							back();
							break;
						}
						
						c = next();
						
						if(c == '{') {
							back();
							
							tag = nextCompound();
							
							expect(']');
							break;
						}
						else if(c == ']') {
							valid = false;
							indices.add(Optional.empty());
						}
						
						else {
							back();
							valid = true;
							
							indices.add(Optional.of(nextInt()));
							expect(']');
						}
					}
					
					node = new PathNodeSubList(name, indices, tag);
				}
			}
			else {
				if(c != 0)
					back();
				
				node = new PathNodeNamed(name);
			}
		}
		
		if(!more())
			return node;
		
		expect('.');
		
		node.setNext(nextPath());
		
		return node;
	}
	
	private void expect(char c) {
		if(next() != c)
			throw syntaxError("'" + c + "' expected");
	}
	
	private int nextInt() {
		String str = nextString(false);
		
		Matcher matcher = PATTERN_INT.matcher(str);
		
		if(!matcher.matches())
			throw syntaxError("Expected Integer");
		
		BigInteger val = new BigInteger(matcher.group(1));
		
		if(!RANGE_INT.inRange(val))
			throw syntaxError("Index is too big");
		
		return val.intValue();
	}
	
	@Override
	protected boolean isValuePartAlt(char c) {
		return c != '[' && c != '{' && c != '.'
			&& !Character.isWhitespace(c);
	}
	
	/**
	 * Read an NBT path from a string
	 * 
	 * @param input an input string
	 * @return the parsed NBT path
	 */
	public static PathNode parsePath(String input) {
		try(StringReader sr = new StringReader(input.strip())) {
			PathParser parser = new PathParser(sr);
			
			PathNode path = parser.nextPath();
			
			if(parser.more())
				throw parser.syntaxError("Trailing data");
			
			return path;
		}
	}
	
}
