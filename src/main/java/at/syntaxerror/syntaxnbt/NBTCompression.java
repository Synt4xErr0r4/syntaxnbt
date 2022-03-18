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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This enum contains all supported compression schemes for NBT files
 * 
 * @author SyntaxError404
 * 
 */
@RequiredArgsConstructor
public enum NBTCompression {

	/** compress NBT using gzip */
	GZIP((byte) 1, GZIPOutputStream::new, GZIPInputStream::new),
	
	/** compress NBT using zlib */
	ZLIB((byte) 2, DeflaterOutputStream::new, InflaterInputStream::new),
	
	/** do not compress NBT */
	NONE((byte) 3, stream -> stream, stream -> stream);
	
	private static final Map<Byte, NBTCompression> MAPPING = new HashMap<>();
	
	static {
		for(NBTCompression compression : values())
			MAPPING.put(compression.getId(), compression);
	}

	/**
	 * Converts an {@link #getId() ID} into its corresponding compression scheme
	 * 
	 * @param id an ID
	 * @return the corresponding scheme
	 */
	public static NBTCompression getCompressionFromId(byte id) {
		if(!MAPPING.containsKey(id))
			throw new NBTException("Unrecognized compression scheme: " + id);
		
		return MAPPING.get(id);
	}
	
	/**
	 * Returns the id of this compression scheme
	 * 
	 * @return the id of this compression scheme
	 */
	@Getter
	private final byte id;
	
	private final StreamFactory<OutputStream> outputStreamFactory;
	private final StreamFactory<InputStream> inputStreamFactory;

	/**
	 * Wraps an output stream around the corresponding decompressor stream
	 * 
	 * @param stream stream to be decompressed
	 * @return the decompressed stream
	 * @throws IOException if an I/O error occured
	 */
	public OutputStream newOutputStream(OutputStream stream) throws IOException {
		return outputStreamFactory.construct(stream);
	}

	/**
	 * Wraps an input stream around the corresponding compressor stream
	 * 
	 * @param stream stream to be compressed
	 * @return the compressed stream
	 * @throws IOException if an I/O error occured
	 */
	public InputStream newInputStream(InputStream stream) throws IOException {
		return inputStreamFactory.construct(stream);
	}
	
	private static interface StreamFactory<S> {
		
		S construct(S out) throws IOException;
		
	}
	
}
