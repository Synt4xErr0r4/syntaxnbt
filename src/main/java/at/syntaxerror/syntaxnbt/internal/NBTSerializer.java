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

import java.io.DataOutputStream;
import java.io.IOException;

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
 * A utility class for serializing NBT tags
 * 
 * @author SyntaxError404
 * 
 */
public class NBTSerializer {

	private static void serializeCompound(TagCompound compound, DataOutputStream output, int depth) throws IOException {
		if(depth-- > 0)
			for(String key : compound.keySet()) {
				Tag<?> value = compound.get(key);
				
				output.writeByte(value.getType().getId());
				output.writeUTF(key);
				
				serialize(value, output, depth);
			}
		
		output.writeByte(TagType.END.getId());
	}
	
	private static void serializeList(TagList<?> list, DataOutputStream output, int depth) throws IOException {
		int sz;
		
		if(depth-- > 0)
			sz = list.size();
		else sz = 0;
		
		TagType type = list.getComponentType();
		
		if(type == null || type == TagType.END) {
			output.writeByte(TagType.END.getId());
			output.writeInt(0);
			
			return;
		}

		output.writeByte(type.getId());
		output.writeInt(sz);
		
		for(int i = 0; i < sz; ++i)
			serialize(list.get(i), output, depth);
	}
	
	private static <N extends Number> void serializeArray(TagArray<N, ?> array, DataOutputStream output,
			ArraySerializer<N> serializer) throws IOException {
		int sz = array.size();
		
		output.writeInt(sz);
		
		for(int i = 0; i < sz; ++i)
			serializer.serialize(array.get(i));
	}
	
	private static void serialize(Tag<?> tag, DataOutputStream output, int depth) throws IOException {
		if(tag instanceof TagCompound compound)
			serializeCompound(compound, output, depth);
		
		else if(tag instanceof TagList<?> list)
			serializeList(list, output, depth);
		
		else if(tag instanceof TagByteArray array)
			serializeArray(array, output, v -> output.writeByte(v));
		
		else if(tag instanceof TagIntArray array)
			serializeArray(array, output, output::writeInt);
		
		else if(tag instanceof TagLongArray array)
			serializeArray(array, output, output::writeLong);
		
		else if(tag instanceof TagString string)
			output.writeUTF(string.getValue());

		else if(tag instanceof TagNumber<?> number)
			switch(number.getType()) {
			case BYTE:		output.writeByte	(number.byteValue());	break;
			case SHORT:		output.writeShort	(number.shortValue());	break;
			case INT:		output.writeInt		(number.intValue());	break;
			case LONG:		output.writeLong	(number.longValue());	break;
			case FLOAT:		output.writeFloat	(number.floatValue());	break;
			case DOUBLE:	output.writeDouble	(number.doubleValue());	break;
			
			default: throw new NBTException("Tag is not serializable: " + tag);
			}
		
		else throw new NBTException("Tag is not serializable: " + tag);
	}

	/**
	 * Writes a compound tag to a stream (NBT format)
	 * 
	 * @param name name of the compound tag (optional)
	 * @param compound compound tag to be serialized
	 * @param output stream to write to
	 * @throws IOException if an I/O error occured
	 */
	public static void serialize(String name, TagCompound compound, DataOutputStream output) throws IOException {
		if(name == null)
			name = "";
		
		serialize(new TagCompound().put(name, compound), output, NBTUtil.MAX_DEPTH);
	}
	
	private static interface ArraySerializer<N extends Number> {
		
		void serialize(N num) throws IOException;
		
	}
	
}
