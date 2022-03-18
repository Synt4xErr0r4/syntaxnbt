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

import java.util.HashMap;
import java.util.Map;

import at.syntaxerror.syntaxnbt.NBTException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This enum contains all valid types of NBT tags.<br>
 * Information according to <a href="https://wiki.vg/NBT#Specification">wiki.vg</a>
 * 
 * @author SyntaxError404
 */
@Getter
@RequiredArgsConstructor
public enum TagType {

	/** Signifies the end of a TAG_Compound. It is only ever used inside a TAG_Compound, and is not named despite being in a TAG_Compound */
	END			(0, "TAG_End", TagEnd.class),
	
	/** A single signed byte */
	BYTE		(1, "TAG_Byte", TagByte.class),
	
	/** A single signed, big endian 16 bit integer */
	SHORT		(2, "TAG_Short", TagShort.class),
	
	/** A single signed, big endian 32 bit integer */
	INT			(3, "TAG_Int", TagInt.class),
	
	/** A single signed, big endian 64 bit integer */
	LONG		(4, "TAG_Long", TagLong.class),
	
	/**
	 * A single, big endian <a href="http://en.wikipedia.org/wiki/IEEE_754-2008">IEEE-754</a> single-precision
	 * floating point number (<a href="http://en.wikipedia.org/wiki/NaN">NaN</a> possible)
	 */
	FLOAT		(5, "TAG_Float", TagFloat.class),

	/**
	 * A single, big endian <a href="http://en.wikipedia.org/wiki/IEEE_754-2008">IEEE-754</a> double-precision
	 * floating point number (<a href="http://en.wikipedia.org/wiki/NaN">NaN</a> possible)
	 */
	DOUBLE		(6, "TAG_Double", TagDouble.class),
	
	/** A length-prefixed array of <b>signed</b> bytes. The prefix is a <b>signed</b> integer (thus 4 bytes) */
	BYTE_ARRAY	(7, "TAG_Byte_Array", TagByteArray.class),
	
	/**
	 * A length-prefixed <a href="https://docs.oracle.com/javase/8/docs/api/java/io/DataInput.html#modified-utf-8">modified UTF-8</a>
	 * string. The prefix is an <b>unsigned</b> short (thus 2 bytes) signifying the length of the string in bytes
	 */
	STRING		(8, "TAG_String", TagString.class),
	
	/**
	 * A list of <b>nameless</b> tags, all of the same type. The list is prefixed with the {@link TagType Type ID} of the items it contains
	 * (thus 1 byte), and the length of the list as a <b>signed</b> integer (a further 4 bytes). If the length of the list is 0 or negative,
	 * the type may be 0 (TAG_End) but otherwise it must be any other type. (The notchian implementation uses TAG_End in that situation,
	 * but another reference implementation by Mojang uses 1 instead; parsers should accept any type if the length is &lt;= 0).
	 */
	LIST		(9, "TAG_List", TagList.class),
	
	/** Effectively a list of a <b>named</b> tags. Order is not guaranteed. */
	COMPOUND	(10, "TAG_Compound", TagCompound.class),
	
	/**
	 * A length-prefixed array of <b>signed</b> integers. The prefix is a <b>signed</b> integer (thus 4 bytes)
	 * and indicates the number of 4 byte integers. 
	 */
	INT_ARRAY	(11, "TAG_Int_Array", TagIntArray.class),
	
	/**
	 * A length-prefixed array of <b>signed</b> longs. The prefix is a <b>signed</b> integer (thus 4 bytes)
	 * and indicates the number of 8 byte longs. 
	 */
	LONG_ARRAY	(12, "TAG_Long_Array", TagLongArray.class);
	
	private static final Map<Class<?>, TagType> CLASS_MAPPING = new HashMap<>();
	private static final Map<Integer, TagType> ID_MAPPING = new HashMap<>();
	
	static {
		for(TagType type : values()) {
			CLASS_MAPPING.put(type.getTagClass(), type);
			ID_MAPPING.put(type.getId(), type);
		}
	}

	/**
	 * Converts a {@link #getTagClass() class} into its corresponding tag type
	 * 
	 * @param clazz a class
	 * @return the corresponding type
	 */
	public static TagType getTypeFromClass(Class<?> clazz) {
		if(!CLASS_MAPPING.containsKey(clazz))
			throw new NBTException("Unrecognized Tag class: " + clazz);
		
		return CLASS_MAPPING.get(clazz);
	}
	
	/**
	 * Converts an {@link #getId() ID} into its corresponding tag type
	 * 
	 * @param id an ID
	 * @return the corresponding type
	 */
	public static TagType getTypeFromId(int id) {
		if(!ID_MAPPING.containsKey(id))
			throw new NBTException("Unrecognized Tag id: " + id);
		
		return ID_MAPPING.get(id);
	}
	
	/**
	 * Returns the ID for this type
	 * 
	 * @return the ID for this type
	 */
	private final int id;
	
	/**
	 * Returns the name for this type
	 * 
	 * @return the name for this type
	 */
	private final String name;
	
	/**
	 * Returns the implementation class for this type
	 * 
	 * @return the implementation class for this type
	 */
	private final Class<?> tagClass;
	
	@Override
	public String toString() {
		return name;
	}
	
}
