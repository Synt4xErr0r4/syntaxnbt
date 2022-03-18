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

import at.syntaxerror.syntaxnbt.NBTException;
import at.syntaxerror.syntaxnbt.NBTUtil;
import at.syntaxerror.syntaxnbt.tag.Tag;
import at.syntaxerror.syntaxnbt.tag.TagArray;
import at.syntaxerror.syntaxnbt.tag.TagByteArray;
import at.syntaxerror.syntaxnbt.tag.TagCompound;
import at.syntaxerror.syntaxnbt.tag.TagIntArray;
import at.syntaxerror.syntaxnbt.tag.TagList;
import at.syntaxerror.syntaxnbt.tag.TagLongArray;
import at.syntaxerror.syntaxnbt.tag.TagNumber;
import at.syntaxerror.syntaxnbt.tag.TagString;
import at.syntaxerror.syntaxnbt.tag.TagType;

/**
 * A utility class for stringifying NBT tags
 * 
 * @author SyntaxError404
 * 
 */
public class SNBTStringifyer {
	
	private static final String NO_QUOTE = "^[a-zA-Z0-9_.+-]+$";

	private static final SNBTStringifyer PLAIN = 	new SNBTStringifyer("",		"",		"",		"",		"");
	private static final SNBTStringifyer COLORFUL = new SNBTStringifyer("§f",	"§a",	"§b",	"§6",	"§c");
	
	private final String colorPunctuator;
	private final String colorString;
	private final String colorIdentifier;
	private final String colorNumber;
	private final String colorSuffix;
	
	private final String quote;
	private final String comma;
	
	private SNBTStringifyer(String punct, String string, String ident, String number, String suffix) {
		colorPunctuator = punct;
		colorString = string;
		colorIdentifier = ident;
		colorNumber = number;
		colorSuffix = suffix;
		
		quote = colorPunctuator + '"';
		comma = colorPunctuator + ", ";
	}
	
	private String process(Tag<?> tag, int depth) {
		if(tag instanceof TagCompound compound)
			return processCompound(compound, depth);
		
		if(tag instanceof TagList<?> list)
			return processList(list, depth);
		
		else if(tag instanceof TagByteArray array)
			return processArray(array, "B", "B");
		
		else if(tag instanceof TagIntArray array)
			return processArray(array, "I", null);
		
		else if(tag instanceof TagLongArray array)
			return processArray(array, "L", "L");
		
		else if(tag instanceof TagString string)
			return new StringBuilder()
				.append(quote)
				.append(colorString)
				.append(quoteSoft(string.getValue()))
				.append(quote)
				.toString();

		else if(tag instanceof TagNumber<?> number) {
			StringBuilder sb = new StringBuilder();
			
			sb.append(colorNumber).append(number.getValue());
			
			if(number.getType() != TagType.INT)
				sb.append(colorSuffix)
					.append(
						switch(number.getType()) {
						case BYTE ->	'b';
						case SHORT ->	's';
						case LONG ->	'l';
						case FLOAT ->	'f';
						case DOUBLE ->	'd';
						
						default -> throw new NBTException("Tag is not stringifyable: " + tag.getType());
						}
					);
			
			return sb.toString();
		}
		
		else throw new NBTException("Tag is not stringifyable: " + tag.getType());
	}
	
	private String processCompound(TagCompound compound, int depth) {
		StringBuilder sb = new StringBuilder();
		sb.append(colorPunctuator).append('{');

		int i = 0;
		
		if(depth-- > 0)
			for(String key : compound.keySet()) {
				if(i++ > 0)
					sb.append(comma);
				
				if(key.matches(NO_QUOTE))
					sb.append(colorIdentifier).append(key);
				
				else sb.append(quote)
					.append(colorIdentifier)
					.append(quoteSoft(key))
					.append(quote);
				
				sb.append(colorPunctuator).append(": ")
					.append(process(compound.get(key), depth));
			}
		
		sb.append(colorPunctuator).append('}');
		return sb.toString();
	}
	
	private String processList(TagList<?> list, int depth) {
		StringBuilder sb = new StringBuilder();
		sb.append(colorPunctuator).append('[');

		if(depth-- > 0)
			for(int i = 0; i < list.size(); ++i) {
				if(i > 0)
					sb.append(comma);
				
				sb.append(process(list.get(i), depth));
			}
		
		sb.append(colorPunctuator).append(']');
		return sb.toString();
	}
	
	private String processArray(TagArray<?, ?> array, String prefix, String suffix) {
		prefix = colorSuffix + prefix;
		
		if(suffix == null)
			suffix = "";
		else suffix = colorSuffix + suffix;

		StringBuilder sb = new StringBuilder();
		sb.append(colorPunctuator).append('[')
			.append(prefix)
			.append(colorPunctuator).append(';');

		if(array.size() != 0)
			sb.append(' ');
		
		for(int i = 0; i < array.size(); ++i) {
			if(i > 0)
				sb.append(comma);
			
			sb.append(colorNumber).append(array.get(i)).append(suffix);
		}
		
		sb.append(colorPunctuator).append(']');
		return sb.toString();
	}

	/**
	 * Writes a tag to a string (SNBT format).
	 * If {@code colored}, the string will contain color codes
	 * (according to Minecraft's old chat color system using section signs {@code §})
	 * 
	 * @param tag tag to be stringifyed
	 * @param colored whether the string should contain color codes
	 * @return the stringifyed compound tag
	 */
	public static String stringify(Tag<?> tag, boolean colored) {
		return (colored ? COLORFUL : PLAIN).process(tag, NBTUtil.MAX_DEPTH);
	}
	
	private static String quoteSoft(String string) {
		StringBuilder sb = new StringBuilder();
		
		for(char c : string.toCharArray())
			switch(c) {
			case '"':	sb.append("\\\"");	break;
			case '\\':	sb.append("\\\\");	break;
			case '\n':	sb.append("\\n");	break;
			default:	sb.append(c);		break;
			}
		
		return sb.toString();
	}
	
	/**
	 * Wrap a string in quotes ({@code "}) and escape special characters, if necessary.
	 * If the string only consists of alphanumerical characters, it is returned without further modification.
	 * 
	 * @param string the string to be quoted
	 * @return the quoted string
	 */
	public static String quote(String string) {
		if(string.matches(NO_QUOTE) && !string.isEmpty())
			return string;
		
		return '"' + quoteSoft(string) + '"';
	}

}
