package se.paradoxia.pxdemo.home.view

import se.paradoxia.pxdemo.BR
import se.paradoxia.pxdemo.databinding.CardAboutMeBinding
import se.paradoxia.pxdemo.databinding.CardProfileHeaderBinding
import se.paradoxia.pxdemo.util.AllOpen
import se.paradoxia.pxdemo.util.BindingTemplateViewHolder

/**
 * Created by mikael on 2018-01-29.
 */

@AllOpen
class AboutMeViewHolder(binding: CardAboutMeBinding) : BindingTemplateViewHolder(BR.cardAboutMe, binding)

@AllOpen
class ProfileHeaderViewHolder(binding: CardProfileHeaderBinding) :
    BindingTemplateViewHolder(BR.cardProfileHeader, binding)

