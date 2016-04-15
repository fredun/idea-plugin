package com.fredun.idea

import com.intellij.openapi.fileTypes.LanguageFileType

object FredunFileType : LanguageFileType(FredunLanguage) {
	override fun getDefaultExtension() = "fn"

	override fun getDescription() = "Fredun language file"

	override fun getIcon() = FredunIcons.FILE

	override fun getName() = "Fredun file"
}