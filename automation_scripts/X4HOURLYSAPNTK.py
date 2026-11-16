from psdi.iface.mic import PublishChannelCache
from psdi.server import MXServer

mxserver = MXServer.getMXServer()
user_info = mbo.getUserInfo()

PublishChannelCache.getInstance().getPublishChannel("X4NTK").publish(mbo, True);