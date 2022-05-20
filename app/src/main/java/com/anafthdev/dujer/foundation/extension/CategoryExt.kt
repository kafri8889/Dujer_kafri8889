package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.db.model.Category

fun Category.isDefault(): Boolean = id == Category.default.id
