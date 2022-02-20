!include "MUI.nsh"

!define NAME "SIIL"
!define JAR "desk.jar"
!define VERSION "29.13.2"
!define PUBLISHER "Azael Reyes"
!define WEBSITE "http://siil.com/"
!define JRE_URL "http://siil.com"

!include "FileFunc.nsh"
!insertmacro GetFileVersion
!insertmacro GetParameters
!include "WordFunc.nsh"
!insertmacro VersionCompare

Name "${NAME}"
OutFile "vers\Installer-${NAME}-${VERSION}-develop.exe"
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


!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_LANGUAGE "Spanish"

LangString JavaInstall ${LANG_ENGLISH} "${NAME} needs the Java Runtime Environment version ${JRE_VERSION} or newer but it is not installed on your system. Do you want to automatically download and install it? Press 'No' if you want to install Java manually later."
LangString JavaInstall ${LANG_SPANISH} "${NAME} necesita Java Runtime Environment version ${JRE_VERSION} o una mas reciente pero no esta instlada en su istema. Quiere descargar automaticamente? Presione 'No' si desea instalar Java manualmente"

LangString Uninstall ${LANG_ENGLISH} "Uninstall"
LangString Uninstall ${LANG_SPANISH} "Desintaldor"


Section
  SetOutPath $INSTDIR\
  
  File src\alpha\desk.jar
  File src\alpha\config.jaas
  File src\alpha\deploy.xml
  File src\alpha\server.xml
  File src\alpha\server.xsd
  File src\alpha\tool.ico
  
  WriteUninstaller "$INSTDIR\Uninstall.exe"
  
  CreateDirectory "$SMPROGRAMS\${NAME}"
  CreateShortCut "$SMPROGRAMS\${NAME}\$(Uninstall).lnk" "$INSTDIR\Uninstall.exe" 
  CreateShortCut "$SMPROGRAMS\${NAME}\Tools.lnk" "javaw.exe" " -jar ${JAR} tool.ico"

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

SectionEnd