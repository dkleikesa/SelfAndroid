@echo off
set ADB_ROOT=D:\android-sdk-windows\platform-tools
:menu
	echo 1: logcat pull
	echo 2: reinstall flp service
	echo 3: ant build
	echo 4: decompile android classes.dex
	echo 5: pull adb logs to d:\debug\
	echo 6: remove android_logs and history_log
	echo 7: pull HistoryTrack log to desktop
	echo 8: delete vivo logs
	echo 9: pull vivo logs to d:\debug\
	echo r: adb reboot the device
	echo.
	::echo exit: To exit 

:select
	::echo.
	set /p var=Please select:
	::echo %var%
	if %var%==1    (goto logcat_pull)
	if %var%==11   (goto logcat_pull)
	if %var%==2    (goto rm_Flp_manger)
	if %var%==22   (goto rm_Flp_manger)
	if %var%==222   (goto rm_Flp_manger)
	if %var%==2222   (goto rm_Flp_manger)
	if %var%==3    (goto ant_build)
	if %var%==4    (goto decompile_classes)
	if %var%==5    (goto pull_adb_logs)
	if %var%==6    (goto remove_all_log)
	if %var%==7    (goto pull_historytrack_log)
	if %var%==8    (goto delete_vivo_log)
	if %var%==9    (goto pull_vivo_log)
	if %var%==r    (goto adb_reboot)
	if %var%==R    (goto adb_reboot)
	if %var%==q    (goto exit) 
	if %var%==quit (goto exit) else ( goto error)
	
:logcat_pull
	start D:\TextAnalysisTool\TextAnalysisTool.NET  D:\debug\logcat.txt /Filters:"C:\Users\jianzhang.ljz\Documents\1111.tat"
	if %var%==1 (start C:\Users\jianzhang.ljz\Desktop\cmd\logcat.bat)
	::start  %ADB_ROOT%\adb logcat -c && %ADB_ROOT%\adb logcat -v threadtime > D:\debug\logcat.txt
	goto success
	
:rm_Flp_manger
	%ADB_ROOT%\adb uninstall com.amap.android.ams
	%ADB_ROOT%\adb remount
	%ADB_ROOT%\adb shell rm -rf /data/data/com.amap.android.ams/
	%ADB_ROOT%\adb shell rm  /system/priv-app/AutoNaviFLP/*
	if %var%==2 (set apk_dir=D:\WorkSpace\src\flp_src\AutonaviFusedProvider\bin\AutonaviFlpHardware.apk)
	if %var%==22 (set apk_dir=D:\WorkSpace\src\flp_src\AutonaviFusedProvider\bin\AutoNaviFLP-release.apk)
	if %var%==222 (set apk_dir=D:\WorkSpace\src\flp_src\AutonaviFusedProvider\bin\AutoNaviFLP.apk)
	if %var%==2222 (set apk_dir=D:\WorkSpace\src\flp_src\AutonaviFusedProvider\bin\AutoNaviFLP-release.apk)
	%ADB_ROOT%\adb push  %apk_dir%  /system/priv-app/AutoNaviFLP/
	%ADB_ROOT%\adb reboot
	goto success
	
:ant_build
	set /p c_path=Please input project path:
	cd \
	if  "%c_path%"=="" (set c_path=D:\WorkSpace\src\flp_src\AutonaviFusedProvider)
	echo %c_path%
	cd /D %c_path%
	start ant debug
	goto success

:decompile_classes
	set /p class_path=Please input project path:
	if  "%class_path%"=="" (echo path error 
	goto success)
	start D:\tools\dex2jar-2.0\d2j-dex2jar.bat --force %class_path% -o D:\tools\1.jar
	start D:\tools\jd-gui.exe D:\tools\1.jar
	echo gogo
	goto success
	
:pull_adb_logs
	rd /s /Q d:\debug\
	md d:\debug\
	%ADB_ROOT%\adb shell "cat /sys/devices/platform/huawei_sensorhub_logbuff/logbuff_flush"
	%ADB_ROOT%\adb shell sleep 1
	%ADB_ROOT%\adb shell shex -p
	%ADB_ROOT%\adb pull /data/log/android_logs d:\debug
::	%ADB_ROOT%\adb pull /data/hisi_logs d:\debug
	%ADB_ROOT%\adb pull /sdcard/Android/data/com.amap.android.ams   d:\debug
	%ADB_ROOT%\adb pull /sdcard/Pictures/Screenshots d:\debug
::	%ADB_ROOT%\adb pull /data/hwzd_logs/gps_log d:\debug\gps_log
	goto success

:remove_all_log
	%ADB_ROOT%\adb shell rm -rf /sdcard/Android/data/com.amap.android.ams/*
	%ADB_ROOT%\adb shell rm -rf /data/log/*
	%ADB_ROOT%\adb shell rm -rf /sdcard/huawei/healthcloud/log/com.huawei.health/*
	%ADB_ROOT%\adb shell rm -rf /data/hwzd_logs/*
	goto success
 
 :delete_vivo_log
	%ADB_ROOT%\adb shell rm -rf /sdcard/Android/data/com.amap.android.ams/*
	%ADB_ROOT%\adb shell rm -rf /sdcard/bbk_log/
	%ADB_ROOT%\adb shell rm -rf /sdcard/huawei/healthcloud/log/com.huawei.health/*
	%ADB_ROOT%\adb shell rm -rf /sdcard/bbklog/
	%ADB_ROOT%\adb shell rm -rf /sdcard/½ØÆÁ
	goto success
	
:pull_vivo_log
	rd /s /Q d:\debug\
	md d:\debug\
	%ADB_ROOT%\adb pull /sdcard/bbk_log d:\debug
	%ADB_ROOT%\adb pull /sdcard/bbklog d:\debug
	%ADB_ROOT%\adb pull /sdcard/½ØÆÁ  d:\debug
	%ADB_ROOT%\adb pull /sdcard/Android/data/com.amap.android.ams/files   d:\debug
	goto success
 
:pull_historytrack_log
	%ADB_ROOT%\adb pull /sdcard/Android/data/com.amap.android.ams/files   C:\Users\jianzhang.ljz\Desktop\historylog
	goto success
 
:adb_reboot
	echo adb_reboot
	%ADB_ROOT%\adb reboot
	goto success


	
:error
	echo select error!
	echo.
	goto menu

:success
   set var=0
	echo success!!!
	echo.
	goto select

:exit 
	exit
