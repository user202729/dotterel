// This file is part of Dotterel which is released under GPL-2.0-or-later.
// See file <LICENSE.txt> or go to <http://www.gnu.org/licenses/> for full license details.

package nimble.dotterel.translation

data class System(
	val keyLayout: KeyLayout,
	val orthography: Orthography,
	val transforms: Map<
		String,
		(FormattedText, UnformattedText, Boolean) -> UnformattedText>,
	val commands: Map<String, (Translator, String) -> TranslationPart>,
	val aliases: Map<String, String>,
	val prefixStrokes: List<String>,
	val suffixStrokes: List<String>,
	val defaultDictionaries: List<String>,
	val defaultFormatting: Formatting
)
