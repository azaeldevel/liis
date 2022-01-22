!include "MUI.nsh"

!define NAME "SIIL"
!define JAR "desk.jar"
!define VERSION "29.10.0"
!define PUBLISHER "Azael Reyes"
!define WEBSITE "http://siil.com/"
!define JRE_VERSION "17"
!define JRE_URL "http://siil.com"

!include "FileFunc.nsh"
!insertmacro GetFileVersion
!insertmacro GetParameters
!include "WordFunc.nsh"
!insertmacro VersionCompare

Name "${NAME}"
OutFile "vers\Installer-${NAME}-${VERSION}.exe"
RequestExecutionLevel admin
;XPStyle on
SetCompressor bzip2
InstallDir $PROGRAMFILES64\${NAME}

!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_UNPAGE_FINISH

;UninstPage uninstConfirm
;UninstPage instfiles

!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_LANGUAGE "Spanish"

LangString JavaInstall ${LANG_ENGLISH} "${NAME} needs the Java Runtime Environment version ${JRE_VERSION} or newer but it is not installed on your system. Do you want to automatically download and install it? Press 'No' if you want to install Java manually later."
LangString JavaInstall ${LANG_SPANISH} "${NAME} necesita Java Runtime Environment version ${JRE_VERSION} o una mas reciente pero no esta instlada en su istema. Quiere descargar automaticamente? Presione 'No' si desea instalar Java manualmente"

LangString Uninstall ${LANG_ENGLISH} "Uninstall"
LangString Uninstall ${LANG_SPANISH} "Desintaldor"

Function GetJRE
  MessageBox MB_YESNO|MB_ICONQUESTION $(JavaInstall) IDNO done
  StrCpy $2 "$TEMP\Java Runtime Environment.exe"
  nsisdl::download /TIMEOUT=30000 ${JRE_URL} $2 
  Pop $R0
  StrCmp $R0 "success" +3
  MessageBox MB_OK "Error: $R0"
  Quit
  ExecWait "$2"
  Delete "$2"
  done:
FunctionEnd

Function DetectJRE
  ${GetFileVersion} "$SYSDIR\javaw.exe" $R1
  ${VersionCompare} ${JRE_VERSION} $R1 $R2
  StrCmp $R2 0 done
  StrCmp $R2 2 done
  Call GetJRE
  done:
FunctionEnd

Section
  SetOutPath $INSTDIR\
  
;  Call DetectJRE
  
  File src\release\desk.jar
  File src\release\config.jaas
  File src\release\deploy.xml
  File src\release\server.xml
  File src\release\server.xsd
  File src\release\tool.ico
  
  WriteUninstaller "$INSTDIR\Uninstall.exe"
  
  CreateDirectory "$SMPROGRAMS\${NAME}"
  CreateShortCut "$SMPROGRAMS\${NAME}\$(Uninstall).lnk" "$INSTDIR\Uninstall.exe" 
  CreateShortCut "$SMPROGRAMS\${NAME}\Tools.lnk" "javaw.exe" " -jar ${JAR} tool.ico"

  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "DisplayName" "${NAME}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "DisplayVersion" "${VERSION}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "DisplayIcon" "$INSTDIR\${NAME}.ico"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "UninstallString" "$INSTDIR\Uninstall.exe"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "Publisher" "${PUBLISHER}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "HelpLink" "${WEBSITE}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "URLInfoAbout" "${WEBSITE}"
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "NoRepair" 1
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "InstallLocation" "$INSTDIR"
  ${GetSize} "$INSTDIR" "/S=0K" $0 $1 $2
  IntFmt $0 "0x%08X" $0
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}" "EstimatedSize" $0
SectionEnd

Section "Uninstall"
  
  Delete "$INSTDIR\desk.jar"
  Delete "$INSTDIR\config.jaas"
  Delete "$INSTDIR\deploy.xml"
  Delete "$INSTDIR\server.xml"
  Delete "$INSTDIR\server.xsd"
  Delete "$INSTDIR\tool.ico"
  Delete "$INSTDIR\Uninstall.exe"
  RMDir "$INSTDIR"
  Delete "$SMPROGRAMS\${NAME}\$(Uninstall).lnk"
  Delete "$SMPROGRAMS\${NAME}\Tools.lnk"
  RMDir "$SMPROGRAMS\${NAME}"
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${NAME}"
SectionEnd