JDK_HOME="/usr/java/latest"
P_SRC="../../../../../../../../../"
P_PKG="tn.edu.eniso.pong.main.shared.dal.corba.generated"

${JDK_HOME}/bin/idlj -td ${P_SRC} -fall -pkgTranslate "ConnectorCorba" ${P_PKG} DalCorba.idl
