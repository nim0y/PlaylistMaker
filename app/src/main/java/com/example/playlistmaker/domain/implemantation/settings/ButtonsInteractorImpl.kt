package com.example.playlistmaker.domain.implemantation.settings

import com.example.playlistmaker.domain.api.settings.ButtonsInteractor
import com.example.playlistmaker.domain.api.settings.ButtonsRepository

class ButtonsInteractorImpl(private val buttonsRepository: ButtonsRepository) : ButtonsInteractor {
    override fun termsOfUse() {
        buttonsRepository.termsOfUse()
    }


    override fun share() {
        buttonsRepository.share()
    }

    override fun support() {
        buttonsRepository.support()
    }

}