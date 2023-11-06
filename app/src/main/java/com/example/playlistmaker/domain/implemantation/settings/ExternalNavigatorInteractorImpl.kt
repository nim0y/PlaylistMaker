package com.example.playlistmaker.domain.implemantation.settings

import com.example.playlistmaker.domain.api.settings.ExternalNavigatorInteractor
import com.example.playlistmaker.domain.api.settings.ExternalNavigatorRepository

class ExternalNavigatorInteractorImpl(private val externalNavigatorRepository: ExternalNavigatorRepository) :
    ExternalNavigatorInteractor {
    override fun termsOfUse() {
        externalNavigatorRepository.termsOfUse()
    }


    override fun share() {
        externalNavigatorRepository.share()
    }

    override fun support() {
        externalNavigatorRepository.support()
    }

}