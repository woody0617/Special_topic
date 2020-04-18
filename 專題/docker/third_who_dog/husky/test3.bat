@echo off
title Test
echo.
echo.
echo.

cd c:\\xampp\htdocs\docker\third_who_dog\husky
python -m scripts.label_image --graph=tf_files/retrained_graph.pb  --image=%1


