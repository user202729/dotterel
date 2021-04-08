// This file is part of Dotterel which is released under GPL-2.0-or-later.
// See file <LICENSE.txt> or go to <http://www.gnu.org/licenses/> for full license details.

package nimble.dotterel.translation.dictionaries

import nimble.dotterel.translation.*

class SerialEncodingDictionary(
	override val keyLayout: KeyLayout,
	override val longestKey: Int = 1
) : Dictionary
{
	override fun get(k: List<Stroke>): String?
	{
		if(k.size != 1)
			return null

		val s = k[0]
		if(s.layout != this.keyLayout || s.isEmpty)
			return null

		val result = StringBuilder()
        for(x in (0 until this.keyLayout.pureKeysList.size).step(4)){
            result.append(('a'.toInt() + ((s.keys shr x) and 0xF)).toChar());
        }
        result.append('z')
		return "{&$result}"
	}
}
