#define ExeName "mang.exe"
#define Name "Management"
#define Publisher "SIIL, Inc."
#define URL "http://www.siil.mx/"

#define phase "r"  


#define Version "3.3.4.2"
#if phase == "a"
  #define dirPhase = "alpha"
#elif  phase == "br"
  #define dirPhase = "beta"
#elif  phase == "r"
  #define dirPhase = "release"
#endif

[Setup]
#if phase == "a"
AppId={{4ABB6360-709E-4F34-9309-9332E89F08B5}}
DefaultDirName={pf}\SIIL\{#dirPhase}
DefaultGroupName=SIIL\{#dirPhase}
#elif  phase == "br"
AppId={{5E8899C0-8F08-40D8-8C1D-C89B29A0329F}}
DefaultDirName={pf}\SIIL\{#dirPhase}
DefaultGroupName=SIIL\{#dirPhase}
#elif  phase == "r"
AppId={{BC9B5236-F7E9-402C-ABF7-5B2A37B895A8}}
DefaultDirName={pf}\SIIL
DefaultGroupName=SIIL
#endif
AppName={#Name}
AppVersion={#Version}{#phase}
AppPublisher={#Publisher}
AppPublisherURL={#URL}
AppSupportURL={#URL}
AppUpdatesURL={#URL}
OutputDir=vers
OutputBaseFilename=mang-v{#Version}{#phase}
Compression=lzma
SolidCompression=yes
UninstallDisplayName=SIIL Management


[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked; OnlyBelowVersion: 0,6.1

[Files]        
Source: "src\{#dirPhase}\mang.exe"; DestDir: "{app}";  
Source: "src\{#dirPhase}\deploy.xml"; DestDir: "{app}"; 

[Icons]
Name: "{group}\{cm:UninstallProgram,{#Name}}"; Filename: "{#dirPhase}\{uninstallexe}"  
Name: "{group}\{cm:ProgramOnTheWeb,{#Name}}"; Filename: "{#URL}"
Name: "{group}\{#Name} {#Version}{#phase}"; Filename: "{app}\{#ExeName}"; Flags: createonlyiffileexists 
Name: "{commondesktop}\{#Name} {#Version}{#phase}"; Filename: "{app}\{#ExeName}"; Tasks: desktopicon; Flags: createonlyiffileexists
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\{#Name} {#Version}{#phase}"; Filename: "{app}\{#ExeName}"; Tasks: quicklaunchicon ; Flags: createonlyiffileexists
