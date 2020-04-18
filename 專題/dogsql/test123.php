<?php

    $var ="corgi";
    $filename = "D15_20";
    $a = glob("C:/xampp/htdocs/docker/third_who_dog/".$var."/tf_files/train/".$filename."/*.*");
    print_r(count($a));
?>