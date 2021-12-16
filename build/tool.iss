#define ExeName "tools.bat"
#define Name "Tools"
#define Publisher "SIIL, Inc."
#define URL "http://www.siil.mx/"

#define phase "r"
                                    
#define major "29"
#define minor "5"
#define patch "3"
#define build "0" 

#if phase == "a"
  #define dirPhase = "alpha"
#elif  phase == "br"
  #define dirPhase = "beta"
#elif  phase == "r"
  #define dirPhase = "release"
#endif

[Setup]
#if phase == "a"
AppId={{FA08A7BA-737D-470C-8717-460DF33B7848}}
DefaultDirName={pf}\SIIL2\{#dirPhase}
DefaultGroupName=SIIL2\{#dirPhase}
#elif  phase == "br"
AppId={{155F5FF6-2458-4121-BEB9-7A768E211436}}
DefaultDirName={pf}\SIIL2\{#dirPhase}
DefaultGroupName=SIIL2\{#dirPhase}
#elif  phase == "r"
AppId={{F2DD93F2-6E58-4494-AD89-4778E5658342}}
DefaultDirName={pf}\SIIL2
DefaultGroupName=SIIL2
#endif
AppName={#Name}
AppVersion={#major}.{#minor}.{#patch}.{#build}-{#phase}
AppPublisher={#Publisher}
AppPublisherURL={#URL}
AppSupportURL={#URL}
AppUpdatesURL={#URL}
OutputDir=vers
OutputBaseFilename=tool-{#major}.{#minor}.{#patch}.{#build}-{#phase}
Compression=lzma
SolidCompression=yes
UninstallDisplayName=SIIL Tool


[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked; OnlyBelowVersion: 0,6.1

[Files]        
Source: "src\{#dirPhase}\deploy.xml"; DestDir: "{app}"; 
Source: "src\{#dirPhase}\config.jaas"; DestDir: "{app}";
Source: "src\{#dirPhase}\server.xml"; DestDir: "{app}"; 
Source: "src\{#dirPhase}\server.xsd"; DestDir: "{app}";    
Source: "src\{#dirPhase}\tools.bat"; DestDir: "{app}";
Source: "src\{#dirPhase}\desk.jar"; DestDir: "{app}";

[Icons]
Name: "{group}\{cm:UninstallProgram,{#Name}}"; Filename: "{#dirPhase}\{uninstallexe}"  
Name: "{group}\{cm:ProgramOnTheWeb,{#Name}}"; Filename: "{#URL}"
Name: "{group}\{#Name} {#major}.{#minor}.{#patch}.{#build}.{#phase}"; Filename: "{app}\{#ExeName}"; Flags: createonlyiffileexists 
Name: "{commondesktop}\{#Name} {#major}{#minor}{#patch}{#build}{#phase}"; Filename: "{app}\{#ExeName}"; Tasks: desktopicon; Flags: createonlyiffileexists
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\{#Name} {#major}{#minor}{#patch}{#build}({#phase})"; Filename: "{app}\{#ExeName}"; Tasks: quicklaunchicon ; Flags: createonlyiffileexists
