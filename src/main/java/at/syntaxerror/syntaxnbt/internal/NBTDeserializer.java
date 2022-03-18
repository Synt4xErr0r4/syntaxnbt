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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * A utility class for deserializing NBT tags
 * 
 * @author SyntaxError404
 * 
 */
public class NBTDeserializer {

	private static void deserializeCompound(TagCompound compound, DataInputStream input) throws IOException {
		Map<String, Tag<?>> tags = new HashMap<>();
		
		while(true) {
			TagType type = TagType.getTypeFromId(input.readByte());
			
			if(type == TagType.END)
				break;
			
			String key = input.readUTF();
			
			tags.put(key, deserializeNew(type, input));
		}
		
		compound.setValue(tags);
	}
	
	@SuppressWarnings("unchecked")
	private static void deserializeList(TagList<?> list, DataInputStream input) throws IOException {
		int sz = input.readInt();
		
		List<Tag<?>> tags = new ArrayList<>();
		
		for(int i = 0; i < sz; ++i)
			tags.add(deserializeNew(list.getComponentType(), input));
		
		((TagList<Tag<?>>) list).setValue(tags);
	}
	
	private static <N extends Number> void deserializeArray(TagArray<N, ?> array, DataInputStream input,
			ArrayDeserializer<N> deserializer) throws IOException {
		int sz = input.readInt();
		
		array.clear();
		
		for(int i = 0; i < sz; ++i)
			array.add(deserializer.deserialize());
	}

	private static Tag<?> deserializeNew(TagType type, DataInputStream input) throws IOException {
		Tag<?> tag;
		
		if(type == TagType.LIST) {
			type = TagType.getTypeFromId(input.readByte());
			
			if(type == TagType.END) {
				input.readInt();
				return TagList.emptyList();
			}
			
			try {
				tag = (Tag<?>) TagType.LIST
					.getTagClass()
					.getConstructor(Class.class)
					.newInstance(type.getTagClass());
			} catch (Exception e) {
				throw new NBTException("Failed to instantiate TAG_List", e);
			}
		}
		else try {
			tag = (Tag<?>) type.getTagClass()
				.getConstructor()
				.newInstance();
		} catch (Exception e) {
			throw new NBTException("Failed to instantiate " + type, e);
		}
		
		deserializeTag(tag, input);
		
		return tag;
	}
	
	private static <T extends Tag<?>> T deserializeTag(T tag, DataInputStream input) throws IOException {
		if(tag instanceof TagCompound compound)
			deserializeCompound(compound, input);
		
		else if(tag instanceof TagList<?> list)
			deserializeList(list, input);
		
		else if(tag instanceof TagByteArray array)
			deserializeArray(array, input, input::readByte);
		
		else if(tag instanceof TagIntArray array)
			deserializeArray(array, input, input::readInt);
		
		else if(tag instanceof TagLongArray array)
			deserializeArray(array, input, input::readLong);
		
		else if(tag instanceof TagString string)
			string.setValue(input.readUTF());

		else if(tag instanceof TagNumber<?> number)
			switch(number.getType()) {
			case BYTE:		((TagByte)		number).setValue(input.readByte());		break;
			case SHORT:		((TagShort)		number).setValue(input.readShort());	break;
			case INT:		((TagInt)		number).setValue(input.readInt());		break;
			case LONG:		((TagLong)		number).setValue(input.readLong());		break;
			case FLOAT:		((TagFloat)		number).setValue(input.readFloat());	break;
			case DOUBLE:	((TagDouble)	number).setValue(input.readDouble());	break;
			
			default: throw new NBTException("Tag is not deserializable: " + tag);
			}
		
		else throw new NBTException("Tag is not deserializable: " + tag);
		
		return tag;
	}
	
	/**
	 * Reads and populates a compound tag from a stream (NBT format)
	 * 
	 * @param tag destination tag
	 * @param input stream to read to
	 * @return the deserialized tag
	 * @throws IOException if an I/O error occured
	 */
	public static TagCompound deserialize(TagCompound tag, DataInputStream input) throws IOException {
		if(input.readByte() != TagType.COMPOUND.getId())
			throw new NBTException("Invalid ID for TAG_Compound");
		
		String name = input.readUTF();
		
		tag.putCompound(name, deserializeTag(new TagCompound(), input));
		
		return tag;
	}
	
	private static interface ArrayDeserializer<N extends Number> {
		
		N deserialize() throws IOException;
		
	}
	
}
