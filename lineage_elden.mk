#
# Copyright (C) 2024 The Android Open Source Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Inherit from products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit_only.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

# Inherit some common Lineage stuff.
$(call inherit-product, vendor/lineage/config/common_full_tablet_wifionly.mk)

# Inherit from elden device.
$(call inherit-product, device/lenovo/elden/device.mk)

## Device identifier
PRODUCT_DEVICE := elden
PRODUCT_NAME := lineage_elden
PRODUCT_BRAND := lenovo
PRODUCT_MODEL := TB322FC
PRODUCT_MANUFACTURER := lenovo

PRODUCT_BUILD_PROP_OVERRIDES += \
    BuildDesc=$(call normalize-path-list, "Lenovo TB322FC_PRC TB322FC 15 AQ3A.250129.001 ZUXOS_1.1.11.073_250612_PRC user release-keys")

BUILD_FINGERPRINT := Lenovo/TB322FC_PRC/TB322FC:15/AQ3A.250129.001/ZUXOS_1.1.11.073_250612_PRC:user/release-keys

# GMS
PRODUCT_GMS_CLIENTID_BASE := android-lenovo