package de.rholambdapi.o2a.intellij.lang.oberon2

data class OberonASymFile(
    val moduleImports: List<ModuleImportElement>,
    val constants: List<ConstantElement>,
    val parameterLists: List<ParameterListElement>,
    val libCalls: List<LibCallElement>,
    val moduleVariables: List<ModuleVariableElement>
) {
}
