package vip.qsos.im.admin.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class NavigationController {

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun index(model: ModelAndView): ModelAndView {
        model.viewName = "console/index"
        return model
    }

    @RequestMapping(value = ["/webclient"], method = [RequestMethod.GET])
    fun webclient(model: ModelAndView): ModelAndView {
        model.viewName = "console/webclient/index"
        return model
    }
}