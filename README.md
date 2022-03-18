# SyntaxNBT [![javadoc](https://img.shields.io/endpoint?label=javadoc&url=https%3A%2F%2Fjavadoc.syntaxerror.at%2Fsyntaxnbt%2F%3Fbadge%3Dtrue%26version%3Dlatest)](https://javadoc.syntaxerror.at/syntaxnbt/latest) ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/Synt4xErr0r4/syntaxnbt/Java%20CI%20with%20Maven)

A NBT and SNBT library for Java (17)

## Overview

This is an implementation for serializing, deserializing, parsing, and stringifying NBTs (Named Binary Tags) by [Mojang Studios](https://mojang.com), according to this [specification](https://wiki.vg/NBT).

## Getting started

In order to use the code, you can either [download the jar](https://github.com/Synt4xErr0r4/syntaxnbt/releases/download/1.0.0/syntaxnbt-1.0.0.jar), or use the Maven dependency:

```xml
<!-- Repository -->

<repository>
  <id>syntaxerror.at</id>
  <url>https://maven.syntaxerror.at</url>
</repository>

<!-- Dependency -->

<dependency>
  <groupId>at.syntaxerror</groupId>
  <artifactId>syntaxnbt</artifactId>
  <version>1.0.0</version>
</dependency>
```

The library itself is located in the module `syntaxnbt`.

## Usage

In order to parse a binary NBT file or an SNBT string, you can use the [NBTUtil](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/NBTUtil.html) class:

```java
import java.io.InputStream;
import at.syntaxerror.syntaxnbt.tag.TagCompound;
import at.syntaxerror.syntaxnbt.NBTUtil;

// read NBT tag from a stream
try(InputStream stream = ...) {
    TagCompound tag = NBTUtil.deserialize(stream);
}

// parse SNBT tag from a string
TagCompound tag = NBTUtil.parse("...");
```

The other way around works analogously:

```java
import java.io.OutputStream;
import at.syntaxerror.syntaxnbt.NBTUtil;

// write NBT tag to a stream
try(OutputStream stream = ...) {
    NBTUtil.serialize(null, stream, tag);
}

// convert SNBT tag to a string
String snbt = NBTUtil.stringify(tag);

// or alternatively:
String snbt = tag.toString();
```

You can also create tags by yourself:

```java
import at.syntaxerror.syntaxnbt.tag.TagCompound;

TagCompound tag = new TagCompound();
TagList<TagString> list = new TagList<>(TagString.class);
TagInt num = new TagInt(1337);
TagByteArray array = new TagByteArray(new byte[] { 1, 2, 3 });
```

### Compounds

Compound Tags are lists of named tags (basically a [`Map<String, Tag<?>>`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Map.html)).
They are implemented in the [`TagCompound`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagCompound.html) class,
exposing the following methods:

- `putX(String key, X value)` - associates the specified value with the specified key in this compound tag. An existing mapping will be overridden
- `getX(String key)` - returns the value to which the specified key is mapped. Throws an exception if this map contains no mapping for the key
- `remove(String key)` - removes the entry associated with the key from the map, if present.
- `has(String key)` - returns true if this map contains a mapping for the specified key
- `size()` - returns the number of key-value mappings in this compound tag
- `clear()` - removes all of the mappings from this map

`X` stands for any of the supported types (listed below). You can specify either tags (e.g. `putByteTag`)
or values (e.g. `putByte`), which will automatically be wrapped around their respective tags.

When `X` is unspecified, the untyped tags are used instead (e.g. `put(String key, Tag<?> value)`).

### Lists

List Tags are lists of unnamed tags ([`List<Tag<?>>`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/List.html)),
only capable of holding tags of the same type (specified by the generic type). This type can also be queried by using the
[`getComponentType()`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagList.html#getComponentType()) method.

If the list is created via [`TagList.emptyList()`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagList.html#emptyList()),
the type of the list is determined by the first operation on the list involving types (e.g. `addByte`).

Lists are implemented in the [`TagList<?>`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagCompound.html) class,
exposing the following methods:

- `addX(X value)` - adds an element to the list
- `addX(int index, X value)` - inserts an element at the specified position in the list
- `setX(int index, X value)` - replaces an element at the specified position in the list
- `getX(int index)` - returns the element at the specified position in this list
- `remove(int index)` - removes the value at the specified position in the list
- `size()` - returns the number of entries in the list
- `clear()` - removes all entries from the list

When `X` is unspecified, the generic type is used instead (e.g. `add(ByteTag value)` for `List<ByteTag>`).

When trying to add a tag that is not compatible with the other tags in the list, an exception is thrown.  
  
In case you are dealing with a list of currently unknown type (`TagList<?>` or `TagList<Tag?>>`), you can
easily cast the list to the desired type by using the `asXList()` methods. This does not create a new list
and will throw an exception if the desired type is incompatible with the [`component type`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagList.html#getComponentType()).

### Array

Array Tags are arrays of values of the same type (`byte[]`, `int[]`, or `long[]`).

They are implemented in the
[`TagByteArray`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagByteArray.html),
[`TagIntArray`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagIntArray.html), and
[`TagLongArray`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagLongArray.html)
classes, which are all sub-classes of [`TagArray`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagArray.html),
exposing the following methods:

- `add(Number value)`, `addTag(T value)` - adds an element to the array
- `add(int index, Number value)`, `addTag(int index, T value)` - inserts an element at the specified position in the array
- `set(int index, Number value)`, `setTag(int index, T value)` - replaces an element at the specified position in the array
- `get(int index)` - returns the element at the specified position in this array
- `remove(int index)` - removes the value at the specified position in the list
- `size()` - returns the number of entries in the array
- `clear()` - removes all entries from the array

`T` stands for the tag-equivalent of elements in the array (e.g. for `TagByteArray`, `T` would be `TagByte`).
However, not the tag itself is added to the array, only the value stored in this tag.

## Tags

The NBT specification specifies the following tags:

ID | Name             | Java type | Implementation | Payload size (bytes) | Description
-- | ---------------- | --------- | -------------- | -------------------- | ----------
0  | `TAG_End`        | `void`    | [`TagEnd`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagEnd.html) | 0 | Signifies the end of a TAG_Compound. It is only ever used inside a TAG_Compound, and is not named despite being in a TAG_Compound
1  | `TAG_Byte`       | `byte`    | [`TagByte`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagByte.html) | 1 | A single signed byte
2  | `TAG_Short`      | `short`   | [`TagShort`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagShort.html) | 2 | A single signed, big endian 16 bit integer
3  | `TAG_Int`        | `int`     | [`TagInt`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagInt.html) | 4 | A single signed, big endian 32 bit integer
4  | `TAG_Long`       | `long`    | [`TagLong`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagLong.html) | 8 | A single signed, big endian 64 bit integer
5  | `TAG_Float`      | `float`   | [`TagFloat`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagFloat.html) | 4 | A single, big endian [IEEE-754](http://en.wikipedia.org/wiki/IEEE_754-2008) single-precision floating point number ([NaN](http://en.wikipedia.org/wiki/NaN) possible)
6  | `TAG_Double`     | `double`  | [`TagDouble`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagDouble.html) | 8 | A single, big endian [IEEE-754](http://en.wikipedia.org/wiki/IEEE_754-2008) double-precision floating point number ([NaN](http://en.wikipedia.org/wiki/NaN) possible)
7  | `TAG_Byte_Array` | `byte[]`  | [`TagByteArray`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagByteArray.html) | 4+n | A length-prefixed array of **signed** bytes. The prefix is a **signed** integer (thus 4 bytes)
8  | `TAG_String`     | `String`  | [`TagString`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagString.html) | 2+n | A length-prefixed [modified UTF-8 string](https://docs.oracle.com/javase/8/docs/api/java/io/DataInput.html#modified-utf-8). The prefix is an **unsigned** short (thus 2 bytes) signifying the length of the string in bytes
9  | `TAG_List`       | `List<Tag>` | [`TagList`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagList.html) | 5+x | A list of **nameless** tags, all of the same type. The list is prefixed with the Type ID of the items it contains (thus 1 byte), and the length of the list as a **signed** integer (a further 4 bytes). If the length of the list is 0 or negative, the type may be 0 (TAG_End) but otherwise it must be any other type. (The notchian implementation uses TAG_End in that situation, but another reference implementation by Mojang uses 1 instead; parsers should accept any type if the length is <= 0).
10 | `TAG_Compound`   | `Map<String, Tag>` | [`TagCompound`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagCompound.html) | 1+x | Effectively a list of a **named** tags. Order is not guaranteed.
11 | `TAG_Int_Array`  | `int[]`   | [`TagIntArray`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagIntArray.html) | 4+4*n | A length-prefixed array of **signed** integers. The prefix is a **signed** integer (thus 4 bytes) and indicates the number of 4 byte integers.
12 | `TAG_Long_Array` | `long[]`  | [`TagLongArray`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/tag/TagLongArray.html) | 4+8*n | A length-prefixed array of **signed** longs. The prefix is a **signed** integer (thus 4 bytes) and indicates the number of 8 byte longs.

## NBT Paths

An NBT Path is used to specify a particular element from an NBT tree. A path consists of multiple nodes, each of
which can be one of seven types (see [here](https://minecraft.fandom.com/wiki/NBT_path_format) for more information).

Paths are parsed via `NBTUtil.parsePath(String path)`, which returns a [`PathNode`](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/path/pathNode.html). Inside this class, there is the `traverse(Tag<?> tag)` method, which returns a List of Tags. When this method is called,
the NBT tree is traversed and all elements matching the path pattern are returned.

```java
import java.util.List;
import at.syntaxerror.syntaxnbt.tag.TagCompound;
import at.syntaxerror.syntaxnbt.tag.Tag;
import at.syntaxerror.syntaxnbt.path.PathNode;
import at.syntaxerror.syntaxnbt.NBTUtil;

// ...

String nbtString = "{ foo: { bar: baz } }";
String pathString = "foo{bar: baz}.bar";

TagCompound tag = NBTUtils.parse(nbtString);
PathNode path = NBTUtils.parsePath(pathString);

// contains a TAG_String with a value of "baz"
List<Tag<?>> result = path.traverse(tag);
```

## Region files

You can also read and write Minecraft's region files (typically named `r.X.Z.mcr`, where `X` and `Z` are the region coordinates).
Each region file contains 32x32 chunks, which further each consist of 16x384x16 blocks. The chunks itself are stored as (compressed)
NBT Tags, therefore the region files are basically similar to an array of NBT Tags, expect that they contain a 'last-modified' flag for each chunk.

Region file format:

Range           | Type              | Size   | Purpose
--------------- | ----------------- | ------ | -----------------------------------------------------------------------------
`0x0000-0x0FFF` | `location[1024]`  | 4KiB   | Offset and size of each chunk in the file
`0x1000-0x1FFF` | `timestamp[1024]` | 4KiB   | Array containing the time each chunk was last modified
`0x1FFF-?`      | `chunk[1024]`     | varies | The actual chunk, stored as a chunk header followed by the (compressed) NBT tag

Chunk header format:

Range       | Type             | Size     | Purpose
----------- | ---------------- | -------- | -------
`0x00-0x03` | `int`            | 4 bytes  | Length of the chunk data
`0x04`      | `byte`           | 1 byte   | Compression scheme; `1` = gzip, `2` = zlib (default), `3` = uncompressed
`0x05-X`    | `TagCompound`    | varies   | The chunk data itself
`X-4096*n`  | `byte[4096*n-X]` | 4096*n-X | Padding, so that the next chunk lies on a 4KiB-page boundary

The [Region](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/region/Region.html) class implements regions,
the [Chunk](https://javadoc.syntaxerror.at/syntaxnbt/latest/syntaxnbt/at/syntaxerror/syntaxnbt/region/Chunk.html) class implements chunks.

They can either be created via their respective constructors or deserialized from a file; you can also serialize them:

```java
import java.io.InputStream;
import at.syntaxerror.syntaxnbt.region.Region;
import at.syntaxerror.syntaxnbt.NBTUtil;

// read Region from a stream
try(InputStream stream = ...) {
    Region region = NBTUtil.deserializeRegion(stream);
}

// write Region to a stream
try(OutputStream stream = ...) {
    NBTUtil.serializeRegion(stream, region);
}
```

Chunks can then be acquired by using the `getChunk(int x, int z)` method, where `x` and `z` are the relative
chunk coordinates ranging from `0` to `31` (inclusive).

If you want to replace a chunk in a region, you can use the `setChunk(int x, int z, Chunk chunk)` method.

The compression scheme is defined by the Region object, but can be overridden for each Chunk individually (via `setCompression(NBTCompression scheme)`).

## Limits

By default, only the first 512 layers of the `Tag<?>` structure are serialized/stringified.
This is added as a countermeasure to circular references, which would create an infinite
loop and eventually cause a StackOverflowError, writing to a stream indefinitely or similar.  
This threshold, however, is configurable by modifying the `MAX_DEPTH` field in the `NBTUtil` class.

## Documentation

The JavaDoc for the latest version can be found [here](https://javadoc.syntaxerror.at/syntaxnbt/latest).

## License

This project is licensed under the [MIT License](https://github.com/Synt4xErr0r4/syntaxnbt/blob/main/LICENSE)