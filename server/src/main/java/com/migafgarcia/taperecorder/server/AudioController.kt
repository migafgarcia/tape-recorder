package com.migafgarcia.taperecorder.server

import com.migafgarcia.taperecorder.server.model.AudioResponse
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@RestController
class AudioController {

    @RequestMapping("/audio", method = [RequestMethod.GET])
    fun audio(): AudioResponse {
        // spawn server
        return AudioResponse("localhost", 65535, "asd")
    }
}