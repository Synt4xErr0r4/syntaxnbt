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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import at.syntaxerror.syntaxnbt.NBTCompression;
import at.syntaxerror.syntaxnbt.NBTException;
import at.syntaxerror.syntaxnbt.NBTUtil;
import at.syntaxerror.syntaxnbt.region.Chunk;
import at.syntaxerror.syntaxnbt.region.Region;
import lombok.experimental.UtilityClass;

/**
 * A utility class for reading and writing region files
 * 
 * @author SyntaxError404
 * 
 */
@UtilityClass
@SuppressWarnings("deprecation")
public class RegionUtil {

	/**
	 * Writes a region to a stream
	 * 
	 * @param region region to be serialized
	 * @param output stream to write to
	 * @throws IOException if an I/O error occured
	 */
	public static void serialize(Region region, RandomAccessFile output) throws IOException {
		try(ByteArrayOutputStream locationBytes = new ByteArrayOutputStream();
			ByteArrayOutputStream timestampBytes = new ByteArrayOutputStream();
			DataOutputStream locationStream = new DataOutputStream(locationBytes);
			DataOutputStream timestampStream = new DataOutputStream(timestampBytes)) {
			
			int offset = 2; // 2*4KiB headers ==> initial offset = 2
			
			for(int x = 0; x < 32; ++x)
				for(int z = 0; z < 32; ++z) {
					Chunk chunk = region.getChunk(x, z);
					
					if(chunk == null) {
						locationStream.writeInt(0);
						timestampStream.writeInt(0);
						continue;
					}
					
					byte[] buf = chunk.serialize(region.getCompression());
					
					int size = buf.length / 4096 + ((buf.length % 4096) == 0 ? 0 : 1);
					
					if(size != (size & 0xFF))
						throw new NBTException("Chunk is too big (>1MiB)");
					
					locationStream.writeInt((offset << 8) | size);
					timestampStream.writeInt(region.getTimestamp(x, z));
					
					output.seek(offset * 4096);
					output.write(buf);
					
					offset += size;
				}
			
			output.seek(0);
			output.write(locationBytes.toByteArray());
			
			if(output.getFilePointer() != 4096)
				throw new NBTException("Location header size mismatch");
			
			output.write(timestampBytes.toByteArray());

			if(output.getFilePointer() != 8192)
				throw new NBTException("Timestamp header size mismatch");
		}
	}

	/**
	 * Reads a region from a stream (NBT format)
	 * 
	 * @param input stream to read to
	 * @return the deserialized region
	 * @throws IOException if an I/O error occured
	 */
	public static Region deserialize(RandomAccessFile input) throws IOException {
		Region region = new Region();
		
		byte[] locationBytes = new byte[4096];
		byte[] timestampBytes = new byte[4096];
		
		input.read(locationBytes);
		input.read(timestampBytes);
		
		try(DataInputStream locationStream = new DataInputStream(new ByteArrayInputStream(locationBytes));
			DataInputStream timestampStream = new DataInputStream(new ByteArrayInputStream(timestampBytes))) {

			for(int x = 0; x < 32; ++x)
				for(int z = 0; z < 32; ++z) {
					int location = locationStream.readInt();
					int timestamp = timestampStream.readInt();
					
					region.setTimestamp(x, z, timestamp);
					
					if(location == 0) // chunk has not been generated yet
						continue;
					
					input.seek(((location >> 8) & 0xFFFFFF) * 4096L);
					
					byte[] buf = new byte[input.readInt()];
					
					input.read(buf);
					
					try(InputStream in = NBTCompression.ZLIB.newInputStream(new ByteArrayInputStream(buf, 1, buf.length-1));
						FileOutputStream fos = new FileOutputStream("/run/media/thomas/Shared/Git/syntaxnbt/src/test/test.mcr")) {
						
						in.transferTo(fos);
					}
					
					try(ByteArrayInputStream buffer = new ByteArrayInputStream(buf)) {
						
						NBTCompression compression = NBTCompression.getCompressionFromId((byte) buffer.read());
						
						Chunk chunk = new Chunk(
							NBTUtil.deserialize(buffer, compression)
								.getCompound("")
						);
						
						chunk.setCompression(compression);
						
						region.setChunk(x, z, chunk);
					}
				}
		}
		
		return region;
	}
	
}
