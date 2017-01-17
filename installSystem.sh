#!/bin/bash

app_package="io.github.otaupdater.otaupdater"
dir_app_name="OtaUpdater"
MAIN_ACTIVITY="MainActivity"
path_sysapp="/system/priv-app"
apk_host="./app/build/outputs/apk/app-debug.apk"
apk_name=$dir_app_name".apk"
apk_target_dir="$path_sysapp/$dir_app_name"
apk_target_sys="$apk_target_dir/$apk_name"

# Delete previous APK
#rm -f $apk_host

adb shell mount -o rw,remount,rw /system 
adb push $apk_host $apk_target_sys

# Give permissions
adb shell "chmod 755 $apk_target_dir"
adb shell "chmod 644 $apk_target_sys"

#Unmount system
adb shell "mount -o remount,ro /"

# Stop the app
adb shell "am force-stop $app_package"

# Re execute the app
adb shell "am start -n \"$app_package/$app_package.$MAIN_ACTIVITY\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"
