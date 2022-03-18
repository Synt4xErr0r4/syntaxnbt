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

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.syntaxerror.syntaxnbt.NBTException;
import at.syntaxerror.syntaxnbt.tag.Tag;
import at.syntaxerror.syntaxnbt.tag.TagArray;
import at.syntaxerror.syntaxnbt.tag.TagByte;
import at.syntaxerror.syntaxnbt.tag.TagByteArray;
import at.syntaxerror.syntaxnbt.tag.TagCompound;
import at.syntaxerror.syntaxnbt.tag.TagDouble;
import at.syntaxerror.syntaxnbt.tag.TagFloat;
import at.syntaxerror.syntaxnbt.tag.TagInt;
import at.syntaxerror.syntaxnbt.tag.TagIntArray;
import at.syntaxerror.syntaxnbt.tag.TagList;
import at.syntaxerror.syntaxnbt.tag.TagLong;
import at.syntaxerror.syntaxnbt.tag.TagLongArray;
import at.syntaxerror.syntaxnbt.tag.TagNumber;
import at.syntaxerror.syntaxnbt.tag.TagShort;
import at.syntaxerror.syntaxnbt.tag.TagString;
import at.syntaxerror.syntaxnbt.tag.TagType;

/**
 * A utility class for parsing SNBT tags
 * 
 * @author SyntaxError404
 * 
 */
public class SNBTParser {

	/** RegEx pattern for matching booleans */
	private static final Pattern PATTERN_BOOLEAN = Pattern.compile(
		"true|false",
		Pattern.CASE_INSENSITIVE
	);
	
	/** RegEx pattern for matching integers */
	protected static final Pattern PATTERN_INTEGER = Pattern.compile(
		"([+-]?(?:0|[1-9]\\d*))([bsl]?)",
		Pattern.CASE_INSENSITIVE
	);

	/** RegEx pattern for matching decimal numbers without decimal point */
	private static final Pattern PATTERN_FLOAT_INTEGER = Pattern.compile(
		"([+-]?\\d+)([fd])",
		Pattern.CASE_INSENSITIVE
	);

	/** RegEx pattern for matching decimal numbers without decimal point and an exponent */
	private static final Pattern PATTERN_FLOAT_INTEGER_EXP = Pattern.compile(
		"([+-]?\\d+e[+-]?\\d+)([fd]?)",
		Pattern.CASE_INSENSITIVE
	);

	/** RegEx pattern for matching decimal numbers with decimal point */
	private static final Pattern PATTERN_FLOAT_DECIMAL = Pattern.compile(
		"([+-]?(?:(?:0|[1-9]\\d*)?\\.\\d*)(?:e[+-]?\\d+)?)([df]?)",
		Pattern.CASE_INSENSITIVE
	);

	/** RegEx patterns for matching decimal numbers */
	private static final Pattern PATTERNS_FLOAT[] = {
		PATTERN_FLOAT_INTEGER,
		PATTERN_FLOAT_INTEGER_EXP,
		PATTERN_FLOAT_DECIMAL
	};

	/** Functions for converting BigDecimals into NBT Tags */
	private static final Map<String, Function<BigDecimal, Tag<?>>> FLOAT_CONVERTERS = Map.of(
		"f", v -> new TagFloat	(v.floatValue()),
		"d", v -> new TagDouble	(v.doubleValue())
	);

	/** Ranges for integer types */
	protected static final Map<String, Range> INTEGER_BOUNDS = Map.of(
		"b", new Range(BigInteger.valueOf(Byte.MIN_VALUE),		BigInteger.valueOf(Byte.MAX_VALUE),		v -> new TagByte	(v.byteValue())),
		"s", new Range(BigInteger.valueOf(Short.MIN_VALUE),		BigInteger.valueOf(Short.MAX_VALUE),	v -> new TagShort	(v.shortValue())),
		"",  new Range(BigInteger.valueOf(Integer.MIN_VALUE),	BigInteger.valueOf(Integer.MAX_VALUE),	v -> new TagInt		(v.intValue())),
		"l", new Range(BigInteger.valueOf(Long.MIN_VALUE),		BigInteger.valueOf(Long.MAX_VALUE),		v -> new TagLong	(v.longValue()))
	);
	
	/** Range for an integer type */
	protected static record Range(
			/** the lower bound */
			BigInteger min,
			/** the upper bound */
			BigInteger max,
			/** function for converting BigIntegers into NBT Tags */
			Function<BigInteger, Tag<?>> converter) {
		
		/**
		 * Checks if a number lies within this range
		 * 
		 * @param number a number
		 * @return whether a number lies within this range
		 */
		protected boolean inRange(BigInteger number) {
			return !(number.compareTo(min) < 0)
				&& !(number.compareTo(max) > 0);
		}
		
		/**
		 * Converts a number into an NBT Tag
		 * 
		 * @param number a number
		 * @return the new NBT tag
		 */
		protected Tag<?> convert(BigInteger number) {
			return converter().apply(number);
		}
		
	}
	
	private Reader reader;

	/** whether the end of the file has been reached */
	private boolean eof;

	/** whether the last character should be re-read */
	private boolean back;
	
	/** the absolute position in the string */
	private long index;
	
	/** the current character */
	private char current;
	
	/** cache containing the string processed so far */
	private StringBuilder cache;
	
	SNBTParser(Reader reader) {
		this.reader = reader.markSupported()
			? reader
			: new BufferedReader(reader);
		
		eof = false;
		back = false;
		
		index = -1;
		
		current = 0;
		
		cache = new StringBuilder();
	}
	
	/**
	 * Whether there are still characters left
	 * 
	 * @return whether there are still characters left
	 */
	protected boolean more() {
		if(back || eof)
			return back && !eof;
		
		return peek() > 0;
	}
	
	/**
	 * Force to re-read the last character
	 */
	protected void back() {
		back = true;
	}
	
	/**
	 * Returns the next character is without
	 * actually incrementing the position
	 * 
	 * @return the next character
	 */
	protected char peek() {
		if(eof)
			return 0;
		
		int c;
		
		try {
			reader.mark(1);
			
			c = reader.read();
			
			reader.reset();
		} catch(Exception e) {
			throw syntaxError("Could not peek from source", e);
		}
		
		return c == -1 ? 0 : (char) c;
	}
	
	/**
	 * Returns the next character
	 * 
	 * @return the next character
	 */
	protected char next() {
		if(back) {
			back = false;
			return current;
		}
		
		int c;
		
		try {
			c = reader.read();
			
			cache.append((char) c);
		} catch(Exception e) {
			throw syntaxError("Could not read from source", e);
		}
		
		if(c < 0) {
			eof = true;
			return 0;
		}
		
		current = (char) c;
		
		index++;
		
		return current;
	}

	/**
	 * Returns the next non-whitespace character
	 * 
	 * @return the next character
	 */
	protected char nextClean() {
		char c;
		do c = next(); while(Character.isWhitespace(c));
		return c;
	}
	
	/**
	 * Returns the next string-like object
	 * 
	 * @param alt use alternative validation check
	 * @return the next string
	 */
	protected String nextString(boolean alt) {
		char c = nextClean();
		
		StringBuilder sb = new StringBuilder();
		
		if(c == '"' || c == '\'') {
			char delim = c;
			
			while(true) {
				if(!more())
					throw syntaxError("Unexpected end of data");
				
				c = next();
				
				if(c == delim)
					break;
				
				if(c == '\r' || c == '\n')
					throw syntaxError("Illegal line terminator in string");
				
				if(c == '\\') {
					c = next();
					
					switch(c) {
					case '\'':
					case '"':
					case '\\':
						sb.append(c);
						continue;
					default:
						throw syntaxError("Illegal escape sequence '\\" + c + "'");
					}
				}
				
				sb.append(c);
			}
			
			return sb.toString();
		}
		
		Predicate<Character> isValuePart = alt
			? this::isValuePartAlt
			: SNBTParser::isValuePart;
		
		if(!isValuePart.test(c))
			throw syntaxError("Expected string");
		
		back();
		
		while(more()) {
			c = next();
			
			if(!isValuePart.test(c)) {
				back();
				break;
			}
			
			sb.append(c);
		}
		
		return sb.toString();
	}
	
	/**
	 * Parses and returns the next TAG_Compound
	 * 
	 * @return the next TAG_Compound
	 */
	protected TagCompound nextCompound() {
		if(nextClean() != '{')
			throw syntaxError("'{' expected in TAG_Compound");
		
		TagCompound compound = new TagCompound();

		char c = nextClean();
		
		while(true) {
			if(c == '}')
				break;
			
			back();
			
			String key = nextString(false);
			
			if(compound.has(key))
				throw syntaxError("Duplicate key " + SNBTStringifyer.quote(key) + " in TAG_Compound");
			
			c = nextClean();

			if(c != ':')
				throw syntaxError("':' expected in TAG_Compound");
			
			compound.put(key, nextTag());
			
			c = nextClean();
			
			if(c == ',') {
				c = nextClean();
				continue;
			}
			
			if(c == '}')
				break;
			
			throw syntaxError("'}' expected in TAG_Compound");
		}
		
		return compound;
	}
	
	private <T extends Tag<?>> void processList(T list, TagType expectation, Consumer<Tag<?>> consumer) {
		char c = nextClean();
		
		while(true) {
			if(c == ']')
				break;
			
			back();
			
			Tag<?> tag = nextTag();
			
			if(!tag.is(expectation))
				throw syntaxError("Expected " + expectation + " in " + list.getType());
			
			consumer.accept(tag);
			
			c = nextClean();
			
			if(c == ',') {
				c = nextClean();
				continue;
			}
			
			if(c == ']')
				break;
			
			throw syntaxError("']' expected in " + list.getType());
		}
	}
	
	private Tag<?> nextList() {
		char c = next();
		
		if(c == 'B' || c == 'I' || c == 'L') {
			if(peek() == ';') {
				TagArray<?, ?> array;
				TagType type;
				
				switch(c) {
				case 'B': type = TagType.BYTE; array = new TagByteArray(); break;
				case 'I': type = TagType.INT;  array = new TagIntArray();  break;
				case 'L': type = TagType.LONG; array = new TagLongArray(); break;
				default: return null;
				};
				
				next();
				
				processList(array, type, tag -> array.addTag((TagNumber<?>) tag));
				
				return array;
			}
			else back();
		}
		
		c = nextClean();
		
		if(c == ']')
			return TagList.emptyList();
		
		back();
		
		Tag<?> first = nextTag();
		
		TagList<Tag<?>> list = TagList.emptyList();
		list.add(first);
		
		c = nextClean();
		
		if(c == ',')
			processList(list, list.getComponentType(), list::add);
		
		else if(c != ']')
			throw syntaxError("']' expected in " + list.getType());
		
		return list;
	}
	
	private Tag<?> nextTag() {
		char c = nextClean();
		
		if(c == '[')
			return nextList();
		
		if(c == '{') {
			back();
			return nextCompound();
		}
		
		if(c == '"' || c == '\'') {
			back();
			return new TagString(nextString(false));
		}
		
		if(!isValuePart(c))
			throw syntaxError("Value expected");

		back();
		
		String str = nextString(false);
		
		Matcher matcher = PATTERN_BOOLEAN.matcher(str);
		
		if(matcher.matches())
			return new TagByte(str.equalsIgnoreCase("true"));
		
		matcher = PATTERN_INTEGER.matcher(str);
		
		if(matcher.matches()) {
			Range range = INTEGER_BOUNDS.get(matcher.group(2).toLowerCase());
			
			BigInteger val = new BigInteger(matcher.group(1));
			
			if(range.inRange(val))
				return range.convert(val);
			
			return new TagString(str);
		}
		
		boolean success = false;
		
		for(Pattern pattern : PATTERNS_FLOAT) {
			matcher = pattern.matcher(str);
			
			if(matcher.matches()) {
				success = true;
				break;
			}
		}
		
		if(!success)
			return new TagString(str);
		
		String suffix = matcher.group(2).toLowerCase();
		
		if(suffix.isEmpty())
			suffix = "d";
		
		return FLOAT_CONVERTERS
			.get(suffix)
			.apply(new BigDecimal(matcher.group(1)));
	}

	/**
	 * Checks whether a character is valid for string-like objects
	 * 
	 * @param c a character
	 * @return whether the character is valid 
	 */
	protected boolean isValuePartAlt(char c) {
		return false;
	}

	/**
	 * Checks whether a character is valid for string-like objects
	 * 
	 * @param c a character
	 * @return whether the character is valid 
	 */
	protected static boolean isValuePart(char c) {
		return (c >= 'a' && c <= 'z')
			|| (c >= 'A' && c <= 'Z')
			|| (c >= '0' && c <= '9')
			|| c == '_' || c == '.'
			|| c == '+' || c == '-';
	}

	/**
	 * Read a compound tag from a string
	 * 
	 * @param input an input string
	 * @return the parsed compound tag
	 */
	public static TagCompound parse(String input) {
		try(StringReader sr = new StringReader(input.strip())) {
			SNBTParser parser = new SNBTParser(sr);
			
			TagCompound tag = parser.nextCompound();
			
			if(parser.more())
				throw parser.syntaxError("Trailing data");
			
			return tag;
		}
	}
	
	private NBTException syntaxError(String message, Throwable cause) {
		return new NBTException(message + this, cause);
	}

	/**
	 * Constructs a new NBT exception
	 * 
	 * @param message the exception message
	 * @return an exception
	 */
	protected NBTException syntaxError(String message) {
		return syntaxError(message, null);
	}
	
	@Override
	public String toString() {
		String highlight = cache.toString();
		
		if(highlight.length() > 10)
			highlight = "..." + highlight.substring(highlight.length() - 10);
		
		if(back)
			highlight = highlight.substring(0, highlight.length() - 1);
		
		return " at position " + index + ": »" + highlight + "«";
	}

}
