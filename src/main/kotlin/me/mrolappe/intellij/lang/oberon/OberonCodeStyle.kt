package me.mrolappe.intellij.lang.oberon

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.*

class OberonCodeStyleSettings(settings: CodeStyleSettings) :
    CustomCodeStyleSettings("OberonCodeStyleSettings", settings) {
}

//class OberonCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
//    override fun createCustomSettings(settings: CodeStyleSettings?): CustomCodeStyleSettings =
//        OberonCodeStyleSettings(settings!!)
//
//    override fun createConfigurable(
//        settings: CodeStyleSettings,
//        modelSettings: CodeStyleSettings
//    ): CodeStyleConfigurable {
//        return object : CodeStyleAbstractConfigurable(settings, modelSettings, this.configurableDisplayName) {
//            override fun createPanel(settings: CodeStyleSettings?): CodeStyleAbstractPanel {
//                return OberonCodeStyleMainPanel(currentSettings, settings)
//            }
//
//        }
//    }
//
//    override fun getConfigurableDisplayName(): String = "Oberon"
//
//}

//class OberonCodeStyleMainPanel(currentSettings: CodeStyleSettings?, settings: CodeStyleSettings?) :
//    TabbedLanguageCodeStylePanel(OberonLanguage.INSTANCE, currentSettings, settings)

class OberonLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage() = OberonLanguage.INSTANCE

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {
            SettingsType.SPACING_SETTINGS -> {
                consumer.showStandardOptions("SPACE_AROUND_ASSIGNMENT_OPERATORS")
                consumer.renameStandardOption("SPACE_AROUND_ASSIGNMENT_OPERATORS", "Separator")
            }

            SettingsType.BLANK_LINES_SETTINGS -> consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE")
            else -> {
                System.err.println("TODO OberonCodeStyle::customizeSettings")
            }
        }
    }

    override fun getCodeSample(settingsType: SettingsType): String {
        return """
            PROCEDURE WBGetFilename* (VAR filename: ARRAY OF CHAR; dir: Dos.FileLockPtr; name: Exec.LSTRPTR): BOOLEAN;

            VAR
              oldCurr, lock: Dos.FileLockPtr;

            BEGIN
              oldCurr:= Dos.CurrentDir(dir);
              lock:= Dos.Lock(name^, Dos.sharedLock);
              IF lock # NIL THEN
                IF ~Dos.NameFromLock(lock, filename, LEN(filename)) THEN
                  filename:= "";
                END;
                Dos.UnLock(lock);
              END;
              dir:= Dos.CurrentDir(oldCurr);
              RETURN filename # "";
            END WBGetFilename;
        """.trimIndent()
    }
}