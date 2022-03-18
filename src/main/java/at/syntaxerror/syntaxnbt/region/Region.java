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
package at.syntaxerror.syntaxnbt.region;

import at.syntaxerror.syntaxnbt.NBTCompression;
import at.syntaxerror.syntaxnbt.NBTException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * A structure holding a 32x32 area of {@link Chunk Chunks}
 * 
 * @author SyntaxError404
 * 
 */
public class Region {

	/**
	 * -- GETTER --
	 * 
	 * Returns the default compression scheme for chunks
	 * 
	 * @return the compression scheme
	 * 
	 * -- SETTER --
	 * 
	 * Sets the default compression scheme for chunks
	 * 
	 * @param compression the compression scheme
	 */
	@NonNull
	@Getter
	@Setter
	private NBTCompression compression;
	
	private int[] timestamps;
	
	private Chunk[] chunks;
	
	/**
	 * Constructs a new region
	 */
	public Region() {
		compression = NBTCompression.ZLIB;
		timestamps = new int[1024];
		chunks = new Chunk[1024];
	}
	
	private int at(int x, int z) {
		if(x < 0 || x > 31 || z < 0 || z > 31)
			throw new NBTException("Chunk coordinate out of bounds");
		
		return x * 32 + z;
	}

	
	/**
	 * Returns a specific chunk. A value of {@code null}
	 * suggest that the chunk has not been generated yet.
	 * 
	 * @param x chunk's X position ({@code 0 >= x < 32})
	 * @param z chunk's Z position ({@code 0 >= z < 32})
	 * @return a specific chunk
	 */
	public Chunk getChunk(int x, int z) {
		return chunks[at(x, z)];
	}

	/**
	 * Overrides a specific chunk
	 * 
	 * @param x chunk's X position ({@code 0 >= x < 32})
	 * @param z chunk's Z position ({@code 0 >= z < 32})
	 * @param chunk new chunk
	 * @return this region
	 */
	public Region setChunk(int x, int z, Chunk chunk) {
		chunks[at(x, z)] = chunk;
		return this;
	}
	
	/**
	 * Returns the timestamp for a specific chunk
	 * 
	 * @param x chunk's X position ({@code 0 >= x < 32})
	 * @param z chunk's Z position ({@code 0 >= z < 32})
	 * @return the timestamp for a specific chunk
	 */
	public int getTimestamp(int x, int z) {
		return timestamps[at(x, z)];
	}
	
	/**
	 * Overrides the timestamp for a specific chunk
	 * 
	 * @param x chunk's X position ({@code 0 >= x < 32})
	 * @param z chunk's Z position ({@code 0 >= z < 32})
	 * @param time new timestamp
	 * @return this region
	 */
	public Region setTimestamp(int x, int z, int time) {
		timestamps[at(x, z)] = time;
		return this;
	}
	
}
