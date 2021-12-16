#define ServerName "SIIL Server" 
#define AppPublisher "SIIL, Inc."
#define AppURL "http://www.siil.mx/"

#define phase "r"

#if phase == "a"
  #define dirPhase = "alpha"
#elif  phase == "br"
  #define dirPhase = "beta"
#elif  phase == "r"
  #define dirPhase = "release"
#endif
#define ServerVersion "2.13"   

[Setup]
#if phase == "a"
AppId={{496A85A5-7700-4E28-84D6-5A4962FB63C5}}
DefaultDirName={pf}\SIIL\{#dirPhase} 
DefaultGroupName=SIIL\{#dirPhase}
#elif  phase == "br"
AppId={{7342DB6B-2A61-48B7-AB78-3634146BBAFD}}
DefaultDirName={pf}\SIIL\{#dirPhase}
DefaultGroupName=SIIL\{#dirPhase}
#elif  phase == "r"
AppId={{A56002C2-8D37-42A7-B873-FCA78B09B6CD}}
DefaultDirName={pf}\SIIL
DefaultGroupName=SIIL
#endif
AppName={#ServerName}
AppVersion={#ServerVersion}{#phase}
AppPublisher={#AppPublisher}
AppPublisherURL={#AppURL}
AppSupportURL={#AppURL}
AppUpdatesURL={#AppURL}
OutputDir=vers
OutputBaseFilename=server-v{#ServerVersion}{#phase}
Compression=lzma
SolidCompression=yes
UninstallDisplayName=SIIL Server

[Files]
Source: "src\service.jar"; DestDir: "{app}"  
Source: "src\installservice.bat"; DestDir: "{app}"
Source: "src\uninstallservice.bat"; DestDir: "{app}"
Source: "src\prunsrv.exe"; DestDir: "{app}"
Source: "src\{#dirPhase}\server.xml"; DestDir: "{app}"
Source: "src\{#dirPhase}\server.xsd"; DestDir: "{app}"

[Run]
Filename: "{app}\installservice.bat"; Parameters: "{#phase}"; StatusMsg: "Installing Windows Services {#phase}..."


[UninstallRun]
Filename: "{app}\uninstallservice.bat"; Parameters: "{#phase}"; StatusMsg: "Uninstalling Windows Services {#phase}..."
