// This file is part of Dotterel which is released under GPL-2.0-or-later.
// See file <LICENSE.txt> or go to <http://www.gnu.org/licenses/> for full license details.

package nimble.dotterel

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log

import java.io.*

import nimble.dotterel.translation.*
import nimble.dotterel.translation.dictionaries.NumbersDictionary
import nimble.dotterel.translation.dictionaries.SerialEncodingDictionary
import nimble.dotterel.util.CaseInsensitiveString

private val CODE_DICTIONARIES = mapOf(
	Pair("SerialEncoding", { keyLayout: KeyLayout ->
		SerialEncodingDictionary(keyLayout)
    }),
	Pair("Numbers", { keyLayout: KeyLayout ->
		NumbersDictionary(
			keyLayout,
			reverseKeys = Stroke(keyLayout, 0L),
			doubleKeys = Stroke(keyLayout, 0L),
			hundredKeys = Stroke(keyLayout, 0L))
	}),
	Pair("AdvancedNumbers", { keyLayout: KeyLayout -> NumbersDictionary(keyLayout) }),
	Pair("AdvancedNumbers(rE)", { keyLayout: KeyLayout ->
		NumbersDictionary(
			keyLayout,
			reverseKeys = keyLayout.parse("-E"))
	}),
	Pair("AdvancedNumbers(rU)", { keyLayout: KeyLayout ->
		NumbersDictionary(
			keyLayout,
			reverseKeys = keyLayout.parse("-U"))
	})
)

private val ANDROID_COMMANDS = mapOf(
	Pair("IME:EDITOR_ACTION", runnableCommand(::editorAction)),
	Pair("IME:SWITCH_PREVIOUS", runnableCommand(::switchPreviousIme)),
	Pair("IME:SWITCH_NEXT", runnableCommand(::switchNextIme)),
	Pair("IME:SWITCH", runnableCommand(::switchIme)),
	Pair("IME:SHOW_PICKER", runnableCommand(::showImePicker)),
	Pair("PLOVER:LOOKUP", runnableCommand(::lookupTranslation)),
	Pair("PLOVER:ADD_TRANSLATION", runnableCommand(::addTranslation))
).mapKeys({ CaseInsensitiveString(it.key) })

class AndroidSystemResources(private val context: Context) : SystemResources
{
	override val transforms = TRANSFORMS
	override val commands = COMMANDS + ANDROID_COMMANDS
	override val codeDictionaries = CODE_DICTIONARIES

	override fun openInputStream(path: String): InputStream?
	{
		try
		{
			val type = path.substringBefore(":")
			return when(type)
			{
				"asset" -> this.context.assets.open(path.drop("asset:/".length))
				"content" -> this.context
					.contentResolver
					.openInputStream(Uri.parse(path))
				"file" -> File(path.drop("file://".length)).inputStream()
				else -> File(path).inputStream()
			}
		}
		catch(e: FileNotFoundException)
		{
			Log.e("Dotterel", "File $path not found")
		}
		catch(e: IOException)
		{
			Log.e("Dotterel", "IO error reading $path: ${e.message}")
		}
		catch(e: SecurityException)
		{
			Log.i("Dotterel", "Permission error reading $path: ${e.message}")
		}

		return null
	}

	override fun openOutputStream(path: String): OutputStream?
	{
		try
		{
			val type = path.substringBefore(":")
			return when(type)
			{
				"asset" -> throw IOException("$path is read only")
				"content" -> this.context
					.contentResolver
					.openOutputStream(Uri.parse(path))
				"file" -> File(path.drop("file://".length)).outputStream()
				else -> File(path).outputStream()
			}
		}
		catch(e: FileNotFoundException)
		{
			Log.e("Dotterel", "File $path not found")
		}
		catch(e: IOException)
		{
			Log.e("Dotterel", "IO error writing $path: ${e.message}")
		}
		catch(e: SecurityException)
		{
			Log.e("Dotterel", "Permission error writing $path: ${e.message}")
		}

		return null
	}

	override fun isReadOnly(path: String): Boolean
	{
		val type = path.substringBefore(":")
		return when(type)
		{
			"asset" -> true
			"content" ->
			{
				val uri = Uri.parse(path)
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
				{
					!context.contentResolver.persistedUriPermissions
						.filter({ it.isWritePermission })
						.any({ it.uri == uri })
				}
				else
				{
					(this.context.checkCallingUriPermission(
						Uri.parse(path),
						Intent.FLAG_GRANT_WRITE_URI_PERMISSION
					)
						!= PackageManager.PERMISSION_GRANTED)
				}
			}
			"file" -> false
			else -> true
		}
	}
}
