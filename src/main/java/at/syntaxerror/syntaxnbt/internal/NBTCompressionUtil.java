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

import java.io.IOException;
import java.io.InputStream;

import at.syntaxerror.syntaxnbt.NBTCompression;
import lombok.experimental.UtilityClass;

/**
 * A utility class for gzip and zlib compression
 * 
 * @author SyntaxError404
 * 
 */
@UtilityClass
public class NBTCompressionUtil {

	/**
	 * Determines the compression of the stream.
	 * The stream must support {@link InputStream#mark(int)} and {@link InputStream#reset()}
	 * 
	 * @param stream stream to be decompressed
	 * @return the compression scheme
	 * @throws IOException if an I/O error occured
	 */
	public static NBTCompression findCompression(InputStream stream) throws IOException {
		stream.mark(2);
		
		byte[] header = new byte[2];
		
		NBTCompression compression = NBTCompression.NONE;
		
		if(stream.read(header) == 2) {
			int msb = header[0] & 0xFF;
			int magic = (msb << 8) | (header[1] & 0xFF);
			
			if(magic == 0x1F8B)
				compression = NBTCompression.GZIP;
			
			else if(msb == 0x78 && (magic % 31) == 0)
				compression = NBTCompression.ZLIB;
		}
		
		stream.reset();
		
		return compression;
	}
	
}
