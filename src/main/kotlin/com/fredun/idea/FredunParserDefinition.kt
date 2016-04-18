package com.fredun.idea

import com.fredun.frontend.parser.FredunLexer
import com.fredun.frontend.parser.FredunParser
import com.fredun.idea.psi.FredunPSIFileRoot
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import org.antlr.jetbrains.adapter.lexer.ANTLRLexerAdaptor
import org.antlr.jetbrains.adapter.lexer.PSIElementTypeFactory
import org.antlr.jetbrains.adapter.lexer.RuleIElementType
import org.antlr.jetbrains.adapter.lexer.TokenIElementType
import org.antlr.jetbrains.adapter.parser.ANTLRParserAdaptor
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.tree.ParseTree

class FredunParserDefinition : ParserDefinition {
	companion object {
		val LOWERCASE_ID: TokenIElementType

		init {
			PSIElementTypeFactory.defineLanguageIElementTypes(FredunLanguage,
					FredunParser.tokenNames,
					FredunParser.ruleNames)
			val tokenIElementTypes = PSIElementTypeFactory.getTokenIElementTypes(FredunLanguage)
			LOWERCASE_ID = tokenIElementTypes[FredunLexer.LOWERCASE_ID]
		}

		val FILE = IFileElementType(FredunLanguage)

		val WHITESPACE = PSIElementTypeFactory.createTokenSet(FredunLanguage, FredunLexer.WS)
		val COMMENTS = PSIElementTypeFactory.createTokenSet(FredunLanguage,
				FredunLexer.BLOCK_COMMENT,
				FredunLexer.DOC_COMMENT,
				FredunLexer.LINE_COMMENT
		)

		val STRING = PSIElementTypeFactory.createTokenSet(FredunLanguage, FredunLexer.QUOTED_STRING)
	}

	override fun createElement(node: ASTNode): PsiElement {
		val elType = node.elementType;
		if (elType is TokenIElementType) {
			return ANTLRPsiNode(node);
		}
		if (elType !is RuleIElementType) {
			return ANTLRPsiNode(node);
		}
		val ruleElType = elType as RuleIElementType;
		when (ruleElType.ruleIndex) {
			else -> return ANTLRPsiNode(node);
		}
	}

	/** Create the root of your PSI tree (a PsiFile).

	 * From IntelliJ IDEA Architectural Overview:
	 * "A PSI (Program Structure Interface) file is the root of a structure
	 * representing the contents of a file as a hierarchy of elements
	 * in a particular programming language."

	 * PsiFile is to be distinguished from a FileASTNode, which is a parse
	 * tree node that eventually becomes a PsiFile. From PsiFile, we can get
	 * it back via: [PsiFile.getNode].
	 */
	override fun createFile(viewProvider: FileViewProvider) = FredunPSIFileRoot(viewProvider)

	override fun createLexer(project: Project?) = ANTLRLexerAdaptor(FredunLanguage, FredunLexer(null))

	override fun createParser(project: Project?): PsiParser? {
		val parser = FredunParser(null);
		return object : ANTLRParserAdaptor(FredunLanguage, parser) {
			override fun parse(parser: Parser?, root: IElementType?): ParseTree? {
				// start rule depends on root passed in; sometimes we want to create an ID node etc...
				//				if (root is IFileElementType) {
				return (parser as FredunParser).start();
				//				}
				// let's hope it's an ID as needed by "rename function"
				//				return (parser as FredunParser).name();
			}
		};
	}

	override fun getCommentTokens() = FredunParserDefinition.COMMENTS

	override fun getStringLiteralElements() = FredunParserDefinition.STRING

	override fun getWhitespaceTokens() = FredunParserDefinition.WHITESPACE

	override fun getFileNodeType() = FredunParserDefinition.FILE

	override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?) = ParserDefinition.SpaceRequirements.MAY
}