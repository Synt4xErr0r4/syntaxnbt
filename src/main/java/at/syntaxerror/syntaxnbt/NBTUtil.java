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
package at.syntaxerror.syntaxnbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import at.syntaxerror.syntaxnbt.internal.NBTCompressionUtil;
import at.syntaxerror.syntaxnbt.internal.NBTDeserializer;
import at.syntaxerror.syntaxnbt.internal.NBTSerializer;
import at.syntaxerror.syntaxnbt.internal.PathParser;
import at.syntaxerror.syntaxnbt.internal.RegionUtil;
import at.syntaxerror.syntaxnbt.internal.SNBTParser;
import at.syntaxerror.syntaxnbt.internal.SNBTStringifyer;
import at.syntaxerror.syntaxnbt.path.PathNode;
import at.syntaxerror.syntaxnbt.region.Region;
import at.syntaxerror.syntaxnbt.tag.TagCompound;
import lombok.experimental.UtilityClass;

/**
 * A utility class for serializing and deserializing NBT and SNBT 
 * 
 * @author SyntaxError404
 * 
 */
@UtilityClass
public class NBTUtil {

	/**
	 * The default maximum number of layers to traverse when
	 * {@link #serialize(String, TagCompound, DataOutputStream) serializing}/{@link #stringify(TagCompound) stringifying}
	 */
	public static int MAX_DEPTH = 512;

	/**
	 * Reads and populates a compound tag from a stream (NBT format).
	 * If the stream is {@link NBTCompression compressed}, it is decompressed automatically
	 * 
	 * @param input stream to read from
	 * @return the deserialized tag
	 * @throws IOException if an I/O error occured
	 * @see #deserialize(InputStream, NBTCompression)
	 */
	public static TagCompound deserialize(InputStream input) throws IOException {
		return deserialize(input, NBTCompressionUtil.findCompression(input));
	}

	/**
	 * Reads and populates a compound tag from a stream (NBT format)
	 * 
	 * @param input stream to read from
	 * @param compression compression of the stream
	 * @return the deserialized tag
	 * @throws IOException if an I/O error occured
	 * @see #deserialize(InputStream)
	 */
	public static TagCompound deserialize(InputStream input, NBTCompression compression) throws IOException {
		try(InputStream decompressed = compression.newInputStream(input);
			DataInputStream in = new DataInputStream(decompressed)) {
			return NBTDeserializer.deserialize(new TagCompound(), in);
		}
	}

	/**
	 * Reads and populates a region from a file
	 * 
	 * @param input file to read from
	 * @return the deserialized region
	 * @throws IOException if an I/O error occured
	 */
	public static Region deserializeRegion(RandomAccessFile input) throws IOException {
		return RegionUtil.deserialize(input);
	}

	/**
	 * Writes a compound tag to a stream (NBT format)
	 * 
	 * @param name name of the compound tag (optional)
	 * @param compound compound tag to be serialized
	 * @param output stream to write to
	 * @throws IOException if an I/O error occured
	 * @see #serialize(String, TagCompound, OutputStream, NBTCompression)
	 */
	public static void serialize(String name, TagCompound compound, OutputStream output) throws IOException {
		serialize(name, compound, output, NBTCompression.NONE);
	}

	/**
	 * Writes a compound tag to a stream (NBT format)
	 * 
	 * @param name name of the compound tag (optional)
	 * @param compound compound tag to be serialized
	 * @param output stream to write to
	 * @param compression compression scheme to apply to the stream
	 * @throws IOException if an I/O error occured
	 * @see #serialize(String, TagCompound, OutputStream)
	 */
	public static void serialize(String name, TagCompound compound, OutputStream output, NBTCompression compression) throws IOException {
		try(OutputStream compressed = compression.newOutputStream(output);
			DataOutputStream out = new DataOutputStream(compressed)) {
			NBTSerializer.serialize(name, compound, out);
		}
	}

	/**
	 * Writes a region to a file
	 * 
	 * @param region region to be serialized
	 * @param output file to write to
	 * @throws IOException if an I/O error occured
	 */
	public static void serializeRegion(Region region, RandomAccessFile output) throws IOException {
		RegionUtil.serialize(region, output);
	}
	
	/**
	 * Reads and populates a compound tag from a string (SNBT format)
	 * 
	 * @param input string to read from
	 * @return the parsed tag
	 */
	public static TagCompound parse(String input) {
		return SNBTParser.parse(input);
	}
	
	/**
	 * Reads an NBT path from a string
	 * 
	 * @param input string to read from
	 * @return the parsed path
	 */
	public static PathNode parsePath(String input) {
		return PathParser.parsePath(input);
	}
	
	/**
	 * Writes a compound tag to a string (SNBT format)
	 * 
	 * @param compound compound tag to be stringifyed
	 * @return the stringifyed compound tag
	 */
	public static String stringify(TagCompound compound) {
		return stringify(compound, false);
	}
	
	/**
	 * Writes a compound tag to a string (SNBT format).
	 * If {@code colored}, the string will contain color codes
	 * (according to Minecraft's old chat color system using section signs {@code §})
	 * 
	 * @param compound compound tag to be stringifyed
	 * @param colored whether the string should contain color codes
	 * @return the stringifyed compound tag
	 */
	public static String stringify(TagCompound compound, boolean colored) {
		return SNBTStringifyer.stringify(compound, colored);
	}
	
	/**
	 * Writes an NBT path to a string
	 * 
	 * @param path path node to be stringifyed
	 * @return the stringifyed path
	 */
	public static String stringify(PathNode path) {
		return path.toString();
	}
	
}
