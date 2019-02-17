// This file is part of Dotterel which is released under GPL-2.0-or-later.
// See file <LICENSE.txt> or go to <http://www.gnu.org/licenses/> for full license details.

package nimble.dotterel.util

import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonObject
import com.eclipsesource.json.JsonValue

@JvmName("toJsonArray")
fun List<JsonValue>.toJson() = JsonArray()
	.also({
		for(key in this)
			it.add(key)
	})
@JvmName("toBooleanJsonArray")
fun List<Boolean>.toJson() = JsonArray()
	.also({
		for(key in this)
			it.add(key)
	})
@JvmName("toIntJsonArray")
fun List<Int>.toJson() = JsonArray()
	.also({
		for(key in this)
			it.add(key)
	})
@JvmName("toLongJsonArray")
fun List<Long>.toJson() = JsonArray()
	.also({
		for(key in this)
			it.add(key)
	})
@JvmName("toFloatJsonArray")
fun List<Float>.toJson() = JsonArray()
	.also({
		for(key in this)
			it.add(key)
	})
@JvmName("toDoubleJsonArray")
fun List<Double>.toJson() = JsonArray()
	.also({
		for(key in this)
			it.add(key)
	})
@JvmName("toStringJsonArray")
fun List<String>.toJson() = JsonArray()
	.also({
		for(key in this)
			it.add(key)
	})


@JvmName("toJsonObject")
fun Map<String, JsonValue>.toJson() = JsonObject()
	.also({
		for(kv in this)
			it.add(kv.key, kv.value)
	})
@JvmName("toBooleanJsonObject")
fun Map<String, Boolean>.toJson() = JsonObject()
	.also({
		for(kv in this)
			it.add(kv.key, kv.value)
	})
@JvmName("toIntJsonObject")
fun Map<String, Int>.toJson() = JsonObject()
	.also({
		for(kv in this)
			it.add(kv.key, kv.value)
	})
@JvmName("toLongJsonObject")
fun Map<String, Long>.toJson() = JsonObject()
	.also({
		for(kv in this)
			it.add(kv.key, kv.value)
	})
@JvmName("toFloatJsonObject")
fun Map<String, Float>.toJson() = JsonObject()
	.also({
		for(kv in this)
			it.add(kv.key, kv.value)
	})
@JvmName("toDoubleJsonObject")
fun Map<String, Double>.toJson() = JsonObject()
	.also({
		for(kv in this)
			it.add(kv.key, kv.value)
	})
@JvmName("toStringJsonObject")
fun Map<String, String>.toJson() = JsonObject()
	.also({
		for(kv in this)
			it.add(kv.key, kv.value)
	})


fun <T> JsonObject.mapKeys(transform: (JsonObject.Member) -> T) =
	this.associateBy({ transform(it) }, { it.value })
fun <T> JsonObject.mapValues(transform: (JsonObject.Member) -> T) =
	this.associateBy({ it.name }, { transform(it) })


fun JsonObject.setNotNull(name: String, value: JsonValue?)
{
	if(value != null)
		this.set(name, value)
}
fun JsonObject.setNotNull(name: String, value: Boolean?)
{
	if(value != null)
		this.set(name, value)
}
fun JsonObject.setNotNull(name: String, value: Int?)
{
	if(value != null)
		this.set(name, value)
}
fun JsonObject.setNotNull(name: String, value: Long?)
{
	if(value != null)
		this.set(name, value)
}
fun JsonObject.setNotNull(name: String, value: Float?)
{
	if(value != null)
		this.set(name, value)
}
fun JsonObject.setNotNull(name: String, value: Double?)
{
	if(value != null)
		this.set(name, value)
}
fun JsonObject.setNotNull(name: String, value: String?)
{
	if(value != null)
		this.set(name, value)
}


fun JsonObject.getOrNull(name: String) =
	this.get(name)?.let({ if(it.isNull) null else it })


// Does not copy value parameter
fun JsonObject.get(name: String, value: JsonObject): JsonObject =
	this.getOrNull(name)?.asObject() ?: value.also({ this.set(name, it) })
// Does not copy value parameter
fun JsonObject.get(name: String, value: JsonArray): JsonArray =
	this.getOrNull(name)?.asArray() ?: value.also({ this.set(name, it) })
fun JsonObject.get(name: String, value: Boolean): Boolean =
	this.getOrNull(name)?.asBoolean() ?: value.also({ this.set(name, it) })
fun JsonObject.get(name: String, value: Int): Int =
	this.getOrNull(name)?.asInt() ?: value.also({ this.set(name, it) })
fun JsonObject.get(name: String, value: Long): Long =
	this.getOrNull(name)?.asLong() ?: value.also({ this.set(name, it) })
fun JsonObject.get(name: String, value: Float): Float =
	this.getOrNull(name)?.asFloat() ?: value.also({ this.set(name, it) })
fun JsonObject.get(name: String, value: Double): Double =
	this.getOrNull(name)?.asDouble() ?: value.also({ this.set(name, it) })
fun JsonObject.get(name: String, value: String): String =
	this.getOrNull(name)?.asString() ?: value.also({ this.set(name, it) })


fun JsonValue.get(path: List<String>): JsonValue? =
	path.fold(
		this as JsonValue?,
		{ acc, it -> acc?.asObject()?.getOrNull(it) })

fun JsonValue.set(path: List<String>, value: JsonValue?)
{
	path.subList(0, path.size - 1)
		.fold(this.asObject(), { acc, it ->
			(acc.getOrNull(it)
				?: JsonObject().also({ empty -> acc.set(it, empty) })
				).asObject()
		})
		.set(path.last(), value)
}
fun JsonValue.set(path: List<String>, value: Boolean)
{
	path.subList(0, path.size - 1)
		.fold(this.asObject(), { acc, it ->
			(acc.getOrNull(it)
				?: JsonObject().also({ empty -> acc.set(it, empty) })
				).asObject()
		})
		.set(path.last(), value)
}
fun JsonValue.set(path: List<String>, value: Int)
{
	path.subList(0, path.size - 1)
		.fold(this.asObject(), { acc, it ->
			(acc.getOrNull(it)
				?: JsonObject().also({ empty -> acc.set(it, empty) })
				).asObject()
		})
		.set(path.last(), value)
}
fun JsonValue.set(path: List<String>, value: Long)
{
	path.subList(0, path.size - 1)
		.fold(this.asObject(), { acc, it ->
			(acc.getOrNull(it)
				?: JsonObject().also({ empty -> acc.set(it, empty) })
				).asObject()
		})
		.set(path.last(), value)
}
fun JsonValue.set(path: List<String>, value: Float)
{
	path.subList(0, path.size - 1)
		.fold(this.asObject(), { acc, it ->
			(acc.getOrNull(it)
				?: JsonObject().also({ empty -> acc.set(it, empty) })
				).asObject()
		})
		.set(path.last(), value)
}
fun JsonValue.set(path: List<String>, value: Double)
{
	path.subList(0, path.size - 1)
		.fold(this.asObject(), { acc, it ->
			(acc.getOrNull(it)
				?: JsonObject().also({ empty -> acc.set(it, empty) })
				).asObject()
		})
		.set(path.last(), value)
}
fun JsonValue.set(path: List<String>, value: String?)
{
	path.subList(0, path.size - 1)
		.fold(this.asObject(), { acc, it ->
			(acc.getOrNull(it)
				?: JsonObject().also({ empty -> acc.set(it, empty) })
				).asObject()
		})
		.set(path.last(), value)
}
