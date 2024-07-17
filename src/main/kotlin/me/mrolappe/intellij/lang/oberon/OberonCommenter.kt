package me.mrolappe.intellij.lang.oberon

import com.intellij.lang.Commenter

class OberonCommenter : Commenter {
    override fun getLineCommentPrefix(): String? = null

    override fun getBlockCommentPrefix(): String = "(*"

    override fun getBlockCommentSuffix(): String = "*)"

    override fun getCommentedBlockCommentPrefix(): String? = null

    override fun getCommentedBlockCommentSuffix(): String? = null
}
