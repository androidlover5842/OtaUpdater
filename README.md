# OtaUpdater
Ota updater for unofficial roms 

Usage :-

#Device tree
Add this lines to your cm.mk or device.mk
```xml
## Replace link with your own 
PRODUCT_PROPERTY_OVERRIDES += ro.updater.uri=https://raw.githubusercontent.com/Grace5921/OtaUpdater/master/Updater.xml
## This is for old builds 
PRODUCT_PROPERTY_OVERRIDES += ro.updater.oldrelease.url=https://raw.githubusercontent.com/Grace5921/OtaUpdater/master/updater-old-release.json 
PRODUCT_PROPERTY_OVERRIDES += ro.rom.version=$(shell date +%Y%m%d)
## Set it true if you want toast like service started
PRODUCT_PROPERTY_OVERRIDES += ro.otaupdate.enable_toast=true
## Set it true if you want log of app in your logcat else false
PRODUCT_PROPERTY_OVERRIDES += ro.otaupdate.enable_log=true
```
# Setting latest release xml

check this example layout file

https://github.com/Grace5921/OtaUpdater/blob/master/Updater.xml

replace 20161230 with your version which is on build.prop (in your new build ro.rom.version)

### Setting url 
replace URL with your own but it should be direct url (like <a href="https://github.com/Arubadel/Arubadel/releases/download/untagged-71b60b7351492a2477d1/app-release.apk">this</a> )

### Setting Changelog

I don't think so i need to tell you about this .

# Setting up old builds release

checkout <a href="https://raw.githubusercontent.com/Grace5921/OtaUpdater/master/updater-old-release.json">this</a> json format 
### Stable or Beta ?
set 	
```xml
"stable" : "true"
"prerelease" : "false",
```
if your old release is stable 

else 
```xml
"prerelease" : "true",
"stable": "false"
```

### tag_name
```xml
"tag_name" : "Your build name like cm13-un----"
```

### name
```xml
"name" : "set name of file (include .zip in extention) this will file name to be downloaded on device"
```

### release_date
you know what to do here :D.

### browser_download_url
```xml
"browser_download_url" : "this should be direct link to your old release as we  did in xml before "
```
### body
```xml
"body" : "add your change log here and make sure you use this for new line "\n" "
```
# Screen shots

 ![Rom Update notification screen shot ][1]

 ![On click notification dialog][2]

 ![App Ui ][3]

 ![App ui dialog][4]
[1]: https://raw.githubusercontent.com/Grace5921/OtaUpdater/master/screenshots/Screenshot_20161221-160242.png
[2]: https://raw.githubusercontent.com/Grace5921/OtaUpdater/master/screenshots/Screenshot_20161221-160330.png
[3]: https://raw.githubusercontent.com/Grace5921/OtaUpdater/master/screenshots/Screenshot_20161221-160248.png
[4]: https://raw.githubusercontent.com/Grace5921/OtaUpdater/master/screenshots/Screenshot_20161221-160255.png
