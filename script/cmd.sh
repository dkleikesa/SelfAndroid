#!/usr/bin/env bash

##############################################################################
##
##  Amap start up script for UN*X
##
##############################################################################

function cmd-branch() {
 git branch 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/\1/'
}

function cmd-clone(){

  # Gradle工程
  git clone git@gitlab.alibaba-inc.com:amap_android_rd/amap_android.git

  cd amap_android

  echo -e  "\n"

  # 主工程
  git clone git@gitlab.alibaba-inc.com:amap_android_rd/amap_module_main.git

  echo -e  "\n"

  # 出行
  git clone git@gitlab.alibaba-inc.com:amap_android_rd/amap_module_route.git

  echo -e  "\n"

  # 搜索模块
  git clone git@gitlab.alibaba-inc.com:amap_android_rd/amap_module_search.git

  echo -e  "\n"

  # 通用模块
  git clone git@gitlab.alibaba-inc.com:amap_android_rd/amap_module_common.git

  echo -e  "\n"

  #离线模块
  git clone git@gitlab.alibaba-inc.com:amap_android_rd/amap_module_offline.git

  echo -e  "\n"

  #用户模块
  git clone git@gitlab.alibaba-inc.com:amap_android_rd/amap_module_operation.git

  echo -e  "\n"

  #生活模块
  git clone git@gitlab.alibaba-inc.com:amap_android_rd/amap_module_life.git

  echo -e  "\n"
  
  ####
  git clone git@gitlab.alibaba-inc.com:amap_android_rd/amap_module_drive.git
  
  echo -e  "\n"
  
  ####
  git clone git@gitlab.alibaba-inc.com:amap_android_rd/amap_module_thirdparty.git
  
  echo -e  "\n"
}


function cmd-pull(){
  
  #cd amap_android_gradle
  # Gradle工程
  echo -e  "\n\n git pull amap_android_gradle\n"
  git pull origin `cmd-branch`:origin/`cmd-branch`

  # 主工程
  cd amap_module_main
  echo -e  "\n\n git pull amap_module_main\n"
  git pull origin `cmd-branch`:origin/`cmd-branch`

  # 出行
  cd ../amap_module_route
  echo -e  "\n\n git pull amap_module_route\n"
  git pull origin `cmd-branch`:origin/`cmd-branch`

  # 搜索模块
  cd ../amap_module_search
  echo -e  "\n\n git pull amap_module_search\n"
  git pull origin `cmd-branch`:origin/`cmd-branch`

  # 通用模块
  cd ../amap_module_common
  echo -e  "\n\n git pull amap_module_common\n"
  git pull origin `cmd-branch`:origin/`cmd-branch`

  #离线模块
  cd ../amap_module_offline
  echo -e  "\n\n git pull amap_module_offline\n"
  git pull origin `cmd-branch`:origin/`cmd-branch`

  #用户模块
  cd ../amap_module_operation
  echo -e  "\n\n git pull amap_module_operation\n"
  git pull origin `cmd-branch`:origin/`cmd-branch`

  #生活模块
  cd ../amap_module_life
  echo -e  "\n\n git pull amap_module_life\n"
  git pull origin `cmd-branch`:origin/`cmd-branch`
  
    #driver模块
  cd ../amap_module_drive
  echo -e  "\n\n git pull amap_module_drive\n"
  git pull origin `cmd-branch`:origin/`cmd-branch`
  
    ####
	cd ../amap_module_thirdparty
	echo -e  "\n\n git pull amap_module_thirdparty\n"
	git pull origin `cmd-branch`:origin/`cmd-branch`
}


function cmd-fetch(){
  # cd amap_android_gradle
  # Gradle工程
  echo -e  "\n\n git fetch amap_android_gradle\n"
  git fetch

  # 主工程
  cd amap_module_main
  echo -e  "\n\n git fetch amap_module_main\n"
  git fetch

  # 出行
  cd ../amap_module_route
  echo -e  "\n\n git fetch amap_module_route\n"
  git fetch

  # 搜索模块
  cd ../amap_module_search
  echo -e  "\n\n git fetch amap_module_search\n"
  git fetch

  # 通用模块
  cd ../amap_module_common
  echo -e  "\n\n git fetch amap_module_common\n"
  git fetch

  #离线模块
  cd ../amap_module_offline
  echo -e  "\n\n git fetch amap_module_offline\n"
  git fetch

  #用户模块
  cd ../amap_module_operation
  echo -e  "\n\n git fetch amap_module_operation\n"
  git fetch

  #生活模块
  cd ../amap_module_life
  echo -e  "\n\n git fetch amap_module_life\n"
  git fetch
  
      ####
	cd ../amap_module_thirdparty
	echo -e  "\n\n git pull amap_module_thirdparty\n"
	git fetch
}

function cmd-checkout(){
  echo -e  "\n\n git checkout" $2
  
  # Gradle工程
  echo -e  "\n\n do amap_android\n"
  git checkout $2
  git pull

  # 主工程
  cd amap_module_main
  echo -e  "\n\n do amap_module_main\n"
  git checkout $2
  git pull

  # 出行
  cd ../amap_module_route
  echo -e  "\n\n do amap_module_route\n"
  git checkout $2
  git pull

  # 搜索模块
  cd ../amap_module_search
  echo -e  "\n\n do amap_module_search\n"
  git checkout $2
  git pull

  # 通用模块
  cd ../amap_module_common
  echo -e  "\n\n do amap_module_common\n"
  git checkout $2
  git pull

  #离线模块
  cd ../amap_module_offline
  echo -e  "\n\n do amap_module_offline\n"
  git checkout $2
  git pull

  #用户模块
  cd ../amap_module_operation
  echo -e  "\n\n do amap_module_operation\n"
  git checkout $2
  git pull

  #生活模块
  cd ../amap_module_life
  echo -e  "\n\n do amap_module_life\n"
  git checkout $2
  git pull
  
  ####
  cd ../amap_module_thirdparty
  echo -e  "\n\n do amap_module_thirdparty\n"
  git checkout $2
  git pull
  
  ####
  cd ../amap_module_drive
  echo -e  "\n\n do amap_module_drive\n"
  git checkout $2
  git pull
}

function cmd-status(){
  echo -e  "\n\n git status" $2
  
  # Gradle工程
  echo -e  "\n\n do amap_android\n"
  git status

  # 主工程
  cd amap_module_main
  echo -e  "\n\n do amap_module_main\n"
  git status

  # 出行
  cd ../amap_module_route
  echo -e  "\n\n do amap_module_route\n"
  git status

  # 搜索模块
  cd ../amap_module_search
  echo -e  "\n\n do amap_module_search\n"
  git status

  # 通用模块
  cd ../amap_module_common
  echo -e  "\n\n do amap_module_common\n"
  git status

  #离线模块
  cd ../amap_module_offline
  echo -e  "\n\n do amap_module_offline\n"
  git status

  #用户模块
  cd ../amap_module_operation
  echo -e  "\n\n do amap_module_operation\n"
  git status

  #生活模块
  cd ../amap_module_life
  echo -e  "\n\n do amap_module_life\n"
  git status
  
  ####
  cd ../amap_module_thirdparty
  echo -e  "\n\n do amap_module_thirdparty\n"
  git status
  
  ####
  cd ../amap_module_drive
  echo -e  "\n\n do amap_module_drive\n"
  git status
}

function freeline-rebuild(){
	gradle checkBeforeCleanBuild
	python freeline/freeline.py -d -f
}
function freeline-build(){
	python freeline/freeline.py -d
}
function install(){
	adb install 
}
if [ $1 = "clone" ]; then
  cmd-clone
elif [ $1 = "pull" ]; then
  cmd-pull
elif [ $1 = "fetch" ]; then
  cmd-fetch
elif [ $1 = "checkout" ]; then
  cmd-checkout $1 $2
elif [ $1 = "rebuild" ]; then
	freeline-rebuild
elif [ $1 = "build" ]; then
    freeline-build
elif [ $1 = "status" ]; then
    cmd-status
else
  echo -e  "Command Unsupported."
  exit 2
fi


