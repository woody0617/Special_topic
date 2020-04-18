@echo off
title Test
echo.
echo.
echo.

cd c:\\docker

cd c:\\xampp\htdocs\docker\first_dog_or_cat

python -m scripts.label_image --graph=tf_files/retrained_graph.pb  --image=tf_files/%1


