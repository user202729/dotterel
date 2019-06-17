// This file is part of Dotterel which is released under GPL-2.0-or-later.
// See file <LICENSE.txt> or go to <http://www.gnu.org/licenses/> for full license details.

package nimble.dotterel.translation.dictionaries

import com.eclipsesource.json.Json

import java.io.InputStream

import nimble.dotterel.translation.KeyLayout
import nimble.dotterel.translation.FileParseException

class JsonDictionary(keyLayout: KeyLayout) :
	BackedDictionary(keyLayout, BackingDictionary())
{
	companion object
	{
		fun load(input: InputStream, keyLayout: KeyLayout): JsonDictionary
		{
			try
			{
				val dictionary = JsonDictionary(keyLayout)
				for(entry in Json.parse(input.bufferedReader()).asObject())
					dictionary[entry.name] = entry.value.asString()
				return dictionary
			}
			catch(e: com.eclipsesource.json.ParseException)
			{
				throw FileParseException("Invalid JSON", e)
			}
			catch(e: java.lang.NullPointerException)
			{
				throw FileParseException("Invalid type", e)
			}
			catch(e: java.lang.UnsupportedOperationException)
			{
				throw FileParseException("Missing type", e)
			}
		}
	}
}
