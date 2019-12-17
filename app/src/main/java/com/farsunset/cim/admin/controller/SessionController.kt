package com.farsunset.cim.admin.controller

import com.farsunset.cim.service.IMSessionService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.annotation.Resource

@Controller
@RequestMapping("/console/session")
class SessionController {

    @Resource
    private val imSessionService: IMSessionService? = null

    @RequestMapping(value = ["/list"])
    fun list(model: Model): String {
        model.addAttribute("sessionList", imSessionService!!.list())
        return "console/session/manage"
    }
}