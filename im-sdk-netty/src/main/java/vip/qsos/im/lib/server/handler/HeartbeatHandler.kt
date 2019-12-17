package vip.qsos.im.lib.server.handler

import vip.qsos.im.lib.server.model.IMSession
import vip.qsos.im.lib.server.model.SendBody

/**
 * @author : 华清松
 * 心跳请求处理，主要是让 netty 重置 channel 的空闲时间
 */
class HeartbeatHandler : IMRequestHandler {
    override fun process(session: IMSession?, message: SendBody?) {}
}