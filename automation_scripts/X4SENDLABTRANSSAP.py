from psdi.server import MXServer
from psdi.util import MXException
from psdi.iface.mic import PublishChannelCache


try:
    PublishChannelCache.getInstance().getPublishChannel("X4PBLABTRANSSAP").publish(mbo, True);
    server = MXServer.getMXServer()
    userinfo=server.getSystemUserInfo()
    
except MXException:
    print("Error in the script")