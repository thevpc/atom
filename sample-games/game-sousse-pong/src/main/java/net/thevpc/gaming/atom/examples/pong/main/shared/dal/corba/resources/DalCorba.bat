pwd
JDK_HOME="C:\Program Files\Java\JDK7.0"
P_SRC="..\..\..\..\..\..\..\..\..\"
P_PKG="net.thevpc.gaming.atom.examples.pong.main.shared.dal.corba.generated"

%JDK_HOME%\bin\idlj.exe -td %P_SRC% -fall -pkgTranslate "ConnectorCorba" %P_PKG% DalCorba.idl
