#!/bin/sh
docker run -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix --env="NO_AT_BRIDGE=1" --device=/dev/dri:/dev/dri -e LIBGL_ALWAYS_SOFTWARE=1 app