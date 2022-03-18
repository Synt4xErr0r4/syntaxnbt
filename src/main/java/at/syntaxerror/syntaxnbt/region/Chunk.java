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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import at.syntaxerror.syntaxnbt.NBTCompression;
import at.syntaxerror.syntaxnbt.NBTException;
import at.syntaxerror.syntaxnbt.NBTUtil;
import at.syntaxerror.syntaxnbt.tag.TagCompound;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * A structure holding information about one Minecraft chunk (16x384x16 blocks)
 * 
 * @author SyntaxError404
 * 
 */
@Getter
public class Chunk {

	/**
	 * Returns the chunk data. After making changes to
	 * this tag (or any of its sub-tags), {@link #setDirty(boolean) setDirty(true)}
	 * has to be called in order to apply those changes.
	 * 
	 * @return the chunk data
	 */
	private TagCompound data;

	@Getter(AccessLevel.NONE)
	private byte[] serialized;

	/**
	 * Checks whether this chunk is marked as dirty (has been
	 * changed since last serialization)
	 * 
	 * @return whether this chunk is marked as dirty
	 */
	private boolean dirty;
	
	/**
	 * Returns the compression scheme used for this chunk
	 * 
	 * @return the compression scheme
	 */
	private NBTCompression compression;
	
	/**
	 * Constructs a new empty chunk. The data within this
	 * chunk is an empty compound tag and therefore <b>not</b>
	 * compatible with the default Minecraft client
	 */
	public Chunk() {
		data = new TagCompound();
	}
	
	/**
	 * Constructs a new chunk from a compound tag
	 * 
	 * @param raw the chunk data
	 */
	public Chunk(TagCompound raw) {
		setRaw(raw);
	}
	
	/**
	 * Sets whether this chunk is marked as dirty (has been
	 * changed since last serialization).
	 * 
	 * @param dirty whether this chunk is marked as dirty
	 * @return this chunk
	 */
	public Chunk setDirty(boolean dirty) {
		this.dirty = dirty;
		return this;
	}
	
	/**
	 * Sets the compression scheme used for this chunk.
	 * If {@code null}, the scheme defined by the Region
	 * ({@link Region#getCompression()}) is used instead.
	 * 
	 * @param compression the compression scheme used for this chunk
	 * @return this chunk
	 */
	public Chunk setCompression(NBTCompression compression) {
		this.compression = compression;
		return setDirty(true);
	}
	
	/**
	 * Overrides the chunk data in this chunk
	 * 
	 * @param data the chunk data
	 * @return this chunk
	 */
	public Chunk setRaw(TagCompound data) {
		this.data = data;
		return setDirty(true);
	}
	
	/**
	 * Serializes this chunk. Do <b>not</b> alter the
	 * returned byte array!
	 * 
	 * @param compression the default compression scheme
	 * @return the serialized data
	 * @deprecated internal use only
	 */
	@Deprecated
	public byte[] serialize(NBTCompression compression) {
		if(!dirty)
			return serialized;
		
		compression = Objects.requireNonNullElse(this.compression, compression);
		
		try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			
			out.write(compression.getId());
			
			NBTUtil.serialize(null, data, out, compression);
			
			return serialized = out.toByteArray();
		} catch (IOException e) {
			throw new NBTException("Failed to serialize Chunk", e);
		}
	}
	
	/**
	 * Defines the world generation status of this chunk.
	 * All status except {@link ChunkStatus#FULL} are used for chunks called
	 * <i>proto-chunks</i>, in other words, for chunks with incomplete generation.
	 * 
	 * @author SyntaxError404
	 */
	public static enum ChunkStatus {
		/** for proto-chunks */
		EMPTY,
		/** for proto-chunks */
		STRUCTURE_STARTS,
		/** for proto-chunks */
		STRUCTURE_REFERENCES,
		/** for proto-chunks */
		BIOMES,
		/** for proto-chunks */
		NOISE,
		/** for proto-chunks */
		SURFACE,
		/** for proto-chunks */
		CARVERS,
		/** for proto-chunks */
		LIQUID_CARVERS,
		/** for proto-chunks */
		FEATURES,
		/** for proto-chunks */
		LIGHT,
		/** for proto-chunks */
		SPAWN,
		/** for proto-chunks */
		HEIGHTMAPS,
		
		/** for normal chunks */
		FULL
		
	}
	
}
