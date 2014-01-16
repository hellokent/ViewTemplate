TEMPLATE = app
CONFIG += console
CONFIG -= app_bundle
CONFIG -= qt

SOURCES += \
    lua/lzio.c \
    lua/lvm.c \
    lua/lundump.c \
    lua/ltm.c \
    lua/ltablib.c \
    lua/ltable.c \
    lua/lstrlib.c \
    lua/lstring.c \
    lua/lstate.c \
    lua/lparser.c \
    lua/loslib.c \
    lua/lopcodes.c \
    lua/lobject.c \
    lua/loadlib.c \
    lua/lmem.c \
    lua/lmathlib.c \
    lua/llex.c \
    lua/liolib.c \
    lua/linit.c \
    lua/lgc.c \
    lua/lfunc.c \
    lua/ldump.c \
    lua/ldo.c \
    lua/ldebug.c \
    lua/ldblib.c \
    lua/lcode.c \
    lua/lbaselib.c \
    lua/lauxlib.c \
    lua/lapi.c \
    luajava/luajava.c

HEADERS += \
    lua/lzio.h \
    lua/lvm.h \
    lua/lundump.h \
    lua/lualib.h \
    lua/luaconf.h \
    lua/lua.h \
    lua/ltm.h \
    lua/ltable.h \
    lua/lstring.h \
    lua/lstate.h \
    lua/lparser.h \
    lua/lopcodes.h \
    lua/lobject.h \
    lua/lmem.h \
    lua/llimits.h \
    lua/llex.h \
    lua/lgc.h \
    lua/lfunc.h \
    lua/ldo.h \
    lua/ldebug.h \
    lua/lcode.h \
    lua/lauxlib.h \
    lua/lapi.h

