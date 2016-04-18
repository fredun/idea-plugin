package com.fredun.idea.psi

import com.fredun.idea.FredunFileType
import com.fredun.idea.FredunIcons
import com.fredun.idea.FredunLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiNamedElement
import org.antlr.jetbrains.adapter.psi.ScopeNode

class FredunPSIFileRoot(fileViewProvider: FileViewProvider) : PsiFileBase(fileViewProvider, FredunLanguage), ScopeNode {
	override fun getFileType() = FredunFileType

	/**
	 * Return null since a file scope has no enclosing scope. It is
	 * not itself in a scope.
	 */
	override fun getContext() = null

	override fun resolve(element: PsiNamedElement?) = null

	override fun getIcon(flags: Int) = FredunIcons.FILE

	override fun toString() = "Fredun File"
}