package vip.qsos.im.lib.server.model

/**消息服务账号*/
interface IMAccount {
    /**ID*/
    var id: Int

    /**消息账号，固定长度9，由1-9组成的9位不重复字符串*/
    var account: String

    /**是否被注册*/
    var used: Boolean
}